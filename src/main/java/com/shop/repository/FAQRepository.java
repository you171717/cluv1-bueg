package com.shop.repository;

import com.shop.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ,Long> {

    @Query("select f from FAQ f where f.question like %:question%")
    List<FAQ> findByQuestion(@Param("question") String question);

}
