package com.tranzvision.gd.workflow.base;

/**
 * 教职员信息存储对象
 * @author zhanglang
 * 2019年7月31日
 *
 */
public class TzStaff {
	
	//SAIF ID
	private String saifId = "";

	//用户ID， Dynamic用户ID
	private String userId = "";
	
	//Portal系统用户的OPRID
	private String oprid = "";
	
	//教职员姓名
	private String name = "";
	
	//教职员手机
	private String phone = "";
	
	//教职员邮箱
	private String emial = "";

	
	
	/**
	 * 获取教职员SAIF ID（登陆用的SAIF ID）
	 * @return
	 */
	public String getSaifId() {
		return saifId;
	}

	public void setSaifId(String saifId) {
		this.saifId = saifId;
	}

	/**
	 * 获取教职员对应Dynamic用户ID
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取Portal系统对应用户的OPRID
	 * @return
	 */
	public String getOprid() {
		return oprid;
	}

	public void setOprid(String oprid) {
		this.oprid = oprid;
	}

	/**
	 * 获取教职员姓名
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取教职员手机，默认获取主要手机
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取教职员邮箱，默认获取主要邮箱
	 * @return
	 */
	public String getEmial() {
		return emial;
	}

	public void setEmial(String emial) {
		this.emial = emial;
	}

}
