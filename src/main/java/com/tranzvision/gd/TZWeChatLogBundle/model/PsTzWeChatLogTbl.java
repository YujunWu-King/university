package com.tranzvision.gd.TZWeChatLogBundle.model;

import java.util.Date;

public class PsTzWeChatLogTbl {

	private String tzJgId;
	
	private String tzAppId;
	
	private String tzXH;
	
	private String tzSendPSN;
	
	private Date tzSendDTime;
	
	private String tzSendState;
	
	private Date tzSendSDTime;
	
	private Integer tzSTotal;
	
	private Integer tzSFilter;
	
	private Integer tzSSucess;
	
	private Integer tzSFail;
	
	private String tzContent;
	
	private String tzMediaId;
	
	private String tzSendType;
	
	private String tzSendMode;
	
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
	
    public String getTzSendPSN() {
        return tzSendPSN;
    }

    public void setTzsendPSN(String tzSendPSN) {
        this.tzSendPSN = tzSendPSN == null ? null : tzSendPSN.trim();
    }
    
    public Date getTzSendDTime() {
        return tzSendDTime;
    }

    public void setTzSendDTime(Date tzSendDTime) {
        this.tzSendDTime = tzSendDTime;
    }
    
    public String getTzSendState() {
        return tzSendState;
    }

    public void setTzSendState(String tzSendState) {
        this.tzSendState = tzSendState == null ? null : tzSendState.trim();
    }
    
    public Date getTzSendSDTime() {
        return tzSendSDTime;
    }

    public void setTzSendSDTime(Date tzSendSDTime) {
        this.tzSendSDTime = tzSendSDTime;
    }
    
    public Integer getTzSTotal() {
        return tzSTotal;
    }

    public void setTzSTotal(Integer tzSTotal) {
        this.tzSTotal = tzSTotal ;
    }
    
    public Integer getTzSFilter() {
        return tzSFilter;
    }

    public void setTzSFilter(Integer tzSFilter) {
        this.tzSFilter = tzSFilter ;
    }
        
    public Integer getTzSSucess() {
        return tzSSucess;
    }

    public void setTzSSucess(Integer tzSSucess) {
        this.tzSSucess = tzSSucess ;
    }
    
    public Integer getTzSFail() {
        return tzSFail;
    }

    public void setTzSFail(Integer tzSFail) {
        this.tzSFail = tzSFail ;
    }
    
    public String getTzContent() {
        return tzContent;
    }

    public void setTzContent(String tzContent) {
        this.tzContent = tzContent == null ? null : tzContent.trim();
    }
        
    public String getTzMediaId() {
        return tzMediaId;
    }

    public void setTzMediaId(String tzMediaId) {
        this.tzMediaId = tzMediaId == null ? null : tzMediaId.trim();
    }
    
    public String getTzSendType() {
        return tzSendType;
    }

    public void setTzSendType(String tzSendType) {
        this.tzSendType = tzSendType == null ? null : tzSendType.trim();
    }
    
    public String getTzSendMode() {
        return tzSendMode;
    }

    public void setTzSendMode(String tzSendMode) {
        this.tzSendMode = tzSendMode == null ? null : tzSendMode.trim();
    }
    
}
