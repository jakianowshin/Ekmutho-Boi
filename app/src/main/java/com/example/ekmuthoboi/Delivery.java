package com.example.ekmuthoboi;

public class Delivery {
    private String delId,delType,delAmount;


    public Delivery(String delId, String delType, String delAmount) {
        this.delId = delId;
        this.delType = delType;
        this.delAmount = delAmount;
    }

    public String getDelId() {
        return delId;
    }

    public void setDelId(String delId) {
        this.delId = delId;
    }

    public String getDelType() {
        return delType;
    }

    public void setDelType(String delType) {
        this.delType = delType;
    }

    public String getDelAmount() {
        return delAmount;
    }

    public void setDelAmount(String delAmount) {
        this.delAmount = delAmount;
    }
}
