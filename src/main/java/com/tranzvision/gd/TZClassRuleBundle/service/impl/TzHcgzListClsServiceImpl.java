package com.tranzvision.gd.TZClassRuleBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassRuleBundle.dao.PsTzClsHcgzTMapper;
import com.tranzvision.gd.TZClassRuleBundle.model.PsTzClsHcgzT;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：班级互斥规则列表;
 * 原PS类：TZ_GD_CLSHCGZ_PKG:TZ_GD_HCGZLIST_CLS
 */
@Service("com.tranzvision.gd.TZClassRuleBundle.service.impl.TzHcgzListClsServiceImpl")
public class TzHcgzListClsServiceImpl extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzClsHcgzTMapper PsTzClsHcgzTMapper;
	/* 查询项目分类列表 */
	@Override
	@SuppressWarnings("unchecked")
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
		String[] resultFldArray = { "TZ_CLS_HCGZ_ID", "TZ_CLS_HCGZ_NAME" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("hcgzId", rowList[0]);
				mapList.put("hcgzName", rowList[1]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/**
	 * 新建项目
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strHcgzId = jacksonUtil.getString("hcgzId");
				String strHcgzName = jacksonUtil.getString("hcgzName");
				
				if("".equals(strHcgzId) ||  "NEXT".equals(strHcgzId.toUpperCase())){
					strHcgzId = String.valueOf(getSeqNum.getSeqNum("PS_TZ_CLS_HCGZ_T", "TZ_CLS_HCGZ_ID"));
					PsTzClsHcgzT psTzClsHcgzT = new PsTzClsHcgzT();
					psTzClsHcgzT.setTzClsHcgzId(strHcgzId);
					psTzClsHcgzT.setTzClsHcgzName(strHcgzName);
					psTzClsHcgzT.setTzJgId(orgid);
					PsTzClsHcgzTMapper.insert(psTzClsHcgzT);
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
	/**
	 * 删除互斥规则、互斥规则班级列表信息
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
				
				if (strHcgzId != null && !"".equals(strHcgzId)) {
					
					Object[] args = new Object[] { strHcgzId };
					jdbcTemplate.update("delete from PS_TZ_CLS_HCGZ_T where TZ_CLS_HCGZ_ID = ?", args);
					jdbcTemplate.update("delete from PS_TZ_CLS_HCGZCS_T where TZ_CLS_HCGZ_ID = ?", args);
					
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
