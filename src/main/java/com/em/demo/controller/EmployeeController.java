package com.em.demo.controller;

import com.em.demo.dto.ApiResponse;
import com.em.demo.model.Employee;
import com.em.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createEmployee(@Valid @RequestBody Employee employee) {
        log.info("Received request to create employee");
        Employee savedEmployee = employeeService.createEmployee(employee);
        log.info("Employee created with ID: {}", savedEmployee.getId());
        return ResponseEntity.ok(ApiResponse.success(savedEmployee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getEmployeeById(@PathVariable String id) {
        log.info("Received request to fetch employee with ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employeeDetails) {
        log.info("Received request to update employee with ID: {}", id);
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        log.info("Employee with ID: {} updated successfully", id);
        return ResponseEntity.ok(ApiResponse.success(updatedEmployee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteEmployee(@PathVariable String id) {
        log.info("Received request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        log.info("Employee with ID: {} deleted successfully", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT) // Use the appropriate status for creation
                .body(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllEmployees() {
        log.info("Received request to fetch all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        log.info("Returning {} employees", employees.size());
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
}
