package com.eeit45team2.lungspringbootversion.backend.order.controller;

import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatus;
import com.eeit45team2.lungspringbootversion.backend.order.model.Order;
import com.eeit45team2.lungspringbootversion.backend.order.pay.EcpayPayment;
import com.eeit45team2.lungspringbootversion.backend.order.pay.PaypalPayment;
import com.eeit45team2.lungspringbootversion.backend.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/Order/ECPAY/Result")
    public String result(@RequestParam("RtnCode") String rtnCode,
                         @RequestParam("MerchantTradeNo") String merchantTradeNo,
                         RedirectAttributes model) {
        System.out.println("RtnCode: " + rtnCode);
        System.out.println("MerchantTradeNo: " + merchantTradeNo);

        Order order = orderService.findByOrderNo(merchantTradeNo).orElse(null);
        if (rtnCode.equals("1")) {
            if (order != null) {
                orderService.pay(order.getOrderId());
            }
        }
        model.addFlashAttribute("orderNo", order.getOrderNo());
        return "redirect:/Front/ordersview";
    }

    @GetMapping("/Order/PAYPAL/Result")
    public String resultPaypal(@RequestParam String token,
                               RedirectAttributes model) {
        System.out.println("Token: " + token);
        String orderNo = PaypalPayment.captureOrder(token);
        Order order = orderService.findByOrderNo(orderNo).orElse(null);
        if (order != null) {
            Order pay = orderService.pay(order.getOrderId());
        }
        model.addFlashAttribute("orderNo", order.getOrderNo());
        return "redirect:/Front/ordersview";
    }


    @GetMapping(value = "/Order/{orderNo}/Pay", produces = "text/html;charset=UTF-8")
    public ResponseEntity<String> pay(@PathVariable String orderNo, HttpServletRequest request) {
        Order order = orderService.findByOrderNo(orderNo).orElse(null);
        String baseURL = request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length()) + request.getContextPath();
        if (order != null && order.getOrderStatus() == OrderStatus.WAIT_PAYMENT) {
            switch (order.getPayType()) {
                case ECPAY:
                    String ecpayUrl = baseURL + "/Order/ECPAY/Result";
                    System.out.println("ecpayUrl:" + ecpayUrl);
                    String string = EcpayPayment.genAioCheckOutALL(order, ecpayUrl);
                    return new ResponseEntity<>(string, HttpStatus.OK);
                case PAYPAL:
                    String paypalUrl = baseURL + "/Order/PAYPAL/Result";
                    System.out.println("paypalUrl:" + paypalUrl);
                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(PaypalPayment.createPayUrl(order, paypalUrl))).build();
            }
        }
        return null;
    }

    @PostMapping("/Order/{orderNo}/Ship")
    public ResponseEntity<Order> deliver(@RequestParam String trackingNumber,
                                         @PathVariable String orderNo) {

        Order order = orderService.findByOrderNo(orderNo).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "請檢查訂單編號");
        }
        Order deliver = orderService.deliver(order.getOrderId(), trackingNumber);

        return ResponseEntity.status(HttpStatus.OK).body(deliver);

    }


    @PostMapping("/Order/{orderNo}/Cancel")
    public ResponseEntity<Order> cancel(@PathVariable String orderNo) {

        Order order = orderService.findByOrderNo(orderNo).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "請檢查訂單編號");
        }
        Order cancel = orderService.cancel(order.getOrderId());

        return ResponseEntity.status(HttpStatus.OK).body(cancel);

    }

    @PostMapping("/Order/{orderNo}/Receive")
    public ResponseEntity<Order> receive(@PathVariable String orderNo) {

        Order order = orderService.findByOrderNo(orderNo).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "請檢查訂單編號");
        }
        Order receive = orderService.receive(order.getOrderId());

        return ResponseEntity.status(HttpStatus.OK).body(receive);

    }

    @PostMapping("/Order/{orderNo}/Refund")
    public ResponseEntity<Order> refund(@PathVariable String orderNo) {

        Order order = orderService.findByOrderNo(orderNo).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "請檢查訂單編號");
        }
        Order cancel = orderService.refund(order.getOrderId());

        return ResponseEntity.status(HttpStatus.OK).body(cancel);

    }

    @PostMapping("/Order/{orderNo}/Refunding")
    public ResponseEntity<Order> refunding(@RequestParam String choose,
                                           @PathVariable String orderNo) {
        Order order = orderService.findByOrderNo(orderNo).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "請檢查訂單編號");
        }
        Order refund = null;
        if (choose.equals("accept")) {
            refund = orderService.accept(order.getOrderId());
        } else if (choose.equals("reject")) {
            refund = orderService.reject(order.getOrderId());
        }

        return ResponseEntity.status(HttpStatus.OK).body(refund);
    }

}
