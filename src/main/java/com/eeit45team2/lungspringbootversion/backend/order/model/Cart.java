package com.eeit45team2.lungspringbootversion.backend.order.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<String, OrderItem> cart = new HashMap<>();
    public Integer getTotal() {
        Integer total = 0;
        for(OrderItem orderItem: cart.values()) {
            Integer price = orderItem.getPrice();
            Integer qty = orderItem.getQty();
            Integer subTotal = price*qty;
            total+=subTotal;
        }
        return total;
    }


    public Map<String, OrderItem> getCart() {
        return cart;
    }



    public void addItem(CartItem cartItem){
        OrderItem orderItem;
        if(cart.containsKey(cartItem.getCartNo())){
            orderItem = cart.get(cartItem.getCartNo());
            orderItem.setQty(orderItem.getQty()+1);
            orderItem.setSubTotal(orderItem.getPrice()*orderItem.getQty());
        }else {
            orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getCartNo());
            orderItem.setPrice(cartItem.getCartPrice());
            orderItem.setProductName(cartItem.getCartName());
            orderItem.setQty(1);
            orderItem.setSubTotal(orderItem.getPrice()*orderItem.getQty());
        }
        cart.put(cartItem.getCartNo(),orderItem);
    }

    public void removeItem(String itemNo){
        cart.remove(itemNo);
    }

    public void updataItem(CartItem cartItem,Integer qty){
        if(!cart.containsKey(cartItem.getCartNo())){
            addItem(cartItem);
        }else {
            OrderItem orderItem = cart.get(cartItem.getCartNo());
            orderItem.setQty(qty);
            orderItem.setSubTotal(orderItem.getPrice()*orderItem.getQty());
            cart.put(cartItem.getCartNo(), orderItem);
        }
    }
}
