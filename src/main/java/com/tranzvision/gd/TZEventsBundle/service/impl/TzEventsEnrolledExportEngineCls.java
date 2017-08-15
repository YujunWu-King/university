package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;

public class TzEventsEnrolledExportEngineCls extends BaseEngine {

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
			Map<String,Object> paramsMap = sqlQuery.queryForMap("select TZ_ART_ID,TZ_REL_URL,TZ_JD_URL,TZ_PARAMS_STR from PS_TZ_HD_BMRDC_AET where RUN_CNTL_ID=?"
					, new Object[] { runControlId });

			if(paramsMap != null){
				String actId = paramsMap.get("TZ_ART_ID").toString();
				String expDirPath = paramsMap.get("TZ_REL_URL").toString();
				String absexpDirPath = paramsMap.get("TZ_JD_URL").toString();
				String strParams = paramsMap.get("TZ_PARAMS_STR").toString();
				
				
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				List<String[]> listDataFields = new ArrayList<String[]>();
				
				/******************************1、生成标题行---开始**************************************/
				dataCellKeys.add(new String[] { "msApplyNo", "面试申请号" });
				
				String sql = "SELECT TZ_ZXBM_XXX_ID,TZ_ZXBM_XXX_NAME,TZ_ZXBM_XXX_ZSXS FROM PS_TZ_ZXBM_XXX_T WHERE TZ_ART_ID=? ORDER BY TZ_PX_XH";
				List<Map<String, Object>> listHeaderFields = sqlQuery.queryForList(sql, new Object[] { actId });
				for (Map<String, Object> mapField : listHeaderFields) {
					// 信息项ID
					String strXxxId = mapField.get("TZ_ZXBM_XXX_ID") == null ? ""
							: String.valueOf(mapField.get("TZ_ZXBM_XXX_ID"));
					// 控件类型
					String strKjType = mapField.get("TZ_ZXBM_XXX_ZSXS") == null ? ""
							: String.valueOf(mapField.get("TZ_ZXBM_XXX_ZSXS"));
					// 信息项名称
					String strXxxName = mapField.get("TZ_ZXBM_XXX_NAME") == null ? ""
							: String.valueOf(mapField.get("TZ_ZXBM_XXX_NAME"));

					dataCellKeys.add(new String[] { strXxxId, strXxxName });

					listDataFields.add(new String[] { strXxxId, strKjType });

				}

				dataCellKeys.add(new String[] { "bmzt", "报名状态" });
				dataCellKeys.add(new String[] { "bmDatetime", "报名时间" });
				dataCellKeys.add(new String[] { "bmqd", "报名渠道" });
				dataCellKeys.add(new String[] { "qdzt", "签到状态" });

				/******************************1、生成标题行---结束**************************************/
				
				
				/******************************2、生成报名人信息数据---开始**************************************/
				
				jacksonUtil.json2Map(strParams);
				
				// 导出报名人信息
				List<?> listBmrIdsExport = new ArrayList<String>();
				
				//导出选中报名人
				if(jacksonUtil.containsKey("bmrIds")){
					listBmrIdsExport = jacksonUtil.getList("bmrIds");
				}
				//导出搜索结果报名人
				if(jacksonUtil.containsKey("searchSql")){
					String searchSql = jacksonUtil.getString("searchSql");
					
					List<Map<String,Object>> bmrList = sqlQuery.queryForList(searchSql);
					
					List<Object> bmrIdList = new ArrayList<Object>();
					for(Map<String,Object> bmrMap: bmrList){
						Object[] bmrIdArr = bmrMap.values().toArray();
						if(bmrIdArr.length > 0){
							bmrIdList.add(bmrIdArr[0]);
						}
					}
					listBmrIdsExport = bmrIdList;
				}
				
				// 数据
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				int num = listBmrIdsExport.size();
				for (int i = 0; i < num; i++) {

					String bmrid = String.valueOf(listBmrIdsExport.get(i));

					Map<String, Object> mapData = new HashMap<String, Object>();

					// 循环信息项，对于控件类型为2-下拉框，取描述值
					for (String[] idAry : listDataFields) {
						String fldName = idAry[0];
						sql = "SELECT " + fldName
								+ " FROM PS_TZ_NAUDLIST_T A LEFT JOIN (SELECT * FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='HDBM') B ON (A.TZ_HD_BMR_ID=B.TZ_LYDX_ID) WHERE A.TZ_ART_ID=? AND A.TZ_HD_BMR_ID=?";
						String fldVal = sqlQuery.queryForObject(sql, new Object[] { actId, bmrid }, "String");

						// 控件类型为2-下拉框，取描述值
						if ("2".equals(idAry[1])) {
							sql = "SELECT TZ_XXX_TRANS_NAME FROM PS_TZ_XXX_TRANS_T WHERE TZ_ART_ID=? AND TZ_ZXBM_XXX_ID=? AND TZ_XXX_TRANS_ID=?";
							fldVal = sqlQuery.queryForObject(sql, new Object[] { actId, fldName, fldVal }, "String");
						}

						mapData.put(fldName, fldVal);

					}

					// 报名渠道、签到状态;
					sql = "SELECT TZ_NREG_STAT,TZ_ZXBM_LY,TZ_BMCY_ZT,OPRID,date_format(TZ_REG_TIME,'%Y-%m-%d %H:%i:%s') as TZ_REG_TIME FROM  PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_HD_BMR_ID=?";
					Map<String, Object> mapqdzt = sqlQuery.queryForMap(sql, new Object[] { actId, bmrid });
					String strBmqd = "";
					String strQdzt = "";
					String strBmzt = "";
					String oprid = "";
					String msApplyNo = ""; /*面试申请号*/
					String regDatetime = "";/*报名时间*/
					if (mapqdzt != null) {
						strBmqd = mapqdzt.get("TZ_ZXBM_LY") == null ? "" : String.valueOf(mapqdzt.get("TZ_ZXBM_LY"));
						strQdzt = mapqdzt.get("TZ_BMCY_ZT") == null ? "" : String.valueOf(mapqdzt.get("TZ_BMCY_ZT"));
						strBmzt = mapqdzt.get("TZ_NREG_STAT") == null ? "" : String.valueOf(mapqdzt.get("TZ_NREG_STAT"));
						oprid = mapqdzt.get("OPRID") == null ? "" : String.valueOf(mapqdzt.get("OPRID"));
						regDatetime = mapqdzt.get("TZ_REG_TIME") == null ? "" : String.valueOf(mapqdzt.get("TZ_REG_TIME"));

						/* 报名渠道、签到状态取转换值配置 */
						sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BMR_APPLY_QD' AND TZ_ZHZ_ID=?";
						strBmqd = sqlQuery.queryForObject(sql, new Object[] { strBmqd }, "String");
						sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_MBRGL_CYZT' AND TZ_ZHZ_ID=?";
						strQdzt = sqlQuery.queryForObject(sql, new Object[] { strQdzt }, "String");
						sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BMR_APPLY_STA' AND TZ_ZHZ_ID=?";
						strBmzt = sqlQuery.queryForObject(sql, new Object[] { strBmzt }, "String");
					}
					mapData.put("bmzt", strBmzt);
					mapData.put("bmDatetime", regDatetime);
					mapData.put("bmqd", strBmqd);
					mapData.put("qdzt", strQdzt);
					
					if(!"".equals(oprid)){
						sql = "SELECT TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? LIMIT 1";
						msApplyNo = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
					}
					mapData.put("msApplyNo", msApplyNo);

					dataList.add(mapData);
				}
				
				sql = "select TZ_SYSFILE_NAME from PS_TZ_EXCEL_DATT_T where PROCESSINSTANCE=?";
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
				/******************************2、生成报名人信息数据---结束**************************************/
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
