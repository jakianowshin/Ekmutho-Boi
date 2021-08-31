package com.example.ekmuthoboi;

import android.graphics.Bitmap;

public class BookList {
    private String productId,productName,price,image;


    public BookList(String productId, String productName, String price, String image) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.image = image;
    }

    public BookList() {

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
