package com.eeit45team2.lungspringbootversion.backend.order.controller;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatus;
import com.eeit45team2.lungspringbootversion.backend.order.constant.PayType;
import com.eeit45team2.lungspringbootversion.backend.order.model.Cart;
import com.eeit45team2.lungspringbootversion.backend.order.model.CartItem;
import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import com.eeit45team2.lungspringbootversion.backend.order.service.OrderService;
import com.eeit45team2.lungspringbootversion.backend.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;

@RestController
@SessionAttributes(value = {"cart"})
public class ShoppingCartController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ProductService productService;

    @Autowired
    public ShoppingCartController(OrderService orderService, MemberService memberService, ProductService productService) {
        this.orderService = orderService;
        this.memberService = memberService;
        this.productService = productService;
    }
    @GetMapping("/Carts")
    public ResponseEntity<Cart> view(@ModelAttribute Cart cart){
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @GetMapping("/Carts/Add")
    public ResponseEntity<Cart> addCart(@RequestParam Integer itemId,
                                        @ModelAttribute Cart cart){
        CartItem cartItem = null;
        cartItem = productService.getProducts(itemId);

        if(cartItem != null){
            cart.addItem(cartItem);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @GetMapping("/Carts/Update")
    public ResponseEntity<Cart> cartUpdate(@RequestParam String itemNo,
                                           @RequestParam Integer qty,
                                           @ModelAttribute Cart cart){
        CartItem cartItem = null;
        Integer itemId = Integer.valueOf(itemNo.substring(1));
        cartItem = productService.getProducts(itemId);
        if (cartItem != null) {
            cart.updataItem(cartItem, qty);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
    @GetMapping("/Carts/Remove")
    public ResponseEntity<Cart> cartRemove(@RequestParam String itemNo,
                                           @ModelAttribute Cart cart){
        CartItem cartItem = null;
        Integer itemId = Integer.valueOf(itemNo.substring(1));
        cartItem = productService.getProducts(itemId);
        if (cartItem != null) {
            cart.removeItem(cartItem.getCartNo());
        }
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @PostMapping(path = "/Front/CheckOut")
    public ResponseEntity<String> checkOut(@ModelAttribute Cart cart,
                                           @RequestParam String address,
                                           @RequestParam String name,
                                           @RequestParam Integer phone,
                                           @RequestParam PayType payType,
                                           HttpServletRequest request,
                                           SessionStatus status,
                                           Principal principal){
        if(principal==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            String baseURL = request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length()) + request.getContextPath();
            String orderNo = null;

            if(cart != null){
                Order order = new Order();
                //訂單編號
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                Date current = new Date();
                int end = (int) (Math.random() * 20);
                order.setOrderNo(df.format(current) + end);
                orderNo = order.getOrderNo();
                order.setOrderDate(current);
                //狀態
                order.setOrderStatus(OrderStatus.WAIT_PAYMENT);

                order.setPayType(payType);

                MemberBean memberBean = memberService.findByMiAccount(principal.getName());

                order.setMemberBean(memberBean);
                order.setAddress(address);
                order.setName(name);
                order.setPhone(phone);
                order.setOrderItems(new LinkedHashSet<>(cart.getCart().values()));

                order.getOrderItems().forEach((e) -> e.setOrder(order));
                order.setTotalPrice(cart.getTotal());
                orderService.createOrder(order);
                status.setComplete();
            }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(baseURL + "/Order/" + orderNo + "/Pay")).build();
    }

}
