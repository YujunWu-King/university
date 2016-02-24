package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

import java.util.Date;

public class PsTzAppproRstT extends PsTzAppproRstTKey {
    private String tzAppproHfBh;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzAppproRst;

    public String getTzAppproHfBh() {
        return tzAppproHfBh;
    }

    public void setTzAppproHfBh(String tzAppproHfBh) {
        this.tzAppproHfBh = tzAppproHfBh == null ? null : tzAppproHfBh.trim();
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

    public String getTzAppproRst() {
        return tzAppproRst;
    }

    public void setTzAppproRst(String tzAppproRst) {
        this.tzAppproRst = tzAppproRst == null ? null : tzAppproRst.trim();
    }
}