package com.shop.repository;

import com.shop.entity.Bid;
import com.shop.entity.ReverseAuction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long>, BidRepositoryCustom {

    Bid findByReverseAuctionAndApprovedYn(ReverseAuction reverseAuction, String approvedYn);

}
