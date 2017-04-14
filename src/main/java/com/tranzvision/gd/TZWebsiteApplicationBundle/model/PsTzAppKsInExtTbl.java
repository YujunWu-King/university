package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

public class PsTzAppKsInExtTbl {
    private String tzOprid;

    private String tzAppMajorName;

    private String tzVolterYear;

    private String tzSelfEmpFlg;

    public String getTzOprid() {
        return tzOprid;
    }

    public void setTzOprid(String tzOprid) {
        this.tzOprid = tzOprid == null ? null : tzOprid.trim();
    }

    public String getTzAppMajorName() {
        return tzAppMajorName;
    }

    public void setTzAppMajorName(String tzAppMajorName) {
        this.tzAppMajorName = tzAppMajorName == null ? null : tzAppMajorName.trim();
    }

    public String getTzVolterYear() {
        return tzVolterYear;
    }

    public void setTzVolterYear(String tzVolterYear) {
        this.tzVolterYear = tzVolterYear == null ? null : tzVolterYear.trim();
    }

    public String getTzSelfEmpFlg() {
        return tzSelfEmpFlg;
    }

    public void setTzSelfEmpFlg(String tzSelfEmpFlg) {
        this.tzSelfEmpFlg = tzSelfEmpFlg == null ? null : tzSelfEmpFlg.trim();
    }
}