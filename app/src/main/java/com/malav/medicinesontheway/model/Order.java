package com.malav.medicinesontheway.model;

/**
 * Created by shahmalav on 21/06/17.
 */

public class Order {
    private String userId;
    private String orderId;
    private String paymentMode;
    private String deliveryMode;
    private Address address;
    private String phoneNumber;
    private String storeId;
    private String storeName;
    private String storeNumber;
    private String emailId;
    private String orderDate;
    private String deliveryDate;
    private String deliveryTime;
    private int isPaymentDone;
    private int isBillGenerated;
    private int isOrderConfirmed;
    private int isOrderExecuted;
    private int isOrderComplete;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIsOrderConfirmed() {
        return isOrderConfirmed;
    }

    public void setIsOrderConfirmed(int isOrderConfirmed) {
        this.isOrderConfirmed = isOrderConfirmed;
    }

    public int getIsOrderExecuted() {
        return isOrderExecuted;
    }

    public void setIsOrderExecuted(int isOrderExecuted) {
        this.isOrderExecuted = isOrderExecuted;
    }

    public int getIsOrderComplete() {
        return isOrderComplete;
    }

    public void setIsOrderComplete(int isOrderComplete) {
        this.isOrderComplete = isOrderComplete;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getIsPaymentDone() {
        return isPaymentDone;
    }

    public void setIsPaymentDone(int isPaymentDone) {
        this.isPaymentDone = isPaymentDone;
    }

    public int getIsBillGenerated() {
        return isBillGenerated;
    }

    public void setIsBillGenerated(int isBillGenerated) {
        this.isBillGenerated = isBillGenerated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
