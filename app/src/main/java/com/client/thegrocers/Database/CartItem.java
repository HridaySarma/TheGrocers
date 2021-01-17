package com.client.thegrocers.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Cart",primaryKeys = {"uid","productId"})
public class CartItem {

    @NonNull
    @ColumnInfo(name = "productId")
    private String productId;

    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "productImage")
    private String productImage;

    @ColumnInfo(name = "productPrice")
    private Double productPrice;

    @ColumnInfo(name = "productSellingPrice")
    private Double productSellingPrice;

    @ColumnInfo(name = "productQuantity")
    private int productQuantity;

    @ColumnInfo(name = "userPhone")
    private String userPhone;

    @NonNull
    @ColumnInfo(name = "uid")
    private String uid;

    @ColumnInfo(name = "packageSize")
    private float packageSize;


    @ColumnInfo(name = "height")
    private float height;

    @ColumnInfo(name = "breadth")
    private float breadth;

    @ColumnInfo(name = "length")
    private float length;


    @NonNull
    public String getProductId() {
        return productId;
    }

    public void setProductId(@NonNull String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Double getProductSellingPrice() {
        return productSellingPrice;
    }

    public void setProductSellingPrice(Double productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
    }

    public float getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(float packageSize) {
        this.packageSize = packageSize;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getBreadth() {
        return breadth;
    }

    public void setBreadth(float breadth) {
        this.breadth = breadth;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this){
            return true;
        }
        if (!(obj instanceof CartItem))
            return false;
        CartItem cartItem = (CartItem)obj;
        return cartItem.getProductId().equals(this.productId);
    }

}
