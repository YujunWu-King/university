package com.tranzvision.gd.TZCanInTsinghuaBundle.model;

public class PsTzCswjPctTbl extends PsTzCswjPctTblKey {
    private Float tzULimit;

    private Float tzLLimit;

    private Float tzHistoryVal;

    private Float tzCuryearVal;

    private Integer tzOrder;

    private String tzXxxkxzMs;

    public Float getTzULimit() {
        return tzULimit;
    }

    public void setTzULimit(Float tzULimit) {
        this.tzULimit = tzULimit;
    }

    public Float getTzLLimit() {
        return tzLLimit;
    }

    public void setTzLLimit(Float tzLLimit) {
        this.tzLLimit = tzLLimit;
    }

    public Float getTzHistoryVal() {
        return tzHistoryVal;
    }

    public void setTzHistoryVal(Float tzHistoryVal) {
        this.tzHistoryVal = tzHistoryVal;
    }

    public Float getTzCuryearVal() {
        return tzCuryearVal;
    }

    public void setTzCuryearVal(Float tzCuryearVal) {
        this.tzCuryearVal = tzCuryearVal;
    }

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }

    public String getTzXxxkxzMs() {
        return tzXxxkxzMs;
    }

    public void setTzXxxkxzMs(String tzXxxkxzMs) {
        this.tzXxxkxzMs = tzXxxkxzMs == null ? null : tzXxxkxzMs.trim();
    }
}