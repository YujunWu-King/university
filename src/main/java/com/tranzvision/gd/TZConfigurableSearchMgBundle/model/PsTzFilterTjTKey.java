package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFilterTjTKey {
    private Integer tzComId;

    private String tzPageId;

    private String tzViewName;

    private Integer tzSeqnum;

    private Integer tzSqunum1;

    public Integer getTzComId() {
        return tzComId;
    }

    public void setTzComId(Integer tzComId) {
        this.tzComId = tzComId;
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

    public Integer getTzSqunum1() {
        return tzSqunum1;
    }

    public void setTzSqunum1(Integer tzSqunum1) {
        this.tzSqunum1 = tzSqunum1;
    }
}