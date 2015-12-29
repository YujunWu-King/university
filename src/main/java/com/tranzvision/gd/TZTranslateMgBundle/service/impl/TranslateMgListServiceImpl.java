/**
 * 
 */
package com.tranzvision.gd.TZTranslateMgBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZTranslateMgBundle.dao.PsTzPtZhzjhTblMapper;
import com.tranzvision.gd.TZTranslateMgBundle.dao.PsTzPtZhzxxTblMapper;
import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzjhTbl;
import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 转换值集合相关功能；原：TZ_GD_TRANS_PKG:TZ_GD_trans2_CLS
 * 
 * @author SHIHUA
 * @since 2015-11-10
 */
@Service("com.tranzvision.gd.TZTranslateMgBundle.service.impl.TranslateMgListServiceImpl")
public class TranslateMgListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private PsTzPtZhzjhTblMapper psTzPtZhzjhTblMapper;
	
	@Autowired
	private PsTzPtZhzxxTblMapper psTzPtZhzxxTblMapper;

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
			if (jacksonUtil.containsKey("transSetID")) {

				String tzZhzjhId = jacksonUtil.getString("transSetID");

				PsTzPtZhzjhTbl psTzPtZhzjhTbl = psTzPtZhzjhTblMapper.selectByPrimaryKey(tzZhzjhId);

				/*
				 * 此部分通过另外的代码实现 // 获取该组件下的页面注册信息 String sql =
				 * "select * from PS_TZ_PT_ZHZXX_TBL where TZ_ZHZJH_ID=?";
				 * List<PsTzPtZhzxxTbl> listZhzxx = sqlQuery.queryForList(sql,
				 * new Object[] { tzZhzjhId });
				 * 
				 * List<Map<String, Object>> listZhzxxJson = new
				 * ArrayList<Map<String, Object>>();
				 * 
				 * for (PsTzPtZhzxxTbl psTzPtZhzxxTbl : listZhzxx) { Map<String,
				 * Object> mapZhzxx = new HashMap<String, Object>();
				 * mapZhzxx.put("transSetID", psTzPtZhzxxTbl.getTzZhzjhId());
				 * mapZhzxx.put("transID", psTzPtZhzxxTbl.getTzZhzId());
				 * mapZhzxx.put("effeDate", psTzPtZhzxxTbl.getTzEffDate());
				 * mapZhzxx.put("isEffe", psTzPtZhzxxTbl.getTzEffStatus());
				 * mapZhzxx.put("shortDesc", psTzPtZhzxxTbl.getTzZhzDms());
				 * mapZhzxx.put("longDesc", psTzPtZhzxxTbl.getTzZhzCms());
				 * mapZhzxx.put("language", "");
				 * 
				 * listZhzxxJson.add(mapZhzxx); }
				 * 
				 * // 获取页面注册信息总数 sql =
				 * "SELECT COUNT(1) FROM ps_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=?"
				 * ; int total = sqlQuery.queryForObject(sql, new Object[] {
				 * tzZhzjhId }, "int");
				 * 
				 * Map<String, Object> mapTotalMainJson = new HashMap<String,
				 * Object>(); mapTotalMainJson.put("total", total);
				 * mapTotalMainJson.put("root", listZhzxxJson);
				 */

				if (psTzPtZhzjhTbl != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("transSetID", psTzPtZhzjhTbl.getTzZhzjhId());
					mapData.put("transSetDesc", psTzPtZhzjhTbl.getTzZhzjhMs());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该转换值集合不存在";
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
	 * 新增或修改转换值集合信息
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

				// 类型标志
				String typeFlag = jacksonUtil.getString("typeFlag").toUpperCase();
				// 信息内容
				Map<String, Object> mapData = jacksonUtil.getMap("data");
				String tzZhzjhId = mapData.get("transSetID").toString();
				String tzZhzjhMs = mapData.get("transSetDesc").toString();

				// 组件信息
				if ("COM".equals(typeFlag)) {

					String sql = "select 'Y' from PS_TZ_PT_ZHZJH_TBL WHERE TZ_ZHZJH_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { tzZhzjhId }, "String");

					if (null != recExists) {
						if (!"".equals(conflictKeys)) {
							comma = ",";
						}
						conflictKeys += comma + tzZhzjhId;
					} else {
						PsTzPtZhzjhTbl psTzPtZhzjhTbl = new PsTzPtZhzjhTbl();
						psTzPtZhzjhTbl.setTzZhzjhId(tzZhzjhId);
						psTzPtZhzjhTbl.setTzZhzjhMs(tzZhzjhMs);
						psTzPtZhzjhTblMapper.insert(psTzPtZhzjhTbl);
					}

				} else if ("PAGE".equals(typeFlag)) {

					this.tzUpdateTransVal(mapData, errMsg);
					if (!"".equals(errorMsg)) {
						comma = "\n";
					}
					errorMsg += comma + errMsg[1];
				}

			}
			if (!"".equals(conflictKeys)) {
				errMsg[0] = "1";
				errMsg[1] = "转换集合ID为：" + conflictKeys + "的信息已经注册，请修改转换集合ID。";
			}
			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] += errorMsg;
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/**
	 * 修改转换值集合信息
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

				// 类型标志
				String typeFlag = jacksonUtil.getString("typeFlag").toUpperCase();
				// 信息内容
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				// 组件信息
				if ("COM".equals(typeFlag)) {

					String tzZhzjhId = mapData.get("transSetID").toString();
					String tzZhzjhMs = mapData.get("transSetDesc").toString();

					String sql = "select 'Y' from PS_TZ_PT_ZHZJH_TBL WHERE TZ_ZHZJH_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { tzZhzjhId }, "String");

					PsTzPtZhzjhTbl psTzPtZhzjhTbl = new PsTzPtZhzjhTbl();
					psTzPtZhzjhTbl.setTzZhzjhId(tzZhzjhId);
					psTzPtZhzjhTbl.setTzZhzjhMs(tzZhzjhMs);

					// 有则更新，无则新增
					if (null != recExists) {
						psTzPtZhzjhTblMapper.updateByPrimaryKey(psTzPtZhzjhTbl);
					} else {
						psTzPtZhzjhTblMapper.insert(psTzPtZhzjhTbl);
					}

				} else if ("PAGE".equals(typeFlag)) {

					this.tzUpdateTransVal(mapData, errMsg);

					if (!"".equals(errorMsg)) {
						comma = "\n";
					}
					errorMsg += comma + errMsg[1];
				}

			}

			if (!"".equals(errorMsg)) {
				errMsg[0] = "1";
				errMsg[1] = errorMsg;
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 删除转换值信息列表
	 */
	@Override
	@Transactional
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

				// 值转换集合id
				String transSetID = jacksonUtil.getString("transSetID");
				// 转换值id
				String transID = jacksonUtil.getString("transID");

				if (transSetID != null && !"".equals(transSetID)) {

					if (transID != null && !"".equals(transID)) {

						// 删除转换值信息;
						sqlQuery.update("delete from PS_TZ_PT_ZHZXX_TBL where TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=?",
								new Object[] { transSetID, transID });
						// 删除语言
						sqlQuery.update("delete from PS_TZ_PT_ZHZXX_LNG where TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=?",
								new Object[] { transSetID, transID });

						errMsg[0] = "0";
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

	/**
	 * 查询转换值信息列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {

			jacksonUtil.json2Map(strParams);
			String transSetID = jacksonUtil.getString("transSetID");
			String language = jacksonUtil.getString("language");
			if (null == language || "".equals(language)) {
				language = "ZHS";
			}

			String sql = "";
			Object[] args;
			if ("ZHS".equals(language)) {
				if (numLimit == 0 && numStart == 0) {
					sql = tzSQLObject.getSQLText("SQL.TZTranslateMgBundle.TzGetZhsZhzxxNolimit");
					args = new Object[] { transSetID };
				} else {
					sql = tzSQLObject.getSQLText("SQL.TZTranslateMgBundle.TzGetZhsZhzxxLimit");
					args = new Object[] { transSetID, numStart, numLimit };
				}
			} else {
				if (numLimit == 0 && numStart == 0) {
					sql = tzSQLObject.getSQLText("SQL.TZTranslateMgBundle.TzGetLangZhzxxNolimit");
					args = new Object[] { language, transSetID };
				} else {
					sql = tzSQLObject.getSQLText("SQL.TZTranslateMgBundle.TzGetLangZhzxxLimit");
					args = new Object[] { language, transSetID, numStart, numLimit };
				}
			}

			List<?> listData = sqlQuery.queryForList(sql, args);
			ArrayList<Map<String, Object>> listDataJson = new ArrayList<Map<String, Object>>();

			for (Object zhzxxData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) zhzxxData;

				String isEffe = "";
				if (mapData.get("TZ_EFF_STATUS").equals("A")) {
					isEffe = "有效";
				} else if (mapData.get("TZ_EFF_STATUS").equals("I")) {
					isEffe = "无效";
				}

				Map<String, Object> mapDataJson = new HashMap<String, Object>();
				mapDataJson.put("transSetID", transSetID);
				mapDataJson.put("transID", mapData.get("TZ_ZHZ_ID"));
				mapDataJson.put("effeDate", mapData.get("TZ_EFF_DATE"));
				mapDataJson.put("isEffe", isEffe);
				mapDataJson.put("shortDesc", mapData.get("TZ_ZHZ_DMS"));
				mapDataJson.put("longDesc", mapData.get("TZ_ZHZ_CMS"));
				mapDataJson.put("language", mapData.get(""));

				listDataJson.add(mapDataJson);

			}

			sql = "select count(1) from PS_TZ_PT_ZHZXX_TBL where TZ_ZHZJH_ID=?";
			int total = sqlQuery.queryForObject(sql, new Object[] { transSetID }, "int");

			mapRet.replace("total", total);
			mapRet.replace("root", listDataJson);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/**
	 * 更新转换值数据
	 * 
	 * @param mapData
	 * @param errMsg
	 * @return
	 */
	@Transactional
	private String tzUpdateTransVal(Map<String, Object> mapData, String[] errMsg) {
		String strRet = "{}";
		try {

			String tzZhzjhId = mapData.get("transSetID").toString();
			String tzZhzId = mapData.get("transID").toString();
			String effeDate = mapData.get("effeDate").toString();
			String tzEffStatus = mapData.get("isEffe").toString();
			String tzZhzDms = mapData.get("shortDesc").toString();
			String tzZhzCms = mapData.get("longDesc").toString();

			String sql = "select 'Y' from PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? and TZ_ZHZ_ID=?";
			String recExists = sqlQuery.queryForObject(sql, new Object[] { tzZhzjhId, tzZhzId }, "String");

			if (null != recExists) {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date tzEffDate = formatter.parse(effeDate);

				PsTzPtZhzxxTbl psTzPtZhzxxTbl = new PsTzPtZhzxxTbl();
				psTzPtZhzxxTbl.setTzZhzjhId(tzZhzjhId);
				psTzPtZhzxxTbl.setTzZhzId(tzZhzId);
				psTzPtZhzxxTbl.setTzEffDate(tzEffDate);
				psTzPtZhzxxTbl.setTzEffStatus(tzEffStatus);
				psTzPtZhzxxTbl.setTzZhzDms(tzZhzDms);
				psTzPtZhzxxTbl.setTzZhzCms(tzZhzCms);
				
				psTzPtZhzxxTblMapper.updateByPrimaryKey(psTzPtZhzxxTbl);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "转换值集合ID为：" + tzZhzjhId + "，转换值ID为：" + tzZhzId + "的信息不存在。";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

}
