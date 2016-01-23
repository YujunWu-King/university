package com.tranzvision.gd.TZProjectSetBundle.service.impl;

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
 * @author 张彬彬; 
 * 功能说明：搜索管理人员;
 * 原PS类：TZ_GD_PROMG_PKG:TZ_GD_USER_CLS
 */
@Service("com.tranzvision.gd.TZProjectSetBundle.service.impl.tzProUserClsServiceImpl")
public class tzProUserClsServiceImpl extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询项目分类列表 */
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
		String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_MOBILE", "TZ_EMAIL" , "TZ_DLZH_ID" , "TZ_JG_ID" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("userOprid", rowList[0]);
				mapList.put("userName", rowList[1]);
				mapList.put("userPhone", rowList[2]);
				mapList.put("userEmail", rowList[3]);
				mapList.put("accountNum", rowList[4]);
				mapList.put("orgId", rowList[5]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
