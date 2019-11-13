package com.tranzvision.gd.workflow.base;

import java.util.Date;

/**
 * 该类用于提供存放委托关系的数据结构
 * @author 张浪	2019-01-11
 *
 */
public class TzClientageInfo {

	//委托明细编号
	private String m_ConsignOrderID;
	
	//业务流程定义编号
	private String m_BusProcessDefID;

	//委托人
	private String m_Consigner;
	
	//受托人
	private String m_Assignee;
	
	//开始日期
	private Date m_BeginDate;
	
	//结束日期
	private Date m_EndDate;
	
	//委托路径
	private String m_ClientagePath;

	
	
	
	
	public String getM_ConsignOrderID() {
		return m_ConsignOrderID;
	}

	public void setM_ConsignOrderID(String m_ConsignOrderID) {
		this.m_ConsignOrderID = m_ConsignOrderID;
	}

	public String getM_BusProcessDefID() {
		return m_BusProcessDefID;
	}

	public void setM_BusProcessDefID(String m_BusProcessDefID) {
		this.m_BusProcessDefID = m_BusProcessDefID;
	}

	public String getM_Consigner() {
		return m_Consigner;
	}

	public void setM_Consigner(String m_Consigner) {
		this.m_Consigner = m_Consigner;
	}

	public String getM_Assignee() {
		return m_Assignee;
	}

	public void setM_Assignee(String m_Assignee) {
		this.m_Assignee = m_Assignee;
	}

	public Date getM_BeginDate() {
		return m_BeginDate;
	}

	public void setM_BeginDate(Date m_BeginDate) {
		this.m_BeginDate = m_BeginDate;
	}

	public Date getM_EndDate() {
		return m_EndDate;
	}

	public void setM_EndDate(Date m_EndDate) {
		this.m_EndDate = m_EndDate;
	}

	public String getM_ClientagePath() {
		return m_ClientagePath;
	}

	public void setM_ClientagePath(String m_ClientagePath) {
		this.m_ClientagePath = m_ClientagePath;
	}

}
