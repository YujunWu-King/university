package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

import java.util.Date;

public class PsTzLxbgDfnT {
    private Long tzCallreportId;

    private String tzCallSubject;

    private Date tzCallDate;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private String tzCallInfo;

    public Long getTzCallreportId() {
        return tzCallreportId;
    }

    public void setTzCallreportId(Long tzCallreportId) {
        this.tzCallreportId = tzCallreportId;
    }

    public String getTzCallSubject() {
        return tzCallSubject;
    }

    public void setTzCallSubject(String tzCallSubject) {
        this.tzCallSubject = tzCallSubject == null ? null : tzCallSubject.trim();
    }

    public Date getTzCallDate() {
        return tzCallDate;
    }

    public void setTzCallDate(Date tzCallDate) {
        this.tzCallDate = tzCallDate;
    }

    public Date getRowAddedDttm() {
        return rowAddedDttm;
    }

    public void setRowAddedDttm(Date rowAddedDttm) {
        this.rowAddedDttm = rowAddedDttm;
    }

    public String getRowAddedOprid() {
        return rowAddedOprid;
    }

    public void setRowAddedOprid(String rowAddedOprid) {
        this.rowAddedOprid = rowAddedOprid == null ? null : rowAddedOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public String getTzCallInfo() {
        return tzCallInfo;
    }

    public void setTzCallInfo(String tzCallInfo) {
        this.tzCallInfo = tzCallInfo == null ? null : tzCallInfo.trim();
    }
}