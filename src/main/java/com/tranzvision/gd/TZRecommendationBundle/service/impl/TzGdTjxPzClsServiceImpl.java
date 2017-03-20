package com.tranzvision.gd.TZRecommendationBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS:TZ_GD_TJX_PKG:TZ_GD_TJX_PZ_CLS
 * 
 * @author tang
 * 
 */
@Service("com.tranzvision.gd.TZRecommendationBundle.service.impl.TzGdTjxPzClsServiceImpl")
public class TzGdTjxPzClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzApptplDyTMapper psTzAppTplDyTMapper;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			if (!"1".equals(errMsg[0])) {
				jacksonUtil.json2Map(strParams);
				String str_app_id = jacksonUtil.getString("tz_app_id");
				String str_language = jacksonUtil.getString("tz_language");
				Map<String, Object> map = new HashMap<>();
				if ("C".equals(str_language)) {
					map = jdbcTemplate.queryForMap(
							"SELECT TZ_CHN_MODAL_ID TZ_MODAL_ID,TZ_CH_M_CONTENT TZ_M_CONTENT,TZ_CHN_QY TZ_QY FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=? limit 0,1",
							new Object[] { str_app_id });
				} else {
					map = jdbcTemplate.queryForMap(
							"SELECT TZ_ENG_MODAL_ID TZ_MODAL_ID,TZ_EN_M_CONTENT TZ_M_CONTENT,TZ_ENG_QY TZ_QY FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=? limit 0,1",
							new Object[] { str_app_id });
				}
				String str_email = "", str_tjx_id = "", str_qy = "";
				if (map != null && map.isEmpty() == false) {
					str_email = (String) map.get("TZ_M_CONTENT");
					str_tjx_id = (String) map.get("TZ_MODAL_ID");
					str_qy = (String) map.get("TZ_QY");
				}
				if (str_qy == null || "".equals(str_qy)) {
					str_qy = "N";
				}

