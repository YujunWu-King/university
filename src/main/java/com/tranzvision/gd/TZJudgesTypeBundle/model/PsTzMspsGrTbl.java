package com.tranzvision.gd.TZJudgesTypeBundle.model;

public class PsTzMspsGrTbl {
    private String tzClpsGrId;

    private String tzJgId;

    private String tzClpsGrName;
    
    private String tzRolename;

    private String tzDescr;

    public String getTzClpsGrId() {
        return tzClpsGrId;
    }

    public void setTzClpsGrId(String tzClpsGrId) {
        this.tzClpsGrId = tzClpsGrId == null ? null : tzClpsGrId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzClpsGrName() {
        return tzClpsGrName;
    }

    public void setTzClpsGrName(String tzClpsGrName) {
        this.tzClpsGrName = tzClpsGrName == null ? null : tzClpsGrName.trim();
    }

	public String getTzRolename() {
		return tzRolename;
	}

	public void setTzRolename(String tzRolename) {
		this.tzRolename = tzRolename;
	}

	public String getTzDescr() {
		return tzDescr;
	}

	public void setTzDescr(String tzDescr) {
		this.tzDescr = tzDescr;
	}
    
}