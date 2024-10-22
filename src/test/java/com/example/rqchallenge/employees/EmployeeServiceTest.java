package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.models.Employee;
import com.example.rqchallenge.employees.models.Employees;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class EmployeeServiceTest {
    private static final Employee EMPLOYEE1 = new Employee("1","Tiger Woods", "1000000", "48", "");
    private static final Employee EMPLOYEE2 = new Employee("2", "Wayne Gretzkey", "10000999", "62", "");
    private static final Employee EMPLOYEE3 = new Employee("3", "Michael Jordan", "100002", "61", "");
    private static final Employee EMPLOYEE4 = new Employee("4", "Lionel Messi", "1000001", "37", "");
    private static final Employee EMPLOYEE5 = new Employee("5", "Lionel Messi", "1000032", "37", "");
    private static final Employee EMPLOYEE6 = new Employee("6", "Lionel Messi", "1000001", "37", "");
    private static final Employee EMPLOYEE7 = new Employee("7","Tiger Woods", "1000000", "48", "");
    private static final Employee EMPLOYEE8 = new Employee("8", "Wayne Gretzkey", "1000003", "62", "");
    private static final Employee EMPLOYEE9 = new Employee("9", "Lionel Messi", "1000001", "37", "");
    private static final Employee EMPLOYEE10 = new Employee("10", "Michael Jordan", "1000002", "61", "");
    private static final Employee EMPLOYEE11 = new Employee("11", "Bob The Builder", "100", "15", "");
    private static final List<Employee> List_OF_EMPLOYEES = new ArrayList<>(List.of(EMPLOYEE1, EMPLOYEE2, EMPLOYEE3, EMPLOYEE4, EMPLOYEE5, EMPLOYEE6, EMPLOYEE7, EMPLOYEE8, EMPLOYEE9, EMPLOYEE10, EMPLOYEE11));
    private static final Employees EMPLOYEES = new Employees(List_OF_EMPLOYEES);

    @Mock
    RestTemplate template;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setup(){
        doReturn(EMPLOYEES).when(template).getForObject(EmployeeService.BASE_URL + "/employees", Employees.class);
    }

    @Test
    void testGetEmployeesByName() {
        var actual = employeeService.getEmployeesByName(EMPLOYEE1.getEmployee_name());

        assertEquals(actual.get(0).getEmployee_name(), EMPLOYEE1.getEmployee_name());
        assertEquals(actual.size(), 2);
    }

    @Test
    void testGetHighestSalary() {
        var actual = employeeService.getHighestSalary();

        assertEquals(actual, Integer.parseInt(EMPLOYEE2.getEmployee_salary()));
    }

    @Test
    void testGetTopTenHighestEarners() {
        var expected = List.of("Wayne Gretzkey", "Lionel Messi", "Michael Jordan", "Wayne Gretzkey", "Michael Jordan", "Lionel Messi", "Lionel Messi", "Lionel Messi", "Tiger Woods", "Tiger Woods");
        var actual = employeeService.getTopTenHighestEarners();

        for(var i = 0; i < expected.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
        assertEquals(actual.size(), 10);

    }

    @Test
    void testCreateEmployeeAllGood() {
        var emp = new HashMap<String, Object>();
        emp.put("employee_name", EMPLOYEE1.getEmployee_name());
        emp.put("employee_salary", EMPLOYEE1.getEmployee_salary());
        emp.put("employee_age", EMPLOYEE1.getEmployee_age());

        doReturn(EMPLOYEE1).when(template).postForObject(EmployeeService.BASE_URL + "/create", null, Employee.class, emp);
        assertEquals(EMPLOYEE1.getId(), employeeService.createEmployee(emp).getId());
    }

    @Test
    void testCreateEmployeeWithException() {
        var emp = new HashMap<String, Object>();
        emp.put("employee_name", EMPLOYEE1.getEmployee_name());
        emp.put("employee_salary", EMPLOYEE1.getEmployee_salary());
        emp.put("employee_age", EMPLOYEE1.getEmployee_age());

        doThrow(RestClientException.class).when(template).postForObject(EmployeeService.BASE_URL + "/create", null, Employees.class, emp);
        assertNull(employeeService.createEmployee(emp));
    }

    @Test
    void testDeleteEmployeeWithException() {
        doThrow(RestClientException.class).when(template).delete(EmployeeService.BASE_URL + "/delete/testId");
        assertNull(employeeService.deleteEmployee("testId"));
    }

    @Test
    void testDeleteEmployeeAllIsGood() {
        doNothing().when(template).delete(EmployeeService.BASE_URL + "/delete/testId");
        assertEquals("successfully deleted record", employeeService.deleteEmployee("testId"));
    }
}
