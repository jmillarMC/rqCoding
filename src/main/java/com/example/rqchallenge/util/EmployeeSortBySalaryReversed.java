package com.example.rqchallenge.util;

import com.example.rqchallenge.employees.models.Employee;

import java.util.Comparator;

public class EmployeeSortBySalaryReversed  implements Comparator<Employee> {

    public int compare(Employee o1, Employee o2) {
        return o2.getEmployee_salary().compareTo(o1.getEmployee_salary());
    }
}

