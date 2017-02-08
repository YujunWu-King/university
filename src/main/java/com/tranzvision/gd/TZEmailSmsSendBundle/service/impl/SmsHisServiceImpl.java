package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 查询短信发送历史 	原PS：TZ_GD_COM_EMLSMS_APP:smsHis
 * @author zhanglang
 * 2017-02-08
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SmsHisServiceImpl")
public class SmsHisServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{"TZ_RWZX_DT_STR", "DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_RWZX_ZT_DESC", "TZ_SEND_COUNT", "TZ_SEND_SUC_COUNT", "TZ_SEND_FAIL_COUNT", "TZ_RWZX_DT_STR", "TZ_SEND_RPT_COUNT", "TZ_REALNAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("status", rowList[0]);
					mapList.put("sendNum", rowList[1]);
					mapList.put("sendSucNum", rowList[2]);
					mapList.put("sendFailNum", rowList[3]);
					mapList.put("sendDt", rowList[4]);
					mapList.put("sendRptNum", rowList[5]);
					mapList.put("operator", rowList[6]);
					
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
