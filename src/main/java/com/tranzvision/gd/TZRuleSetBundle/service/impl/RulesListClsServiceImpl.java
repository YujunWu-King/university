package com.tranzvision.gd.TZRuleSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_JYGZ_GL_PKG:TZ_JYGZ_GL_CLS
 * 
 * 校验规则定义
 */
@Service("com.tranzvision.gd.TZRuleSetBundle.service.impl.RulesListClsServiceImpl")
public class RulesListClsServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	
	/* 查询规则列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_JYGZ_MC", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_JYGZ_ID", "TZ_JYGZ_MC", "TZ_JYGZ_JSLMC", "TZ_JYGZ_APPCLSID", "TZ_JYGZ_TSXX", "TZ_JYGZ_TSXX_EN", "TZ_EFFEXP_ZT"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("jygzID", rowList[0]);
					mapList.put("jygzName", rowList[1]);
					mapList.put("jygzClssName", rowList[2]);
					mapList.put("jygzFwdJy", rowList[3]);
					mapList.put("jygzTsxx", rowList[4]);
					mapList.put("jygzEnTsxx", rowList[5]);
					mapList.put("jygzState", rowList[6]);
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
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 校验规则id
				String jygzID = jacksonUtil.getString("jygzID");
				if(StringUtils.isNotEmpty(jygzID)){
					// 删除规则定义信息;
					sqlQuery.update("DELETE FROM PS_TZ_JYGZ_DY_T WHERE TZ_JYGZ_ID=?", new Object[] { jygzID });
					// 删除规则定义多语言信息;
					sqlQuery.update("DELETE FROM PS_TZ_JYGZ_DY_ENG WHERE TZ_JYGZ_ID=?", new Object[] { jygzID });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
