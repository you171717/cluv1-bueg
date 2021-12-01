package com.study.repository;

import com.study.entity.Employee;
import com.study.entity.EmployeeId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    @DisplayName("@Embeddable 어노테이션 사용 복합키 테스트")
    public void testEmbeddableAnnotation() {
        EmployeeId empId = new EmployeeId();
        empId.setEmpNo(1);
        empId.setEmpName("Tester");

        Employee emp = new Employee();
        emp.setEmployeeId(empId);
        emp.setPhone("000-0000-0000");

        employeeRepository.save(emp);

        emp = employeeRepository.findByEmployeeId(empId);

        System.out.println(emp.toString());
        System.out.println(emp.getEmployeeId().toString());
    }

}