package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.models.Employee;
import com.example.rqchallenge.employees.models.Employees;
import com.example.rqchallenge.util.EmployeeSortBySalary;
import com.example.rqchallenge.util.EmployeeSortBySalaryReversed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    static final String BASE_URL = "https://dummy.restapiexample.com/api/v1";
    private final Log log = LogFactory.getLog(EmployeeService.class);

    @Autowired
    private RestTemplate restTemplate;


    List<Employee> getEmployees() {
        try {
            return restTemplate.getForObject(BASE_URL + "/employees", Employees.class).getEmployees();
        }catch (RestClientException rce){
            log.error("Error in getting all employees");
            log.error(rce.getMessage());
            return null;
        }
    }

    List<Employee> getEmployeesByName(String name) {
        return getEmployees().stream()
                             .filter(employee -> employee.getEmployee_name().equals(name))
                             .collect(Collectors.toList());
    }

    Employee getEmployeeById(String id) {
        try {
            return restTemplate.getForObject(BASE_URL + "/employee/" + id, Employee.class);
        }catch (RestClientException rce){
            log.error("Error in getting employee with id " + id);
            return null;
        }
    }

    Integer getHighestSalary() {
        return getEmployees().stream()
                             .max(new EmployeeSortBySalary())
                             .map(employee -> Integer.parseInt(employee.getEmployee_salary()))
                             .orElse(null);

    }

    List<String> getTopTenHighestEarners() {
        return getEmployees().stream()
                             .sorted(new EmployeeSortBySalaryReversed())
                             .limit(10)
                             .map(Employee::getEmployee_name)
                             .collect(Collectors.toList());
    }

    Employee createEmployee(Map<String, Object> employeeInput) {
        try {
            return restTemplate.postForObject(BASE_URL + "/create", employeeInput, Employee.class);
        }catch(RestClientException rce) {
            log.error("Error in creating an employee");
            log.error(rce.getMessage());
            return null;
        }
    }

    String deleteEmployee(String id) {
        try{
            restTemplate.delete(BASE_URL + "/delete/" + id);
            return "successfully deleted record";
        }catch(RestClientException rce){
            log.error("Error in deleting an employee with id " + id);
            log.error(rce.getMessage());
            return null;
        }
    }
}
