package com.tranzvision.gd.TZComRegMgBundle.model;

import java.util.Date;

public class PsTzAqPagzcTbl extends PsTzAqPagzcTblKey {
    private String tzPageMc;

    private Short tzPageXh;

    private String tzPageIswburl;

    private String tzPageKhdjs;

    private String tzPageFwdcls;

    private String tzPageMrsy;

    private String tzPageNewwin;

    private String tzPageRefcode;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzPageWburl;

    public String getTzPageMc() {
        return tzPageMc;
    }

    public void setTzPageMc(String tzPageMc) {
        this.tzPageMc = tzPageMc == null ? null : tzPageMc.trim();
    }

    public Short getTzPageXh() {
        return tzPageXh;
    }

    public void setTzPageXh(Short tzPageXh) {
        this.tzPageXh = tzPageXh;
    }

    public String getTzPageIswburl() {
        return tzPageIswburl;
    }

    public void setTzPageIswburl(String tzPageIswburl) {
        this.tzPageIswburl = tzPageIswburl == null ? null : tzPageIswburl.trim();
    }

    public String getTzPageKhdjs() {
        return tzPageKhdjs;
    }

    public void setTzPageKhdjs(String tzPageKhdjs) {
        this.tzPageKhdjs = tzPageKhdjs == null ? null : tzPageKhdjs.trim();
    }

    public String getTzPageFwdcls() {
        return tzPageFwdcls;
    }

    public void setTzPageFwdcls(String tzPageFwdcls) {
        this.tzPageFwdcls = tzPageFwdcls == null ? null : tzPageFwdcls.trim();
    }

    public String getTzPageMrsy() {
        return tzPageMrsy;
    }

    public void setTzPageMrsy(String tzPageMrsy) {
        this.tzPageMrsy = tzPageMrsy == null ? null : tzPageMrsy.trim();
    }

    public String getTzPageNewwin() {
        return tzPageNewwin;
    }

    public void setTzPageNewwin(String tzPageNewwin) {
        this.tzPageNewwin = tzPageNewwin == null ? null : tzPageNewwin.trim();
    }

    public String getTzPageRefcode() {
        return tzPageRefcode;
    }

    public void setTzPageRefcode(String tzPageRefcode) {
        this.tzPageRefcode = tzPageRefcode == null ? null : tzPageRefcode.trim();
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

    public String getTzPageWburl() {
        return tzPageWburl;
    }

    public void setTzPageWburl(String tzPageWburl) {
        this.tzPageWburl = tzPageWburl == null ? null : tzPageWburl.trim();
    }
}