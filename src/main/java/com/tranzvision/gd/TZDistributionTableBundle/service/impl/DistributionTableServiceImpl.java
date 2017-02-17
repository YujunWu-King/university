package com.tranzvision.gd.TZDistributionTableBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZDistributionTableBundle.dao.PsTzFbdzTblMapper;
import com.tranzvision.gd.TZDistributionTableBundle.model.PsTzFbdzTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZDistributionTableBundle.service.impl.DistributionTableServiceImpl")
public class DistributionTableServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private PsTzFbdzTblMapper psTzFbdzTblMapper;

	/**
	 * 分布对照表管理列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID", "TZ_M_FBDZ_ID", "TZ_M_FBDZ_NAME" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("orgId", rowList[0]);
				mapList.put("distrId", rowList[1]);
				mapList.put("distrName", rowList[2]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("distrId")) {
				// 分布对照表编号;
				String distrId = jacksonUtil.getString("distrId");
				PsTzFbdzTbl psTzFbdzTbl = psTzFbdzTblMapper.selectByPrimaryKey(distrId);
				if (psTzFbdzTbl != null) {
					
					returnJsonMap.put("distrId", psTzFbdzTbl.getTzMFbdzId());
					returnJsonMap.put("distrName", psTzFbdzTbl.getTzMFbdzName());
					returnJsonMap.put("status", psTzFbdzTbl.getTzMFbdzZt());
					
				} else {
					errMsg[0] = "1";
					errMsg[1] = "数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}


	/*删除*/
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
				String distrId = "";
				// 内容页信息;
				String strInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strInfo);
				// 获取分布对照表编号
				distrId = jacksonUtil.getString("distrId");
				
			    if(distrId != null && !"".equals(distrId)){
			    	
			    	String deleteSQL = "DELETE FROM PS_TZ_FBDZ_TBL WHERE TZ_M_FBDZ_ID=?";
			    	jdbcTemplate.update(deleteSQL, new Object[]{distrId});
			    	
			    	String deleteSQLMX = "DELETE FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=?";
			    	jdbcTemplate.update(deleteSQLMX, new Object[]{distrId});
			    	
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
