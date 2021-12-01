package com.study.repository;

import com.study.entity.Owner;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    @Query("select o from Owner o join fetch o.cats")
    List<Owner> findAllJoinFetch();

    @EntityGraph(attributePaths = "cats")
    @Query("select o from Owner o")
    List<Owner> findAllEntityGraph();

}
