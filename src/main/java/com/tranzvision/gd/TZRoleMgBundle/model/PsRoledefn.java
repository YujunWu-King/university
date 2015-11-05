package com.tranzvision.gd.TZRoleMgBundle.model;

import java.util.Date;

public class PsRoledefn {
    private String rolename;

    private Integer version;

    private String roletype;

    private String descr;

    private String qryname;

    private String rolestatus;

    private String recname;

    private String fieldname;

    private String pcEventType;

    private String qrynameSec;

    private String pcFunctionName;

    private String rolePcodeRuleOn;

    private String roleQueryRuleOn;

    private String ldapRuleOn;

    private String allownotify;

    private String allowlookup;

    private Date lastupddttm;

    private String lastupdoprid;

    private String descrlong;

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype == null ? null : roletype.trim();
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr == null ? null : descr.trim();
    }

    public String getQryname() {
        return qryname;
    }

    public void setQryname(String qryname) {
        this.qryname = qryname == null ? null : qryname.trim();
    }

    public String getRolestatus() {
        return rolestatus;
    }

    public void setRolestatus(String rolestatus) {
        this.rolestatus = rolestatus == null ? null : rolestatus.trim();
    }

    public String getRecname() {
        return recname;
    }

    public void setRecname(String recname) {
        this.recname = recname == null ? null : recname.trim();
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname == null ? null : fieldname.trim();
    }

    public String getPcEventType() {
        return pcEventType;
    }

    public void setPcEventType(String pcEventType) {
        this.pcEventType = pcEventType == null ? null : pcEventType.trim();
    }

    public String getQrynameSec() {
        return qrynameSec;
    }

    public void setQrynameSec(String qrynameSec) {
        this.qrynameSec = qrynameSec == null ? null : qrynameSec.trim();
    }

    public String getPcFunctionName() {
        return pcFunctionName;
    }

    public void setPcFunctionName(String pcFunctionName) {
        this.pcFunctionName = pcFunctionName == null ? null : pcFunctionName.trim();
    }

    public String getRolePcodeRuleOn() {
        return rolePcodeRuleOn;
    }

    public void setRolePcodeRuleOn(String rolePcodeRuleOn) {
        this.rolePcodeRuleOn = rolePcodeRuleOn == null ? null : rolePcodeRuleOn.trim();
    }

    public String getRoleQueryRuleOn() {
        return roleQueryRuleOn;
    }

    public void setRoleQueryRuleOn(String roleQueryRuleOn) {
        this.roleQueryRuleOn = roleQueryRuleOn == null ? null : roleQueryRuleOn.trim();
    }

    public String getLdapRuleOn() {
        return ldapRuleOn;
    }

    public void setLdapRuleOn(String ldapRuleOn) {
        this.ldapRuleOn = ldapRuleOn == null ? null : ldapRuleOn.trim();
    }

    public String getAllownotify() {
        return allownotify;
    }

    public void setAllownotify(String allownotify) {
        this.allownotify = allownotify == null ? null : allownotify.trim();
    }

    public String getAllowlookup() {
        return allowlookup;
    }

    public void setAllowlookup(String allowlookup) {
        this.allowlookup = allowlookup == null ? null : allowlookup.trim();
    }

    public Date getLastupddttm() {
        return lastupddttm;
    }

    public void setLastupddttm(Date lastupddttm) {
        this.lastupddttm = lastupddttm;
    }

    public String getLastupdoprid() {
        return lastupdoprid;
    }

    public void setLastupdoprid(String lastupdoprid) {
        this.lastupdoprid = lastupdoprid == null ? null : lastupdoprid.trim();
    }

    public String getDescrlong() {
        return descrlong;
    }

    public void setDescrlong(String descrlong) {
        this.descrlong = descrlong == null ? null : descrlong.trim();
    }
}