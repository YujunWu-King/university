package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzArtRecTblWithBLOBs extends PsTzArtRecTbl {
    private String tzArtTitleStyle;

    private String tzArtConent;

    private String tzOutArtUrl;

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
}