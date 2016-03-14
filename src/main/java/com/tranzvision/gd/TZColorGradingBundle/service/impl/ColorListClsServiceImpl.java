package com.tranzvision.gd.TZColorGradingBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZColorGradingBundle.dao.PsTzColorSortTMapper;
import com.tranzvision.gd.TZColorGradingBundle.model.PsTzColorSortT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_GD_BMGL_COLOR_PKG:TZ_GD_COLOR_LIST_CLS
 * 颜色管理
 *
 */
@Service("com.tranzvision.gd.TZColorGradingBundle.service.impl.ColorListClsServiceImpl")
public class ColorListClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzColorSortTMapper psTzColorSortTMapper;
	/**
	 * 颜色展示 列表显示
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "{}";
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if(StringUtils.isBlank(orgId)){
			errorMsg[0] = "1";
			errorMsg[1] = "您不属于任何机构！";
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		try {
			
			//颜色列表SQL
			String sql = "";
			//查询结果
			List<?> resultlist = null;
			
			if(numLimit == 0){
				sql = "SELECT TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE FROM PS_TZ_COLOR_SORT_T WHERE TZ_JG_ID = ?  AND TZ_COLOR_STATUS <> 'N' ORDER BY TZ_COLOR_SORT_ID";
				resultlist = sqlQuery.queryForList(sql, new Object[] { orgId });
			}else{
				sql = "SELECT TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE FROM PS_TZ_COLOR_SORT_T WHERE TZ_JG_ID = ? AND TZ_COLOR_STATUS <> 'N' ORDER BY TZ_COLOR_SORT_ID LIMIT ?,?";
				resultlist = sqlQuery.queryForList(sql, new Object[] { orgId, numStart, numStart + numLimit });
			}
			
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String colorSortID = result.get("TZ_COLOR_SORT_ID") == null ? "" : String.valueOf(result.get("TZ_COLOR_SORT_ID"));
				String colorName = result.get("TZ_COLOR_NAME") == null ? "" : String.valueOf(result.get("TZ_COLOR_NAME"));
				String colorCode = result.get("TZ_COLOR_CODE") == null ? "" : String.valueOf(result.get("TZ_COLOR_CODE"));

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("colorSortID", colorSortID);
				mapRetJson.put("colorName", colorName);
				mapRetJson.put("colorCode", colorCode);
				
				listData.add(mapRetJson);
			}
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_COLOR_SORT_T WHERE TZ_JG_ID = ? AND TZ_COLOR_STATUS <> 'N'";
			int numTotal = sqlQuery.queryForObject(totalSQL, new Object[] { orgId}, "Integer");
			
			mapRet.replace("total", numTotal);
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}

		return jacksonUtil.Map2json(mapRet);

	}
	
	
	/**
	 * 新增或修改颜色类别信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if(StringUtils.isBlank(orgId)){
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能添加颜色类别。";
			return strRet;
		}
		
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			Date dateNow = new Date();
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> infoData = jacksonUtil.getMap("data");
				String colorSortID = (String)infoData.get("colorSortID");
				String colorName = (String)infoData.get("colorName");
				String colorCode = (String)infoData.get("colorCode");
		
				if(StringUtils.equals(colorSortID, "NEXT")){
					String sql = "SELECT COUNT(1) FROM PS_TZ_COLOR_SORT_T WHERE TZ_JG_ID = ? AND (TZ_COLOR_NAME = ? OR TZ_COLOR_CODE = ?) AND TZ_COLOR_STATUS <> 'N'";
					int count = sqlQuery.queryForObject(sql, new Object[] { orgId,colorName,colorCode }, "Integer");

					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前部门存在有相同名称或码制的颜色定义。";
						return strRet;
					}else{
						PsTzColorSortT psTzColorSortT = new PsTzColorSortT();
						colorSortID = "" + getSeqNum.getSeqNum("TZ_COLOR_SORT_T", "TZ_COLOR_SORT_ID");
						StringUtils.leftPad(colorSortID,5,'0');
						psTzColorSortT.setTzColorSortId(colorSortID);
						psTzColorSortT.setTzColorName(colorName);
						psTzColorSortT.setTzColorCode(colorCode);
						psTzColorSortT.setTzColorStatus("Y");
						psTzColorSortT.setTzJgId(orgId);
						psTzColorSortT.setRowAddedDttm(dateNow);
						psTzColorSortT.setRowAddedOprid(oprid);
						psTzColorSortT.setRowLastmantDttm(dateNow);
						psTzColorSortT.setRowLastmantOprid(oprid);
						
						psTzColorSortTMapper.insert(psTzColorSortT);
					}
				}else{
					PsTzColorSortT psTzColorSortT = new PsTzColorSortT();
					psTzColorSortT.setTzColorSortId(colorSortID);
					psTzColorSortT.setTzColorName(colorName);
					psTzColorSortT.setTzColorCode(colorCode);
					psTzColorSortT.setRowLastmantDttm(dateNow);
					psTzColorSortT.setRowLastmantOprid(oprid);
					
					psTzColorSortTMapper.updateByPrimaryKeySelective(psTzColorSortT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/***
	 * 删除颜色类别信息
	 */
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
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 控件id
				String colorSortID = jacksonUtil.getString("colorSortID");
				
				if(StringUtils.isNotBlank(colorSortID) && !StringUtils.equals(colorSortID, "NEXT")){
					sqlQuery.update("UPDATE PS_TZ_COLOR_SORT_T SET TZ_COLOR_STATUS='N' WHERE TZ_COLOR_SORT_ID = ?", new Object[] { colorSortID });
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
