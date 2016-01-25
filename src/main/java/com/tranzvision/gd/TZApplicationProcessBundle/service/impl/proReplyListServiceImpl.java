package com.tranzvision.gd.TZApplicationProcessBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：报名流程详情类;
 * 原PS类：TZ_GD_PROTMPLATE_PKG:TTZ_GD_PROBACK_CLS
 */
@Service("com.tranzvision.gd.TZApplicationProcessBundle.service.impl.proReplyListServiceImpl")
public class proReplyListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	 
	/* 查询报名流程列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);
		/*模版编号*/
		String strAppProcessTmpId = jacksonUtil.getString("TZ_APPPRO_TMP_ID");
		/*流程编号*/
		String strAppProcessId = jacksonUtil.getString("TZ_APPPRO_ID");
		
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try{
			int total = 0;
			// 查询总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_APPPRO_HF_T WHERE TZ_APPPRO_TMP_ID=? AND TZ_APPPRO_ID=?";
			total = sqlQuery.queryForObject(totalSQL, new Object[] { strAppProcessTmpId,strAppProcessId },"Integer");
			String sql = "SELECT TZ_APPPRO_HF_BH,TZ_APPPRO_COLOR,TZ_CLS_RESULT,TZ_WFB_DEFALT_BZ FROM PS_TZ_APPPRO_HF_T WHERE TZ_APPPRO_TMP_ID= ? AND TZ_APPPRO_ID= ? LIMIT ?,?";
			List<?> listData = sqlQuery.queryForList(sql, new Object[] { strAppProcessTmpId,strAppProcessId,numStart,numLimit });
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strMsgId = "";
				String strMsgName = "";
				String strMsgColor = "";
				String strDefaultFlag = "";
				
				strMsgId = String.valueOf(mapData.get("TZ_APPPRO_HF_BH"));
				strMsgName = String.valueOf(mapData.get("TZ_CLS_RESULT"));
				strMsgColor = String.valueOf(mapData.get("TZ_APPPRO_COLOR"));
				strDefaultFlag = String.valueOf(mapData.get("TZ_WFB_DEFALT_BZ"));
				
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("TZ_APPPRO_TMP_ID", strAppProcessTmpId);
				mapJson.put("TZ_APPPRO_ID", strAppProcessId);
				mapJson.put("TZ_APPPRO_HF_BH", strMsgId);
				mapJson.put("TZ_APPPRO_COLOR", strMsgColor);
				mapJson.put("TZ_CLS_RESULT", strMsgName);
				mapJson.put("TZ_WFB_DEFALT_BZ", strDefaultFlag);
				
				listJson.add(mapJson);
				mapRet.replace("total",total);
			}
			mapRet.replace("root", listJson);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
 
		return jacksonUtil.Map2json(mapRet);
	}
}
