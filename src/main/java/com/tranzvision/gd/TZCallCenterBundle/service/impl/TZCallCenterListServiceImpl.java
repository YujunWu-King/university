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
		String[] resultFldArray = { "TZ_XH", "TZ_CALL_TYPE", "TZ_PHONE","TZ_CALL_DTIME","TZ_DEALWITH_ZT","TZ_DESCR","TZ_LOGIN_NAME","TZ_STU_NAME","TZ_MSH_ID" };

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
				mapList.put("jtPsnName", rowList[6]);
				mapList.put("callName", rowList[7]);
				mapList.put("callMshId", rowList[8]);
				
				String strCallOprid = sqlQuery.queryForObject("SELECT TZ_OPRID FROM PS_TZ_PH_JDD_TBL WHERE TZ_XH=?", new Object[]{rowList[0]}, "String");
				mapList.put("callOprid", strCallOprid);
				//历史来电数量
				String sql = "SELECT COUNT(1) FROM PS_TZ_PH_JDD_TBL WHERE TZ_PHONE=? AND TZ_XH<>?";
				String callCount = sqlQuery.queryForObject(sql, new Object[]{rowList[2],rowList[0]}, "String");
				mapList.put("viewHistoryCall", callCount);
				
				//报名活动数
				String strActCountSQL = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_G_V WHERE TZ_ZY_SJ=? AND TZ_NREG_STAT='1'";
				Integer actCount = sqlQuery.queryForObject(strActCountSQL, new Object[]{rowList[2]}, "Integer");
				if(actCount==null){
					actCount = 0;
				}
				mapList.put("bmrBmActCount", String.valueOf(actCount));
				
				
				listData.add(mapList);
			}
			
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
