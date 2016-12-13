package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcwjBgzwtT extends PsTzDcwjBgzwtTKey {
    private String tzQuName;

    private String tzShortName;

    private Integer tzOrder;

    private Integer tzWeight;

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

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }

    public Integer getTzWeight() {
        return tzWeight;
    }

    public void setTzWeight(Integer tzWeight) {
        this.tzWeight = tzWeight;
    }
}