package com.tranzvision.gd.TZScoolTypeListManage.model;

import java.util.Date;

public class PsTzShoolLogT {
    private String tzSchoolTypeid;

    private String tzSchoolTypename;

    private String tzSchoolDec;

    private String tzSchoolFlag;

    private Date tzSchoolAddtime;

    public String getTzSchoolTypeid() {
        return tzSchoolTypeid;
    }

    public void setTzSchoolTypeid(String tzSchoolTypeid) {
        this.tzSchoolTypeid = tzSchoolTypeid == null ? null : tzSchoolTypeid.trim();
    }

    public String getTzSchoolTypename() {
        return tzSchoolTypename;
    }

    public void setTzSchoolTypename(String tzSchoolTypename) {
        this.tzSchoolTypename = tzSchoolTypename == null ? null : tzSchoolTypename.trim();
    }

    public String getTzSchoolDec() {
        return tzSchoolDec;
    }

    public void setTzSchoolDec(String tzSchoolDec) {
        this.tzSchoolDec = tzSchoolDec == null ? null : tzSchoolDec.trim();
    }

    public String getTzSchoolFlag() {
        return tzSchoolFlag;
    }

    public void setTzSchoolFlag(String tzSchoolFlag) {
        this.tzSchoolFlag = tzSchoolFlag == null ? null : tzSchoolFlag.trim();
    }

    public Date getTzSchoolAddtime() {
        return tzSchoolAddtime;
    }

    public void setTzSchoolAddtime(Date tzSchoolAddtime) {
        this.tzSchoolAddtime = tzSchoolAddtime;
    }
}