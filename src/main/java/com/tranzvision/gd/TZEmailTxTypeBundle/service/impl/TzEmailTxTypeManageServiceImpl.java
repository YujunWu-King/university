package com.tranzvision.gd.TZEmailTxTypeBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZEmailTxTypeBundle.service.impl.TzEmailTxTypeManageServiceImpl")
public class TzEmailTxTypeManageServiceImpl extends FrameworkImpl{

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
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_TX_TYPE_ID", "TZ_TX_TYPE_DESC", "TZ_TX_SSLX", "TZ_TX_DESC", "TZ_IS_USED" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("txTypeId", rowList[0]);
					mapList.put("txTypeName", rowList[1]);
					mapList.put("txType", rowList[2]);
					mapList.put("txTypeDesc", rowList[3]);
					mapList.put("isValid", rowList[4]);

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
	
	
	
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// txTypeId;
				String txTypeId = jacksonUtil.getString("txTypeId");
				if (txTypeId != null && !"".equals(txTypeId)) {
					
					String gxSql = "select TZ_TX_RULE_ID FROM PS_TZ_TX_LBTJGX_T WHERE TZ_TX_TYPE_ID=?";
					List<Map<String,Object>> gxList = sqlQuery.queryForList(gxSql, new Object[]{txTypeId});
					if(gxList != null){
						for(Map<String,Object> gxMap: gxList){
							String ruleId = gxMap.get("TZ_TX_RULE_ID") == null? "" 
									: gxMap.get("TZ_TX_RULE_ID").toString();
							
							String delGxSql = "delete from PS_TZ_TX_LBTJGX_T where TZ_TX_TYPE_ID=? and TZ_TX_RULE_ID=?";
							sqlQuery.update(delGxSql, new Object[]{txTypeId, ruleId});
						}
					}
					//删除;
					String deletesql = "DELETE FROM PS_TZ_TX_TYPE_TBL WHERE TZ_TX_TYPE_ID=?";
					sqlQuery.update(deletesql, new Object[]{txTypeId});
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
