package com.tranzvision.gd.TZEmailParameterBundle.model;

import java.util.Date;

public class PsTzEmlsDefTbl {
    private String tzEmlservId;

    private String tzJgId;

    private String tzEmlAddr100;

    private String tzIsDefault;

    private String tzChsSname;

    private String tzSmtpAddr;

    private String tzEmlAlias;

    private String tzUsrName;

    private String tzUsrPwd;

    private String tzDescr254;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzEmlservId() {
        return tzEmlservId;
    }

    public void setTzEmlservId(String tzEmlservId) {
        this.tzEmlservId = tzEmlservId == null ? null : tzEmlservId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzEmlAddr100() {
        return tzEmlAddr100;
    }

    public void setTzEmlAddr100(String tzEmlAddr100) {
        this.tzEmlAddr100 = tzEmlAddr100 == null ? null : tzEmlAddr100.trim();
    }

    public String getTzIsDefault() {
        return tzIsDefault;
    }

    public void setTzIsDefault(String tzIsDefault) {
        this.tzIsDefault = tzIsDefault == null ? null : tzIsDefault.trim();
    }

    public String getTzChsSname() {
        return tzChsSname;
    }

    public void setTzChsSname(String tzChsSname) {
        this.tzChsSname = tzChsSname == null ? null : tzChsSname.trim();
    }

    public String getTzSmtpAddr() {
        return tzSmtpAddr;
    }

    public void setTzSmtpAddr(String tzSmtpAddr) {
        this.tzSmtpAddr = tzSmtpAddr == null ? null : tzSmtpAddr.trim();
    }

    public String getTzEmlAlias() {
        return tzEmlAlias;
    }

    public void setTzEmlAlias(String tzEmlAlias) {
        this.tzEmlAlias = tzEmlAlias == null ? null : tzEmlAlias.trim();
    }

    public String getTzUsrName() {
        return tzUsrName;
    }

    public void setTzUsrName(String tzUsrName) {
        this.tzUsrName = tzUsrName == null ? null : tzUsrName.trim();
    }

    public String getTzUsrPwd() {
        return tzUsrPwd;
    }

    public void setTzUsrPwd(String tzUsrPwd) {
        this.tzUsrPwd = tzUsrPwd == null ? null : tzUsrPwd.trim();
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