package com.tranzvision.gd.TZApplicationVerifiedBundle.model;
/**
 * 面试分组类
 * @author Administrator
 *
 */
public class PsTzInteGroup {
	private Integer tz_group_id;	//分组id
	private String tz_group_name;	//分组名
	private String tz_clps_gr_id;	//所属评委组
	private String tz_class_id;		//班级id
	private String tz_apply_pc_id;	//批次id
	
	public Integer getTz_group_id() {
		return tz_group_id;
	}
	public void setTz_group_id(Integer tz_group_id) {
		this.tz_group_id = tz_group_id;
	}
	public String getTz_group_name() {
		return tz_group_name;
	}
	public void setTz_group_name(String tz_group_name) {
		this.tz_group_name = tz_group_name;
	}
	public String getTz_clps_gr_id() {
		return tz_clps_gr_id;
	}
	public void setTz_clps_gr_id(String tz_clps_gr_id) {
		this.tz_clps_gr_id = tz_clps_gr_id;
	}
	public String getTz_class_id() {
		return tz_class_id;
	}
	public void setTz_class_id(String tz_class_id) {
		this.tz_class_id = tz_class_id;
	}
	public String getTz_apply_pc_id() {
		return tz_apply_pc_id;
	}
	public void setTz_apply_pc_id(String tz_apply_pc_id) {
		this.tz_apply_pc_id = tz_apply_pc_id;
	}
	
	
}
