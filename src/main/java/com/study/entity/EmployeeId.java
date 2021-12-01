package com.study.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class EmployeeId implements Serializable {

    private int empNo;
    private String empName;

}
