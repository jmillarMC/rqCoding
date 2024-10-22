package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.models.Employee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class EmployeeController implements IEmployeeController{
    private final Log log = LogFactory.getLog(EmployeeController.class);
    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("getAllEmployees");
        var employees = employeeService.getEmployees();
        if(Objects.isNull(employees)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        log.info("getEmployeesByNameSearch");
        var filteredEmployees = employeeService.getEmployeesByName(searchString);
        if(Objects.isNull(filteredEmployees)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filteredEmployees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("getEmployeeById");
        var employee = employeeService.getEmployeeById(id);
        if(Objects.isNull(employee)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("getHighestSalaryOfEmployees");
        var salary = employeeService.getHighestSalary();
        if(Objects.isNull(salary)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(salary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("getTopTenHighestEarningEmployeeNames");
        var employeeNames = employeeService.getTopTenHighestEarners();
        if(Objects.isNull(employeeNames)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employeeNames);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        log.info("createEmployee");
        var newEmployee = employeeService.createEmployee(employeeInput);
        if(Objects.isNull(newEmployee)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newEmployee);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        log.info("deleteEmployeeById");
        var message = employeeService.deleteEmployee(id);
        if(Objects.isNull(message)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(message);
    }
}
