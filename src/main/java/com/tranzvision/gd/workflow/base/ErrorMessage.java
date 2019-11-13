package com.tranzvision.gd.workflow.base;

/**
 * @author 工作流错误信息
 *
 */
public class ErrorMessage {

	//错误代码
	private String errorCode = "0"; //默认为0
	
	//错误信息描述
	private String errorMsg = "";

	
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
