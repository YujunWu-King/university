package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

import java.util.Date;

public class PsTzXsxsLogT {
    private Integer tzOperateId;

    private String tzLeadId;

    private String tzLeadStatus2;

    private String tzLeadStatus1;

    private String tzOperateDesc;

    private String tzDemo;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public Integer getTzOperateId() {
        return tzOperateId;
    }

    public void setTzOperateId(Integer tzOperateId) {
        this.tzOperateId = tzOperateId;
    }

    public String getTzLeadId() {
        return tzLeadId;
    }

    public void setTzLeadId(String tzLeadId) {
        this.tzLeadId = tzLeadId == null ? null : tzLeadId.trim();
    }

    public String getTzLeadStatus2() {
        return tzLeadStatus2;
    }

    public void setTzLeadStatus2(String tzLeadStatus2) {
        this.tzLeadStatus2 = tzLeadStatus2 == null ? null : tzLeadStatus2.trim();
    }

    public String getTzLeadStatus1() {
        return tzLeadStatus1;
    }

    public void setTzLeadStatus1(String tzLeadStatus1) {
        this.tzLeadStatus1 = tzLeadStatus1 == null ? null : tzLeadStatus1.trim();
    }

    public String getTzOperateDesc() {
        return tzOperateDesc;
    }

    public void setTzOperateDesc(String tzOperateDesc) {
        this.tzOperateDesc = tzOperateDesc == null ? null : tzOperateDesc.trim();
    }

    public String getTzDemo() {
        return tzDemo;
    }

    public void setTzDemo(String tzDemo) {
        this.tzDemo = tzDemo == null ? null : tzDemo.trim();
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
}