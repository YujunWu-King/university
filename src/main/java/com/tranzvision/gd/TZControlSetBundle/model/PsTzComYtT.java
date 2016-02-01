package com.tranzvision.gd.TZControlSetBundle.model;

public class PsTzComYtT extends PsTzComYtTKey {
    private String tzComLxId;

    private String tzQyBz;

    private Integer tzOrder;

    public String getTzComLxId() {
        return tzComLxId;
    }

    public void setTzComLxId(String tzComLxId) {
        this.tzComLxId = tzComLxId == null ? null : tzComLxId.trim();
    }

    public String getTzQyBz() {
        return tzQyBz;
    }

    public void setTzQyBz(String tzQyBz) {
        this.tzQyBz = tzQyBz == null ? null : tzQyBz.trim();
    }

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }
}