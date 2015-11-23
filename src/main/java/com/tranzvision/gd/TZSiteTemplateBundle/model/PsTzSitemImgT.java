package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemImgT extends PsTzSitemImgTKey {
    private Integer tzImgXh;

    private String tzImgName;

    private String tzImgView;

    public Integer getTzImgXh() {
        return tzImgXh;
    }

    public void setTzImgXh(Integer tzImgXh) {
        this.tzImgXh = tzImgXh;
    }

    public String getTzImgName() {
        return tzImgName;
    }

    public void setTzImgName(String tzImgName) {
        this.tzImgName = tzImgName == null ? null : tzImgName.trim();
    }

    public String getTzImgView() {
        return tzImgView;
    }

    public void setTzImgView(String tzImgView) {
        this.tzImgView = tzImgView == null ? null : tzImgView.trim();
    }
}