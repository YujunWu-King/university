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
import com.tranzvision.gd.TZClueConfDefnBundle.dao.PsTzXsxsZxlbTMapper;
import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsZxlbT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZClueConfDefnBundle.service.impl.TzConsultTypeServiceImpl")
public class TzConsultTypeServiceImpl extends FrameworkImpl {
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
	private PsTzXsxsZxlbTMapper psTzXsxsZxlbTMapper;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_ZXLB_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_ZXLB_ID", "TZ_JG_ID", "TZ_LABEL_NAME", "TZ_LABEL_DESC", "TZ_LABEL_STATUS" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			@SuppressWarnings("unchecked")
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("consultSortID", rowList[0]);
				mapList.put("orgID", rowList[1]);
				mapList.put("consultSortName", rowList[2]);
				mapList.put("consultSortDesc", rowList[3]);
				mapList.put("consultSortStatus", rowList[4]);
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

			if (jacksonUtil.containsKey("consultSortID")) {

				String consultSortID = jacksonUtil.getString("consultSortID");
				PsTzXsxsZxlbT psTzXsxsZxlbT = psTzXsxsZxlbTMapper.selectByPrimaryKey(consultSortID);
				if (psTzXsxsZxlbT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("consultSortID", psTzXsxsZxlbT.getTzZxlbId());
					retMap.put("orgID", psTzXsxsZxlbT.getTzJgId());
					retMap.put("consultSortName", psTzXsxsZxlbT.getTzLabelName());
					retMap.put("consultSortDesc", psTzXsxsZxlbT.getTzLabelDesc());
					retMap.put("consultSortStatus", psTzXsxsZxlbT.getTzLabelStatus());
					returnJsonMap.replace("formData", retMap);
				} else {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("consultSortID", "");
					returnJsonMap.replace("formData", retMap);
					// errMsg[0] = "1";
					// errMsg[1] = "该咨询类别数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该咨询类别数据不存在";
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
		returnJsonMap.put("consultSortID", "");
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
				String reasonID = jacksonUtil.getString("consultSortID");
				String reason = jacksonUtil.getString("consultSortName");
				String description = jacksonUtil.getString("consultSortDesc");
				String status = jacksonUtil.getString("consultSortStatus");
				if ("NEXT".equals(reasonID)) {
					reasonID = String.valueOf(getSeqNum.getSeqNum("PS_TZ_XSXS_ZXLB_T", "TZ_ZXLB_ID"));
					PsTzXsxsZxlbT psTzXsxsZxlbT = new PsTzXsxsZxlbT();
					psTzXsxsZxlbT.setTzZxlbId(reasonID);
					psTzXsxsZxlbT.setTzJgId(orgID);
					psTzXsxsZxlbT.setTzLabelName(reason);
					psTzXsxsZxlbT.setTzLabelDesc(description);
					psTzXsxsZxlbT.setTzLabelStatus(status);

					psTzXsxsZxlbT.setRowAddedDttm(new Date());
					psTzXsxsZxlbT.setRowAddedOprid(oprID);
					psTzXsxsZxlbT.setRowLastmantDttm(new Date());
					psTzXsxsZxlbT.setRowLastmantOprid(oprID);
					int i = psTzXsxsZxlbTMapper.insert(psTzXsxsZxlbT);
					if (i > 0) {
						returnJsonMap.replace("consultSortID", reasonID);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "咨询类别信息保存失败";
					}
				} else {
					String sql = "select COUNT(1) from PS_TZ_XSXS_ZXLB_T WHERE TZ_ZXLB_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { reasonID }, "Integer");
					if (count > 0) {
						PsTzXsxsZxlbT psTzXsxsZxlbT = new PsTzXsxsZxlbT();
						psTzXsxsZxlbT.setTzZxlbId(reasonID);
						psTzXsxsZxlbT.setTzLabelName(reason);
						psTzXsxsZxlbT.setTzLabelDesc(description);
						psTzXsxsZxlbT.setTzLabelStatus(status);

						psTzXsxsZxlbT.setRowLastmantDttm(new Date());
						psTzXsxsZxlbT.setRowLastmantOprid(oprID);
						int i = psTzXsxsZxlbTMapper.updateByPrimaryKeySelective(psTzXsxsZxlbT);
						if (i > 0) {
							returnJsonMap.replace("consultSortID", reasonID);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "咨询类别信息保存失败";
						}
					} else {
						errMsg[0] = "1";
						errMsg[1] = "咨询类别编号不存在！";
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
				String reasonID = "";
				// 内容页信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strUserInfo);

				reasonID = jacksonUtil.getString("consultSortID");

				if (reasonID != null && !"".equals(reasonID)) {

					String deleteSQL = "DELETE FROM PS_TZ_XSXS_ZXLB_T WHERE TZ_ZXLB_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { reasonID });

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
