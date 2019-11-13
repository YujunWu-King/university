package com.tranzvision.gd.workflow.base;

import java.util.List;

/**
 * 用于存放步骤动作及下一步骤责任人信息
 * @author 张浪	2019-01-16
 *
 */
public class TzStpActInfo {

	//动作类编号
	private String m_ActClsId;
	//动作类名称
	private String m_ActClsName;
	//是否判断条件动作，选择路由时需要多选一
	private boolean m_IsConAction;
	//是否结束动作
	private boolean m_IsEndAction;
	//下一步骤编号
	private String m_NextStepId;
	//下一步骤责任人列表
	private List<String> m_NextUserList;
	//步骤到下一步骤动作路径列表
	private List<String> m_RouAllPathList;
	
	
	
	
	
	public String getM_ActClsId() {
		return m_ActClsId;
	}
	public void setM_ActClsId(String m_ActClsId) {
		this.m_ActClsId = m_ActClsId;
	}
	public String getM_ActClsName() {
		return m_ActClsName;
	}
	public void setM_ActClsName(String m_ActClsName) {
		this.m_ActClsName = m_ActClsName;
	}
	public boolean isM_IsEndAction() {
		return m_IsEndAction;
	}
	public void setM_IsEndAction(boolean m_IsEndAction) {
		this.m_IsEndAction = m_IsEndAction;
	}
	public boolean isM_IsConAction() {
		return m_IsConAction;
	}
	public void setM_IsConAction(boolean m_IsConAction) {
		this.m_IsConAction = m_IsConAction;
	}
	public String getM_NextStepId() {
		return m_NextStepId;
	}
	public void setM_NextStepId(String m_NextStepId) {
		this.m_NextStepId = m_NextStepId;
	}
	public List<String> getM_NextUserList() {
		return m_NextUserList;
	}
	public void setM_NextUserList(List<String> m_NextUserList) {
		this.m_NextUserList = m_NextUserList;
	}
	public List<String> getM_RouAllPathList() {
		return m_RouAllPathList;
	}
	public void setM_RouAllPathList(List<String> m_RouAllPathList) {
		this.m_RouAllPathList = m_RouAllPathList;
	}
}
