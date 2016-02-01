package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

public class PsTzAppCompTbl extends PsTzAppCompTblKey {
    private String tzHasComplete;

    public String getTzHasComplete() {
        return tzHasComplete;
    }

    public void setTzHasComplete(String tzHasComplete) {
        this.tzHasComplete = tzHasComplete == null ? null : tzHasComplete.trim();
    }
}