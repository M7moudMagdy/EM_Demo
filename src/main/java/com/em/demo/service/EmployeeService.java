package com.em.demo.service;

import com.em.demo.exception.EmployeeNotFoundException;
import com.em.demo.exception.InvalidByThirdPartyException;
import com.em.demo.exception.RateLimitExceededException;
import com.em.demo.model.Employee;
import com.em.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmailValidationService emailValidationService;
    private final DepartmentVerificationService departmentVerificationService;
    private final EmailNotificationService emailNotificationService;
    private final RateLimiterService rateLimiterService;
    private final AuditLoggingService auditLoggingService;

    public void tryConsume() {
        if (!rateLimiterService.tryConsume()) {
            throw new RateLimitExceededException(Utils.RATE_LIMIT_EXCEEDED);
        }
    }

    public Employee createEmployee(Employee employee) {
        log.info("Creating employee: {}", employee);
        auditLoggingService.logAction("START", "Employee", null, "Starting employee creation process");

        tryConsume();

        if (!emailValidationService.validateEmail(employee.getEmail())) {
            throw new InvalidByThirdPartyException(Utils.INVALID_EMAIL);
        }

        if (!departmentVerificationService.isDepartmentValid(employee.getDepartment())) {
            throw new InvalidByThirdPartyException(Utils.INVALID_DEPARTMENT);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());


        auditLoggingService.logAction(Utils.ACTION_SUCCESS, "Employee", savedEmployee.getId().toString(), "Employee created successfully");

        emailNotificationService.sendEmployeeCreationNotification(employee.getEmail(), employee.getFirstName() + " " + employee.getLastName());

        return savedEmployee;
    }

    public Employee getEmployeeById(String id) {
        log.info("Fetching employee with ID: {}", id);
        auditLoggingService.logAction(Utils.ACTION_START, "Employee", id, "Retrieve Employee");

        tryConsume();

        UUID uuid = UUID.fromString(id);


        Employee employee = employeeRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", id);
                    auditLoggingService.logAction(Utils.ACTION_FAILED, "Employee", id, "Employee not found");
                    throw new EmployeeNotFoundException("Employee not found with id: " + id);
                });

        auditLoggingService.logAction(Utils.ACTION_SUCCESS, "Employee", id, "Employee found");

        return employee;
    }

    public Employee updateEmployee(String id, Employee employeeDetails) {
        log.info("Updating employee with ID: {}", id);
        auditLoggingService.logAction(Utils.ACTION_START, "Employee", id, "Starting employee updating process");

        tryConsume();

        if (!emailValidationService.validateEmail(employeeDetails.getEmail())) {
            throw new IllegalArgumentException(Utils.INVALID_EMAIL);
        }

        if (!departmentVerificationService.isDepartmentValid(employeeDetails.getDepartment())) {
            throw new IllegalArgumentException(Utils.INVALID_DEPARTMENT);
        }

        Employee employee = getEmployeeById(id);
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setSalary(employeeDetails.getSalary());


        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully with ID: {}", updatedEmployee.getId());

        auditLoggingService.logAction(Utils.ACTION_SUCCESS, "Employee", id, "Employee updated");

        return updatedEmployee;
    }

    public void deleteEmployee(String id) {
        log.info("Deleting employee with ID: {}", id);
        auditLoggingService.logAction(Utils.ACTION_START, "Employee", id, "Starting employee deleting process");
        tryConsume();

        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);

        auditLoggingService.logAction(Utils.ACTION_SUCCESS, "Employee", id, "Employee deleted");

        log.info("Employee deleted successfully with ID: {}", id);
    }

    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        auditLoggingService.logAction(Utils.ACTION_START, "Employee", null, "retrieve Employees list");

        tryConsume();

        List<Employee> employees = employeeRepository.findAll();
        log.info("Total employees found: {}", employees.size());

        auditLoggingService.logAction(Utils.ACTION_SUCCESS, "Employee", null, "retrieved Employees list");

        return employees;
    }
}
