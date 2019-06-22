package com.dolphintechno.dolphindigitalflux.model;

public class SpecifiedProductDetail {

    String productId;
    String productName;
    String longInfo;
    String mrp;
    String brandId;
    String sellerName;
    String shippingCharge;
    String imageNameOne;
    String imageNameTwo;
    String imageNameThree;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLongInfo(String longInfo) {
        this.longInfo = longInfo;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }


    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getLongInfo() {
        return longInfo;
    }

    public String getMrp() {
        return mrp;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public String getImageNameOne() {
        return imageNameOne;
    }

    public void setImageNameOne(String imageNameOne) {
        this.imageNameOne = imageNameOne;
    }

    public String getImageNameTwo() {
        return imageNameTwo;
    }

    public void setImageNameTwo(String imageNameTwo) {
        this.imageNameTwo = imageNameTwo;
    }

    public String getImageNameThree() {
        return imageNameThree;
    }

    public void setImageNameThree(String imageNameThree) {
        this.imageNameThree = imageNameThree;
    }
}
