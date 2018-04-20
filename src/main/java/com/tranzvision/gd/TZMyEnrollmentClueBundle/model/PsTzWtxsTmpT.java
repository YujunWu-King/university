package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

import java.util.Date;

public class PsTzWtxsTmpT extends PsTzWtxsTmpTKey {
    private String tzJgId;

    private String tzJyCz;

    private Integer tzOrderNum;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private String tzLeadDescr;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzJyCz() {
        return tzJyCz;
    }

    public void setTzJyCz(String tzJyCz) {
        this.tzJyCz = tzJyCz == null ? null : tzJyCz.trim();
    }

    public Integer getTzOrderNum() {
        return tzOrderNum;
    }

    public void setTzOrderNum(Integer tzOrderNum) {
        this.tzOrderNum = tzOrderNum;
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

    public String getTzLeadDescr() {
        return tzLeadDescr;
    }

    public void setTzLeadDescr(String tzLeadDescr) {
        this.tzLeadDescr = tzLeadDescr == null ? null : tzLeadDescr.trim();
    }
}