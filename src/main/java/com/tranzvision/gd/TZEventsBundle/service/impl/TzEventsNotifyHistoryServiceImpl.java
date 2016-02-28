/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 查看通知函件发送历史操作类
 * 
 * @author SHIHUA
 * @since 2016-02-25
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsNotifyHistoryServiceImpl")
public class TzEventsNotifyHistoryServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段
			String[][] orderByArr = new String[][] { new String[] { "TZ_MAX_ZD_SEQ", "DESC" },
					new String[] { "TZ_START_DT", "DESC" }, new String[] { "TZ_START_TM", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_HUOD_ID", "TZ_TASK_LX", "TZ_JCSL_ZT", "TZ_TOTAL_NUM", "TZ_SUCC_NUM",
					"TZ_FAIL_NUM", "TZ_SLTJ_DT" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("activityId", rowList[0]);
					mapList.put("sendType", rowList[1]);
					mapList.put("sendStatus", rowList[2]);
					mapList.put("sendCount", rowList[3]);
					mapList.put("successNum", rowList[4]);
					mapList.put("failedNum", rowList[5]);
					mapList.put("sendDate", rowList[6]);

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
