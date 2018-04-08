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
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMspsKshTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 添加面试考生
 * @author zhang lang 
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewAddStudentImpl")
public class TzInterviewAddStudentImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzMspsKshTblMapper psTzMspsKshTblMapper;
	
	
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
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};
			
			jacksonUtil.json2Map(strParams);

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_CLASS_ID", "TZ_APP_INS_ID", "OPRID", "TZ_MSH_ID", "TZ_REALNAME", "TZ_LEN_PROID", "TZ_COMPANY_NAME","TZ_ZY_SJ","TZ_ZY_EMAIL"};
			
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
					mapList.put("area", rowList[5]);
					mapList.put("componey", rowList[6]);
					mapList.put("mobile", rowList[7]);
					mapList.put("email", rowList[8]);
					/*
					//面试资格
					String sql = "SELECT B.TZ_ZHZ_DMS FROM PS_TZ_CLPS_KSH_TBL A,PS_TZ_PT_ZHZXX_TBL B,PS_TZ_CLS_BATCH_T C WHERE A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_BATCH_ID AND A.TZ_MSHI_ZGFLG=B.TZ_ZHZ_ID AND B.TZ_EFF_STATUS ='A' AND B.TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND A.TZ_CLASS_ID=? AND A.TZ_APP_INS_ID=? ORDER BY CONVERT(A.TZ_APPLY_PC_ID,SIGNED) DESC";
					String msZgFlag = sqlQuery.queryForObject(sql, new Object[]{rowList[0], rowList[1]}, "String");
					if("".equals(msZgFlag) || msZgFlag==null){
						sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND TZ_EFF_STATUS ='A' AND TZ_ZHZ_ID='W'";
						msZgFlag = sqlQuery.queryForObject(sql, "String");
					}
					
					//标签
					String strLabel = "";
					sql = "SELECT TZ_LABEL_NAME FROM PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B WHERE A.TZ_LABEL_ID=B.TZ_LABEL_ID AND TZ_APP_INS_ID=?";
					List<Map<String, Object>> labelList = sqlQuery.queryForList(sql, new Object[]{rowList[1]});
					for(Map<String, Object> mapLabel: labelList){
						String label = String.valueOf(mapLabel.get("TZ_LABEL_NAME"));
						if(!"".equals(label) && label != null){
							strLabel = strLabel == ""? label : strLabel+ "； " +label;
						}
					}
					
					mapList.put("msZGFlag", msZgFlag);
					mapList.put("label", strLabel);
					*/
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
						PsTzMspsKshTblKey psTzMspsKshTblKey = new PsTzMspsKshTblKey();
						psTzMspsKshTblKey.setTzClassId(classID);
						psTzMspsKshTblKey.setTzApplyPcId(batchID);
						psTzMspsKshTblKey.setTzAppInsId(appInsId);
						
						PsTzMspsKshTbl psTzMspsKshTbl = psTzMspsKshTblMapper.selectByPrimaryKey(psTzMspsKshTblKey);
						if(psTzMspsKshTbl==null){
							psTzMspsKshTbl = new PsTzMspsKshTbl();
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
