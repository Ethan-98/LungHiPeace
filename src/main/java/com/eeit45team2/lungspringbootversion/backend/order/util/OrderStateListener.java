package com.eeit45team2.lungspringbootversion.backend.order.util;

import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatus;
import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatusChangeEvent;
import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;


@Component("orderStateListener")
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {
    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public boolean payTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderStatus(OrderStatus.WAIT_DELIVER);
        return true;
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public boolean deliverTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderStatus(OrderStatus.WAIT_RECEIVE);
        return true;
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public boolean receiveTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderStatus(OrderStatus.FINISH);
        return true;
    }

    @OnTransition(source = "WAIT_PAYMENT", target = "CANCELLED")
    public boolean cancelTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderStatus(OrderStatus.CANCELLED);
        return true;
    }

    @OnTransition(source = "WAIT_DELIVER", target = "REFUNDING")
    public boolean refundTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderStatus(OrderStatus.REFUNDING);
        return true;
    }

    @OnTransition(source = "REFUNDING", target = "REFUNDED")
    public boolean acceptTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderStatus(OrderStatus.REFUNDED);
        return true;
    }

    @OnTransition(source = "REFUNDING", target = "WAIT_DELIVER")
    public boolean rejectTransition(Message<OrderStatusChangeEvent> message) {
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderStatus(OrderStatus.WAIT_DELIVER);
        return true;
    }
}
