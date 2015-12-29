/**
 * 
 */
package com.tranzvision.gd.TZTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 元模板配置列表页面类，原PS：TZ_GD_EMLSMSSET_PKG:TZ_GD_RESTMPMG_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-01
 */
@Service("com.tranzvision.gd.TZTemplateBundle.service.impl.TzTemplateMgServiceImpl")
public class TzTemplateMgServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

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
			String[] resultFldArray = { "TZ_YMB_ID", "TZ_YMB_NAME", "TZ_JG_ID", "TZ_JG_NAME", "TZ_USE_FLAG" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				int listcount = list.size();
				for (int i = 0; i < listcount; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("restempid", rowList[0]);
					mapList.put("restempname", rowList[1]);
					mapList.put("restemporg", rowList[2]);
					mapList.put("orgname", rowList[3]);
					mapList.put("isneed", rowList[4]);

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

	/**
	 * 删除元模板信息
	 */
	@Override
	@Transactional
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
				// 参数
				String restempid = jacksonUtil.getString("restempid");

				if (restempid != null && !"".equals(restempid)) {

					/* 删除该元模版下的参数定义、数据采集规则定义 */
					Object[] args = new Object[] { restempid };
					sqlQuery.update("delete from PS_TZ_TMP_DEFN_TBL where TZ_YMB_ID = ?", args);
					sqlQuery.update("delete from PS_TZ_TMP_PARA_TBL where TZ_YMB_ID = ?", args);
					sqlQuery.update("delete from PS_TZ_TMP_RRKF_TBL where TZ_YMB_ID = ?", args);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 动态查询邮箱服务
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("restemporg") && jacksonUtil.containsKey("restemporg")) {

				String restemporg = jacksonUtil.getString("restemporg");

				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("orgid", restemporg);

				String sql = "select ifnull(TZ_EMLSERV_ID,'') as TZ_EMLSERV_ID,ifnull(TZ_EML_ADDR100,'') as TZ_EML_ADDR100 from PS_TZ_EMLS_DEF_TBL where TZ_JG_ID = ? and TZ_IS_DEFAULT = 'Y'";
				Map<String, Object> emailMap = sqlQuery.queryForMap(sql, new Object[] { restemporg });
				if (emailMap != null) {
					mapData.put("emlid", String.valueOf(emailMap.get("TZ_EMLSERV_ID")));
					mapData.put("emladdr", String.valueOf(emailMap.get("TZ_EML_ADDR100")));
				}

				sql = "select ifnull(TZ_SMS_SERV_ID,'') as TZ_SMS_SERV_ID, ifnull(TZ_SMS_SERV_NAME,'') as TZ_SMS_SERV_NAME from PS_TZ_SMSSERV_TBL";
				Map<String, Object> smsMap = sqlQuery.queryForMap(sql, new Object[] { restemporg });
				if (smsMap != null) {
					mapData.put("smsid", String.valueOf(emailMap.get("TZ_SMS_SERV_ID")));
					mapData.put("smsName", String.valueOf(emailMap.get("TZ_SMS_SERV_NAME")));
				}

				strRet = jacksonUtil.Map2json(mapData);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
