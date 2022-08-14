package com.eeit45team2.lungspringbootversion.backend.order.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@JsonIgnoreProperties("order")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemNo;

    //商品
    private String productId;

    //商品名稱
    private String productName;

    //商品單價
    private Integer price;

    //訂購數量
    private Integer qty;

    //小計
    private Integer subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
