package com.shop.repository;

import com.shop.entity.ReverseAuction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReverseAuctionRepository extends JpaRepository<ReverseAuction, Long>, ReverseAuctionRepositoryCustom {

}