				String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);

				String sql_bmb = "SELECT TZ_APP_TPL_ID,TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_JG_ID=? AND TZ_APP_TPL_YT='REG' AND TZ_EFFEXP_ZT='Y' AND TZ_USE_TYPE='TJX'";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_bmb, new Object[] { str_jg_id });
				String str_bmb_id = "", str_bmb_name = "";
				String str_xz_desc = "<option value=''>请选择</option>";
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						str_bmb_id = (String) list.get(i).get("TZ_APP_TPL_ID");
						str_bmb_name = (String) list.get(i).get("TZ_APP_TPL_MC");
						if (str_bmb_id != null && str_bmb_id.equals(str_tjx_id)) {
							str_xz_desc = str_xz_desc + "<option value='" + str_bmb_id + "' selected='selected'>"
									+ str_bmb_name + "</option>";
						} else {
							str_xz_desc = str_xz_desc + "<option value='" + str_bmb_id + "'>" + str_bmb_name
									+ "</option>";
						}
					}
				}

				String sql_email_mb = "SELECT TZ_TMPL_ID,TZ_TMPL_NAME FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID=?";
				list = jdbcTemplate.queryForList(sql_email_mb, new Object[] { str_jg_id });
				String str_email_id = "", str_email_name = "";
				String str_email_desc = "<option value=''>请选择</option>";
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						str_email_id = (String) list.get(i).get("TZ_TMPL_ID");
						str_email_name = (String) list.get(i).get("TZ_TMPL_NAME");
						if (str_email_id != null && str_email_id.equals(str_email)) {
							str_email_desc = str_email_desc + "<option value='" + str_email_id
									+ "' selected='selected'>" + str_email_name + "</option>";
						} else {
							str_email_desc = str_email_desc + "<option value='" + str_email_id + "'>" + str_email_name
									+ "</option>";
						}
					}
				}

				Map<String, Object> tjxMap = new HashMap<>();
				tjxMap.put("tjx_xz", str_xz_desc);
				tjxMap.put("email_desc", str_email_desc);
				tjxMap.put("tjx_qy", str_qy);

				returnJsonMap.replace("formData", tjxMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 修改 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String str_bmb_id = jacksonUtil.getString("tz_app_id");
				String str_language = jacksonUtil.getString("tz_language");
				String str_tjx_id = jacksonUtil.getString("bmb_id");
				String str_email_desc = jacksonUtil.getString("email_desc");
				String str_tjx_qy = jacksonUtil.getString("tjx_qy");
				if (str_bmb_id != null && !"".equals(str_bmb_id)) {
					PsTzApptplDyTWithBLOBs psTzAppTplDyT = psTzAppTplDyTMapper.selectByPrimaryKey(str_bmb_id);
					if (psTzAppTplDyT != null) {
						if ("C".equals(str_language)) {
							psTzAppTplDyT.setTzChnModalId(str_tjx_id);
							psTzAppTplDyT.setTzChMContent(str_email_desc);
							psTzAppTplDyT.setTzChnQy(str_tjx_qy);
						} else {
							psTzAppTplDyT.setTzEngModalId(str_tjx_id);
							psTzAppTplDyT.setTzEnMContent(str_email_desc);
							psTzAppTplDyT.setTzEngQy(str_tjx_qy);
						}
						psTzAppTplDyTMapper.updateByPrimaryKeySelective(psTzAppTplDyT);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "更新数据时发生错误！";
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

	// 验证推荐信是否完成;
	public String yzTjx(String strAppInsId) {
		String str_return = "";
		int appInsId = 0;
		if (StringUtils.isNumeric(strAppInsId)) {
			appInsId = Integer.parseInt(strAppInsId);
		}
		String str_mb_id = jdbcTemplate.queryForObject(
				"SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=? limit 0,1", new Object[] { appInsId },
				"String");
		String str_language = jdbcTemplate.queryForObject(
				"SELECT TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=? limit 0,1",
				new Object[] { str_mb_id }, "String");

		// 查推荐信最小填写数目;
		int num_min = 0, num_no = 0;
		String str_yz = "";
		num_min = jdbcTemplate.queryForObject(
				"SELECT TZ_XXX_MIN_LINE FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID=? AND TZ_COM_LMC='recommendletter' limit 0,1",
				new Object[] { str_mb_id }, "Integer");
		// 查推荐信完成情况;
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT TZ_TJX_APP_INS_ID,ATTACHSYSFILENAME,ATTACHUSERFILE FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y'",
				new Object[] { appInsId });
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				int num_tjx_app_id = (int) list.get(i).get("TZ_TJX_APP_INS_ID");
				String str_fj_1 = (String) list.get(i).get("ATTACHSYSFILENAME");
				String str_fj_2 = (String) list.get(i).get("ATTACHUSERFILE");

				String str_tjx_zt = "";
				if (num_tjx_app_id > 0) {
					str_tjx_zt = jdbcTemplate.queryForObject(
							"SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=? limit 0,1",
							new Object[] { num_tjx_app_id }, "String");
					if ("U".equals(str_tjx_zt)) {
						num_no = num_no + 1;
					} else {
						str_yz = "a";
					}
				} else {
					if (str_fj_1 != null && !"".equals(str_fj_1) && str_fj_2 != null && !"".equals(str_fj_2)) {
						num_no = num_no + 1;
					} else {
						str_yz = "b";
					}
				}
			}
		}

		if ("a".equals(str_yz)) {
			// str_return = "发送的推荐信中有未提交";
			str_return = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "REF_NO_SUB",
					str_language, "发送的推荐信中有未提交", "发送的推荐信中有未提交");
		} else {
			if ("b".equals(str_yz)) {
				str_return = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET",
						"REF_NO_UPLOAD", str_language, "推荐信中有附件未上传", "推荐信中有附件未上传");
				// str_return = "推荐信中有附件未上传";
			} else {
				if (num_no < num_min) {
					str_return = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "REF_NUM",
							str_language, "完成的推荐信数目未达到最小数目要求（最小行数为", "完成的推荐信数目未达到最小数目要求（最小行数为") + num_min + "）";
					// str_return = "完成的推荐信数目未达到最小数目要求（最小行数为" | &num_min | "）";
				}
			}
		}

		if (str_return == null || "".equals(str_return)) {
			str_return = "success";
		}
		return str_return;

	}

	@Override
	// 获取推荐人信息;
	public String tzGetJsonData(String strParams) {
		Map<String, Object> returnMap = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			int appinsId = 0;
			String str_app_ins_id = jacksonUtil.getString("APP_INS_ID");
			if (StringUtils.isNumeric(str_app_ins_id)) {
				appinsId = Integer.parseInt(str_app_ins_id);
			}
			String str_ref_ins_id = jacksonUtil.getString("TZ_REF_LETTER_ID");

			String tjxSQL = "SELECT TZ_TJX_TITLE,TZ_REFERRER_GNAME,TZ_REFERRER_NAME,TZ_COMP_CNAME,TZ_POSITION,TZ_TJR_GX,TZ_EMAIL,TZ_PHONE_AREA,TZ_PHONE,TZ_GENDER,TZ_TJX_YL_1,TZ_TJX_YL_2,TZ_TJX_YL_3,TZ_TJX_YL_4,TZ_TJX_YL_5,TZ_TJX_YL_6,TZ_TJX_YL_7,TZ_TJX_YL_8,TZ_TJX_YL_9,TZ_TJX_YL_10,TZ_GENDER FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID=? AND TZ_TJX_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y'";
			Map<String, Object> tjxMap = jdbcTemplate.queryForMap(tjxSQL, new Object[] { str_ref_ins_id, appinsId });
			// TZ_TJX_TITLE, TZ_REFERRER_GNAME, TZ_REFERRER_NAME, TZ_COMP_CNAME,
			// TZ_POSITION, TZ_TJR_GX, TZ_EMAIL, TZ_PHONE_AREA, TZ_PHONE,
			// TZ_GENDER, TZ_TJX_YL_1, TZ_TJX_YL_2, TZ_TJX_YL_3, TZ_TJX_YL_4,
			// TZ_TJX_YL_5, TZ_GENDER
			// str_tjr_title, str_tjr_gname, str_tjr_name, str_tjr_company,
			// str_tjr_zw, str_tjr_gx, str_tjr_email, str_tjr_phone_area,
			// str_tjr_dh, str_tjr_xb, str_tjr_yl1, str_tjr_yl2, str_tjr_yl3,
			// str_tjr_yl4, str_tjr_yl5, str_sex
			String str_tjr_dh = "";
			// String str_name_suff = "";
			if (tjxMap != null) {
				String str_tjr_phone_area = tjxMap.get("TZ_PHONE_AREA") == null ? ""
						: String.valueOf(tjxMap.get("TZ_PHONE_AREA"));
				str_tjr_dh = tjxMap.get("TZ_PHONE") == null ? "" : String.valueOf(tjxMap.get("TZ_PHONE"));
				if (str_tjr_phone_area != null && !"".equals(str_tjr_phone_area) && str_tjr_dh != null
						&& !"".equals(str_tjr_dh)) {
					str_tjr_dh = str_tjr_phone_area + "-" + str_tjr_dh;
				}

				String str_name_suff = "";
				String str_none_blank = jdbcTemplate.queryForObject(
						"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
						new Object[] { "TZ_REF_TITLE_NONE_BLANK" }, "String");
				String str_none_blank_c = jdbcTemplate.queryForObject(
						"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
						new Object[] { "TZ_REF_TITLE_NONE_BLANK_C" }, "String");

				String str_tjr_title = tjxMap.get("TZ_TJX_TITLE") == null ? ""
						: String.valueOf(tjxMap.get("TZ_TJX_TITLE"));

				//if (str_tjr_title != null && !"".equals(str_tjr_title) && !str_tjr_title.equals(str_none_blank)
				//		&& !str_tjr_title.equals(str_none_blank_c)) {
				//	str_name_suff = str_tjr_title;
				//}

				String str_tjr_gname = tjxMap.get("TZ_REFERRER_GNAME") == null ? ""
						: String.valueOf(tjxMap.get("TZ_REFERRER_GNAME"));
				//if (str_tjr_gname != null && !"".equals(str_tjr_gname)) {
				//	if (str_name_suff != null && !"".equals(str_name_suff)) {
				//		str_name_suff = str_name_suff + " " + str_tjr_gname;
				//	} else {
				//		str_name_suff = str_tjr_gname;
				//	}
				//}

				String str_tjr_name = tjxMap.get("TZ_REFERRER_NAME") == null ? ""
						: String.valueOf(tjxMap.get("TZ_REFERRER_NAME"));

				//if (str_name_suff != null && !"".equals(str_name_suff)) {
				//	str_tjr_name = str_name_suff + " " + str_tjr_name;
				//}

				returnMap.put("TJR_NAME", str_tjr_name);
				returnMap.put("TJR_GNAME", str_tjr_gname);
				returnMap.put("TJR_COMPANY", tjxMap.get("TZ_COMP_CNAME"));
				returnMap.put("TJR_ZW", tjxMap.get("TZ_POSITION"));
				returnMap.put("TJR_GX", tjxMap.get("TZ_TJR_GX"));
				returnMap.put("TJR_EMAIL", tjxMap.get("TZ_EMAIL"));
				returnMap.put("TJR_PHONE", str_tjr_dh);
				returnMap.put("TJR_SEX", tjxMap.get("TZ_GENDER"));
				returnMap.put("TJR_YL1", tjxMap.get("TZ_TJX_YL_1"));
				returnMap.put("TJR_YL2", tjxMap.get("TZ_TJX_YL_2"));
				returnMap.put("TJR_YL3", tjxMap.get("TZ_TJX_YL_3"));
				returnMap.put("TJR_YL4", tjxMap.get("TZ_TJX_YL_4"));
				returnMap.put("TJR_YL5", tjxMap.get("TZ_TJX_YL_5"));
				returnMap.put("TJR_YL6", tjxMap.get("TZ_TJX_YL_6"));
				returnMap.put("TJR_YL7", tjxMap.get("TZ_TJX_YL_7"));
				returnMap.put("TJR_YL8", tjxMap.get("TZ_TJX_YL_8"));
				returnMap.put("TJR_YL9", tjxMap.get("TZ_TJX_YL_9"));
				returnMap.put("TJR_YL10", tjxMap.get("TZ_TJX_YL_10"));
				returnMap.put("TZ_SEX", tjxMap.get("TZ_GENDER"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(returnMap);
	}

}
