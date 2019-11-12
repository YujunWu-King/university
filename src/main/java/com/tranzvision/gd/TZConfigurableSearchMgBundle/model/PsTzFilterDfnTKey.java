package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFilterDfnTKey {
    private String tzComId;

    private String tzPageId;

    private String tzViewName;
    
    private String tzAppClassName;
    
    private String tzType;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzPageId() {
        return tzPageId;
    }

    public void setTzPageId(String tzPageId) {
        this.tzPageId = tzPageId == null ? null : tzPageId.trim();
    }

    public String getTzViewName() {
        return tzViewName;
    }

    public void setTzViewName(String tzViewName) {
        this.tzViewName = tzViewName == null ? null : tzViewName.trim();
    }

	public String getTzAppClassName() {
		return tzAppClassName;
	}

	public void setTzAppClassName(String tzAppClassName) {
		this.tzAppClassName = tzAppClassName;
	}

	public String getTzType() {
		return tzType;
	}

	public void setTzType(String tzType) {
		this.tzType = tzType;
	}

}