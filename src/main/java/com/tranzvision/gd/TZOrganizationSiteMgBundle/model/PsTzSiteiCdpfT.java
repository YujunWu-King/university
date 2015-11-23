package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiCdpfT extends PsTzSiteiCdpfTKey {
    private String tzSkinState;

    private String tzSkinName;

    private String tzTypeImg;

    private String tzNowImg;

    public String getTzSkinState() {
        return tzSkinState;
    }

    public void setTzSkinState(String tzSkinState) {
        this.tzSkinState = tzSkinState == null ? null : tzSkinState.trim();
    }

    public String getTzSkinName() {
        return tzSkinName;
    }

    public void setTzSkinName(String tzSkinName) {
        this.tzSkinName = tzSkinName == null ? null : tzSkinName.trim();
    }

    public String getTzTypeImg() {
        return tzTypeImg;
    }

    public void setTzTypeImg(String tzTypeImg) {
        this.tzTypeImg = tzTypeImg == null ? null : tzTypeImg.trim();
    }

    public String getTzNowImg() {
        return tzNowImg;
    }

    public void setTzNowImg(String tzNowImg) {
        this.tzNowImg = tzNowImg == null ? null : tzNowImg.trim();
    }
}