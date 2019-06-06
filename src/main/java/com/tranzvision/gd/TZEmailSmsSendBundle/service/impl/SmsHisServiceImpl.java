package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 查询短信发送历史 	原PS：TZ_GD_COM_EMLSMS_APP:smsHis
 * @author zhanglang
 * 2017-02-08
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SmsHisServiceImpl")
public class SmsHisServiceImpl extends FrameworkImpl {
	//@Autowired
	//private FliterForm fliterForm;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {	
			jacksonUtil.json2Map(strParams);
			String jgId = "";
			String tmpId = "";
			if (jacksonUtil.containsKey("condition")) {
				Map<String, Object> conditionJson = jacksonUtil.getMap("condition");
				jgId = conditionJson.get("TZ_JG_ID-value") == null ? "" : String.valueOf(conditionJson.get("TZ_JG_ID-value"));
				tmpId = conditionJson.get("TZ_TMPL_ID-value") == null ? "" : String.valueOf(conditionJson.get("TZ_TMPL_ID-value"));
			}
			String sql = tzGDObject.getSQLText("SQL.TZEmailSmsSendBundle.TzSmsHisTjTotal");
			int total = jdbcTemplate.queryForObject(sql,new Object[]{jgId,tmpId},"Integer");
			
			sql = tzGDObject.getSQLText("SQL.TZEmailSmsSendBundle.TzSmsHisTj");
			List<Map<String ,Object>> list = null;
			list = jdbcTemplate.queryForList(sql,new Object[]{jgId,tmpId,numStart,numLimit});
			
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size();i++){
					Map<String, Object> map = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("status", map.get("TZ_RWZX_ZT_DESC"));
					mapList.put("sendNum", map.get("TZ_SEND_COUNT"));
					mapList.put("sendSucNum", map.get("TZ_SEND_SUC_COUNT"));
					mapList.put("sendFailNum", map.get("TZ_SEND_FAIL_COUNT"));
					mapList.put("sendDt", map.get("TZ_RWZX_DT_STR"));
					mapList.put("sendRptNum", map.get("TZ_SEND_RPT_COUNT"));
					mapList.put("operator", map.get("TZ_REALNAME"));
					
					listData.add(mapList);
				}
			}
			
			mapRet.replace("total", total);
			mapRet.replace("root", listData);
			/*
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{"TZ_RWZX_DT_STR", "DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_RWZX_ZT_DESC", "TZ_SEND_COUNT", "TZ_SEND_SUC_COUNT", "TZ_SEND_FAIL_COUNT", "TZ_RWZX_DT_STR", "TZ_SEND_RPT_COUNT", "TZ_REALNAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("status", rowList[0]);
					mapList.put("sendNum", rowList[1]);
					mapList.put("sendSucNum", rowList[2]);
					mapList.put("sendFailNum", rowList[3]);
					mapList.put("sendDt", rowList[4]);
					mapList.put("sendRptNum", rowList[5]);
					mapList.put("operator", rowList[6]);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
