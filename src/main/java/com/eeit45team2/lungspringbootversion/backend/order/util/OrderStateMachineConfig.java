package com.eeit45team2.lungspringbootversion.backend.order.util;


import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatus;
import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatusChangeEvent;
import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import com.eeit45team2.lungspringbootversion.backend.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;


@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderStatusChangeEvent> {


    @Autowired
    OrderRepository repository;


    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderStatusChangeEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.WAIT_PAYMENT)
                .states(EnumSet.allOf(OrderStatus.class));
    }


    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderStatusChangeEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatus.WAIT_PAYMENT).target(OrderStatus.WAIT_DELIVER).event(OrderStatusChangeEvent.PAYED)
                .and()
                .withExternal().source(OrderStatus.WAIT_DELIVER).target(OrderStatus.WAIT_RECEIVE).event(OrderStatusChangeEvent.DELIVERY)
                .and()
                .withExternal().source(OrderStatus.WAIT_RECEIVE).target(OrderStatus.FINISH).event(OrderStatusChangeEvent.RECEIVED)
                .and()
                .withExternal().source(OrderStatus.WAIT_PAYMENT).target(OrderStatus.CANCELLED).event(OrderStatusChangeEvent.CANCEL)
                .and()
                .withExternal().source(OrderStatus.WAIT_DELIVER).target(OrderStatus.REFUNDING).event(OrderStatusChangeEvent.REFUND)
                .and()
                .withExternal().source(OrderStatus.REFUNDING).target(OrderStatus.REFUNDED).event(OrderStatusChangeEvent.ACCEPT)
                .and()
                .withExternal().source(OrderStatus.REFUNDING).target(OrderStatus.WAIT_DELIVER).event(OrderStatusChangeEvent.REJECT);
    }



    @Bean
    public DefaultStateMachinePersister persister(){
        return new DefaultStateMachinePersister<>(new StateMachinePersist<Object, Object, Order>() {

            @Override
            public void write(StateMachineContext<Object, Object> stateMachineContext, Order order) throws Exception {
                Order orderDb = repository.findById(order.getOrderId()).orElse(null);
                if(orderDb!=null){
                    orderDb.setOrderStatus(order.getOrderStatus());
                    Order save = repository.save(orderDb);
                    System.out.println("save:"+save);
                }
            }

            @Override
            public StateMachineContext<Object, Object> read(Order order) throws Exception {
                Order orderDb = repository.findById(order.getOrderId()).orElse(null);
                if(orderDb!=null){
                    return new DefaultStateMachineContext(orderDb.getOrderStatus(), null, null, null);
                }
                return null;
            }
        });
    }
}
