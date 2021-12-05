package com.shop.repository;

import com.shop.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {

    List<ReviewImg> findByOrderItemIdOrderByIdAsc(Long orderItemId);

    ReviewImg findByOrderItemId(Long orderItemId);

}
