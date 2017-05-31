package com.tranzvision.gd.TZEmailTxTypeBundle.model;

public class PsTzTxTypeTbl {
    private String tzTxTypeId;

    private String tzTxTypeDesc;

    private String tzTxSslx;

    private String tzTxDesc;

    private String tzIsUsed;

    public String getTzTxTypeId() {
        return tzTxTypeId;
    }

    public void setTzTxTypeId(String tzTxTypeId) {
        this.tzTxTypeId = tzTxTypeId == null ? null : tzTxTypeId.trim();
    }

    public String getTzTxTypeDesc() {
        return tzTxTypeDesc;
    }

    public void setTzTxTypeDesc(String tzTxTypeDesc) {
        this.tzTxTypeDesc = tzTxTypeDesc == null ? null : tzTxTypeDesc.trim();
    }

    public String getTzTxSslx() {
        return tzTxSslx;
    }

    public void setTzTxSslx(String tzTxSslx) {
        this.tzTxSslx = tzTxSslx == null ? null : tzTxSslx.trim();
    }

    public String getTzTxDesc() {
        return tzTxDesc;
    }

    public void setTzTxDesc(String tzTxDesc) {
        this.tzTxDesc = tzTxDesc == null ? null : tzTxDesc.trim();
    }

    public String getTzIsUsed() {
        return tzIsUsed;
    }

    public void setTzIsUsed(String tzIsUsed) {
        this.tzIsUsed = tzIsUsed == null ? null : tzIsUsed.trim();
    }
}