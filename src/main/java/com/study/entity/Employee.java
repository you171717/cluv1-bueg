package com.study.entity;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "employee")
public class Employee {

    @EmbeddedId
    private EmployeeId employeeId;

    private String phone;

}
