package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFltDstConTKey {
    private String tzComId;

    private String tzPageId;

    private String tzViewName;

    private Integer tzFltdstOrder;

    private Integer tzFltdstCOrder;

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

    public Integer getTzFltdstOrder() {
        return tzFltdstOrder;
    }

    public void setTzFltdstOrder(Integer tzFltdstOrder) {
        this.tzFltdstOrder = tzFltdstOrder;
    }

    public Integer getTzFltdstCOrder() {
        return tzFltdstCOrder;
    }

    public void setTzFltdstCOrder(Integer tzFltdstCOrder) {
        this.tzFltdstCOrder = tzFltdstCOrder;
    }
}