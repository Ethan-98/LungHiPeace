package com.eeit45team2.lungspringbootversion.backend.order.repository;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatus;
import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderNo(String orderNo);

    Page<Order> findAllByMemberBean(MemberBean memberBean, Pageable pageable);

    Page<Order> findAllByOrderStatusAndMemberBean(OrderStatus status, MemberBean memberBean, Pageable pageable);
}
