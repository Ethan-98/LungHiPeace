package com.eeit45team2.lungspringbootversion.backend.order.model;

public class CartItemI implements CartItem{
    private Integer productId;
    private String productName;
    private Integer productPrice;

    @Override
    public String getCartNo() {
        return "P"+ productId;
    }

    @Override
    public Integer getCartPrice() {
        return productPrice;
    }

    @Override
    public String getCartName() {
        return productName;
    }
}
