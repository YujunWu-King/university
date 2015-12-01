package com.tranzvision.gd.TZTemplateParameterBundle.model;

import java.util.Date;

public class PsTzExParaTbl {
    private String tzParaId;

    private String tzParaCname;

    private String tzParaType;

    private String tzDescr254;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzParaId() {
        return tzParaId;
    }

    public void setTzParaId(String tzParaId) {
        this.tzParaId = tzParaId == null ? null : tzParaId.trim();
    }

    public String getTzParaCname() {
        return tzParaCname;
    }

    public void setTzParaCname(String tzParaCname) {
        this.tzParaCname = tzParaCname == null ? null : tzParaCname.trim();
    }

    public String getTzParaType() {
        return tzParaType;
    }

    public void setTzParaType(String tzParaType) {
        this.tzParaType = tzParaType == null ? null : tzParaType.trim();
    }

    public String getTzDescr254() {
        return tzDescr254;
    }

    public void setTzDescr254(String tzDescr254) {
        this.tzDescr254 = tzDescr254 == null ? null : tzDescr254.trim();
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

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }

    public Date getSyncdttm() {
        return syncdttm;
    }

    public void setSyncdttm(Date syncdttm) {
        this.syncdttm = syncdttm;
    }
}