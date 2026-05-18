package com.example.myapplication.db.entity;

import com.example.myapplication.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {

    public static EmployeeEntity toEntity(Employee emp){
        return new EmployeeEntity(emp.getId(),
                emp.getName(),
                emp.getDepartment(),
                emp.getPosition(),
                emp.getSalary(),
                emp.getEmail(),
                emp.getPhone());
    }
    public static Employee toModel(EmployeeEntity entity){
        Employee employee = new Employee();
        employee.setId(entity.getId());
        employee.setName(entity.getName());
        employee.setDepartment(entity.getDepartment());
        employee.setPosition(entity.getPosition());
        employee.setSalary(entity.getSalary());
        employee.setEmail(entity.getEmail());
        employee.setPhone(entity.getPhone());
        return employee;
    }

    public static List<EmployeeEntity> toEntityList(List<Employee> list){
        List<EmployeeEntity> result = new ArrayList<>();
        for(Employee e : list) result.add(toEntity(e));
        return result;
    }

    public static List<Employee> toModelList(List<EmployeeEntity> list){
        List<Employee> result = new ArrayList<>();
        for(EmployeeEntity e : list) result.add(toModel(e));
        return result;
    }
}
