package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy 
 * @version 创建时间：2016年7月27日 下午3:48:13 类说明  问卷模板列表页面
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.SurveyListClsServiceImpl")
public class SurveyListClsServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;

	/* 查询调查表列表 */
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
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_APP_TPL_ID", "TZ_APP_TPL_MC", "TZ_EFFEXP_ZT" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();

					mapList.put("TZ_APP_TPL_ID", rowList[0]);
					mapList.put("TZ_APP_TPL_MC", rowList[1]);
					mapList.put("TZ_EFFEXP_ZT", rowList[2]);
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

	/* 删除 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		if (actData.length == 0) {
			return strRet;
		}

		int dataLength = actData.length;
		for (int num = 0; num < dataLength; num++) {
			// 表单内容
			String strForm = actData[num];
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strForm);
			String TZ_APP_TPL_ID = jacksonUtil.getString("TZ_APP_TPL_ID");

			if (!StringUtils.isBlank(TZ_APP_TPL_ID)) {
				this.deleteMbInfo(TZ_APP_TPL_ID, errMsg);
			}
		}
		return strRet;
	}

	private void deleteMbInfo(String TZ_APP_TPL_ID, String[] errMsg) {
		try {
			// 删除指定控件id的控件信息
			Object[] args = new Object[] { TZ_APP_TPL_ID };
			String isZcSQL = "DELETE FROM  PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID=?";
			int i = jdbcTemplate.update(isZcSQL, args);
			if (i <= 0) {
				errMsg[0] = "1";
				errMsg[1] = "删除模板失败！";
			} else {
				errMsg[0] = "0";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
	}

}
