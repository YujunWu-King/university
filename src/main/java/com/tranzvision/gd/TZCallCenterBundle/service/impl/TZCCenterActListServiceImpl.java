package com.tranzvision.gd.TZCallCenterBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZCallCenterBundle.service.Impl.TZCCenterActListServiceImpl")
public class TZCCenterActListServiceImpl  extends FrameworkImpl {
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
		
		jacksonUtil.json2Map(comParams);
		String strOrgId = jacksonUtil.getString("TZ_JG_ID");
		String strPhone = jacksonUtil.getString("TZ_ZY_SJ");
		String strOprid = jacksonUtil.getString("oprId");
		
		String strActListSQL = "SELECT OPRID,TZ_ZY_SJ,TZ_ART_ID,TZ_NACT_NAME,date_format(TZ_START_DT,'%Y-%m-%d') TZ_START_DT,date_format(TZ_START_TM,'%H:%i') TZ_START_TM,date_format(TZ_END_DT,'%Y-%m-%d') TZ_END_DT,date_format(TZ_END_TM,'%H:%i') TZ_END_TM,TZ_NACT_ADDR FROM PS_TZ_CCALL_HD_VW";
		String strActCountSQL = "SELECT COUNT(1) FROM PS_TZ_CCALL_HD_VW";
		String strWhere = " WHERE TZ_JG_ID='" + strOrgId + "'";
		if(strOprid!=null&&!"".equals(strOprid)&&!"null".equals(strOprid)){
			strWhere = strWhere + " AND (OPRID='" + strOprid + "' OR TZ_ZY_SJ='" + strPhone + "')";
		}else{
			strWhere = strWhere + " AND TZ_ZY_SJ='" + strPhone + "'";
		}
		strWhere = strWhere + " AND TZ_NREG_STAT='1'";
		strActListSQL = strActListSQL + strWhere + " limit " + numStart + "," + numLimit;
		strActCountSQL = strActCountSQL + strWhere;
		
		List<?> actList = sqlQuery.queryForList(strActListSQL,new Object[]{});
		if(actList!=null&&actList.size()>0){
			for(Object sObj:actList){
				Map<String,Object> actMap = (Map<String, Object>) sObj;
				Map<String, Object> mapList = new HashMap<String, Object>();
				String strPar1 = actMap.get("OPRID")==null?"":String.valueOf(actMap.get("OPRID"));
				String strPar2 = actMap.get("TZ_ZY_SJ")==null?"":String.valueOf(actMap.get("TZ_ZY_SJ"));
				String strPar3 = actMap.get("TZ_ART_ID")==null?"":String.valueOf(actMap.get("TZ_ART_ID"));
				String strPar4 = actMap.get("TZ_NACT_NAME")==null?"":String.valueOf(actMap.get("TZ_NACT_NAME"));
				String strPar5 = actMap.get("TZ_START_DT")==null?"":String.valueOf(actMap.get("TZ_START_DT"));
				String strPar6 = actMap.get("TZ_START_TM")==null?"":String.valueOf(actMap.get("TZ_START_TM"));
				String strPar7 = actMap.get("TZ_END_DT")==null?"":String.valueOf(actMap.get("TZ_END_DT"));
				String strPar8 = actMap.get("TZ_END_TM")==null?"":String.valueOf(actMap.get("TZ_END_TM"));
				String strPar9 = actMap.get("TZ_NACT_ADDR")==null?"":String.valueOf(actMap.get("TZ_NACT_ADDR"));
				
				mapList.put("oprid", strPar1);
				mapList.put("artId", strPar3);
				mapList.put("artName", strPar4);
				mapList.put("startDt",strPar5);
				mapList.put("startTime", strPar6);
				mapList.put("endDt", strPar7);
				mapList.put("endTime", strPar8);
				mapList.put("artAddr", strPar9);
				
				listData.add(mapList);
			}
			Integer count = sqlQuery.queryForObject(strActCountSQL, new Object[]{}, "Integer");
			if(count==null){
				count = 0;
			}
			mapRet.replace("total", count);
			mapRet.replace("root", listData);
		}
		/*// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_END_DT", "DESC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "OPRID", "TZ_ZY_SJ","TZ_ART_ID", "TZ_NACT_NAME","TZ_START_DT","TZ_START_TM","TZ_END_DT","TZ_END_TM","TZ_NACT_ADDR" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null){
			
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				//第一个grid
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("oprid", rowList[0]);
				mapList.put("artId", rowList[2]);
				mapList.put("artName", rowList[3]);
				mapList.put("startDt", rowList[4]);
				mapList.put("startTime", rowList[5]);
				mapList.put("endDt", rowList[6]);
				mapList.put("endTime", rowList[7]);
				mapList.put("artAddr", rowList[8]);
			        
				listData.add(mapList);
			}
			
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}*/

		return jacksonUtil.Map2json(mapRet);
	}
}
