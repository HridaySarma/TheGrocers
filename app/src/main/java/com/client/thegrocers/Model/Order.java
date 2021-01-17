package com.client.thegrocers.Model;

import com.yuvraj.thegroceryapp.Database.CartItem;

import java.util.List;

public class Order {
    private String userId,userName,userPhone,transactionId;
    private double lat,lng,totalPayment,finalPayment;
    private boolean cod;
    private int discount;
    private List<CartItem> cartItemList;
    private Long createDate;
    private long deliveryDate;
    private float finalWeight,finalHeight,finalBreadth,finalLength;
    private String orderNumber;
    private int orderStatus;
    private AddressModel address;


    public Order() {
    }

    public Order(String userId, String userName, String userPhone, String transactionId, double lat, double lng, double totalPayment, double finalPayment, boolean cod, int discount, List<CartItem> cartItemList, Long createDate, long deliveryDate, float finalWeight, float finalHeight, float finalBreadth, float finalLength, String orderNumber, int orderStatus, AddressModel address) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.transactionId = transactionId;
        this.lat = lat;
        this.lng = lng;
        this.totalPayment = totalPayment;
        this.finalPayment = finalPayment;
        this.cod = cod;
        this.discount = discount;
        this.cartItemList = cartItemList;
        this.createDate = createDate;
        this.deliveryDate = deliveryDate;
        this.finalWeight = finalWeight;
        this.finalHeight = finalHeight;
        this.finalBreadth = finalBreadth;
        this.finalLength = finalLength;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(double finalPayment) {
        this.finalPayment = finalPayment;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public float getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(float finalWeight) {
        this.finalWeight = finalWeight;
    }

    public float getFinalHeight() {
        return finalHeight;
    }

    public void setFinalHeight(float finalHeight) {
        this.finalHeight = finalHeight;
    }

    public float getFinalBreadth() {
        return finalBreadth;
    }

    public void setFinalBreadth(float finalBreadth) {
        this.finalBreadth = finalBreadth;
    }

    public float getFinalLength() {
        return finalLength;
    }

    public void setFinalLength(float finalLength) {
        this.finalLength = finalLength;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }
}
