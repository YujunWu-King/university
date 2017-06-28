package com.tranzvision.gd.TZCallCenterBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZCallCenterBundle.service.Impl.TZCallCenterListServiceImpl")
public class TZCallCenterListServiceImpl  extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
	/* 系统变量列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String strTransSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_EFF_STATUS='A' AND TZ_ZHZ_ID=?";
		
		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_CALL_DTIME", "DESC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_XH", "TZ_CALL_TYPE", "TZ_PHONE","TZ_CALL_DTIME","TZ_DEALWITH_ZT","TZ_DESCR" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null){
			
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				//第一个grid
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("receiveId", rowList[0]);
				mapList.put("callType", rowList[1]);
				mapList.put("callPhone", rowList[2]);
				mapList.put("callDTime", rowList[3]);
				mapList.put("dealWithZT", rowList[4]);
				mapList.put("callDesc", rowList[5]);
			        
				listData.add(mapList);
			}
			
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
