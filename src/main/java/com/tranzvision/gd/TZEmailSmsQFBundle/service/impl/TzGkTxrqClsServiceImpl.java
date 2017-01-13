package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 功能描述：EDM统计-查看收件人
 * TZ_GK_EDM_PKG:TZ_GK_TXRQ_CLS
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzGkTxrqClsServiceImpl")
public class TzGkTxrqClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery jdbcTemplate;


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
		String[] resultFldArray = {"TZ_EDM_TIME", "PRCSINSTANCE", "RUNSTATUS" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("txAETime", rowList[0]);
				mapList.put("AEId", rowList[1]);
				mapList.put("AEState", rowList[2]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		// 邮件群发批次编号;
		String strEmailID = jacksonUtil.getString("emlQfId");
		String RUNSTATUS = "";
		try{
			int PRCSINSTANCE = jdbcTemplate.queryForObject("select PRCSINSTANCE from PS_TZ_YJQFTXRZ_T where TZ_TXAE_DTTM in(select max(TZ_TXAE_DTTM) from PS_TZ_YJQFTXRZ_T where TZ_MLSM_QFPC_ID=?)", new Object[]{strEmailID},"Integer");
			RUNSTATUS = jdbcTemplate.queryForObject("select RUNSTATUS from PSPRCSRQST where PRCSINSTANCE=?", new Object[]{PRCSINSTANCE},"String");
		}catch(Exception e){
			RUNSTATUS = "";
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("runStatus",RUNSTATUS);
		return jacksonUtil.Map2json(map);
	}
}
