package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.util.ArrayList;
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
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 申请用户管理；原：TZ_GD_USERGL_PKG:TZ_GD_USERGL_CLS
 * 
 * @author tang
 * @since 2015-11-20
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerAccountMgServiceImpl")
@SuppressWarnings("unchecked")
public class LeaguerAccountMgServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzRegUserTMapper PsTzRegUserTMapper;
	@Autowired
	private TZGDObject tzGdObject;
	
	//@Override
	public String tzQueryList11(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			//获取当前机构;
			String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			Integer userCount = 0;
			String strUserCountSql = "SELECT COUNT(*) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=?";
			userCount = SqlQuery.queryForObject(strUserCountSql, new Object[]{strJgid}, "Integer");
			
			//显示的注册项;
			int showFieldNum = 0;
			String showFieldNumSQL = "SELECT COUNT(1) from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' and TZ_REG_FIELD_ID not in ('TZ_PASSWORD','TZ_REPASSWORD') order by TZ_ORDER asc limit 0,10";
			showFieldNum = SqlQuery.queryForObject(showFieldNumSQL, new Object[]{strJgid}, "Integer");
			
			// json数据要的结果字段;
			String[] resultFldArray = new String[showFieldNum + 1];
			resultFldArray[0] = "OPRID";			
			if(showFieldNum != 0){
				String showSQL = "SELECT TZ_REG_FIELD_ID from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' and TZ_REG_FIELD_ID not in ('TZ_PASSWORD','TZ_REPASSWORD') order by TZ_ORDER asc limit 0,10";
				List<Map<String, Object>> list = SqlQuery.queryForList(showSQL,new Object[]{strJgid});
				for(int j = 0; j<list.size(); j++){
					String regFieldId = (String) list.get(j).get("TZ_REG_FIELD_ID");
					if("TZ_GENDER".equals(regFieldId)){
						regFieldId = "TZ_ZHZ_DMS";
					}
					resultFldArray[j+1] = regFieldId;
				}
			}
			
			String[][] orderByArr = null;
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("OPRID", rowList[0]);
					
					int rowListLen = rowList.length;
					for(int k = 0 ; k < 10; k++){
						if(k < rowListLen-1){
							mapList.put("fieldStr_" + (k + 1) , rowList[ k + 1]);
						}else{
							mapList.put("fieldStr_" + (k + 1) , "");
						}
					}
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
			/*String strTransSql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'";
			
			//姓名、证件号、面试申请号、性别、手机、邮箱、报考批次（取值如下图，显示完整的班级批次名称，多个批次需要合并显示）、激活状态、创建时间、账号锁定状态、黑名单
			String strRegUserList = "SELECT A.OPRID,A.TZ_REALNAME,A.TZ_GENDER,A.NATIONAL_ID,(SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='TZ_GENDER' AND A.TZ_ZHZ_ID=A.TZ_GENDER AND TZ_EFF_STATUS='A' limit 0,1) TZ_GENDERDESC,B.TZ_ZY_SJ,B.TZ_ZY_EMAIL,C.TZ_JIHUO_ZT,(SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='TZ_JIHUO_ZT' AND A.TZ_ZHZ_ID=C.TZ_JIHUO_ZT AND TZ_EFF_STATUS='A' limit 0,1) TZ_JIHUODESC,date_format(C.TZ_ZHCE_DT,'%Y-%m-%d %H:%i') TZ_ZHCE_DT,A.TZ_BLACK_NAME,(SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='TZ_BLACK_NAME' AND A.TZ_ZHZ_ID=A.TZ_BLACK_NAME AND TZ_EFF_STATUS='A' limit 0,1) TZ_BLACKUSER,C.TZ_MSH_ID FROM PS_TZ_REG_USER_T A LEFT JOIN PS_TZ_LXFSINFO_TBL B ON A.OPRID=B.TZ_LYDX_ID JOIN PS_TZ_AQ_YHXX_TBL C ON A.OPRID=C.TZ_DLZH_ID WHERE B.TZ_LXFS_LY='ZCYH' AND TZ_JG_ID=? limit ?,?";
			List<Map<String, Object>> userMap = SqlQuery.queryForList(strRegUserList, new Object[] { strJgid, numStart,numLimit});
			if(userMap!=null&&userMap.size()>0){
			    for(Object userMapObj:userMap){
				Map<String,Object> result1=(Map<String,Object>) userMapObj;
				String strOprid = result1.get("OPRID")==null ? "" : String.valueOf(result1.get("OPRID"));
				String strName = result1.get("TZ_REALNAME")==null ? "" : String.valueOf(result1.get("TZ_REALNAME"));
				String strGender = result1.get("TZ_GENDER")==null ? "" : String.valueOf(result1.get("TZ_GENDER"));				
				String strGenderDesc = result1.get("TZ_GENDERDESC")==null ? "" : String.valueOf(result1.get("TZ_GENDERDESC"));
				String strNationId = result1.get("NATIONAL_ID")==null ? "" : String.valueOf(result1.get("NATIONAL_ID"));
				String strPhone = result1.get("TZ_ZY_SJ")==null ? "" : String.valueOf(result1.get("TZ_ZY_SJ"));
				String strEmail = result1.get("TZ_ZY_EMAIL")==null ? "" : String.valueOf(result1.get("TZ_ZY_EMAIL"));
				String strActive = result1.get("TZ_JIHUO_ZT")==null ? "" : String.valueOf(result1.get("TZ_JIHUO_ZT"));				
				String strActiveDesc = result1.get("TZ_JIHUODESC")==null ? "" : String.valueOf(result1.get("TZ_JIHUODESC"));
				String strAddDateTime = result1.get("TZ_ZHCE_DT")==null ? "" : String.valueOf(result1.get("TZ_ZHCE_DT"));
				String strBlackUserFlg = result1.get("TZ_BLACK_NAME")==null ? "" : String.valueOf(result1.get("TZ_BLACK_NAME"));
				String strBlackUserFlgDesc = result1.get("TZ_BLACKUSER")==null ? "" : String.valueOf(result1.get("TZ_BLACKUSER"));
				String strMshID = result1.get("ROW_ADDED_DTTM")==null ? "" : String.valueOf(result1.get("TZ_MSH_ID"));
				String strLockSql = "SELECT (SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='ACCTLOCK' AND A.TZ_ZHZ_ID=P.ACCTLOCK AND TZ_EFF_STATUS='A' limit 0,1) TZ_ZHZ_DMS FROM PSOPRDEFN P WHERE OPRID=?";
				String strLockFlg = SqlQuery.queryForObject(strLockSql, new Object[] {strOprid}, "String");
				strNationId = "130827198811111111";
				strMshID = "2017XXXXX";
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("OPRID", strOprid);
				mapList.put("userName", strName);
				mapList.put("userSex", strGenderDesc);
				mapList.put("nationId", strNationId);
				mapList.put("userEmail", strEmail);
				mapList.put("userPhone", strPhone);
				mapList.put("jihuoZt", strActiveDesc);
				mapList.put("zcTime", strAddDateTime);
				mapList.put("acctlock", strLockFlg);
				mapList.put("hmdUser", strBlackUserFlgDesc);	
				mapList.put("mshId", strMshID);
				mapList.put("applyInfo", "2017年国际MBA招生第一批次;2017年国际MBA招生第=批次");
				
				//"TZ_REALNAME", "TZ_ZHZ_DMS", "TZ_EMAIL", "TZ_MOBILE", "TZ_JIHUO_ZT_DESC", "TZ_ZHCE_DT", "ACCTLOCK", "TZ_BLACK_NAME"};
				
				listData.add(mapList);
			    }
			}
			mapRet.replace("total", userCount);
			mapRet.replace("root", listData);*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};
			
			// json数据要的结果字段;
			String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_GENDER", "TZ_EMAIL", "TZ_MOBILE", "TZ_JIHUO_ZT", "TZ_ZHCE_DT", "ACCTLOCK", "TZ_BLACK_NAME","NATIONAL_ID","TZ_MSH_ID","TZ_CLASS_NAME"};
			
			//String admin = "\"TZ_JG_ID-operator\":\"01\",\"TZ_JG_ID-value\":\"ADMIN\",";
			//strParams.replaceAll(admin, "");
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);
			
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("OPRID", rowList[0]);
					mapList.put("userName", rowList[1]);
					mapList.put("userSex", rowList[2]);
					mapList.put("userEmail", rowList[3]);
					mapList.put("userPhone", rowList[4]);
					mapList.put("jihuoZt", rowList[5]);
					mapList.put("zcTime", rowList[6]);
					mapList.put("acctlock", rowList[7]);
					mapList.put("hmdUser", rowList[8]);
					mapList.put("nationId", rowList[9]);
					mapList.put("mshId", rowList[10]);
					mapList.put("applyInfo", rowList[11]);
					listData.add(mapList);
				}
				
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	/* 关闭账号*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("OPRID")){
					// 用户账号;
				    String strOprId = jacksonUtil.getString("OPRID");

				    Psoprdefn psoprdefn = new Psoprdefn();
				    psoprdefn.setOprid(strOprId);
				    psoprdefn.setAcctlock(Short.valueOf("1"));
				    int i = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
					if (i > 0) {
					} else {
						errMsg[0] = "1";
						errMsg[1] = "信息保存失败";
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/* 加入黑名单*/
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("OPRID")){
					// 用户账号;
				    String strOprId = jacksonUtil.getString("OPRID");
				    //TZ_REG_USER_T
				    PsTzRegUserT psTzRegUserT = new PsTzRegUserT();	
				    psTzRegUserT.setOprid(strOprId);
				    psTzRegUserT.setTzBlackName("Y");				    
				    int i = PsTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
					if (i > 0) {
					} else {
						errMsg[0] = "1";
						errMsg[1] = "信息保存失败";
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	//重置密码等操作;
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("success", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			//重置密码检查;
			if("CHGPWD".equals(oprType)){
				jacksonUtil.json2Map(strParams);
				if(jacksonUtil.containsKey("data")){
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("data");
					if(list != null){
						for(int i = 0 ; i< list.size(); i++){
							String OPRID = (String) list.get(i).get("OPRID");
							String sql = "select count(1) from PS_TZ_AQ_YHXX_TBL a, PSOPRDEFN b where a.OPRID = b.OPRID and b.OPRID=? and (a.TZ_JIHUO_ZT<>'Y' or b.ACCTLOCK=1)";
							int count = SqlQuery.queryForObject(sql, new Object[]{OPRID},"Integer" );
							if(count > 0){
								errorMsg[0] = "1";
								errorMsg[1] = "不能修改未激活或锁定的账号的密码";
								returnJsonMap.replace("success", "false");
							}else{
								returnJsonMap.replace("success", "true");
							}
						}
					}
				}
			}
			
			//修改密码;
			if("PWD".equals(oprType)){
				jacksonUtil.json2Map(strParams);
				if(jacksonUtil.containsKey("password") && jacksonUtil.containsKey("data")){
					String password = jacksonUtil.getString("password");
					if(password == null || "".equals(password.trim())){
						errorMsg[0] = "1";
						errorMsg[1] = "密码不能为空";
					}
					
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("data");
					if(list != null && !"1".equals(errorMsg[0])){
						for(int i = 0 ; i< list.size(); i++){
							String OPRID = (String) list.get(i).get("OPRID");
							String tmpPassword = DESUtil.encrypt(password,"TZGD_Tranzvision");
							
							Psoprdefn psoprdefn = new Psoprdefn();
						    psoprdefn.setOprid(OPRID);
						    psoprdefn.setOperpswd(tmpPassword);
						    
						    int success = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
							if (success > 0) {
								returnJsonMap.replace("success", "true");
							} else {
								errorMsg[0] = "1";
								errorMsg[1] = "修改用户密码失败。";
								returnJsonMap.replace("success", "false");
							}
						}
					}
				}
			}
			//获取搜索条件中下拉框
			if("GETVALUE".equals(oprType)){
			    returnJsonMap.replace("success", "true");
			    returnJsonMap.put("dataStore", this.getSearchTranslateValue());			    
			}
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	//搜索条件中的下拉框值
	public String getSearchTranslateValue(){
	    String strRet = "{}";
	    Map<String, Object> returnJsonMap = new HashMap<String, Object>();
	    returnJsonMap.put("success", "success");
	    JacksonUtil jacksonUtil = new JacksonUtil();
	    
	    try{
        	    String strTranslateSql = "SELECT TZ_ZHZ_ID,TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_EFF_STATUS='A'";
        	    //申请的专业
        	    String strReturn1Json = "";
        	    List<Map<String, Object>> mapList1 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_MBAX_ZHUANY"});
        	    if(mapList1!=null&&mapList1.size()>0){
        		for(Object userMapObj:mapList1){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn1Json)){
        			strReturn1Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);			
        		    }else{
        			strReturn1Json = strReturn1Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }
        		    
        		}
        	    }
        	    returnJsonMap.put("applyMajorStore","[" + strReturn1Json + "]");
        	    //批次-TZ_APPLY_PCH
        	    String strReturn2Json = "";
        	    List<Map<String, Object>> mapList2 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_APPLY_PCH"});
        	    if(mapList2!=null&&mapList2.size()>0){
        		for(Object userMapObj:mapList2){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn2Json)){
        			strReturn2Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn2Json = strReturn2Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }
        		    
        		}
        	    }
        	    returnJsonMap.put("batchStore","[" +  strReturn2Json + "]");
        	    //志愿-TZ_APPLY_ZY_ID
        	    String strReturn3Json = "";
        	    List<Map<String, Object>> mapList3 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_APPLY_ZY_ID"});
        	    if(mapList3!=null&&mapList3.size()>0){
        		for(Object userMapObj:mapList3){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn3Json)){
        			strReturn3Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn3Json = strReturn3Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }
        		    
        		}
        	    }
        	    returnJsonMap.put("zhiyStore", "[" + strReturn3Json + "]");
        	    //报名表状态-TZ_APP_FORM_STA
        	    String strReturn4Json = "";
        	    List<Map<String, Object>> mapList4 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_APPFORM_STATE"});
        	    if(mapList4!=null&&mapList4.size()>0){
        		for(Object userMapObj:mapList4){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn4Json)){
        			strReturn4Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn4Json = strReturn4Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("appStatusStore", "[" + strReturn4Json + "]");
        	    //条件录取资格面试结果 - TZ_TJLQZG
        	    String strReturn5Json = "";
        	    List<Map<String, Object>> mapList5 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_TJLQZG"});
        	    if(mapList5!=null&&mapList5.size()>0){
        		for(Object userMapObj:mapList5){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn5Json)){
        			strReturn5Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn5Json = strReturn5Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("luStore", "[" + strReturn5Json + "]");
        	    //条件录取资格项目 - TZ_TJLQZG_XM
        	    String strReturn6Json = "";
        	    List<Map<String, Object>> mapList6 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_TJLQZG_XM"});
        	    if(mapList6!=null&&mapList6.size()>0){
        		for(Object userMapObj:mapList6){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn6Json)){
        			strReturn6Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn6Json = strReturn6Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("lu2Store", "[" + strReturn6Json + "]");
        	    //获得结果批次-与报考批次一样
        	    String strReturn7Json = "";
        	    /*List<Map<String, Object>> mapList7 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_MSPS_JGPC"});
        	    if(mapList7!=null&&mapList7.size()>0){
        		for(Object userMapObj:mapList7){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn7Json)){
        			strReturn7Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn7Json = strReturn7Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("getResultStore", "[" + strReturn7Json + "]");
        	    //面试批次-与报考批次一样
        	    String strReturn8Json = "";
        	    /*List<Map<String, Object>> mapList8 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_MSPS_PC"});
        	    if(mapList8!=null&&mapList8.size()>0){
        		for(Object userMapObj:mapList8){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn8Json)){
        			strReturn8Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn8Json = strReturn8Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("getResultStore", "[" + strReturn8Json + "]");
        	    //最初奖学金授予情况/最终奖学金授予情况-TZ_MBA_SCHOLARSHIP
        	    String strReturn9Json = "";
        	    List<Map<String, Object>> mapList9 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_MBA_SCHOLARSHIP"});
        	    if(mapList9!=null&&mapList9.size()>0){
        		for(Object userMapObj:mapList9){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn9Json)){
        			strReturn9Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn9Json = strReturn9Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("zcJxjStore", "[" + strReturn9Json + "]");
        	    //联考前条件录取资格-与条件录取资格一样
        	    String strReturn10Json = "";
        	    /*List<Map<String, Object>> mapList10 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_LKQTJLQZGZT"});
        	    if(mapList10!=null&&mapList10.size()>0){
        		for(Object userMapObj:mapList10){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn10Json)){
        			strReturn10Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn10Json = strReturn10Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("lkqTjlqStore", "[" + strReturn10Json + "]");
        	    //联考前条件录取项目-与条件录取项目一样
        	    String strReturn11Json = "";
        	    /*List<Map<String, Object>> mapList11 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_LKQTJLQZGZT_XM"});
        	    if(mapList11!=null&&mapList11.size()>0){
        		for(Object userMapObj:mapList11){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn11Json)){
        			strReturn11Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn11Json = strReturn11Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("lkqTjlqXmStore", "[" + strReturn11Json + "]");
        	    //预录取资格-与条件录取资格一样
        	    String strReturn12Json = "";
        	    /*List<Map<String, Object>> mapList12 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_YLQZGZT"});
        	    if(mapList12!=null&&mapList12.size()>0){
        		for(Object userMapObj:mapList12){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn12Json)){
        			strReturn12Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn12Json = strReturn12Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("ylqZgStore", "[" + strReturn12Json + "]");
        	    //预录取资格项目-与条件录取项目一样
        	    String strReturn13Json = "";
        	    /*List<Map<String, Object>> mapList13 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_YLQZGZT_XM"});
        	    if(mapList13!=null&&mapList13.size()>0){
        		for(Object userMapObj:mapList13){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn13Json)){
        			strReturn13Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn13Json = strReturn13Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("ylqZgXmStore", "[" + strReturn13Json + "]");
        	    //正式录取资格
        	    String strReturn14Json = "";
        	    List<Map<String, Object>> mapList14 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_ZSLQZGZT"});
        	    if(mapList14!=null&&mapList14.size()>0){
        		for(Object userMapObj:mapList14){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn14Json)){
        			strReturn14Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn14Json = strReturn14Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("zslqZgStore", "[" + strReturn14Json + "]");
        	    //正式录取资格项目-与条件录取项目一样
        	    String strReturn15Json = "";
        	    /*List<Map<String, Object>> mapList15 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_ZSLQZGZT_XM"});
        	    if(mapList15!=null&&mapList15.size()>0){
        		for(Object userMapObj:mapList15){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn15Json)){
        			strReturn15Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn15Json = strReturn15Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("zslqZgXmStore", "[" + strReturn15Json + "]");
        	    //入学情况
        	    String strReturn16Json = "";
        	    List<Map<String, Object>> mapList16 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_RXQK"});
        	    if(mapList16!=null&&mapList16.size()>0){
        		for(Object userMapObj:mapList16){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn16Json)){
        			strReturn16Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn16Json = strReturn16Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("ruXueQkStore", "[" + strReturn16Json + "]");
        	    //入学项目-与条件录取项目一样
        	    String strReturn17Json = "";
        	    /*List<Map<String, Object>> mapList17 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_RXQK_XM"});
        	    if(mapList17!=null&&mapList17.size()>0){
        		for(Object userMapObj:mapList17){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn17Json)){
        			strReturn17Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn17Json = strReturn17Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }*/
        	    returnJsonMap.put("ruXueXmStore", "[" + strReturn17Json + "]");
        	    //工作所在省市
        	    String strReturn18Json = "";
        	    String strProvinceSql = "SELECT STATE,DESCR FROM PS_STATE_TBL WHERE COUNTRY='CHN'";
        	    List<Map<String, Object>> mapList18 = SqlQuery.queryForList(strProvinceSql, new Object[] {});
        	    if(mapList18!=null&&mapList18.size()>0){
        		for(Object userMapObj:mapList18){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("STATE")==null ? "" : String.valueOf(result1.get("STATE"));
        		    String TLDesc = result1.get("DESCR")==null ? "" : String.valueOf(result1.get("DESCR"));
        		    if("".equals(strReturn18Json)){
        			strReturn18Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn18Json = strReturn18Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("workProvinceStore", "[" + strReturn18Json + "]");
        	    //行业类别
        	    String strReturn19Json = "";
        	    List<Map<String, Object>> mapList19 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_MBA_ZS_HYLB"});
        	    if(mapList19!=null&&mapList19.size()>0){
        		for(Object userMapObj:mapList19){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn19Json)){
        			strReturn19Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn19Json = strReturn19Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("industryTypeStore", "[" + strReturn19Json + "]");
        	    //工作职能
        	    String strReturn20Json = "";
        	    List<Map<String, Object>> mapList20 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_MBA_ZS_GWZN"});
        	    if(mapList20!=null&&mapList20.size()>0){
        		for(Object userMapObj:mapList20){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn20Json)){
        			strReturn20Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn20Json = strReturn20Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("workZhNStore", "[" + strReturn20Json + "]");
        	    //职位类型-TZ_WORK_TYPE
        	    String strReturn21Json = "";
        	    List<Map<String, Object>> mapList21 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_MBA_ZW_LX"});
        	    if(mapList21!=null&&mapList21.size()>0){
        		for(Object userMapObj:mapList21){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn21Json)){
        			strReturn21Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn21Json = strReturn21Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("PositionTypeStore", "[" + strReturn21Json + "]");
        	    //是否
        	    String strReturn22Json = "";
        	    List<Map<String, Object>> mapList22 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_SF_SALE"});
        	    if(mapList22!=null&&mapList22.size()>0){
        		for(Object userMapObj:mapList22){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn22Json)){
        			strReturn22Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn22Json = strReturn22Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("isYN", "[" + strReturn22Json + "]");
        	    //政治面貌
        	    String strReturn23Json = "";
        	    List<Map<String, Object>> mapList23 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_POLITICAL"});
        	    if(mapList23!=null&&mapList23.size()>0){
        		for(Object userMapObj:mapList23){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn23Json)){
        			strReturn23Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn23Json = strReturn23Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("ZhZhMMStore", "[" + strReturn23Json + "]");
        	    //是否有效
        	    String strReturn24Json = "";
        	    List<Map<String, Object>> mapList24 = SqlQuery.queryForList(strTranslateSql, new Object[] {"TZ_ISVALID"});
        	    if(mapList24!=null&&mapList24.size()>0){
        		for(Object userMapObj:mapList24){
        		    Map<String,Object> result1=(Map<String,Object>) userMapObj;
        		    String TValue = result1.get("TZ_ZHZ_ID")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
        		    String TLDesc = result1.get("TZ_ZHZ_DMS")==null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
        		    if("".equals(strReturn24Json)){
        			strReturn24Json =  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }else{
        			strReturn24Json = strReturn24Json + "," +  tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc);
        		    }		    
        		}
        	    }
        	    returnJsonMap.put("isValStore", "[" + strReturn24Json + "]");
        	    
        	    strRet = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ALL_STORE",strReturn1Json,strReturn2Json,strReturn3Json,strReturn4Json,strReturn5Json,strReturn6Json,strReturn7Json,strReturn8Json,strReturn9Json,strReturn10Json,strReturn11Json,strReturn12Json,strReturn13Json,strReturn14Json,strReturn15Json,strReturn16Json,strReturn17Json,strReturn18Json,strReturn19Json,strReturn20Json,strReturn21Json,strReturn22Json,strReturn23Json,strReturn24Json);
        	    
	    }catch(Exception e){
		System.out.println(e.toString());
	    }
	    
	    return strRet;
	}
}
