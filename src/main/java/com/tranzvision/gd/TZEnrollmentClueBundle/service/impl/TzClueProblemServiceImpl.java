package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzWtxsTmpTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsBmbTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsLogTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzWtxsTmpT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzWtxsTmpTKey;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsLogT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 有问题的线索
 * @author LuYan 2017-11-6
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueProblemServiceImpl")
public class TzClueProblemServiceImpl extends FrameworkImpl {

	@Autowired
	private TZGDObject tzSqlObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private PsTzXsxsBmbTMapper psTzXsxsBmbTMapper;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private PsTzXsxsLogTMapper psTzXsxsLogTMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzWtxsTmpTMapper psTzWtxsTmpTMapper;
	
	
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		try {
			
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			ArrayList<Map<String, Object>> listData = new ArrayList<>();
			
			String sql = tzSqlObject.getSQLText("SQL.TZEnrollmentClueServiceImpl.TzGetProblemClueInfo");
			List<Map<String, Object>> listClue = sqlQuery.queryForList(sql,new Object[]{orgId,oprid});
			for(Map<String, Object> mapClue :listClue) {
				String clueId = mapClue.get("TZ_LEAD_ID") == null ? "" : mapClue.get("TZ_LEAD_ID").toString();
				String wtType = mapClue.get("TZ_WT_TYPE") == null ? "" : mapClue.get("TZ_WT_TYPE").toString();
				String clueDesc = mapClue.get("TZ_LEAD_DESCR") == null ? "" : mapClue.get("TZ_LEAD_DESCR").toString();
				String orderNum = mapClue.get("TZ_ORDER_NUM") == null ? "" : mapClue.get("TZ_ORDER_NUM").toString();
				String realname = mapClue.get("TZ_REALNAME") == null ? "" : mapClue.get("TZ_REALNAME").toString();
				String mobile = mapClue.get("TZ_MOBILE") == null ? "" : mapClue.get("TZ_MOBILE").toString();
				String company = mapClue.get("TZ_COMP_CNAME") == null ? "" : mapClue.get("TZ_COMP_CNAME").toString();
				String createWay = mapClue.get("TZ_RSFCREATE_WAY") == null ? "" : mapClue.get("TZ_RSFCREATE_WAY").toString();
				String createWayDesc = mapClue.get("TZ_RSFCREATE_WAY_DESC") == null ? "" : mapClue.get("TZ_RSFCREATE_WAY_DESC").toString();
				String localId = mapClue.get("TZ_XSQU_ID") == null ? "" : mapClue.get("TZ_XSQU_ID").toString();
				String localName = mapClue.get("TZ_LOCAL_NAME") == null ? "" : mapClue.get("TZ_LOCAL_NAME").toString();
				String createDttm = mapClue.get("ROW_ADDED_DTTM") == null ? "" : mapClue.get("ROW_ADDED_DTTM").toString();
				String zrrOprid = mapClue.get("TZ_ZR_OPRID") == null ? "" : mapClue.get("TZ_ZR_OPRID").toString();
				String zrrName = mapClue.get("TZ_ZRR_NAME") == null ? "" : mapClue.get("TZ_ZRR_NAME").toString();
				
				Map<String, Object> mapData = new HashMap<String,Object>();
				mapData.put("clueId", clueId);
				mapData.put("wtType", wtType);
				mapData.put("clueDesc", wtType+"-"+orderNum+","+clueDesc);
				mapData.put("name", realname);
				mapData.put("mobile", mobile);
				mapData.put("company", company);
				mapData.put("createWay", createWay);
				mapData.put("createWayDesc", createWayDesc);
				mapData.put("localName", localName);
				mapData.put("createDttm", createDttm);
				mapData.put("chargeName", zrrName);
				mapData.put("suggestOperation", "");
				mapData.put("systemRuleTip", "");
				mapData.put("isChecked", "");
				
				listData.add(mapData);
			}	
			
			//总数
			Integer total = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_WTXS_TMP_T WHERE TZ_JG_ID=? AND OPRID=?", new Object[]{orgId,oprid},"Integer"); 
			
			mapRet.replace("total", total);
			mapRet.replace("root", listData);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
		
	}
	
	
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			//更新问题线索表中建议操作字段为空
			sqlQuery.update("UPDATE PS_TZ_WTXS_TMP_T SET TZ_JY_CZ='' WHERE OPRID=?",new Object[]{oprid});
			sqlQuery.update("COMMIT");
			
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				String clueId = jacksonUtil.getString("clueId");
				String clueDesc = jacksonUtil.getString("clueDesc");
				String chargeOprid = jacksonUtil.getString("chargeOprid");
				String chargeName = jacksonUtil.getString("chargeName");
				String suggestOperation = jacksonUtil.getString("suggestOperation");
				
