package kr.co.t_woori.market.order;

import java.io.Serializable;
import java.util.ArrayList;

import kr.co.t_woori.market.goods.Goods;

/**
 * Created by rladn on 2017-10-25.
 */

public class OrderHistory implements Serializable {

    private String orderNum, totalPrice, address, deliveryTime, request, payment, orderTime, state, phone, recipient, phone2;

    public OrderHistory(String orderNum, String totalPrice, String address, String deliveryTime, String request, String payment, String orderTime, String state, String phone, String recipient, String phone2) {
        this.orderNum = orderNum;
        this.totalPrice = totalPrice;
        this.address = address;
        this.deliveryTime = deliveryTime;
        this.request = request;
        this.payment = payment;
        this.orderTime = orderTime;
        this.state = state;
        this.phone = phone;
        this.recipient = recipient;
        this.phone2 = phone2;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getRequest() {
        return request;
    }

    public String getPayment() {
        return payment;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getState() {
        return state;
    }

    public String getPhone() {
        return phone;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getPhone2() {
        return phone2;
    }
}
