package com.tranzvision.gd.workflow.base;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBusProNotice.service.impl.EmailFunServiceImpl;
import com.tranzvision.gd.TZBusProNotice.service.impl.NoticeFunctionServiceImpl;
import com.tranzvision.gd.TZCreateTaskBundle.service.imple.TZCreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.WorkflowActionsBundle.service.impl.ExpressionEngineImpl;
import com.tranzvision.gd.WorkflowActionsBundle.service.impl.WorkFlowPublicImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 工作流引擎相关bean
 * @author 张浪
 *
 */
@Service
public class TzWflObject {

	@Autowired
	private ExpressionEngineImpl expressionEngine;
	
	@Autowired
	private WorkFlowPublicImpl workFlowPublic;
	
	@Autowired
	private TZCreateTaskServiceImpl tzCreateTask;

	@Autowired
	private NoticeFunctionServiceImpl noticeFunction;
	
	@Autowired
	private EmailFunServiceImpl emailSpFun;
	
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	
	
	
	
	
	/**
	 * 工作流模板 路由设置 条件表达式引擎
	 * @return
	 */
	public ExpressionEngineImpl getExpressionEngine() {
		return expressionEngine;
	}


	/**
	 * 工作流常用的方法获取
	 * @return
	 */
	public WorkFlowPublicImpl getWorkFlowPublic() {
		return workFlowPublic;
	}


	/**
	 * 获取创建事件任务接口
	 * @return
	 */
	public TZCreateTaskServiceImpl getTzCreateTask() {
		return tzCreateTask;
	}


	/**
	 * 获取工作流通知接口
	 * @return
	 */
	public NoticeFunctionServiceImpl getNoticeFunction() {
		return noticeFunction;
	}


	/**
	 * 工作流邮件审批
	 * @return
	 */
	public EmailFunServiceImpl getEmailSpFun() {
		return emailSpFun;
	}
	
	
	
	/**
	 * 获取短信邮件任务Bean
	 * @return
	 */
	public CreateTaskServiceImpl getCreateTaskServiceImpl() {
		return createTaskServiceImpl;
	}


	/**
	 * 获取短信邮件发送Bean
	 * @return
	 */
	public SendSmsOrMalServiceImpl getSendSmsOrMalServiceImpl() {
		return sendSmsOrMalServiceImpl;
	}
	


	/**
	 * 获取当前登录人教职员dynamic用户userid
	 * @return
	 */
	public String getCurrentUserId() {
		String userId = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		if(StringUtils.isNotBlank(oprid)) {
			userId = this.getUserIdByOprid(oprid);
		}
		return userId;
	}
	
	
	/**
	 * 根据oprid获取教职员对应dyna用户ID
	 * @param oprid
	 * @return
	 */
	public String getUserIdByOprid(String oprid) {
		String userId = sqlQuery.queryForObject("select tzms_user_uniqueid from tzms_tea_defn_t where tzms_oprid=?", 
				new Object[] {	oprid }, "String");
		
		if(userId == null) userId = "";
		return userId;
	}
	
	
	
	
	/**
	 * 获取教职员信息
	 * @param userid	用户ID
	 * @return
	 */
	public TzStaff getStaffInfoByUserid(String userid) {
		TzStaff tzStaff = new TzStaff();
		
		if(StringUtils.isNotBlank(userid)) {
			
			TzWorkflowFunc tzWorkflowFunc = new TzWorkflowFunc();
			Map<String,String> contactInfo = tzWorkflowFunc.getTeaContactInfoByUserId(userid);
			
			if(contactInfo != null) {
				
				tzStaff.setUserId(userid);
				tzStaff.setOprid(contactInfo.get("oprid"));
				tzStaff.setSaifId(contactInfo.get("saifId"));
				tzStaff.setName(contactInfo.get("name"));
				tzStaff.setPhone(contactInfo.get("phone"));
				tzStaff.setEmial(contactInfo.get("email"));
			}else {
				tzStaff = null;
			}
		}else {
			tzStaff = null;
		}
		return tzStaff;
	}
	
	

	
	
