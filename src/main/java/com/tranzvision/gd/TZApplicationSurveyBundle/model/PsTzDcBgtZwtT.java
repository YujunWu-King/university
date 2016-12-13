package com.tranzvision.gd.TZApplicationSurveyBundle.model;

import java.math.BigDecimal;

public class PsTzDcBgtZwtT extends PsTzDcBgtZwtTKey {
    private Integer tzOrder;

    private String tzQuName;

    private String tzShortName;

    private BigDecimal tzWeight;

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }

    public String getTzQuName() {
        return tzQuName;
    }

    public void setTzQuName(String tzQuName) {
        this.tzQuName = tzQuName == null ? null : tzQuName.trim();
    }

    public String getTzShortName() {
        return tzShortName;
    }

    public void setTzShortName(String tzShortName) {
        this.tzShortName = tzShortName == null ? null : tzShortName.trim();
    }

    public BigDecimal getTzWeight() {
        return tzWeight;
    }

    public void setTzWeight(BigDecimal tzWeight) {
        this.tzWeight = tzWeight;
    }
}