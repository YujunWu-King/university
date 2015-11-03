package com.tranzvision.gd.TZThemeMgBundle.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZThemeMgBundle.dao.PsTzPtZtxxTblMapper;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

import net.sf.json.JSONObject;

@Service("com.tranzvision.gd.TZThemeMgBundle.service.impl.ThemeGlServiceImpl")
public class ThemeGlServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzPtZtxxTblMapper psTzPtZtxxTblMapper;
	
	/* 加载主题列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		FliterForm fliterForm = new FliterForm();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_ZT_ID", "ASC" } };
		fliterForm.orderByArr = orderByArr;

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_ZT_ID", "TZ_ZT_MC", "TZ_ZT_MS", "TZ_DESCR30" };
		String jsonString = "";

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

		if (obj == null || obj.length == 0) {
			strRet = "{\"total\":0,\"root\":[]}";
		} else {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				jsonString = jsonString + ",{\"themeID\":\"" + rowList[0] + "\",\"themeName\":\"" + rowList[1]
						+ "\",\"themeDesc\":\"" + rowList[2] + "\",\"themeState\":\"" + rowList[3] + "\"}";
			}
			if (!"".equals(jsonString)) {
				jsonString = jsonString.substring(1);
			}

			strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
		}

		return strRet;
	}

	// 功能说明：删除主题定义;
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];

				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 主题 ID;
				String strThemeId = CLASSJson.getString("themeID");
				if (strThemeId != null && !"".equals(strThemeId)) {
					psTzPtZtxxTblMapper.deleteByPrimaryKey(strThemeId);
					String sql = "DELETE from PS_TZ_PT_ZTZY_TBL WHERE TZ_ZT_ID=?";
					jdbcTemplate.update(sql,new Object[]{strThemeId});
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
