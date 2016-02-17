package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsBmlcTWithBLOBs extends PsTzClsBmlcT {
    private String tzDefContent;

    private String tzTmpContent;

    public String getTzDefContent() {
        return tzDefContent;
    }

    public void setTzDefContent(String tzDefContent) {
        this.tzDefContent = tzDefContent == null ? null : tzDefContent.trim();
    }

    public String getTzTmpContent() {
        return tzTmpContent;
    }

    public void setTzTmpContent(String tzTmpContent) {
        this.tzTmpContent = tzTmpContent == null ? null : tzTmpContent.trim();
    }
}