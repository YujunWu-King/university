package com.tranzvision.gd.util.dynamicsBase;

import com.tranzvision.gd.util.base.AnalysisClsMethod;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * ClassName: AnalysisDynaRole
 * @author zhongcg 
 * @version 1.0 
 * Create Time: 2019年1月14日 下午4:32:29  
 * Description: 解析系统角色引擎
 */
public class AnalysisDynaRole {

	private SqlQuery sqlQuery;
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");

		
	
	/**
	 * 构造方法
	 */
	public AnalysisDynaRole() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
	}
	


	/**
	 * Description:查询流程角色的成员ID
	 * Create Time: 2019年1月18日 上午9:43:41
	 * @author zhongcg 
	 * @param tzms_wflinsid 业务流程实例ID
	 * @param tzms_stpinsid 流程步骤实例ID
	 * @param tzms_userId 当前登录人ID
	 * @param tzms_wfrodn_tid 流程角色ID
	 * @return List<String>
	 */
	public List<String> getUserIds(String tzms_wflinsid, String tzms_stpinsid, String tzms_userId, String tzms_wfrodn_tid) {
		List<String> userIds = new ArrayList<>();
		try {
			logger.info("★★★角色解析开始★★★");
			logger.info("角色解析参数：tzms_wflinsid：\""+ tzms_wflinsid +"\"，tzms_stpinsid：\""+ tzms_stpinsid +"\"，tzms_userId：\""+ tzms_userId +"\"，tzms_wfrodn_tid：\""+ tzms_wfrodn_tid +"\"");
			
			//角色类型，角色ID,职位ID
			Map<String, Object> wrfodnMap = sqlQuery.queryForMap("SELECT tzms_role_type,tzms_rolegw_uniqueid FROM tzms_wfrodn_tBase WHERE cast(tzms_wfrodn_tid as nvarchar(36))=?", 
					new Object[] { tzms_wfrodn_tid });
			
			if(wrfodnMap == null) {
				throw new TzException("【角色解析错误】传入的角色ID：\"" + tzms_wfrodn_tid + "\"对应的角色不存在");
			}
			//角色类型
			String tzmsRoleType = wrfodnMap.get("tzms_role_type") == null ? "" : wrfodnMap.get("tzms_role_type").toString();
			
			switch (tzmsRoleType) {
			case "1":	//静态角色
				userIds = this.getUserIdsForStaticRole(tzms_wfrodn_tid);
				break;
			case "2":	//静态职位
				userIds = this.getUserIdsForStaticPosition(tzms_wfrodn_tid);	
				break;
			case "3":	//动态角色
				userIds = this.getUserIdsForDynamicRole(tzms_wflinsid, tzms_stpinsid, tzms_wfrodn_tid, tzms_userId);
				break;
			case "4":	//定制代码开发
				userIds = this.getUserIdsForCode(tzms_wflinsid, tzms_stpinsid, tzms_wfrodn_tid);
				break;
			default:
				throw new TzException("【角色解析错误】角色类型不正确，请检查流程角色配置中角色类型是否配置正确");
			}
		} catch (TzException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【角色解析错误】发生严重错误，", e);
		}
		return userIds;
	}
	
	
	
	
	/**
	 * 静态角色
	 * Description:
	 * Create Time: 2019年1月18日 上午9:51:10
	 * @author zhongcg 
	 * @param tzms_wfrodn_tid 流程角色ID
	 * @return List<String>
	 * @throws TzException
	 */
	private List<String> getUserIdsForStaticRole(String tzms_wfrodn_tid) throws TzException {
		List<String> userIds = new ArrayList<>();
		try {
			String sql = "select A.tzms_user_uniqueid from tzms_tea_defn_tBase A,tzms_wfrodn_t_tzms_tea_defn_tBase B WHERE A.tzms_tea_defn_tid=B.tzms_tea_defn_tid AND cast(B.tzms_wfrodn_tid as nvarchar(36))=?";
			List<String> userList = sqlQuery.queryForList(sql, new Object[] { tzms_wfrodn_tid }, "String");
			
			if(userList != null && userList.size() > 0) {
				for (String userId : userList) {
					if(StringUtils.isNotBlank(userId) && !userIds.contains(userId)) {
						userIds.add(userId);
					}
				}
			}
		} catch (Exception e) {
			throw new TzException("【角色解析错误】静态角色解析异常错误", e);
		}
		return userIds;
	}
	
	
	
	
	/**
	 * 静态职位
	 * Description:
	 * Create Time: 2019年1月18日 上午9:51:48
	 * @author zhongcg 
	 * @param tzms_wfrodn_tid 角色ID
	 * @return List<String>
	 * @throws TzException
	 */
	private List<String> getUserIdsForStaticPosition(String tzms_wfrodn_tid) throws TzException {
		List<String> userIds = new ArrayList<>();
		try {
			//查询该职位所有的员工id
			String sql = "SELECT A.tzms_user_uniqueid FROM tzms_tea_defn_tBase A,tzms_post_tea_tBase B,tzms_position_mgr_tBase C,tzms_wfrodn_t_tzms_position_tBase D WHERE A.tzms_tea_defn_tid = B.tzms_staff_uniqueid AND B.tzms_position_uniqueid=C.tzms_position_mgr_tid AND C.tzms_position_mgr_tid=D.tzms_position_mgr_tid and cast(D.tzms_wfrodn_tid as nvarchar(36))=?";
			List<String> userList = sqlQuery.queryForList(sql, new Object[] { tzms_wfrodn_tid }, "String");
			
			if(userList != null && userList.size() > 0) {
				for (String userId : userList) {
					if(StringUtils.isNotBlank(userId) && !userIds.contains(userId)) {
						userIds.add(userId);
					}
				}
			}
		} catch (Exception e) {
			throw new TzException("【角色解析错误】静态角色解析异常错误", e);
		}
		
		return userIds;
	}
	
	


	/**
	 * 定制代码开发
	 * Description:
	 * Create Time: 2019年1月18日 上午9:52:30
	 * @author zhongcg 
	 * @param tzms_wflinsid	流程实例ID
	 * @param tzms_stpinsid	步骤实例ID
	 * @param tzms_wfrodn_tid 流程角色ID
	 * @return List<String>
	 * @throws TzException
	 */
	@SuppressWarnings("unchecked")
	private List<String> getUserIdsForCode(String tzms_wflinsid,String tzms_stpinsid,String tzms_wfrodn_tid) throws TzException {
		
		List<String> userIds = new ArrayList<>();
		try {
			//定制开发接口
			String tzms_sys_varid = sqlQuery.queryForObject("select tzms_sys_varid from tzms_wfrodn_tBase where cast(tzms_wfrodn_tid as nvarchar(36))=?", 
					new Object[] { tzms_wfrodn_tid }, "String");
			if(StringUtils.isBlank(tzms_sys_varid)) {
				throw new TzException("【角色解析错误】定制代码开发\""+ tzms_wfrodn_tid +"\"定义不存在");
			}
			
			//流程业务数据ID
			String dataRecId = "";
			if(StringUtils.isNotBlank(tzms_wflinsid)) {
				dataRecId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
						new Object[] { tzms_wflinsid }, "String");
			}
			
			//系统变量解析，参数：流程实例ID、步骤实例ID、业务数据ID
			String[] param = { tzms_wflinsid, tzms_stpinsid, dataRecId };
			AnalysisDynaSysVar analysisDynaSysVar = new AnalysisDynaSysVar();
			analysisDynaSysVar.setM_SysVarID(tzms_sys_varid);
			analysisDynaSysVar.setM_SysVarParam(param);
			
			Object obj = analysisDynaSysVar.GetVarValue();
			if(obj == null) {
				return userIds;
			}
			
			try {
				List<String> list = (List<String>) obj;
				if(list != null && list.size() > 0) {
					for(String userid : list) {
						if(StringUtils.isNotBlank(userid) && !userIds.contains(userid)) {
							userIds.add(userid);
						}
					}
				}
			}catch (Exception e) {
				throw new TzException("【角色解析错误】定制代码开发返回值配置错误", e);
			}
		}catch (TzException e) {
			throw e;
		}catch (Exception e) {
			throw new TzException("【角色解析错误】定制代码开发角色解析异常错误", e);
		}

		return userIds;
	}

	
	
	
	
	/**
	 * Description: 动态角色
	 * Create Time: 2019年1月18日 上午9:53:19
	 * @author zhongcg 
	 * @param tzms_wflinsid 流程实例ID
	 * @param tzms_stpinsid 步骤实例ID
	 * @param tzms_wfrodn_tid 流程角色ID
	 * @param tzms_userId 当前登录人
	 * @return List<String>
	 * @throws TzException
	 */
	@SuppressWarnings("unchecked")
	private List<String> getUserIdsForDynamicRole(String tzms_wflinsid, String tzms_stpinsid, String tzms_wfrodn_tid, String tzms_userId) 
			throws TzException {
		List<String> userIds = new ArrayList<>();
		try {
			Map<String, Object> dynamicMap = sqlQuery.queryForMap("select tzms_query_type,tzms_parameter,tzms_field_name,tzms_busi_field_means,tzms_adrule_enabled,tzms_adrule_type,tzms_adrolegw_uniqueid,tzms_adrolehb_uniqueid,tzms_wflstp_uniqueid from tzms_wfrodn_tBase where tzms_wfrodn_tid=?", 
					new Object[] { tzms_wfrodn_tid });
			
			//查找依据，1-运行参数、2-页面字段
			String queryType = dynamicMap.get("tzms_query_type") == null ? "" : dynamicMap.get("tzms_query_type").toString();
			//运行参数类型，1-当前登录人、2-步骤操作人、3-步骤访问角色
			String tzmsPara = dynamicMap.get("tzms_parameter") == null ? "" : dynamicMap.get("tzms_parameter").toString();
			//页面字段
			String fieldName = dynamicMap.get("tzms_field_name") == null ? "" : dynamicMap.get("tzms_field_name").toString();
			//业务字段含义，1-人员、2-部门
			int tzms_busi_field_means = dynamicMap.get("tzms_busi_field_means") == null ? 0 : (int) dynamicMap.get("tzms_busi_field_means");
			//启用高级规则查找
			boolean adrule_enabled = dynamicMap.get("tzms_adrule_enabled") == null ? false : (boolean) dynamicMap.get("tzms_adrule_enabled");
			//高级查找规则，1-根据职位级别查找、2-根据汇报关系查找
			String tzms_adrule_type = dynamicMap.get("tzms_adrule_type") == null ? "" : dynamicMap.get("tzms_adrule_type").toString();
			//高级规则-查找职位级别
			String tzms_adrolegw_uniqueid = dynamicMap.get("tzms_adrolegw_uniqueid") == null ? "" : dynamicMap.get("tzms_adrolegw_uniqueid").toString();
			//高级规则-查找汇报关系
			String tzms_adrolehb_uniqueid = dynamicMap.get("tzms_adrolehb_uniqueid") == null ? "" : dynamicMap.get("tzms_adrolehb_uniqueid").toString();
			//运行参数 - 业务流程步骤信息
			String tzms_wflstp_uniqueid = dynamicMap.get("tzms_wflstp_uniqueid") == null ? "" : dynamicMap.get("tzms_wflstp_uniqueid").toString();
			
			//关联业务实体
			String tableName = "";
			//业务数据ID 
			String wflrecordId = "";
			Map<String, Object> wflMap = sqlQuery.queryForMap("SELECT A.tzms_wflrecord_uniqueid,B.tzms_entity_name FROM tzms_wflins_tbl A join tzms_wfcldn_tBase B on (A.tzms_wfcldn_uniqueid=cast(B.tzms_wfcldn_tid as nvarchar(36))) where A.tzms_wflinsid=?", 
					new Object[] { tzms_wflinsid });
			if(wflMap != null) {
				tableName = wflMap.get("tzms_entity_name") == null ? "" : wflMap.get("tzms_entity_name").toString();
				wflrecordId = wflMap.get("tzms_wflrecord_uniqueid") == null ? "" : wflMap.get("tzms_wflrecord_uniqueid").toString();
			}

			//页面字段
			String ymzd = "";
			//查找依据取值是否为空
			if(StringUtils.isBlank(queryType) 
					|| (!"1".equals(queryType) && !"2".equals(queryType))) {
				throw new TzException("【角色解析错误】动态角色配置错误，查找依据未配置或配置不正确");
			}

			//查找依据：运行参数
			if("1".equals(queryType)) {
				//运行参数取值是否正确
				if(StringUtils.isBlank(tzmsPara) 
						|| (!"1".equals(tzmsPara) && !"2".equals(tzmsPara) && !"3".equals(tzmsPara))) {
					throw new TzException("【角色解析错误】动态角色配置错误，运行参数类型未配置或配置不正确");
				}
				
				//1-当前登录人
				if("1".equals(tzmsPara)) {
					if(StringUtils.isNotBlank(tzms_userId) && !userIds.contains(tzms_userId)) {
						userIds.add(tzms_userId);
					}
				}
				
				//2-步骤操作人
				if("2".equals(tzmsPara)) {
					//流程参数判断
					if(StringUtils.isBlank(tzms_wflinsid)) {
						throw new TzException("【角色解析错误】动态角色解析失败，查找依据为运行参数-步骤操作人，角色解析时未传入流程参数");
					}
					if(StringUtils.isBlank(tzms_wflstp_uniqueid)) {
						throw new TzException("【角色解析错误】动态角色配置错误，查找依据为运行参数-步骤操作人，未配置步骤操作人对应步骤");
					}
					
					//根据设置的步骤操作人查询步骤责任人，非抄送任务
					List<String> list = sqlQuery.queryForList("SELECT tzms_stpproid FROM tzms_stpins_tbl WHERE tzms_wflinsid=? AND tzms_wflstpid=? and tzms_asgmethod<>'1' and tzms_stpproid is not null", 
							new Object[] { tzms_wflinsid, tzms_wflstp_uniqueid }, "String");
					if(list != null && list.size() > 0) {
						for (String userId : list) {
							if(StringUtils.isNotBlank(userId) && !userIds.contains(userId)) {
								userIds.add(userId);
							}
						}
					}
				}
				
				//3-步骤访问角色
				if("3".equals(tzmsPara)) {
					//流程参数判断
					if(StringUtils.isBlank(tzms_wflinsid)) {
						throw new TzException("【角色解析错误】动态角色解析失败，查找依据为运行参数-步骤访问角色，角色解析时未传入流程参数");
					}
					if(StringUtils.isBlank(tzms_wflstp_uniqueid)) {
						throw new TzException("【角色解析错误】动态角色配置错误，查找依据为运行参数-步骤访问角色，未配置对应步骤");
					}
					//查询指定步骤对应的角色
					String tzms_wfrole_uniqueid = sqlQuery.queryForObject("SELECT tzms_wfrole_uniqueid FROM tzms_wflstp_tBase WHERE cast(tzms_wflstp_tid as nvarchar(36))=?", 
							new Object[] { tzms_wflstp_uniqueid },"String");
					
					//如果为当前角色，直接返回，防止死循环
					if(tzms_wfrodn_tid.equalsIgnoreCase(tzms_wfrole_uniqueid)) {
						return userIds;
					}
					userIds = this.getUserIds(tzms_wflinsid, tzms_stpinsid, tzms_userId, tzms_wfrole_uniqueid);
				}
			}
			
			//查找依据：页面字段
			if("2".equals(queryType)) {
				//业务流程关联实体
				if(StringUtils.isBlank(tableName)) {
					throw new TzException("【角色解析错误】动态角色解析错误，没有查找到流程关联实体");
				}
				//页面字段
				if(StringUtils.isBlank(fieldName)) {
					throw new TzException("【角色解析错误】动态角色配置错误，查找依据为页面字段，未配置页面字段");
				}
				
				//查询页面字段取值
				String ymzdSql = "SELECT " + fieldName + " FROM " + tableName + "Base where cast(" + tableName + "id as nvarchar(36))=?";
				ymzd = sqlQuery.queryForObject(ymzdSql, new Object[] { wflrecordId }, "String");
				if(StringUtils.isBlank(ymzd)) {
					return userIds;
				}
				
				//如果人员字段配置的是部门,并且没有启用高级查找， 返回该部门所有人
				if(adrule_enabled == false) {	//没有启用高级查找
					if(tzms_busi_field_means == 2) {	//业务字段含义：部门
						//部门信息
						String tzms_org = ymzd;

						//部门存在，并且人员还没有找到，那么一直查找上级部门对应的责任人
						while(StringUtils.isNotBlank(tzms_org)) {
							//1.查询该部门的所有员工
							List<String> list = sqlQuery.queryForList("SELECT distinct B.tzms_user_uniqueid from tzms_org_user_tBase A,tzms_tea_defn_tBase B where A.tzms_tea_defnid=B.tzms_tea_defn_tid AND cast(A.tzms_org_uniqueid as nvarchar(36))=? and tzms_user_uniqueid is not null", 
									new Object[] { tzms_org }, "String");
							
							//查找到人员，直接返回
							if(list != null && list.size() > 0) {
								userIds.addAll(list);
								break;
							}
							
							//没有查找到人员，将上级部门赋值给部门,继续查找上级部门
							tzms_org = sqlQuery.queryForObject("SELECT tzms_parent_deptid FROM tzms_org_structure_tree_tBase WHERE tzms_org_structure_tree_tid=?", 
									new Object[] { tzms_org }, "String");
						}
					}else if(tzms_busi_field_means == 1){	//业务字段含义：人员
						//用户是否存在
						String exists = sqlQuery.queryForObject("select top 1 'Y' from tzms_tea_defn_tBase where cast(tzms_user_uniqueid as nvarchar(36))=?", 
								new Object[] { ymzd }, "String");
						if("Y".equals(exists) && !userIds.contains(ymzd)) {
							userIds.add(ymzd);
						}
					}
				}
			}
			
			
			//启用高级规则查找,以上操作查询的结果作为高级查找的输入，流程角色的成员由高级查找的输出结果确定
			if(adrule_enabled == true) {
				//高级查找规则是否配置，1-根据职位级别查找、2-根据汇报关系查找
				if(StringUtils.isBlank(tzms_adrule_type) 
						|| (!"1".equals(tzms_adrule_type) && !"2".equals(tzms_adrule_type))) {
					throw new TzException("【角色解析错误】动态角色配置错误，高级查找规则未配置或配置不正确");
				}
				
				//将集合转数组
				String[] para = new String[userIds.size()];
				userIds.toArray(para);
				
				//用户ID置为空
				userIds.clear();
				
				/**
				 * 1、如果输入的是人员，则总体上还按原来的逻辑进行解析，但对于根据职位级别查找需要同时满足如下几个条件：
     			 *	a)查找的人员必须归属当输入人员所归属的部门；
     			 *	b)查找的人员必须在当输入人员所归属的部门中的一个职位或者多个职位下，同时这些职位对应的职位类型中至少有一个是动态角色配置中指定的职位级别或者职位类型；
     			 *	c)查找的人员的职位级别或者职位类型必须与动态角色配置中指定的职位级别或者职位类型一致、相同；
     			 *	d)如果在当前输入人所归属的部门没有找到对应的人员，则到上级部门按上述逻辑接着进行查找，直到找到对应的责任人；如果找到根节点部门还是没有找到对应责任人，则报错；
				 * 2、如果输入的是部门，则高级查找只能根据职位级别进行查找，不能根据汇报关系进行查找；
				 *	根据指定部门查找的逻辑如：参照第1点即可。第1点中是将输入人转化成归属部门后查找责任人，此处直接指定了部门，则少了这个转换过程，直接根据指定的部门根据职位级别进行查找责任人。
				 */
				
				//根据职务查找
				if("1".equals(tzms_adrule_type)) {	
					//判断职务是否配置
					if(StringUtils.isBlank(tzms_adrolegw_uniqueid)) {
						throw new TzException("【角色解析错误】动态角色配置错误，高级查找-根据职务查找未配置职务");
					}
					
					//页面字段，且业务字段含义：部门
					if("2".equals(queryType) && tzms_busi_field_means == 2) {
						userIds = this.advancedSearchByPosition(tzms_adrolegw_uniqueid, "B", ymzd, null);
					}else {
						String defaultDept = "";	//人员默认部门
						if("2".equals(queryType) && tzms_busi_field_means == 1) {	//人员
							para = new String[] { ymzd };
							
							//页面字段为申请人字段，且申请人归属多个部门的情况，需要根据表单选择的申请部门查找
							if("tzms_apply_user".equalsIgnoreCase(fieldName)) {
								//判断申请人是否存在多个部门下
								int deptCount = sqlQuery.queryForObject("select count(*) from tzms_org_user_tBase A where exists(select 'Y' from tzms_tea_defn_t where cast(tzms_user_uniqueid as nvarchar(36))=? and tzms_tea_defn_tid=A.tzms_tea_defnid)", 
										new Object[] { ymzd }, "int");
								if(deptCount > 1) {
									//获取表单中的申请人部门
									//拼接查询语句
									String sql = "SELECT tzms_apply_dept FROM " + tableName + "Base where cast(" + tableName + "id as nvarchar(36))=?";
									String sqrDeptId = sqlQuery.queryForObject(sql, new Object[] { wflrecordId }, "String");
									if(StringUtils.isNotBlank(sqrDeptId)) {
										defaultDept = sqrDeptId;
									}
								}
							}
						}
						
						userIds = this.advancedSearchByPosition(tzms_adrolegw_uniqueid, "A", defaultDept, para);
					}
				}
				
				//根据汇报关系查找
				if("2".equals(tzms_adrule_type)) {
					//判断是否配置汇报关系
					if(StringUtils.isBlank(tzms_adrolehb_uniqueid)) {
						throw new TzException("【角色解析错误】动态角色配置错误，高级查找-根据汇报关系查找未配置汇报关系");
					}
					
					String defaultDept = "";	//人员默认部门
					if("2".equals(queryType) && tzms_busi_field_means == 1) {	//人员
						para = new String[] { ymzd };
						
						//页面字段为申请人字段，且申请人归属多个部门的情况，需要根据表单选择的申请部门查找
						if("tzms_apply_user".equalsIgnoreCase(fieldName)) {
							//判断申请人是否存在多个部门下
							int deptCount = sqlQuery.queryForObject("select count(*) from tzms_org_user_tBase A where exists(select 'Y' from tzms_tea_defn_t where cast(tzms_user_uniqueid as nvarchar(36))=? and tzms_tea_defn_tid=A.tzms_tea_defnid)", 
									new Object[] { ymzd }, "int");
							if(deptCount > 1) {
								//获取表单中的申请人部门
								//拼接查询语句
								String sql = "SELECT tzms_apply_dept FROM " + tableName + "Base where cast(" + tableName + "id as nvarchar(36))=?";
								String sqrDeptId = sqlQuery.queryForObject(sql, new Object[] { wflrecordId }, "String");
								if(StringUtils.isNotBlank(sqrDeptId)) {
									defaultDept = sqrDeptId;
								}
							}
						}
					}
					
					//根据汇报关系查询应用程序类ID
					String tzms_cls_uniqueid = sqlQuery.queryForObject("SELECT tzms_cls_uniqueid from tzms_hbgz_dfn_tBase where cast(tzms_hbgz_dfn_tid as nvarchar(36))=?", 
							new Object[] { tzms_adrolehb_uniqueid }, "String");
					if(StringUtils.isNotBlank(tzms_cls_uniqueid)) {
						//解析汇报关系类
						try {
							AnalysisClsMethod analysisCls = new AnalysisClsMethod(tzms_cls_uniqueid);
							//设置参数类型，如果是DLL类只能传入基本类型，参数类型需要与参数对应
							String[] parameterTypes = { "String[]", "String" };
							Object[] param = new Object[] { para, defaultDept };
							//设置类方法参数
							analysisCls.setJavaClsParameter(parameterTypes, param);
							
							Object obj = analysisCls.execute();
							
							List<String> users = (List<String>) obj;
							if(users != null && users.size() > 0) {
								userIds.addAll(users);
							}
						}catch (Exception e) {
							throw new TzException("【角色解析错误】动态角色配置错误，高级查找-根据汇报关系查找，汇报关系解析异常。", e);
						}
					}else {
						throw new TzException("【角色解析错误】动态角色配置错误，高级查找-根据汇报关系查找，指定的汇报关系未配置");
					}
				}
			}
			//去重
			userIds = userIds.stream().distinct().collect(Collectors.toList());
		} catch (TzException e) {
			throw e;
		} catch (Exception e) {
			throw new TzException("【角色解析错误】定制代码开发返回值配置错误", e);
		}
		
		return userIds;
	}
	

	
	
	/**
	 * 角色-高级查找-根据职务查找
	 * 根据职务查找规则：先查找部门（如果多部门，按所属所有部门查找），根据部门（如果没找到向父级部门查找）找到指定职务的所有职位下的人员。
	 * @param jobcdDefId	职务（职位级别）
	 * @param searchType	搜索类型，A-根据人员搜索（users），B-根据部门搜索（strDeptId）
	 * @param strDeptId		部门ID
	 * @param users			人员列表
	 * @return
	 */
	private List<String> advancedSearchByPosition(String jobcdDefId, String searchType, String strDeptId, String[] users){
		List<String> userIds = new ArrayList<String>();
		
		if("A".equals(searchType)) {
			if(users == null || users.length == 0) {
				return userIds;
			}
			
			for(String userId : users) {
				//查询人员所属部门
				String sql = "select tzms_org_uniqueid from tzms_org_user_tBase A where exists(select 'Y' from tzms_tea_defn_t where tzms_user_uniqueid=? and tzms_tea_defn_tid=A.tzms_tea_defnid)";
				//默认部门
				if(StringUtils.isNotBlank(strDeptId)) {
					sql += " and cast(A.tzms_org_uniqueid as nvarchar(36))='"+ strDeptId +"'";
				}
				
				List<String> deptList = sqlQuery.queryForList(sql, new Object[] { userId }, "String");
				if(deptList != null && deptList.size() > 0) {
					for(String deptId: deptList) {
						
						//部门下是否有指定职务的人员
						List<String> deptUsers = sqlQuery.queryForList("select tzms_user_uniqueid from tzms_position_mgr_tBase A join tzms_post_tea_tBase B on(A.tzms_position_mgr_tId=B.tzms_position_uniqueid) join tzms_tea_defn_tBase C on(B.tzms_staff_uniqueid=C.tzms_tea_defn_tId) where A.tzms_lon_dept=? and A.tzms_pos_level_uniqueid=? and C.tzms_user_uniqueid is not null", 
								new Object[] { deptId, jobcdDefId }, "String");
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
											new Object[] { parentDeptId, jobcdDefId }, "String");
									if(pDeptUsers != null && pDeptUsers.size() > 0) {
										deptUsers.addAll(pDeptUsers);
										deptUsers = deptUsers.stream().distinct().collect(Collectors.toList());	//去重
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
			}
		}else if("B".equals(searchType)) {
			//部门下是否有指定职务的人员
			List<String> deptUsers = sqlQuery.queryForList("select tzms_user_uniqueid from tzms_position_mgr_tBase A join tzms_post_tea_tBase B on(A.tzms_position_mgr_tId=B.tzms_position_uniqueid) join tzms_tea_defn_tBase C on(B.tzms_staff_uniqueid=C.tzms_tea_defn_tId) where A.tzms_lon_dept=? and A.tzms_pos_level_uniqueid=? and C.tzms_user_uniqueid is not null", 
					new Object[] { strDeptId, jobcdDefId }, "String");
			if(deptUsers != null && deptUsers.size() > 0) {
				userIds.addAll(deptUsers);
				//去重
				userIds = userIds.stream().distinct().collect(Collectors.toList());
			}else {
				//继续查找父级部门
				deptUsers = new ArrayList<String>();
				
				String searchDeptId = strDeptId;
				while(StringUtils.isNotBlank(searchDeptId) && deptUsers.size() == 0) {
					//父级部门
					String parentDeptId = sqlQuery.queryForObject("SELECT tzms_parent_deptid FROM tzms_org_structure_tree_tBase WHERE tzms_org_structure_tree_tid=? and tzms_parent_deptid is not null", 
							new Object[] { searchDeptId }, "String");
					
					if(StringUtils.isNotBlank(parentDeptId)) {
						List<String> pDeptUsers = sqlQuery.queryForList("select tzms_user_uniqueid from tzms_position_mgr_tBase A join tzms_post_tea_tBase B on(A.tzms_position_mgr_tId=B.tzms_position_uniqueid) join tzms_tea_defn_tBase C on(B.tzms_staff_uniqueid=C.tzms_tea_defn_tId) where A.tzms_lon_dept=? and A.tzms_pos_level_uniqueid=? and C.tzms_user_uniqueid is not null", 
								new Object[] { parentDeptId, jobcdDefId }, "String");
						if(pDeptUsers != null && pDeptUsers.size() > 0) {
							deptUsers.addAll(pDeptUsers);
							deptUsers = deptUsers.stream().distinct().collect(Collectors.toList());	//去重
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
		}
		
		return userIds;
	}
}