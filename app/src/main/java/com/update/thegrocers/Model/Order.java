package com.update.thegrocers.Model;


import com.update.thegrocers.Database.CartItem;

import java.util.List;

public class Order {
    private String userId,userName,userPhone,userEmail,transactionId,rpayTransactionId;
    private double lat,lng,totalPayment,finalPayment;
    private boolean cod;
    private int discount;
    private List<CartItem> cartItemList;
    private Long createDate;
    private long deliveryDate;
    private String orderNumber;
    private int orderStatus;
    private AddressModel address;
    private String specialInstructions;
    private CouponModel couponModel;
    private int discountAmount;


    public Order() {
    }

    public Order(String userId, String userName, String userPhone, String userEmail, String transactionId, String rpayTransactionId, double lat, double lng, double totalPayment, double finalPayment, boolean cod, int discount, List<CartItem> cartItemList, Long createDate, long deliveryDate, String orderNumber, int orderStatus, AddressModel address, String specialInstructions, CouponModel couponModel, int discountAmount) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.transactionId = transactionId;
        this.rpayTransactionId = rpayTransactionId;
        this.lat = lat;
        this.lng = lng;
        this.totalPayment = totalPayment;
        this.finalPayment = finalPayment;
        this.cod = cod;
        this.discount = discount;
        this.cartItemList = cartItemList;
        this.createDate = createDate;
        this.deliveryDate = deliveryDate;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.address = address;
        this.specialInstructions = specialInstructions;
        this.couponModel = couponModel;
        this.discountAmount = discountAmount;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRpayTransactionId() {
        return rpayTransactionId;
    }

    public void setRpayTransactionId(String rpayTransactionId) {
        this.rpayTransactionId = rpayTransactionId;
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

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public CouponModel getCouponModel() {
        return couponModel;
    }

    public void setCouponModel(CouponModel couponModel) {
        this.couponModel = couponModel;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }
}
