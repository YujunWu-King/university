package com.tranzvision.gd.TZJudgesTypeBundle.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZJudgesTypeBundle.dao.PsTzJugtypTblMapper;
import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzJugtypTbl;
import com.tranzvision.gd.TZProjectGradingSetBundle.dao.PsTzPrjTypeTMapper;
import com.tranzvision.gd.TZProjectGradingSetBundle.model.PsTzPrjTypeT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author xzx; 
 * 功能说明：评委类型定义;
 * 原PS类：
 */
@Service("com.tranzvision.gd.TZJudgesTypeBundle.service.impl.JudgesTypeMgInfoServiceImpl")
public class JudgesTypeMgInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzJugtypTblMapper PsTzJugtypTblMapper;
	@Autowired
	private PsTzPrjTypeTMapper PsTzPrjTypeTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/* 获取评委类型信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("jugTypeId")) {
				// 项目分类编号;
				String tzJugtypId = jacksonUtil.getString("jugTypeId");
				PsTzJugtypTbl psTzJugtypTbl = PsTzJugtypTblMapper.selectByPrimaryKey(tzJugtypId);
//				PsTzPrjTypeT psTzPrjTypeT = PsTzPrjTypeTMapper.selectByPrimaryKey(strProTypeId);
				if (psTzJugtypTbl != null) {
					
					returnJsonMap.put("jugTypeId", psTzJugtypTbl.getTzJugtypId());
					returnJsonMap.put("jugTypeName", psTzJugtypTbl.getTzJugtypName());
					returnJsonMap.put("roleName", psTzJugtypTbl.getRolename());
					returnJsonMap.put("jugTypeStatus", psTzJugtypTbl.getTzJugtypStat());
					
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
		returnJsonMap.put("jugID", "");
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
				// 评审类型编号;
				String strJugTypeId = (String) infoData.get("jugTypeId");
				// 评审类型名称;
				String strJugTypeName = (String) infoData.get("jugTypeName");
				// 角色名称;
				String strRoleName = (String) infoData.get("roleName");
//				String strRoleId = (String) infoData.get("roleId");
				//有效状态
				String strjugTypeStatus = (String) infoData.get("jugTypeStatus");
				PsTzJugtypTbl psTzJugtypTbl;
				
				if ("NEXT".equals(strJugTypeId)) {
//					strJugTypeId = "PRJ_TYPE_" + String.valueOf(getSeqNum.getSeqNum("TZ_PRJ_TYPE_T", "TZ_PRJ_TYPE_ID"));
					strJugTypeId = String.valueOf(getSeqNum.getSeqNum("TZ_JUGTYP_TBL", "TZ_JUGTYP_ID"));
					psTzJugtypTbl = new PsTzJugtypTbl();
					psTzJugtypTbl.setTzJgId(orgid);
					psTzJugtypTbl.setTzJugtypId(strJugTypeId);
					psTzJugtypTbl.setTzJugtypName(strJugTypeName);
					psTzJugtypTbl.setRolename(strRoleName);
//					psTzJugtypTbl.setRolename(strRoleId);
					psTzJugtypTbl.setTzJugtypStat(strjugTypeStatus);
					psTzJugtypTbl.setRowAddedDttm(new Date());
					psTzJugtypTbl.setRowAddedOprid(oprid);
					psTzJugtypTbl.setRowLastmantDttm(new Date());
					psTzJugtypTbl.setRowLastmantOprid(oprid);
					int i = PsTzJugtypTblMapper.insert(psTzJugtypTbl);
					if (i > 0) {
						returnJsonMap.replace("jugID", strJugTypeId);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "项目分类信息保存失败";
					}
				} else{
					String sql = "select COUNT(1) from PS_TZ_JUGTYP_TBL WHERE TZ_JUGTYP_ID=?";
					int count = jdbcTemplate.queryForObject(sql, new Object[] { strJugTypeId }, "Integer");
					if (count > 0) {
						psTzJugtypTbl = new PsTzJugtypTbl();
						psTzJugtypTbl.setTzJgId(orgid);
						psTzJugtypTbl.setTzJugtypId(strJugTypeId);
						psTzJugtypTbl.setTzJugtypName(strJugTypeName);
						psTzJugtypTbl.setRolename(strRoleName);
//						psTzJugtypTbl.setRolename(strRoleId);
						psTzJugtypTbl.setTzJugtypStat(strjugTypeStatus);
						psTzJugtypTbl.setRowAddedDttm(new Date());
						psTzJugtypTbl.setRowAddedOprid(oprid);
						psTzJugtypTbl.setRowLastmantDttm(new Date());
						psTzJugtypTbl.setRowLastmantOprid(oprid);
						int i = PsTzJugtypTblMapper.updateByPrimaryKey(psTzJugtypTbl);
//						int i = PsTzPrjTypeTMapper.updateByPrimaryKeySelective(psTzPrjTypeT);
						if (i > 0) {
							returnJsonMap.replace("jugID", strJugTypeId);
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
