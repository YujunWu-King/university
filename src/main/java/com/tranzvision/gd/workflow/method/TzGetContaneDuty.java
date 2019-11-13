package com.tranzvision.gd.workflow.method;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TzGetContaneDuty {

	private SqlQuery sqlQuery;
	public TzGetContaneDuty() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
	}
	/**
	 * 获得知会角色
	 * 
	 * @param wflInsId 工作流实例ID
	 * @param stpInsId 工作流步骤实例ID
	 * @param wflRecId 业务数据ID
	 * @return
	 */
	public List<String> getOwnerId(String wflInsId, String stpInsId, String wflRecId) {
		List<String> list = new ArrayList<String>();
		//查询申请人
		String applyUser = sqlQuery.queryForObject("SELECT tzms_apply_user FROM tzms_teacher_lesson_contract WHERE tzms_teacher_lesson_contractid = '"+wflRecId+"'", "String");
		String template = sqlQuery.queryForObject("SELECT tzms_project_template FROM tzms_teacher_lesson_contract WHERE tzms_teacher_lesson_contractid = '"+wflRecId+"'", "String");
		if(StringUtils.isNotBlank(template)) {
			String fuzeren = sqlQuery.queryForObject("SELECT tzms_print_owner FROM tzms_project_template_t WHERE tzms_project_template_tid = '"+template+"'", "String");
			if(StringUtils.isNotBlank(fuzeren)) {
				String id = sqlQuery.queryForObject("SELECT tzms_user_uniqueid FROM tzms_tea_defn_t WHERE tzms_tea_defn_tid = '"+fuzeren+"'", "String");
				if(StringUtils.isNotBlank(id) && !id.equals(applyUser)) {
					System.out.println("------------------------------------");
					System.out.println("ID："+id);
					System.out.println("applyUser："+applyUser);
					list.add(id);
				}
			}
		}
		System.out.println("+++++++++++++++++++");
		System.out.println(list.size());
		System.out.println(list);
		return list;
	}
	
}
