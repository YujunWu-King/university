package com.tranzvision.gd.TZWeChatLogBundle.model;

public class PsTzWeChatLogTblKey {
	
	private String tzJgId;
	
	private String tzAppId;
	
	private String tzXH;
	
	public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
    
    public String getTzAppId() {
        return tzAppId;
    }

    public void setTzAppId(String tzAppId) {
        this.tzAppId = tzAppId == null ? null : tzAppId.trim();
    }
    
    public String getTzXH() {
        return tzXH;
    }

    public void setTzXH(String tzXH) {
        this.tzXH = tzXH == null ? null : tzXH.trim();
    }
}