	/**
	 * 根据人员获取对应职务教职员信息
	 * @param oprid			用户oprid
	 * @param positionType	职务类型： A-部门主管，B-部门主任，C-分管院长，D-职员
	 * @return
	 */
	public List<TzStaff> getStaffByOpridAndPosition(String oprid, String positionType){
		
		List<TzStaff> staffList = new ArrayList<TzStaff>();
		
		String positionName = "";
		switch (positionType) {
		case "A":
			positionName = "部门主管";
			break;
		case "B":
			positionName = "部门主任";
			break;
		case "C":
			positionName = "分管院长";
			break;
		case "D":
			positionName = "职员";
			break;
		default:
			break;
		}
		//根据职务描述查询职务ID
		String positionId = sqlQuery.queryForObject("select tzms_jobcd_dfn_tId from tzms_jobcd_dfn_tBase where tzms_jobcode_name=?", 
				new Object[] { positionName }, "String");
		
		if(StringUtils.isNotBlank(positionId)) {
			//获取教职员userid
			String userId = this.getUserIdByOprid(oprid);
			
			List<String> userIds = new ArrayList<String>();
			
			//查询人员所属部门
			String sql = "select tzms_org_uniqueid from tzms_org_user_tBase where tzms_tea_defnid=(select tzms_tea_defn_tid from tzms_tea_defn_t where CAST (tzms_user_uniqueid AS VARCHAR (36))=?)";
			List<String> deptList = sqlQuery.queryForList(sql, new Object[] { userId }, "String");
			
			if(deptList != null && deptList.size() > 0) {
				for(String deptId: deptList) {
					//部门下是否有指定职务的人员
					List<String> deptUsers = sqlQuery.queryForList("select tzms_user_uniqueid from tzms_position_mgr_tBase A join tzms_post_tea_tBase B on(A.tzms_position_mgr_tId=B.tzms_position_uniqueid) join tzms_tea_defn_tBase C on(B.tzms_staff_uniqueid=C.tzms_tea_defn_tId) where A.tzms_lon_dept=? and A.tzms_pos_level_uniqueid=? and C.tzms_user_uniqueid is not null", 
							new Object[] { deptId, positionId }, "String");
					
					if(deptUsers != null && deptUsers.size() > 0) {
						userIds.addAll(deptUsers);
						//去重
						userIds = userIds.stream().distinct().collect(Collectors.toList());
						continue;
					}else {
						//继续查找父级部门
						deptUsers = new ArrayList<String>();
						
						String searchDeptId = deptId;
						while(StringUtils.isNotBlank(searchDeptId) && deptUsers.size() == 0) {
							//父级部门
							String parentDeptId = sqlQuery.queryForObject("SELECT tzms_parent_deptid FROM tzms_org_structure_tree_tBase WHERE tzms_org_structure_tree_tid=? and tzms_parent_deptid is not null", 
									new Object[] { searchDeptId }, "String");
							
							if(StringUtils.isNotBlank(parentDeptId)) {
								List<String> pDeptUsers = sqlQuery.queryForList("select tzms_user_uniqueid from tzms_position_mgr_tBase A join tzms_post_tea_tBase B on(A.tzms_position_mgr_tId=B.tzms_position_uniqueid) join tzms_tea_defn_tBase C on(B.tzms_staff_uniqueid=C.tzms_tea_defn_tId) where A.tzms_lon_dept=? and A.tzms_pos_level_uniqueid=? and C.tzms_user_uniqueid is not null", 
										new Object[] { parentDeptId, positionId }, "String");
								
								if(pDeptUsers != null && pDeptUsers.size() > 0) {
									deptUsers.addAll(pDeptUsers);
									//去重
									deptUsers = deptUsers.stream().distinct().collect(Collectors.toList());	
									break;
								}
							}
							searchDeptId = parentDeptId;
						}
						
						if(deptUsers.size() > 0) {
							userIds.addAll(deptUsers);
							//去重
							userIds = userIds.stream().distinct().collect(Collectors.toList());
							continue;
						}
					}
				}
			}
			
			if(userIds.size() > 0) {
				for(String userid : userIds) {
					
					TzStaff tzStaff = this.getStaffInfoByUserid(userid);
					if(tzStaff != null) {
						staffList.add(tzStaff);
					}
				}
			}
		}
		
		return staffList;
	}
	
	
	
	
	/**
	 * 根据部门获取对应职务教职员信息
	 * @param deptUnid		部门定义uid
	 * @param positionType	职务类型： A-部门主管，B-部门主任，C-分管院长，D-职员
	 * @return
	 */
	public List<TzStaff> getStaffByDepartmentAndPosition(String deptUnid, String positionType){
		
		List<TzStaff> staffList = new ArrayList<TzStaff>();
		
		String positionName = "";
		switch (positionType) {
		case "A":
			positionName = "部门主管";
			break;
		case "B":
			positionName = "部门主任";
			break;
		case "C":
			positionName = "分管院长";
			break;
		case "D":
			positionName = "职员";
			break;
		default:
			break;
		}
		//根据职务描述查询职务ID
		String positionId = sqlQuery.queryForObject("select tzms_jobcd_dfn_tId from tzms_jobcd_dfn_tBase where tzms_jobcode_name=?", 
				new Object[] { positionName }, "String");
		
		if(StringUtils.isNotBlank(positionId)) {
	
			List<String> userIds = new ArrayList<String>();
			
			//部门下是否有指定职务的人员
			List<String> deptUsers = sqlQuery.queryForList("select tzms_user_uniqueid from tzms_position_mgr_tBase A join tzms_post_tea_tBase B on(A.tzms_position_mgr_tId=B.tzms_position_uniqueid) join tzms_tea_defn_tBase C on(B.tzms_staff_uniqueid=C.tzms_tea_defn_tId) where A.tzms_lon_dept=? and A.tzms_pos_level_uniqueid=? and C.tzms_user_uniqueid is not null", 
					new Object[] { deptUnid, positionId }, "String");
			
			if(deptUsers != null && deptUsers.size() > 0) {
				userIds.addAll(deptUsers);
				//去重
				userIds = userIds.stream().distinct().collect(Collectors.toList());
			}else {
				//继续查找父级部门
				deptUsers = new ArrayList<String>();
				
				String searchDeptId = deptUnid;
				while(StringUtils.isNotBlank(searchDeptId) && deptUsers.size() == 0) {
					//父级部门
					String parentDeptId = sqlQuery.queryForObject("SELECT tzms_parent_deptid FROM tzms_org_structure_tree_tBase WHERE tzms_org_structure_tree_tid=? and tzms_parent_deptid is not null", 
							new Object[] { searchDeptId }, "String");
					
					if(StringUtils.isNotBlank(parentDeptId)) {
						List<String> pDeptUsers = sqlQuery.queryForList("select tzms_user_uniqueid from tzms_position_mgr_tBase A join tzms_post_tea_tBase B on(A.tzms_position_mgr_tId=B.tzms_position_uniqueid) join tzms_tea_defn_tBase C on(B.tzms_staff_uniqueid=C.tzms_tea_defn_tId) where A.tzms_lon_dept=? and A.tzms_pos_level_uniqueid=? and C.tzms_user_uniqueid is not null", 
								new Object[] { parentDeptId, positionId }, "String");
						
						if(pDeptUsers != null && pDeptUsers.size() > 0) {
							deptUsers.addAll(pDeptUsers);
							//去重
							deptUsers = deptUsers.stream().distinct().collect(Collectors.toList());	
							break;
						}
					}
					searchDeptId = parentDeptId;
				}
				
				if(deptUsers.size() > 0) {
					userIds.addAll(deptUsers);
					//去重
					userIds = userIds.stream().distinct().collect(Collectors.toList());
				}
			}
			
			if(userIds.size() > 0) {
				for(String userid : userIds) {
					
					TzStaff tzStaff = this.getStaffInfoByUserid(userid);
					if(tzStaff != null) {
						staffList.add(tzStaff);
					}
				}
			}
		}
		
		return staffList;
	}



}
