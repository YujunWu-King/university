package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * PS:TZ_EMAIL_PREVIEW_PKG:TZ_EMAIL_PREVIEW_CLS
 * 功能说明：邮件群发预览
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzEmailPreviewClsServiceImpl")
public class TzEmailPreviewClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzOther(String OperateType, String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		String type = "";
		if (jacksonUtil.containsKey("type")) {
			type = jacksonUtil.getString("type");
		}

		String returnString = "";
		if ("previewEmail".equals(type)) {
			returnString = this.previewEmail(comParams, errorMsg);
		}
		if ("previewOtherEmail".equals(type)) {
			returnString = this.previewOtherEmail(comParams, errorMsg);
		}
		// if("checkEmailAudience".equals(type)){
		// returnString = this.checkEmailAudience(comParams, errorMsg);
		// }
		return returnString;
	}

	/* 从定义到预览 */
	private String previewEmail(String comParams, String[] errorMsg) {
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

		// 发件人邮箱;
		String senderEmail = jacksonUtil.getString("senderEmail");
		String senderEmail_DaiLi = jdbcTemplate.queryForObject(
				"select TZ_EML_ADDR100 from PS_TZ_EMLS_DEF_TBL where TZ_EMLSERV_ID=? and TZ_JG_ID=?",
				new Object[] { senderEmail, orgID }, "String");
		if (senderEmail_DaiLi != null && !"".equals(senderEmail_DaiLi)) {
			senderEmail = senderEmail_DaiLi;
		}

		// 手动输入的邮箱;
		String keyInputEmail = jacksonUtil.getString("keyInputEmail");
		
		// 添加听众;
		// String audIDTotal = jacksonUtil.getString("audIDTotal");
		// 邮件主题;
		String emailTheme = jacksonUtil.getString("emailTheme");
		// 邮件模板;
		// String emailModal = jacksonUtil.getString("emailModal");
		// 获取邮件中配置的模板;
		// String emlTmpId = jacksonUtil.getString("emlTmpId");
		// 邮件内容;
		String emailContent = jacksonUtil.getString("emailContent");

		// 手动输入邮箱数组;
		String[] arr_str_input_email = {};
		if (keyInputEmail != null && !"".equals(keyInputEmail)) {
			arr_str_input_email = keyInputEmail.split(",");
		}
		String audCyrTotal = String.valueOf(arr_str_input_email.length);

		int showNum = 0;
		String AddresseeEmail = "";
		if (viewNum != null && !"".equals(viewNum) && StringUtils.isNumeric(viewNum)) {
			showNum = Integer.valueOf(viewNum);
			AddresseeEmail = arr_str_input_email[showNum - 1];
		} else {
			AddresseeEmail = "";
		}

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

			String selectFldString = "TZ_AUDCY_ID";
			if (storeFileName != null && !"".equals(storeFileName)) {
				if (subjectFileName != null && !"".equals(subjectFileName)) {
					selectFldString = selectFldString + "," + subjectFileName;
				}

				if (subjectContent != null && !"".equals(subjectContent)) {
					selectFldString = selectFldString + "," + subjectContent;
				}

				String str_sql = "select " + selectFldString + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID= ? AND "
						+ storeFileName + "= ? limit 0,1";
				Map<String, Object> map = jdbcTemplate.queryForMap(str_sql, new Object[] { sendPcId, AddresseeEmail });
				if (map != null) {
					if (subjectFileName != null && !"".equals(subjectFileName)) {
						emailTheme = (String) map.get(subjectFileName);
					}

					if (subjectContent != null && !"".equals(subjectContent)) {
						emailContent = (String) map.get(subjectContent);
					}
				}
			}
		}

		AddresseeEmail = AddresseeEmail.replace("]", "").replace("[", "");
		returnMap2.put("senderEmail", senderEmail);
		returnMap2.put("AddresseeEmail", AddresseeEmail);
		returnMap2.put("emailTheme", emailTheme);
		returnMap2.put("emailContent", emailContent);
		returnMap2.put("audCyrTotal", audCyrTotal);
		returnMap2.put("currentPageNum", viewNum);
		returnMap2.put("AudID", "");
		returnMap1.replace("formData", returnMap2);
		return jacksonUtil.Map2json(returnMap1);
	}

	/* 从定义到预览 */
	private String previewOtherEmail(String comParams, String[] errorMsg) {
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

		// 发件人邮箱;
		String senderEmail = jacksonUtil.getString("senderEmail");
		String senderEmail_DaiLi = jdbcTemplate.queryForObject(
				"select TZ_EML_ADDR100 from PS_TZ_EMLS_DEF_TBL where TZ_EMLSERV_ID=? and TZ_JG_ID=?",
				new Object[] { senderEmail, orgID }, "String");
		if (senderEmail_DaiLi != null && !"".equals(senderEmail_DaiLi)) {
			senderEmail = senderEmail_DaiLi;
		}

		// 手动输入的邮箱;
		String keyInputEmail = jacksonUtil.getString("keyInputEmail");
		// 添加听众;
		// String audIDTotal = jacksonUtil.getString("audIDTotal");
		// 邮件主题;
		String emailTheme = jacksonUtil.getString("emailTheme");
		// 邮件模板;
		// String emailModal = jacksonUtil.getString("emailModal");
		// 获取邮件中配置的模板;
		// String emlTmpId = jacksonUtil.getString("emlTmpId");
		// 邮件内容;
		String emailContent = jacksonUtil.getString("emailContent");

		// 手动输入邮箱数组;
		String[] arr_str_input_email = {};
		if (keyInputEmail != null && !"".equals(keyInputEmail)) {
			arr_str_input_email = keyInputEmail.split(",");
		}
		String audCyrTotal = String.valueOf(arr_str_input_email.length);
		
		int showNum = 0;
		String AddresseeEmail = "";
		if (viewNum != null && !"".equals(viewNum) && StringUtils.isNumeric(viewNum)) {
			showNum = Integer.valueOf(viewNum);
			AddresseeEmail = arr_str_input_email[showNum - 1];
		} else {
			AddresseeEmail = "";
		}

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

			String selectFldString = "TZ_AUDCY_ID";
			if (storeFileName != null && !"".equals(storeFileName)) {
				if (subjectFileName != null && !"".equals(subjectFileName)) {
					selectFldString = selectFldString + "," + subjectFileName;
				}

				if (subjectContent != null && !"".equals(subjectContent)) {
					selectFldString = selectFldString + "," + subjectContent;
				}

				String str_sql = "select " + selectFldString + " FROM PS_TZ_MLSM_DRNR_T WHERE TZ_MLSM_QFPC_ID= ? AND "
						+ storeFileName + "= ? limit 0,1";
				Map<String, Object> map = jdbcTemplate.queryForMap(str_sql, new Object[] { sendPcId, AddresseeEmail });
				if (map != null) {
					if (subjectFileName != null && !"".equals(subjectFileName)) {
						emailTheme = (String) map.get(subjectFileName);
					}

					if (subjectContent != null && !"".equals(subjectContent)) {
						emailContent = (String) map.get(subjectContent);
					}
				}
			}
		}

		AddresseeEmail = AddresseeEmail.replace("]", "").replace("[", "");
		returnMap2.put("senderEmail", senderEmail);
		returnMap2.put("AddresseeEmail", AddresseeEmail);
		returnMap2.put("emailTheme", emailTheme);
		returnMap2.put("emailContent", emailContent);
		returnMap2.put("audCyrTotal", audCyrTotal);
		returnMap2.put("currentPageNum", viewNum);
		returnMap2.put("AudID", "");
		returnMap1.replace("formData", returnMap2);
		return jacksonUtil.Map2json(returnMap1);
	}

	/*
	 * 从定义到预览 private String checkEmailAudience(String comParams, String[]
	 * errorMsg) { return null; }
	 */
}
