package com.eeit45team2.lungspringbootversion.backend.order.constant;

import com.fasterxml.jackson.annotation.JsonValue;


public enum OrderStatus {

    WAIT_PAYMENT("待付款"),
    WAIT_DELIVER("待出貨"),
    WAIT_RECEIVE("待收貨"),
    FINISH("完成"),
    CANCELLED("已取消"),
    REFUNDING("退款審核中"),
    REFUNDED("已退款");

    private String code;

    OrderStatus(String string) {
        code = string;
    }


    @JsonValue
    public String getCode() {
        return code;
    }
}
