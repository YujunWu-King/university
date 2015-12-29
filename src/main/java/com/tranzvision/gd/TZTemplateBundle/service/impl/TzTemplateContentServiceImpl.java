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
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpRrkfTblMapper;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpRrkfTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 元模版模版内容参数定义相关类，原PS：TZ_GD_EMLSMSSET_PKG:TZ_GD_RESTMPLCONT_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-01
 */
@Service("com.tranzvision.gd.TZTemplateBundle.service.impl.TzTemplateContentServiceImpl")
public class TzTemplateContentServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzTmpRrkfTblMapper psTzTmpRrkfTblMapper;

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
				String keyname = jacksonUtil.getString("keyname");
				String paraid = jacksonUtil.getString("paraid");

				String sql = "select 'Y' from PS_TZ_TMP_RRKF_TBL where TZ_JG_ID=? and TZ_YMB_ID=? and TZ_KEY_NAME=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid, keyname },
						"String");

				if (!"Y".equals(recExists)) {

					PsTzTmpRrkfTbl psTzTmpRrkfTbl = new PsTzTmpRrkfTbl();
					psTzTmpRrkfTbl.setTzJgId(restemporg);
					psTzTmpRrkfTbl.setTzYmbId(restempid);
					psTzTmpRrkfTbl.setTzKeyName(keyname);
					psTzTmpRrkfTbl.setTzParaId(paraid);

					psTzTmpRrkfTblMapper.insert(psTzTmpRrkfTbl);

				} else {
					conflictKeys = conflictKeys + comma + keyname;
					comma = ",";
				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "当前元模版下，关键字为：" + conflictKeys + "的内容参数已经定义,请重新选择。";
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
				String keyname = jacksonUtil.getString("keyname");
				String paraid = jacksonUtil.getString("paraid");

				String sql = "select 'Y' from PS_TZ_TMP_RRKF_TBL where TZ_JG_ID=? and TZ_YMB_ID=? and TZ_KEY_NAME=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid, keyname },
						"String");

				if ("Y".equals(recExists)) {

					PsTzTmpRrkfTbl psTzTmpRrkfTbl = new PsTzTmpRrkfTbl();
					psTzTmpRrkfTbl.setTzJgId(restemporg);
					psTzTmpRrkfTbl.setTzYmbId(restempid);
					psTzTmpRrkfTbl.setTzKeyName(keyname);
					psTzTmpRrkfTbl.setTzParaId(paraid);

					psTzTmpRrkfTblMapper.updateByPrimaryKey(psTzTmpRrkfTbl);

				} else {
					errorMsg = errorMsg + comma + keyname;
					comma = ",";
				}
			}

			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "模版参数信息：" + errorMsg + " 不存在。";
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
			if (jacksonUtil.containsKey("restempid") && jacksonUtil.containsKey("restempid")) {

				String restempid = jacksonUtil.getString("restempid");
				String restemporg = jacksonUtil.getString("restemporg");
				String keyname = jacksonUtil.getString("keyname");

				String sql = "select TZ_PARA_ID from PS_TZ_TMP_RRKF_TBL where TZ_JG_ID=? and TZ_YMB_ID=? and TZ_KEY_NAME = ?";
				String paraid = sqlQuery.queryForObject(sql, new Object[] { restemporg, restempid, keyname }, "String");

				String paraname = "";
				if (null != paraid && !"".equals(paraid)) {
					sql = "select TZ_PARA_CNAME from ps_TZ_EX_PARA_TBL where TZ_PARA_ID = ?";
					paraname = sqlQuery.queryForObject(sql, new Object[] { paraid }, "String");
				}

				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("restempid", restempid);
				mapData.put("restemporg", restemporg);
				mapData.put("keyname", keyname);
				mapData.put("paraid", paraid);
				mapData.put("paraname", paraname);

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
