package com.study.repository;

import com.study.entity.Employee;
import com.study.entity.EmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, EmployeeId> {

    Employee findByEmployeeId(EmployeeId employeeId);

}
