package com.tranzvision.gd.TZApplicationProcessBundle.model;

public class PsTzAppProStpT extends PsTzAppProStpTKey {
    private Integer tzSortNum;

    private String tzAppproName;

    private String tzDefContent;

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }

    public String getTzAppproName() {
        return tzAppproName;
    }

    public void setTzAppproName(String tzAppproName) {
        this.tzAppproName = tzAppproName == null ? null : tzAppproName.trim();
    }

    public String getTzDefContent() {
        return tzDefContent;
    }

    public void setTzDefContent(String tzDefContent) {
        this.tzDefContent = tzDefContent == null ? null : tzDefContent.trim();
    }
}