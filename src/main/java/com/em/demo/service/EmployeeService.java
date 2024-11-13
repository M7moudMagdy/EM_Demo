package com.em.demo.service;

import com.em.demo.model.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee getEmployeeById(String id);
    Employee updateEmployee(String id, Employee employeeDetails);
    void deleteEmployee(String id);
    List<Employee> getAllEmployees();
}
