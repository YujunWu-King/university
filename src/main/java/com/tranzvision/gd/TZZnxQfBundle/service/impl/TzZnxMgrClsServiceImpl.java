package com.tranzvision.gd.TZZnxQfBundle.service.impl;

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
 * 站内信群发群发管理
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZZnxQfBundle.service.impl.TzZnxMgrClsServiceImpl")
public class TzZnxMgrClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	/* 加载邮件群发批次列表 */
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
		String[][] orderByArr = new String[][] { { "TZ_DSFS_DT", "DESC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_MLSM_QFPC_ID", "TZ_MLSM_QFPC_DESC", "TZ_MAL_SUBJUECT", "TZ_CREPER", "TZ_DSFS_DT", "TZ_JG_ID"};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
				numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("znxQfId", rowList[0]);
				mapList.put("znxQfDesc", rowList[1]);
				mapList.put("znxSubj", rowList[2]);
				mapList.put("crePer", rowList[3]);
				mapList.put("creDt", rowList[4]);
				mapList.put("orgId", rowList[5]);
				
				String readFlag = "0/0";
				String sql = "select sum(TZ_ZNX_STATUS='Y') as TZ_READ_NUM,sum(TZ_ZNX_STATUS='N') as TZ_UNREAD_NUM from PS_TZ_ZNX_REC_T A where exists(select 'X' from PS_TZ_ZNX_MSG_T where TZ_ZNX_MSGID=A.TZ_ZNX_MSGID and TZ_MLSM_QFPC_ID=?)";
				Map<String,Object> readMap = sqlQuery.queryForMap(sql, new Object[]{ rowList[0] });
				if(readMap != null){
					String readNum = readMap.get("TZ_READ_NUM")==null ? "0" : readMap.get("TZ_READ_NUM").toString();
					String unReadNum = readMap.get("TZ_UNREAD_NUM")==null ? "0" : readMap.get("TZ_UNREAD_NUM").toString();
					readFlag = readNum + "/" + unReadNum;
				}
				mapList.put("readFlag", readFlag);
				
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
