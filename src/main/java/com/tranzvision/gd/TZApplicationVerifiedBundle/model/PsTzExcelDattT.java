package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzExcelDattT {
    private Integer processinstance;

    private String tzSysfileName;

    private String tzFileName;

    private String tzCfLj;

    private String tzFjRecName;

    private String tzFwqFwlj;

    public Integer getProcessinstance() {
        return processinstance;
    }

    public void setProcessinstance(Integer processinstance) {
        this.processinstance = processinstance;
    }

    public String getTzSysfileName() {
        return tzSysfileName;
    }

    public void setTzSysfileName(String tzSysfileName) {
        this.tzSysfileName = tzSysfileName == null ? null : tzSysfileName.trim();
    }

    public String getTzFileName() {
        return tzFileName;
    }

    public void setTzFileName(String tzFileName) {
        this.tzFileName = tzFileName == null ? null : tzFileName.trim();
    }

    public String getTzCfLj() {
        return tzCfLj;
    }

    public void setTzCfLj(String tzCfLj) {
        this.tzCfLj = tzCfLj == null ? null : tzCfLj.trim();
    }

    public String getTzFjRecName() {
        return tzFjRecName;
    }

    public void setTzFjRecName(String tzFjRecName) {
        this.tzFjRecName = tzFjRecName == null ? null : tzFjRecName.trim();
    }

    public String getTzFwqFwlj() {
        return tzFwqFwlj;
    }

    public void setTzFwqFwlj(String tzFwqFwlj) {
        this.tzFwqFwlj = tzFwqFwlj == null ? null : tzFwqFwlj.trim();
    }
}