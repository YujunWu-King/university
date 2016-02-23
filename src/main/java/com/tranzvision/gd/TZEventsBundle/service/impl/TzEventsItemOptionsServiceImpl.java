/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzXxxTrEnTMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzXxxTransTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTrEnT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTrEnTKey;
import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTransT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTransTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 报名项下拉值定义，原PS：TZ_GD_HDGL:ActivityItemOption
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsItemOptionsServiceImpl")
public class TzEventsItemOptionsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private PsTzXxxTransTMapper psTzXxxTransTMapper;

	@Autowired
	private PsTzXxxTrEnTMapper psTzXxxTrEnTMapper;

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		String strRet = "";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String activityId = jacksonUtil.getString("activityId");
			String applyItemId = jacksonUtil.getString("applyItemId");

			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetItemOptionsList");
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { activityId, applyItemId });

			for (Map<String, Object> mapData : listData) {

				Map<String, Object> mapJson = new HashMap<String, Object>();

				mapJson.put("transId",
						mapData.get("TZ_XXX_TRANS_ID") == null ? "" : String.valueOf(mapData.get("TZ_XXX_TRANS_ID")));

				mapJson.put("transPxXh",
						mapData.get("TZ_PX_XH") == null ? "" : String.valueOf(mapData.get("TZ_PX_XH")));

				mapJson.put("transName", mapData.get("TZ_XXX_TRANS_NAME") == null ? ""
						: String.valueOf(mapData.get("TZ_XXX_TRANS_NAME")));

				mapJson.put("transNameEng",
						mapData.get("TZ_OPT_VALUE") == null ? "" : String.valueOf(mapData.get("TZ_OPT_VALUE")));
				
				listJson.add(mapJson);

			}

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", 0);
			mapRet.put("root", listJson);
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "查询失败！" + e.getMessage();
		}

		return strRet;

	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String activityId = jacksonUtil.getString("activityId");
				String applyItemId = jacksonUtil.getString("applyItemId");

				Map<String, Object> mapParams = jacksonUtil.getMap("data");

				String transId = mapParams.get("transId") == null ? "" : String.valueOf(mapParams.get("transId"));
				int transPxXh = mapParams.get("transPxXh") == null ? 0
						: Integer.parseInt(String.valueOf(mapParams.get("transPxXh")));
				String transName = mapParams.get("transName") == null ? "" : String.valueOf(mapParams.get("transName"));
				String transNameEng = mapParams.get("transNameEng") == null ? ""
						: String.valueOf(mapParams.get("transNameEng"));

				if ("".equals(transName)) {
					continue;
				}

				String sql = "select 'Y' from PS_TZ_XXX_TRANS_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and TZ_XXX_TRANS_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { activityId, applyItemId, transId },
						"String");

				PsTzXxxTransT psTzXxxTransT = new PsTzXxxTransT();
				psTzXxxTransT.setTzArtId(activityId);
				psTzXxxTransT.setTzZxbmXxxId(applyItemId);
				psTzXxxTransT.setTzXxxTransId(transId);
				psTzXxxTransT.setTzPxXh(transPxXh);
				psTzXxxTransT.setTzXxxTransName(transName);
				if ("Y".equals(recExists)) {
					psTzXxxTransTMapper.updateByPrimaryKey(psTzXxxTransT);
				} else {
					psTzXxxTransTMapper.insert(psTzXxxTransT);
				}

				if (!"".equals(transNameEng)) {

					sql = "select 'Y' from PS_TZ_XXX_TR_EN_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and TZ_XXX_TRANS_ID=? and LANGUAGE_CD='ENG'";
					recExists = sqlQuery.queryForObject(sql, new Object[] { activityId, applyItemId, transId },
							"String");

					PsTzXxxTrEnT psTzXxxTrEnT = new PsTzXxxTrEnT();
					psTzXxxTrEnT.setTzArtId(activityId);
					psTzXxxTrEnT.setTzZxbmXxxId(applyItemId);
					psTzXxxTrEnT.setTzXxxTransId(transId);
					psTzXxxTrEnT.setLanguageCd("ENG");
					psTzXxxTrEnT.setTzOptValue(transNameEng);
					if ("Y".equals(recExists)) {
						psTzXxxTrEnTMapper.updateByPrimaryKey(psTzXxxTrEnT);
					} else {
						psTzXxxTrEnTMapper.insert(psTzXxxTrEnT);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "更新失败！" + e.getMessage();
		}

		return strRet;
	}

	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String activityId = jacksonUtil.getString("activityId");
				String applyItemId = jacksonUtil.getString("applyItemId");

				Map<String, Object> mapParams = jacksonUtil.getMap("data");

				String transId = mapParams.get("transId") == null ? "" : String.valueOf(mapParams.get("transId"));
				if (null == activityId || "".equals(activityId) || null == applyItemId || "".equals(applyItemId)
						|| "".equals(transId)) {
					continue;
				}

				PsTzXxxTransTKey psTzXxxTransTKey = new PsTzXxxTransTKey();
				psTzXxxTransTKey.setTzArtId(activityId);
				psTzXxxTransTKey.setTzZxbmXxxId(applyItemId);
				psTzXxxTransTKey.setTzXxxTransId(transId);
				psTzXxxTransTMapper.deleteByPrimaryKey(psTzXxxTransTKey);

				PsTzXxxTrEnTKey psTzXxxTrEnTKey = new PsTzXxxTrEnTKey();
				psTzXxxTrEnTKey.setTzArtId(activityId);
				psTzXxxTrEnTKey.setTzZxbmXxxId(applyItemId);
				psTzXxxTrEnTKey.setTzXxxTransId(transId);
				psTzXxxTrEnTKey.setLanguageCd("ENG");
				psTzXxxTrEnTMapper.deleteByPrimaryKey(psTzXxxTrEnTKey);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "删除失败！" + e.getMessage();
		}

		return strRet;
	}

}
