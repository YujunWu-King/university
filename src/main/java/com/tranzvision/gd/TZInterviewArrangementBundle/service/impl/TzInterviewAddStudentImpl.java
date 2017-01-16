package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMspsKshTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 添加面试考生
 * @author zhang lang 
 * 原PS：TZ_GD_MS_ARR_PKG:TZ_GD_MS_ADDSTU_CLS
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewAddStudentImpl")
public class TzInterviewAddStudentImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzMspsKshTblMapper psTzMspsKshTblMapper;
	
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");

			if(null != classID && !"".equals(classID)){
				//查询面试安排总数
				String sql = "SELECT COUNT(*) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[]{classID}, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=?";
					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[]{classID});

					for(Map<String,Object> mapData : listData){
						Map<String,Object> mapJson = new HashMap<String,Object>();
						//报名表实例ID
						String appIns = String.valueOf(mapData.get("TZ_APP_INS_ID"));
						
						//查询姓名
						sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND TZ_APP_INS_ID=? limit 1)";
						String name = jdbcTemplate.queryForObject(sql, new Object[]{classID, appIns}, "String");
						
						//考生类型
						sql = "SELECT TZ_COLOR_SORT_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND TZ_APP_INS_ID=? ORDER BY CONVERT(TZ_APP_INS_ID,SIGNED) DESC";
						String sort = jdbcTemplate.queryForObject(sql, new Object[]{classID, appIns}, "String");
						
						//材料评审批次、面试资格
						sql = "SELECT B.TZ_BATCH_NAME,(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND TZ_EFF_STATUS ='A' AND TZ_ZHZ_ID=A.TZ_MSHI_ZGFLG) TZ_MSHI_ZGFLG FROM PS_TZ_CLPS_KSH_TBL A,PS_TZ_CLS_BATCH_T B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_BATCH_ID AND A.TZ_CLASS_ID=? AND A.TZ_APP_INS_ID=? ORDER BY CONVERT(A.TZ_APPLY_PC_ID,SIGNED)  DESC";
						Map<String,Object> msgzMap = jdbcTemplate.queryForMap(sql, new Object[]{classID, appIns});
						
						String msClpsPc = "";
						String msZgFlag = "";
						if(msgzMap != null){
							msClpsPc = msgzMap.get("TZ_BATCH_NAME") == null? "" : String.valueOf(msgzMap.get("TZ_BATCH_NAME"));
							msZgFlag = msgzMap.get("TZ_MSHI_ZGFLG") == null? "" : String.valueOf(msgzMap.get("TZ_MSHI_ZGFLG"));
						}
						
						if("".equals(msZgFlag)){
							sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND TZ_EFF_STATUS ='A' AND TZ_ZHZ_ID='W'";
							msZgFlag = jdbcTemplate.queryForObject(sql, new Object[]{}, "String");
						}
						
						//标签
						String strLabel = "";
						sql = "SELECT TZ_LABEL_NAME FROM PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B WHERE A.TZ_LABEL_ID=B.TZ_LABEL_ID AND TZ_APP_INS_ID=?";
						List<Map<String, Object>> labelList = jdbcTemplate.queryForList(sql, new Object[]{appIns});
						for(Map<String, Object> mapLabel: labelList){
							String label = String.valueOf(mapLabel.get("TZ_LABEL_NAME"));
							if(!"".equals(label) && label != null){
								strLabel = strLabel == ""? label : strLabel+ "； " +label;
							}
						}
						
						mapJson.put("classID", classID);
						mapJson.put("appId", appIns);
						mapJson.put("stuName", name);
						mapJson.put("msCLPSPC", msClpsPc);
						mapJson.put("msZGFlag", msZgFlag);
						mapJson.put("lxEmail", "");//联系邮箱，取值待定
						mapJson.put("sort", sort);//考生类型
						mapJson.put("label", strLabel);
						mapJson.put("isConfTimezone", "");
						mapJson.put("university", "");
						
						listJson.add(mapJson);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
			case "addInterviewStuList":
				//保存批次面试预约安排 
				strRet = this.addInterviewStuList(strParams,errorMsg);
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
	private String addInterviewStuList(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			
			List<Map<String,Object>> addStuData = (List<Map<String, Object>>) jacksonUtil.getList("add");
			for(Map<String,Object> stuDateMap: addStuData){
				Long appId = Long.valueOf(String.valueOf(stuDateMap.get("appId")));
				
				PsTzMspsKshTblKey psTzMspsKshTblKey = new PsTzMspsKshTblKey();
				psTzMspsKshTblKey.setTzClassId(classID);
				psTzMspsKshTblKey.setTzApplyPcId(batchID);
				psTzMspsKshTblKey.setTzAppInsId(appId);
				
				PsTzMspsKshTbl psTzMspsKshTbl = psTzMspsKshTblMapper.selectByPrimaryKey(psTzMspsKshTblKey);
				if(psTzMspsKshTbl == null){
					psTzMspsKshTbl = new PsTzMspsKshTbl();
					psTzMspsKshTbl.setTzClassId(classID);
					psTzMspsKshTbl.setTzApplyPcId(batchID);
					psTzMspsKshTbl.setTzAppInsId(appId);
					
					psTzMspsKshTblMapper.insert(psTzMspsKshTbl);
				}
			}
			rtnMap.replace("success", "success");
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
			rtnMap.replace("success", "fail");
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
}
