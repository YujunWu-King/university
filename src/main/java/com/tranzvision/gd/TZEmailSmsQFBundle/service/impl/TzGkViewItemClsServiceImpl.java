package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * TZ_GK_EDM_PKG:TZ_GK_VIEWITEM_CLS rem EDP统计： 指标项详情列表;
 **/
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.imp.TzGkViewItemClsServiceImpl")
public class TzGkViewItemClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 获得不同指标项对应的视图;
		jacksonUtil.json2Map(comParams);
		String cfgSrhId = jacksonUtil.getString("cfgSrhId");
		String strFindView = cfgSrhId.split("\\.")[2];

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {};
		// 发送总数/发送成功数;
		if ("TZ_YJQF_FSZS_VW".equals(strFindView) || "TZ_YJQF_FSCG_VW".equals(strFindView)) {
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_REALNAME", "TZ_JS_EMAIL", "TZ_EDM_TIME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("personName", rowList[0]);
					mapList.put("sendEmail", rowList[1]);
					mapList.put("sendTime", rowList[2]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		}

		// 硬退数/软退数;
		if ("TZ_YJQF_YT_VW".equals(strFindView) || "TZ_YJQF_RT_VW".equals(strFindView)) {
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EMAIL", "TZ_EDM_TIME", "TZ_TX_DESC", "TZ_MAL_CONTENT" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("refusedEmail", rowList[0]);
					mapList.put("refusedTime", rowList[1]);
					mapList.put("refusedReason", rowList[2]);
					mapList.put("mailContent", rowList[3]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		}

		// 总打开数;
		if ("TZ_YJQF_ZDK_VW".equals(strFindView)) {
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EMAIL", "TZ_IP_ADDR", "TZ_EDM_TIME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("openEmail", rowList[0]);
					mapList.put("ipAddr", rowList[1]);
					mapList.put("openTime", rowList[2]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		}

		// 净打开数;
		if ("TZ_YJQF_JDK_VW".equals(strFindView)) {
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EMAIL" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("openEmail", rowList[0]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		}

		// 总点击数;
		if ("TZ_YJQF_ZDJ_VW".equals(strFindView)) {
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EMAIL", "TZ_EDM_TIME", "TZ_RED_URL" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("clickEmail", rowList[0]);
					mapList.put("clickTime", rowList[1]);
					mapList.put("clickWebsite", rowList[2]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		}

		// 净点击数;
		if ("TZ_YJQF_JDJ_VW".equals(strFindView)) {
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EMAIL", "TZ_RED_URL" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("clickEmail", rowList[0]);
					mapList.put("clickWebsite", rowList[1]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		}

		// 退订数;
		if ("TZ_YJQF_TD_VW".equals(strFindView)) {
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EMAIL", "TZ_TDQD_CHN" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("backEmail", rowList[0]);
					mapList.put("backChannel", rowList[1]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}

		}
		return jacksonUtil.Map2json(mapRet);
	}

}
