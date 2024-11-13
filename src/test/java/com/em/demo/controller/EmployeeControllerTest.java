
package com.em.demo.controller;

import com.em.demo.controller.EmployeeController;
import com.em.demo.exception.EmployeeNotFoundException;
import com.em.demo.model.Employee;
import com.em.demo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("Engineering");
        employee.setSalary(BigDecimal.valueOf(50000));
    }

    @Test
    void createEmployee() throws Exception {
        Mockito.when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value(employee.getFirstName()));
    }

    @Test
    void getEmployeeById() throws Exception {
        Mockito.when(employeeService.getEmployeeById(String.valueOf(employee.getId()))).thenReturn(employee);

        mockMvc.perform(get("/api/v1/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.email").value(employee.getEmail()))
                .andExpect(jsonPath("$.data.department").value(employee.getDepartment()));
    }

    @Test
    void getEmployeeById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(employeeService.getEmployeeById(String.valueOf(id))).thenThrow(new EmployeeNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployee() throws Exception {
        employee.setDepartment("Marketing");
        Mockito.when(employeeService.updateEmployee(String.valueOf(any(UUID.class)), any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/api/v1/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Request was successful"));
    }

    @Test
    void deleteEmployee() throws Exception {
        Mockito.doNothing().when(employeeService).deleteEmployee(String.valueOf(employee.getId()));

        mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllEmployees() throws Exception {
        Mockito.when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(employee));

        mockMvc.perform(get("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(1));
    }
}
