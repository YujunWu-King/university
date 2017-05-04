package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 *TZ_SMS_PREVIEW_PKG:TZ_SMS_PREVIEW_CLS 功能说明：短信群发预览
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzSmsPreviewClsServiceImpl")
public class TzSmsPreviewClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private AyalysisExcItemsValServiceImpl ayalysisExcItemsValServiceImpl;

	@Autowired
	private AyalysisMbSysVar ayalysisMbSysVar;

	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Override
	public String tzOther(String OperateType, String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		String type = "";
		if (jacksonUtil.containsKey("type")) {
			type = jacksonUtil.getString("type");
		}

		String returnString = "";
		if ("previewSMS".equals(type)) {
			returnString = this.previewSMS(comParams, errorMsg);
		}
		if ("previewOtherSMS".equals(type)) {
			returnString = this.previewOtherSMS(comParams, errorMsg);
		}
		if ("checkSmsAudience".equals(type)) {
			returnString = this.checkSmsAudience(comParams, errorMsg);
		}
		return returnString;
	}

	/* 从定义页面预览短信 */
	private String previewSMS(String comParams, String[] errorMsg) {
		// 返回;
		Map<String, Object> returnMap1 = new HashMap<>();
		Map<String, Object> returnMap2 = new HashMap<>();
		returnMap1.put("formData", returnMap2);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		// 机构编号;
		String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 邮件或短信群发ID;
		String sendPcId = jacksonUtil.getString("sendPcId");

		// 获取发送模式，若为true则是一般发送，false则是导入excel发送;
		String sendType = jacksonUtil.getString("sendType");
		// 当前预览的页面数字;
		String viewNum = jacksonUtil.getString("viewNumber");
		// 手动输入的邮箱;
		String keyInputEmail = jacksonUtil.getString("keyInputEmail");
		if (keyInputEmail != null && !"".equals(keyInputEmail)) {
			if(keyInputEmail.indexOf("[") == 0 && (keyInputEmail.lastIndexOf("]") + 1) == keyInputEmail.length()){
				keyInputEmail = keyInputEmail.substring(keyInputEmail.indexOf("[")+1, keyInputEmail.lastIndexOf("]"));
			}
		}
		// 添加听众;
		String audIDTotal = jacksonUtil.getString("audIDTotal");
		// 邮件主题;
		String emailTheme = jacksonUtil.getString("emailTheme");
		// 邮件模板;
		String emailModal = jacksonUtil.getString("emailModal");
		// 获取邮件中配置的模板;
		String emlTmpId = jacksonUtil.getString("emlTmpId");
		// 邮件内容;
		String emailContent = jacksonUtil.getString("emailContent");

		// 手动输入邮箱数组;
		String[] arr_str_input_email = null;
		if (keyInputEmail != null && !"".equals(keyInputEmail)) {
			arr_str_input_email = keyInputEmail.split(",");
		}

		// 选择的听众ID;
		String[] arr_str_choice_aud = null;
		if (audIDTotal != null && !"".equals(audIDTotal)) {
			arr_str_choice_aud = audIDTotal.split(",");
		}

		// 选择的听众ID对应的邮箱;
		String str_choice_email = "";
		String[] arr_str_choice_email = null;

		// 新添：获取听众集合解析出来的oprid;
		// String audienceOprID = jacksonUtil.getString("AudienceOprID");
		String really_totalAudienceOprID = "";

		String audID = "";
		// 当选择了听众作为收件人时;
		if (arr_str_choice_aud != null && arr_str_choice_aud.length > 0) {
			// 创建听众;
			audID = createTaskServiceImpl.createAudience("", orgID, "邮件群发", "QFRW");

			for (int num_aud = 0; num_aud < arr_str_choice_aud.length; num_aud++) {
				String id = arr_str_choice_aud[num_aud];
				if (id != null && !"".equals(id)) {
					String strAudSQL = "select a.OPRID,b.TZ_ZY_SJ,c.TZ_REALNAME FROM PS_TZ_AUD_LIST_T a, PS_TZ_LXFSINFO_TBL b,PS_TZ_AQ_YHXX_TBL c where a.TZ_LXFS_LY=b.TZ_LXFS_LY and a.TZ_LYDX_ID=b.TZ_LYDX_ID and a.OPRID=c.OPRID and a.TZ_AUD_ID=? and a.TZ_DXZT<>'N'";
					List<Map<String, Object>> audList = jdbcTemplate.queryForList(strAudSQL, new Object[] { id });
					if (audList != null && audList.size() > 0) {
						for (int j = 0; j < audList.size(); j++) {
							String phone = (String) audList.get(j).get("TZ_ZY_SJ");
							String oprid = (String) audList.get(j).get("OPRID");
							if (phone != null && !"".equals(phone)) {
								if ("".equals(str_choice_email)) {
									really_totalAudienceOprID = oprid;
									str_choice_email = phone;
								} else {
									really_totalAudienceOprID = really_totalAudienceOprID + "," + oprid;
									str_choice_email = str_choice_email + "," + phone;
								}
								String name = "";
								createTaskServiceImpl.addAudCy(audID, name, "", phone, "", "", "", "", oprid, "", "",
										"");
							}

						}
					}
				}

			}

		}

		if (str_choice_email != null && !"".equals(str_choice_email)) {
			arr_str_choice_email = str_choice_email.split(",");
		}

		int audCyrTotal = 0;
		if (arr_str_input_email != null) {
			audCyrTotal = arr_str_input_email.length;
		}
		if (arr_str_choice_email != null) {
			audCyrTotal = audCyrTotal + arr_str_choice_email.length;
		}

		String[] arr_str_total_email = {};
		if (keyInputEmail == null || "".equals(keyInputEmail)) {
			if (str_choice_email != null && !"".equals(str_choice_email)) {
				arr_str_total_email = str_choice_email.split(",");
			}
		} else {
			if (str_choice_email != null && !"".equals(str_choice_email)) {
				arr_str_total_email = (keyInputEmail + "," + str_choice_email).split(",");
			}else{
				arr_str_total_email = keyInputEmail.split(",");
			}
		}

		int showNum = 0;
		String AddresseeEmail = "";
		if (viewNum != null && !"".equals(viewNum) && StringUtils.isNumeric(viewNum)) {
			showNum = Integer.valueOf(viewNum);
			AddresseeEmail = arr_str_total_email[showNum - 1];
		} else {
			AddresseeEmail = "";
		}

		String[] arr_str_choice_OprID = null;
		if (really_totalAudienceOprID != null && !"".equals(really_totalAudienceOprID)) {
			arr_str_choice_OprID = really_totalAudienceOprID.split(",");
		}

		if ("true".equals(sendType)) {
			if (arr_str_input_email != null && showNum > arr_str_input_email.length && emlTmpId != null && !"".equals(emlTmpId)) {
				// 元模板ID,听众成员ID;
				String ymbId = "", audCyId = "";
				String currentOprID = arr_str_choice_OprID[showNum - arr_str_input_email.length - 1];

				audCyId = jdbcTemplate.queryForObject(
						"select TZ_AUDCY_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and OPRID=? limit 0,1",
						new Object[] { audID, currentOprID }, "String");
				ymbId = jdbcTemplate.queryForObject(
						"select TZ_YMB_ID from PS_TZ_SMSTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?",
						new Object[] { orgID, emlTmpId }, "String");

				ArrayList<String[]> arrayList = ayalysisMbSysVar.ayalyMbVar(orgID, ymbId, audID, audCyId);

				if (arrayList != null && arrayList.size() > 0) {
					for (int i = 0; i < arrayList.size(); i++) {
						String[] str = arrayList.get(i);
						String name = str[0];
						String value = str[1];
						
						emailContent = emailContent.replace(name, value);
					}
				}
			}
		} else {
			if ("false".equals(sendType)) {
				String storeFileName = jdbcTemplate.queryForObject(
						"SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='B'",
						new Object[] { sendPcId }, "String"); /* 手机或者邮箱存储字段 */
				String subjectFileName = jdbcTemplate.queryForObject(
						"SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='C'",
						new Object[] { sendPcId }, "String");/* 邮件主题或短信签名存储字段 */
				String subjectContent = jdbcTemplate.queryForObject(
						"SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='D'",
						new Object[] { sendPcId }, "String");/* 邮件主题或短信内容存储字段 */

				String str_TZ_AUDCY_ID = "";

				String str_sql = "select TZ_AUDCY_ID  FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=? AND "
						+ storeFileName + "=?";
				str_TZ_AUDCY_ID = jdbcTemplate.queryForObject(str_sql, new Object[] { sendPcId, AddresseeEmail },
						"String");
				if (str_TZ_AUDCY_ID != null && !"".equals(str_TZ_AUDCY_ID)) {
					ArrayList<Map<String, String>> excelArr = ayalysisExcItemsValServiceImpl.ayalyExcVar(sendPcId,
							str_TZ_AUDCY_ID);
					if (excelArr != null && excelArr.size() > 0) {
						for (int j = 0; j < excelArr.size(); j++) {
							emailContent = emailContent.replace(excelArr.get(j).get("name"),
									excelArr.get(j).get("value"));
						}
					}
				}

			}
		}

		AddresseeEmail = AddresseeEmail.replace("]", "").replace("[", "");

		returnMap2.put("senderPhone", "");
		returnMap2.put("AddresseePhone", AddresseeEmail);
		returnMap2.put("SmsTheme", emailTheme);
		returnMap2.put("SmsContent", emailContent);
		returnMap2.put("audCyrTotal", audCyrTotal);
		returnMap2.put("currentPageNum", viewNum);
		returnMap2.put("AudID", audID);
		returnMap2.put("SmsContentArea", emailContent);
		returnMap1.replace("formData", returnMap2);
		return jacksonUtil.Map2json(returnMap1);
	}

	/* 从定义到预览 */
	private String previewOtherSMS(String comParams, String[] errorMsg) {
		// 返回;
		Map<String, Object> returnMap1 = new HashMap<>();
		Map<String, Object> returnMap2 = new HashMap<>();
		returnMap1.put("formData", returnMap2);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		// 机构编号;
		String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 邮件或短信群发ID;
		String sendPcId = jacksonUtil.getString("sendPcId");

		// 获取发送模式，若为true则是一般发送，false则是导入excel发送;
		String sendType = jacksonUtil.getString("sendType");
		// 当前预览的页面数字;
		String viewNum = jacksonUtil.getString("viewNumber");

		// 手动输入的邮箱;
		String keyInputEmail = jacksonUtil.getString("keyInputEmail");
		// 添加听众;
		String audIDTotal = jacksonUtil.getString("audIDTotal");
		// 邮件主题;
		String emailTheme = jacksonUtil.getString("emailTheme");
		// 邮件模板;
		String emailModal = jacksonUtil.getString("emailModal");
		// 获取邮件中配置的模板;
		String emlTmpId = jacksonUtil.getString("emlTmpId");
		// 邮件内容;
		String emailContent = jacksonUtil.getString("emailContent");

		String audID = jacksonUtil.getString("audID");

		// 手动输入邮箱数组;
		String[] arr_str_input_email = {};
		if (keyInputEmail != null && !"".equals(keyInputEmail)) {
			arr_str_input_email = keyInputEmail.split(",");
		}

		// 选择的听众ID;
		String[] arr_str_choice_aud = {};
		arr_str_choice_aud = audIDTotal.split(",");

		// 选择的听众ID对应的邮箱;
		String str_choice_email = "";
		String[] arr_str_choice_email = {};

		// 新添：获取听众集合解析出来的oprid;
		String audienceOprID = jacksonUtil.getString("AudienceOprID");
		String[] arr_str_choice_OprID = audienceOprID.split(",");

		// 当选择了听众作为收件人时;
		if (arr_str_choice_aud.length > 0) {
			for (int num_aud = 0; num_aud < arr_str_choice_aud.length; num_aud++) {
				String id = arr_str_choice_aud[num_aud];
				if (id != null && !"".equals(id)) {
					String strAudSQL = "select b.TZ_ZY_SJ FROM PS_TZ_AUD_LIST_T a, PS_TZ_LXFSINFO_TBL b,PS_TZ_AQ_YHXX_TBL c where a.TZ_LXFS_LY=b.TZ_LXFS_LY and a.TZ_LYDX_ID=b.TZ_LYDX_ID and a.OPRID=c.OPRID and a.TZ_AUD_ID=? and a.TZ_DXZT<>'N'";
					List<Map<String, Object>> audList = jdbcTemplate.queryForList(strAudSQL, new Object[] { id });
					if (audList != null && audList.size() > 0) {
						for (int j = 0; j < audList.size(); j++) {
							String phone = (String) audList.get(j).get("TZ_ZY_SJ");
							if (phone != null && !"".equals(phone)) {
								if ("".equals(str_choice_email)) {
									str_choice_email = phone;
								} else {
									str_choice_email = str_choice_email + "," + phone;
								}
							}

						}
					}
				}

			}

		}

		if (str_choice_email != null && !"".equals(str_choice_email)) {
			arr_str_choice_email = str_choice_email.split(",");
		}

		//int audCyrTotal = arr_str_input_email.length + arr_str_choice_email.length;
		int audCyrTotal = 0;
		if(arr_str_input_email != null){
			audCyrTotal = arr_str_input_email.length;
		}
		if(arr_str_choice_email != null){
			audCyrTotal = audCyrTotal + arr_str_choice_email.length;
		}

		String[] arr_str_total_email = {};
		if (keyInputEmail == null || "".equals(keyInputEmail)) {
			if (str_choice_email != null && !"".equals(str_choice_email)) {
				arr_str_total_email = str_choice_email.split(",");
			}
		} else {
			if (str_choice_email != null && !"".equals(str_choice_email)) {
				arr_str_total_email = (keyInputEmail + "," + str_choice_email).split(",");
			}else{
				arr_str_total_email = keyInputEmail.split(",");
			}
		}

		int showNum = 0;
		String AddresseeEmail = "";
		if (viewNum != null && !"".equals(viewNum) && StringUtils.isNumeric(viewNum)) {
			showNum = Integer.valueOf(viewNum);
			AddresseeEmail = arr_str_total_email[showNum - 1];
		} else {
			AddresseeEmail = "";
		}

		if ("true".equals(sendType)) {
			if (arr_str_input_email != null && showNum > arr_str_input_email.length && emlTmpId != null && !"".equals(emlTmpId)) {
				// 元模板ID,听众成员ID;
				String ymbId = "", audCyId = "";
				String currentOprID = arr_str_choice_OprID[showNum - arr_str_input_email.length - 1];

				audCyId = jdbcTemplate.queryForObject(
						"select TZ_AUDCY_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and OPRID=? limit 0,1",
						new Object[] { audID, currentOprID }, "String");
				ymbId = jdbcTemplate.queryForObject(
						"select TZ_YMB_ID from PS_TZ_SMSTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?",
						new Object[] { orgID, emlTmpId }, "String");

				ArrayList<String[]> arrayList = ayalysisMbSysVar.ayalyMbVar(orgID, ymbId, audID, audCyId);

				if (arrayList != null && arrayList.size() > 0) {
					for (int i = 0; i < arrayList.size(); i++) {
						String[] str = arrayList.get(i);
						String name = str[0];
						String value = str[1];
						
						emailContent = emailContent.replace(name, value);
					}
				}
			}
		} else {
			if ("false".equals(sendType)) {
				String storeFileName = jdbcTemplate.queryForObject(
						"SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='B'",
						new Object[] { sendPcId }, "String"); /* 手机或者邮箱存储字段 */
				String subjectFileName = jdbcTemplate.queryForObject(
						"SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='C'",
						new Object[] { sendPcId }, "String");/* 邮件主题或短信签名存储字段 */
				String subjectContent = jdbcTemplate.queryForObject(
						"SELECT TZ_FIELD_NAME FROM PS_TZ_EXC_SET_TBL WHERE TZ_MLSM_QFPC_ID=? AND TZ_XXX_TYPE='D'",
						new Object[] { sendPcId }, "String");/* 邮件主题或短信内容存储字段 */

				String str_TZ_AUDCY_ID = "";

				String str_sql = "select TZ_AUDCY_ID  FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID=? AND "
						+ storeFileName + "=?";
				str_TZ_AUDCY_ID = jdbcTemplate.queryForObject(str_sql, new Object[] { sendPcId, AddresseeEmail },
						"String");
				if (str_TZ_AUDCY_ID != null && !"".equals(str_TZ_AUDCY_ID)) {
					ArrayList<Map<String, String>> excelArr = ayalysisExcItemsValServiceImpl.ayalyExcVar(sendPcId,
							str_TZ_AUDCY_ID);
					if (excelArr != null && excelArr.size() > 0) {
						for (int j = 0; j < excelArr.size(); j++) {
							emailContent = emailContent.replace(excelArr.get(j).get("name"),
									excelArr.get(j).get("value"));
						}
					}
				}

			}
		}

		AddresseeEmail = AddresseeEmail.replace("]", "").replace("[", "");

		returnMap2.put("senderPhone", "");
		returnMap2.put("AddresseePhone", AddresseeEmail);
		returnMap2.put("SmsTheme", emailTheme);
		returnMap2.put("SmsContent", emailContent);
		returnMap2.put("audCyrTotal", audCyrTotal);
		returnMap2.put("currentPageNum", viewNum);
		returnMap2.put("AudID", audID);
		returnMap2.put("SmsContentArea", emailContent);
		returnMap1.replace("formData", returnMap2);
		return jacksonUtil.Map2json(returnMap1);
	}

	// 检查是否合格;
	private String checkSmsAudience(String comParams, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		String totalAudience = jacksonUtil.getString("totalAudience");
		String really_totalAudience = "", really_totalAudienceEmail = "", really_totalAudienceOprID = "";
		if (totalAudience != null && !"".equals(totalAudience)) {
			String[] array_totalAudience = totalAudience.split(",");

			for (int i = 0; i < array_totalAudience.length; i++) {
				String audId = array_totalAudience[i];
				int count = jdbcTemplate.queryForObject(
						"select count(1) from PS_TZ_AUD_DEFN_T a,PS_TZ_AUD_LIST_T b where a.TZ_AUD_ID=b.TZ_AUD_ID and a.TZ_AUD_ID=?",
						new Object[] { audId }, "Integer");
				if (count > 0) {
					if ("".equals(really_totalAudience)) {
						really_totalAudience = audId;
					} else {
						really_totalAudience = really_totalAudience + "," + audId;
					}

					String sql = "select a.OPRID,b.TZ_ZY_SJ,c.TZ_REALNAME FROM PS_TZ_AUD_LIST_T a, PS_TZ_LXFSINFO_TBL b,PS_TZ_AQ_YHXX_TBL c where a.TZ_LXFS_LY=b.TZ_LXFS_LY and a.TZ_LYDX_ID=b.TZ_LYDX_ID and a.OPRID=c.OPRID and a.TZ_AUD_ID=? and a.TZ_DXZT<>'N'";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { audId });
					if (list != null) {
						for (int j = 0; j < list.size(); j++) {
							String oprid = (String) list.get(j).get("OPRID");
							String phone = (String) list.get(j).get("TZ_ZY_SJ");
							String firstname = (String) list.get(j).get("TZ_REALNAME");

							if (!"".equals(phone)) {
								if ("".equals(really_totalAudienceEmail)) {
									really_totalAudienceEmail = phone;
									really_totalAudienceOprID = oprid;
								} else {
									really_totalAudienceEmail = really_totalAudienceEmail + "," + phone;
									really_totalAudienceOprID = really_totalAudienceOprID + "," + oprid;
								}
							}

						}
					}

				}
			}

		}
		returnMap.put("totalAudienceID", really_totalAudience);
		returnMap.put("totalAudienceOprID", really_totalAudienceOprID);
		returnMap.put("totalAudienceEmail", really_totalAudienceEmail);

		return jacksonUtil.Map2json(returnMap);
	}

}
