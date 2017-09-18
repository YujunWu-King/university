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
 * 证书日志查询
 * 
 * @author xzx
 * @since 2017-3-6 
 */
@Service("com.tranzvision.gd.TZStuCertificateBundle.service.impl.TzStuCertOprLogServiceImpl")
@SuppressWarnings("unchecked")
public class TzStuCertOprLogServiceImpl extends FrameworkImpl {
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
			String[] resultFldArray = { "TZ_JG_ID","TZ_ZHSH_ID","TZ_CERT_TYPE_NAME", "TZ_REALNAME", "TZ_CLASS_NAME", "TZ_CZ_TYPE","ROW_ADDED_DTTM","ROW_ADDED_DATE"};
			
//			String admin = "\"TZ_JG_ID-operator\":\"01\",\"TZ_JG_ID-value\":\"ADMIN\",";
//			strParams.replaceAll(admin, "");
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("jgId", rowList[0]);
					mapList.put("certNo", rowList[1]);
					mapList.put("certTypeName", rowList[2]);
					mapList.put("name", rowList[3]);
					mapList.put("className", rowList[4]);
					mapList.put("operationType", rowList[5]);
					mapList.put("timeStamp", rowList[6]);
					mapList.put("timeStampDate", rowList[7]);
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
