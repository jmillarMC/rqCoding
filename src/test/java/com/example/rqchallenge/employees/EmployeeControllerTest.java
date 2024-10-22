package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.models.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    private static final Employee EMPLOYEE1 = new Employee("1","Tiger Woods", "1000000", "48", "");
    private static final Employee EMPLOYEE2 = new Employee("2", "Wayne Gretzkey", "1000001", "62", "");
    private static final ArrayList<Employee> EMPLOYEES = new ArrayList<>(List.of(EMPLOYEE1, EMPLOYEE2));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;


    @Test
    void testGetAllEmployeesWhenNoEmployees() throws Exception {
        doReturn(null).when(employeeService).getEmployees();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void testGetAllEmployeesWhenThereAreEmployees() throws Exception {
        var objM = new ObjectMapper();
        doReturn(EMPLOYEES).when(employeeService).getEmployees();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objM.writeValueAsString(EMPLOYEES)));
    }

    @Test
    void getEmployeesByNameSearchWhenNoEmployees() throws Exception {
        doReturn(null).when(employeeService).getEmployeesByName("TEST");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/search/{searchString}","TEST"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
    @Test
    void getEmployeesByNameSearchWhenThereAreEmployeesWithThatName() throws Exception {
        var objM = new ObjectMapper();
        doReturn(EMPLOYEES).when(employeeService).getEmployeesByName("TEST");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/search/{searchString}","TEST"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objM.writeValueAsString(EMPLOYEES)));
    }

    @Test
    void getEmployeeByIdWhenNoEmployeeWithThatID() throws Exception {
        doReturn(null).when(employeeService).getEmployeeById("TESTID");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/{id}", "TESTID"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void getEmployeeByIdWhenThereAreNoEmployeesWithThatId() throws Exception {
        var objM = new ObjectMapper();
        doReturn(EMPLOYEE1).when(employeeService).getEmployeeById("TESTID");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/{id}", "TESTID"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objM.writeValueAsString(EMPLOYEE1)));
    }

    @Test
    void getHighestSalaryOfEmployeesWhenThereAreNoEmployees() throws Exception {
        doReturn(null).when(employeeService).getHighestSalary();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/highestSalary"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        doReturn(Integer.parseInt(EMPLOYEE1.getEmployee_salary())).when(employeeService).getHighestSalary();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/highestSalary"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(EMPLOYEE1.getEmployee_salary()));
    }

    @Test
    void getTopTenHighestEarningEmployeeNamesWithNoEmployees() throws Exception {
        doReturn(null).when(employeeService).getTopTenHighestEarners();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/topTenHighestEarningEmployeeNames"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NO_CONTENT.value()))
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        var objM = new ObjectMapper();
        var employeeNames = List.of(EMPLOYEE1.getEmployee_name(), EMPLOYEE2.getEmployee_name());
        doReturn(employeeNames).when(employeeService).getTopTenHighestEarners();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/topTenHighestEarningEmployeeNames"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objM.writeValueAsString(employeeNames)));
    }

    @Test
    void createEmployeeWhenAllGood() throws Exception {
        var objM = new ObjectMapper();
        var emp = new HashMap<String, Object>();

        emp.put("employee_name", EMPLOYEE1.getEmployee_name());
        emp.put("employee_salary", EMPLOYEE1.getEmployee_salary());
        emp.put("employee_age", EMPLOYEE1.getEmployee_age());

        doReturn(EMPLOYEE1).when(employeeService).createEmployee(emp);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .accept(MediaType.APPLICATION_JSON)
                                                    .content(objM.writeValueAsString(emp)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objM.writeValueAsString(EMPLOYEE1)));

    }

    @Test
    void createEmployeeWhenErrorCreatingEmployee() throws Exception {
        var objM = new ObjectMapper();
        var emp = new HashMap<String, Object>();
        emp.put("employee_name", EMPLOYEE1.getEmployee_name());
        doReturn(null).when(employeeService).createEmployee(emp);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .accept(MediaType.APPLICATION_JSON)
                                                    .content(objM.writeValueAsString(emp)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteEmployeeByIdWhenProblemDeleting() throws Exception {
        doReturn(null).when(employeeService).deleteEmployee("TESTID");
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/{id}", "TESTID"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteEmployeeByIdWhenAllGood() throws Exception {
        var success = "Successful";
        doReturn(success).when(employeeService).deleteEmployee("TESTID");
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/{id}", "TESTID"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(success));
    }
}
