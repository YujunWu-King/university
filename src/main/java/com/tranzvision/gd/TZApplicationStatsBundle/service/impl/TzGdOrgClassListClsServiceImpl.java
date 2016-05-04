package com.tranzvision.gd.TZApplicationStatsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
/**
 * 
 * @author WRL 报名管理-学生报考多项目统计--添加统计项目
 */
@Service("com.tranzvision.gd.TZApplicationStatsBundle.service.impl.TzGdOrgClassListClsServiceImpl")

public class TzGdOrgClassListClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询统计项目列表 */
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
		String[] resultFldArray = { "TZ_CLASS_ID","TZ_CLASS_NAME","TZ_IS_APP_OPEN","TZ_JG_ID" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
			             
				mapList.put("classId", rowList[0]);
				mapList.put("className", rowList[1]);
				mapList.put("classStatus", rowList[2]);
				mapList.put("orgId", rowList[3]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
