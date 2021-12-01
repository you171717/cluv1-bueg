package com.study.repository;

import com.study.entity.Employer;
import com.study.entity.EmployerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, EmployerId> {

}
