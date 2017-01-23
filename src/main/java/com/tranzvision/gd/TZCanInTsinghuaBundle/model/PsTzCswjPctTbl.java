package com.tranzvision.gd.TZCanInTsinghuaBundle.model;

public class PsTzCswjPctTbl extends PsTzCswjPctTblKey {
    private Integer tzULimit;

    private Integer tzLLimit;

    private Integer tzHistoryVal;

    private Integer tzCuryearVal;

    private Integer tzOrder;

    private String tzXxxkxzMs;

    public Integer getTzULimit() {
        return tzULimit;
    }

    public void setTzULimit(Integer tzULimit) {
        this.tzULimit = tzULimit;
    }

    public Integer getTzLLimit() {
        return tzLLimit;
    }

    public void setTzLLimit(Integer tzLLimit) {
        this.tzLLimit = tzLLimit;
    }

    public Integer getTzHistoryVal() {
        return tzHistoryVal;
    }

    public void setTzHistoryVal(Integer tzHistoryVal) {
        this.tzHistoryVal = tzHistoryVal;
    }

    public Integer getTzCuryearVal() {
        return tzCuryearVal;
    }

    public void setTzCuryearVal(Integer tzCuryearVal) {
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