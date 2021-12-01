package com.study.repository;

import com.study.entity.Employer;
import com.study.entity.EmployerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EmployerRepositoryTest {

    @Autowired
    EmployerRepository employerRepository;

    @Test
    @DisplayName("@IdClass 어노테이션 사용 복합키 테스트")
    public void testIdClassAnnotation() {
        Employer empr = new Employer();
        empr.setEmprNo(1);
        empr.setEmprName("Developer");
        empr.setPhone("111-1111-1111");

        employerRepository.save(empr);

        EmployerId emprId = new EmployerId();
        emprId.setEmprNo(1);
        emprId.setEmprName("Developer");

        empr = employerRepository.findById(emprId).get();

        System.out.println(empr.toString());
    }

}