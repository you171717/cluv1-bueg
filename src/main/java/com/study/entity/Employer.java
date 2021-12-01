package com.study.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "employer")
@IdClass(EmployerId.class)
public class Employer {

    @Id
    private int emprNo;

    @Id
    private String emprName;

    private String phone;

}
