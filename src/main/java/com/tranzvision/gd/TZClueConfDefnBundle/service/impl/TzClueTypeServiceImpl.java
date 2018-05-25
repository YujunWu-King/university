package com.tranzvision.gd.TZClueConfDefnBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClueConfDefnBundle.dao.PsTzXsxsXslbTMapper;
import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsXslbT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZClueConfDefnBundle.service.impl.TzClueTypeServiceImpl")
public class TzClueTypeServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzXsxsXslbTMapper psTzXsxsXslbTMapper;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_COLOUR_SORT_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_COLOUR_SORT_ID", "TZ_JG_ID", "TZ_COLOUR_NAME", "TZ_COLOUR_CODE",
				"TZ_COLOR_STATUS" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			@SuppressWarnings("unchecked")
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("clueSortID", rowList[0]);
				mapList.put("orgID", rowList[1]);
				mapList.put("clueSortName", rowList[2]);
				mapList.put("clueSortCode", rowList[3]);
				mapList.put("clueSortStatus", rowList[4]);
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
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("clueSortID")) {
				
				String clueSortID = jacksonUtil.getString("clueSortID");
				PsTzXsxsXslbT psTzXsxsXslbT = psTzXsxsXslbTMapper.selectByPrimaryKey(clueSortID);
				if (psTzXsxsXslbT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("clueSortID", psTzXsxsXslbT.getTzColourSortId());
					retMap.put("orgID", psTzXsxsXslbT.getTzJgId());
					retMap.put("clueSortName", psTzXsxsXslbT.getTzColourName());
					retMap.put("clueSortCode", psTzXsxsXslbT.getTzColourCode());
					retMap.put("clueSortStatus", psTzXsxsXslbT.getTzColorStatus());
					returnJsonMap.replace("formData",retMap);
				} else {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("clueSortID", "");
					returnJsonMap.replace("formData",retMap);
					// errMsg[0] = "1";
					// errMsg[1] = "该线索类别数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该线索类别数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("clueSortID", "");
		String oprID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				// Map<String, Object> infoData = jacksonUtil.getMap("data");
				// String strJugTypeId = (String) infoData.get("jugTypeId");
				String clueSortID = jacksonUtil.getString("clueSortID");
				String clueSortName = jacksonUtil.getString("clueSortName");
				String clueSortCode = jacksonUtil.getString("clueSortCode");
				String clueSortStatus = jacksonUtil.getString("clueSortStatus");
				if ("NEXT".equals(clueSortID)) {
					clueSortID = String.valueOf(getSeqNum.getSeqNum("PS_TZ_XSXS_XSLB_T", "TZ_COLOUR_SORT_ID"));
					PsTzXsxsXslbT psTzXsxsXslbT = new PsTzXsxsXslbT();
					psTzXsxsXslbT.setTzColourSortId(clueSortID);
					psTzXsxsXslbT.setTzJgId(orgID);
					psTzXsxsXslbT.setTzColourName(clueSortName);
					psTzXsxsXslbT.setTzColourCode(clueSortCode);
					psTzXsxsXslbT.setTzColorStatus(clueSortStatus);
					
					psTzXsxsXslbT.setRowAddedDttm(new Date());
					psTzXsxsXslbT.setRowAddedOprid(oprID);
					psTzXsxsXslbT.setRowLastmantDttm(new Date());
					psTzXsxsXslbT.setRowLastmantOprid(oprID);
					int i = psTzXsxsXslbTMapper.insert(psTzXsxsXslbT);
					if (i > 0) {
						returnJsonMap.replace("clueSortID", clueSortID);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "线索类别信息保存失败";
					}
				} else{
					String sql = "select COUNT(1) from PS_TZ_XSXS_XSLB_T WHERE TZ_COLOUR_SORT_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { clueSortID }, "Integer");
					if (count > 0) {
						PsTzXsxsXslbT psTzXsxsXslbT = new PsTzXsxsXslbT();
						psTzXsxsXslbT.setTzColourSortId(clueSortID);
						psTzXsxsXslbT.setTzColourName(clueSortName);
						psTzXsxsXslbT.setTzColourCode(clueSortCode);
						psTzXsxsXslbT.setTzColorStatus(clueSortStatus);
						
						psTzXsxsXslbT.setRowLastmantDttm(new Date());
						psTzXsxsXslbT.setRowLastmantOprid(oprID);
						int i = psTzXsxsXslbTMapper.updateByPrimaryKeySelective(psTzXsxsXslbT);
						if (i > 0) {
							returnJsonMap.replace("clueSortID", clueSortID);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "线索类别信息保存失败";
						}
					} else{
						errMsg[0] = "1";
						errMsg[1] = "线索类别编号不存在！";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
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
				String clueSortID = "";
				// 内容页信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strUserInfo);

				clueSortID = jacksonUtil.getString("clueSortID");

				if (clueSortID != null && !"".equals(clueSortID)) {

					String deleteSQL = "DELETE FROM PS_TZ_XSXS_XSLB_T WHERE TZ_COLOUR_SORT_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { clueSortID });

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
