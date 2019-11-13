package com.tranzvision.gd.workflow.base;

/**
 * 获取事件触发时机说明
 * @author zhanglang
 * 2019年7月5日
 *
 *
 *1.	工作流级事件： 101-初始化前、102-初始化后、103-载入时、104-结束前、105-结束后、106-撤回时、108-退回时、109-补充意见时、110-转发时、111-提交时
 *2.	步骤级事件：201-初始化前、202-初始化后、203-载入时、204-结束前、205-结束后、206-撤回时、207-签收时、208-退回时、209-转发时、210-提交时、211-退回前、212-同意时、213-拒绝时
 *3.	动作级事件：301-流转前、302-流转后
 *4.	其他事件：401-退回时、402-撤回时、403-转发时
 *5.	指定步骤退回事件：501-退回时
 *
 */
public class TzEventsDesc {

	/**
	 * 工作流事件级别
	 * @param level
	 * @return
	 */
	public static String eventLevel(String level) {
		String levelDesc = "";
		switch (level) {
		case "1":
			levelDesc = "工作流级事件";
			break;
		case "2":
			levelDesc = "步骤级事件";	
			break;
		case "3":
			levelDesc = "动作级事件";
			break;
		case "4":
			levelDesc = "其他事件";
			break;
		case "5":
			levelDesc = "指定步骤退回事件";
			break;
		default:
			levelDesc = level;
			break;
		}
		return levelDesc;
	}
	
	
	/**
	 * 工作流事件触发时机
	 * @param trigger
	 * @return
	 */
	public static String eventTrigger(String trigger) {
		String triggerDesc = "";
		switch (trigger) {
		case "101":
		case "201":
			triggerDesc = "初始化前";
			break;
		case "102":
		case "202":
			triggerDesc = "初始化后";
			break;
		case "103":
		case "203":
			triggerDesc = "载入时";
			break;
		case "104":
		case "204":
			triggerDesc = "结束前";
			break;
		case "105":
		case "205":
			triggerDesc = "结束后";
			break;
		case "106":
		case "206":
		case "402":
			triggerDesc = "撤回时";
			break;
		case "207":
			triggerDesc = "签收时";
			break;
		case "108":
		case "208":
		case "401":
		case "501":
			triggerDesc = "退回时";
			break;
		case "109":
			triggerDesc = "补充意见时";
			break;
		case "110":
		case "209":
		case "403":
			triggerDesc = "转发时";
			break;
		case "111":
		case "210":
			triggerDesc = "提交时";
			break;
		case "211":
			triggerDesc = "退回前";
			break;
		case "212":
			triggerDesc = "同意时";
			break;
		case "213":
			triggerDesc = "拒绝时";
			break;
		case "301":
			triggerDesc = "流转前";
			break;
		case "302":
			triggerDesc = "流转后";
			break;
		default:
			triggerDesc = trigger;
			break;
		}
		return triggerDesc;
	}
	
}
