package com.tranzvision.gd.workflow.method;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzStaff;
import com.tranzvision.gd.workflow.base.TzWflObject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取申请人部门主任
 * @author ZhouWH
 * 2019年8月6日
 *
 */
public class TzGetDirectorOfDepartment {

	private SqlQuery sqlQuery;

	private TzWflObject tzWflObject;

	public TzGetDirectorOfDepartment() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		tzWflObject = (TzWflObject) getSpringBeanUtil.getAutowiredSpringBean("TzWflObject");
	}

	/**
	 * 获取承担部门的部门主任
	 * 
	 * @param wflInsId 工作流实例ID
	 * @param stpInsId 工作流步骤实例ID
	 * @param wflRecId 业务数据ID
	 * @return
	 */
	public List<String> getWflName(String wflInsId, String stpInsId, String wflRecId) {

		// 流程表单对应的实体
		String entityName;
		// 申请人的组织机构部门
		String department;
		// 部门主任
		String positionType = "B";
		// 角色数组
		List<String> roleUsers = new ArrayList();

		entityName = sqlQuery.queryForObject(
				"select tzms_entity_name from tzms_wfcldn_tBase A where exists(select 'Y' from tzms_wflins_tbl where tzms_wflinsid=? and tzms_wfcldn_uniqueid=A.tzms_wfcldn_tid)",
				new Object[] { wflInsId }, "String");
		if (StringUtils.isNotBlank(entityName)) {
			department = sqlQuery.queryForObject(
					"select tzms_apply_dept from " + entityName + " where " + entityName + "id = ? ",
					new Object[] { wflRecId }, "String");
			if (StringUtils.isNotBlank(department)) {
				List<TzStaff> taffList = tzWflObject.getStaffByDepartmentAndPosition(department, positionType);
				for (TzStaff tzStaff : taffList) {
					if(!roleUsers.contains(tzStaff.getUserId())) {
						roleUsers.add(tzStaff.getUserId());
					}
					
				}
			}
		}
		return roleUsers;
	}

	
	/**
	 * 教授服务时间确认单
	 * 获取承担部门的部门主任
	 * 
	 * @param wflInsId 工作流实例ID
	 * @param stpInsId 工作流步骤实例ID
	 * @param wflRecId 业务数据ID
	 * @return
	 */
	public List<String> getSerWorkName(String wflInsId, String stpInsId, String wflRecId) {

		// 流程表单对应的实体
		String entityName;
		// 申请人的组织机构部门
		List<String> departments = new ArrayList<String>();;
		// 部门主任
		String positionType = "B";
		// 角色数组
		List<String> roleUsers = new ArrayList();

		
			 departments = sqlQuery.queryForList(
					"select tzms_bear_dept from tzms_serv_workload where tzms_servwork_unique = ? ",
					new Object[] { wflRecId }, "String");
			if (departments!=null) {
				for (String department : departments) {
					List<TzStaff> taffList = tzWflObject.getStaffByDepartmentAndPosition(department, positionType);
					for (TzStaff tzStaff : taffList) {
						if(!roleUsers.contains(tzStaff.getUserId())) {
							roleUsers.add(tzStaff.getUserId());
						}
					}
				}
			}
		System.out.println("=============department:"+departments+"====================roleUsers:"+roleUsers);
		return roleUsers;
	}

	
	/**
	 * 教授教学时间确认单
	 * 获取承担部门的部门主任
	 * 
	 * @param wflInsId 工作流实例ID
	 * @param stpInsId 工作流步骤实例ID
	 * @param wflRecId 业务数据ID
	 * @return
	 */
	public List<String> getTeaWorkName(String wflInsId, String stpInsId, String wflRecId) {

		// 流程表单对应的实体
		String entityName;
		// 申请人的组织机构部门
		List<String> departments  = new ArrayList<String>();;
		// 部门主任
		String positionType = "B";
		// 角色数组
		List<String> roleUsers = new ArrayList();

		
			 departments = sqlQuery.queryForList(
					"select tzms_bear_dept from tzms_tea_workload where tzms_tea_work_unique = ? ",
					new Object[] { wflRecId }, "String");
			if (departments!=null) {
				for (String department : departments) {
					List<TzStaff> taffList = tzWflObject.getStaffByDepartmentAndPosition(department, positionType);
					for (TzStaff tzStaff : taffList) {
						if(!roleUsers.contains(tzStaff.getUserId())) {
							roleUsers.add(tzStaff.getUserId());
						}
					}
				}
			}
		System.out.println("=============department:"+departments+"====================roleUsers:"+roleUsers);
		return roleUsers;
	}
	
	/**
	 * 课酬支付
	 * 获取承担部门的部门主任
	 * 
	 * @param wflInsId 工作流实例ID
	 * @param stpInsId 工作流步骤实例ID
	 * @param wflRecId 业务数据ID
	 * @return
	 */
	public List<String> getKCName(String wflInsId, String stpInsId, String wflRecId) {

		// 流程表单对应的实体
		String entityName;
		// 申请人的组织机构部门
		List<String> departments = new ArrayList<String>();
		// 部门主任
		String positionType = "B";
		// 角色数组
		List<String> roleUsers = new ArrayList<String>();

		
			departments = sqlQuery.queryForList(
					"select tzms_pay_dept_uniqueid from tzms_paylist_voucher_t where tzms_tearem_paylist_uniqueid in (select tzms_tearem_paylist_tid from tzms_tearem_paylist_t where tzms_tearem_payapp_uniqueid = ? ) ",
					new Object[] { wflRecId }, "String");
			if (departments!=null) {
				for (String department : departments) {
					List<TzStaff> taffList = tzWflObject.getStaffByDepartmentAndPosition(department, positionType);
					for (TzStaff tzStaff : taffList) {
						if(!roleUsers.contains(tzStaff.getUserId())) {
							roleUsers.add(tzStaff.getUserId());
						}
					}
				}
			}
		System.out.println("=============department:"+departments+"====================roleUsers:"+roleUsers);
		return roleUsers;
	}
}
