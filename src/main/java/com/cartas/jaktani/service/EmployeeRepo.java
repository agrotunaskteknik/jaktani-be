package com.cartas.jaktani.service;

import com.cartas.jaktani.model.Employee;

import java.util.Map;

public interface EmployeeRepo {

    // Save a new employee.
    void save(Employee employee);

    // Find employee by id.
    Employee findById(String id);

    // Final all employees.
    Map<String, Employee> findAll();

    // Delete a employee.
    void delete(String id);
}
