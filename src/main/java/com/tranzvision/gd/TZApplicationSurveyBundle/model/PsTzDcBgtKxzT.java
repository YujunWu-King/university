package com.tranzvision.gd.TZApplicationSurveyBundle.model;

import java.math.BigDecimal;

public class PsTzDcBgtKxzT extends PsTzDcBgtKxzTKey {
    private Integer tzOrder;

    private String tzOptName;

    private BigDecimal tzWeight;

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

    public BigDecimal getTzWeight() {
        return tzWeight;
    }

    public void setTzWeight(BigDecimal tzWeight) {
        this.tzWeight = tzWeight;
    }
}