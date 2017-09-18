package com.tranzvision.gd.TZWeChatMaterialBundle.model;

public class PsTzWxTwlTblKey {
    private String tzJgId;

    private String tzWxAppid;

    private String tzXh;

    private String tzSeqnum;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzWxAppid() {
        return tzWxAppid;
    }

    public void setTzWxAppid(String tzWxAppid) {
        this.tzWxAppid = tzWxAppid == null ? null : tzWxAppid.trim();
    }

    public String getTzXh() {
        return tzXh;
    }

    public void setTzXh(String tzXh) {
        this.tzXh = tzXh == null ? null : tzXh.trim();
    }

    public String getTzSeqnum() {
        return tzSeqnum;
    }

    public void setTzSeqnum(String tzSeqnum) {
        this.tzSeqnum = tzSeqnum == null ? null : tzSeqnum.trim();
    }
}