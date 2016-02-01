package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

public class PsTzAppHiddenT extends PsTzAppHiddenTKey {
    private String tzIsHidden;

    public String getTzIsHidden() {
        return tzIsHidden;
    }

    public void setTzIsHidden(String tzIsHidden) {
        this.tzIsHidden = tzIsHidden == null ? null : tzIsHidden.trim();
    }
}