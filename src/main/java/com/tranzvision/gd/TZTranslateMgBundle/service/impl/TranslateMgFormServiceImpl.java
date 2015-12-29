/**
 * 
 */
package com.tranzvision.gd.TZTranslateMgBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZTranslateMgBundle.dao.PsTzPtZhzxxLngMapper;
import com.tranzvision.gd.TZTranslateMgBundle.dao.PsTzPtZhzxxTblMapper;
import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxLng;
import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxTbl;
import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 转换值定义功能；原：TZ_GD_TRANS_PKG:TZ_GD_trans3_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-10
 */
@Service("com.tranzvision.gd.TZTranslateMgBundle.service.impl.TranslateMgFormServiceImpl")
public class TranslateMgFormServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzPtZhzxxTblMapper psTzPtZhzxxTblMapper;

	@Autowired
	private PsTzPtZhzxxLngMapper psTzPtZhzxxLngMapper;

	/**
	 * 获取转换值信息
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
			if (jacksonUtil.containsKey("transSetID") && jacksonUtil.containsKey("transID")) {

				String tzZhzjhId = jacksonUtil.getString("transSetID");
				String tzZhzId = jacksonUtil.getString("transID");
				String language = jacksonUtil.getString("language");
				if (null == language || "".equals(language)) {
					language = "ZHS";
				}

				PsTzPtZhzxxTbl psTzPtZhzxxTbl = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

				if ("ZHS".equals(language)) {
					PsTzPtZhzxxTblKey psTzPtZhzxxTblKey = new PsTzPtZhzxxTblKey();
					psTzPtZhzxxTblKey.setTzZhzjhId(tzZhzjhId);
					psTzPtZhzxxTblKey.setTzZhzId(tzZhzId);

					psTzPtZhzxxTbl = psTzPtZhzxxTblMapper.selectByPrimaryKey(psTzPtZhzxxTblKey);

				} else {

					Map<String, Object> mapZhzxx = sqlQuery.queryForMap(
							tzSQLObject.getSQLText("SQL.TZTranslateMgBundle.TzSelectOneZhzxx"),
							new Object[] { language, tzZhzjhId, tzZhzId });
					if (mapZhzxx != null) {
						psTzPtZhzxxTbl = new PsTzPtZhzxxTbl();
						psTzPtZhzxxTbl.setTzZhzjhId(mapZhzxx.get("TZ_ZHZJH_ID").toString());
						psTzPtZhzxxTbl.setTzZhzId(mapZhzxx.get("TZ_ZHZ_ID").toString());
						psTzPtZhzxxTbl.setTzEffDate(format.parse(mapZhzxx.get("TZ_EFF_DATE").toString()));
						psTzPtZhzxxTbl.setTzEffStatus(mapZhzxx.get("TZ_EFF_STATUS").toString());
						psTzPtZhzxxTbl.setTzZhzDms(mapZhzxx.get("TZ_ZHZ_DMS").toString());
						psTzPtZhzxxTbl.setTzZhzCms(mapZhzxx.get("TZ_ZHZ_CMS").toString());
					}
				}

				if (psTzPtZhzxxTbl != null) {

					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("transSetID", psTzPtZhzxxTbl.getTzZhzjhId());
					mapData.put("transID", psTzPtZhzxxTbl.getTzZhzId());
					mapData.put("effeDate", format.format(psTzPtZhzxxTbl.getTzEffDate()));
					mapData.put("isEffe", psTzPtZhzxxTbl.getTzEffStatus());
					mapData.put("shortDesc", psTzPtZhzxxTbl.getTzZhzDms());
					mapData.put("longDesc", psTzPtZhzxxTbl.getTzZhzCms());
					mapData.put("language", language);

					strRet = jacksonUtil.Map2json(mapData);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该转换值信息不存在";
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
	 * 新增转换值信息
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
			Date lastupddttm = new Date();
			String lastupdoprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 转换值集合ID
				String tzZhzjhId = jacksonUtil.getString("transSetID");
				// 转换值ID
				String tzZhzId = jacksonUtil.getString("transID");
				// 语言
				String language = jacksonUtil.getString("language");

				String sql = "select 'Y' from PS_TZ_PT_ZHZXX_TBL where TZ_ZHZJH_ID=? and TZ_ZHZ_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { tzZhzjhId, tzZhzId }, "String");

				if (null != recExists) {
					if (!"".equals(conflictKeys)) {
						comma = ",";
					}
					conflictKeys += comma + tzZhzId;
				} else {

					String tzZhzDms = jacksonUtil.getString("shortDesc");
					String tzZhzCms = jacksonUtil.getString("longDesc");

					PsTzPtZhzxxTbl psTzPtZhzxxTbl = new PsTzPtZhzxxTbl();
					psTzPtZhzxxTbl.setTzZhzjhId(tzZhzjhId);
					psTzPtZhzxxTbl.setTzZhzId(tzZhzId);
					psTzPtZhzxxTbl.setTzEffDate(jacksonUtil.getDate("effeDate"));
					psTzPtZhzxxTbl.setTzEffStatus(jacksonUtil.getString("isEffe"));
					psTzPtZhzxxTbl.setTzZhzDms(tzZhzDms);
					psTzPtZhzxxTbl.setTzZhzCms(tzZhzCms);
					psTzPtZhzxxTbl.setLastupddttm(lastupddttm);
					psTzPtZhzxxTbl.setLastupdoprid(lastupdoprid);

					psTzPtZhzxxTblMapper.insert(psTzPtZhzxxTbl);

					// 写入语言表
					if (!"ZHS".equals(language)) {
						sql = "select 'Y' from PS_TZ_PT_ZHZXX_LNG where TZ_ZHZJH_ID=? and TZ_ZHZ_ID=? and TZ_LANGUAGE_ID=?";
						recExists = sqlQuery.queryForObject(sql, new Object[] { tzZhzjhId, tzZhzId, language },
								"String");

						PsTzPtZhzxxLng psTzPtZhzxxLng = new PsTzPtZhzxxLng();
						psTzPtZhzxxLng.setTzZhzjhId(tzZhzjhId);
						psTzPtZhzxxLng.setTzZhzId(tzZhzId);
						psTzPtZhzxxLng.setTzZhzDms(tzZhzDms);
						psTzPtZhzxxLng.setTzZhzCms(tzZhzCms);
						psTzPtZhzxxLng.setTzLanguageId(language);

						if (null == recExists) {
							psTzPtZhzxxLngMapper.insert(psTzPtZhzxxLng);
						} else {
							psTzPtZhzxxLngMapper.updateByPrimaryKey(psTzPtZhzxxLng);
						}

					}

				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "当前转换集合下，转换值ID为：" + conflictKeys + "的信息已经注册,请修改转换值ID。";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 更新转换值信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String errorMsg = "";
		String comma = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date lastupddttm = new Date();
			String lastupdoprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 转换值集合ID
				String tzZhzjhId = jacksonUtil.getString("transSetID");
				// 转换值ID
				String tzZhzId = jacksonUtil.getString("transID");
				// 语言
				String language = jacksonUtil.getString("language");

				String sql = "select 'Y' from PS_TZ_PT_ZHZXX_TBL where TZ_ZHZJH_ID=? and TZ_ZHZ_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { tzZhzjhId, tzZhzId }, "String");

				if (null != recExists) {

					String tzZhzDms = jacksonUtil.getString("shortDesc");
					String tzZhzCms = jacksonUtil.getString("longDesc");

					PsTzPtZhzxxTbl psTzPtZhzxxTbl = new PsTzPtZhzxxTbl();
					psTzPtZhzxxTbl.setTzZhzjhId(tzZhzjhId);
					psTzPtZhzxxTbl.setTzZhzId(tzZhzId);
					psTzPtZhzxxTbl.setTzEffDate(jacksonUtil.getDate("effeDate"));
					psTzPtZhzxxTbl.setTzEffStatus(jacksonUtil.getString("isEffe"));
					psTzPtZhzxxTbl.setLastupddttm(lastupddttm);
					psTzPtZhzxxTbl.setLastupdoprid(lastupdoprid);
					if ("ZHS".equals(language)) {
						psTzPtZhzxxTbl.setTzZhzDms(tzZhzDms);
						psTzPtZhzxxTbl.setTzZhzCms(tzZhzCms);
					}
					psTzPtZhzxxTblMapper.updateByPrimaryKeySelective(psTzPtZhzxxTbl);

					// 写入语言表
					if (!"ZHS".equals(language)) {
						sql = "select 'Y' from PS_TZ_PT_ZHZXX_LNG where TZ_ZHZJH_ID=? and TZ_ZHZ_ID=? and TZ_LANGUAGE_ID=?";
						recExists = sqlQuery.queryForObject(sql, new Object[] { tzZhzjhId, tzZhzId, language },
								"String");

						PsTzPtZhzxxLng psTzPtZhzxxLng = new PsTzPtZhzxxLng();
						psTzPtZhzxxLng.setTzZhzjhId(tzZhzjhId);
						psTzPtZhzxxLng.setTzZhzId(tzZhzId);
						psTzPtZhzxxLng.setTzZhzDms(tzZhzDms);
						psTzPtZhzxxLng.setTzZhzCms(tzZhzCms);
						psTzPtZhzxxLng.setTzLanguageId(language);

						if (null == recExists) {
							psTzPtZhzxxLngMapper.insert(psTzPtZhzxxLng);
						} else {
							psTzPtZhzxxLngMapper.updateByPrimaryKey(psTzPtZhzxxLng);
						}

					}

				} else {
					if (!"".equals(errorMsg)) {
						comma = ",";
					}
					errorMsg += comma + tzZhzId;

				}

			}

			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = "转换值：" + errorMsg + "不存在。";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
