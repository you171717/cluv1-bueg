package com.study.entity;

import com.study.repository.CatRepository;
import com.study.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class NPlus1Test {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CatRepository catRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Test
    public void nPlus1Test() {
        List<Cat> cats = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            Cat cat = new Cat();
            cat.setName("Cat " + i);

            cats.add(cat);
        }

        catRepository.saveAll(cats);

        List<Owner> owners = new ArrayList<>();

        for(int j = 0; j < 10; j++) {
            Owner owner = new Owner();
            owner.setName("Owner " + j);
            owner.setCats(cats);

            owners.add(owner);
        }

        ownerRepository.saveAll(owners);

        entityManager.clear();

        System.out.println("---------------------------------------------------------");

        List<Owner> allOwners = ownerRepository.findAll();
//      List<Owner> allOwners = ownerRepository.findAllJoinFetch();
//      List<Owner> allOwners = ownerRepository.findAllEntityGraph();

        for(Owner owner : allOwners) {
            for(Cat cat : owner.getCats()) {
                System.out.println(cat.getName());
            }
        }
    }

}