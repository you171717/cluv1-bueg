package com.shop.repository;

import com.shop.constant.GiftStatus;
import com.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.member.email = :email and " +
            "o.giftStatus = :giftStatus " +
            "order by o.orderDate desc"
    )
    List<Order> findOrdersStatus(@Param("email") String email,
                                 Pageable pageable, @Param("giftStatus") GiftStatus giftStatus);

    @Query("select o from Order o where o.member.email = :email order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);

    @Query("select count(o) from Order o " +
            "where 1=1 " +
            "AND o.orderStatus = 'RETURN'"
    )
    Long countOrderForReturnList(@Param("email") String email);

    @Query("select o from Order o " +
            "where 1=1 " +
            "AND o.orderStatus = 'RETURN'" +
            "order by o.orderDate desc"
    )
    List<Order> findOrdersForReturnList(@Param("email") String email, Pageable pageable);

}
