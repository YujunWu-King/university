package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.org.eclipse.jdt.core.dom.ReturnStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsLogTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsLogT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 线索详情
 * @author LuYan 2017-10-10
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueDetailServiceImpl")
public class TzClueDetailServiceImpl extends FrameworkImpl {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private  GetSeqNum getSeqNum;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private PsTzXsxsLogTMapper psTzXsxsLogTMapper;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	
	/*获取线索信息*/
	public String tzQuery(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			jacksonUtil.json2Map(strParams);
			String clueId = jacksonUtil.getString("clueId");
			
			if (!StringUtils.isBlank(clueId)) {
				String sql = tzSQLObject.getSQLText("SQL.TZEnrollmentClueServiceImpl.TzGetClueInfo");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{clueId});
				
				if(mapData!=null) {
					String TZ_LEAD_STATUS = mapData.get("TZ_LEAD_STATUS") == null ? "" : mapData.get("TZ_LEAD_STATUS").toString();
					String TZ_JY_GJ_RQ = mapData.get("TZ_JY_GJ_RQ") == null ? "" : mapData.get("TZ_JY_GJ_RQ").toString();
					String TZ_THYY_ID = mapData.get("TZ_THYY_ID") == null ? "" : mapData.get("TZ_THYY_ID").toString();
					String TZ_GBYY_ID = mapData.get("TZ_GBYY_ID") == null ? "" : mapData.get("TZ_GBYY_ID").toString();
					String TZ_COLOUR_SORT_ID = mapData.get("TZ_COLOUR_SORT_ID") == null ? "" : mapData.get("TZ_COLOUR_SORT_ID").toString();
					String TZ_ZR_OPRID = mapData.get("TZ_ZR_OPRID") == null ? "" : mapData.get("TZ_ZR_OPRID").toString();
					String TZ_ZRR_NAME = mapData.get("TZ_ZRR_NAME") == null ? "" : mapData.get("TZ_ZRR_NAME").toString();
					String TZ_KH_OPRID = mapData.get("TZ_KH_OPRID") == null ? "" : mapData.get("TZ_KH_OPRID").toString();
					String TZ_REALNAME = mapData.get("TZ_REALNAME") == null ? "" : mapData.get("TZ_REALNAME").toString();
					String TZ_COMP_CNAME = mapData.get("TZ_COMP_CNAME") == null ? "" : mapData.get("TZ_COMP_CNAME").toString();
					String TZ_POSITION = mapData.get("TZ_POSITION") == null ? "" : mapData.get("TZ_POSITION").toString();
					String TZ_MOBILE = mapData.get("TZ_MOBILE") == null ? "" : mapData.get("TZ_MOBILE").toString();
					String TZ_PHONE = mapData.get("TZ_PHONE") == null ? "" : mapData.get("TZ_PHONE").toString();
					String TZ_REFEREE_NAME = mapData.get("TZ_REFEREE_NAME") == null ? "" : mapData.get("TZ_REFEREE_NAME").toString();
					String TZ_XSQU_ID = mapData.get("TZ_XSQU_ID") == null ? "" : mapData.get("TZ_XSQU_ID").toString();
					String TZ_XSQU_DESC = mapData.get("TZ_XSQU_DESC") == null ? "" : mapData.get("TZ_XSQU_DESC").toString();
					String TZ_BZ = mapData.get("TZ_BZ") == null ? "" : mapData.get("TZ_BZ").toString();
					String TZ_BMR_STATUS = mapData.get("TZ_BMR_STATUS") == null ? "" : mapData.get("TZ_BMR_STATUS").toString();
				
					Map<String, Object> mapFormData = new HashMap<String,Object>();
					mapFormData.put("clueId", clueId);
					mapFormData.put("clueState", TZ_LEAD_STATUS);
					mapFormData.put("backReasonId", TZ_THYY_ID);
					mapFormData.put("closeReasonId", TZ_GBYY_ID);
					mapFormData.put("contactDate", TZ_JY_GJ_RQ);
					mapFormData.put("chargeOprid", TZ_ZR_OPRID);
					mapFormData.put("chargeName", TZ_ZRR_NAME);
					mapFormData.put("cusOprid", TZ_KH_OPRID);
					mapFormData.put("cusName", TZ_REALNAME);
					mapFormData.put("cusMobile", TZ_MOBILE);
					mapFormData.put("companyName", TZ_COMP_CNAME);
					mapFormData.put("position", TZ_POSITION);
					mapFormData.put("phone", TZ_PHONE);
					mapFormData.put("localId", TZ_XSQU_ID);
					mapFormData.put("localAddress", TZ_XSQU_DESC);
					mapFormData.put("colorType", TZ_COLOUR_SORT_ID);
					mapFormData.put("bkStatus", TZ_BMR_STATUS);
					mapFormData.put("refereeName", TZ_REFEREE_NAME);
					mapFormData.put("memo", TZ_BZ);
					
					mapRet.put("formData", mapFormData);
					
					strRet = jacksonUtil.Map2json(mapRet);
				}
			} else{
				errorMsg[0] = "1";
				errorMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*获取报名信息*/
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errorMsg) {
		String strRet = "";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			jacksonUtil.json2Map(strParams);
			String clueId = jacksonUtil.getString("clueId");
			
			if(clueId!=null && !"".equals(clueId)) {
				String sql = tzSQLObject.getSQLText("SQL.TZEnrollmentClueServiceImpl.TzGetClueBmbInfo");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{clueId});
				if(mapData!=null) {
					String TZ_APP_INS_ID=mapData.get("TZ_APP_INS_ID")==null?"":mapData.get("TZ_APP_INS_ID").toString();
					String TZ_APP_FORM_STA=mapData.get("TZ_APP_FORM_STA")==null?"":mapData.get("TZ_APP_FORM_STA").toString();
				    String OPRID=mapData.get("OPRID")==null?"":mapData.get("OPRID").toString();
					String TZ_REALNAME=mapData.get("TZ_REALNAME")==null?"":mapData.get("TZ_REALNAME").toString();
					String TZ_MOBILE=mapData.get("TZ_MOBILE")==null?"":mapData.get("TZ_MOBILE").toString();
					String TZ_EMAIL=mapData.get("TZ_EMAIL")==null?"":mapData.get("TZ_EMAIL").toString();
					String TZ_CLASS_ID=mapData.get("TZ_CLASS_ID")==null?"":mapData.get("TZ_CLASS_ID").toString();
					String TZ_CLASS_NAME=mapData.get("TZ_CLASS_NAME")==null?"":mapData.get("TZ_CLASS_NAME").toString();
					String TZ_FORM_SP_STA=mapData.get("TZ_FORM_SP_STA")==null?"待审核":mapData.get("TZ_FORM_SP_STA").toString();
				
					String TZ_JF_STATUS=sqlQuery.queryForObject("select 'Y' from PS_TZ_XFJIAOF_TBL where TZ_APP_INS_ID=?", new Object[]{TZ_APP_INS_ID}, "String");
					if("Y".equals(TZ_JF_STATUS)){
						TZ_JF_STATUS="已缴费";
					}else{
						TZ_JF_STATUS="未缴费";
					}
					String TZ_LQ_STATUS=sqlQuery.queryForObject("SELECT 'Y' from TZ_IMP_LQJD_TBL where TZ_APP_INS_ID=? and TZ_LQ_STATE='Y'", new Object[]{TZ_APP_INS_ID}, "String");
					if("Y".equals(TZ_LQ_STATUS)){
						TZ_LQ_STATUS="已录取";
					}else{
						TZ_LQ_STATUS="未录取";
					}
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("clueId", clueId);
					mapList.put("bmbId", TZ_APP_INS_ID);
					mapList.put("tjStatus", TZ_APP_FORM_STA);
					mapList.put("bmrOprid", OPRID);
					mapList.put("bmrName", TZ_REALNAME);
					mapList.put("bmrMobile", TZ_MOBILE);
					mapList.put("bmrEmail", TZ_EMAIL);
					mapList.put("bmrClassId", TZ_CLASS_ID);
					mapList.put("bmrClassName", TZ_CLASS_NAME);
					mapList.put("bmrCsStatus", TZ_FORM_SP_STA);
					mapList.put("bmrsfStatus", TZ_JF_STATUS);
					mapList.put("bmrLqStatus", TZ_LQ_STATUS);
					listData.add(mapList);
				}
			}
			
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
		Map<String,Object> returnMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				String typeFlag=jacksonUtil.getString("typeFlag");
				Map<String,Object> dataMap=jacksonUtil.getMap("data");
				//基本信息保存
				if("BASIC".equals(typeFlag)){
					returnMap=this.tzEditClueInfo(dataMap,errMsg);
				}
				//退回线索
				if("BACK".equals(typeFlag)){
					returnMap=this.tzEditClueBackReason(dataMap,errMsg);
				}
				//关闭线索
				if("CLOSE".equals(typeFlag)){
					returnMap=this.tzEditClueCloseReason(dataMap,errMsg);
				}
				//转交线索
				if("GIVE".equals(typeFlag)){
					returnMap=this.tzEditClueResponsible(dataMap,errMsg);
				}
				//延迟联系
				if("DELAY".equals(typeFlag)){
					returnMap=this.tzEditClueContactDate(dataMap,errMsg);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnMap);
	}
	
	
	/*线索基本信息保存*/
	private Map<String, Object> tzEditClueInfo(Map<String, Object> dataMap, String[] errMsg) {
		
		Map<String,Object> map=new HashMap<String,Object>();
		String clueId="";
		String clueState="";
		String backReasonId="";
		String closeReasonId="";
		String contactDate="";
		String chargeOprid="";
		String chargeName="";
		String cusName="";
		String cusMobile="";
		String companyName="";
		String position="";
		String phone="";
		String localId="";
		String localAddress="";
		String colorType="";
		String bkStatus="";
		String refereeName="";
		String memo="";
		String fromType="";
		String createWay="";
		
		try {
			String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
			String jgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
			if(dataMap!=null){
				clueId=dataMap.get("clueId")==null?"":dataMap.get("clueId").toString();
				clueState=dataMap.get("clueState")==null?"":dataMap.get("clueState").toString();
				backReasonId=dataMap.get("backReasonId")==null?"":dataMap.get("backReasonId").toString();
				closeReasonId=dataMap.get("closeReasonId")==null?"":dataMap.get("closeReasonId").toString();
				contactDate=dataMap.get("contactDate")==null?"":dataMap.get("contactDate").toString();
				chargeOprid=dataMap.get("chargeOprid")==null?"":dataMap.get("chargeOprid").toString();
				chargeName=dataMap.get("chargeName")==null?"":dataMap.get("chargeName").toString();
				cusName=dataMap.get("cusName")==null?"":dataMap.get("cusName").toString();
				cusMobile=dataMap.get("cusMobile")==null?"":dataMap.get("cusMobile").toString();
				companyName=dataMap.get("companyName")==null?"":dataMap.get("companyName").toString();
				position=dataMap.get("position")==null?"":dataMap.get("position").toString();
				phone=dataMap.get("phone")==null?"":dataMap.get("phone").toString();
				localId=dataMap.get("localId")==null?"":dataMap.get("localId").toString();
				localAddress=dataMap.get("localAddress")==null?"":dataMap.get("localAddress").toString();
				colorType=dataMap.get("colorType")==null?"":dataMap.get("colorType").toString();
				bkStatus=dataMap.get("bkStatus")==null?"":dataMap.get("bkStatus").toString();
				refereeName=dataMap.get("refereeName")==null?"":dataMap.get("refereeName").toString();
				memo=dataMap.get("memo")==null?"":dataMap.get("memo").toString();
				fromType=dataMap.get("fromType")==null?"":dataMap.get("fromType").toString();
			}
		
			if("".equals(clueId)||clueId==null){

				//创建方式：招生线索管理-手工创建、我的招生线索-自主开发
				if("MYXS".equals(fromType)) {
					createWay = "I";
				} else {
					createWay = "G";
				}
				clueId=String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
				PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
				PsTzXsxsInfoT.setTzLeadId(clueId);
				PsTzXsxsInfoT.setTzJgId(jgId);
				PsTzXsxsInfoT.setTzLeadStatus(clueState);
				//线索创建方式
				PsTzXsxsInfoT.setTzRsfcreateWay(createWay);	
				PsTzXsxsInfoT.setTzZrOprid(chargeOprid);
				PsTzXsxsInfoT.setTzRealname(cusName);
				PsTzXsxsInfoT.setTzCompCname(companyName);
				PsTzXsxsInfoT.setTzMobile(cusMobile);
				PsTzXsxsInfoT.setTzPhone(phone);
				PsTzXsxsInfoT.setTzPosition(position);
				PsTzXsxsInfoT.setTzXsquId(localId);
				PsTzXsxsInfoT.setTzRefereeName(refereeName);
				PsTzXsxsInfoT.setTzBz(memo);
				PsTzXsxsInfoT.setTzColourSortId(colorType);
				PsTzXsxsInfoT.setRowAddedOprid(oprid);
				PsTzXsxsInfoT.setRowLastmantOprid(oprid);
				PsTzXsxsInfoT.setRowAddedDttm(new java.util.Date());
				PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
				psTzXsxsInfoTMapper.insert(PsTzXsxsInfoT);
				map.put("clueId", clueId);
				map.put("bkStatus", "A");
				
				/*查询是否存在姓名相同未关闭的线索*/
				Integer existName = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID<>? AND TZ_LEAD_STATUS<>'G' AND TZ_REALNAME=?", new Object[]{clueId,cusName},"Integer");
				if(existName>0) {
					map.put("existName", "Y");
				} else {
					map.put("existName", "");
				}
				
				
			}else{
				PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
				PsTzXsxsInfoT.setTzLeadId(clueId);
				PsTzXsxsInfoT.setTzJgId(jgId);
				PsTzXsxsInfoT.setTzLeadStatus(clueState);
				PsTzXsxsInfoT.setTzThyyId(backReasonId);
				PsTzXsxsInfoT.setTzGbyyId(closeReasonId);
				if(contactDate!=null && !"".equals(contactDate)) {
					PsTzXsxsInfoT.setTzJyGjRq(sdf.parse(contactDate));
				}
				PsTzXsxsInfoT.setTzZrOprid(chargeOprid);
				PsTzXsxsInfoT.setTzRealname(cusName);
				PsTzXsxsInfoT.setTzCompCname(companyName);
				PsTzXsxsInfoT.setTzMobile(cusMobile);
				PsTzXsxsInfoT.setTzPhone(phone);
				PsTzXsxsInfoT.setTzPosition(position);
				PsTzXsxsInfoT.setTzXsquId(localId);
				PsTzXsxsInfoT.setTzRefereeName(refereeName);
				PsTzXsxsInfoT.setTzBz(memo);
				PsTzXsxsInfoT.setTzColourSortId(colorType);
				PsTzXsxsInfoT.setRowLastmantOprid(oprid);
				PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
				psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
				
				map.put("clueId", clueId);
				
				//报考状态
				String sql = tzSQLObject.getSQLText("SQL.TZEnrollmentClueServiceImpl.TzGetClueBmbStateInfo");
				Map<String, Object> mapBmb = sqlQuery.queryForMap(sql, new Object[]{clueId});
				if(mapBmb!=null) {
					String TZ_FORM_SP_STA =  mapBmb.get("TZ_FORM_SP_STA") == null ? "" : mapBmb.get("TZ_FORM_SP_STA").toString();
					String TZ_MS_PLAN = mapBmb.get("TZ_MS_PLAN") == null ? "" : mapBmb.get("TZ_MS_PLAN").toString();
					String TZ_BS_RESULT = mapBmb.get("TZ_BS_RESULT") == null ? "" : mapBmb.get("TZ_BS_RESULT").toString();
					String TZ_LQ_STATE = mapBmb.get("TZ_LQ_STATE") == null ? "" : mapBmb.get("TZ_LQ_STATE").toString();
					String TZ_KX_PLAN = mapBmb.get("TZ_KX_PLAN") == null ? "" : mapBmb.get("TZ_KX_PLAN").toString();
					
					if(TZ_KX_PLAN!=null && !"".equals(TZ_KX_PLAN)) {
						//已入学
						bkStatus="G";
					} else if("Y".equals(TZ_LQ_STATE)) {
						//已录取
						bkStatus="F";
					} else if("Y".equals(TZ_BS_RESULT)) {
						//面试通过
						bkStatus="E";
					} else if("Y".equals(TZ_MS_PLAN)) {
						//已安排面试时间
						bkStatus="D";
					} else if("A".equals(TZ_FORM_SP_STA)) {
						//初审通过
						bkStatus="C";
					} else {
						//已报名
						bkStatus="B";
					}
				} else {
					//未报名
					bkStatus="A";
				}
				map.put("bkStatus", bkStatus);
				map.put("existName", "");
			}
			   
			map.put("clueState", clueState);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	/*退回线索*/
	private Map<String, Object> tzEditClueBackReason(Map<String, Object> dataMap,String[] errMsg) {
		Map<String,Object> retMap=new HashMap<String, Object>();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		try {
			String clueId = "";
			String backPersonOprid = "";
			String backPersonName = "";
			String backReasonId = "";
			String demo = "";
			String chargeOpridPage = "";
			String clueStatePage = "";
			
			if(dataMap!=null) {
				clueId = dataMap.get("clueId") == null ? "" : dataMap.get("clueId").toString();
				backPersonOprid = dataMap.get("backPersonOprid") == null ? "" : dataMap.get("backPersonOprid").toString();
				backPersonName = dataMap.get("backPersonName") == null ? "" : dataMap.get("backPersonName").toString();
				backReasonId = dataMap.get("backReasonId") == null ? "" : dataMap.get("backReasonId").toString();
				demo = dataMap.get("demo") == null ? "" : dataMap.get("demo").toString();
				chargeOpridPage = dataMap.get("chargeOpridPage") == null ? "" : dataMap.get("chargeOpridPage").toString();
				if("NEXT".equals(chargeOpridPage)) {
					chargeOpridPage="";
				}
				clueStatePage = dataMap.get("clueStatePage") == null ? "" : dataMap.get("clueStatePage").toString();
			}
			
			if(!"".equals(clueId)) {
				//查询当前线索的责任人和线索状态
				Map<String, Object> mapClue = sqlQuery.queryForMap("SELECT A.TZ_ZR_OPRID,B.TZ_REALNAME TZ_ZRR_NAME,A.TZ_LEAD_STATUS FROM PS_TZ_XSXS_INFO_T A LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON A.TZ_ZR_OPRID=B.OPRID WHERE A.TZ_LEAD_ID = ?",new Object[]{clueId});
				if(mapClue!=null) {
					String chargeOpridOrigin = mapClue.get("TZ_ZR_OPRID") == null ? "" : mapClue.get("TZ_ZR_OPRID").toString();
					String chargeNameOrigin = mapClue.get("TZ_ZRR_NAME") == null ? "" : mapClue.get("TZ_ZRR_NAME").toString();
					String clueStateOrigin = mapClue.get("TZ_LEAD_STATUS") == null ? "" : mapClue.get("TZ_LEAD_STATUS").toString();
					
					if(chargeOpridPage.equals(chargeOpridOrigin) && clueStatePage.equals(clueStateOrigin)) {
						//更新线索状态表
						PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
						PsTzXsxsInfoT.setTzLeadId(clueId);
						PsTzXsxsInfoT.setTzLeadStatus("F");
						PsTzXsxsInfoT.setTzThyyId(backReasonId);
						PsTzXsxsInfoT.setTzZrOprid(backPersonOprid);
						PsTzXsxsInfoT.setRowLastmantOprid(oprid);
						PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
						psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
						
						//更新线索日志表
						
						//责任人姓名
						String zrrOprid = "", zrrName = "";
						if(backPersonOprid!=null && !"".equals(backPersonOprid)
							  && backPersonName!=null && !"".equals(backPersonName)) {
							zrrOprid = backPersonOprid;
							zrrName = backPersonName;
						} else {
							zrrOprid = chargeOpridOrigin;
							zrrName = chargeNameOrigin;
						}		
						
						//退回线索原因
						String backReasonName = sqlQuery.queryForObject("SELECT TZ_LABEL_NAME FROM PS_TZ_THYY_XSGL_T WHERE TZ_THYY_ID=?", new Object[]{backReasonId},"String");
					
						String tzOperateDesc="退回线索，责任人" + zrrName +"，退回原因：" + backReasonName;
						
						PsTzXsxsLogT PsTzXsxsLogT=new PsTzXsxsLogT();
						int operateId=getSeqNum.getSeqNum("TZ_XSXS_LOG_T", "TZ_OPERATE_ID");
						PsTzXsxsLogT.setTzOperateId(operateId);
						PsTzXsxsLogT.setTzLeadId(clueId);
						PsTzXsxsLogT.setTzLeadStatus1(clueStatePage);
						PsTzXsxsLogT.setTzLeadStatus2("F");
						PsTzXsxsLogT.setTzDemo(demo);
						PsTzXsxsLogT.setTzOperateDesc(tzOperateDesc);
						PsTzXsxsLogT.setRowAddedOprid(oprid);
						PsTzXsxsLogT.setRowAddedDttm(new java.util.Date());
						PsTzXsxsLogT.setRowLastmantOprid(oprid);
						PsTzXsxsLogT.setRowLastmantDttm(new java.util.Date());
						psTzXsxsLogTMapper.insert(PsTzXsxsLogT);
						
					} else {
						errMsg[0] = "1";
						errMsg[1] = "【数据冲突】无法更新当前线索属性，请重新刷新当前页面。";
					}
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return retMap;
	}
	
	
	/*关闭线索*/
	private Map<String, Object> tzEditClueCloseReason(Map<String, Object> dataMap,String[] errMsg) {
		Map<String,Object> retMap=new HashMap<String, Object>();
		String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			String clueId="";
			String closeReasonId="";
			String demo="";
			String nowStatur="";
			String chargeOpridPage="";
			if(dataMap!=null){
				clueId=dataMap.get("clueId")==null?"":dataMap.get("clueId").toString();
		        closeReasonId=dataMap.get("closeReasonId")==null?"":dataMap.get("closeReasonId").toString();
				demo=dataMap.get("demo")==null?"":dataMap.get("demo").toString();
				nowStatur=dataMap.get("clueStatePage")==null?"":dataMap.get("clueStatePage").toString();
				chargeOpridPage=dataMap.get("chargeOpridPage")==null?"":dataMap.get("chargeOpridPage").toString();
			}
			String beforeStatus=sqlQuery.queryForObject("select TZ_LEAD_STATUS from PS_TZ_XSXS_INFO_T where TZ_LEAD_ID=?", new Object[]{clueId}, "String");
			String realName=sqlQuery.queryForObject("select TZ_REALNAME from PS_TZ_YHZH_NB_VW where OPRID=?", new Object[]{chargeOpridPage}, "String");
			String closeReason=sqlQuery.queryForObject("SELECT TZ_LABEL_NAME FROM PS_TZ_GBYY_XSGL_T WHERE TZ_GBYY_ID=?", new Object[]{closeReasonId}, "String");
			
			String tzOperateDesc="关闭线索，责任人" + realName +"，关闭原因：" + closeReason;
			
			if(nowStatur.equals(beforeStatus)){
				//更新线索状态表
				PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
				PsTzXsxsInfoT.setTzLeadId(clueId);
				PsTzXsxsInfoT.setTzLeadStatus("G");
				PsTzXsxsInfoT.setTzGbyyId(closeReasonId);
				PsTzXsxsInfoT.setRowLastmantOprid(oprid);
				PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
				psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
				
				//写日志表
				PsTzXsxsLogT PsTzXsxsLogT=new PsTzXsxsLogT();
				int operateId=getSeqNum.getSeqNum("TZ_XSXS_LOG_T", "TZ_OPERATE_ID");
				PsTzXsxsLogT.setTzOperateId(operateId);
				PsTzXsxsLogT.setTzLeadId(clueId);
				PsTzXsxsLogT.setTzLeadStatus1(nowStatur);
				PsTzXsxsLogT.setTzLeadStatus2("G");
				PsTzXsxsLogT.setTzDemo(demo);
				PsTzXsxsLogT.setTzOperateDesc(tzOperateDesc);
				PsTzXsxsLogT.setRowAddedOprid(oprid);
				PsTzXsxsLogT.setRowAddedDttm(new java.util.Date());
				PsTzXsxsLogT.setRowLastmantOprid(oprid);
				PsTzXsxsLogT.setRowLastmantDttm(new java.util.Date());
				psTzXsxsLogTMapper.insert(PsTzXsxsLogT);
			}else{
				errMsg[0] = "1";
				errMsg[1] = "【数据冲突】无法更新当前线索属性，请重新刷新当前页面。";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return retMap;
	}
	
	
	/*转交线索*/
	private Map<String, Object> tzEditClueResponsible(Map<String, Object> dataMap, String[] errMsg) {
		Map<String,Object> retMap=new HashMap<String, Object>();
		String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			String clueIdSelect="";
			String clueId="";
			String chargeOprid="";
			String chargeName="";
			String demo="";
			String chargeOpridPage="";
			String clueStatePage="";
			if(dataMap!=null){
				clueIdSelect=dataMap.get("clueIdSelect")==null?"":dataMap.get("clueIdSelect").toString();
				clueId=dataMap.get("clueId")==null?"":dataMap.get("clueId").toString();
				chargeOprid=dataMap.get("chargeOprid")==null?"":dataMap.get("chargeOprid").toString();
				chargeName=dataMap.get("chargeName")==null?"":dataMap.get("chargeName").toString();
				demo=dataMap.get("demo")==null?"":dataMap.get("demo").toString();
				chargeOpridPage=dataMap.get("chargeOpridPage")==null?"":dataMap.get("chargeOpridPage").toString();
				if("NEXT".equals(chargeOpridPage)) {
					chargeOpridPage="";
				}
				clueStatePage=dataMap.get("clueStatePage")==null?"":dataMap.get("clueStatePage").toString();
			}
			
			if(!"".equals(clueIdSelect)) {
				//招生线索管理页面，批量修改线索责任人
				String[] clueIdArr = clueIdSelect.split(",");
				for(String clueIdTmp : clueIdArr) {
					
					//查询当前线索的状态、责任人
					String clueStateTmp = "",chargeOpridTmp = "",chargeNameTmp = "";
					Map<String, Object> mapTmp = sqlQuery.queryForMap("SELECT A.TZ_ZR_OPRID,B.TZ_REALNAME TZ_ZRR_NAME,A.TZ_LEAD_STATUS FROM PS_TZ_XSXS_INFO_T A LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON A.TZ_ZR_OPRID=B.OPRID WHERE A.TZ_LEAD_ID = ?",new Object[]{clueIdTmp});
					if(mapTmp!=null) {
						chargeOpridTmp = mapTmp.get("TZ_ZR_OPRID") == null ? "" : mapTmp.get("TZ_ZR_OPRID").toString();
						chargeNameTmp = mapTmp.get("TZ_ZRR_NAME") == null ? "" : mapTmp.get("TZ_ZRR_NAME").toString();
						clueStateTmp = mapTmp.get("TZ_LEAD_STATUS") == null ? "" : mapTmp.get("TZ_LEAD_STATUS").toString();
					}
					
					if(chargeOprid.equals(chargeOpridTmp)) {
						//责任人相同，不操作
					} else {
					
						//更新线索状态表
						PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
						PsTzXsxsInfoT.setTzLeadId(clueIdTmp);
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
						PsTzXsxsLogT.setTzLeadId(clueIdTmp);
						PsTzXsxsLogT.setTzLeadStatus1(clueStateTmp);
						PsTzXsxsLogT.setTzLeadStatus2("C");
						PsTzXsxsLogT.setTzDemo(demo);
						PsTzXsxsLogT.setTzOperateDesc(tzOperateDesc);
						PsTzXsxsLogT.setRowAddedOprid(oprid);
						PsTzXsxsLogT.setRowAddedDttm(new java.util.Date());
						PsTzXsxsLogT.setRowLastmantOprid(oprid);
						PsTzXsxsLogT.setRowLastmantDttm(new java.util.Date());
						psTzXsxsLogTMapper.insert(PsTzXsxsLogT);
					}
				}
			} else {
			
				if(!"".equals(clueId)) {
					//查询当前线索的责任人和线索状态
					Map<String, Object> mapClue = sqlQuery.queryForMap("SELECT A.TZ_ZR_OPRID,B.TZ_REALNAME TZ_ZRR_NAME,A.TZ_LEAD_STATUS FROM PS_TZ_XSXS_INFO_T A LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON A.TZ_ZR_OPRID=B.OPRID WHERE A.TZ_LEAD_ID = ?",new Object[]{clueId});
					if(mapClue!=null) {
						String chargeOpridOrigin = mapClue.get("TZ_ZR_OPRID") == null ? "" : mapClue.get("TZ_ZR_OPRID").toString();
						String chargeNameOrigin = mapClue.get("TZ_ZRR_NAME") == null ? "" : mapClue.get("TZ_ZRR_NAME").toString();
						String clueStateOrigin = mapClue.get("TZ_LEAD_STATUS") == null ? "" : mapClue.get("TZ_LEAD_STATUS").toString();
											
						if(chargeOpridPage.equals(chargeOpridOrigin) && clueStatePage.equals(clueStateOrigin)) {
							//更新线索状态表
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
							PsTzXsxsLogT.setTzLeadStatus1(clueStatePage);
							PsTzXsxsLogT.setTzLeadStatus2("C");
							PsTzXsxsLogT.setTzDemo(demo);
							PsTzXsxsLogT.setTzOperateDesc(tzOperateDesc);
							PsTzXsxsLogT.setRowAddedOprid(oprid);
							PsTzXsxsLogT.setRowAddedDttm(new java.util.Date());
							PsTzXsxsLogT.setRowLastmantOprid(oprid);
							PsTzXsxsLogT.setRowLastmantDttm(new java.util.Date());
							psTzXsxsLogTMapper.insert(PsTzXsxsLogT);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "【数据冲突】无法更新当前线索属性，请重新刷新当前页面。";
						}
					}
				}
			}
			
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return retMap;
	}
	
	
	/*延迟联系线索*/
	private Map<String, Object> tzEditClueContactDate(Map<String, Object> dataMap, String[] errMsg) {
		Map<String,Object> retMap=new HashMap<String, Object>();
		String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String clueId="";
			String contactDate="";
			String demo="";
			String chargeOpridPage="";
			String clueStatePage="";
			
			if(dataMap!=null){
				clueId=dataMap.get("clueId")==null?"":dataMap.get("clueId").toString();
				contactDate=dataMap.get("contactDate")==null?"":dataMap.get("contactDate").toString();
				demo=dataMap.get("demo")==null?"":dataMap.get("demo").toString();
				clueStatePage=dataMap.get("clueStatePage")==null?"":dataMap.get("clueStatePage").toString();
				chargeOpridPage=dataMap.get("chargeOpridPage")==null?"":dataMap.get("chargeOpridPage").toString();
			}
			String beforeStatus=sqlQuery.queryForObject("select TZ_LEAD_STATUS from PS_TZ_XSXS_INFO_T where TZ_LEAD_ID=?", new Object[]{clueId}, "String");
			String realName=sqlQuery.queryForObject("select TZ_REALNAME from PS_TZ_YHZH_NB_VW where OPRID=?", new Object[]{chargeOpridPage}, "String");
			
			String tzOperateDesc="延迟联系线索，责任人" +realName + "，建议跟进日期为：" + contactDate;
			
			if(clueStatePage.equals(beforeStatus)){
				//更新线索状态表
				PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
				PsTzXsxsInfoT.setTzLeadId(clueId);
				PsTzXsxsInfoT.setTzLeadStatus("D");
				PsTzXsxsInfoT.setTzJyGjRq(sdf.parse(contactDate));;
				PsTzXsxsInfoT.setRowLastmantOprid(oprid);
				PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
				psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
				
				//写日志表
				PsTzXsxsLogT PsTzXsxsLogT=new PsTzXsxsLogT();
				int operateId=getSeqNum.getSeqNum("TZ_XSXS_LOG_T", "TZ_OPERATE_ID");
				PsTzXsxsLogT.setTzOperateId(operateId);
				PsTzXsxsLogT.setTzLeadId(clueId);
				PsTzXsxsLogT.setTzLeadStatus1(clueStatePage);
				PsTzXsxsLogT.setTzLeadStatus2("D");
				PsTzXsxsLogT.setTzDemo(demo);
				PsTzXsxsLogT.setTzOperateDesc(tzOperateDesc);
				PsTzXsxsLogT.setRowAddedOprid(oprid);
				PsTzXsxsLogT.setRowAddedDttm(new java.util.Date());
				PsTzXsxsLogT.setRowLastmantOprid(oprid);
				PsTzXsxsLogT.setRowLastmantDttm(new java.util.Date());
				psTzXsxsLogTMapper.insert(PsTzXsxsLogT);
			}else{
				errMsg[0] = "1";
				errMsg[1] = "【数据冲突】无法更新当前线索属性，请重新刷新当前页面。";
			}
				
				
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return retMap;
	}
	
	
	/*删除报名信息*/
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		Map<String,Object> map=new HashMap<String,Object>();

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return "{}";
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String clueId = jacksonUtil.getString("clueId");
				if (clueId != null && !"".equals(clueId)) {
					sqlQuery.update("delete from PS_TZ_XSXS_BMB_T where TZ_LEAD_ID=?", new Object[] { clueId });
                    map.put("clueId", clueId);
                    map.put("bkStatus", "A");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(map);
	}
	
	
	@Override
	public String tzOther(String operateType,String strParams,String[] errorMsg) {
		String strRet = "";
		
		try {
			//获取当前登录人信息
			if("tzGetCurrentName".equals(operateType)) {
				strRet = getCurrentName(strParams,errorMsg);
			}
			//获取下拉框字段信息
			if("tzGetDropDownInfo".equals(operateType)) {
				strRet = getDropDownInfo(strParams,errorMsg);
			}
			//获取当前登录人的线索角色
			if("tzGetPersonRole".equals(operateType)) {
				strRet = getPersonRole(strParams,errorMsg);
			}
			//获取历史线索客户信息
			if("tzGetCustomerInfo".equals(operateType)) {
				strRet = getCustomerInfo(strParams,errorMsg);
			}
			//根据手机号码判断是否已存在未关闭线索
			if("tzIsExist".equals(operateType)) {
				strRet = isExist(strParams,errorMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;		
	}
	
	
	/*获取当前登录人信息*/
	public String getCurrentName(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前登录人oprid
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			//当前登录人姓名
			String name = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[]{oprid},"String");
			
			//当前登录人主管地区
			String localId = "", localName = "";
			Map<String,Object> mapData = sqlQuery.queryForMap("SELECT A.TZ_AQDQ_LABEL,(SELECT B.TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T B  WHERE A.TZ_AQDQ_LABEL=B.TZ_LABEL_NAME AND B.TZ_LABEL_STATUS='Y') TZ_LABEL_DESC,COUNT(1) TZ_COUNT FROM PS_TZ_AQ_DQ_T A WHERE A.OPRID=? AND A.TZ_TYPE_LABEL='Z' LIMIT 0,1",new Object[]{oprid});
			if(mapData!=null) {
				String count = mapData.get("TZ_COUNT") == null ? "0" : mapData.get("TZ_COUNT").toString();
				if("0".equals(count)) {
				} else {
					localId = mapData.get("TZ_AQDQ_LABEL") == null ? "" : mapData.get("TZ_AQDQ_LABEL").toString();
					localName = mapData.get("TZ_LABEL_DESC") == null ? "" : mapData.get("TZ_LABEL_DESC").toString();
				}
			}
			
			mapRet.put("currentOprid", oprid);
			mapRet.put("currentName", name);
			mapRet.put("currentLocalId", localId);
			mapRet.put("currentLocalName", localName);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*获取下拉框字段信息*/
	public String getDropDownInfo(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			String clueId = jacksonUtil.getString("clueId");
			
			String sql = tzSQLObject.getSQLText("SQL.TZEnrollmentClueServiceImpl.TzGetDropDownInfo");
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[]{clueId});
			
			if(mapData!=null) {
				String backReasonId = mapData.get("TZ_THYY_ID") == null ? "" : mapData.get("TZ_THYY_ID").toString();
				String backReasonName = mapData.get("TZ_THYY_NAME") == null ? "" : mapData.get("TZ_THYY_NAME").toString();
				String closeReasonId = mapData.get("TZ_GBYY_ID") == null ? "" : mapData.get("TZ_GBYY_ID").toString();
				String closeReasonName = mapData.get("TZ_GBYY_NAME") == null ? "" : mapData.get("TZ_GBYY_NAME").toString();
				String colorTypeId = mapData.get("TZ_COLOUR_SORT_ID") == null ? "" : mapData.get("TZ_COLOUR_SORT_ID").toString();
				String colorTypeName = mapData.get("TZ_COLOUR_NAME") == null ? "" : mapData.get("TZ_COLOUR_NAME").toString();
				String colorTypeCode = mapData.get("TZ_COLOUR_CODE") == null ? "" : mapData.get("TZ_COLOUR_CODE").toString();
				
				String backPersonOprid = "", backPersonName = "";
				
				/*退回至取值：优先查找操作日志表中最近指定我是责任人的操作人，如果找不到，去线索创建人*/
				//当前登录人姓名
				String name = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[]{oprid},"String");
				
				String backSql =  "SELECT COUNT(1) TZ_COUNT,ROW_ADDED_OPRID FROM PS_TZ_XSXS_LOG_T WHERE TZ_LEAD_ID='" + clueId + "' AND TZ_OPERATE_DESC LIKE '%" + name + "%' ORDER BY ROW_ADDED_DTTM DESC LIMIT 0,1";
				Map<String, Object> mapLog = sqlQuery.queryForMap(backSql);
				if(mapLog!=null) {
					backPersonOprid = mapLog.get("ROW_ADDED_OPRID") == null ? "" : mapLog.get("ROW_ADDED_OPRID").toString();
				}
				
				if(backPersonOprid!=null && !"".equals(backPersonOprid)) {
					
				} else {
					backPersonOprid = sqlQuery.queryForObject("SELECT ROW_ADDED_OPRID FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID=?", new Object[]{clueId},"String");
				}
				
				backPersonName = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[]{backPersonOprid},"String");
			
				mapRet.put("backReasonId", backReasonId);
				mapRet.put("backReasonName", backReasonName);
				mapRet.put("closeReasonId", closeReasonId);
				mapRet.put("closeReasonName", closeReasonName);
				mapRet.put("colorTypeId", colorTypeId);
				mapRet.put("colorTypeName", colorTypeName);
				mapRet.put("colorTypeCode", colorTypeCode);
				mapRet.put("backPersonOprid", backPersonOprid);
				mapRet.put("backPersonName", backPersonName);
				
				strRet = jacksonUtil.Map2json(mapRet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*当前登录人的销售线索角色*/
	public String getPersonRole(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			String roleWhere = "";
			//能查看所有地区的角色：线索管理员+一般人员
			String role = getHardCodePoint.getHardCodePointVal("TZ_XSXS_ALLDQ_ROLE");
			String[] roleArr = role.split(",");
			for(int i=0;i<roleArr.length;i++) {
				if(i==0) {
					roleWhere = "'" + roleArr[i] + "'";
				} else {
					roleWhere += "," + "'" + roleArr[i] + "'";
				}
			}
			
			String sql = "SELECT COUNT(1) FROM PSROLEUSER WHERE ROLEUSER='" + oprid + "' AND ROLENAME IN (" + roleWhere + ")";
			Integer roleCount = sqlQuery.queryForObject(sql, "Integer");
			
			String viewAllFlag = "";
			if(roleCount>0) {
				viewAllFlag = "Y";
			}
			
			mapRet.put("viewAllFlag", viewAllFlag);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*获取历史线索客户信息*/
	public String getCustomerInfo(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			String mobile = "", company = "", position = "", phone = "";
			
			jacksonUtil.json2Map(strParams);		
			String cusOprid = jacksonUtil.getString("cusOprid");
			String cusName = jacksonUtil.getString("cusName");
			String nameCompany = jacksonUtil.getString("nameCompany");
			String companyName = "";
			
			String sql = "";
			
			if(cusOprid!=null && !"".equals(cusOprid)) {
				sql = "SELECT TZ_MOBILE,TZ_COMP_CNAME,TZ_POSITION,TZ_PHONE FROM PS_TZ_XSXS_INFO_T WHERE TZ_KH_OPRID='" + cusOprid + "'";
			} else {
				String[] arrInfo = nameCompany.split(",");
				if(arrInfo.length==2) {
					companyName =  arrInfo[1];
					sql = "SELECT TZ_MOBILE,TZ_COMP_CNAME,TZ_POSITION,TZ_PHONE FROM PS_TZ_XSXS_INFO_T WHERE TZ_REALNAME='" + cusName + "' AND TZ_COMP_CNAME='" + companyName + "'";
				} else {
					sql = "SELECT TZ_MOBILE,TZ_COMP_CNAME,TZ_POSITION,TZ_PHONE FROM PS_TZ_XSXS_INFO_T WHERE TZ_REALNAME='" + cusName + "'";
				}
			}
			
			Map<String,Object> mapData = sqlQuery.queryForMap(sql);
			if(mapData!=null) {
				mobile = mapData.get("TZ_MOBILE") == null ? "" : mapData.get("TZ_MOBILE").toString();
				company = mapData.get("TZ_COMP_CNAME") == null ? "" : mapData.get("TZ_COMP_CNAME").toString();
				position = mapData.get("TZ_POSITION") == null ? "" : mapData.get("TZ_POSITION").toString();
				phone = mapData.get("TZ_PHONE") == null ? "" : mapData.get("TZ_PHONE").toString();
			}
	
			mapRet.put("mobile", mobile);
			mapRet.put("company", company);
			mapRet.put("position", position);
			mapRet.put("phone", phone);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*根据手机号码判断是否存在未关闭的线索*/
	public String isExist(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			String existFlag = "";
			
			jacksonUtil.json2Map(strParams);
			String cusMobile = jacksonUtil.getString("cusMobile");
			
			if(cusMobile!=null && !"".equals(cusMobile)) {
				Integer existNum = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_INFO_T WHERE TZ_MOBILE=? AND TZ_LEAD_STATUS<>'G'", new Object[]{cusMobile},"Integer");
				if(existNum>0) {
					//已存在不创建线索
					existFlag = "Y";
				} 
			}
			
			mapRet.put("existFlag", existFlag);
			strRet = jacksonUtil.Map2json(mapRet);			
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}

}