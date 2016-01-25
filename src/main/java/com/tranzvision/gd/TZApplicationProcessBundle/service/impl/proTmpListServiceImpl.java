package com.tranzvision.gd.TZApplicationProcessBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationProcessBundle.dao.PsTzAppProTmpTMapper;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProTmpT;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：项目流程配置管理页面;
 * 原PS类：TZ_GD_PROTMPLATE_PKG:TZ_GD_PROTMP_LIST_CLS
 */
@Service("com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proTmpListServiceImpl")
public class proTmpListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzAppProTmpTMapper PsTzAppProTmpTMapper;
	/* 查询项目流程配置模版列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_APPPRO_TMP_ID", "TZ_APPPRO_TMP_NAME", "TZ_APPPRO_STATUS" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("TZ_APPPRO_TMP_ID", rowList[0]);
				mapList.put("TZ_APPPRO_TMP_NAME", rowList[1]);
				mapList.put("TZ_APPPRO_STATUS", rowList[2]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	/* 获取流程模版信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("TZ_APPPRO_TMP_ID")) {
				// 流程模版编号;
				String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
				PsTzAppProTmpT psTzAppProTmpT = PsTzAppProTmpTMapper.selectByPrimaryKey(strAppProcessTmpId);
				if (psTzAppProTmpT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("TZ_APPPRO_TMP_ID", psTzAppProTmpT.getTzAppproTmpId());
					retMap.put("TZ_APPPRO_TMP_NAME", psTzAppProTmpT.getTzAppproTmpName());
					retMap.put("TZ_APPPRO_STATUS", psTzAppProTmpT.getTzAppproStatus());
					returnJsonMap.replace("formData",retMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该流程模版数据不存在！";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该流程模版数据不存在！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	/**
	 * 删除报名流程模版
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 流程模版编号;
				String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
				
				if (strAppProcessTmpId != null && !"".equals(strAppProcessTmpId)) {
					PsTzAppProTmpTMapper.deleteByPrimaryKey(strAppProcessTmpId);
					/*同时删除流程对应的常用回复短语信息和流程信息*/
					Object[] args = new Object[] { strAppProcessTmpId };
					sqlQuery.update("DELETE FROM PS_TZ_APPPRO_STP_T WHERE TZ_APPPRO_TMP_ID = ?", args);
					sqlQuery.update("DELETE FROM PS_TZ_APPPRO_HF_T WHERE TZ_APPPRO_TMP_ID = ?", args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}
