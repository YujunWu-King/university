package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzAppproRstTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzAppproRstT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzAppproRstTKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS:TZ_LCJG_PUB_PKG:TZ_LCJG_PUBP_CLS
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzLcJgPubpClsServiceImpl")
public class TzLcJgPubpClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzAppproRstTMapper psTzAppproRstTMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

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
			String str_bmb_id1 = jacksonUtil.getString("bmb_id");
			String str_first_name = "", str_bmb_id = "", str_color_id = "", str_fb_desc = "";
			if (str_bmb_id1 != null && !"".equals(str_bmb_id1)) {
				String[] arr_bmb_id = str_bmb_id1.split(",");
				if (arr_bmb_id != null && arr_bmb_id.length > 0) {
					for (int i = 0; i < arr_bmb_id.length; i++) {
						String str_oprid = jdbcTemplate.queryForObject(
								"SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND TZ_APP_INS_ID=?",
								new Object[] { str_bj_id, Long.parseLong(arr_bmb_id[i]) }, "String");
						String str_first_name1 = jdbcTemplate.queryForObject(
								"SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[] { str_oprid },
								"String");
						if ("".equals(str_first_name)) {
							str_first_name = str_first_name1;
						} else {
							str_first_name = str_first_name + "," + str_first_name1;
						}

						if ("".equals(str_bmb_id)) {
							str_bmb_id = arr_bmb_id[i];
						} else {
							str_bmb_id = str_bmb_id + "," + arr_bmb_id[i];
						}
					}
				}

				String sql = "SELECT TZ_CLS_RESULT,TZ_APPPRO_CONTENT,TZ_APPPRO_HF_BH FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_WFB_DEFALT_BZ='on' limit 0,1";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { str_bj_id, str_bmlc_id });
				if (map != null) {
					// str_hfy_name = (String)map.get("TZ_CLS_RESULT");
					str_fb_desc = (String) map.get("TZ_APPPRO_CONTENT");
					str_color_id = (String) map.get("TZ_APPPRO_HF_BH");

					str_fb_desc = (String) map.get("TZ_APPPRO_CONTENT");
					if (str_fb_desc == null) {
						str_fb_desc = "";
					}
					str_color_id = (String) map.get("TZ_APPPRO_HF_BH");
					if (str_color_id == null) {
						str_color_id = "";
					}
				}
				Map<String, Object> jsonMap = new HashMap<>();
				jsonMap.put("bj_id", str_bj_id);
				jsonMap.put("bmb_id", str_bmb_id);
				jsonMap.put("ry_name", str_first_name);
				jsonMap.put("ry_lc", str_bmlc_id);
				jsonMap.put("jg_id", str_color_id);
				jsonMap.put("plgb_area", str_fb_desc);
				returnJsonMap.replace("formData", jsonMap);

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	// 修改;
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strComContent;
		}
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 保存数据;
				String str_bj_id = jacksonUtil.getString("bj_id");
				String str_bmlc_id = jacksonUtil.getString("ry_lc");
				String str_bmb_id1 = jacksonUtil.getString("bmb_id");
				if (str_bmb_id1 != null && !"".equals(str_bmb_id1)) {
					String[] arr_bmb_id = str_bmb_id1.split(",");
					if (arr_bmb_id != null && arr_bmb_id.length > 0) {
						String str_jg_id = jacksonUtil.getString("jg_id");
						String str_plgb_area = jacksonUtil.getString("plgb_area");
						for (int i = 0; i < arr_bmb_id.length; i++) {
							long appinsId = Long.parseLong(arr_bmb_id[i]);
							PsTzAppproRstTKey psTzAppproRstTkey = new PsTzAppproRstTKey();
							psTzAppproRstTkey.setTzAppInsId(appinsId);
							psTzAppproRstTkey.setTzAppproId(str_bmlc_id);
							psTzAppproRstTkey.setTzClassId(str_bj_id);
							PsTzAppproRstT psTzAppproRstT = psTzAppproRstTMapper.selectByPrimaryKey(psTzAppproRstTkey);
							if (psTzAppproRstT != null) {
								psTzAppproRstT.setTzAppproHfBh(str_jg_id);
								psTzAppproRstT.setTzAppproRst(str_plgb_area);
								psTzAppproRstT.setRowLastmantDttm(new Date());
								psTzAppproRstT.setRowLastmantOprid(oprid);
								psTzAppproRstTMapper.updateByPrimaryKeySelective(psTzAppproRstT);
							} else {
								psTzAppproRstT = new PsTzAppproRstT();
								psTzAppproRstT.setTzClassId(str_bj_id);
								psTzAppproRstT.setTzAppproId(str_bmlc_id);
								psTzAppproRstT.setTzAppInsId(appinsId);
								psTzAppproRstT.setTzAppproHfBh(str_jg_id);
								psTzAppproRstT.setTzAppproRst(str_plgb_area);
								psTzAppproRstT.setRowAddedDttm(new Date());
								psTzAppproRstT.setRowAddedOprid(oprid);
								psTzAppproRstT.setRowLastmantDttm(new Date());
								psTzAppproRstT.setRowLastmantOprid(oprid);
								psTzAppproRstTMapper.insert(psTzAppproRstT);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strComContent;
	}

	// 根据不同的结果获取公布结果;
	@Override
	public String tzGetJsonData(String strParams) {
		Map<String, Object> returnMap = new HashMap<>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_bmlc_id = jacksonUtil.getString("bmlc_id");
			String str_old_value = jacksonUtil.getString("old_value");
			String str_new_value = jacksonUtil.getString("new_value");
			String str_desc = "";
			String str_content = "1";
			if (!"null".equals(str_old_value)) {
				String sql = "SELECT TZ_APPPRO_CONTENT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH=?";
				str_desc = jdbcTemplate.queryForObject(sql, new Object[] { str_bj_id, str_bmlc_id, str_new_value},
						"String");
			} else {
				str_content = "2";
			}
			returnMap.put("gbjg_pd", str_content);
			returnMap.put("gbjg_desc", str_desc);
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return jacksonUtil.Map2json(returnMap);
	}
}
