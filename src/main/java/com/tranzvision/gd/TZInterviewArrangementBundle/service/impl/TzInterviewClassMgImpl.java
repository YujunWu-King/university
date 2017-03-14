package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 高端产品-面试管理-面试日程安排  原PS:TZ_GD_MS_ARR_PKG:TZ_GD_MS_CLASS_CLS
 * @author ZHANG LANG
 *
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewClassMgImpl")
public class TzInterviewClassMgImpl extends FrameworkImpl{
	
	@Autowired
	private FliterForm fliterForm;

	/**
	 * 班级管理列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {new String[]{"TZ_YEAR","DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_CLASS_ID", "TZ_CLASS_NAME", "TZ_PRJ_NAME", "TZ_PRJ_TYPE_NAME","TZ_YEAR", "TZ_NUM_APPLICANTS", "TZ_NUM_NOAUDIT"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classID", rowList[0]);
					mapList.put("className", rowList[1]);
					mapList.put("projectName", rowList[2]);
					mapList.put("projectType", rowList[3]);
					mapList.put("enrollmentYear", rowList[4]);
					mapList.put("applicantsNumber", rowList[5]);
					mapList.put("noauditNumber", rowList[6]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}
}


