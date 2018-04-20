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
import com.tranzvision.gd.TZClueConfDefnBundle.dao.PsTzXsxsDqbqTMapper;
import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsDqbqT;
import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsDqbqTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZClueConfDefnBundle.service.impl.TzAreaLabelServiceImpl")
public class TzAreaLabelServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzXsxsDqbqTMapper psTzXsxsDqbqTMapper;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_LABEL_NAME", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID", "TZ_LABEL_NAME", "TZ_LABEL_DESC",	"TZ_LABEL_STATUS" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			@SuppressWarnings("unchecked")
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("orgID", rowList[0]);
				mapList.put("areaLabelName", rowList[1]);
				mapList.put("areaLabelDesc", rowList[2]);
				mapList.put("areaLabelStatus", rowList[3]);
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
		String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("areaLabelName")) {
				
				String areaLabelName = jacksonUtil.getString("areaLabelName");
				PsTzXsxsDqbqTKey psTzXsxsDqbqTKey = new PsTzXsxsDqbqTKey();
				psTzXsxsDqbqTKey.setTzJgId(orgID);
				psTzXsxsDqbqTKey.setTzLabelName(areaLabelName);
				PsTzXsxsDqbqT psTzXsxsDqbqT = psTzXsxsDqbqTMapper.selectByPrimaryKey(psTzXsxsDqbqTKey);
				if (psTzXsxsDqbqT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("orgID", psTzXsxsDqbqT.getTzJgId());
					retMap.put("areaLabelName", psTzXsxsDqbqT.getTzLabelName());
					retMap.put("areaLabelDesc", psTzXsxsDqbqT.getTzLabelDesc());
					retMap.put("areaLabelStatus", psTzXsxsDqbqT.getTzLabelStatus());
					returnJsonMap.replace("formData",retMap);
				} else {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("areaLabelName", "");
					returnJsonMap.replace("formData",retMap);
					// errMsg[0] = "1";
					// errMsg[1] = "该地区标签数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该地区标签数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// String strRet = "";
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areaLabelName", "");
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
				String areaLabel = jacksonUtil.getString("areaLabelName");
				areaLabel = areaLabel.toUpperCase();
				String description = jacksonUtil.getString("areaLabelDesc");
				String status = jacksonUtil.getString("areaLabelStatus");

				String sql = "select COUNT(1) from PS_TZ_XSXS_DQBQ_T WHERE TZ_LABEL_NAME=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { areaLabel }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "地区码:" + areaLabel + ",已经存在";
				} else {
					PsTzXsxsDqbqT psTzXsxsDqbqT = new PsTzXsxsDqbqT();
					psTzXsxsDqbqT.setTzJgId(orgID);
					psTzXsxsDqbqT.setTzLabelName(areaLabel);
					psTzXsxsDqbqT.setTzLabelDesc(description);
					psTzXsxsDqbqT.setTzLabelStatus(status);
					
					psTzXsxsDqbqT.setRowAddedDttm(new Date());
					psTzXsxsDqbqT.setRowAddedOprid(oprID);
					psTzXsxsDqbqT.setRowLastmantDttm(new Date());
					psTzXsxsDqbqT.setRowLastmantOprid(oprID);
					int i = psTzXsxsDqbqTMapper.insert(psTzXsxsDqbqT);
					if (i > 0) {
						returnJsonMap.replace("areaLabelName", areaLabel);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "地区标签信息保存失败";
					}
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areaLabelName", "");
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
				String areaLabel = jacksonUtil.getString("areaLabelName");
				areaLabel = areaLabel.toUpperCase();
				String description = jacksonUtil.getString("areaLabelDesc");
				String status = jacksonUtil.getString("areaLabelStatus");
				
				String sql = "select COUNT(1) from PS_TZ_XSXS_DQBQ_T WHERE TZ_LABEL_NAME=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { areaLabel }, "Integer");
				if (count > 0) {
					PsTzXsxsDqbqT psTzXsxsDqbqT = new PsTzXsxsDqbqT();
					psTzXsxsDqbqT.setTzJgId(orgID);
					psTzXsxsDqbqT.setTzLabelName(areaLabel);
					psTzXsxsDqbqT.setTzLabelDesc(description);
					psTzXsxsDqbqT.setTzLabelStatus(status);
					
					psTzXsxsDqbqT.setRowLastmantDttm(new Date());
					psTzXsxsDqbqT.setRowLastmantOprid(oprID);
					int i = psTzXsxsDqbqTMapper.updateByPrimaryKeySelective(psTzXsxsDqbqT);
					if (i > 0) {
						returnJsonMap.replace("areaLabelName", areaLabel);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "地区标签信息保存失败";
					}
				} else{
					errMsg[0] = "1";
					errMsg[1] = "地区码:" + areaLabel + "不存在";
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
				String areaLabelName = "";
				// 内容页信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strUserInfo);

				areaLabelName = jacksonUtil.getString("areaLabelName");

				if (areaLabelName != null && !"".equals(areaLabelName)) {

					String deleteSQL = "DELETE FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_LABEL_NAME=?";
					jdbcTemplate.update(deleteSQL, new Object[] { areaLabelName });

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
