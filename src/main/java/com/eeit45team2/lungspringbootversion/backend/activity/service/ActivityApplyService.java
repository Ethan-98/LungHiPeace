package com.eeit45team2.lungspringbootversion.backend.activity.service;

import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityApply;
import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityBean;
import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityApplyService {

    List<ActivityApply> findAllByMember(MemberBean memberBean);

    //show all
    public List<ActivityApply> findAll();

    //save
    public void save(ActivityApply order);

    //FindById
    public ActivityApply FindById(Integer id);

    //delete
    public void delete(Integer id);

    ActivityApply FindById(Long id);

    void delete(Long id);

    ActivityApply createOrder(ActivityApply order);

//    public List<ActivityApply> abdoglistAll(Long keyword);

//    Optional<ActivityApply> findByOrderNo(String orderNo);

//    ActivityApply pay(Integer orderId);

//    Optional<ActivityApply> findByOrderId(Integer orderId);

//    void deleteOrder(Integer orderId);

    //發貨
//    ActivityApply deliver(Integer orderId, String trackingNumber);

    //取消
//    ActivityApply cancel(Integer orderId);

    //收貨
//    ActivityApply receive(Integer orderId);

    //提出退款
//    ActivityApply refund(Integer orderId);

    //退款同意
//    ActivityApply accept(Integer orderId);

    //退款拒絕
//    ActivityApply reject(Integer orderId);


//    Page<ActivityApply> findAllByOrderStatusAndMember(OrderStatus status, MemberBean memberBean, Pageable pageable);

//    Page<ActivityApply> findAllByMember(MemberBean memberBean, Pageable pageable);
}
