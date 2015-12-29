package com.tranzvision.gd.TZThemeMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZThemeMgBundle.dao.PsTzPtZtxxTblMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang 功能说明：高端产品-主题管理 原PS类：TZ_GD_THEMEGL_PKG：TZ_GD_THEMEGL_CLS
 */
@Service("com.tranzvision.gd.TZThemeMgBundle.service.impl.ThemeGlServiceImpl")
public class ThemeGlServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzPtZtxxTblMapper psTzPtZtxxTblMapper;
	@Autowired
	private FliterForm fliterForm;

	/* 加载主题列表 */
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
			String[][] orderByArr = new String[][] { { "TZ_ZT_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_ZT_ID", "TZ_ZT_MC", "TZ_ZT_MS", "TZ_DESCR30" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("themeID", rowList[0]);
					mapList.put("themeName", rowList[1]);
					mapList.put("themeDesc", rowList[2]);
					mapList.put("themeState", rowList[3]);

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

	// 功能说明：删除主题定义;
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
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 主题 ID;
				String strThemeId = jacksonUtil.getString("themeID");
				if (strThemeId != null && !"".equals(strThemeId)) {
					psTzPtZtxxTblMapper.deleteByPrimaryKey(strThemeId);
					String sql = "DELETE from PS_TZ_PT_ZTZY_TBL WHERE TZ_ZT_ID=?";
					jdbcTemplate.update(sql, new Object[] { strThemeId });
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
