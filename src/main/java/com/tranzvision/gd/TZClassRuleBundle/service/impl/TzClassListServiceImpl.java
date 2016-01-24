package com.tranzvision.gd.TZClassRuleBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassRuleBundle.dao.PsTzClsHcgzcsTMapper;
import com.tranzvision.gd.TZClassRuleBundle.model.PsTzClsHcgzcsTKey;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：搜索班级列表;
 * 原PS类：TZ_GD_CLSHCGZ_PKG:TZ_GD_CLASSLIST_CLS
 */
@Service("com.tranzvision.gd.TZClassRuleBundle.service.impl.TzClassListServiceImpl")
public class TzClassListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzClsHcgzcsTMapper PsTzClsHcgzcsTMapper;
	/* 查询班级列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try{
			int total = 0;
			// 查询总数;
			String totalSQL = "SELECT COUNT(TZ_CLASS_ID) FROM PS_TZ_CLASS_INF_T WHERE TZ_JG_ID=?";
			total = jdbcTemplate.queryForObject(totalSQL, new Object[] { orgid },"Integer");
			String sql = "SELECT cast(TZ_CLASS_ID as unsigned int) TZ_CLASS_ID,TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T WHERE TZ_JG_ID= ? ORDER BY cast(TZ_CLASS_ID as unsigned int) DESC";
			List<?> listData = jdbcTemplate.queryForList(sql, new Object[] { orgid });
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;
				String strClassId = "";
				String strClassName = "";

				strClassId = String.valueOf(mapData.get("TZ_CLASS_ID"));
				strClassName = String.valueOf(mapData.get("TZ_CLASS_NAME"));

				Map<String, Object> mapJson = new HashMap<String, Object>();
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
	 * 添加班级到列表
	 * 
	 * @param oprType
	 * @param comParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public String tzOther(String oprType,String comParams, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			
			if("addClasses".equals(oprType))
			{
				String strHcgzId = jacksonUtil.getString("hcgzId");
				List<?> listData = jacksonUtil.getList("add");
				
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
				
					String strClassId = "";
					strClassId = String.valueOf(mapData.get("hcgzClassId"));
					/*规则中是否存在该班级*/
					String sql = "SELECT count(1) FROM PS_TZ_CLS_HCGZCS_T WHERE TZ_CLS_HCGZ_ID= ? AND TZ_CLASS_ID = ?";
					
					int isExistClassNum = jdbcTemplate.queryForObject(sql, new Object[] { strHcgzId,strClassId }, "Integer");
					
					if (isExistClassNum == 0) {
						PsTzClsHcgzcsTKey psTzClsHcgzcsTKey = new PsTzClsHcgzcsTKey();
						psTzClsHcgzcsTKey.setTzClsHcgzId(strHcgzId);
						psTzClsHcgzcsTKey.setTzClassId(strClassId);
						PsTzClsHcgzcsTMapper.insert(psTzClsHcgzcsTKey);
					}
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
