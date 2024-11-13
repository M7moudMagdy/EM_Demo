package com.em.demo.service.impl;

import com.em.demo.exception.EmployeeNotFoundException;
import com.em.demo.model.Employee;
import com.em.demo.repository.EmployeeRepository;
import com.em.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        log.info("Creating employee: {}", employee);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return savedEmployee;
    }

    @Override
    public Employee getEmployeeById(String id) {
        log.info("Fetching employee with ID: {}", id);

        UUID uuid = UUID.fromString(id);

        return employeeRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", id);
                    return new EmployeeNotFoundException("Employee not found with id: " + id);
                });
    }

    @Override
    public Employee updateEmployee(String id, Employee employeeDetails) {
        log.info("Updating employee with ID: {}", id);
        Employee employee = getEmployeeById(id);
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setSalary(employeeDetails.getSalary());
        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully with ID: {}", updatedEmployee.getId());
        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(String id) {
        log.info("Deleting employee with ID: {}", id);
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
        log.info("Employee deleted successfully with ID: {}", id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        log.info("Total employees found: {}", employees.size());
        return employees;
    }
}
