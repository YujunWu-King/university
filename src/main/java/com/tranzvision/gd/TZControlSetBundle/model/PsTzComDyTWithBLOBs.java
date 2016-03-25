package com.tranzvision.gd.TZControlSetBundle.model;

public class PsTzComDyTWithBLOBs extends PsTzComDyT {
    private String tzComIconlj;

    private String tzComExphtml;

    public String getTzComIconlj() {
        return tzComIconlj;
    }

    public void setTzComIconlj(String tzComIconlj) {
        this.tzComIconlj = tzComIconlj == null ? null : tzComIconlj.trim();
    }

    public String getTzComExphtml() {
        return tzComExphtml;
    }

    public void setTzComExphtml(String tzComExphtml) {
        this.tzComExphtml = tzComExphtml == null ? null : tzComExphtml.trim();
    }
}