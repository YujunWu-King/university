package com.tranzvision.gd.TZWeChatBundle.model;

public class PsTzOpenidTbl extends PsTzOpenidTblKey {
    private String tzDelFlg;

    public String getTzDelFlg() {
        return tzDelFlg;
    }

    public void setTzDelFlg(String tzDelFlg) {
        this.tzDelFlg = tzDelFlg == null ? null : tzDelFlg.trim();
    }
}