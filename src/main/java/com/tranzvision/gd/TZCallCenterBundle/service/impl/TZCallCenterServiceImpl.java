package com.tranzvision.gd.TZCallCenterBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 接待单详细信息
 * @author yuds
 */
@Service("com.tranzvision.gd.TZCallCenterBundle.service.Impl.TZCallCenterServiceImpl")
public class TZCallCenterServiceImpl  extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
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
				
				//第二个grid
				String strSQl = "SELECT TZ_ENTER_CLPS,TZ_RESULT FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?";
				Map<String, Object> SMAP = sqlQuery.queryForMap(strSQl, new Object[]{rowList[5]});
				
				if(SMAP!=null){
					String strExistFlg = SMAP.get("TZ_ENTER_CLPS")==null?"":String.valueOf(SMAP.get("TZ_ENTER_CLPS"));
					String strPshRel = SMAP.get("TZ_RESULT")==null?"":String.valueOf(SMAP.get("TZ_RESULT"));
					mapList.put("isOnMaterials", strExistFlg);				
					mapList.put("materialResult", strPshRel);
				}
				
				//第三个grid
				String strSQl2 = "SELECT TZ_TIME,TZ_ADDRESS,TZ_RESULT,TZ_RESULT_CODE FROM TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?";
				Map<String, Object> SMAP2= sqlQuery.queryForMap(strSQl2, new Object[]{rowList[5]});
				
				if(SMAP2!=null){
					String strPar1 = SMAP2.get("TZ_TIME")==null?"":String.valueOf(SMAP2.get("TZ_TIME"));
					String strPar2 = SMAP2.get("TZ_ADDRESS")==null?"":String.valueOf(SMAP2.get("TZ_ADDRESS"));
					String strPar3 = SMAP2.get("TZ_RESULT")==null?"":String.valueOf(SMAP2.get("TZ_RESULT"));
					
					mapList.put("interviewDtime", strPar1);				
					mapList.put("interviewLocation", strPar2);
					mapList.put("interviewResult", strPar3);
				}	
				
				listData.add(mapList);
			}
			
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
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
			String strPhone = jacksonUtil.getString("phone");
			String strCallXh = jacksonUtil.getString("callXh");
			//接待单ID
			returnMap.put("receiveId", strCallXh);
			//人员OPRID
			returnMap.put("oprId", strOprid);
			//注册状态
			if("".equals(strOprid)||strOprid==null){
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
			String strSQL1 = "SELECT TZ_CALL_TYPE,TZ_PHONE,TZ_CALL_DTIME,TZ_DEALWITH_ZT,TZ_DESCR FROM PS_TZ_PH_JDD_TBL WHERE TZ_XH=?";
			Map<String, Object> return1 = sqlQuery.queryForMap(strSQL1, new Object[]{strCallXh});
			if(return1!=null&&return1.size()>0){
				String strPar1 = return1.get("TZ_CALL_TYPE")==null?"":String.valueOf(return1.get("TZ_CALL_TYPE"));
				String strPar2 = return1.get("TZ_PHONE")==null?"":String.valueOf(return1.get("TZ_PHONE"));
				String strPar3 = return1.get("TZ_CALL_DTIME")==null?"":String.valueOf(return1.get("TZ_CALL_DTIME"));
				String strPar4 = return1.get("TZ_DEALWITH_ZT")==null?"":String.valueOf(return1.get("TZ_DEALWITH_ZT"));
				String strPar5 = return1.get("TZ_DESCR")==null?"":String.valueOf(return1.get("TZ_DESCR"));
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
			
			String strSQL3 = "SELECT TZ_EMAIL,TZ_JIHUO_ZT,TZ_ZHCE_DT,TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
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
			returnMap.put("viewHistoryCall", "2");
			
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
			jacksonUtil.json2Map(strParams);
			// 获取来电人员的OPRID
			if ("GETUSER".equals(oprType)) {
				String strPhone = jacksonUtil.getString("phone");
				String strType = jacksonUtil.getString("type");
				String strCallXh = jacksonUtil.getString("callXh");
				String sql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE=? limit 0,1";
				strOprid = sqlQuery.queryForObject(sql, new Object[]{strPhone}, "String");
				if(strOprid==null||"".equals(strOprid)){
					sql = "SELECT TZ_LYDX_ID FROM PS_TZ_LXFSINFO_TBL WHERE TZ_ZY_SJ=? limit 0,1";
					strOprid = sqlQuery.queryForObject(sql, new Object[]{strPhone}, "String");
					if(strOprid==null){
						strOprid = "";
					}
				}
				returnMap.put("OPRID", strOprid);
				//历史来电数量
				sql = "SELECT COUNT(1) FROM PS_TZ_PH_JDD_TBL WHERE TZ_PHONE=? AND TZ_XH<>?";
				String callCount = sqlQuery.queryForObject(sql, new Object[]{strPhone,strCallXh}, "String");
				returnMap.put("viewHistoryCall", callCount);
				
				return jacksonUtil.Map2json(returnMap);
			}
			if("SEARCHUSER".equals(oprType)){
				String strPhone = jacksonUtil.getString("phone");
				String strEmail = jacksonUtil.getString("email");
				String strName = jacksonUtil.getString("name");
				
				String sql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE 1=1";
				if(strPhone!=null&&!"".equals(strPhone)){
					sql = sql + " AND TZ_MOBILE LIKE '%" + strPhone + "%'";
				}
				
				if(strEmail!=null&&!"".equals(strEmail)){
					sql = sql + " AND TZ_EMAIL LIKE '%" + strEmail + "%'";
				}
				
				if(strName!=null&&!"".equals(strName)){
					sql = sql + " AND TZ_REALNAME LIKE '%" + strName + "%'";
				}
				sql = sql + " limit 0,1";
				
				strOprid = sqlQuery.queryForObject(sql, new Object[]{}, "String");
				returnMap.put("OPRID", strOprid);			
				
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
					String strUpdateSQL = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_JIHUO_ZT = 'Y' WHERE OPRID=?";
					String password = "123456";
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
				String strCallXh = jacksonUtil.getString("callXh");
				String strCallDesc = jacksonUtil.getString("callDesc");
				String strDealwithZT = jacksonUtil.getString("dealwithZT");
				if(strCallXh!=null&&!"".equals(strCallXh)){
					String strUpdateSQL = "UPDATE PS_TZ_PH_JDD_TBL SET TZ_DEALWITH_ZT=?,TZ_DESCR=? WHERE TZ_XH=?";
					int success = sqlQuery.update(strUpdateSQL, new Object[]{strDealwithZT,strCallDesc,strCallXh});
					if(success > 0){
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
}
