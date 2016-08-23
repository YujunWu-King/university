package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzArtRecTblWithBLOBs extends PsTzArtRecTbl {
    private String tzArtTitleStyle;

    private String tzArtConent;

    private String tzOutArtUrl;

    private String tzLong1;

    private String tzLong2;

    private String tzLong3;

    private String tzAbout;

    public String getTzArtTitleStyle() {
        return tzArtTitleStyle;
    }

    public void setTzArtTitleStyle(String tzArtTitleStyle) {
        this.tzArtTitleStyle = tzArtTitleStyle == null ? null : tzArtTitleStyle.trim();
    }

    public String getTzArtConent() {
        return tzArtConent;
    }

    public void setTzArtConent(String tzArtConent) {
        this.tzArtConent = tzArtConent == null ? null : tzArtConent.trim();
    }

    public String getTzOutArtUrl() {
        return tzOutArtUrl;
    }

    public void setTzOutArtUrl(String tzOutArtUrl) {
        this.tzOutArtUrl = tzOutArtUrl == null ? null : tzOutArtUrl.trim();
    }

    public String getTzLong1() {
        return tzLong1;
    }

    public void setTzLong1(String tzLong1) {
        this.tzLong1 = tzLong1 == null ? null : tzLong1.trim();
    }

    public String getTzLong2() {
        return tzLong2;
    }

    public void setTzLong2(String tzLong2) {
        this.tzLong2 = tzLong2 == null ? null : tzLong2.trim();
    }

    public String getTzLong3() {
        return tzLong3;
    }

    public void setTzLong3(String tzLong3) {
        this.tzLong3 = tzLong3 == null ? null : tzLong3.trim();
    }

    public String getTzAbout() {
        return tzAbout;
    }

    public void setTzAbout(String tzAbout) {
        this.tzAbout = tzAbout == null ? null : tzAbout.trim();
    }
}