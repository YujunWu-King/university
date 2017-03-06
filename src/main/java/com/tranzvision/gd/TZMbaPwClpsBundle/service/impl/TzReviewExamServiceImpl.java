package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * 材料评审考生名单
 * @author WRL(TZ_GD_CLPS_PKG:TZ_GD_APPS_CLS)
 *
 */
@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewExamServiceImpl")
public class TzReviewExamServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Override
	public String tzQuery(String strParams,String[] errMsg) {
		String strRtn = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			//班级编号
			String classId = jacksonUtil.getString("classID");
			
			//批次编号
			String batchID = jacksonUtil.getString("batchID");
			
			/*评审状态*/
			String sql = "SELECT CASE WHEN TZ_DQPY_ZT = 'A' THEN '进行中' WHEN TZ_DQPY_ZT = 'B' THEN '已结束' ELSE '未开始' END FROM PS_TZ_CLPS_GZ_TBL  WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";
			String strReviewStatus = sqlQuery.queryForObject(sql, new Object[]{classId,batchID}, "String");

			/*报考考生数量*/
			String sqlAppNumber = "SELECT COUNT(*) FROM PS_TZ_FORM_WRK_T WRK,PS_TZ_APP_INS_T INS WHERE WRK.TZ_APP_INS_ID = INS.TZ_APP_INS_ID AND TZ_APP_FORM_STA = 'S' AND WRK.TZ_CLASS_ID = ? AND TZ_BATCH_ID = ?";
			int numApplicantsNumber = sqlQuery.queryForObject(sqlAppNumber, new String[]{classId,batchID}, "int");

			/*材料评审考生数量*/
			String sqlReviewAppNumber = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";
			int numMaterialsReviewApplicantsNumber = sqlQuery.queryForObject(sqlReviewAppNumber, new String[]{classId,batchID}, "int");
			
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("classID", classId);
			mapData.put("batchID", batchID);
			mapData.put("applicantsNumber", numApplicantsNumber);
			mapData.put("materialsReviewApplicantsNumber", numMaterialsReviewApplicantsNumber);
			mapData.put("status", strReviewStatus);

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("formData", mapData);

			strRtn = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRtn;
	}
	
	public String tzOther(String oprType,String comParams, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if("ReStudentLis".equals(oprType)){
				/*获取考生听众*/
				
			}
			if("getStudentColumns".equals(oprType)){
				 /*获取考生动态展示的列*/
				strRet = this.getStudentColumns(comParams, errMsg);
			}
			if("viewMailHistory".equals(oprType)){
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	@Override
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRtn="{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			String classId = jacksonUtil.getString("classId");
			String applyBatchId = jacksonUtil.getString("applyBatchId");
			String queryType = jacksonUtil.getString("queryType");
			

			
		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRtn;
	}
	
	private String getStudentColumns(String comParams, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
		//班级编号
		String classId = jacksonUtil.getString("classID");
		//批次编号
		String batchID = jacksonUtil.getString("batchID");
		
		String sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
		String appModalId = sqlQuery.queryForObject(sql, new String[]{classId},"String");
		
		
		
//		method getStudentColumns
//		   /+ &strParams as String, +/
//		   /+ &errorCode as Number out, +/
//		   /+ &strErrorDesc as String out +/
//		   /+ Returns String +/
//		   
//		   Local string &strRet;
//		   try
//		      rem 解析json类;
//		      Local JavaObject &jsonUtil = CreateJavaObject("com.tranzvision.util.PaseJsonUtil");
//		      Local TZ_GD_BASE_APP:TZUTILITY &trsObj = create TZ_GD_BASE_APP:TZUTILITY();
//		      rem 将字符串转换成json;
//		      Local JavaObject &CLASSJson = &jsonUtil.getJson(&strParams);
//		      
//
//		      Local TZ_GD_KJGL_PKG:TZ_GD_COMM_CLS &commonClass = create TZ_GD_KJGL_PKG:TZ_GD_COMM_CLS();
//		      Local string &TZ_APP_MODAL_ID;
//		      SQLExec("select TZ_APP_MODAL_ID  from ps_TZ_CLASS_INF_T where TZ_CLASS_ID=:1", &strClassID, &TZ_APP_MODAL_ID);
//		      Local string &strExportTplType = &commonClass.GetHardcodePointValue("TZ_CLMS_EXPORT_TPLTYPE");
//		      Local TZ_GD_APPFORM_INFO_PKG:GetApplicationInfo &obj_ks_info = create TZ_GD_APPFORM_INFO_PKG:GetApplicationInfo();
//		      Local string &str_ksInfoTitlejson = &obj_ks_info.getColumns(&strExportTplType, &TZ_APP_MODAL_ID);
//		      rem Local string &str_ksInfoTitlejson = GetHTMLText(HTML.TZ_CLMS_TEST_HTML);
//		      rem  Error "----&strExportTplType is :" | &strExportTplType | "---&TZ_APP_MODAL_ID is :" | &TZ_APP_MODAL_ID | "--" | &str_ksInfoTitlejson;
//		      rem 考生信息自定义取考生信息 20151009start;
//		      
//		      Local JavaObject &fieldJson;
//		      Local string &str_title, &str_field_value;
//		      
//		      Local JavaObject &jsonArray, &jsonInfoArray;
//		      
//		      
//		      Local string &dyColTh = "";
//		      
//		      
//		      If All(&str_ksInfoTitlejson) Then
//		         try
//		            rem 将字符串转换成json;
//		            &CLASSJson = &jsonUtil.getJson(&str_ksInfoTitlejson);
//		            &jsonArray = &CLASSJson.getJSONArray("columns");
//		            
//		            Local number &i;
//		            For &i = 0 To &jsonArray.size() - 1
//		               rem 将数组中内容转成json;
//		               &fieldJson = &jsonUtil.getJson(&jsonArray.getString(&i));
//		               rem 字段名称;
//		               &str_title = &fieldJson.getString("fieldName");
//		               
//		               
//		               If &dyColTh = "" Then
//		                  rem 2016-02-01 宋子成 json 数据转义处理;
//		                  &str_title = &trsObj.Transformchar(&str_title);
//		                  &dyColTh = &str_title;
//		               Else
//		                  rem 2016-02-01 宋子成 json 数据转义处理;
//		                  &str_title = &trsObj.Transformchar(&str_title);
//		                  &dyColTh = &dyColTh | "," | &str_title;
//		               End-If;
//		               
//		            End-For;
//		            
//		         catch Exception &e89
//		         end-try;
//		         
//		         
//		      End-If;
//		      
//		      rem    &dyColTh = "Second Country/Region of Citizenship,school";
//		   catch Exception &e
//		      &errorCode = 1;
//		      &strErrorDesc = &e.ToString();
//		   end-try;
//		   &strRet = GetHTMLText(HTML.TZ_GD_CLPS_STUDENTCOLUMNS_HTML, &dyColTh);
//		   Return &strRet;
//		end-method;
		return null;
	}
}
