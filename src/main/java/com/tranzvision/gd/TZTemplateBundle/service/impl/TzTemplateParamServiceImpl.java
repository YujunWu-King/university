/**
 * 
 */
package com.tranzvision.gd.TZTemplateBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpParaTblMapper;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpParaTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 元模版参数定义相关类，原PS：TZ_GD_EMLSMSSET_PKG:TZ_GD_RESTMPLPARA_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-01
 */
@Service("com.tranzvision.gd.TZTemplateBundle.service.impl.TzTemplateParamServiceImpl")
public class TzTemplateParamServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzTmpParaTblMapper psTzTmpParaTblMapper;

	/**
	 * 新增模版内容参数信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String conflictKeys = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String restempid = jacksonUtil.getString("restempid");
				String restemporg = jacksonUtil.getString("restemporg");
				String paraid = jacksonUtil.getString("paraid");
				String paraalias = jacksonUtil.getString("paraalias");
				String systvar = jacksonUtil.getString("systvar");

				String sql = "select 'Y' from PS_TZ_TMP_PARA_TBL where TZ_JG_ID=? and TZ_YMB_ID=? and TZ_PARA_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid, paraid },
						"String");

				if (!"Y".equals(recExists)) {

					PsTzTmpParaTbl psTzTmpParaTbl = new PsTzTmpParaTbl();
					psTzTmpParaTbl.setTzJgId(restemporg);
					psTzTmpParaTbl.setTzParaAlias(paraalias);
					psTzTmpParaTbl.setTzParaId(paraid);
					psTzTmpParaTbl.setTzSysvarid(systvar);
					psTzTmpParaTbl.setTzYmbId(restempid);

					psTzTmpParaTblMapper.insert(psTzTmpParaTbl);

				} else {
					conflictKeys = conflictKeys + comma + paraid;
					comma = ",";
				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "当前元模版下，参数名称为：" + conflictKeys + "的参数已经定义,请重新选择参数。";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 更新元模版参数信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String restempid = jacksonUtil.getString("restempid");
				String restemporg = jacksonUtil.getString("restemporg");
				String paraid = jacksonUtil.getString("paraid");
				String paraalias = jacksonUtil.getString("paraalias");
				String systvar = jacksonUtil.getString("systvar");

				String sql = "select 'Y' from PS_TZ_TMP_PARA_TBL where TZ_JG_ID=? and TZ_YMB_ID=? and TZ_PARA_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid, paraid },
						"String");

				if ("Y".equals(recExists)) {

					PsTzTmpParaTbl psTzTmpParaTbl = new PsTzTmpParaTbl();
					psTzTmpParaTbl.setTzJgId(restemporg);
					psTzTmpParaTbl.setTzParaAlias(paraalias);
					psTzTmpParaTbl.setTzParaId(paraid);
					psTzTmpParaTbl.setTzSysvarid(systvar);
					psTzTmpParaTbl.setTzYmbId(restempid);

					psTzTmpParaTblMapper.updateByPrimaryKey(psTzTmpParaTbl);

				} else {
					errorMsg = errorMsg + comma + paraid;
					comma = ",";
				}
			}

			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "参数名称为：" + errorMsg + " 不存在。";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 获取元模版内容参数信息
	 * 
	 * @param strParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("paraid") && jacksonUtil.containsKey("paraid")) {

				String restempid = jacksonUtil.getString("restempid");
				String restemporg = jacksonUtil.getString("restemporg");
				String paraid = jacksonUtil.getString("paraid");

				String sql = "select TZ_PARA_ALIAS,TZ_SYSVARID from PS_TZ_TMP_PARA_TBL where TZ_JG_ID=? and TZ_YMB_ID=? and TZ_PARA_ID=?";
				Map<String, Object> mapParam = sqlQuery.queryForMap(sql,
						new Object[] { restemporg, restempid, paraid });

				sql = "select TZ_PARA_CNAME from ps_TZ_EX_PARA_TBL where TZ_PARA_ID = ?";
				String paraname = sqlQuery.queryForObject(sql, new Object[] { paraid }, "String");
				String paraalias = null == mapParam.get("TZ_PARA_ALIAS") ? ""
						: String.valueOf(mapParam.get("TZ_PARA_ALIAS"));

				String sysvarid = "";
				String sysvarname = "";
				if (null != mapParam) {

					sysvarid = null == mapParam.get("TZ_SYSVARID") ? "" : String.valueOf(mapParam.get("TZ_SYSVARID"));

					if (!"".equals(sysvarid)) {
						sql = "select TZ_SYSVARNAME from PS_TZ_SYSVAR_T where TZ_SYSVARID = ?";
						sysvarname = sqlQuery.queryForObject(sql, new Object[] { sysvarid }, "String");
					}
				}

				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("restempid", restempid);
				mapData.put("restemporg", restemporg);
				mapData.put("paraid", paraid);
				mapData.put("paraname", paraname);
				mapData.put("paraalias", paraalias);
				mapData.put("systvar", sysvarid);
				mapData.put("systvarname", sysvarname);

				strRet = jacksonUtil.Map2json(mapData);

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

}
