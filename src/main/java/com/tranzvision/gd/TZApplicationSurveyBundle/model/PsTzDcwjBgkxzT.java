package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcwjBgkxzT extends PsTzDcwjBgkxzTKey {
    private Integer tzOrder;

    private String tzOptName;

    private String tzWeight;

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }

    public String getTzOptName() {
        return tzOptName;
    }

    public void setTzOptName(String tzOptName) {
        this.tzOptName = tzOptName == null ? null : tzOptName.trim();
    }

    public String getTzWeight() {
        return tzWeight;
    }

    public void setTzWeight(String tzWeight) {
        this.tzWeight = tzWeight == null ? null : tzWeight.trim();
    }
}