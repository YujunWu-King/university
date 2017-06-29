package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFltDstFldT extends PsTzFltDstFldTKey {
    private String tzFltdstFld;

    private String tzFltdstSrchRec;

    private String tzFltdstDesc;

    private String tzFltdstStatus;

    private String tzFltdstDefault;

    public String getTzFltdstFld() {
        return tzFltdstFld;
    }

    public void setTzFltdstFld(String tzFltdstFld) {
        this.tzFltdstFld = tzFltdstFld == null ? null : tzFltdstFld.trim();
    }

    public String getTzFltdstSrchRec() {
        return tzFltdstSrchRec;
    }

    public void setTzFltdstSrchRec(String tzFltdstSrchRec) {
        this.tzFltdstSrchRec = tzFltdstSrchRec == null ? null : tzFltdstSrchRec.trim();
    }

    public String getTzFltdstDesc() {
        return tzFltdstDesc;
    }

    public void setTzFltdstDesc(String tzFltdstDesc) {
        this.tzFltdstDesc = tzFltdstDesc == null ? null : tzFltdstDesc.trim();
    }

    public String getTzFltdstStatus() {
        return tzFltdstStatus;
    }

    public void setTzFltdstStatus(String tzFltdstStatus) {
        this.tzFltdstStatus = tzFltdstStatus == null ? null : tzFltdstStatus.trim();
    }

    public String getTzFltdstDefault() {
        return tzFltdstDefault;
    }

    public void setTzFltdstDefault(String tzFltdstDefault) {
        this.tzFltdstDefault = tzFltdstDefault == null ? null : tzFltdstDefault.trim();
    }
}