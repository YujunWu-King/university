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
import com.tranzvision.gd.TZClueConfDefnBundle.dao.PsTzThyyXsglTMapper;
import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzThyyXsglT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZClueConfDefnBundle.service.impl.TzReturnReasonServiceImpl")
public class TzReturnReasonServiceImpl extends FrameworkImpl {
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
	private PsTzThyyXsglTMapper psTzThyyXsglTMapper;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_THYY_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_THYY_ID", "TZ_JG_ID", "TZ_LABEL_NAME", "TZ_LABEL_DESC",	"TZ_LABEL_STATUS" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			@SuppressWarnings("unchecked")
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("reasonID", rowList[0]);
				mapList.put("orgID", rowList[1]);
				mapList.put("reason", rowList[2]);
				mapList.put("description", rowList[3]);
				mapList.put("status", rowList[4]);
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

			if (jacksonUtil.containsKey("reasonID")) {
				
				String reasonID = jacksonUtil.getString("reasonID");
				PsTzThyyXsglT psTzThyyXsglT = psTzThyyXsglTMapper.selectByPrimaryKey(reasonID);
				if (psTzThyyXsglT != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("reasonID", psTzThyyXsglT.getTzThyyId());
					retMap.put("orgID", psTzThyyXsglT.getTzJgId());
					retMap.put("reason", psTzThyyXsglT.getTzLabelName());
					retMap.put("description", psTzThyyXsglT.getTzLabelDesc());
					retMap.put("status", psTzThyyXsglT.getTzLabelStatus());
					returnJsonMap.replace("formData",retMap);
				} else {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("reasonID", "");
					returnJsonMap.replace("formData",retMap);
					// errMsg[0] = "1";
					// errMsg[1] = "该退回原因数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "该退回原因数据不存在";
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
		returnJsonMap.put("reasonID", "");
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
				String reasonID = jacksonUtil.getString("reasonID");
				String reason = jacksonUtil.getString("reason");
				String description = jacksonUtil.getString("description");
				String status = jacksonUtil.getString("status");
				if ("NEXT".equals(reasonID)) {
					reasonID = String.valueOf(getSeqNum.getSeqNum("PS_TZ_THYY_XSGL_T", "TZ_THYY_ID"));
					PsTzThyyXsglT psTzThyyXsglT = new PsTzThyyXsglT();
					psTzThyyXsglT.setTzThyyId(reasonID);
					psTzThyyXsglT.setTzJgId(orgID);
					psTzThyyXsglT.setTzLabelName(reason);
					psTzThyyXsglT.setTzLabelDesc(description);
					psTzThyyXsglT.setTzLabelStatus(status);
					
					psTzThyyXsglT.setRowAddedDttm(new Date());
					psTzThyyXsglT.setRowAddedOprid(oprID);
					psTzThyyXsglT.setRowLastmantDttm(new Date());
					psTzThyyXsglT.setRowLastmantOprid(oprID);
					int i = psTzThyyXsglTMapper.insert(psTzThyyXsglT);
					if (i > 0) {
						returnJsonMap.replace("reasonID", reasonID);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "退回原因信息保存失败";
					}
				} else{
					String sql = "select COUNT(1) from PS_TZ_THYY_XSGL_T WHERE TZ_THYY_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { reasonID }, "Integer");
					if (count > 0) {
						PsTzThyyXsglT psTzThyyXsglT = new PsTzThyyXsglT();
						psTzThyyXsglT.setTzThyyId(reasonID);
						psTzThyyXsglT.setTzLabelName(reason);
						psTzThyyXsglT.setTzLabelDesc(description);
						psTzThyyXsglT.setTzLabelStatus(status);
						
						psTzThyyXsglT.setRowLastmantDttm(new Date());
						psTzThyyXsglT.setRowLastmantOprid(oprID);
						int i = psTzThyyXsglTMapper.updateByPrimaryKeySelective(psTzThyyXsglT);
						if (i > 0) {
							returnJsonMap.replace("reasonID", reasonID);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "退回原因信息保存失败";
						}
					} else{
						errMsg[0] = "1";
						errMsg[1] = "退回原因编号不存在！";
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

				reasonID = jacksonUtil.getString("reasonID");

				if (reasonID != null && !"".equals(reasonID)) {

					String deleteSQL = "DELETE FROM PS_TZ_THYY_XSGL_T WHERE TZ_THYY_ID=?";
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