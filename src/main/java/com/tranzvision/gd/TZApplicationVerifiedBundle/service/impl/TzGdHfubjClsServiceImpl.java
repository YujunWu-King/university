package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlchfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlchfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlchfTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.GetSeqNum;

/**
 * 
 * @author tang PS:TZ_GD_LCFB_PKG:TZ_GD_HFYBJ_CLS
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdHfubjClsServiceImpl")
public class TzGdHfubjClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzClsBmlchfTMapper psTzClsBmlchfTMapper;
	@Autowired
	private GetSeqNum GetSeqNum;

	// 查询表单
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_bmlc_id = jacksonUtil.getString("bmlc_id");
			String str_hfy_id = jacksonUtil.getString("hfy_id");

			
			String str_color = "", str_lc_name = "", str_xs_name = "", str_mrfb = "",str_sysvar = "";
			String sql = "SELECT TZ_APPPRO_COLOR,TZ_CLS_RESULT,TZ_APPPRO_CONTENT,TZ_SYSVAR,TZ_WFB_DEFALT_BZ FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH=?";
			
			Map<String, Object> map = jdbcTemplate.queryForMap(sql,
					new Object[] { str_bj_id, str_bmlc_id, str_hfy_id });
			if (map != null) {
				str_color = (String) map.get("TZ_APPPRO_COLOR");
				str_lc_name = (String) map.get("TZ_CLS_RESULT");
				str_xs_name = (String) map.get("TZ_APPPRO_CONTENT");
				str_mrfb = (String) map.get("TZ_WFB_DEFALT_BZ");
				str_sysvar = (String) map.get("TZ_SYSVAR");
			}

			str_xs_name = str_xs_name.replace("<p>", "");
			str_xs_name = str_xs_name.replace("</p>", "");
			if ("on".equals(str_mrfb)) {
				str_mrfb = "true";
			} else {
				str_mrfb = "false";
			}

			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("bj_id", str_bj_id);
			jsonMap.put("bmlc_id", str_bmlc_id);
			jsonMap.put("dybh_id", str_hfy_id);
			jsonMap.put("colorCode", str_color);
			jsonMap.put("hf_jg", str_lc_name);
			jsonMap.put("ms_zg", str_xs_name);
			jsonMap.put("sysvar", str_sysvar);
			jsonMap.put("hf_mrz", str_mrfb);
			returnJsonMap.replace("formData", jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	// 新增;
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		Map<String, Object> jsonmap = new HashMap<>();
		jsonmap.put("dybh_id", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return jacksonUtil.Map2json(jsonmap);
		}

		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 保存数据;
				String str_hf_mrz = "";
				if (jacksonUtil.containsKey("hf_mrz")) {
					str_hf_mrz = jacksonUtil.getString("hf_mrz");
				} else {
					str_hf_mrz = "";
				}

				String str_bj_id = jacksonUtil.getString("bj_id");
				String str_bmlc_id = jacksonUtil.getString("bmlc_id");
				String colorCode = jacksonUtil.getString("colorCode");
				String hf_jg = jacksonUtil.getString("hf_jg");
				String ms_zg = jacksonUtil.getString("ms_zg");
				String sysvar = jacksonUtil.getString("sysvar");
				if ("on".equals(str_hf_mrz)) {
					jdbcTemplate.update(
							"UPDATE PS_TZ_CLS_BMLCHF_T SET TZ_WFB_DEFALT_BZ='N' WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=?",
							new Object[] { str_bj_id, str_bmlc_id });
				}

				PsTzClsBmlchfT psTzClsBmlchfT = new PsTzClsBmlchfT();

				psTzClsBmlchfT.setTzClassId(str_bj_id);
				psTzClsBmlchfT.setTzAppproId(str_bmlc_id);
				int templateId = GetSeqNum.getSeqNum("TZ_CLS_BMLCHF_T", "TZ_APPPRO_HF_BH");
				psTzClsBmlchfT.setTzAppproHfBh(String.valueOf(templateId));
				psTzClsBmlchfT.setTzAppproColor(colorCode);
				psTzClsBmlchfT.setTzClsResult(hf_jg);
				psTzClsBmlchfT.setTzAppproContent(ms_zg);
				psTzClsBmlchfT.setTzSysvar(sysvar);
				psTzClsBmlchfT.setTzWfbDefaltBz(str_hf_mrz);
				psTzClsBmlchfTMapper.insert(psTzClsBmlchfT);
				jsonmap.replace("dybh_id", templateId);

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(jsonmap);
	}

	// 修改;
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strComContent;
		}

		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 保存数据;
				String str_hf_mrz = "";
				if (jacksonUtil.containsKey("hf_mrz")) {
					str_hf_mrz = jacksonUtil.getString("hf_mrz");
				} else {
					str_hf_mrz = "";
				}

				String str_bj_id = jacksonUtil.getString("bj_id");
				String str_bmlc_id = jacksonUtil.getString("bmlc_id");
				String str_dybh_id = jacksonUtil.getString("dybh_id");
				String colorCode = jacksonUtil.getString("colorCode");
				String hf_jg = jacksonUtil.getString("hf_jg");
				String ms_zg = jacksonUtil.getString("ms_zg");
				String sysvar = jacksonUtil.getString("sysvar");
				if ("on".equals(str_hf_mrz)) {
					jdbcTemplate.update(
							"UPDATE PS_TZ_CLS_BMLCHF_T SET TZ_WFB_DEFALT_BZ='N' WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=?",
							new Object[] { str_bj_id, str_bmlc_id });
				}
				PsTzClsBmlchfTKey psTzClsBmlchfTKey = new PsTzClsBmlchfTKey();
				psTzClsBmlchfTKey.setTzClassId(str_bj_id);
				psTzClsBmlchfTKey.setTzAppproId(str_bmlc_id);
				psTzClsBmlchfTKey.setTzAppproHfBh(str_dybh_id);

				PsTzClsBmlchfT psTzClsBmlchfT = psTzClsBmlchfTMapper.selectByPrimaryKey(psTzClsBmlchfTKey);
				if (psTzClsBmlchfT != null) {
					psTzClsBmlchfT.setTzAppproColor(colorCode);
					psTzClsBmlchfT.setTzClsResult(hf_jg);
					psTzClsBmlchfT.setTzAppproContent(ms_zg);
					psTzClsBmlchfT.setTzWfbDefaltBz(str_hf_mrz);
					psTzClsBmlchfT.setTzSysvar(sysvar);
					psTzClsBmlchfTMapper.updateByPrimaryKeySelective(psTzClsBmlchfT);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新数据时发生错误！";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strComContent;
	}
}
