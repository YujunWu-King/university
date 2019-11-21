package com.tranzvision.gd.util.log;

import org.apache.commons.lang.StringUtils;

/**
 * 获取代码调用位置信息
 * @author zhanglang
 * 2019年8月12日
 *
 */
public class Calling {
	
	//调用类名
	private String callClass = "";
	
	
	public Calling(String thisClass) {
		if(StringUtils.isNotBlank(thisClass)) {
			this.callClass = thisClass;
		}else {
			this.callClass = getClass().getName();
		}
	}




	/**
	 * 获取当前代码被调用的位置
	 * @return
	 */
	public String getCallerName() {
		String callerName = "";

		StackTraceElement tmpstack[] = Thread.currentThread().getStackTrace();
		boolean findFlag = false;
		for (StackTraceElement stackitem : tmpstack) {			
			if ((stackitem.getClassName().indexOf(callClass)) >= 0) {
				findFlag = true;
			} else if (findFlag == true) {
				callerName = stackitem.getClassName() + "." + stackitem.getMethodName() +"["+ stackitem.getLineNumber() + " line]";
				break;
			}
		}

		return callerName;
	}
	
	
	
	
	/**
	 * 获取当前代码执行的位置
	 * @return
	 */
	public String getCurrentLocation() {
		String currLocation = "";
		
		StackTraceElement tmpstack[] = Thread.currentThread().getStackTrace();
		boolean findFlag = false;
		for (StackTraceElement stackitem : tmpstack) {			
			if ((stackitem.getClassName().indexOf(callClass)) >= 0 && !findFlag) {
				findFlag = true;
			} else if (findFlag == true) {
				currLocation = stackitem.getClassName() + "." + stackitem.getMethodName() +"["+ stackitem.getLineNumber() + " line]";
				break;
			}
		}
		
		return currLocation;
	}
}
