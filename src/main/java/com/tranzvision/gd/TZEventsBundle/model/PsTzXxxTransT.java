package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzXxxTransT extends PsTzXxxTransTKey {
    private Integer tzPxXh;

    private String tzXxxTransName;

    public Integer getTzPxXh() {
        return tzPxXh;
    }

    public void setTzPxXh(Integer tzPxXh) {
        this.tzPxXh = tzPxXh;
    }

    public String getTzXxxTransName() {
        return tzXxxTransName;
    }

    public void setTzXxxTransName(String tzXxxTransName) {
        this.tzXxxTransName = tzXxxTransName == null ? null : tzXxxTransName.trim();
    }
}