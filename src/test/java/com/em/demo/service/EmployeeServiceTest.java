package com.em.demo.service;

import com.em.demo.exception.EmployeeNotFoundException;
import com.em.demo.model.Employee;
import com.em.demo.repository.EmployeeRepository;
import com.em.demo.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment("Engineering");
        employee.setSalary(BigDecimal.valueOf(50000));
    }

    @Test
    void createEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee createdEmployee = employeeService.createEmployee(employee);

        assertThat(createdEmployee).isNotNull();
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        Employee foundEmployee = employeeService.getEmployeeById(String.valueOf(employee.getId()));

        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getId()).isEqualTo(employee.getId());
        verify(employeeRepository, times(1)).findById(employee.getId());
    }

    @Test
    void getEmployeeById_NotFound() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(String.valueOf(id)));
        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void updateEmployee() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        employee.setDepartment("Marketing");
        Employee updatedEmployee = employeeService.updateEmployee(String.valueOf(employee.getId()), employee);

        assertThat(updatedEmployee.getDepartment()).isEqualTo("Marketing");
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void deleteEmployee() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee(String.valueOf(employee.getId()));

        verify(employeeRepository, times(1)).delete(employee);
    }
}
