package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPskshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 添加面试考生
 * @author zhang lang 
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewAddStudentImpl")
public class TzInterviewAddStudentImpl extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private PsTzMsPskshTblMapper psTzMspsKshTblMapper;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};
			
			jacksonUtil.json2Map(strParams);
			Map<String,Object> paramsMap = jacksonUtil.getMap();
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_CLASS_ID", "TZ_APP_INS_ID", "OPRID", "TZ_MSH_ID", "TZ_REALNAME", "TZ_ZY_SJ","TZ_ZY_EMAIL","TZ_CLASS_NAME","TZ_BATCH_NAME","TZ_SFCJ_MSZC", "TZ_LEN_PROID"};
			
			try{
				//如果是江苏面试管理员，只能搜索到常住省份为江苏的考生，多个角色用英文逗号分隔
				String jsRoleName = getHardCodePoint.getHardCodePointVal("TZ_JIANGSU_MSADM_ROLE");
				if(jsRoleName != null 
						&& !"".equals(jsRoleName)){
					String [] roleNameArr = jsRoleName.split(",");
					
					String sql = "select 'Y' from PSROLEUSER where ROLEUSER=? and ROLENAME=?"; 
					for(String roleName: roleNameArr){
						String isJsAdm = sqlQuery.queryForObject(sql, new Object[]{ oprid, roleName }, "String");
						if("Y".equals(isJsAdm)){
							Map<String,Object> conditionMap = jacksonUtil.getMap("condition");
							conditionMap.put("TZ_LEN_PROID-operator", "01");
							conditionMap.put("TZ_LEN_PROID-value", "江苏");
							
							paramsMap.replace("condition", conditionMap);
							strParams = jacksonUtil.Map2json(paramsMap);
							break;
						}
					}
				}
			}catch (NullPointerException nEx) {
				nEx.printStackTrace();
			}
			
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classID", rowList[0]);
					mapList.put("appId", rowList[1]);
					mapList.put("oprid", rowList[2]);
					mapList.put("mssqh", rowList[3]);
					mapList.put("stuName", rowList[4]);
					mapList.put("mobile", rowList[5]);
					mapList.put("email", rowList[6]);
					
					mapList.put("className", rowList[7]);
					mapList.put("batchName", rowList[8]);
					mapList.put("msZhuanC", rowList[9]);
					mapList.put("province", rowList[10]);
					
					String sql = "select 'Y' from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
					String inThisBatch = sqlQuery.queryForObject(sql, new Object[]{ classID, batchID, rowList[1] }, "String");
					String addStatus = "未添加";
					if("Y".equals(inThisBatch)){
						addStatus = "已添加";
					}
					mapList.put("addStatus", addStatus);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
			case "tzAddStudents":
				//保存批次面试预约安排 
				strRet = this.tzAddStudents(strParams,errorMsg);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	/*
	 * 添加参与本批次的面试考生
	 */
	@SuppressWarnings({ "unchecked"})
	private String tzAddStudents(String strParams, String[] errorMsg){
		String strRtn = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "success");
		rtnMap.put("message", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			List<Map<String,Object>> stuList = (List<Map<String,Object>>) jacksonUtil.getList("stuList");
			
			if(stuList != null && stuList.size() > 0){
				for(Map<String,Object> stuInfoMap : stuList){
					
					long appInsId = stuInfoMap.get("appId") == null ? 0 : Long.valueOf(stuInfoMap.get("appId").toString());
					
					if(appInsId>0){
						PsTzMsPskshTblKey psTzMspsKshTblKey = new PsTzMsPskshTblKey();
						psTzMspsKshTblKey.setTzClassId(classID);
						psTzMspsKshTblKey.setTzApplyPcId(batchID);
						psTzMspsKshTblKey.setTzAppInsId(appInsId);
						
						PsTzMsPskshTbl psTzMspsKshTbl = psTzMspsKshTblMapper.selectByPrimaryKey(psTzMspsKshTblKey);
						if(psTzMspsKshTbl==null){
							psTzMspsKshTbl = new PsTzMsPskshTbl();
							psTzMspsKshTbl.setTzClassId(classID);
							psTzMspsKshTbl.setTzApplyPcId(batchID);
							psTzMspsKshTbl.setTzAppInsId(appInsId);
							psTzMspsKshTbl.setRowAddedOprid(oprid);
							psTzMspsKshTbl.setRowAddedDttm(new Date());
							psTzMspsKshTbl.setRowLastmantOprid(oprid);
							psTzMspsKshTbl.setRowLastmantDttm(new Date());
							
							psTzMspsKshTblMapper.insert(psTzMspsKshTbl);
						}
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		
		strRtn = jacksonUtil.Map2json(rtnMap);
		return strRtn;
	}
}
