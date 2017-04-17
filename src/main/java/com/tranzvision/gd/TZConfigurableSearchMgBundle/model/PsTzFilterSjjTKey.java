package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFilterSjjTKey {
    private String tzComId;

    private String tzPageId;

    private String tzViewName;

    private Integer tzSeqnum;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzPageId() {
        return tzPageId;
    }

    public void setTzPageId(String tzPageId) {
        this.tzPageId = tzPageId == null ? null : tzPageId.trim();
    }

    public String getTzViewName() {
        return tzViewName;
    }

    public void setTzViewName(String tzViewName) {
        this.tzViewName = tzViewName == null ? null : tzViewName.trim();
    }

    public Integer getTzSeqnum() {
        return tzSeqnum;
    }

    public void setTzSeqnum(Integer tzSeqnum) {
        this.tzSeqnum = tzSeqnum;
    }
}