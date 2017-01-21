package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * TZ_GD_SMSQF_PKG:TZ_SMSQF_GL_CLS
 * 
 * 获取短信群发任务列表
 *
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzSmsqfGlClsServiceImpl")
public class TzSmsqfGlClsServiceImpl extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {{"ROW_ADDED_DTTM","DESC"}};

		// json数据要的结果字段;
		String[] resultFldArray = {"TZ_MLSM_QFPC_ID", "TZ_MLSM_QFPC_DESC",  "OPRID", "TZ_REALNAME", "ROW_ADDED_DTTM"};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("smsQfId", rowList[0]);
				mapList.put("smsQfDesc", rowList[1]);
				mapList.put("oprid", rowList[2]);
				mapList.put("crePer", rowList[3]);
				mapList.put("creDt", rowList[4]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
