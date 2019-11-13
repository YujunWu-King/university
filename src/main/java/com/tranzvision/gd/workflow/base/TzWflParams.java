package com.tranzvision.gd.workflow.base;

/**
 * 工作流参数
 * @author 张浪	2019-01-09
 */
public class TzWflParams {

	private String wflInsId = "";	/*工作流实例编号*/
	
	private String wflStpInsId = "";	/*工作流步骤实例编号*/	
	
	private String eventDefnId = ""; /*事件定义编号*/
	
	private String wflStrParams = ""; /*工作流字符串参数*/
	
	
	
	
	
	
	
	public String getWflInsId() {
		return wflInsId;
	}

	public void setWflInsId(String wflInsId) {
		this.wflInsId = wflInsId;
	}

	public String getWflStpInsId() {
		return wflStpInsId;
	}

	public void setWflStpInsId(String wflStpInsId) {
		this.wflStpInsId = wflStpInsId;
	}

	public String getEventDefnId() {
		return eventDefnId;
	}

	public void setEventDefnId(String eventDefnId) {
		this.eventDefnId = eventDefnId;
	}

	public String getWflStrParams() {
		return wflStrParams;
	}

	public void setWflStrParams(String wflStrParams) {
		this.wflStrParams = wflStrParams;
	}
}
