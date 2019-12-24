package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.util.base.JacksonUtil;


@Service("com.tranzvision.gd.TZBaseBundle.service.impl.DemoImpl")
public class DemoImpl extends FrameworkImpl {
	

	@Autowired
	private FliterForm fliterForm;

	
	/* 获取用户账号信息列表 */
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
			String[][] orderByArr = new String[][] { { "TZ_REALNAME", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_REALNAME", "TZ_MOBILE", "TZ_CLASS_NAME", "TZ_BATCH_NAME", "TZ_APP_FORM_STA", "TZ_APP_SUB_DTTM",
					"TZ_FILL_PROPORTION", "OPRID" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("TZ_REALNAME", rowList[0]);
					mapList.put("TZ_MOBILE", rowList[1]);
					mapList.put("TZ_CLASS_NAME", rowList[2]);
					mapList.put("TZ_BATCH_NAME", rowList[3]);
					mapList.put("TZ_APP_FORM_STA", rowList[4]);
					mapList.put("TZ_APP_SUB_DTTM", rowList[5]);
					mapList.put("TZ_FILL_PROPORTION", rowList[6]);
					mapList.put("OPRID", rowList[7]);
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
}
