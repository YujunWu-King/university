package com.tranzvision.gd.TZProjectGradingSetBundle.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZProjectGradingSetBundle.dao.PsTzPrjTypeTMapper;
import com.tranzvision.gd.TZProjectGradingSetBundle.model.PsTzPrjTypeT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：项目分类定义;
 * 原PS类：TZ_GD_PROTYPE_PKG:TZ_PROTYPEINF0_CLS
 */
@Service("com.tranzvision.gd.TZProjectGradingSetBundle.service.impl.ProTypeInfoClsServiceImpl")
public class ProTypeInfoClsServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzPrjTypeTMapper PsTzPrjTypeTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/* 获取项目分类信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("proTypeId")) {
				// 项目分类编号;
				String strProTypeId = jacksonUtil.getString("proTypeId");
				PsTzPrjTypeT psTzPrjTypeT = PsTzPrjTypeTMapper.selectByPrimaryKey(strProTypeId);
				if (psTzPrjTypeT != null) {
					
					returnJsonMap.put("proTypeId", psTzPrjTypeT.getTzPrjTypeId());
					returnJsonMap.put("proTypeName", psTzPrjTypeT.getTzPrjTypeName());
					returnJsonMap.put("proTypeDesc", psTzPrjTypeT.getTzPrjTypeDesc());
					returnJsonMap.put("proTypeStatus", psTzPrjTypeT.getTzPrjTypeStatus());
					
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该项目分类数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该项目分类数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	// 新增项目分类定义;
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("prjID", "");
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 项目分类编号;
				String strProTypeId = (String) infoData.get("proTypeId");
				// 项目分类名称;
				String strProTypeName = (String) infoData.get("proTypeName");
				// 项目分类描述;
				String strProTypeDesc = (String) infoData.get("proTypeDesc");
				//项目分类有效状态
				String strProTypeStatus = (String) infoData.get("proTypeStatus");
				
				PsTzPrjTypeT psTzPrjTypeT;
				
				if ("NEXT".equals(strProTypeId)) {
					strProTypeId = "PRJ_TYPE_" + String.valueOf(getSeqNum.getSeqNum("TZ_PRJ_TYPE_T", "TZ_PRJ_TYPE_ID"));
					psTzPrjTypeT = new PsTzPrjTypeT();
					psTzPrjTypeT.setTzJgId(orgid);
					psTzPrjTypeT.setTzPrjTypeId(strProTypeId);
					psTzPrjTypeT.setTzPrjTypeName(strProTypeName);
					psTzPrjTypeT.setTzPrjTypeDesc(strProTypeDesc);
					psTzPrjTypeT.setTzPrjTypeStatus(strProTypeStatus);
					psTzPrjTypeT.setRowAddedDttm(new Date());
					psTzPrjTypeT.setRowAddedOprid(oprid);
					psTzPrjTypeT.setRowLastmantDttm(new Date());
					psTzPrjTypeT.setRowLastmantOprid(oprid);
					int i = PsTzPrjTypeTMapper.insert(psTzPrjTypeT);
					if (i > 0) {
						returnJsonMap.replace("prjID", strProTypeId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "项目分类信息保存失败";
					}
				} else{
					String sql = "select COUNT(1) from PS_TZ_PRJ_TYPE_T WHERE TZ_PRJ_TYPE_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { strProTypeId }, "Integer");
					if (count > 0) {
						psTzPrjTypeT = new PsTzPrjTypeT();
						psTzPrjTypeT.setTzJgId(orgid);
						psTzPrjTypeT.setTzPrjTypeId(strProTypeId);
						psTzPrjTypeT.setTzPrjTypeName(strProTypeName);
						psTzPrjTypeT.setTzPrjTypeDesc(strProTypeDesc);
						psTzPrjTypeT.setTzPrjTypeStatus(strProTypeStatus);
						psTzPrjTypeT.setRowAddedDttm(new Date());
						psTzPrjTypeT.setRowAddedOprid(oprid);
						psTzPrjTypeT.setRowLastmantDttm(new Date());
						psTzPrjTypeT.setRowLastmantOprid(oprid);
						int i = PsTzPrjTypeTMapper.updateByPrimaryKeySelective(psTzPrjTypeT);
						if (i > 0) {
							returnJsonMap.replace("prjID", strProTypeId);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "项目分类信息保存失败";
						}
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
	
	// 新增项目分类定义;
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("prjID", "");
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 项目分类编号;
				String strProTypeId = (String) infoData.get("proTypeId");
				// 项目分类名称;
				String strProTypeName = (String) infoData.get("proTypeName");
				// 项目分类描述;
				String strProTypeDesc = (String) infoData.get("proTypeDesc");
				//项目分类有效状态
				String strProTypeStatus = (String) infoData.get("proTypeStatus");
				PsTzPrjTypeT psTzPrjTypeT;
				
				if ("NEXT".equals(strProTypeId)) {
					strProTypeId = "PRJ_TYPE_" + String.valueOf(getSeqNum.getSeqNum("TZ_PRJ_TYPE_T", "TZ_PRJ_TYPE_ID"));
					psTzPrjTypeT = new PsTzPrjTypeT();
					psTzPrjTypeT.setTzJgId(orgid);
					psTzPrjTypeT.setTzPrjTypeId(strProTypeId);
					psTzPrjTypeT.setTzPrjTypeName(strProTypeName);
					psTzPrjTypeT.setTzPrjTypeDesc(strProTypeDesc);
					psTzPrjTypeT.setTzPrjTypeStatus(strProTypeStatus);
					psTzPrjTypeT.setRowAddedDttm(new Date());
					psTzPrjTypeT.setRowAddedOprid(oprid);
					psTzPrjTypeT.setRowLastmantDttm(new Date());
					psTzPrjTypeT.setRowLastmantOprid(oprid);
					int i = PsTzPrjTypeTMapper.insert(psTzPrjTypeT);
					if (i > 0) {
						returnJsonMap.replace("prjID", strProTypeId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "项目分类信息保存失败";
					}
				} else{
					String sql = "select COUNT(1) from PS_TZ_PRJ_TYPE_T WHERE TZ_PRJ_TYPE_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { strProTypeId }, "Integer");
					if (count > 0) {
						psTzPrjTypeT = new PsTzPrjTypeT();
						psTzPrjTypeT.setTzJgId(orgid);
						psTzPrjTypeT.setTzPrjTypeId(strProTypeId);
						psTzPrjTypeT.setTzPrjTypeName(strProTypeName);
						psTzPrjTypeT.setTzPrjTypeDesc(strProTypeDesc);
						psTzPrjTypeT.setTzPrjTypeStatus(strProTypeStatus);
						psTzPrjTypeT.setRowAddedDttm(new Date());
						psTzPrjTypeT.setRowAddedOprid(oprid);
						psTzPrjTypeT.setRowLastmantDttm(new Date());
						psTzPrjTypeT.setRowLastmantOprid(oprid);
						int i = PsTzPrjTypeTMapper.updateByPrimaryKeySelective(psTzPrjTypeT);
						if (i > 0) {
							returnJsonMap.replace("prjID", strProTypeId);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "项目分类信息保存失败";
						}
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
}
