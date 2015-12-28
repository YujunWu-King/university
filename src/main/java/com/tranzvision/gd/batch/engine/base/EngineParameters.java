/**
 * 
 */
package com.tranzvision.gd.batch.engine.base;

import java.util.Date;

/**
 * @author LiGang
 * 2015/12/02
 */
public class EngineParameters
{
	private Integer processInstanceId = 0;
	private String loginUserAccount = "";
	private String runControlId = "";
	private String cycleExpression = "";
	private String batchServer = "";
	private Date planExcuteDateTime = null;
	
	public void setProcessInstanceId(Integer procInstanceId)
	{
		if(procInstanceId != null && procInstanceId >= 1)
		{
			processInstanceId = procInstanceId;
		}
	}
	
	public Integer getProcessInstanceId()
	{
		return processInstanceId;
	}
	
	public void setLoginUserAccount(String loginUser)
	{
		if(loginUser != null && loginUser.trim().equals("") == false)
		{
			loginUserAccount = loginUser.trim();
		}
	}
	
	public String getLoginUserAccount()
	{
		return loginUserAccount;
	}
	
	public void setRunControlId(String runCtrlId)
	{
		if(runCtrlId != null && runCtrlId.trim().equals("") == false)
		{
			runControlId = runCtrlId.trim();
		}
	}
	
	public String getRunControlId()
	{
		return runControlId;
	}
	
	public void setCycleExpression(String cycleExp)
	{
		if(cycleExp != null && cycleExp.trim().equals("") == false)
		{
			cycleExpression = cycleExp.trim();
		}
	}
	
	public String getCycleExpression()
	{
		return cycleExpression;
	}
	
	public void setBatchServer(String bthServer)
	{
		if(bthServer != null && bthServer.trim().equals("") == false)
		{
			batchServer = bthServer.trim();
		}
	}
	
	public String getBatchServer()
	{
		return batchServer;
	}
	
	public void setPlanExcuteDateTime(Date pExeDateTime)
	{
		if(pExeDateTime != null)
		{
			planExcuteDateTime = pExeDateTime;
		}
	}
	
	public Date getPlanExcuteDateTime()
	{
		return planExcuteDateTime;
	}
}
