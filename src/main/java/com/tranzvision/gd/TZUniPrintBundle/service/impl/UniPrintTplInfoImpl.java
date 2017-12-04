package com.tranzvision.gd.TZUniPrintBundle.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZImportTplMgBundle.dao.TzImpTplDfnTMapper;
import com.tranzvision.gd.TZImportTplMgBundle.dao.TzImpTplFldTMapper;
import com.tranzvision.gd.TZImportTplMgBundle.model.TzImpTplDfnT;
import com.tranzvision.gd.TZImportTplMgBundle.model.TzImpTplFldT;
import com.tranzvision.gd.TZUniPrintBundle.dao.TzDymbTblMapper;
import com.tranzvision.gd.TZUniPrintBundle.model.TzDymbTbl;
import com.tranzvision.gd.TZUniPrintBundle.model.TzDymbTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
/*
 * 打印模板信息
 * 清华mba招生
 */
@Service("com.tranzvision.gd.TZUniPrintBundle.service.impl.UniPrintTplInfoImpl")
public class UniPrintTplInfoImpl extends FrameworkImpl {
	@Autowired
	private TzImpTplDfnTMapper TzImpTplDfnTMapper;
	@Autowired
	private TzImpTplFldTMapper TzImpTplFldTMapper;
	@Autowired
	private TzDymbTblMapper TzDymbTblMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	
	/* 获取打印模板定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("tplId")&&jacksonUtil.containsKey("jgId")) {
				String tplId = jacksonUtil.getString("tplId");
				String jgId = jacksonUtil.getString("jgId");
				
				TzDymbTblKey tzDymbTblKey = new TzDymbTblKey();
				tzDymbTblKey.setTzJgId(jgId);
				tzDymbTblKey.setTzDymbId(tplId);
				
				TzDymbTbl tzDymbTbl=  TzDymbTblMapper.selectByPrimaryKey(tzDymbTblKey);
				
				if (tzDymbTbl != null) {
					returnJsonMap.put("TZ_JG_ID", tzDymbTbl.getTzJgId());
					returnJsonMap.put("TZ_DYMB_ID", tzDymbTbl.getTzDymbId());
					returnJsonMap.put("TZ_DYMB_NAME", tzDymbTbl.getTzDymbName());
					returnJsonMap.put("TZ_DYMB_ZT", tzDymbTbl.getTzDymbZt());
					returnJsonMap.put("TZ_DYMB_DRMB_ID", tzDymbTbl.getTzDymbDrmbId());
					returnJsonMap.put("TZ_DYMB_MENO", tzDymbTbl.getTzDymbMeno());
					returnJsonMap.put("TZ_DYMB_PDF_URL", tzDymbTbl.getTzDymbPdfUrl());
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该打印模板不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该打印模板不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

		@Override
		public String tzAdd(String[] actData, String[] errMsg) {
			String strRet = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					// 表单内容;
					String strForm = actData[num];
					// 将字符串转换成json;
					jacksonUtil.json2Map(strForm);
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");

					String type = (String) jacksonUtil.getString("type");
					switch(type){
					case "TPL":
						String tplId = (String) infoData.get("tplId");
						String tplName = (String) infoData.get("tplName");
						String targetTbl = (String) infoData.get("targetTbl");
						String javaClass = (String) infoData.get("javaClass");
						String excelTpl = (String) infoData.get("excelTpl");
						String enableMapping = (String) infoData.get("enableMapping");
						
						String sql = "SELECT COUNT(1) from TZ_IMP_TPL_DFN_T WHERE TZ_TPL_ID=?";
						int count = jdbcTemplate.queryForObject(sql, new Object[] { tplId }, "Integer");
						if (count > 0) {
							errMsg[0] = "1";
							errMsg[1] = "模板编号为：" + tplId + "的信息已经存在，请修改模板编号。";
						} else {
							TzImpTplDfnT tzImpTplDfnT = new TzImpTplDfnT();
							tzImpTplDfnT.setTzTplId(tplId);
							tzImpTplDfnT.setTzTplName(tplName);
							tzImpTplDfnT.setTzTargetTbl(targetTbl);
							tzImpTplDfnT.setTzJavaClass(javaClass);
							tzImpTplDfnT.setTzExcelTpl(excelTpl);
							tzImpTplDfnT.setTzEnableMapping(enableMapping);
							
							TzImpTplDfnTMapper.insert(tzImpTplDfnT);
						}
						break;
					case "FIELD":
						if(errMsg[0]!="1")this.tzUpdateFields(infoData,errMsg);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg[0] = "1";
				errMsg[1] = e.toString();
			}
			return strRet;
		}

		@Override
		public String tzUpdate(String[] actData, String[] errMsg) {
			String strRet = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					// 表单内容;
					String strForm = actData[num];
					// 将字符串转换成json;
					jacksonUtil.json2Map(strForm);
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");

					String type = (String) jacksonUtil.getString("type");
					switch(type){
					case "TPL":
						Boolean targetTblChanged = jacksonUtil.getBoolean("targetTblChanged");
						
						String tplId = (String) infoData.get("tplId");

						if(targetTblChanged){
							String sqlDelete = "DELETE FROM TZ_IMP_TPL_FLD_T WHERE TZ_TPL_ID=?";
							jdbcTemplate.update(sqlDelete, new Object[] { tplId });
						}
						
						String tplName = (String) infoData.get("tplName");
						String targetTbl = (String) infoData.get("targetTbl");
						String javaClass = (String) infoData.get("javaClass");
						String excelTpl = (String) infoData.get("excelTpl");
						String enableMapping = (String) infoData.get("enableMapping");
	
						String sql = "SELECT COUNT(1) from TZ_IMP_TPL_DFN_T WHERE TZ_TPL_ID=?";
						int count = jdbcTemplate.queryForObject(sql, new Object[] { tplId }, "Integer");
						if (count > 0) {
							TzImpTplDfnT tzImpTplDfnT = new TzImpTplDfnT();
							tzImpTplDfnT.setTzTplId(tplId);
							tzImpTplDfnT.setTzTplName(tplName);
							tzImpTplDfnT.setTzTargetTbl(targetTbl);
							tzImpTplDfnT.setTzJavaClass(javaClass);
							tzImpTplDfnT.setTzExcelTpl(excelTpl);
							tzImpTplDfnT.setTzEnableMapping(enableMapping);
							
							TzImpTplDfnTMapper.updateByPrimaryKey(tzImpTplDfnT);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "模板编号为：" + tplId + "的信息不存在。";						
						}
						break;
					case "FIELD":
						if(errMsg[0]!="1")this.tzUpdateFields(infoData,errMsg);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg[0] = "1";
				errMsg[1] = e.toString();
			}
			return strRet;
		}
		
		/*加载打印模板字段列表 */
		@Override
		@SuppressWarnings("unchecked")
		public String tzQueryList(String comParams, int numLimit, int numStart,
				String[] errorMsg) {
			// 返回值;
			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", 0);
			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
			mapRet.put("root", listData);
			JacksonUtil jacksonUtil = new JacksonUtil();

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_DYMB_ID", "ASC" } };
			//String[][] orderByArr = new String[][] {  };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID", "TZ_DYMB_ID", "TZ_DYMB_FIELD_ID", "TZ_DYMB_FIELD_SM", "TZ_DYMB_FIELD_QY", "TZ_DYMB_FIELD_PDF"};
System.out.println("-->1");
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
					numLimit, numStart, errorMsg);
			System.out.println("-->2");
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("TZ_JG_ID", rowList[0]);
					mapList.put("TZ_DYMB_ID", rowList[1]);
					mapList.put("TZ_DYMB_FIELD_ID", rowList[2]);
					mapList.put("TZ_DYMB_FIELD_SM", rowList[3]);
					mapList.put("TZ_DYMB_FIELD_QY", rowList[4]);
					mapList.put("TZ_DYMB_FIELD_PDF", rowList[5]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

			return jacksonUtil.Map2json(mapRet);
		}
		
