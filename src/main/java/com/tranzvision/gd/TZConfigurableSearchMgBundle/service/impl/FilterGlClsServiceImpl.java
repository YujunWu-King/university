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
import com.tranzvision.gd.util.base.JacksonUtil;

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
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询可配置搜索列表 */
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
			String[][] orderByArr = new String[][] { { "TZ_COM_ID", "ASC" },{ "TZ_PAGE_ID", "ASC" },{ "TZ_VIEW_NAME", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_COM_ID", "TZ_COM_MC", "TZ_PAGE_ID", "TZ_PAGE_MC", "TZ_VIEW_NAME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null ){
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ComID", rowList[0]);
					map.put("comMc", rowList[1]);
					map.put("PageID", rowList[2]);
					map.put("pageMc", rowList[3]);
					map.put("ViewMc", rowList[4]);
					listData.add(map);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	@Override
	/* 删除可配置搜索 */
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				
				// 信息内容;
				String str_com_id = jacksonUtil.getString("ComID");
				String str_page_id = jacksonUtil.getString("PageID");
				String str_view_name = jacksonUtil.getString("ViewMc");
				
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
