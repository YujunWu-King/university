package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * PS:TZ_EMIAL_VIEWHISTORY_PKG:TZ_EMAIL_DETAIL_CLS
 * 
 * @author Administrator 功能说明：邮件历史查看
 *
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzEmailDetailClsServiceImpl")
public class TzEmailDetailClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	/* 邮件历史查看 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EML_SMS_TASK_ID", "TZ_AUDIENCE_ID", "TZ_AUDCY_ID", "TZ_AUD_XM", "TZ_ZY_EMAIL", "OPRID", "TZ_RWSL_ID", "TZ_FS_ZT", "TZ_FS_DT"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("taskID", rowList[0]);
					mapList.put("AddresseeEmail", rowList[1]);
					mapList.put("audCyID", rowList[2]);
					mapList.put("AddresseeName", rowList[3]);
					mapList.put("AddresseeEmail", rowList[4]);
					mapList.put("audCyOprID", rowList[5]);
					mapList.put("rwslID", rowList[6]);
					mapList.put("status", rowList[7]);
					mapList.put("sendTime", rowList[8]);
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
