/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 高端产品-班级管理-班级列表页面 原PS：TZ_GD_BJGL_CLS:TZ_GD_BJGL_CLS
 * 
 * @author SHIHUA
 * @since 2016-01-28
 * @author ZXW  2017-01-21
 * 功能修改说明：新增班级的时候，同时给材料面试初筛相关字段赋值
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassMgServiceImpl")
public class TzClassMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	/**
	 * 获得班级列表（班级编号、班级名称、所属项目、项目类别）
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
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_CLASS_NAME", "TZ_PRJ_NAME","TZ_RX_DT", "TZ_START_DT", "TZ_END_DT",
					"TZ_IS_OPEN_DESC" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("bj_id", rowList[0]);
					mapList.put("bj_name", rowList[1]);
					mapList.put("xm_name", rowList[2]);
					mapList.put("rx_time", rowList[3]);
					mapList.put("begin_time", rowList[4]);
					mapList.put("end_time", rowList[5]);
					mapList.put("bj_kt_desc", rowList[6]);

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
