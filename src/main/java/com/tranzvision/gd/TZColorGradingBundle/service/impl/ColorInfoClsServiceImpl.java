package com.tranzvision.gd.TZColorGradingBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
 * @author  WRL TZ_GD_BMGL_COLOR_PKG:TZ_GD_COLOR_BJ_CLS
 * 颜色定义
 *
 */
@Service("com.tranzvision.gd.TZColorGradingBundle.service.impl.ColorInfoClsServiceImpl")
public class ColorInfoClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzColorSortTMapper psTzColorSortTMapper;
	
	/**
	 * 获取颜色定义信息
	 * 
	 * @param comParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String comParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if (jacksonUtil.containsKey("colorSortID")) {

				String colorSortID = jacksonUtil.getString("colorSortID");
				
				PsTzColorSortT psTzColorSortT = psTzColorSortTMapper.selectByPrimaryKey(colorSortID);
				
				if (psTzColorSortT != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					
					mapData.put("colorSortID", psTzColorSortT.getTzColorSortId());
					mapData.put("colorName", psTzColorSortT.getTzColorName());
					mapData.put("colorCode", psTzColorSortT.getTzColorCode());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该颜色数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/**
	 * 修改颜色定义
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		
		if(StringUtils.isBlank(orgId)){
			errMsg[0] = "1";
			errMsg[1] = "您不属于任何机构，不能修改颜色定义！";
			return strRet;
		}
		try {
			int dataLength = actData.length;
			Date dateNow = new Date();
			
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				String colorSortID = jacksonUtil.getString("colorSortID");
				String colorName = jacksonUtil.getString("colorName");
				String colorCode = jacksonUtil.getString("colorCode");
				
				if(StringUtils.equals(colorSortID, "NEXT")){
					String sql = "SELECT COUNT(1) FROM PS_TZ_COLOR_SORT_T WHERE TZ_JG_ID = ? AND (TZ_COLOR_NAME = ? OR TZ_COLOR_CODE = ?)";
					int count = sqlQuery.queryForObject(sql, new Object[] { orgId,colorName,colorCode }, "Integer");

					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前部门存在有相同名称或码值的颜色定义。";
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
						
						int isize = psTzColorSortTMapper.insert(psTzColorSortT);
						if(isize > 0){
							return colorSortID;
						}
					}
				}else{
					String sql = "SELECT COUNT(1) FROM PS_TZ_COLOR_SORT_T WHERE TZ_JG_ID = ? AND (TZ_COLOR_NAME = ? OR TZ_COLOR_CODE = ?) AND TZ_COLOR_SORT_ID <> ?";
					int count = sqlQuery.queryForObject(sql, new Object[] { orgId,colorName,colorCode,colorSortID }, "Integer");

					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前部门存在有相同名称或码值的颜色定义。";
						return strRet;
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