				//选择了责任人
				if(chargeOprid!=null && !"".equals(chargeOprid)) {
					//查询线索当前状态、责任人
					String clueStateNow = "",chargeOpridNow = "";
					Map<String,Object> mapNow = sqlQuery.queryForMap("SELECT TZ_LEAD_STATUS,TZ_ZR_OPRID FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID=?", new Object[]{clueId});
					if(mapNow!=null) {
						clueStateNow = mapNow.get("TZ_LEAD_STATUS") == null ? "" : mapNow.get("TZ_LEAD_STATUS").toString();
						chargeOpridNow = mapNow.get("TZ_ZR_OPRID") == null ? "" : mapNow.get("TZ_ZR_OPRID").toString();
					}
					
					if(!chargeOprid.equals(chargeOpridNow)) {
						//更新线索责任人
						PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
						PsTzXsxsInfoT.setTzLeadId(clueId);
						PsTzXsxsInfoT.setTzLeadStatus("C");
						PsTzXsxsInfoT.setTzZrOprid(chargeOprid);
						PsTzXsxsInfoT.setRowLastmantOprid(oprid);
						PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
						psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
						
						//更新线索日志表	
						String tzOperateDesc="转交线索，责任人" + chargeName ;
						
						PsTzXsxsLogT PsTzXsxsLogT=new PsTzXsxsLogT();
						int operateId=getSeqNum.getSeqNum("TZ_XSXS_LOG_T", "TZ_OPERATE_ID");
						PsTzXsxsLogT.setTzOperateId(operateId);
						PsTzXsxsLogT.setTzLeadId(clueId);
						PsTzXsxsLogT.setTzLeadStatus1(clueStateNow);
						PsTzXsxsLogT.setTzLeadStatus2("C");
						//PsTzXsxsLogT.setTzDemo(demo);
						PsTzXsxsLogT.setTzOperateDesc(tzOperateDesc);
						PsTzXsxsLogT.setRowAddedOprid(oprid);
						PsTzXsxsLogT.setRowAddedDttm(new java.util.Date());
						PsTzXsxsLogT.setRowLastmantOprid(oprid);
						PsTzXsxsLogT.setRowLastmantDttm(new java.util.Date());
						psTzXsxsLogTMapper.insert(PsTzXsxsLogT);
					}
					
				}
				
