package com.eeit45team2.lungspringbootversion.backend.order.controller;

import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import com.eeit45team2.lungspringbootversion.backend.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/Backendorder")
public class OrderView {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orderlist")
    public String viewBackEndOrder(Model model){
        List<Order> orders =null;
        orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "/Backendorder/BackOrder";
    }
}
