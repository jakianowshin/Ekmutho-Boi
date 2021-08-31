package com.example.ekmuthoboi;

public class OrderDetailModel {
    private String productId,productName,single_total,single_price,dis_price,dollerprice,dollerTotal,qty,orderId;

    public OrderDetailModel(String productId, String productName,
                            String single_total, String single_price, String dis_price, String dollerprice,
                            String dollerTotal, String qty, String orderId) {
        this.productId = productId;
        this.productName = productName;
        this.single_total = single_total;
        this.single_price = single_price;
        this.dis_price = dis_price;
        this.dollerprice = dollerprice;
        this.dollerTotal = dollerTotal;
        this.qty = qty;
        this.orderId = orderId;
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

    public String getSingle_total() {
        return single_total;
    }

    public void setSingle_total(String single_total) {
        this.single_total = single_total;
    }

    public String getSingle_price() {
        return single_price;
    }

    public void setSingle_price(String single_price) {
        this.single_price = single_price;
    }

    public String getDis_price() {
        return dis_price;
    }

    public void setDis_price(String dis_price) {
        this.dis_price = dis_price;
    }

    public String getDollerprice() {
        return dollerprice;
    }

    public void setDollerprice(String dollerprice) {
        this.dollerprice = dollerprice;
    }

    public String getDollerTotal() {
        return dollerTotal;
    }

    public void setDollerTotal(String dollerTotal) {
        this.dollerTotal = dollerTotal;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
