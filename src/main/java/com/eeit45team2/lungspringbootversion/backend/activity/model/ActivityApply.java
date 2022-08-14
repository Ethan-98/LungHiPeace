package com.eeit45team2.lungspringbootversion.backend.activity.model;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatus;
import com.eeit45team2.lungspringbootversion.backend.order.constant.OrderStatusConverter;
import com.eeit45team2.lungspringbootversion.backend.order.constant.PayType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ActivityApply")
public class ActivityApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(unique = true)
    private String orderNo;

    @Transient
    private String username;
    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "miNo")
    @JsonIgnore
    private MemberBean memberBean;

    private Integer acid;

    private String name;

    private String address;

    private Integer phone;

    private int applyaudit;//審核 是否通過申請

    private String acname;
    private String acdate;
    private String acvenue;
    private String acor;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus orderStatus;

    public ActivityApply() {
    }

    public int getApplyaudit() {
        return applyaudit;
    }

    public void setApplyaudit(int applyaudit) {
        this.applyaudit = applyaudit;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUsername() {
        if (memberBean != null) {
            return memberBean.getMiName();
        } else {
            return null;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MemberBean getMemberBean() {
        return memberBean;
    }

    public void setMemberBean(MemberBean memberBean) {
        this.memberBean = memberBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getAcdate() {
        return acdate;
    }

    public void setAcdate(String acdate) {
        this.acdate = acdate;
    }

    public String getAcor() {
        return acor;
    }

    public void setAcor(String acor) {
        this.acor = acor;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getAcname() {
        return acname;
    }

    public void setAcname(String acname) {
        this.acname = acname;
    }

    public String getAcvenue() {
        return acvenue;
    }

    public void setAcvenue(String acvenue) {
        this.acvenue = acvenue;
    }

    public Integer getAcid() {
        return acid;
    }

    public void setAcid(Integer acid) {
        this.acid = acid;
    }
}
