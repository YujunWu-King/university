package com.tranzvision.gd.TZStuCertificateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 查看校友信息更新列表 
 * 
 * @author xzx
 * @since 2017-3-8 
 */
@Service("com.tranzvision.gd.TZStuCertificateBundle.service.impl.TzAlumniInfoServiceImpl")
@SuppressWarnings("unchecked")
public class TzAlumniInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{ "ROW_ADDED_DTTM", "DESC" }};

			// json数据要的结果字段;
//			String[] resultFldArray = { "TZ_APP_INS_ID","TZ_DC_WJ_ID","TZ_REALNAME","TZ_SEX","TZ_PRO_CLASS", "TZ_STATE", "ROW_ADDED_DTTM","ROW_LASTMANT_DTTM"};
			String[] resultFldArray = { "TZ_APP_INS_ID","TZ_DC_WJ_ID","TZ_REALNAME", "TZ_STATE", "ROW_ADDED_DTTM","ROW_LASTMANT_DTTM"};
			
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("wjInsId", rowList[0]);
					mapList.put("wjId", rowList[1]);
					mapList.put("name", rowList[2]);
//					mapList.put("sex", rowList[3]);
//					mapList.put("className", rowList[4]);
					mapList.put("state", rowList[3]);
					mapList.put("ksSj", rowList[4]);
					mapList.put("jsSj", rowList[5]);
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
