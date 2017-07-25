package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFltdstRoleTKey {
    private String tzComId;

    private String tzPageId;

    private String tzViewName;

    private Integer tzFltdstOrder;

    private String rolename;

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

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }
}