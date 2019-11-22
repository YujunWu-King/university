package com.tranzvision.gd.TZBaseBundle.service.impl;

/**
 * 
* ClassName: ConditionBean
* @author caoy  
* @version 1.0 
* Create Time: 2019年11月21日 下午4:17:08 
* Description: 可配置查询的CLASS模式用的Bean
 */
public class ConditionBean {
	private String operator;
	private String value;
	private String translatevalue;
	
	private String fild;
	

	public String getFild() {
		return fild;
	}

	public void setFild(String fild) {
		this.fild = fild;
	}

	private String sql;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTranslatevalue() {
		return translatevalue;
	}

	public void setTranslatevalue(String translatevalue) {
		this.translatevalue = translatevalue;
	}

}