		/*更新列表字段 */
		public String tzUpdateFields(Map<String, Object> infoData, String[] errMsg){
			String tplId = (String) infoData.get("tplId");
			String field = (String) infoData.get("field");
			String fieldName = (String) infoData.get("fieldName");
			int seq = (int) infoData.get("seq");
			String required = ((Boolean) infoData.get("required"))==true?"Y":"N";
			String cover = ((Boolean) infoData.get("cover"))==true?"Y":"N";
			String display = ((Boolean) infoData.get("display"))==true?"Y":"N";
			String columnTitle = (String) infoData.get("columnTitle");
			
			String sql = "SELECT COUNT(1) from TZ_IMP_TPL_FLD_T WHERE TZ_TPL_ID=? AND TZ_FIELD=?";
			
			TzImpTplFldT tzImpTplFldT = new TzImpTplFldT();
			
			int count = jdbcTemplate.queryForObject(sql, new Object[] { tplId , field }, "Integer");
			if (count > 0) {
				tzImpTplFldT.setTzTplId(tplId);
				tzImpTplFldT.setTzField(field);
				tzImpTplFldT.setTzFieldName(fieldName);
				tzImpTplFldT.setTzSeq(seq);
				tzImpTplFldT.setTzRequired(required);
				tzImpTplFldT.setTzCover(cover);
				tzImpTplFldT.setTzDisplay(display);
				tzImpTplFldT.setTzColTitle(columnTitle);
				
				TzImpTplFldTMapper.updateByPrimaryKey(tzImpTplFldT);
			} else {
				tzImpTplFldT.setTzTplId(tplId);
				tzImpTplFldT.setTzField(field);
				tzImpTplFldT.setTzFieldName(fieldName);
				tzImpTplFldT.setTzSeq(seq);
				tzImpTplFldT.setTzRequired(required);
				tzImpTplFldT.setTzCover(cover);
				tzImpTplFldT.setTzDisplay(display);
				tzImpTplFldT.setTzColTitle(columnTitle);
				
				TzImpTplFldTMapper.insert(tzImpTplFldT);
			}
			return "";
		}

}
