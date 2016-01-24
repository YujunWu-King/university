package com.tranzvision.gd.TZClassRuleBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassRuleBundle.dao.PsTzClsHcgzcsTMapper;
import com.tranzvision.gd.TZClassRuleBundle.model.PsTzClsHcgzcsTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：班级互斥信息列表;
 * 原PS类：TZ_GD_CLSHCGZ_PKG:TZ_GD_HCGZXQ_CLS
 */
@Service("com.tranzvision.gd.TZClassRuleBundle.service.impl.TzHcgzInfoClsServiceImpl")
public class TzHcgzInfoClsServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzClsHcgzcsTMapper PsTzClsHcgzcsTMapper;
	/* 查询项目分类列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);

		String strHcgzId = jacksonUtil.getString("hcgzId");
		
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try{
			int total = 0;
			// 查询总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_CLS_HCGZCS_T WHERE TZ_CLS_HCGZ_ID= ?";
			total = jdbcTemplate.queryForObject(totalSQL, new Object[] { strHcgzId },"Integer");
			String sql = "SELECT cast(A.TZ_CLASS_ID as unsigned int) TZ_CLASS_ID,B.TZ_CLASS_NAME FROM PS_TZ_CLS_HCGZCS_T A,PS_TZ_CLASS_INF_T B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_CLS_HCGZ_ID= ? ORDER BY TZ_CLASS_ID";
			List<?> listData = jdbcTemplate.queryForList(sql, new Object[] { strHcgzId });
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strClassId = "";
				String strClassName = "";

				strClassId = String.valueOf(mapData.get("TZ_CLASS_ID"));
				strClassName = String.valueOf(mapData.get("TZ_CLASS_NAME"));

				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("hcgzId", strHcgzId);
				mapJson.put("hcgzClassId", strClassId);
				mapJson.put("hcgzClassName", strClassName);

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
	
	/**
	 * 删除班级列表信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strHcgzId = jacksonUtil.getString("hcgzId");
				String strhcgzClassId = jacksonUtil.getString("hcgzClassId");
				
				
				if (strHcgzId != null && !"".equals(strHcgzId) && strhcgzClassId != null && !"".equals(strhcgzClassId)) {
					
					PsTzClsHcgzcsTKey psTzClsHcgzcsTKey = new PsTzClsHcgzcsTKey();
					psTzClsHcgzcsTKey.setTzClsHcgzId(strHcgzId);
					psTzClsHcgzcsTKey.setTzClassId(strhcgzClassId);
					PsTzClsHcgzcsTMapper.deleteByPrimaryKey(psTzClsHcgzcsTKey);
					
				}
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("success", "success");
				strRet = jacksonUtil.Map2json(mapJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}
