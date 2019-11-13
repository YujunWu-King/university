package com.tranzvision.gd.TZCreateTaskBundle.service.imple;

import com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.adfs.TZAdfsLoginObject;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaRole;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 *ClassName: TZCreateTaskServiceImpl
 * @author 吴玉军
 * @version 1.0
 * Create Time: 2019年2月21日 下午4:53:49 
 * Description:事件生成任务引擎
 */
@Service("com.tranzvision.gd.TZCreateTaskBundle.service.imple.TZCreateTaskServiceImpl")
public class TZCreateTaskServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzWebsiteLoginService tzWebsiteLoginService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;

	/**
	 * 
	 * Description: 根据事件创建相关任务 Create Time: 2019年2月20日 上午11:25:19
	 * 
	 * @author ZWH
	 * @param tzms_task_template_tid 任务模板ID
	 * @param tzms_wflinsid	流程实例ID
	 * @param stpInsId	步骤实例
	 * @return
	 */
	public String createTask(String tzms_task_template_tid, String tzms_wflinsid, String stpInsId) throws TzException {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		returnMap.put("status", "");
		returnMap.put("msg", "");
		try {
			//当前登录人
			String currOprid = tzWebsiteLoginService.getLoginedUserOprid(request);
			
			String sql = "select tzms_asynchronous from tzms_task_template_t where tzms_task_template_tid = ? ";
			int isasyn = sqlQuery.queryForObject(sql, new Object[] { tzms_task_template_tid }, "int");
		
			if(isasyn==1) {
				
				new asynCreateTask(sqlQuery,tzWebsiteLoginService,currOprid,getHardCodePoint,tzms_task_template_tid, tzms_wflinsid, stpInsId).start();
			}else{
				
				createTaskYB(sqlQuery,tzWebsiteLoginService,currOprid,getHardCodePoint,tzms_task_template_tid, tzms_wflinsid, stpInsId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		returnMap.put("status", "success");
		returnMap.put("msg", "任务创建成功");
		
		return jacksonUtil.Map2json(returnMap);
	}
	
	
	
	/**
	 * Description: 开启线程执行事件 Create Time: 2019年9月11日 
	 * 
	 * @author 吴玉军
	 * @param tzms_task_template_tid 任务模板ID
	 * @param tzms_wflinsid	流程实例ID
	 * @param stpInsId	步骤实例
	 * @return
	 */
	public void createTaskYB(SqlQuery sqlQuery, TzWebsiteLoginService tzWebsiteLoginService, String currOprid, GetHardCodePoint getHardCodePoint, String tzms_task_template_tid, String tzms_wflinsid, String stpInsId) {
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			
			//工作流业务数据ID
			String wflDateRecId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[]{ tzms_wflinsid }, "String");
			if(wflDateRecId == null) wflDateRecId = "";

			
			//获取任务模板的任务事项
			List<Map<String,Object>> taskMattList = sqlQuery.queryForList("select tzms_task_mat,tzms_shared_unique,tzms_wfrodn_unique,tzms_timeout,tzms_priority,tzms_task_cla_unique,tzms_task_matters_tid,tzms_task_title from tzms_task_matters_tBase where tzms_task_tem_unique=?  and statecode='0'", 
					new Object[] { tzms_task_template_tid });
			//获取任务模板名称
			String taskName = sqlQuery.queryForObject("select tzms_task_tem_name from tzms_task_template_tBase where tzms_task_template_tid=?  and statecode='0'", 
					new Object[] { tzms_task_template_tid },"String");
			
			//获取流程表单对应的实体
			String entitynamesql = "select tzms_entity_name from tzms_wfcldn_tBase where tzms_wfcldn_tid = (select tzms_wfcldn_uniqueid from tzms_wflins_tbl where tzms_wflinsid = ?)";
			String entityname = sqlQuery.queryForObject(entitynamesql, new Object[] { tzms_wflinsid },"String");

			//获取流程申请人
			String applyNameSql = "Select tzms_name from "+entityname+" where "+entityname+"id =( Select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid= ?)";
			String applyName = sqlQuery.queryForObject(applyNameSql, 
					new Object[] { tzms_wflinsid },"String");
			
			
			if(taskMattList != null && taskMattList.size() > 0) {
				TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
				String dynURL = "";
				String dynUserName = "";
				String dynUserPswd = "";
				String dynDomain = "";
				try {
					dynURL = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_LOGIN_URL");
					dynUserName = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
					dynUserPswd = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_PSWD");
					dynDomain = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_DOMAIN");
				} catch (NullPointerException e) {
					e.printStackTrace();
					throw new TzException("Dynamics登录信息未配置");
				}
				tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);
				
				
				for(Map<String,Object> taskMattMap:taskMattList) {
					//任务分类名称
					String typename = "";
					
					String tzms_task_title = taskMattMap.get("tzms_task_title")!=null?taskMattMap.get("tzms_task_title").toString():"";
					String tzms_task_matters_tid = taskMattMap.get("tzms_task_matters_tid")!=null?taskMattMap.get("tzms_task_matters_tid").toString():"";
					
					//舍弃任务摘要功能，改为 教授名+模板名称+任务类别+任务事项
					/*
					//获取任务摘要
					List<Map<String, Object>> variableList = sqlQuery.queryForList("select tzms_variable_unique from tzms_task_abstract_tBase where tzms_task_mat_unique=? ORDER BY tzms_serial_number asc", 
							new Object[] { tzms_task_matters_tid });
					
					StringBuffer postfix = new StringBuffer();
					if(variableList != null && variableList.size() > 0) {
						for(Map<String, Object> variableMap:variableList) {
							String tzms_variable_unique = variableMap.get("tzms_variable_unique") != null ? variableMap.get("tzms_variable_unique").toString() : "";
							if(!"".equals(tzms_variable_unique)) {
								//系统变量参数：工作流实例ID、步骤实例ID、工作流业务数据ID
								String[] sysVarParam = { tzms_wflinsid, stpInsId, wflDateRecId };
								AnalysisDynaSysVar analysisSysVar = new AnalysisDynaSysVar();
								analysisSysVar.setM_SysVarID(tzms_variable_unique);
								analysisSysVar.setM_SysVarParam(sysVarParam);
								
								Object obj = analysisSysVar.GetVarValue();
								if(obj !=null && !"".equals(obj.toString())) {
									postfix.append(obj.toString());
									postfix.append(",");
								}else {
									System.out.println("获取");
								}
							}
						}
					}
					if(postfix.length() > 0) {
						postfix.delete(postfix.length()-1, postfix.length());
					}
					*/
					
					AnalysisDynaRole analysisDynaRole = new AnalysisDynaRole();
					//任务事项
					String tzms_task_mat = taskMattMap.get("tzms_task_mat")!=null?taskMattMap.get("tzms_task_mat").toString():"";

					//任务分类
					String tzms_task_cla_unique =  taskMattMap.get("tzms_task_cla_unique")!=null?taskMattMap.get("tzms_task_cla_unique").toString():"";
					//任务状态（初始化）
					int tzms_task_status= 0;
					//活动类型(数据结构规定的)
					int activitytypecode =4212;
					//优先级
					Integer tzms_priority = taskMattMap.get("tzms_priority")!=null?Integer.valueOf(taskMattMap.get("tzms_priority").toString()):null;
					//开始时间
					String scheduledstart = format.format(new Date());
					//超时时限(持续时间)
					Integer tzms_timeout =taskMattMap.get("tzms_timeout")!=null?Integer.valueOf(taskMattMap.get("tzms_timeout").toString()):null;
					//截止日期
					String scheduledend = this.plusDay(tzms_timeout);
					//说明（暂时没有设为空）
					String description="";
					//关于
					String regardingobjectid = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", new Object[] {tzms_wflinsid}, "String");
					//获取实体对象名
					String tzms_entity_name = sqlQuery.queryForObject("select B.tzms_entity_name from tzms_wflins_tbl A LEFT JOIN tzms_wfcldn_tBase B on A.tzms_wfcldn_uniqueid=B.tzms_wfcldn_tid where A.tzms_wflinsid=?", new Object[] {tzms_wflinsid}, "String");
					//共享人角色
					String tzms_shared_unique = taskMattMap.get("tzms_shared_unique")!=null?taskMattMap.get("tzms_shared_unique").toString():"";
					
					
					typename = sqlQuery.queryForObject("select tzms_task_cla from tzms_task_classification_tBase where tzms_task_classification_tid = ?", 
							new Object[] { tzms_task_cla_unique }, "String");
					
					//任务主题
					String subject ="【"+applyName + taskName+"】" + typename + "-"+ tzms_task_mat;
					
					/*if("".equals(tzms_shared_unique)) {
						String entityName = sqlQuery.queryForObject("select tzms_task_tem_name from tzms_task_template_tBase where tzms_task_template_tid=?", new Object[] {tzms_task_template_tid}, "String");
						String returnStr = this.shareTask(entityName, tzms_task_template_tid, tzms_shared_unique);
						if("fail".equals(returnStr)) {
							returnMap.put("status", "fail");
							returnMap.put("msg", tzms_shared_unique+"共享任务失败");
							return jacksonUtil.Map2json(returnMap);
						}
					}*/
					//工作流角色
					String tzms_wfrodn_unique = taskMattMap.get("tzms_wfrodn_unique")!=null?taskMattMap.get("tzms_wfrodn_unique").toString():"";
					//根据oprid 获取dynamic userId
					String tzms_userId = sqlQuery.queryForObject("select tzms_user_uniqueid from tzms_tea_defn_tBase where tzms_oprid=?", 
							new Object[] { currOprid }, "String");
					
					
					List<String> userIds = analysisDynaRole.getUserIds(tzms_wflinsid,stpInsId,tzms_userId,tzms_shared_unique);
					tzms_shared_unique = String.join(";", userIds);
					
					
					
					userIds= analysisDynaRole.getUserIds(tzms_wflinsid,stpInsId,tzms_userId,tzms_wfrodn_unique);
					//多个人建立多个任务
					if(userIds != null && userIds.size() > 0) {
						for(String userId :userIds) {
							if("null".equals(String.valueOf(tzms_timeout)) || "".equals(String.valueOf(tzms_timeout))) {
								tzms_timeout = 0;
							}
							String status = this.writeDate(tzADFSObject,dynDomain,userId,subject,tzms_task_cla_unique,tzms_task_status,activitytypecode,
									tzms_priority,scheduledstart,scheduledend,description,regardingobjectid,tzms_timeout,tzms_entity_name,tzms_shared_unique);
							
							System.out.println(status+"+++++++++++status");
							if("fail".equals(status)) {
								throw new TzException(userId+"任务创建失败");
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		
	}
	
	public String shareTask(String entityName,String entityId,String userId) {
		String returnStr = "fail";
		JacksonUtil jacksonUtil = new JacksonUtil();
		TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();

		try {
			String dynURL = "";
			String dynUserName = "";
			String dynUserPswd = "";
			String dynDomain = "";
			try {
				dynURL = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_LOGIN_URL");
				dynUserName = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
				dynUserPswd = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_PSWD");
				dynDomain = getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_DOMAIN");
			} catch (NullPointerException e) {
				e.printStackTrace();
				throw new TzException("Dynamics登录信息未配置");
			}
			tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);

			String url = dynDomain + "/ISV/TZEMSServices/Services/RecordsShared.asmx/recordsShared";
			Map<String, Object> map = new HashMap<>();
			map.put("entityName", entityName);
			map.put("entityId", entityId);
			map.put("userId", userId);
			
			map.put("ReadAccess", 1);
			map.put("WriteAccess", 0);
			map.put("DeleteAccess", 0);
			map.put("AppendAccess", 0);
			map.put("AssignAccess", 0);
			map.put("ShareAccess", 0);
			String postData = jacksonUtil.Map2json(map);
			if(tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postData) == true) {
				if(tzADFSObject.isRequestSuccess() == true) {
					String result = tzADFSObject.getWebAPIResult();
					jacksonUtil.json2Map(result);
					if (!"1".equals(jacksonUtil.getString("code"))) {
						returnStr = "success";
					} 
				}
			}
		} catch (Exception e) {
			return "fail";
		}
		return returnStr;
	}
	
	
	
	
	
	/**
	 * 
	* Description:
	* Create Time: 2019年2月21日 下午3:32:06
	* @author 吴玉军
	* @param userId	dynamic用户ID
	* @param subject	主题
	* @param tzms_task_cla_unique	任务分类
	* @param tzms_task_status	任务状态
	* @param activitytypecode	活动类型
	* @param tzms_priority	优先级
	* @param scheduledstart	开始日期
	* @param scheduledend	截止日期
	* @param description	说明
	* @param regardingobjectid	关于
	* @param tzms_timeout	持续时间
	* @return
	 */
	public String writeDate(TZAdfsLoginObject tzADFSObject, String dynDomain, String userId, String subject, String tzms_task_cla_unique, Integer tzms_task_status, Integer activitytypecode,
                            Integer tzms_priority, String scheduledstart, String scheduledend, String description, String regardingobjectid, Integer tzms_timeout, String tzms_entity_name, String tzms_shared_unique) {
		System.out.println("tzms_shared_unique================>"+tzms_shared_unique);
		
		String returnStr = "fail";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String url = dynDomain + "/ISV/TZEMSServices/services/TZCreateTask_service.asmx/CreateT";
			Map<String, Object> map = new HashMap<>();
			
			//生产GUID
			String uid = UUID.randomUUID().toString();
			
			map.put("activityid", uid);// 任务系统ID可以为空
			map.put("subject", subject);// 主题
			map.put("ownerid", userId);//负责人
			//map.put("tzms_task_cla_unique", tzms_task_cla_unique);// 任务分类
			//map.put("tzms_task_cla_unique@odata.bind", "/tzms_task_classification_ts("+tzms_task_cla_unique+")");// 任务分类
			Map<String,Object> tempMap = new HashMap<String,Object>();
			tempMap.put("entityName", "tzms_task_classification_t");
			tempMap.put("entityId", tzms_task_cla_unique);
			map.put("tzms_task_cla_unique", tempMap);
			map.put("tzms_task_status", tzms_task_status);// 任务状态
			map.put("activitytypecode", activitytypecode);// 活动类型
			map.put("prioritycode", tzms_priority); // 优先级 int 0：低 1：正常 2：高
			map.put("scheduledstart", scheduledstart);//开始日期
			map.put("scheduledend", scheduledend);// 截止日期
			map.put("description", description);// 说明
			map.put("tzms_shared_unique", tzms_shared_unique);//共享人
			//map.put("regardingobjectid@odata.bind", "/"+tzms_entity_name+"s("+regardingobjectid+")");//关于
			tempMap = new HashMap<String,Object>();
			tempMap.put("entityName", tzms_entity_name);
			tempMap.put("entityId", regardingobjectid);
			map.put("regardingobjectid", tempMap);
			
			map.put("actualdurationminutes", tzms_timeout);// 持续时间，int
			
			String postData = jacksonUtil.Map2json(map);
			postData = "jsonText="+postData;
			System.out.println("~~~~~~~~~~~~~~~"+postData);
			if(tzADFSObject.callDynamicsCRMWebAPIForJSONData(url, postData) == true) {
				if(tzADFSObject.isRequestSuccess() == true) {
					returnStr = "success";
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
			
		}
		return returnStr;
	}
	
	
	
	/**
	 * 
	* Description: 当前日期+n天的日期
	* Create Time: 2019年2月21日 下午3:25:25
	* @author 吴玉军
	* @param dayNum n天
	* @return 当前日期+n天的日期
	 */
	public String plusDay(Integer dayNum) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date  currdate = new Date();
        if(dayNum==null) {
			return format.format(currdate);
		}
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, dayNum);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        return enddate;
	}
}
