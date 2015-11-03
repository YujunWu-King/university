package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFilterYsfT extends PsTzFilterYsfTKey {
    private String tzFilterBdyQy;

    private String tzIsDefOprt;

    public String getTzFilterBdyQy() {
        return tzFilterBdyQy;
    }

    public void setTzFilterBdyQy(String tzFilterBdyQy) {
        this.tzFilterBdyQy = tzFilterBdyQy == null ? null : tzFilterBdyQy.trim();
    }

    public String getTzIsDefOprt() {
        return tzIsDefOprt;
    }

    public void setTzIsDefOprt(String tzIsDefOprt) {
        this.tzIsDefOprt = tzIsDefOprt == null ? null : tzIsDefOprt.trim();
    }
}