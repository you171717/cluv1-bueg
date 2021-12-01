package com.study.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployerId implements Serializable {

    private int emprNo;
    private String emprName;

}
