package com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterDfnTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldTKey;
import com.tranzvision.gd.util.base.PaseJsonUtil;

import net.sf.json.JSONObject;

/**
 * 可配置搜索
 * @author tang
 * 原PS类
 * TZ_GD_FILTERGL_PKG:TZ_GD_FILTERGL_CLS
 */
@Service("com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl.FilterGlClsServiceImpl")
public class FilterGlClsServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzFilterDfnTMapper psTzFilterDfnTMapper;
	
	/* 查询可配置搜索列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		try {
			FliterForm fliterForm = new FliterForm();

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_COM_ID", "ASC" },{ "TZ_PAGE_ID", "ASC" },{ "TZ_VIEW_NAME", "ASC" } };
			fliterForm.orderByArr = orderByArr;

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_COM_ID", "TZ_COM_MC", "TZ_PAGE_ID", "TZ_PAGE_MC", "TZ_VIEW_NAME" };
			String jsonString = "";

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

			if (obj == null || obj.length == 0) {
				strRet = "{\"total\":0,\"root\":[]}";
			} else {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("ComID", rowList[0]);
					map.put("comMc", rowList[1]);
					map.put("PageID", rowList[2]);
					map.put("pageMc", rowList[3]);
					map.put("ViewMc", rowList[4]);
					
					jsonString = jsonString +","+ JSONObject.fromObject(map).toString();
				}
				
				if (!"".equals(jsonString)) {
					jsonString = jsonString.substring(1);
				}

				strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
	
	@Override
	/* 删除可配置搜索 */
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 信息内容;
				String str_com_id = CLASSJson.getString("ComID");
				String str_page_id = CLASSJson.getString("PageID");
				String str_view_name = CLASSJson.getString("ViewMc");
				
				PsTzFilterDfnTKey psTzFilterDfnTKey = new PsTzFilterDfnTKey();
				psTzFilterDfnTKey.setTzComId(str_com_id);
				psTzFilterDfnTKey.setTzPageId(str_page_id);
				psTzFilterDfnTKey.setTzViewName(str_view_name);
				psTzFilterDfnTMapper.deleteByPrimaryKey(psTzFilterDfnTKey);

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
