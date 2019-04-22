package com.tranzvision.gd.TZViewByProBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * ClassName: ViewByProServiceImpl
 * @author 陈斯  zhongcg迁移
 * @version 1.0 
 * Create Time: 2018年11月28日 上午11:24:09 
 * Description: 按项目查看
 */
@Service("com.tranzvision.gd.TZViewByProBundle.service.impl.ViewByProServiceImpl")
public class ViewByProServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	
	
	/**
	 * 
	* Description:加载项目列表
	* Create Time: 2018年11月28日 上午11:24:42
	* @author 陈斯 
	* @param comParams
	* @param numLimit
	* @param numStart
	* @param errorMsg
	* @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_PRJ_ID", "TZ_PRJ_NAME", "TZ_PRJ_DESC", "TZ_PRJ_TYPE_NAME", "TZ_IS_OPEN",
				"TZ_ISOPEN_DESC" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("projectId", rowList[0]);
				mapList.put("projectName", rowList[1]);
				mapList.put("projectDesc", rowList[2]);
				mapList.put("projectType", rowList[3]);
				mapList.put("usedStatus", rowList[4]);
				mapList.put("statusDesc", rowList[5]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	

}
