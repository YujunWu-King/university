package com.tranzvision.gd.TZCallCenterBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsBmbTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 接待单详细信息
 * @author yuds
 */
@Service("com.tranzvision.gd.TZCallCenterBundle.service.Impl.TZCallCenterServiceImpl")
public class TZCallCenterServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	
	@Autowired
	private PsTzXsxsBmbTMapper psTzXsxsBmbTMapper;
	
	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		try {
			//String orgId = jacksonUtil.getString("orgid");
			String strTel = jacksonUtil.getString("tel");
			String strType = jacksonUtil.getString("type");
			//根据电话和类型查找
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "";
	}
	
	/* 系统变量列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(comParams);
		
		if(jacksonUtil.containsKey("type") 	//考生线索列表
				&& "CLUELIST".equals(jacksonUtil.getString("type"))){
			//当前登录人
			String currOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//考生oprid
			String oprid = jacksonUtil.getString("oprid");
			
			if(oprid != null && !"".equals(oprid)){
				String sql = "SELECT TZ_LEAD_ID,A.TZ_REALNAME,TZ_LEAD_STATUS,(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_LEAD_STATUS' AND TZ_ZHZ_ID = A.TZ_LEAD_STATUS ";
				sql += "AND TZ_EFF_STATUS = 'A') AS TZ_LEAD_STATUS_DESC,TZ_ZR_OPRID,B.TZ_REALNAME AS TZ_ZRR_NAME,date_format(A.ROW_ADDED_DTTM,'%Y-%m-%d %H:%i') as ROW_ADDED_DTTM,TZ_RSFCREATE_WAY,(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_RSFCREATE_WAY' ";
				sql += "AND TZ_ZHZ_ID = A.TZ_RSFCREATE_WAY AND TZ_EFF_STATUS = 'A') AS TZ_RSFCREATE_WAY_DESC FROM PS_TZ_XSXS_INFO_T A  LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON(A.TZ_ZR_OPRID=B.OPRID) WHERE TZ_KH_OPRID = ?";
				
				List<Map<String,Object>> clueList = sqlQuery.queryForList(sql, new Object[]{ oprid });
				if(clueList != null && clueList.size() > 0){
					for(Map<String,Object> clueMap : clueList){
						Map<String,Object> ksxsMap = new HashMap<String,Object>();
						
						String clueId = clueMap.get("TZ_LEAD_ID") == null ? "" : clueMap.get("TZ_LEAD_ID").toString();
						String name = clueMap.get("TZ_REALNAME") == null ? "" : clueMap.get("TZ_REALNAME").toString();
						String clueStatus = clueMap.get("TZ_LEAD_STATUS_DESC") == null ? "" : clueMap.get("TZ_LEAD_STATUS_DESC").toString();
						String zrrOprid = clueMap.get("TZ_ZR_OPRID") == null ? "" : clueMap.get("TZ_ZR_OPRID").toString();
						String zrrName = clueMap.get("TZ_ZRR_NAME") == null ? "" : clueMap.get("TZ_ZRR_NAME").toString();
						String createType = clueMap.get("TZ_RSFCREATE_WAY_DESC") == null ? "" : clueMap.get("TZ_RSFCREATE_WAY_DESC").toString();
						String addTime = clueMap.get("ROW_ADDED_DTTM") == null ? "" : clueMap.get("ROW_ADDED_DTTM").toString();
						
						//其他责任人
						String qtZrrSql = "select group_concat(TZ_REALNAME SEPARATOR '，') from TZ_XS_QTZRR_V A where TZ_LEAD_ID=? and TZ_ZRR_OPRID<>?";
						String qtZrrName = sqlQuery.queryForObject(qtZrrSql, new Object[]{clueId, zrrOprid}, "String");
						if(qtZrrName != null && !"".equals(qtZrrName)){
							if(zrrName == null || "".equals(zrrName)){
								zrrName = qtZrrName;
							}else{
								zrrName = zrrName + "，" +  qtZrrName;
							}
						}
						
						//当前登录人是否为责任人
						String isClueZrr = "N";
						if(currOprid != null && currOprid.equals(zrrOprid)){
							isClueZrr = "Y";
						}else{
							isClueZrr = sqlQuery.queryForObject("select 'Y' from PS_TZ_XS_QTZRR_TBL where TZ_LEAD_ID=? and TZ_ZRR_OPRID=?", 
									new Object[]{ clueId, currOprid }, "String");
							if(isClueZrr == null) isClueZrr = "N";
						}
						
						ksxsMap.put("clueId", clueId);
						ksxsMap.put("name", name);
						ksxsMap.put("clueStatus", clueStatus);
						ksxsMap.put("zrrName", zrrName);
						ksxsMap.put("createType", createType);
						ksxsMap.put("addTime", addTime);
						ksxsMap.put("isClueZrr", isClueZrr);
						
						listData.add(ksxsMap);
					}
					
					//查询总数
					sql = "select count(1) from PS_TZ_XSXS_INFO_T where TZ_KH_OPRID = ?";
					int total = sqlQuery.queryForObject(sql, new Object[]{ oprid }, "int");
					
					mapRet.put("total", total);
					mapRet.put("root", listData);
				}
			}
			
		}else{
			String strTransSQL = "SELECT TZ_ZHZ_DMS  FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_EFF_STATUS='A' AND TZ_ZHZ_ID=?";
			
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_CREATE_DTIME", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "OPRID", "TZ_CLASS_ID", "TZ_CLASS_NAME","TZ_BATCH_ID","TZ_BATCH_NAME","TZ_APP_INS_ID","TZ_CREATE_DTIME","TZ_APP_FORM_STA" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null){
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					//第一个grid
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("oprid", rowList[0]);
					mapList.put("classId", rowList[1]);
					mapList.put("className", rowList[2]);
					mapList.put("batchId", rowList[3]);
					mapList.put("batchName", rowList[4]);
					mapList.put("appInsId", rowList[5]);
					mapList.put("appCreateDtime", rowList[6]);
					mapList.put("appBmStatus", rowList[7]);
					
					String strTplId = sqlQuery.queryForObject("SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?", new Object[]{rowList[5]}, "String");
					if(strTplId==null){
						strTplId = "";
					}
					mapList.put("clpsBmbTplId", strTplId);
					//第二个grid
//					String strSQl = "SELECT TZ_ENTER_CLPS,TZ_RESULT FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?";
//					Map<String, Object> SMAP = sqlQuery.queryForMap(strSQl, new Object[]{rowList[5]});
//					
//					if(SMAP!=null){
//						String strExistFlg = SMAP.get("TZ_ENTER_CLPS")==null?"":String.valueOf(SMAP.get("TZ_ENTER_CLPS"));
//						String strPshRel = SMAP.get("TZ_RESULT")==null?"":String.valueOf(SMAP.get("TZ_RESULT"));
//						mapList.put("isOnMaterials", strExistFlg);				
//						mapList.put("materialResult", strPshRel);
//					}
					
					//第三个grid
					String strSQl2 = "SELECT TZ_TIME,TZ_ADDRESS,TZ_RESULT_CODE FROM TZ_IMP_MSZG_TBL WHERE TZ_APP_INS_ID=?";
			//		String strSQl2 = "SELECT TZ_TIME,TZ_ADDRESS,TZ_RESULT,TZ_RESULT_CODE FROM TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?";
					Map<String, Object> SMAP2= sqlQuery.queryForMap(strSQl2, new Object[]{rowList[5]});
					String strSQL3="select TZ_RESULT_CODE from TZ_IMP_MSJG_TBL where TZ_APP_INS_ID=?";
					String strPar3=sqlQuery.queryForObject(strSQL3, new Object[]{rowList[5]},"String");
					if(SMAP2!=null){
						String strPar1 = SMAP2.get("TZ_TIME")==null?"":String.valueOf(SMAP2.get("TZ_TIME"));
						String strPar2 = SMAP2.get("TZ_ADDRESS")==null?"":String.valueOf(SMAP2.get("TZ_ADDRESS"));
						mapList.put("interviewDtime", strPar1);				
						mapList.put("interviewLocation", strPar2);
						mapList.put("interviewResult", strPar3);
					}	
					
					listData.add(mapList);
				}
				
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strOprid = jacksonUtil.getString("OPRID");
			System.out.println("strOprid="+strOprid);
//			System.out.println("1111="+strOprid);
			String strPhone = jacksonUtil.getString("phone");
			System.out.println("strPhone="+strPhone);
//			String sql1="select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_MOBILE=? and TZ_JG_ID=?";
//			String strOprid=sqlQuery.queryForObject(sql1,new Object[]{strPhone,"SEM"}, "String");
		
			String strCallXh = jacksonUtil.getString("callXh");
			System.out.println("strCallXh="+strCallXh);
			//接待单ID
			returnMap.put("receiveId", strCallXh);
			//人员OPRID
			returnMap.put("oprId", strOprid);
			//注册状态
			TZCallCenterServiceImpl  callCenterService = new TZCallCenterServiceImpl(); 
			if("".equals(strOprid)||strOprid==null||"null".equals(strOprid)){
				returnMap.put("registerStatus", "未注册");				
			}else{
				returnMap.put("registerStatus", "已注册");
			}
			
			// 头像地址;
			String titleImageUrlSQL = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
			Map<String , Object> imgMap = sqlQuery.queryForMap(titleImageUrlSQL,new Object[]{strOprid});
			String titleImageUrl = "";
			if(imgMap != null){
				String tzAttAUrl = (String)imgMap.get("TZ_ATT_A_URL");
				String sysImgName = (String)imgMap.get("TZ_ATTACHSYSFILENA");
				if(tzAttAUrl != null &&!"".equals(tzAttAUrl)
					&& sysImgName != null &&!"".equals(sysImgName)){
					if(tzAttAUrl.lastIndexOf("/") + 1 == tzAttAUrl.length()){
						titleImageUrl = tzAttAUrl + sysImgName;
					}else{
						titleImageUrl = tzAttAUrl + "/" + sysImgName;
					}
				}
			}
			returnMap.put("titleImageUrl", titleImageUrl);
			
			String strTransSQL = "SELECT TZ_ZHZ_DMS  FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_EFF_STATUS='A' AND TZ_ZHZ_ID=?";
			
			//来电时间和主叫号码
			String strSQL1 = "SELECT TZ_CALL_TYPE,TZ_PHONE,date_format(TZ_CALL_DTIME,'%Y-%m-%d %H:%i') TZ_CALL_DTIME,TZ_DEALWITH_ZT,TZ_DESCR,TZ_DLZH_ID,TZ_LEAD_ID FROM PS_TZ_PH_JDD_TBL WHERE TZ_XH=?";
			Map<String, Object> return1 = sqlQuery.queryForMap(strSQL1, new Object[]{strCallXh});
			if(return1!=null&&return1.size()>0){
				String strPar1 = return1.get("TZ_CALL_TYPE")==null?"":String.valueOf(return1.get("TZ_CALL_TYPE"));
				String strPar2 = return1.get("TZ_PHONE")==null?"":String.valueOf(return1.get("TZ_PHONE"));
				String strPar3 = return1.get("TZ_CALL_DTIME")==null?"":String.valueOf(return1.get("TZ_CALL_DTIME"));
				String strPar4 = return1.get("TZ_DEALWITH_ZT")==null?"":String.valueOf(return1.get("TZ_DEALWITH_ZT"));
				String strPar5 = return1.get("TZ_DESCR")==null?"":String.valueOf(return1.get("TZ_DESCR"));
				String strPar6 = return1.get("TZ_DLZH_ID")==null?"":String.valueOf(return1.get("TZ_DLZH_ID"));
				String leadId = return1.get("TZ_LEAD_ID")==null?"":String.valueOf(return1.get("TZ_LEAD_ID"));
				
				returnMap.put("phoneNum", strPar2);
				//来电号码拼接归属地信息
				String strAreaDesc = "";
				if("0".equals(TZCallCenterServiceImpl.left(strPar2,1))){
					String strAreaCode = TZCallCenterServiceImpl.left(strPar2,3);
					String strTmpSQL = "SELECT TZ_MOBILE_AREA FROM PS_TZ_SJ_GSD_REL_T WHERE TZ_AREA_CODE=?";
					strAreaDesc = sqlQuery.queryForObject(strTmpSQL, new Object[]{strAreaCode}, "String");
					if(strAreaDesc==null||"".equals(strAreaDesc)){
						strAreaCode = TZCallCenterServiceImpl.left(strPar2,4);
						strAreaDesc = sqlQuery.queryForObject(strTmpSQL, new Object[]{strAreaCode}, "String");
					}
				}else{
					String strAreaCode = TZCallCenterServiceImpl.left(strPar2,7);
					String strTmpSQL = "SELECT TZ_MOBILE_AREA FROM PS_TZ_SJ_GSD_REL_T WHERE TZ_MOBILE_NUMBER=?";
					strAreaDesc = sqlQuery.queryForObject(strTmpSQL, new Object[]{strAreaCode}, "String");
					try{
						String[] spli = strAreaDesc.split(" ");
						if(spli[0].equals(spli[1])){
							strAreaDesc = spli[0];
						}
					}catch(Exception e){
						/*doNothing*/
					}					
				}
				if(strAreaDesc!=null&&!"".equals(strAreaDesc)){
					strPar2 = strPar2 + "（" + strAreaDesc + "）";
				}
				returnMap.put("callPhoneNum", strPar2);
				returnMap.put("callDTime", strPar3);
				returnMap.put("dealwithZT", strPar4);
				returnMap.put("callDesc", strPar5);
				returnMap.put("leadId", leadId);
				String strStaffName = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?", new Object[]{strPar6}, "String");
				returnMap.put("staffName", strStaffName);
			}
			
			String strSQL2 = "SELECT TZ_REALNAME,TZ_GENDER,BIRTHDATE,TZ_BLACK_NAME FROM PS_TZ_REG_USER_T WHERE OPRID=?";
			Map<String, Object> return2 = sqlQuery.queryForMap(strSQL2, new Object[]{strOprid});
			if(return2!=null&&return2.size()>0){
				String strPar1 = return2.get("TZ_REALNAME")==null?"":String.valueOf(return2.get("TZ_REALNAME"));
				String strPar2 = return2.get("TZ_GENDER")==null?"":String.valueOf(return2.get("TZ_GENDER"));
				String strPar3 = return2.get("BIRTHDATE")==null?"":String.valueOf(return2.get("BIRTHDATE"));
				String strPar4 = return2.get("TZ_BLACK_NAME")==null?"":String.valueOf(return2.get("TZ_BLACK_NAME"));
				returnMap.put("bmrName", strPar1);
				strPar2 = sqlQuery.queryForObject(strTransSQL, new Object[]{"TZ_GENDER",strPar2},"String");
				returnMap.put("bmrGender", strPar2);
				returnMap.put("bmrBirthdate", strPar3);
				if("".equals(strPar4)||strPar4==null){
					strPar4 = "N";
				}
				strPar4 = sqlQuery.queryForObject(strTransSQL, new Object[]{"TZ_BLACK_NAME",strPar4},"String");
				returnMap.put("bmrBlackList", strPar4);
			}
			
			String strSQL3 = "SELECT TZ_EMAIL,TZ_JIHUO_ZT,date_format(TZ_ZHCE_DT,'%Y-%m-%d %H:%i') TZ_ZHCE_DT,TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
			Map<String, Object> return3 = sqlQuery.queryForMap(strSQL3, new Object[]{strOprid});
			if(return3!=null&&return3.size()>0){
				String strPar1 = return3.get("TZ_EMAIL")==null?"":String.valueOf(return3.get("TZ_EMAIL"));
				String strPar2 = return3.get("TZ_JIHUO_ZT")==null?"":String.valueOf(return3.get("TZ_JIHUO_ZT"));
				String strPar3 = return3.get("TZ_ZHCE_DT")==null?"":String.valueOf(return3.get("TZ_ZHCE_DT"));
				String strPar4 = return3.get("TZ_MSH_ID")==null?"":String.valueOf(return3.get("TZ_MSH_ID"));
				returnMap.put("bmrRegEmail", strPar1);
				strPar2 = sqlQuery.queryForObject(strTransSQL, new Object[]{"TZ_JIHUO_ZT",strPar2},"String");
				returnMap.put("bmrAccActiveStatus", strPar2);
				returnMap.put("bmrRegDtime", strPar3);
				returnMap.put("bmrMshId",strPar4);
			}
			//锁定状态
			String strSQL4 = "SELECT ACCTLOCK FROM PSOPRDEFN WHERE OPRID=?";
			String strAccLockCode = sqlQuery.queryForObject(strSQL4, new Object[]{strOprid}, "String");
			if(strAccLockCode!=null){
				String strAccLockDesc = sqlQuery.queryForObject(strTransSQL, new Object[]{"ACCTLOCK",strAccLockCode},"String");
				returnMap.put("bmrLockStatus",strAccLockDesc);
			}
			//报考班级及批次
			String strBmBatchInfo = "";
			String strSQL5 = "SELECT TZ_CLASS_NAME,TZ_BATCH_NAME FROM PS_TZ_USER_CALL1_VW WHERE OPRID=? ORDER BY TZ_CREATE_DTIME DESC";
			List<Map<String, Object>> return1List = sqlQuery.queryForList(strSQL5, new Object[]{strOprid});
			if(return1List!=null&&return1List.size()>0){
				for(Object sObj:return1List){
					Map<String, Object> return4 = (Map<String, Object>) sObj;
					String strPar1 = return4.get("TZ_CLASS_NAME")==null?"":String.valueOf(return4.get("TZ_CLASS_NAME"));
					String strPar2 = return4.get("TZ_BATCH_NAME")==null?"":String.valueOf(return4.get("TZ_BATCH_NAME"));
					if(strPar2==null||"".equals(strPar2)){
						strBmBatchInfo = strBmBatchInfo + strPar1 + ";";
					}else{
						strBmBatchInfo = strBmBatchInfo + strPar1 + "_" + strPar2 + ";";
					}
				}
			}
			returnMap.put("bmrBkProject", strBmBatchInfo);
			//历史来电记录
			String sql = "SELECT COUNT(1) FROM PS_TZ_PH_JDD_TBL WHERE TZ_PHONE=? AND TZ_XH<>?";
			String callCount = sqlQuery.queryForObject(sql, new Object[]{strPhone,strCallXh}, "String");
			returnMap.put("viewHistoryCall", callCount);
			//报名活动数			
			String strActCountSQL = "";
			Integer actCount = 0;
			if(strOprid!=null&&!"".equals(strOprid)){
				strActCountSQL = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_G_V WHERE ( OPRID=? OR TZ_ZY_SJ=? ) AND TZ_NREG_STAT='1'";
				actCount = sqlQuery.queryForObject(strActCountSQL, new Object[]{strOprid,strPhone}, "Integer");					
			}else{
				strActCountSQL = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_G_V WHERE TZ_ZY_SJ=? AND TZ_NREG_STAT='1'";
				actCount = sqlQuery.queryForObject(strActCountSQL, new Object[]{strPhone}, "Integer");
			}
			if(actCount==null){
				actCount = 0;
			}
			
			returnMap.put("bmrBmActCount", String.valueOf(actCount));
			
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			
			e.printStackTrace();
		}
		
		return jacksonUtil.Map2json(returnMap);
	}
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strResponse = "";
		String strOprid = "";
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			TZCallCenterServiceImpl  callCenterService = new TZCallCenterServiceImpl(); 
			jacksonUtil.json2Map(strParams);
			// 获取来电人员的OPRID
			if ("GETUSER".equals(oprType)) {
				String strPhone = jacksonUtil.getString("phone");
				String strType = jacksonUtil.getString("type");
				String strCallXh = jacksonUtil.getString("callXh");
				
				String tmpSQL = "SELECT TZ_OPRID FROM PS_TZ_PH_JDD_TBL WHERE TZ_XH=?";				
				strOprid = sqlQuery.queryForObject(tmpSQL, new Object[]{strCallXh}, "String");
				if(strOprid==null||"".equals(strOprid)){
					strOprid = callCenterService.findOprID(strCallXh, strPhone);
					if(strOprid!=null&&!"".equals(strOprid)){
						tmpSQL = "UPDATE PS_TZ_PH_JDD_TBL SET TZ_OPRID=? WHERE TZ_XH=?";
						sqlQuery.update(tmpSQL, new Object[]{strOprid,strCallXh});
					}
				}
				returnMap.put("OPRID", strOprid);
				//历史来电数量
				String sql = "SELECT COUNT(1) FROM PS_TZ_PH_JDD_TBL WHERE TZ_PHONE=? AND TZ_XH<>?";
				String callCount = sqlQuery.queryForObject(sql, new Object[]{strPhone,strCallXh}, "String");
				returnMap.put("viewHistoryCall", callCount);
				
				//报名活动数				
				String strActCountSQL = "";
				Integer actCount = 0;
				if(strOprid!=null&&!"".equals(strOprid)){
					strActCountSQL = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_G_V WHERE ( OPRID=? OR TZ_ZY_SJ=? ) AND TZ_NREG_STAT='1'";
					actCount = sqlQuery.queryForObject(strActCountSQL, new Object[]{strOprid,strPhone}, "Integer");					
				}else{
					strActCountSQL = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_G_V WHERE TZ_ZY_SJ=? AND TZ_NREG_STAT='1'";
					actCount = sqlQuery.queryForObject(strActCountSQL, new Object[]{strPhone}, "Integer");
				}
				if(actCount==null){
					actCount = 0;
				}
				
				returnMap.put("bmrBmActCount", String.valueOf(actCount));
				
				return jacksonUtil.Map2json(returnMap);
			}
			if("SEARCHUSER".equals(oprType)){
				String strPhone = jacksonUtil.getString("phone");
				String strEmail = jacksonUtil.getString("email");
				String strName = jacksonUtil.getString("name");
				String strMshId = jacksonUtil.getString("mshId");
				String strXh = jacksonUtil.getString("callXh");
				
				String sql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL";
				String countSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL";
				
				String strWhere = " WHERE TZ_RYLX='ZCYH'";
				if(strPhone!=null&&!"".equals(strPhone)){
					strWhere = strWhere + " AND TZ_MOBILE LIKE '%" + strPhone + "%'";					
				}
				
				if(strEmail!=null&&!"".equals(strEmail)){
					strWhere = strWhere + " AND TZ_EMAIL LIKE '%" + strEmail + "%'";
				}
				
				if(strName!=null&&!"".equals(strName)){
					strWhere = strWhere + " AND TZ_REALNAME LIKE '%" + strName + "%'";
				}
				
				if(strMshId!=null&&!"".equals(strMshId)){
					strWhere = strWhere + " AND TZ_MSH_ID LIKE '%" + strMshId + "%'";
				}
				
				sql = sql + strWhere + " limit 0,1";
				countSQL = countSQL + strWhere;
				strOprid = sqlQuery.queryForObject(sql, new Object[]{}, "String");
				Integer count = sqlQuery.queryForObject(countSQL, new Object[]{}, "Integer");
				if(count==null){
					count = 0;
				}
				returnMap.put("PSNCOUNT", count);
				returnMap.put("OPRID", strOprid);
				if(count==1&&strOprid!=null&&!"".equals(strOprid)){
					sql = "UPDATE PS_TZ_PH_JDD_TBL SET TZ_OPRID=? WHERE TZ_XH=?";
					sqlQuery.update(sql, new Object[]{strOprid,strXh});
				}
				return jacksonUtil.Map2json(returnMap);
			}
			//激活账号
			if("ACTIVE".equals(oprType)){
				strOprid = jacksonUtil.getString("OPRID");
				if(strOprid!=null&&!"".equals(strOprid)){
					String strUpdateSQL = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_JIHUO_ZT = 'Y' WHERE OPRID=?";
					int success = sqlQuery.update(strUpdateSQL, new Object[]{strOprid});
					if(success > 0){
						returnMap.put("success", "true");
					}else{
						errorMsg[0] = "1";
						errorMsg[1] = "激活账号失败";
					}
				}
				return jacksonUtil.Map2json(returnMap);
			}
			//修改密码
			if("UPDATEPSW".equals(oprType)){
				strOprid = jacksonUtil.getString("OPRID");
				if(strOprid!=null&&!"".equals(strOprid)){					
					String password = jacksonUtil.getString("password");
					
					String tmpPassword = DESUtil.encrypt(password,"TZGD_Tranzvision");
					
					Psoprdefn psoprdefn = new Psoprdefn();
				    psoprdefn.setOprid(strOprid);
				    psoprdefn.setOperpswd(tmpPassword);
				    
				    int success = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
					if (success > 0) {
						returnMap.put("success", "true");
					} else {
						errorMsg[0] = "1";
						errorMsg[1] = "修改用户密码失败。";
					}
					return jacksonUtil.Map2json(returnMap);
				}
			}
			//激活账号-锁定
			if("INVALID".equals(oprType)){
				strOprid = jacksonUtil.getString("OPRID");
				if(strOprid!=null&&!"".equals(strOprid)){
					Psoprdefn psoprdefn = new Psoprdefn();
				    psoprdefn.setOprid(strOprid);
				    short acclock = 1;
				    psoprdefn.setAcctlock(acclock);
				    int success = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
					if (success > 0) {
						returnMap.put("success", "true");
					} else {
						errorMsg[0] = "1";
						errorMsg[1] = "锁定用户失败。";
					}
				}
				return jacksonUtil.Map2json(returnMap);
			}
			//加入黑名单
			if("ADDBLACK".equals(oprType)){
				strOprid = jacksonUtil.getString("OPRID");
				if(strOprid!=null&&!"".equals(strOprid)){
					String strUpdateSQL = "UPDATE PS_TZ_REG_USER_T SET TZ_BLACK_NAME='Y' WHERE OPRID=?";
					int success = sqlQuery.update(strUpdateSQL, new Object[]{strOprid});
					if(success > 0){
						returnMap.put("success", "true");
					}else{
						errorMsg[0] = "1";
						errorMsg[1] = "加入黑名单失败";
					}
				}
				return jacksonUtil.Map2json(returnMap);
			}
			//保存接待单信息
			if("SAVEINFO".equals(oprType)){
				strOprid = jacksonUtil.getString("OPRID");
				String strCallXh = jacksonUtil.getString("callXh");
				String strCallDesc = jacksonUtil.getString("callDesc");
				String strDealwithZT = jacksonUtil.getString("dealwithZT");
				if(strCallXh!=null&&!"".equals(strCallXh)){
					String strUpdateSQL = "UPDATE PS_TZ_PH_JDD_TBL SET TZ_DEALWITH_ZT=?,TZ_DESCR=?,TZ_OPRID=? WHERE TZ_XH=?";
					int success = sqlQuery.update(strUpdateSQL, new Object[]{strDealwithZT,strCallDesc,strOprid,strCallXh});
					if(success > 0){
						//修改报名表提交状态
						List<?> mRecords = jacksonUtil.getList("mRecords");
						if(mRecords.size()>0){
							for (Object obj : mRecords) {

								Map<String, Object> mapFormData = (Map<String, Object>) obj;
								String str_app_ins_id = mapFormData.get("appInsId")==null?"":String.valueOf(mapFormData.get("appInsId"));
								String str_app_status = mapFormData.get("appBmStatus")==null?"":String.valueOf(mapFormData.get("appBmStatus"));
								if(str_app_ins_id!=null&&!"".equals(str_app_ins_id)){
									strUpdateSQL = "UPDATE PS_TZ_APP_INS_T SET TZ_APP_FORM_STA=? WHERE TZ_APP_INS_ID=?";
									success = sqlQuery.update(strUpdateSQL, new Object[]{str_app_status,str_app_ins_id});
								}
							}
						}
						returnMap.put("success", "true");
					}else{
						errorMsg[0] = "1";
						errorMsg[1] = "保存信息失败";
					}					
					
				}
			}
			
			if("SMSMODEL".equals(oprType)){
				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("total", 0);
				ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
				mapRet.put("root", listData);
				
				String strOrgId = jacksonUtil.getString("ORGID");
				String strSmsSQL = "SELECT TZ_TMPL_ID,TZ_TMPL_NAME FROM PS_TZ_SMSTMPL_VW WHERE TZ_JG_ID=?";
				List<Map<String,Object>> stMap = sqlQuery.queryForList(strSmsSQL, new Object[]{strOrgId});
				System.out.println("stMap="+stMap);
	
				if(stMap!=null&&stMap.size()>0){
					for(Object sObj:stMap){
						Map<String,Object> sMap = (Map<String,Object>) sObj;
						String strPar1 = sMap.get("TZ_TMPL_ID")==null?"":String.valueOf(sMap.get("TZ_TMPL_ID"));
						String strPar2 = sMap.get("TZ_TMPL_NAME")==null?"":String.valueOf(sMap.get("TZ_TMPL_NAME"));
						
						Map<String, Object> tMap = new HashMap<String, Object>();
						tMap.put("smsId", strPar1);
						tMap.put("smsName", strPar2);
						
						listData.add(tMap);
					}
				
				
				}
				mapRet.replace("total", stMap.size());
				mapRet.replace("root", listData);
				return jacksonUtil.Map2json(mapRet);
			}
			
			if("tzSendMessage".equals(oprType)){
				Map<String, Object> mapRet = new HashMap<String, Object>();
				
				String strCallPhone = jacksonUtil.getString("phone");
				String strOprId = jacksonUtil.getString("oprId");
				//当前机构
				String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				//创建短信发送听众
				String crtAudi = createTaskServiceImpl.createAudience("",currentOrgId,"电话盒子接待单短信发送", "CCLL");
				
				if (!"".equals(crtAudi)) {
					String name="";
					String email = "";
					if(strOprId!=null&&!"".equals(strOprId)){
						String sql = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
						Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{currentOrgId,strOprId});
						if(mapData!=null) {
							name = mapData.get("TZ_REALNAME") == null ? "" : mapData.get("TZ_REALNAME").toString();
							email = mapData.get("TZ_EMAIL") == null ? "" : mapData.get("TZ_EMAIL").toString();
						}
					}
					createTaskServiceImpl.addAudCy(crtAudi,name, "", strCallPhone, "", email, "", "", strOprId, "","", "");
					
					mapRet.put("audienceId", crtAudi);
					return jacksonUtil.Map2json(mapRet);
				}
			}
			//创建销售线索
			if("CREATECLUE".equals(oprType)){
				Map<String, Object> mapRet = new HashMap<String, Object>();
				
				strOprid = jacksonUtil.getString("OPRID");
				String strCallXh = jacksonUtil.getString("callXh");
				String phoneNum = jacksonUtil.getString("phoneNum");
				
				//当前机构
				String currOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				String currOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				
				if(phoneNum != null && !"".equals(phoneNum) 
						&& strCallXh != null && !"".equals(strCallXh)){
					
					String sql = "select TZ_LEAD_ID from PS_TZ_PH_JDD_TBL where TZ_XH=? and TZ_PHONE=?";
					Map<String, Object> return1 = sqlQuery.queryForMap(sql, new Object[]{ strCallXh, phoneNum });
					String leadId = "";
					int existsCount = 0;
					if(return1 != null){
						leadId = return1.get("TZ_LEAD_ID")==null?"":String.valueOf(return1.get("TZ_LEAD_ID"));
					}
					
					sql = "select count(*) from PS_TZ_XSXS_INFO_T where TZ_JG_ID=? and TZ_LEAD_STATUS<>'G' and (TZ_MOBILE<>' ' and TZ_MOBILE=?)";
					existsCount = sqlQuery.queryForObject(sql, new Object[]{ currOrgId, phoneNum }, "int");
					
					if(leadId != null && !"".equals(leadId)){
						errorMsg[0] = "1";
						errorMsg[1] = "接待单已创建销售线索";
					}else{
						if(existsCount > 0){
							errorMsg[0] = "1";
							errorMsg[1] = "创建失败，系统中已存在手机对应的线索";
						}else{
							sql = "SELECT TZ_REALNAME,TZ_COMPANY_NAME FROM PS_TZ_REG_USER_T WHERE OPRID=?";
							Map<String, Object> return2 = sqlQuery.queryForMap(sql, new Object[]{strOprid});
							String name = "";
							String company = "";
							if(return2 != null){
								name = return2.get("TZ_REALNAME")==null?"":String.valueOf(return2.get("TZ_REALNAME"));
								company = return2.get("TZ_COMPANY_NAME")==null?"":String.valueOf(return2.get("TZ_COMPANY_NAME"));
							}
							
							sql = "SELECT TZ_EMAIL FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
							String email = sqlQuery.queryForObject(sql, new Object[]{ strOprid }, "String");
							
							
							String TZ_LEAD_ID = String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
							
							PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = new PsTzXsxsInfoTWithBLOBs();
							psTzXsxsInfoT.setTzLeadId(TZ_LEAD_ID);
							psTzXsxsInfoT.setTzJgId(currOrgId);

							psTzXsxsInfoT.setTzRsfcreateWay("F"); /*接待单快速创建*/
							psTzXsxsInfoT.setTzLeadStatus("A");
							
							psTzXsxsInfoT.setTzRealname(name);
							psTzXsxsInfoT.setTzKhOprid(strOprid);
							psTzXsxsInfoT.setTzEmail(email);
							psTzXsxsInfoT.setTzMobile(phoneNum);
							psTzXsxsInfoT.setTzCompCname(company);
							
							psTzXsxsInfoT.setRowAddedDttm(new Date());
							psTzXsxsInfoT.setRowAddedOprid(currOprid);
							psTzXsxsInfoT.setRowLastmantDttm(new Date());
							psTzXsxsInfoT.setRowLastmantOprid(currOprid);
							
							int rtn = psTzXsxsInfoTMapper.insert(psTzXsxsInfoT);
							if(rtn > 0){
								sql = "UPDATE PS_TZ_PH_JDD_TBL SET TZ_LEAD_ID=? WHERE TZ_XH=?";
								sqlQuery.update(sql, new Object[]{TZ_LEAD_ID, strCallXh});
								
								mapRet.put("leadId", TZ_LEAD_ID);
								
								//线索关联报名表
//								sql = "select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T A where OPRID=? and not exists(select 'Y' from PS_TZ_XSXS_BMB_T B join PS_TZ_XSXS_INFO_T C on(B.TZ_LEAD_ID=C.TZ_LEAD_ID) where B.TZ_APP_INS_ID=A.TZ_APP_INS_ID and C.TZ_LEAD_STATUS<>'G') limit 0,1";
								sql = "select max(TZ_APP_INS_ID) as TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where OPRID=?";
								Long appInsId = sqlQuery.queryForObject(sql, new Object[]{ strOprid }, "Long");
								if(appInsId != null && appInsId > 0){
									PsTzXsxsBmbT psTzXsxsBmbT = new PsTzXsxsBmbT(); 
									psTzXsxsBmbT.setTzLeadId(TZ_LEAD_ID);
									psTzXsxsBmbT.setTzAppInsId(appInsId);
									psTzXsxsBmbT.setRowAddedDttm(new Date());
									psTzXsxsBmbT.setRowAddedOprid(currOprid);
									psTzXsxsBmbT.setRowLastmantDttm(new Date());
									psTzXsxsBmbT.setRowLastmantOprid(currOprid);
									
									psTzXsxsBmbTMapper.insert(psTzXsxsBmbT);
								}
							}
						}
					}
				}
				return jacksonUtil.Map2json(mapRet);
			}
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();			
		}
		return strResponse;
	}
	
	//从左边截取字符串
	public static String left(String str,int len){
		
		try{
			
			String subStr = str.substring(0, len);
			
			return subStr;
			
		}catch(Exception e){
			//字符截取出现问题，返回str
			e.printStackTrace();
		}
		
		return str;
	}
	
	//根据手机号码查找oprid
	public String findOprID(String strCallXh,String strPhone){
		String strOprid = "";
		String sql = "SELECT P.OPRID FROM PSOPRDEFN P,PS_TZ_AQ_YHXX_TBL Q WHERE P.OPRID=Q.OPRID AND Q.TZ_RYLX='ZCYH' AND P.ACCTLOCK='0' AND TZ_MOBILE=? limit 0,1";
		try{
			strOprid = sqlQuery.queryForObject(sql, new Object[]{strPhone}, "String");
		}catch(Exception e){
			/*无结果时会抛出异常*/
			strOprid = "";
		}
		if(strOprid==null||"".equals(strOprid)){
			sql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_RYLX='ZCYH' AND TZ_MOBILE=? limit 0,1";
			try{
				strOprid = sqlQuery.queryForObject(sql, new Object[]{strPhone}, "String");
			}catch(Exception e){
				/*无结果时会报错*/
			}
			if(strOprid==null||"".equals(strOprid)){
				sql = "SELECT TZ_LYDX_ID FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_ZY_SJ=? limit 0,1";
				try{
					strOprid = sqlQuery.queryForObject(sql, new Object[]{strPhone}, "String");
				}catch(Exception e){
					/*无结果时会报错*/
				}
				if(strOprid==null||"".equals(strOprid)){
					//查找上一次接待单涉及的人员						
					sql = "SELECT TZ_OPRID FROM PS_TZ_PH_JDD_TBL WHERE TZ_PHONE=? AND TZ_XH<>? ORDER BY TZ_CALL_DTIME DESC limit 0,1";
					try{
						strOprid = sqlQuery.queryForObject(sql, new Object[]{strPhone,strCallXh}, "String");
					}catch(Exception e){
						/*无结果时会报错*/
					}
					if(strOprid==null){
						strOprid = "";
					}						
				}
			}
		}
		return strOprid;
	}
}
