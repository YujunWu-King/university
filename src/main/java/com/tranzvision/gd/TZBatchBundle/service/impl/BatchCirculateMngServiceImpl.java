package com.tranzvision.gd.TZBatchBundle.service.impl;

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
 * @author TANG
 * 进程循环定义列表
 */
@Service("com.tranzvision.gd.TZBatchBundle.service.impl.BatchCirculateMngServiceImpl")
public class BatchCirculateMngServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询进程循环列表 */
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
			String[][] orderByArr = new String[][] { {"TZ_XH_MC","ASC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID","TZ_XH_MC", "TZ_XH_MS" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("orgId", rowList[0]);
					mapList.put("batchCirculateName", rowList[1]);
					mapList.put("batchCirculateDecs", rowList[2]);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
}
