package com.shop.repository;

import com.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // CateCode로 조회
    Category findByCateCode(Long id);

}
