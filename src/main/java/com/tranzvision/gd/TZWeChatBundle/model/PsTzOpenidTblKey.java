package com.tranzvision.gd.TZWeChatBundle.model;

public class PsTzOpenidTblKey {
    private String openid;

    private String tzJgId;

    private String tzSiteiId;

    private String tzDlzhId;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzDlzhId() {
        return tzDlzhId;
    }

    public void setTzDlzhId(String tzDlzhId) {
        this.tzDlzhId = tzDlzhId == null ? null : tzDlzhId.trim();
    }
}