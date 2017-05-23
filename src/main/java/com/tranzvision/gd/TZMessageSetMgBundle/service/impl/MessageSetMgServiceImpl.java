/**
 * 
 */
package com.tranzvision.gd.TZMessageSetMgBundle.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.Memoryparameter;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 消息集合管理功能；原：TZ_GD_MESSAGE_PKG:TZ_MESSAGESET_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-09
 */
@Service("com.tranzvision.gd.TZMessageSetMgBundle.service.impl.MessageSetMgServiceImpl")
public class MessageSetMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	final String LJ = "@";

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
			String[] resultFldArray = { "TZ_XXJH_ID", "TZ_XXJH_MC" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("msgSetID", rowList[0]);
					mapList.put("msgSetDesc", rowList[1]);

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
				// 消息集合id
				String msgSetID = jacksonUtil.getString("msgSetID");

				if (msgSetID != null && !"".equals(msgSetID)) {

					// 删除转换值集合信息;
					sqlQuery.update("delete from PS_TZ_PT_XXJH_TBL where TZ_XXJH_ID=?", new Object[] { msgSetID });
					// 删除转换值信息;
					sqlQuery.update("delete from PS_TZ_PT_XXDY_TBL where TZ_XXJH_ID=?", new Object[] { msgSetID });

					Map<String, Map<String, String>> map = Memoryparameter.messageText;
					Iterator<Map.Entry<String, Map<String, String>>> iter = map.entrySet().iterator();
					List<String> keyList = new ArrayList<String>();
					while (iter.hasNext()) {
						Map.Entry<String, Map<String, String>> entry = (Map.Entry<String, Map<String, String>>) iter
								.next();

						keyList.add(entry.getKey());
					}
					String key = null;
					for (Object objDataTap : keyList) {
						key = (String) objDataTap;
						if (key.startsWith(msgSetID)) {
							Memoryparameter.messageText.remove(key);
						}
					}

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
