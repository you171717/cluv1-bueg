package com.shop.service;

import com.shop.constant.OrderStatus;
import com.shop.constant.ReturnStatus;
import com.shop.constant.GiftStatus;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.shop.constant.GiftStatus.BUY;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemTagRepository itemTagRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        //Tag별 누적 판매 증가
        List<ItemTag> itemTag = itemTagRepository.findByItem_Id(item.getId());

        for(ItemTag itemtag : itemTag) {
            itemtag.getTag().addTotalSell();
        }

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // 사용 포인트 보다 가지고 있는 포인트가 적을 시 경고문 출력
        if(member.getPoint() < orderDto.getUsedPoint()) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }

        Order order = Order.createOrder(member, orderItemList, BUY,
                member.getAddress(), member.getAddressDetail());

        // 계산된 포인트 함수 호출
        this.processPointUsage(member, order);

        orderRepository.save(order);

        return order.getId();
    }

    // 선물하기
    public Long gift(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        // 이메일 정보를 이용해 회원 정보 조회
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티 생성
        OrderItem orderItem =
                OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성 (상태 : 선물)
        Order order = Order.createOrder(member, orderItemList , GiftStatus.GIFT,
                orderDto.getAddress(), orderDto.getAddressDetail());
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);

        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);

            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");

                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());

                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    // 구매/선물 상태 조회
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderListStatus(String email, Pageable pageable, GiftStatus giftStatus){

        List<Order> orders = orderRepository.findOrdersStatus(email, pageable, giftStatus);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        // 주문 리스트 순회
        for(Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        Member curMember = memberRepository.findByEmail(email);

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        Member savedMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String email, Integer usedPoint) {
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

            orderItemList.add(orderItem);
        }

        // 부족 경고문
        if(member.getPoint() < usedPoint) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }

        Order order = Order.createOrder(member, orderItemList ,BUY, member.getAddress(), member.getAddressDetail());

        // 포인트 계산 함수 호출
        this.processPointUsage(member, order);

        orderRepository.save(order);

        return order.getId();
    }

    // 포인트 계산 함수
    public void processPointUsage(Member member, Order order) {
        member.setPoint(member.getPoint() - order.getUsedPoint() + order.getAccPoint());

        memberRepository.save(member);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.getById(orderId);
    }

    @Transactional
    public void returnReqOrder(Order order) throws Exception {
        List<OrderItem> orderItemList = order.getOrderItems();

        for (OrderItem orderItem : orderItemList) {
            orderItem.setReturnReqDate(LocalDateTime.now());
            orderItem.setReturnPrice(orderItem.getOrderPrice());
            orderItem.setReturnCount(orderItem.getCount());
            orderItem.setReturnStatus(ReturnStatus.N);
            orderItemRepository.save(orderItem);
        }
        order.setReturnReqDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.RETURN);
        order.setReturnStatus(ReturnStatus.N);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getReturnList(String email, Pageable pageable) throws Exception{
        List<Order> orders = orderRepository.findOrdersForReturnList(email, pageable);
        Long totalCount = orderRepository.countOrderForReturnList(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional
    public void returnConfirmOrder(Long orderId) {
        Order order = this.getOrder(orderId);

        List<OrderItem> orderItemList = order.getOrderItems();

        for (OrderItem orderItem : orderItemList) {
            orderItem.setReturnConfirmDate(LocalDateTime.now());
            orderItem.setReturnPrice(orderItem.getOrderPrice());
            orderItem.setReturnCount(orderItem.getCount());
            orderItem.setReturnStatus(ReturnStatus.Y);
            orderItemRepository.save(orderItem);
        }
        order.setReturnConfirmDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.RETURN);
        order.setReturnStatus(ReturnStatus.Y);
        orderRepository.save(order);

    }

}
