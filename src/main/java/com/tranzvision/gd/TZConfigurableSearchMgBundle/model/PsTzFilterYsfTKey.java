package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFilterYsfTKey {
    private String tzComId;

    private String tzPageId;

    private String tzViewName;
    
    private String tzAppClassName;

    private String tzFilterFld;

    private String tzFilterYsf;

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

	public String getTzFilterFld() {
        return tzFilterFld;
    }

    public void setTzFilterFld(String tzFilterFld) {
        this.tzFilterFld = tzFilterFld == null ? null : tzFilterFld.trim();
    }

    public String getTzFilterYsf() {
        return tzFilterYsf;
    }

    public void setTzFilterYsf(String tzFilterYsf) {
        this.tzFilterYsf = tzFilterYsf == null ? null : tzFilterYsf.trim();
    }
    
}