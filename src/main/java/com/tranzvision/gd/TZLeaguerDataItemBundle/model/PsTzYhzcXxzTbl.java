package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

public class PsTzYhzcXxzTbl extends PsTzYhzcXxzTblKey {
    private String tzOptValue;

    private String tzSelectFlg;

    private Integer tzOrder;

    public String getTzOptValue() {
        return tzOptValue;
    }

    public void setTzOptValue(String tzOptValue) {
        this.tzOptValue = tzOptValue == null ? null : tzOptValue.trim();
    }

    public String getTzSelectFlg() {
        return tzSelectFlg;
    }

    public void setTzSelectFlg(String tzSelectFlg) {
        this.tzSelectFlg = tzSelectFlg == null ? null : tzSelectFlg.trim();
    }

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }
}