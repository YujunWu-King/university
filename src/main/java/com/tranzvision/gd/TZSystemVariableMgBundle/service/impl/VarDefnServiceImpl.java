package com.tranzvision.gd.TZSystemVariableMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSystemVariableMgBundle.dao.PsTzSvChainMapper;
import com.tranzvision.gd.TZSystemVariableMgBundle.dao.PsTzSysvarTMapper;
import com.tranzvision.gd.TZSystemVariableMgBundle.model.PsTzSvChainKey;
import com.tranzvision.gd.TZSystemVariableMgBundle.model.PsTzSysvarT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang 
 * 功能说明：高端产品-系统变量定义； 
 * 原PS类：TZ_GD_SYSVAR_PKG:TZ_GD_VARDEFN_CLS
 */
@Service("com.tranzvision.gd.TZSystemVariableMgBundle.service.impl.VarDefnServiceImpl")
public class VarDefnServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzSysvarTMapper psTzSysvarTMapper;
	@Autowired
	private PsTzSvChainMapper psTzSvChainMapper;
	
	/* 获取系统变量定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("systemVarId")) {
				// 系统变量ID;
				String sysVarId = jacksonUtil.getString("systemVarId");
				
				PsTzSysvarT psTzSysvarT = psTzSysvarTMapper.selectByPrimaryKey(sysVarId);
				if (psTzSysvarT != null) {
					returnJsonMap.put("systemVarId", psTzSysvarT.getTzSysvarid());
					returnJsonMap.put("systemVarName", psTzSysvarT.getTzSysvarname());
					returnJsonMap.put("isValid", psTzSysvarT.getTzEffflg());
					returnJsonMap.put("systemVarDesc", psTzSysvarT.getTzSysvardesc());
					returnJsonMap.put("sysVarDataType", psTzSysvarT.getTzSysvartype());
					returnJsonMap.put("getValType", psTzSysvarT.getTzValmethod());
					returnJsonMap.put("sqlValue", psTzSysvarT.getTzLngstrcont());
					returnJsonMap.put("appClass", psTzSysvarT.getTzAppclsReg());
					returnJsonMap.put("constant", psTzSysvarT.getTzConstant());
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该系统变量数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该系统变量数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	@Override
	/* 新增系统变量 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 系统变量ID;
				String sysVarId = jacksonUtil.getString("systemVarId");
				// 系统变量名称;
				String SysVarName = jacksonUtil.getString("systemVarName");
				// 是否有效;
				String isValid = jacksonUtil.getString("isValid");
				// 系统变量描述;
				String sysVarDesc = jacksonUtil.getString("systemVarDesc");
				// 数据类型;
				String dataType = jacksonUtil.getString("sysVarDataType");
				// 取值方式;
				String getValType = jacksonUtil.getString("getValType");
				// AppClass;
				String appCls = jacksonUtil.getString("appClass");
				// 常量;
				String constant = jacksonUtil.getString("constant");
				// SQL语句;
				String sqlCont = jacksonUtil.getString("sqlValue");

				String sql = "select COUNT(1) from PS_TZ_SYSVAR_T WHERE TZ_SYSVARID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { sysVarId }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "系统变量ID：" + sysVarId + "已存在，请修改系统变量ID";
				} else {
					PsTzSysvarT psTzSysvarT = new PsTzSysvarT();
					psTzSysvarT.setTzSysvarid(sysVarId);
					psTzSysvarT.setTzSysvarname(SysVarName);
					psTzSysvarT.setTzEffflg(isValid);
					psTzSysvarT.setTzSysvardesc(sysVarDesc);
					psTzSysvarT.setTzSysvartype(dataType);
					psTzSysvarT.setTzValmethod(getValType);
					psTzSysvarT.setTzAppclsReg(appCls);
					psTzSysvarT.setTzConstant(constant);
					psTzSysvarT.setTzLngstrcont(sqlCont);
					/*** TODO %USERID ***/
					psTzSysvarT.setRowAddedDttm(new Date());
					psTzSysvarT.setRowAddedOprid("TZ_7");
					psTzSysvarT.setRowLastmantDttm(new Date());
					psTzSysvarT.setRowLastmantOprid("TZ_7");
					psTzSysvarTMapper.insert(psTzSysvarT);

					/* 如果取值方式为SQL且存在系统变量调用，则将系统变量调用存储到系统变量调用关系表中 */
					if ("SQL".equals(getValType)) {
						List<Integer> positionArr = new ArrayList<Integer>();
						List<String> sysVarArray = new ArrayList<String>();
						this.getSysVarArray(sqlCont, positionArr, sysVarArray);

						int size = positionArr.size();
						if ((size % 2) != 0) {
							errMsg[0] = "1";
							errMsg[1] = "系统变量调用$不匹配";
							return strRet;
						}

						if (sysVarArray.size() > 0) {
							for (int i = 0; i < sysVarArray.size(); i++) {
								PsTzSvChainKey psTzSvChainKey = new PsTzSvChainKey();
								psTzSvChainKey.setTzSysvarid(sysVarId);
								psTzSvChainKey.setTzCSysvarid(sysVarArray.get(i));
								psTzSvChainMapper.insert(psTzSvChainKey);
							}
						}
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
	/* 新增系统变量 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 系统变量ID;
				String sysVarId = jacksonUtil.getString("systemVarId");
				// 系统变量名称;
				String SysVarName = jacksonUtil.getString("systemVarName");
				// 是否有效;
				String isValid = jacksonUtil.getString("isValid");
				// 系统变量描述;
				String sysVarDesc = jacksonUtil.getString("systemVarDesc");
				// 数据类型;
				String dataType = jacksonUtil.getString("sysVarDataType");
				// 取值方式;
				String getValType = jacksonUtil.getString("getValType");
				// AppClass;
				String appCls = jacksonUtil.getString("appClass");
				// 常量;
				String constant = jacksonUtil.getString("constant");
				// SQL语句;
				String sqlCont = jacksonUtil.getString("sqlValue");

				String sql = "select COUNT(1) from PS_TZ_SYSVAR_T WHERE TZ_SYSVARID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { sysVarId }, "Integer");
				if (count > 0) {
					PsTzSysvarT psTzSysvarT = new PsTzSysvarT();
					psTzSysvarT.setTzSysvarid(sysVarId);
					psTzSysvarT.setTzSysvarname(SysVarName);
					psTzSysvarT.setTzEffflg(isValid);
					psTzSysvarT.setTzSysvardesc(sysVarDesc);
					psTzSysvarT.setTzSysvartype(dataType);
					psTzSysvarT.setTzValmethod(getValType);
					psTzSysvarT.setTzAppclsReg(appCls);
					psTzSysvarT.setTzConstant(constant);
					psTzSysvarT.setTzLngstrcont(sqlCont);
					/*** TODO %USERID ***/
					psTzSysvarT.setRowLastmantDttm(new Date());
					psTzSysvarT.setRowLastmantOprid("TZ_7");
					psTzSysvarTMapper.updateByPrimaryKeySelective(psTzSysvarT);

					/* 如果取值方式为SQL且存在系统变量调用，则将系统变量调用存储到系统变量调用关系表中 */
					if ("SQL".equals(getValType)) {
						List<Integer> positionArr = new ArrayList<Integer>();
						List<String> sysVarArray = new ArrayList<String>();
						this.getSysVarArray(sqlCont, positionArr, sysVarArray);

						int size = positionArr.size();
						if ((size % 2) != 0) {
							errMsg[0] = "1";
							errMsg[1] = "系统变量调用$不匹配";
							return strRet;
						}

						String deleteSQL = "DELETE FROM PS_TZ_SV_CHAIN WHERE TZ_SYSVARID=?";
						jdbcTemplate.update(deleteSQL, new Object[] { sysVarId });

						if (sysVarArray.size() > 0) {
							for (int i = 0; i < sysVarArray.size(); i++) {
								PsTzSvChainKey psTzSvChainKey = new PsTzSvChainKey();
								psTzSvChainKey.setTzSysvarid(sysVarId);
								psTzSvChainKey.setTzCSysvarid(sysVarArray.get(i));
								psTzSvChainMapper.insert(psTzSvChainKey);
							}
						}
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "系统变量ID：" + sysVarId + "不存在";

				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	private void getSysVarArray(String strSql, List<Integer> positionArr, List<String> sysVarArray) {
		int count = 0;
		int Position = strSql.indexOf("$");
		while (Position >= 0) {
			positionArr.add(Position);
			Position = strSql.indexOf("$", Position + 1);

			count++;

			/* 将引用系统变量放入数组，只有$符号连续第奇偶个之间字符串才算作引用系统变量 */
			if ((Position > 0) && ((count % 2) != 0)) {
				String strArrItem = strSql.substring(positionArr.get(positionArr.size() - 1) + 1, Position);
				if (!sysVarArray.contains(strArrItem)) {
					sysVarArray.add(strArrItem);
				}
			}
		}
	}
}
