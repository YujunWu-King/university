package com.tranzvision.gd.TZCanInTsinghuaBundle.model;

public class PsTzCswjDcxTbl extends PsTzCswjDcxTblKey {
    private String tzXxxMc;

    private String tzXxxDesc;

    private Integer tzOrder;

    public String getTzXxxMc() {
        return tzXxxMc;
    }

    public void setTzXxxMc(String tzXxxMc) {
        this.tzXxxMc = tzXxxMc == null ? null : tzXxxMc.trim();
    }

    public String getTzXxxDesc() {
        return tzXxxDesc;
    }

    public void setTzXxxDesc(String tzXxxDesc) {
        this.tzXxxDesc = tzXxxDesc == null ? null : tzXxxDesc.trim();
    }

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }
}