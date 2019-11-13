package com.tranzvision.gd.WorkflowActionsBundle.service.impl;

import java.util.Map;

/**
 * 
 * @ClassName: ConditionBean
 * @author caoy
 * @version 1.0
 * @Create Time: 2019年1月14日 下午5:59:04
 * @Description: 业务流程动作条件表的实例BEAN
 */
public class ConditionBean {

	private String and_or;
	// 左符号
	private String left_paren;
	// 页面字段名称
	private String field_name;
	// 操作符
	private String operator_flg;
	// 取值类型
	private String value_type;
	// 常量
	private String cond_value;
	// 系统变量
	private String svar_uniqueid;
	// 环境变量
	private String environment_type;
	// 右符号
	private String right_paren;

	
	/**
	 * 
	* @Description: 从数据库MPA转成BEAN
	* @Create Time: 2019年1月14日 下午6:03:41
	* @author caoy 
	* @param map
	* @return
	 */
	public static ConditionBean chageMap(Map<String, Object> map) {
		ConditionBean cb = new ConditionBean();
		// and或or
		String and_or = map.get("tzms_and_or") != null ? map.get("tzms_and_or").toString() : "";
		// 左符号
		String left_paren = map.get("tzms_left_paren") != null ? map.get("tzms_left_paren").toString() : "";
		// 页面字段名称
		String field_name = map.get("tzms_field_name") != null ? map.get("tzms_field_name").toString() : "";
		// 操作符
		String operator_flg = map.get("tzms_operator_flg") != null ? map.get("tzms_operator_flg").toString() : "";
		// 取值类型
		String value_type = map.get("tzms_value_type") != null ? map.get("tzms_value_type").toString() : "";
		// 常量
		String cond_value = map.get("tzms_cond_value") != null ? map.get("tzms_cond_value").toString() : "";
		// 系统变量
		String svar_uniqueid = map.get("tzms_svar_uniqueid") != null ? map.get("tzms_svar_uniqueid").toString() : "";
		// 环境变量
		String environment_type = map.get("tzms_environment_type") != null ? map.get("tzms_environment_type").toString()
				: "";
		// 右符号
		String right_paren = map.get("tzms_right_paren") != null ? map.get("tzms_right_paren").toString() : "";
		
		cb.setAnd_or(and_or);
		cb.setCond_value(cond_value);
		cb.setLeft_paren(left_paren);
		cb.setEnvironment_type(environment_type);
		cb.setField_name(field_name);
		cb.setOperator_flg(operator_flg);
		cb.setRight_paren(right_paren);
		cb.setSvar_uniqueid(svar_uniqueid);
		cb.setValue_type(value_type);
		return cb;
	}

	public String getAnd_or() {
		return and_or;
	}

	public void setAnd_or(String and_or) {
		this.and_or = and_or;
	}

	public String getLeft_paren() {
		return left_paren;
	}

	public void setLeft_paren(String left_paren) {
		this.left_paren = left_paren;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getOperator_flg() {
		return operator_flg;
	}

	public void setOperator_flg(String operator_flg) {
		this.operator_flg = operator_flg;
	}

	public String getValue_type() {
		return value_type;
	}

	public void setValue_type(String value_type) {
		this.value_type = value_type;
	}

	public String getCond_value() {
		return cond_value;
	}

	public void setCond_value(String cond_value) {
		this.cond_value = cond_value;
	}

	public String getSvar_uniqueid() {
		return svar_uniqueid;
	}

	public void setSvar_uniqueid(String svar_uniqueid) {
		this.svar_uniqueid = svar_uniqueid;
	}

	public String getEnvironment_type() {
		return environment_type;
	}

	public void setEnvironment_type(String environment_type) {
		this.environment_type = environment_type;
	}

	public String getRight_paren() {
		return right_paren;
	}

	public void setRight_paren(String right_paren) {
		this.right_paren = right_paren;
	}

}
