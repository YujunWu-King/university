/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsHfdyTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzSbminfRepTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsHfdyT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzSbminfRepT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 短信内容管理，原PS：TZ_GD_BJGL_CLS:TZ_BJ_DXNR
 * 
 * @author SHIHUA
 * @since 2016-02-02
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassSmsServiceImpl")
public class TzClassSmsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzSbminfRepTMapper psTzSbminfRepTMapper;

	@Autowired
	private PsTzClsHfdyTMapper psTzClsHfdyTMapper;

	private final String prefixMsgId = "GD_MSG_";

	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String smtDtTmpID = jacksonUtil.getString("smtDtTmpID");
			String smtDtID = jacksonUtil.getString("smtDtID");

			String sql = "select count(*) from PS_TZ_SBMINF_REP_T where TZ_SBMINF_TMP_ID=? and TZ_SBMINF_ID=?";
			int total = sqlQuery.queryForObject(sql, new Object[] { smtDtTmpID, smtDtID }, "int");

			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

			if (total > 0) {
				sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetSmsList");

				List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { smtDtTmpID, smtDtID });

				for (Map<String, Object> mapData : listData) {

					Map<String, Object> mapJson = new HashMap<String, Object>();

					mapJson.put("smtDtTmpID", smtDtTmpID);
					mapJson.put("smtDtID", smtDtID);
					mapJson.put("msgId", mapData.get("TZ_SBMINF_REP_ID") == null ? ""
							: String.valueOf(mapData.get("TZ_SBMINF_REP_ID")));
					mapJson.put("msgContent",
							mapData.get("TZ_SBMINF_REP") == null ? "" : String.valueOf(mapData.get("TZ_SBMINF_REP")));
					mapJson.put("order",
							mapData.get("TZ_SORT_NUM") == null ? "" : String.valueOf(mapData.get("TZ_SORT_NUM")));

					listJson.add(mapJson);
				}

			}

			Map<String, Object> mapListData = new HashMap<String, Object>();
			mapListData.put("total", total);
			mapListData.put("root", listJson);

			Map<String, Object> mapIDs = new HashMap<String, Object>();
			mapIDs.put("smtDtTmpID", smtDtTmpID);
			mapIDs.put("smtDtID", smtDtID);

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("formData", mapIDs);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "取数失败！" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 新增常用回复短语信息
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> mapParam = jacksonUtil.getMap("data");

				String smtDtTmpID = String.valueOf(mapParam.getOrDefault("smtDtTmpID", ""));
				String smtDtID = String.valueOf(mapParam.getOrDefault("smtDtID", ""));

				if (!"".equals(smtDtTmpID) && !"".equals(smtDtID)) {

					String msgContent = String.valueOf(mapParam.getOrDefault("msgContent", ""));
					int order = Integer.parseInt(String.valueOf(mapParam.getOrDefault("order", "0")));

					String msgId = String.valueOf(mapParam.getOrDefault("msgId", ""));

					if ("".equals(msgId)) {
						msgId = prefixMsgId
								+ String.valueOf(getSeqNum.getSeqNum("TZ_SBMINF_REP_T", "TZ_SBMINF_REP_ID"));
					} else {
						msgId = prefixMsgId + msgId;
					}

					PsTzSbminfRepT psTzSbminfRepT = new PsTzSbminfRepT();
					psTzSbminfRepT.setTzSbminfTmpId(smtDtTmpID);
					psTzSbminfRepT.setTzSbminfId(smtDtID);
					psTzSbminfRepT.setTzSbminfRepId(msgId);
					psTzSbminfRepT.setTzSbminfRep(msgContent);
					psTzSbminfRepT.setTzSortNum(order);
					psTzSbminfRepTMapper.insert(psTzSbminfRepT);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "添加失败！" + e.getMessage();
		}

		return strRet;
	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		try {

			JacksonUtil jacksonUtil = new JacksonUtil();

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> mapParam = jacksonUtil.getMap("data");

				String str_bj_id = String.valueOf(mapParam.getOrDefault("bj_id", ""));
				String str_jdzl_id = String.valueOf(mapParam.getOrDefault("djzl_id", ""));
				String str_hfdx_id = String.valueOf(mapParam.getOrDefault("hfdx_id", ""));
				String str_hfdx_desc = String.valueOf(mapParam.getOrDefault("hfdx_desc", ""));

				if (null == str_hfdx_id || "".equals(str_hfdx_id)) {
					str_hfdx_id = prefixMsgId
							+ String.valueOf(getSeqNum.getSeqNum("TZ_CLS_HFDY_T", "TZ_SBMINF_REP_ID"));
				}

				String sql = "select 'Y' from PS_TZ_CLS_HFDY_T where TZ_CLASS_ID=? and TZ_SBMINF_ID=? and TZ_SBMINF_REP_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { str_bj_id, str_jdzl_id, str_hfdx_id },
						"String");

				PsTzClsHfdyT psTzClsHfdyT = new PsTzClsHfdyT();
				psTzClsHfdyT.setTzClassId(str_bj_id);
				psTzClsHfdyT.setTzSbminfId(str_jdzl_id);
				psTzClsHfdyT.setTzSbminfRepId(str_hfdx_id);
				psTzClsHfdyT.setTzSbminfRep(str_hfdx_desc);

				if ("Y".equals(recExists)) {
					psTzClsHfdyTMapper.updateByPrimaryKey(psTzClsHfdyT);
				} else {
					psTzClsHfdyTMapper.insert(psTzClsHfdyT);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "更新失败！" + e.getMessage();
		}

		return strRet;
	}

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String smtDtTmpID = jacksonUtil.getString("bj_id");
			String smtDtID = jacksonUtil.getString("djzl_id");

			String sql = "SELECT COUNT(1) FROM PS_TZ_CLS_HFDY_T WHERE TZ_CLASS_ID=? and TZ_SBMINF_ID=?";
			int total = sqlQuery.queryForObject(sql, new Object[] { smtDtTmpID, smtDtID }, "int");

			sql = "select TZ_SBMINF_REP_ID,TZ_SBMINF_REP from PS_TZ_CLS_HFDY_T where TZ_CLASS_ID=? and TZ_SBMINF_ID=?";

			List<Map<String, Object>> listData;

			if (numLimit == 0 && numStart == 0) {
				listData = sqlQuery.queryForList(sql, new Object[] { smtDtTmpID, smtDtID });
			} else {
				sql = sql + " limit ?,?";
				listData = sqlQuery.queryForList(sql, new Object[] { smtDtTmpID, smtDtID, numStart, numLimit });
			}

			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

			for (Map<String, Object> mapData : listData) {
				String msgId = mapData.get("TZ_SBMINF_REP_ID") == null ? ""
						: String.valueOf(mapData.get("TZ_SBMINF_REP_ID"));
				String msgContent = mapData.get("TZ_SBMINF_REP") == null ? ""
						: String.valueOf(mapData.get("TZ_SBMINF_REP"));

				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("bj_id", smtDtTmpID);
				mapJson.put("djzl_id", smtDtID);
				mapJson.put("hfdx_id", msgId);
				mapJson.put("hfdx_desc", msgContent);

				listJson.add(mapJson);
			}

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", total);
			mapRet.put("root", listJson);

			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", 0);
			mapRet.put("root", "");
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}

	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errorMsg) {
		String strRet = "";

		try {

			JacksonUtil jacksonUtil = new JacksonUtil();

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> mapParam = jacksonUtil.getMap("data");

				String str_bj_id = String.valueOf(mapParam.getOrDefault("bj_id", ""));
				String str_djzl_id = String.valueOf(mapParam.getOrDefault("djzl_id", ""));
				String str_hfdx_id = String.valueOf(mapParam.getOrDefault("hfdx_id", ""));

				if (null != str_bj_id && !"".equals(str_bj_id) && null != str_djzl_id && !"".equals(str_djzl_id)) {
					String sql = "delete from PS_TZ_CLS_HFDY_T where TZ_CLASS_ID=? and TZ_SBMINF_ID=? and TZ_SBMINF_REP_ID=?";
					sqlQuery.update(sql, new Object[] { str_bj_id, str_djzl_id, str_hfdx_id });
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "删除失败！" + e.getMessage();
		}

		return strRet;
	}

}