				PsTzWtxsTmpTKey psTzWtxsTmpTKey = new PsTzWtxsTmpTKey();
				psTzWtxsTmpTKey.setOprid(oprid);
				psTzWtxsTmpTKey.setTzLeadId(clueId);
				PsTzWtxsTmpT psTzWtxsTmpT = psTzWtxsTmpTMapper.selectByPrimaryKey(psTzWtxsTmpTKey);
				if(psTzWtxsTmpT==null) {
					
				} else {
					psTzWtxsTmpT.setTzJyCz(suggestOperation);
					psTzWtxsTmpT.setRowLastmantDttm(new Date());
					psTzWtxsTmpT.setRowLastmantOprid(oprid);
					psTzWtxsTmpTMapper.updateByPrimaryKeySelective(psTzWtxsTmpT);
				}
				
			}
			
			strRet = this.dealWithProblemClue(oprid,errMsg);
			
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	/*处理问题线索*/
	public String dealWithProblemClue(String oprid,String[] errMsg) {
		String strRet = "";
		Map<String,Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			String desc_sql = "SELECT DISTINCT TZ_LEAD_DESCR FROM PS_TZ_WTXS_TMP_T WHERE OPRID=? AND TZ_JY_CZ<>''";
			List<Map<String, Object>> listDesc = sqlQuery.queryForList(desc_sql,new Object[]{oprid});
			
			for(Map<String, Object> mapDesc : listDesc) {
				String clueDesc = mapDesc.get("TZ_LEAD_DESCR") == null ? "" : mapDesc.get("TZ_LEAD_DESCR").toString();
				
				String clue_sql = "SELECT TZ_LEAD_ID,TZ_JY_CZ FROM PS_TZ_WTXS_TMP_T WHERE OPRID=? AND TZ_JY_CZ<>'' AND TZ_LEAD_DESCR=?";
				List<Map<String, Object>> listClue = sqlQuery.queryForList(clue_sql,new Object[]{oprid,clueDesc});
				
				/*查询保留操作的个数*/
				Integer stayCount = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_WTXS_TMP_T WHERE OPRID=? AND TZ_JY_CZ='A' AND TZ_LEAD_DESCR=?", new Object[]{oprid,clueDesc},"Integer");
				if(stayCount!=null && !"".equals(stayCount)) {
					
				} else {
					stayCount = 0;
				}
					
				
				for(Map<String, Object> mapClue : listClue) {
					String clueId = mapClue.get("TZ_LEAD_ID") == null ? "" : mapClue.get("TZ_LEAD_ID").toString();
					String jycz = mapClue.get("TZ_JY_CZ") == null ? "" : mapClue.get("TZ_JY_CZ").toString();
					
					if("A".equals(jycz)) {
						//保留
						
						//如果存在多个保留的线索，不关联报名表，仅有一个时，才关联
						if(stayCount==1) {
							//查询线索是否有关联报名表
							Integer bmbCount = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_BMB_T WHERE TZ_LEAD_ID=?", new Object[]{clueId},"Integer");
							if(bmbCount>0) {
								//已关联，不处理
							} else {
								//查询问题线索组内的其他线索是否关联报名表，并找到最新的报名表
								String sql_bmb = "SELECT A.TZ_LEAD_ID ,B.TZ_APP_INS_ID";
								sql_bmb += " FROM PS_TZ_WTXS_TMP_T A ,PS_TZ_XSXS_BMB_T B";
								sql_bmb += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND A.OPRID=? AND A.TZ_LEAD_DESCR=? AND A.TZ_LEAD_ID<>?";
								sql_bmb += " ORDER BY CAST(B.TZ_APP_INS_ID AS SIGNED) DESC";
								sql_bmb += " LIMIT 0,1";
								
								Map<String, Object> mapBmb = sqlQuery.queryForMap(sql_bmb,new Object[]{oprid,clueDesc,clueId});
								if(mapBmb!=null) {
									String clueId_bmb = mapBmb.get("TZ_LEAD_ID") == null ? "" : mapBmb.get("TZ_LEAD_ID").toString();
									String appinsId_bmb = mapBmb.get("TZ_APP_INS_ID") == null ? "" : mapBmb.get("TZ_APP_INS_ID").toString();
									
									//把最新的报名表关联到保留的线索下，并解除其与之前线索的关系
									PsTzXsxsBmbT psTzXsxsBmbT = new PsTzXsxsBmbT();
									psTzXsxsBmbT.setTzLeadId(clueId);
									psTzXsxsBmbT.setTzAppInsId(Long.valueOf(appinsId_bmb));
									psTzXsxsBmbT.setRowAddedDttm(new Date());
									psTzXsxsBmbT.setRowAddedOprid(oprid);
									psTzXsxsBmbT.setRowLastmantDttm(new Date());
									psTzXsxsBmbT.setRowLastmantOprid(oprid);
									psTzXsxsBmbTMapper.insertSelective(psTzXsxsBmbT);
									
									sqlQuery.update("DELETE FROM PS_TZ_XSXS_BMB_T WHERE TZ_LEAD_ID=? AND TZ_APP_INS_ID=?",new Object[]{clueId_bmb,appinsId_bmb});
									sqlQuery.update("COMMIT");
								}
							}
						}
					} else if("B".equals(jycz)) {
						//关闭
						
						//查询线索信息
						String beforeStatus = "",zrrOprid = "",zrrName = "";
						String sql_clue = "SELECT A.TZ_LEAD_STATUS,A.TZ_ZR_OPRID ,B.TZ_REALNAME";
						sql_clue += " FROM PS_TZ_XSXS_INFO_T A LEFT JOIN PS_TZ_YHZH_NB_VW B ON A.TZ_ZR_OPRID=B.OPRID";
						sql_clue += " WHERE A.TZ_LEAD_ID=?";
						Map<String, Object> map_clue = sqlQuery.queryForMap(sql_clue,new Object[]{clueId});
						if(map_clue!=null) {
							beforeStatus = map_clue.get("TZ_LEAD_STATUS") == null ? "" : map_clue.get("TZ_LEAD_STATUS").toString();
							zrrOprid = map_clue.get("TZ_ZR_OPRID") == null ? "" : map_clue.get("TZ_ZR_OPRID").toString();
							zrrName = map_clue.get("TZ_REALNAME") == null ? "" : map_clue.get("TZ_REALNAME").toString();
						}
						
						
						//查询保留下来的线索信息
						String stayClueId = "",stayZrrOprid = "",stayZrrName = "";
						String stay_clue_sql = "SELECT A.TZ_LEAD_ID ,B.TZ_ZR_OPRID,C.TZ_REALNAME";
						stay_clue_sql += " FROM PS_TZ_WTXS_TMP_T A ,PS_TZ_XSXS_INFO_T B LEFT JOIN PS_TZ_YHZH_NB_VW C ON B.TZ_ZR_OPRID=C.OPRID";
						stay_clue_sql += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND A.OPRID=? AND A.TZ_JY_CZ='A' AND A.TZ_LEAD_DESCR=?";
						stay_clue_sql += " LIMIT 0,1";
						Map<String, Object> mapStayClue = sqlQuery.queryForMap(stay_clue_sql,new Object[]{oprid,clueDesc});
						if(mapStayClue!=null) {
							stayClueId = mapStayClue.get("TZ_LEAD_ID") == null ? "" : mapStayClue.get("TZ_LEAD_ID").toString();
							stayZrrOprid = mapStayClue.get("TZ_ZR_OPRID") == null ? "" : mapStayClue.get("TZ_ZR_OPRID").toString();
							stayZrrName = mapStayClue.get("TZ_REALNAME") == null ? "" : mapStayClue.get("TZ_REALNAME").toString();
						}
						
						//线索状态改为“关闭”
						String wtxsGbyy = "";
						
						//如果仅有一个保留的线索，记录问题线索关闭原因：转换为线索**，责任人**
						if(stayCount==1) {
							if(!"".equals(stayClueId)) {
								wtxsGbyy = "转换为线索"+stayClueId;
								if(!"".equals(stayZrrName)) {
									wtxsGbyy += "，责任人"+stayZrrName;
								}
							}
						} else {
							//如果存在多个保留的线索，问题线索关闭原因：问题线索
							wtxsGbyy = "问题线索";
							//解除当前线索与报名表的关系，供其他保留线索关联报名表
							sqlQuery.update("DELETE FROM PS_TZ_XSXS_BMB_T WHERE TZ_LEAD_ID=? ",new Object[]{clueId});
							sqlQuery.update("COMMIT");
						}
						
						PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
						PsTzXsxsInfoT.setTzLeadId(clueId);
						PsTzXsxsInfoT.setTzLeadStatus("G");
						PsTzXsxsInfoT.setTzWtxsGbyy(wtxsGbyy);
						PsTzXsxsInfoT.setRowLastmantDttm(new Date());
						PsTzXsxsInfoT.setRowLastmantOprid(oprid);
						psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
						
						//日志记录			
						String tzOperateDesc="关闭线索，责任人" + zrrName +"，关闭原因：" + wtxsGbyy;
						PsTzXsxsLogT PsTzXsxsLogT=new PsTzXsxsLogT();
						int operateId=getSeqNum.getSeqNum("TZ_XSXS_LOG_T", "TZ_OPERATE_ID");
						PsTzXsxsLogT.setTzOperateId(operateId);
						PsTzXsxsLogT.setTzLeadId(clueId);
						PsTzXsxsLogT.setTzLeadStatus1(beforeStatus);
						PsTzXsxsLogT.setTzLeadStatus2("G");
						PsTzXsxsLogT.setTzDemo("");
						PsTzXsxsLogT.setTzOperateDesc(tzOperateDesc);
						PsTzXsxsLogT.setRowAddedOprid(oprid);
						PsTzXsxsLogT.setRowAddedDttm(new Date());
						PsTzXsxsLogT.setRowLastmantOprid(oprid);
						PsTzXsxsLogT.setRowLastmantDttm(new Date());
						psTzXsxsLogTMapper.insert(PsTzXsxsLogT);
					}

				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	@Override
	public String tzOther(String operateType,String strParams,String[] errorMsg) {
		String strRet = "";
		
		try {
			//系统建议操作
			if("tzSystemSuggest".equals(operateType)) {
				strRet = systemSuggest(strParams,errorMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;		
	}
	 
	
	/*系统建议操作*/
	public String systemSuggest(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("clueData", "[]");
		
		try {
			
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			/* *
			 * 系统规则：
			 * 1. 先到先得
			 * 2. 报名人地区为准
			 * 3. 中心职员?天延迟规则
			 * */
			
			//规则描述信息
			String rule_1="",rule_2="",rule_3_prefix="",rule_3_suffix="";
			String sql_rule = "SELECT TZ_MSG_TEXT FROM PS_TZ_PT_XXDY_TBL WHERE TZ_XXJH_ID='TZ_WTXS_SYSTEM_RULE_TIP' AND TZ_JG_ID=? AND TZ_LANGUAGE_ID='ZHS' ORDER BY TZ_MSG_ID";
			List<Map<String, Object>> list_rule = sqlQuery.queryForList(sql_rule, new Object[]{orgId});
			rule_1 = list_rule.get(0).get("TZ_MSG_TEXT") == null ? "" : list_rule.get(0).get("TZ_MSG_TEXT").toString();
			rule_2 = list_rule.get(1).get("TZ_MSG_TEXT") == null ? "" : list_rule.get(1).get("TZ_MSG_TEXT").toString();
			rule_3_prefix = list_rule.get(2).get("TZ_MSG_TEXT") == null ? "" : list_rule.get(2).get("TZ_MSG_TEXT").toString();
			rule_3_suffix = list_rule.get(3).get("TZ_MSG_TEXT") == null ? "" : list_rule.get(3).get("TZ_MSG_TEXT").toString();
			
			Integer delayDay = Integer.valueOf(getHardCodePoint.getHardCodePointVal("TZ_WTXS_ZXZY_DELAY_DAY"));
			
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.DATE, -delayDay);
			Date delayDttm = nowTime.getTime();
			
			String sql_group = "SELECT TZ_WT_TYPE,TZ_LEAD_DESCR FROM PS_TZ_WTXS_TMP_T WHERE OPRID=? AND TZ_JG_ID=? GROUP BY TZ_WT_TYPE,TZ_LEAD_DESCR";
			List<Map<String, Object>> list_group = sqlQuery.queryForList(sql_group,new Object[]{oprid,orgId});
			for(Map<String, Object> map_group : list_group) {
				String wtType = map_group.get("TZ_WT_TYPE") == null ? "" : map_group.get("TZ_WT_TYPE").toString();
				String leadDesc = map_group.get("TZ_LEAD_DESCR") == null ? "" : map_group.get("TZ_LEAD_DESCR").toString();
				
				//线索规则已确定标识
				Boolean bool_rule = false;
				
				//查询责任人中是否存在具有“中心职员”角色且职员类型为“班级主管”的人，且创建时间在早于当前系统时间?天内
				//如果只有一个中心职员，就是这个人的；如果有多个中心职员，先到先得
				String sql_zxzy_get = "SELECT A.TZ_LEAD_ID,B.TZ_ZR_OPRID,C.ROLEUSER";
				sql_zxzy_get += " FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_INFO_T B,PSROLEUSER C,PS_TZ_AQ_YHXX_TBL D";
				sql_zxzy_get += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND C.ROLEUSER=B.TZ_ZR_OPRID AND D.OPRID=B.TZ_ZR_OPRID";
				sql_zxzy_get += " AND C.ROLENAME='SEM_TZGD_XS_COMMON' AND D.TZ_STAFF_TYPE='B'";
				sql_zxzy_get += " AND B.ROW_ADDED_DTTM >= ?";
				sql_zxzy_get += " AND A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
				sql_zxzy_get += " AND B.TZ_ZR_OPRID<>''";
				sql_zxzy_get += " ORDER BY B.ROW_ADDED_DTTM";
				
				List<Map<String, Object>> list_zxzy_get = sqlQuery.queryForList(sql_zxzy_get,new Object[]{delayDttm,oprid,orgId,wtType,leadDesc});
				if(list_zxzy_get.size()>0) {
					Map<String,Object> map_zxzy_get = list_zxzy_get.get(0);
					String clueId_zxzy_get = map_zxzy_get.get("TZ_LEAD_ID") == null ? "" : map_zxzy_get.get("TZ_LEAD_ID").toString();
					
				
					//存在中心职员，查询这个分组下的所有问题线索
					String sql_zxzy = "SELECT A.TZ_LEAD_ID FROM PS_TZ_WTXS_TMP_T A WHERE A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
					List<Map<String, Object>> list_zxzy = sqlQuery.queryForList(sql_zxzy,new Object[]{oprid,orgId,wtType,leadDesc});
					
					for(Map<String, Object>map_zxzy : list_zxzy) {
						//建议操作
						String suggest_zxzy = "B"; //关闭
						//系统规则提示
						String systemTip_zxzy = "";
						
						String clueId_zxzy = map_zxzy.get("TZ_LEAD_ID") == null ? "" : map_zxzy.get("TZ_LEAD_ID").toString();
						if(clueId_zxzy.equals(clueId_zxzy_get)) {
							suggest_zxzy = "A"; //保留
							//如果有多个中心职员
							if(list_zxzy_get.size()>1) {
								systemTip_zxzy = rule_1;
							} else {
								systemTip_zxzy = rule_3_prefix+String.valueOf(delayDay)+rule_3_suffix;	
							}
						}
						
						Map<String, Object> mapData_zxzy = new HashMap<String,Object>();
						mapData_zxzy.put("clueId", clueId_zxzy);
						mapData_zxzy.put("suggestOperation", suggest_zxzy);
						mapData_zxzy.put("systemRuleTip", systemTip_zxzy);
						
						listData.add(mapData_zxzy);
					}
					
					bool_rule = true;
				}
				
				
				
				if(!bool_rule) {
					//报名人地区为准规则
					
					//查询线索的常住地是否不同
					String sql_local_count = "SELECT COUNT(1) FROM (";
					sql_local_count += " SELECT DISTINCT B.TZ_XSQU_ID";
					sql_local_count += " FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_INFO_T B";
					sql_local_count += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
					sql_local_count += " ) TMP";
					Integer localCount = sqlQuery.queryForObject(sql_local_count,new Object[]{oprid,orgId,wtType,leadDesc},"Integer");
					
					if(localCount>1) {
						//问题线索的常住地不同，使用报名人地区为准规则，否则，先到先得
						
						//查询线索关联的报名人地区
						String sql_clue_bmr = "SELECT A.TZ_LEAD_ID,B.TZ_APP_INS_ID,C.OPRID,D.TZ_COMMENT1,D.TZ_LEN_PROID,";
						sql_clue_bmr += "(SELECT M.TZ_LABEL_NAME FROM PS_TZ_XSXS_DQBQ_T M WHERE M.TZ_LABEL_DESC=D.TZ_LEN_PROID AND M.TZ_JG_ID=A.TZ_JG_ID) TZ_LOCAL_ID";
						sql_clue_bmr += " FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_BMB_T B,PS_TZ_FORM_WRK_T C,PS_TZ_REG_USER_T D";
						sql_clue_bmr += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND B.TZ_APP_INS_ID=C.TZ_APP_INS_ID AND C.OPRID=D.OPRID";
						sql_clue_bmr += " AND A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
						sql_clue_bmr += " LIMIT 0,1";
						
						Map<String,Object> map_bmr = sqlQuery.queryForMap(sql_clue_bmr,new Object[]{oprid,orgId,wtType,leadDesc});
						if(map_bmr!=null) {
							String appinsId_bmr = map_bmr.get("TZ_APP_INS_ID") == null ? "0" : map_bmr.get("TZ_APP_INS_ID").toString();
							String countryId_bmr = map_bmr.get("TZ_COMMENT1") == null ? "" : map_bmr.get("TZ_COMMENT1").toString();
							String localId_bmr = map_bmr.get("TZ_LOCAL_ID") == null ? "" : map_bmr.get("TZ_LOCAL_ID").toString();
							
							String zrr_localId = "";
							
							/* 如果有国籍，先按国籍来，
							 * 外籍/港澳台不考虑常住地
							 * 中国大陆根据常住地来
							 * 没有国籍，按照常住地来
							 */
							if(countryId_bmr!=null && !"".equals(countryId_bmr)) {
								if("HK".equals(countryId_bmr)||"TW".equals(countryId_bmr)||"MAC".equals(countryId_bmr)||"FORE".equals(countryId_bmr)) {
									zrr_localId = countryId_bmr;
								} 
								if("CHN".equals(countryId_bmr)) {
									if(localId_bmr!=null && !"".equals(localId_bmr)) {
										zrr_localId = localId_bmr;
									}
								}
							} else {
								if(localId_bmr!=null && !"".equals(localId_bmr)) {
									zrr_localId = localId_bmr;
								}
							}
							
							//查询责任人是否对报名人的常住地有权限
							ArrayList<String> list_local_match = new ArrayList<>();
							ArrayList<String> list_local_all = new ArrayList<>();
							
							String sql_zrr_local_permis = "SELECT A.TZ_LEAD_ID,B.TZ_ZR_OPRID,";
							sql_zrr_local_permis += "(SELECT COUNT(1) FROM PSROLEUSER C WHERE C.ROLEUSER=B.TZ_ZR_OPRID AND C.ROLENAME IN ('SEM_TZGD_XS_COMMON','SEM_TZGD_XS_DEPT_ADM') ) TZ_ALL_LOCAL_PERMIS,";
							sql_zrr_local_permis += "(SELECT COUNT(1) FROM PS_TZ_AQ_DQ_T D WHERE D.OPRID=B.TZ_ZR_OPRID AND D.TZ_AQDQ_LABEL=?) TZ_BMR_LOCAL_PERMIS";
							sql_zrr_local_permis += " FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_INFO_T B";
							sql_zrr_local_permis += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
							sql_zrr_local_permis += " ORDER BY B.ROW_ADDED_DTTM";
							
							List<Map<String, Object>> list_zrr_local_permis = sqlQuery.queryForList(sql_zrr_local_permis, new Object[]{zrr_localId,oprid,orgId,wtType,leadDesc});
							for(Map<String, Object> map_zrr_local_permis : list_zrr_local_permis) {
								String clueId_zrr_local_permis = map_zrr_local_permis.get("TZ_LEAD_ID") == null ? "" : map_zrr_local_permis.get("TZ_LEAD_ID").toString();
								String zrrOprid_zrr_local_permis = map_zrr_local_permis.get("TZ_ZR_OPRID") == null ? "" : map_zrr_local_permis.get("TZ_ZR_OPRID").toString();
								Integer all_zrr_local_permis = map_zrr_local_permis.get("TZ_ALL_LOCAL_PERMIS") == null ? 0 : Integer.valueOf(map_zrr_local_permis.get("TZ_ALL_LOCAL_PERMIS").toString());
								Integer bmr_zrr_local_permis = map_zrr_local_permis.get("TZ_BMR_LOCAL_PERMIS") == null ? 0 : Integer.valueOf(map_zrr_local_permis.get("TZ_BMR_LOCAL_PERMIS").toString());
								
								list_local_all.add(clueId_zrr_local_permis);
								
								if(zrrOprid_zrr_local_permis!=null && !"".equals(zrrOprid_zrr_local_permis)) {
									if(all_zrr_local_permis>0) {
										list_local_match.add(clueId_zrr_local_permis);
									} else if(bmr_zrr_local_permis>0) {
										list_local_match.add(clueId_zrr_local_permis);
									}
								}		
							}
							
							/*
							 * 如果只有一个线索符合对常住地有权限，则就是这个线索了
							 * 如果有多个线索符合对常住地有权限，采用先到先得，因为已经根据创建时间升序排序，则也是第一个
							 */
							if(list_local_match.size()>0) {
								String clueId_local_match_get = list_local_match.get(0);
								
								for(String clueId_local : list_local_all) {
									//建议操作
									String suggest_local = "B"; //关闭
									//系统规则提示
									String systemTip_local = "";
									
									if(clueId_local.equals(clueId_local_match_get)) {
										suggest_local = "A"; //保留
										//如果有多个线索符合
										if(list_local_match.size()>1) {
											systemTip_local = rule_1;
										} else {
											systemTip_local = rule_2;
										}
									}
									
									Map<String, Object> mapData_local = new HashMap<String,Object>();
									mapData_local.put("clueId", clueId_local);
									mapData_local.put("suggestOperation", suggest_local);
									mapData_local.put("systemRuleTip", systemTip_local);
									
									listData.add(mapData_local);
								}
								
								bool_rule = true;
							}		
						}
					}
				}
				
				
				
				if(!bool_rule) {
					//先到先得规则
					
					//取时间最早且有责任人的线索数据
					String sql_xdxd_get = "SELECT A.TZ_LEAD_ID,B.ROW_ADDED_DTTM,B.TZ_ZR_OPRID";
					sql_xdxd_get += " FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_INFO_T B";
					sql_xdxd_get += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND B.TZ_ZR_OPRID<>'' AND A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
					sql_xdxd_get += " ORDER BY B.ROW_ADDED_DTTM";
					sql_xdxd_get += " LIMIT 0,1";
					Map<String, Object> map_xdxd_get = sqlQuery.queryForMap(sql_xdxd_get,new Object[]{oprid,orgId,wtType,leadDesc});
					String clueId_xdxd_get = map_xdxd_get.get("TZ_LEAD_ID") == null ? "" : map_xdxd_get.get("TZ_LEAD_ID").toString();
					
					
					String sql_xdxd = "SELECT A.TZ_LEAD_ID,B.ROW_ADDED_DTTM,B.TZ_ZR_OPRID";
					sql_xdxd += " FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_INFO_T B";
					sql_xdxd += " WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID AND A.OPRID=? AND A.TZ_JG_ID=? AND A.TZ_WT_TYPE=? AND A.TZ_LEAD_DESCR=?";
					sql_xdxd += " ORDER BY B.ROW_ADDED_DTTM";
					
					List<Map<String, Object>> list_xdxd = sqlQuery.queryForList(sql_xdxd,new Object[]{oprid,orgId,wtType,leadDesc});
					if(list_xdxd.size()>0) {						
						for(Map<String, Object>map_xdxd : list_xdxd) {
							//建议操作
							String suggest_xdxd = "B"; //关闭
							//系统规则提示
							String systemTip_xdxd = "";
							
							String clueId_xdxd = map_xdxd.get("TZ_LEAD_ID") == null ? "" : map_xdxd.get("TZ_LEAD_ID").toString();
							if(clueId_xdxd.equals(clueId_xdxd_get)) {
								suggest_xdxd = "A"; //保留
								systemTip_xdxd = rule_1;
							}
							
							Map<String, Object> mapData_xdxd = new HashMap<String,Object>();
							mapData_xdxd.put("clueId", clueId_xdxd);
							mapData_xdxd.put("suggestOperation", suggest_xdxd);
							mapData_xdxd.put("systemRuleTip", systemTip_xdxd);
							
							listData.add(mapData_xdxd);
						}
						bool_rule = true;
					}
				}
				
				mapRet.replace("clueData", listData);
				strRet = jacksonUtil.Map2json(mapRet);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
}
