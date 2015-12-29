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
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

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
	private JacksonUtil jacksonUtil;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
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

			//显示的注册项;
			int showFieldNum = 0;
			String showFieldNumSQL = "SELECT COUNT(1) from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' and TZ_REG_FIELD_ID not in ('TZ_PASSWORD','TZ_REPASSWORD') order by TZ_ORDER asc limit 0,10";
			showFieldNum = jdbcTemplate.queryForObject(showFieldNumSQL, new Object[]{strJgid}, "Integer");
			
			// json数据要的结果字段;
			String[] resultFldArray = new String[showFieldNum + 1];
			resultFldArray[0] = "OPRID";			
			if(showFieldNum != 0){
				String showSQL = "SELECT TZ_REG_FIELD_ID from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' and TZ_REG_FIELD_ID not in ('TZ_PASSWORD','TZ_REPASSWORD') order by TZ_ORDER asc limit 0,10";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(showSQL,new Object[]{strJgid});
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
			String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_ZHZ_DMS", "TZ_EMAIL", "TZ_MOBILE", "TZ_JIHUO_ZT_DESC", "TZ_ZHCE_DT", "ACCTLOCK"};
			
			String admin = "\"TZ_JG_ID-operator\":\"01\",\"TZ_JG_ID-value\":\"ADMIN\",";
			strParams.replaceAll(admin, "");
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
	
	//重置密码等操作;
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("success", "");
		
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
							int count = jdbcTemplate.queryForObject(sql, new Object[]{OPRID},"Integer" );
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
							password = DESUtil.encrypt(password,"TZGD_Tranzvision");
							
							Psoprdefn psoprdefn = new Psoprdefn();
						    psoprdefn.setOprid(OPRID);
						    psoprdefn.setOperpswd(password);
						    
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
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
