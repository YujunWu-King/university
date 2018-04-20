package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 招生线索导出进程
 * @author LuYan 2017-10-13
 *
 */
public class TzClueExportEngineCls extends BaseEngine {

	@SuppressWarnings("unchecked")
	@Override
	public void OnExecute() throws Exception {
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		
		// 运行id;
		String runControlId = this.getRunControlID();
		int processinstance =  this.getProcessInstanceID();
		
		try{
			//导出参数
			Map<String,Object> paramsMap = sqlQuery.queryForMap("select TZ_TYPE,TZ_REL_URL,TZ_JD_URL,TZ_PARAMS_STR from PS_TZ_XSXS_DC_AET where RUN_CNTL_ID=?"
					, new Object[] { runControlId });

			if(paramsMap != null){
				String exportType = paramsMap.get("TZ_TYPE").toString();
				String expDirPath = paramsMap.get("TZ_REL_URL").toString();
				String absexpDirPath = paramsMap.get("TZ_JD_URL").toString();
				String strParams = paramsMap.get("TZ_PARAMS_STR").toString();
				
				
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				
				jacksonUtil.json2Map(strParams);
				
				/******************************1、生成标题行---开始**************************************/
				dataCellKeys.add(new String[] { "name", "姓名" });
				dataCellKeys.add(new String[] { "mobile", "手机" });
				dataCellKeys.add(new String[] { "companyName", "公司" });
				dataCellKeys.add(new String[] { "position", "职位" });
				dataCellKeys.add(new String[] { "localAddress", "常住地" });
				dataCellKeys.add(new String[] { "bkStateDesc", "报考状态" });
				dataCellKeys.add(new String[] { "memo", "备注" });
				dataCellKeys.add(new String[] { "leadStateDesc", "线索状态" });
				dataCellKeys.add(new String[] { "chargeName", "责任人" });
				//dataCellKeys.add(new String[] { "refereeName", "推荐人" });
				dataCellKeys.add(new String[] { "createDttm", "创建时间" });
				dataCellKeys.add(new String[] { "createWayDesc", "创建方式" });
				dataCellKeys.add(new String[] { "clueId", "线索ID" });
				if("MYXS".equals(exportType)) {
					//我的招生线索导出类别、关闭原因
					dataCellKeys.add(new String[] { "colorType", "类别" });
					dataCellKeys.add(new String[] { "reason", "关闭原因" });
				}
				if("ZSXS".equals(exportType)) {
					//招生线索管理导出关闭/退回原因
					dataCellKeys.add(new String[] { "reason", "关闭/退回原因" });
				}
				/******************************1、生成标题行---结束**************************************/
				
				/******************************2、招生线索数据---开始**************************************/
				// 数据
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();				
				
				//导出搜索结果销售线索
				if(jacksonUtil.containsKey("searchSql")){
					String searchSql = jacksonUtil.getString("searchSql");
					List<Map<String,Object>> clueIdList = sqlQuery.queryForList(searchSql);
					
					for(Map<String,Object> clueIdMap: clueIdList){
						String clueId = clueIdMap.get("TZ_LEAD_ID") == null ? "" : clueIdMap.get("TZ_LEAD_ID").toString();
						
						this.tzExportData(sqlQuery,clueId,dataList,exportType);
					}
				}
				
				String sql = "select TZ_SYSFILE_NAME from PS_TZ_EXCEL_DATT_T where PROCESSINSTANCE=?";
				String strUseFileName = sqlQuery.queryForObject(sql, new Object[]{ processinstance }, "String");
				
				boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
				if (rst) {
					String urlExcel = excelHandle.getExportExcelPath();
					try {
						sqlQuery.update(
								"update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
								new Object[] { urlExcel, processinstance });
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				/******************************2、招生线索数据---结束**************************************/
			}else{
				this.logError("获取批处理参数失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			this.logError("系统错误，"+e.getMessage());
		}
	}
	
	
	private void tzExportData(SqlQuery sqlQuery,String clueId,List<Map<String, Object>> dataList,String exportType){
			
		String name = "", mobile = "", companyName = "", position = "", localAddress = "", bkStateDesc = "", memo = "", 
			   leadStateDesc = "", chargeName = "", refereeName = "", createDttm = "", createWayDesc = "", colorType = "", closeReason = "", reason = "";
			  
		try {
			String sql = "SELECT A.TZ_LEAD_STATUS,(SELECT M.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL M WHERE M.TZ_ZHZ_ID=A.TZ_LEAD_STATUS AND M.TZ_ZHZJH_ID='TZ_LEAD_STATUS') TZ_LEAD_STATUS_DESC,A.TZ_JY_GJ_RQ,A.TZ_THYY_ID,C.TZ_LABEL_NAME TZ_THYY_DESC,A.TZ_GBYY_ID,D.TZ_LABEL_NAME TZ_GBYY_DESC,";
			sql += "A.TZ_COLOUR_SORT_ID,E.TZ_COLOUR_NAME,A.TZ_ZR_OPRID,B.TZ_REALNAME TZ_ZRR_NAME,A.TZ_KH_OPRID,A.TZ_REALNAME,A.TZ_COMP_CNAME,A.TZ_POSITION,A.TZ_MOBILE,A.TZ_PHONE,A.TZ_REFEREE_NAME,";
			sql += "A.TZ_XSQU_ID,(SELECT C.TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T C  WHERE A.TZ_XSQU_ID=C.TZ_LABEL_NAME) TZ_XSQU_DESC,A.TZ_BZ,date_format(A.ROW_ADDED_DTTM,'%Y-%m-%d %H:%i') ROW_ADDED_DTTM,A.TZ_RSFCREATE_WAY,";
			sql += "(SELECT M.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL M WHERE M.TZ_ZHZ_ID=A.TZ_RSFCREATE_WAY AND M.TZ_ZHZJH_ID='TZ_RSFCREATE_WAY') TZ_RSFCREATE_WAY_DESC,IF(A.TZ_LEAD_STATUS='F',C.TZ_LABEL_NAME,IF(A.TZ_LEAD_STATUS='G',D.TZ_LABEL_NAME,'')) TZ_REASON";
			sql += " FROM PS_TZ_XSXS_INFO_T A LEFT JOIN PS_TZ_AQ_YHXX_TBL B ON A.TZ_ZR_OPRID=B.OPRID";
			sql += " LEFT JOIN PS_TZ_THYY_XSGL_T C ON A.TZ_THYY_ID=C.TZ_THYY_ID";
			sql += " LEFT JOIN PS_TZ_GBYY_XSGL_T D ON A.TZ_GBYY_ID=D.TZ_GBYY_ID";
			sql += " LEFT JOIN PS_TZ_XSXS_XSLB_T E ON A.TZ_COLOUR_SORT_ID=E.TZ_COLOUR_SORT_ID";
			sql += " WHERE A.TZ_LEAD_ID=?";
			Map<String, Object> mapClue = sqlQuery.queryForMap(sql,new Object[]{clueId});
			
			if(mapClue!=null) {
				
				name = mapClue.get("TZ_REALNAME") == null ? "" : mapClue.get("TZ_REALNAME").toString();
				mobile = mapClue.get("TZ_MOBILE") == null ? "" : mapClue.get("TZ_MOBILE").toString();
				companyName = mapClue.get("TZ_COMP_CNAME") == null ? "" : mapClue.get("TZ_COMP_CNAME").toString();
				position = mapClue.get("TZ_POSITION") == null ? "" : mapClue.get("TZ_POSITION").toString();
				localAddress = mapClue.get("TZ_XSQU_DESC") == null ? "" : mapClue.get("TZ_XSQU_DESC").toString();
				memo = mapClue.get("TZ_BZ") == null ? "" : mapClue.get("TZ_BZ").toString();
				leadStateDesc = mapClue.get("TZ_LEAD_STATUS_DESC") == null ? "" : mapClue.get("TZ_LEAD_STATUS_DESC").toString();
				chargeName = mapClue.get("TZ_ZRR_NAME") == null ? "" : mapClue.get("TZ_ZRR_NAME").toString();
				refereeName = mapClue.get("TZ_REFEREE_NAME") == null ? "" : mapClue.get("TZ_REFEREE_NAME").toString();
				createDttm = mapClue.get("ROW_ADDED_DTTM") == null ? "" : mapClue.get("ROW_ADDED_DTTM").toString();
				createWayDesc = mapClue.get("TZ_RSFCREATE_WAY_DESC") == null ? "" : mapClue.get("TZ_RSFCREATE_WAY_DESC").toString();
				colorType = mapClue.get("TZ_COLOUR_NAME") == null ? "" : mapClue.get("TZ_COLOUR_NAME").toString();
				closeReason = mapClue.get("TZ_GBYY_DESC") == null ? "" : mapClue.get("TZ_GBYY_DESC").toString();
				reason = mapClue.get("TZ_REASON") == null ? "" : mapClue.get("TZ_REASON").toString();
				
				//报考状态
				String bkStateSql = "SELECT A.TZ_LEAD_ID,A.TZ_APP_INS_ID,B.TZ_FORM_SP_STA,C.TZ_MS_PLAN,D.TZ_BS_RESULT,E.TZ_LQ_STATE,F.TZ_KX_PLAN";
				bkStateSql += " FROM PS_TZ_XSXS_BMB_T A LEFT JOIN PS_TZ_FORM_WRK_T B ON A.TZ_APP_INS_ID=B.TZ_APP_INS_ID";
				bkStateSql += " LEFT JOIN PS_TZ_KSBM_EXT_TBL C ON A.TZ_APP_INS_ID=C.TZ_APP_INS_ID";
				bkStateSql += " LEFT JOIN TZ_IMP_BSBM_TBL D ON A.TZ_APP_INS_ID=D.TZ_APP_INS_ID";
				bkStateSql += " LEFT JOIN TZ_IMP_LQJD_TBL E ON A.TZ_APP_INS_ID=E.TZ_APP_INS_ID";
				bkStateSql += " LEFT JOIN TZ_IMP_KXAP_TBL F ON A.TZ_APP_INS_ID=F.TZ_APP_INS_ID";
				bkStateSql += " WHERE A.TZ_LEAD_ID=?";
				Map<String, Object> mapBmb = sqlQuery.queryForMap(bkStateSql, new Object[]{clueId});
				if(mapBmb!=null) {
					String TZ_FORM_SP_STA =  mapBmb.get("TZ_FORM_SP_STA") == null ? "" : mapBmb.get("TZ_FORM_SP_STA").toString();
					String TZ_MS_PLAN = mapBmb.get("TZ_MS_PLAN") == null ? "" : mapBmb.get("TZ_MS_PLAN").toString();
					String TZ_BS_RESULT = mapBmb.get("TZ_BS_RESULT") == null ? "" : mapBmb.get("TZ_BS_RESULT").toString();
					String TZ_LQ_STATE = mapBmb.get("TZ_LQ_STATE") == null ? "" : mapBmb.get("TZ_LQ_STATE").toString();
					String TZ_KX_PLAN = mapBmb.get("TZ_KX_PLAN") == null ? "" : mapBmb.get("TZ_KX_PLAN").toString();
					
					if(TZ_KX_PLAN!=null && !"".equals(TZ_KX_PLAN)) {
						bkStateDesc="已入学";
					} else if("Y".equals(TZ_LQ_STATE)) {
						bkStateDesc="已录取";
					} else if("Y".equals(TZ_BS_RESULT)) {
						bkStateDesc="面试通过";
					} else if("Y".equals(TZ_MS_PLAN)) {
						bkStateDesc="已安排面试时间";
					} else if("A".equals(TZ_FORM_SP_STA)) {
						bkStateDesc="初审通过";
					} else {
						bkStateDesc="已报名";
					}
				} else {
					bkStateDesc="未报名";
				}
				
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("name", name);
				mapData.put("mobile", mobile);
				mapData.put("companyName", companyName);
				mapData.put("position", position);
				mapData.put("localAddress", localAddress);
				mapData.put("bkStateDesc", bkStateDesc);
				mapData.put("memo", memo);
				mapData.put("leadStateDesc", leadStateDesc);
				mapData.put("chargeName", chargeName);
				//mapData.put("refereeName", refereeName);
				mapData.put("createDttm", createDttm);
				mapData.put("createWayDesc", createWayDesc);
				mapData.put("clueId", clueId);
				if("MYXS".equals(exportType)) {
					//我的招生线索导出类别、关闭原因
					mapData.put("colorType", colorType);
					mapData.put("reason", closeReason);
				}
				if("ZSXS".equals(exportType)) {
					//招生线索管理导出关闭/退回原因
					mapData.put("reason", reason);
				}

				dataList.add(mapData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
