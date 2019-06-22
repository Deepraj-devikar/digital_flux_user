package com.dolphintechno.dolphindigitalflux.model;

import android.graphics.drawable.Drawable;

public class CartProduct {

    public int image;
    public Drawable imageDrw;
    String ProductId;
    String ProductName;
    String ProductMrp;
    String ProductSellerName;
    String ProductShippingCharge;
    String ProductQuantity;
    String imgNameDB;

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductMrp(String productMrp) {
        ProductMrp = productMrp;
    }

    public void setProductSellerName(String productSellerName) {
        ProductSellerName = productSellerName;
    }

    public void setProductShippingCharge(String productShippingCharge) {
        ProductShippingCharge = productShippingCharge;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductId() {
        return ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductMrp() {
        return ProductMrp;
    }

    public String getProductSellerName() {
        return ProductSellerName;
    }

    public String getProductShippingCharge() {
        return ProductShippingCharge;
    }

    public String getImgNameDB() {
        return imgNameDB;
    }

    public void setImgNameDB(String imgNameDB) {
        this.imgNameDB = imgNameDB;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }
}
