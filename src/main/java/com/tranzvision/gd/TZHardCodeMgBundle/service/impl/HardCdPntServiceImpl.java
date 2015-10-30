package com.tranzvision.gd.TZHardCodeMgBundle.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZHardCodeMgBundle.dao.PsCmbcHardcdPntMapper;
/*
 * Hardcode点定义， 原PS类：TZ_GD_HARDCODE_PKG:TZ_GD_HARDCODE_CLS
 * @author tang
 */
@Service("com.tranzvision.gd.TZHardCodeMgBundle.service.impl.HardCdPntServiceImpl")
public class HardCdPntServiceImpl extends FrameworkImpl {
	@Autowired
	private PsCmbcHardcdPntMapper psCmbcHardcdPntMapper;
	
	/* 加载HardCode列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		String strRet = "";
		FliterForm fliterForm = new FliterForm();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "CMBC_HARDCODE_PNT", "ASC" } };
		fliterForm.orderByArr = orderByArr;

		// json数据要的结果字段;
		String[] resultFldArray = { "CMBC_HARDCODE_PNT", "CMBC_DESCR254", "CMBC_HARDCODE_VAL" };
		String jsonString = "";

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, comParams,
				numLimit, numStart, errorMsg);

		if (obj == null || obj.length == 0) {
			strRet = "{\"total\":0,\"root\":[]}";
		} else {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				jsonString = jsonString + ",{\"hardCodeName\":\"" + rowList[0]
						+ "\",\"hardCodeDesc\":\"" + rowList[1]+ "\",\"hardCodeValue\":\"" + rowList[2] + "\"}";
			}
			if (!"".equals(jsonString)) {
				jsonString = jsonString.substring(1);
			}

			strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
		}

		return strRet;
	}
}
