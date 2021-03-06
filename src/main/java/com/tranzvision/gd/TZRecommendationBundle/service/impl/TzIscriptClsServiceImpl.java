package com.tranzvision.gd.TZRecommendationBundle.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppEngineImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * PS:TZ_GD_TJX_PKG:TZ_ISCRIPT_CLS
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZRecommendationBundle.service.impl.TzIscriptClsServiceImpl")
public class TzIscriptClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private TzTjxClsServiceImpl tzTjxClsServiceImpl;
	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private tzOnlineAppEngineImpl tzOnlineAppEngineImpl;
	

	/* 推荐信接口 */
	@Override
	public String tzOther(String operateType, String strParams, String[] errorMsg) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		String mess = "";
		try {
			jacksonUtil.json2Map(strParams);
			String strTjxType = "";
			String str_app_ins_version = "";
			String strTjrId = "", strEmail = "";
			String strTitle = "", strGname = "", strName = "", strCompany = "", strPosition = "", strPhone_area = "",
					strPhone_no = "", strGender = "", strAdd1 = "", strAdd2 = "", strAdd3 = "", strAdd4 = "",
					strAdd5 = "", strAdd6 = "", strAdd7 = "", strAdd8 = "", strAdd9 = "", strAdd10 = "", strTjrgx = "",
					str_sysfilename = "", str_filename = "", accessPath = "", tzAttAUrl = "";
			String strTzsqrFlg = "";
			String str_tjx_valid = "";

			String sendFlag = "";

			// 报名表ID;
			long numAppinsId = Long.parseLong(jacksonUtil.getString("rec_app_ins_id"));
			
			String strTplId = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T A WHERE TZ_APP_INS_ID = ?",
					new Object[] { numAppinsId }, "String");
			
			if (!"DELETE".equals(operateType)) {
				// 推荐人编号;
				strTjrId = jacksonUtil.getString("rec_num");

				strEmail = jacksonUtil.getString("rec_email");

				strTjxType = jacksonUtil.getString("rec_language");
				strTitle = jacksonUtil.getString("rec_title");
				strGname = jacksonUtil.getString("rec_gname");
				strName = jacksonUtil.getString("rec_name");
				strCompany = jacksonUtil.getString("rec_company");
				strPosition = jacksonUtil.getString("rec_post");
				strPhone_area = jacksonUtil.getString("rec_phone_area");
				strPhone_no = jacksonUtil.getString("rec_phone_no");
				strGender = jacksonUtil.getString("rec_sex");

				strAdd1 = "";
				strAdd2 = "";
				strAdd3 = "";
				strAdd4 = "";
				strAdd5 = "";
				strAdd6 = "";
				strAdd7 = "";
				strAdd8 = "";
				strAdd9 = "";
				strAdd10 = "";

				/**/
				String str_tpl_lang = jdbcTemplate.queryForObject(
						"SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID",
						new Object[] { numAppinsId }, "String");
				String str_value_s = "", str_value_l = "";
				Map<String, Object> zhzMap = jdbcTemplate.queryForMap(
						"SELECT TZ_ZHZ_DMS,TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_APP_REF_TITLE' AND TZ_ZHZ_ID = ?",
						new Object[] { strTitle });
				if (zhzMap != null) {
					str_value_s = (String) zhzMap.get("TZ_ZHZ_DMS");
					str_value_l = (String) zhzMap.get("TZ_ZHZ_CMS");
					if ("ENG".equals(str_tpl_lang)) {
						strTitle = str_value_l;
						if (strTjxType == null || "".equals(strTjxType)) {
							strTjxType = "E";
						}
					} else {
						strTitle = str_value_s;
					}
				}
				if (jacksonUtil.containsKey("tjx_valid")) {
					str_tjx_valid = jacksonUtil.getString("tjx_valid");
				} else {
					str_tjx_valid = "Y";
				}

				if (jacksonUtil.containsKey("rec_by1")) {
					strAdd1 = jacksonUtil.getString("rec_by1");
				} else {
					strAdd1 = "";
				}

				if (jacksonUtil.containsKey("rec_by2")) {
					strAdd2 = jacksonUtil.getString("rec_by2");
				} else {
					strAdd2 = "";
				}

				if (jacksonUtil.containsKey("rec_by3")) {
					strAdd3 = jacksonUtil.getString("rec_by3");
				} else {
					strAdd3 = "";
				}

				if (jacksonUtil.containsKey("rec_by4")) {
					strAdd4 = jacksonUtil.getString("rec_by4");
				} else {
					strAdd4 = "";
				}

				if (jacksonUtil.containsKey("rec_by5")) {
					strAdd5 = jacksonUtil.getString("rec_by5");
				} else {
					strAdd5 = "";
				}

				if (jacksonUtil.containsKey("rec_by6")) {
					strAdd6 = jacksonUtil.getString("rec_by6");
				} else {
					strAdd6 = "";
				}

				if (jacksonUtil.containsKey("rec_by7")) {
					strAdd7 = jacksonUtil.getString("rec_by7");
				} else {
					strAdd7 = "";
				}

				if (jacksonUtil.containsKey("rec_by8")) {
					strAdd8 = jacksonUtil.getString("rec_by8");
				} else {
					strAdd8 = "";
				}

				if (jacksonUtil.containsKey("rec_by9")) {
					strAdd9 = jacksonUtil.getString("rec_by9");
				} else {
					strAdd9 = "";
				}

				if (jacksonUtil.containsKey("rec_by10")) {
					strAdd10 = jacksonUtil.getString("rec_by10");
				} else {
					strAdd10 = "";
				}

				str_filename = "";
				if (jacksonUtil.containsKey("filename")) {
					str_filename = jacksonUtil.getString("filename");
				} else {
					str_filename = "";
				}

				str_sysfilename = "";
				if (jacksonUtil.containsKey("sysfilename")) {
					str_sysfilename = jacksonUtil.getString("sysfilename");
				} else {
					str_sysfilename = "";
				}

				accessPath = "";
				if (jacksonUtil.containsKey("accessPath")) {
					accessPath = jacksonUtil.getString("accessPath");
				} else {
					accessPath = "";
				}
				tzAttAUrl = "";
				if (accessPath != null && !"".equals(accessPath)) {
					tzAttAUrl = request.getServletContext().getRealPath(accessPath);
				}

				strTjrgx = jacksonUtil.getString("rec_relation");

				// 是否邮件通知申请人;
				strTzsqrFlg = jacksonUtil.getString("email_tx");
				str_app_ins_version = jacksonUtil.getString("TZ_APP_INS_VERSION");

			}

			String str_app_ins_version_db = "";
			String str_refLetterType = "";
			//String strOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			/*OPRID应该获取报名表工作表中的OPRID*/
			String strOprid = jdbcTemplate.queryForObject(
					"SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? limit 0,1",
					new Object[] { numAppinsId }, "String");
			
			if (numAppinsId == 0) {
				if ("E".equals(strTjxType)) {
					mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "SEND_FAILD",
							"ENG", "", "");
				} else {
					mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "SEND_FAILD",
							"CHN", "", "");
				}

				return "\"" + mess + "\"";
			}

			/* 当前报名表实例版本是否和数据库一致 */
			if (!"DELETE".equals(operateType) && !"SAVE".equals(operateType)) {
				if (numAppinsId > 0) {
					str_app_ins_version_db = jdbcTemplate.queryForObject(
							"SELECT TZ_APP_INS_VERSION FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ? limit 0,1",
							new Object[] { numAppinsId }, "String");
				}
				if (!str_app_ins_version_db.equals(str_app_ins_version)) {
					if ("E".equals(strTjxType)) {
						mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET",
								"PAGE_INVALID", "ENG", "", "");
					} else {
						mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET",
								"PAGE_INVALID", "CHN", "", "");
					}

					return "\"" + mess + "\"";
				}
			}
			// System.out.println(operateType);
			if ("SEND".equals(operateType)) {
				// modity by caoy 推荐信点击重新发送，重新转发的时候，如果是同一个推荐人，
				// 则需要判断一下发送间隔，如果是10分钟之内刚发送，则需要提示，您发送的邮件间隔太短了，在XXX点之前您不能在重复给XXX邮箱发送邮件。
				// N:发送给自己 Y：发送给推荐人
				String email = "";
				if (jacksonUtil.containsKey("send_falg")) {
					sendFlag = jacksonUtil.getString("send_falg");
				} else {
					sendFlag = "Y";
				}
				if (sendFlag.equals("N")) {
					email = jdbcTemplate.queryForObject(
							"SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?",
							new Object[] { strOprid }, "String");
				} else {
					email = strEmail;
				}
				// System.out.println("sendFlag:"+sendFlag);
				// System.out.println("email:"+email);
				String sql = "SELECT TZ_FS_DT FROM PS_TZ_TJX_FSRZ_TBL WHERE TZ_APP_INS_ID=? AND OPRID=? AND TZ_FS_ZT=? AND TZ_JS_EMAIL=? order by TZ_FS_DT desc limit 0,1";
				String sendTime = jdbcTemplate.queryForObject(sql, new Object[] { numAppinsId, strOprid, "SUC", email },
						"String");
				boolean flag = true;
				// System.out.println("sendTime:" + sendTime);
				if (sendTime != null && !sendTime.equals("")) {
					// 2017-03-13 17:30:17.0
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(
							com.tranzvision.gd.util.Calendar.DateUtil.parseTimeStamp(sendTime.substring(0, 19)));
					calendar.add(Calendar.MINUTE, 10);
					java.util.Date checkTime = calendar.getTime();

					java.util.Date now = new java.util.Date();

					/*临时注释*/
					if (checkTime.after(now)) {
						mess = "您发送的邮件间隔太短了，在" + com.tranzvision.gd.util.Calendar.DateUtil.formatLongDate(checkTime)
								+ "之前您不能在重复给" + email + "邮箱发送邮件。";
						flag = false;
					}
					 
				}

				if (flag) {
					// 保存并发送邮件;
					str_refLetterType = "S";
					mess = tzTjxClsServiceImpl.saveTJX(numAppinsId, strOprid, strTjrId, strEmail, strTjxType, strTitle,
							strGname, strName, strCompany, strPosition, strPhone_area, strPhone_no, strGender, strAdd1,
							strAdd2, strAdd3, strAdd4, strAdd5, strAdd6, strAdd7, strAdd8, strAdd9, strAdd10, strTjrgx,
							str_sysfilename, str_filename, "S", "Y", accessPath, tzAttAUrl);
					// System.out.println("mess:" + mess);
					if ("SUCCESS".equals(mess)) {

						// System.out.println("sendFlag:" + sendFlag);
						mess = tzTjxClsServiceImpl.sendTJX(numAppinsId, strOprid, strTjrId, sendFlag);

						// 写入推荐信发送日志表
						if ("SUCCESS".equals(mess)) {
							tzTjxClsServiceImpl.sendTJXLog(numAppinsId, strOprid, strTjrId, email, "SUC");

							// N:发送给自己 Y：发送给推荐人
							// 推荐人推荐信发出 发送站内信
							if (sendFlag.equals("Y")) {
								tzTjxClsServiceImpl.sendSiteEmail(numAppinsId, "TZ_TJX_SEND", "推荐人推荐信发出提醒", "TJXS",
										tzTjxClsServiceImpl.tjxId);
							}

						} else {
							tzTjxClsServiceImpl.sendTJXLog(numAppinsId, strOprid, strTjrId, email, "FAIL");
						}

						// System.out.println("mess:" + mess);
						if ("Y".equals(strTzsqrFlg)) {
							// 发送邮件通知给申请人;
							if ("SUCCESS".equals(mess)) {
								tzTjxClsServiceImpl.sendTZ(numAppinsId, strOprid, strTjrId);
							}
						}
					}
				}
			}

			if ("CHANGE".equals(operateType)) {
				// 更换推荐人;
				mess = tzTjxClsServiceImpl.changeTJR(numAppinsId, strTjrId, strOprid);
				/*检查推荐信*/
				tzOnlineAppEngineImpl.checkRefletter(numAppinsId, strTplId);
			}

			if ("DELETE".equals(operateType)) {
				/*
				jdbcTemplate.update("DELETE FROM PS_TZ_KS_TJX_TBL  WHERE TZ_APP_INS_ID = ? AND TZ_MBA_TJX_YX = 'N'",
						new Object[] { numAppinsId });
				jdbcTemplate.update("DELETE FROM PS_TZ_KS_TJX_TBL  WHERE TZ_APP_INS_ID = ? AND (TZ_REFLETTERTYPE = 'U' OR TZ_REFLETTERTYPE = '')",
						new Object[] { numAppinsId });
						*/
				// 删除无效的推荐信;
				String getRefLetterSQL = "SELECT TZ_REF_LETTER_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID = ? AND (TZ_MBA_TJX_YX = 'N' OR TZ_REFLETTERTYPE = 'U' OR TZ_REFLETTERTYPE = '')";
				List<Map<String, Object>> getRefLetterList = jdbcTemplate.queryForList(getRefLetterSQL, new Object[] { numAppinsId });
				if (getRefLetterList != null && getRefLetterList.size() > 0) {
					for (int j = 0; j < getRefLetterList.size(); j++) {
						String strRefLetterId = (String) getRefLetterList.get(j).get("TZ_REF_LETTER_ID");
						jdbcTemplate.update("DELETE FROM PS_TZ_KS_TJX_TBL  WHERE TZ_REF_LETTER_ID = ?",
								new Object[] { strRefLetterId });
					}
				}
			}

			if ("SAVE".equals(operateType)) {
				str_refLetterType = jacksonUtil.getString("rec_type");
				String str_update = "", str_ref_id = "";
				Map<String, Object> map = jdbcTemplate.queryForMap(
						"SELECT 'Y' IS_EXIST,TZ_REF_LETTER_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_TJR_ID=? limit 0,1",
						new Object[] { numAppinsId, strTjrId });
				if (map != null) {
					str_update = (String) map.get("IS_EXIST");
					str_ref_id = (String) map.get("TZ_REF_LETTER_ID");
				}
				if ("Y".equals(str_update)) {
					PsTzKsTjxTbl psTzKsTjxTbl = psTzKsTjxTblMapper.selectByPrimaryKey(str_ref_id);
					if (psTzKsTjxTbl != null) {
						psTzKsTjxTbl.setTzRefLetterId(str_ref_id);
						psTzKsTjxTbl.setTzAppInsId(numAppinsId);
						psTzKsTjxTbl.setOprid(strOprid);
						if(!"".equals(strTjxType)){	
							psTzKsTjxTbl.setTzTjxType(strTjxType);
						}
						psTzKsTjxTbl.setTzTjrId(strTjrId);
						psTzKsTjxTbl.setTzMbaTjxYx(str_tjx_valid);
						psTzKsTjxTbl.setTzTjxTitle(strTitle);
						psTzKsTjxTbl.setTzReferrerGname(strGname);
						psTzKsTjxTbl.setTzReferrerName(strName);
						psTzKsTjxTbl.setTzCompCname(strCompany);
						psTzKsTjxTbl.setTzPosition(strPosition);
						psTzKsTjxTbl.setTzEmail(strEmail);
						psTzKsTjxTbl.setTzPhoneArea(strPhone_area);
						psTzKsTjxTbl.setTzPhone(strPhone_no);
						psTzKsTjxTbl.setTzGender(strGender);
						if(!"".equals(str_refLetterType)){
							psTzKsTjxTbl.setTzReflettertype(str_refLetterType);
						}
						psTzKsTjxTbl.setTzTjxYl1(strAdd1);
						psTzKsTjxTbl.setTzTjxYl2(strAdd2);
						psTzKsTjxTbl.setTzTjxYl3(strAdd3);
						psTzKsTjxTbl.setTzTjxYl4(strAdd4);
						psTzKsTjxTbl.setTzTjxYl5(strAdd5);
						psTzKsTjxTbl.setTzTjxYl6(strAdd6);
						psTzKsTjxTbl.setTzTjxYl7(strAdd7);
						psTzKsTjxTbl.setTzTjxYl8(strAdd8);
						psTzKsTjxTbl.setTzTjxYl9(strAdd9);
						psTzKsTjxTbl.setTzTjxYl10(strAdd10);
						psTzKsTjxTbl.setTzTjrGx(strTjrgx);

						psTzKsTjxTbl.setAttachsysfilename(str_sysfilename);
						psTzKsTjxTbl.setAttachuserfile(str_filename);
						psTzKsTjxTbl.setTzAccessPath(accessPath);
						psTzKsTjxTbl.setTzAttAUrl(tzAttAUrl);

						psTzKsTjxTbl.setRowLastmantDttm(new Date());
						psTzKsTjxTbl.setRowLastmantOprid(strOprid);
						psTzKsTjxTblMapper.updateByPrimaryKeySelective(psTzKsTjxTbl);
					}
				} else {
					mess = tzTjxClsServiceImpl.saveTJX(numAppinsId, strOprid, strTjrId, strEmail, strTjxType, strTitle,
							strGname, strName, strCompany, strPosition, strPhone_area, strPhone_no, strGender, strAdd1,
							strAdd2, strAdd3, strAdd4, strAdd5, strAdd6, strAdd7, strAdd8, strAdd9, strAdd10, strTjrgx,
							str_sysfilename, str_filename, str_refLetterType, str_tjx_valid, accessPath, tzAttAUrl);
				}
				/*检查推荐信*/
				tzOnlineAppEngineImpl.checkRefletter(numAppinsId, strTplId);
			}

		} catch (Exception e) {
			mess = "发生错误";
			e.printStackTrace();
		}
		return "\"" + mess + "\"";
	}

	@Override
	public String tzGetJsonData(String strParams) {
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("TJX_ZT", "");
		returnMap.put("zhs_qy", "");
		returnMap.put("eng_qy", "");
		returnMap.put("tjxAppInsID", "");
		returnMap.put("refLetterId", "");
		returnMap.put("refAppTplId", "");
		returnMap.put("refFileName", "");
		returnMap.put("refFileUrl", "");
		returnMap.put("viewFileName", "");
		returnMap.put("recommend_18", "");
		returnMap.put("recommend_1", "");
		returnMap.put("recommend_17", "");
		returnMap.put("recommend_2", "");
		returnMap.put("recommend_3", "");
		returnMap.put("recommend_15", "");
		returnMap.put("recommend_16", "");
		returnMap.put("recommend_4", "");
		returnMap.put("recommend_5", "");
		returnMap.put("recommend_6", "");
		returnMap.put("recommend_10", "");
		returnMap.put("recommend_11", "");
		returnMap.put("recommend_12", "");
		returnMap.put("recommend_13", "");
		returnMap.put("recommend_14", "");
		returnMap.put("recommend_19", "");
		returnMap.put("recommend_20", "");
		returnMap.put("recommend_21", "");
		returnMap.put("recommend_22", "");
		returnMap.put("recommend_23", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			/* 将字符串转换成json */
			long str_app_ins_id = Long.parseLong(jacksonUtil.getString("APP_INS_ID"));
			String str_rownum = jacksonUtil.getString("rownum");
			String str_class_id = jacksonUtil.getString("class_id");
			String str_app_tpl_id = jacksonUtil.getString("TZ_APP_TPL_ID");
			long str_tjx_app_ins_id = 0L;
			String str_y = "", str_tjx_zt = "", str_ref_letter_id = "";
			String str_tjx_language = "";
			String str_tjx_app_tpl_id = "";
			String str_refLetterSysFile = "", str_refLetterUserFile = "";
			String str_att_a_url = "";
			String strRefType = "";
			String strRecommend_1 = "";
			String strRecommend_2 = "";
			String strRecommend_3 = "";
			String strRecommend_4 = "";
			String strRecommend_5 = "";
			String strRecommend_6 = "";
			String strRecommend_7 = "";
			String strRecommend_8 = "";
			String strRecommend_9 = "";
			String strRecommend_10 = "";
			String strRecommend_11 = "";
			String strRecommend_12 = "";
			String strRecommend_13 = "";
			String strRecommend_14 = "";
			String strRecommend_15 = "";
			String strRecommend_16 = "";
			String strRecommend_17 = "";
			String strRecommend_18 = "";
			String strRecommend_19 = "";
			String strRecommend_20 = "";
			String strRecommend_21 = "";
			String strRecommend_22 = "";
			String strRecommend_23 = "";
			String sql = "SELECT TZ_TJX_APP_INS_ID,'Y' STR_Y,TZ_REF_LETTER_ID,TZ_TJX_TYPE,TZ_REFLETTERTYPE,ATTACHSYSFILENAME,ATTACHUSERFILE,TZ_ACCESS_PATH,"
					+ " TZ_REFERRER_NAME,TZ_REFERRER_GNAME,TZ_COMP_CNAME,TZ_POSITION,TZ_TJX_TITLE,TZ_TJR_GX,TZ_EMAIL,TZ_PHONE_AREA,TZ_PHONE,TZ_GENDER,"
					+ " TZ_TJX_YL_1,TZ_TJX_YL_2,TZ_TJX_YL_3,TZ_TJX_YL_4,TZ_TJX_YL_5,TZ_TJX_YL_6,TZ_TJX_YL_7,TZ_TJX_YL_8,TZ_TJX_YL_9,TZ_TJX_YL_10"
					+ " FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y' AND TZ_TJR_ID=? limit 0,1";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { str_app_ins_id, str_rownum });
			if (map != null) {
				try {
					String tjx_ins_id = map.get("TZ_TJX_APP_INS_ID") == null ? "0"
							: String.valueOf(map.get("TZ_TJX_APP_INS_ID"));
					if ("".equals(tjx_ins_id)) {
						tjx_ins_id = "0";
					}
					str_tjx_app_ins_id = Long.parseLong(tjx_ins_id);
				} catch (Exception e) {
					e.printStackTrace();
					str_tjx_app_ins_id = 0L;
				}

				str_y = (String) map.get("STR_Y");
				str_ref_letter_id = (String) map.get("TZ_REF_LETTER_ID");
				str_tjx_language = (String) map.get("TZ_TJX_TYPE");
				str_refLetterSysFile = (String) map.get("ATTACHSYSFILENAME");
				str_refLetterUserFile = (String) map.get("ATTACHUSERFILE");
				str_att_a_url = (String) map.get("TZ_ACCESS_PATH");
				strRefType = (String) map.get("TZ_REFLETTERTYPE");
				strRecommend_18 =  map.get("TZ_TJX_TITLE")== null?"":String.valueOf(map.get("TZ_TJX_TITLE"));
				strRecommend_1 =  map.get("TZ_REFERRER_NAME")== null?"":String.valueOf(map.get("TZ_REFERRER_NAME"));
				strRecommend_17 =  map.get("TZ_REFERRER_GNAME")== null?"":String.valueOf(map.get("TZ_REFERRER_GNAME"));
				strRecommend_2 =  map.get("TZ_COMP_CNAME")== null?"":String.valueOf(map.get("TZ_COMP_CNAME"));
				strRecommend_3 =  map.get("TZ_POSITION")== null?"":String.valueOf(map.get("TZ_POSITION"));
				strRecommend_15 =  map.get("TZ_GENDER")== null?"":String.valueOf(map.get("TZ_GENDER"));
				strRecommend_16 =  map.get("TZ_PHONE_AREA")== null?"":String.valueOf(map.get("TZ_PHONE_AREA"));
				strRecommend_4 =  map.get("TZ_PHONE")== null?"":String.valueOf(map.get("TZ_PHONE"));
				strRecommend_5 =  map.get("TZ_EMAIL")== null?"":String.valueOf(map.get("TZ_EMAIL"));
				strRecommend_6 =  map.get("TZ_TJR_GX")== null?"":String.valueOf(map.get("TZ_TJR_GX"));
				strRecommend_10 =  map.get("TZ_TJX_YL_1")== null?"":String.valueOf(map.get("TZ_TJX_YL_1"));
				strRecommend_11 =  map.get("TZ_TJX_YL_2")== null?"":String.valueOf(map.get("TZ_TJX_YL_2"));
				strRecommend_12 =  map.get("TZ_TJX_YL_3")== null?"":String.valueOf(map.get("TZ_TJX_YL_3"));
				strRecommend_13 =  map.get("TZ_TJX_YL_4")== null?"":String.valueOf(map.get("TZ_TJX_YL_4"));
				strRecommend_14 =  map.get("TZ_TJX_YL_5")== null?"":String.valueOf(map.get("TZ_TJX_YL_5"));
				strRecommend_19 =  map.get("TZ_TJX_YL_6")== null?"":String.valueOf(map.get("TZ_TJX_YL_6"));
				strRecommend_20 =  map.get("TZ_TJX_YL_7")== null?"":String.valueOf(map.get("TZ_TJX_YL_7"));
				strRecommend_21 =  map.get("TZ_TJX_YL_8")== null?"":String.valueOf(map.get("TZ_TJX_YL_8"));
				strRecommend_22 =  map.get("TZ_TJX_YL_9")== null?"":String.valueOf(map.get("TZ_TJX_YL_9"));
				strRecommend_23 =  map.get("TZ_TJX_YL_10")== null?"":String.valueOf(map.get("TZ_TJX_YL_10"));

			}

			if ("Y".equals(str_y)) {
				if (str_tjx_app_ins_id > 0) {
					str_tjx_zt = jdbcTemplate.queryForObject(
							"SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=? limit 0,1",
							new Object[] { str_tjx_app_ins_id }, "String");
					if ("U".equals(str_tjx_zt)) {
						str_tjx_zt = "已完成";
					} else {
						if (str_att_a_url != null && !"".equals(str_att_a_url)) {
							str_tjx_zt = "已完成";
						} else {
							if("".equals(strRefType)){
								str_tjx_app_ins_id = 0;
								str_ref_letter_id = "0";
								str_tjx_zt = "未发送";
							}else{
								str_tjx_app_ins_id = 0;
								str_ref_letter_id = "0";
								str_tjx_zt = "已发送";
							}
						}
					}
				} else {
					if("".equals(strRefType)){
						str_tjx_zt = "未发送";
					}else{
						str_tjx_zt = "已发送";
					}
				}
			}

			if (str_tjx_zt == null || "".equals(str_tjx_zt)) {
				str_tjx_zt = "未发送";
			}

			String str_qy_zhs = "", str_qy_eng = "";

			Map<String, Object> map2 = jdbcTemplate.queryForMap(
					"SELECT B.TZ_CHN_QY,B.TZ_ENG_QY FROM PS_TZ_CLASS_INF_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_CLASS_ID=? AND A.TZ_APP_MODAL_ID=B.TZ_APP_TPL_ID limit 0,1",
					new Object[] { str_class_id });
			if (map2 != null) {
				str_qy_zhs = (String) map2.get("TZ_CHN_QY");
				str_qy_eng = (String) map2.get("TZ_ENG_QY");
			}
			if (str_qy_zhs == null) {
				str_qy_zhs = "";
			}
			if (str_qy_eng == null) {
				str_qy_eng = "";
			}

			if ("C".equals(str_tjx_language)) {
				str_tjx_app_tpl_id = jdbcTemplate.queryForObject(
						"SELECT TZ_CHN_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? limit 0,1",
						new Object[] { str_app_tpl_id }, "String");
			} else {
				str_tjx_app_tpl_id = jdbcTemplate.queryForObject(
						"SELECT TZ_ENG_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? limit 0,1",
						new Object[] { str_app_tpl_id }, "String");
			}
			if (str_tjx_app_tpl_id == null) {
				str_tjx_app_tpl_id = "";
			}
			if (str_refLetterSysFile == null) {
				str_refLetterSysFile = "";
			}
			if (str_refLetterUserFile == null) {
				str_refLetterUserFile = "";
			}
			if (str_att_a_url == null) {
				str_att_a_url = "";
			}
			if ("".equals(str_refLetterSysFile) || "".equals(str_refLetterUserFile) || "".equals(str_att_a_url)) {
				str_refLetterSysFile = "";
				str_refLetterUserFile = "";
				str_att_a_url = "";
			}
			returnMap.replace("TJX_ZT", str_tjx_zt);
			returnMap.replace("zhs_qy", str_qy_zhs);
			returnMap.replace("eng_qy", str_qy_eng);
			returnMap.replace("tjxAppInsID", String.valueOf(str_tjx_app_ins_id));
			returnMap.replace("refLetterId", str_ref_letter_id);
			returnMap.replace("refAppTplId", str_tjx_app_tpl_id);
			returnMap.replace("refFileName", str_refLetterSysFile);
			returnMap.replace("refFileUrl", str_att_a_url);
			returnMap.replace("viewFileName", str_refLetterUserFile);
			returnMap.replace("recommend_18", strRecommend_18);
			returnMap.replace("recommend_1", strRecommend_1);
			returnMap.replace("recommend_17", strRecommend_17);
			returnMap.replace("recommend_2", strRecommend_2);
			returnMap.replace("recommend_3", strRecommend_3);
			returnMap.replace("recommend_15", strRecommend_15);
			returnMap.replace("recommend_16", strRecommend_16);
			returnMap.replace("recommend_4", strRecommend_4);
			returnMap.replace("recommend_5", strRecommend_5);
			returnMap.replace("recommend_6", strRecommend_6);
			returnMap.replace("recommend_10", strRecommend_10);
			returnMap.replace("recommend_11", strRecommend_11);
			returnMap.replace("recommend_12", strRecommend_12);
			returnMap.replace("recommend_13", strRecommend_13);
			returnMap.replace("recommend_14", strRecommend_14);
			returnMap.replace("recommend_19", strRecommend_19);
			returnMap.replace("recommend_20", strRecommend_20);
			returnMap.replace("recommend_21", strRecommend_21);
			returnMap.replace("recommend_22", strRecommend_22);
			returnMap.replace("recommend_23", strRecommend_23);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(returnMap);
	}

	/*
	 * private String getRefLetterFiles(long str_app_ins_id,String sysFileName){
	 * String url = ""; if(sysFileName == null || "".equals(sysFileName)){
	 * return ""; }else{ url = jdbcTemplate.queryForObject(
	 * "SELECT TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND ATTACHSYSFILENAME=?"
	 * , new Object[]{str_app_ins_id,sysFileName},"String"); if(url != null &&
	 * !"".equals(url)){ if(url.lastIndexOf("/") + 1 != url.length()){ url = url
	 * + "/"; } if(request.getServerPort() == 80){ url = "http://" +
	 * request.getServerName() + request.getContextPath() + url + sysFileName;
	 * }else{ url = "http://" + request.getServerName() + ":" +
	 * request.getServerPort() + request.getContextPath() + url + sysFileName; }
	 * }
	 * 
	 * 
	 * } return url; }
	 */

	@Override
	public String tzGetHtmlContent(String strParams) {
		String reutrnHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			/* 报名表实例ID */
			long strAppInsId = Long.parseLong(jacksonUtil.getString("tz_app_ins_id"));
			/* PDF地址 */
			String pdfFileUrl = jacksonUtil.getString("pdfFileUrl");

			/* 窗口宽度、高度 */
			// String winWidth = jacksonUtil.getString("winWidth");
			String winHeight = jacksonUtil.getString("winHeight");

			String lan = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A left join PS_TZ_APPTPL_DY_T B on  A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID WHERE TZ_APP_INS_ID=? limit 0,1",
					new Object[] { strAppInsId }, "String");

			// 双语化;
			String readerTitle = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET",
					"PDF_VIEW", lan, "PDF预览", "PDF preview");
			// 文件FTP临时存放路径/export/home/PT852/webserv/ALTZDEV/applications/peoplesoft/PORTAL.war//linkfile/FileUpLoad/appFormAttachment/;
			int height = Integer.parseInt(winHeight) - 40;
			reutrnHtml = tzGdObject.getHTMLText("HTML.TZRecommendationBundle.TZ_PDF_READER_HTML", pdfFileUrl,
					readerTitle, String.valueOf(height), request.getContextPath());
		} catch (Exception e) {
			e.printStackTrace();
			reutrnHtml = "";
		}
		return reutrnHtml;
	}

}
