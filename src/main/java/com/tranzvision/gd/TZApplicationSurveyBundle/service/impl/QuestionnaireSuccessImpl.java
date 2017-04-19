package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 在线调查--提交成功页面
 * 
 * @author CAOY
 *
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireSuccessImpl")
public class QuestionnaireSuccessImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private TZGDObject tzGdObject;

	@Override
	public String tzGetHtmlContent(String comParams) {

		String successFlag = "0";
		String strMsg = "";

		String accessType = "", language = "";
		String strReturn = "";
		JacksonUtil jsonUtil = new JacksonUtil();

		/* 参数 */
		jsonUtil.json2Map(comParams);

		/* 调查问卷应用编号 */
		String str_appId = request.getParameter("classid");

		/* 问卷编号 */
		String surveyID = "";

		/* 从参数中获取问卷编号、实例编号 */
		if (str_appId != null && !str_appId.equals("")) {
			surveyID = request.getParameter("SURVEY_WJ_ID");
			accessType = request.getParameter("AC");
		} else {
			if (jsonUtil.containsKey("SURVEY_WJ_ID")) {
				surveyID = jsonUtil.getString("SURVEY_WJ_ID");
			}
			if (jsonUtil.containsKey("AC")) {
				accessType = jsonUtil.getString("AC");
			}
		}

		if (StringUtils.isEmpty(accessType)) {
			accessType = "P";
		}

		if (StringUtils.isEmpty(surveyID)) {
			successFlag = "1";
			language = tzLoginServiceImpl.getSysLanaguageCD(request);
			strMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("PARAM_ERROR", "1", language, "参数错误！",
					"Parameter error !");
		}
		String path = request.getContextPath();
		if (successFlag.equals("0")) {
			String jumpUrl = "", strJGNR = "", header = "", footer = "", strTitle = "", strModeDesc = "";
			String time = "0";
			Map<String, Object> dateMap = jdbcTemplate.queryForMap(
					"SELECT TZ_DC_WJ_TZDZ,TZ_DC_WJ_TLSJ,TZ_DC_WJ_JGNR,TZ_DC_WJBT,TZ_APP_TPL_LAN,TZ_DC_JTNR,TZ_DC_JWNR FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ?",
					new Object[] { surveyID });
			if (dateMap != null) {
				if (dateMap.containsKey("TZ_DC_WJ_TZDZ") && dateMap.get("TZ_DC_WJ_TZDZ") != null) {
					jumpUrl = dateMap.get("TZ_DC_WJ_TZDZ").toString();
				}
				if (dateMap.containsKey("TZ_DC_WJ_TLSJ") && dateMap.get("TZ_DC_WJ_TLSJ") != null) {
					time = dateMap.get("TZ_DC_WJ_TLSJ").toString();
					if(StringUtils.isBlank(time)){
						time = "0";
					}
				}
				if (dateMap.containsKey("TZ_DC_WJ_JGNR") && dateMap.get("TZ_DC_WJ_JGNR") != null) {
					strJGNR = dateMap.get("TZ_DC_WJ_JGNR").toString();
				}
				if (dateMap.containsKey("TZ_DC_WJBT") && dateMap.get("TZ_DC_WJBT") != null) {
					strTitle = dateMap.get("TZ_DC_WJBT").toString();
				}
				if (dateMap.containsKey("TZ_APP_TPL_LAN") && dateMap.get("TZ_APP_TPL_LAN") != null) {
					language = dateMap.get("TZ_APP_TPL_LAN").toString();
				}
				if (dateMap.containsKey("TZ_DC_JTNR") && dateMap.get("TZ_DC_JTNR") != null) {
					header = dateMap.get("TZ_DC_JTNR").toString();
				}
				if (dateMap.containsKey("TZ_DC_JWNR") && dateMap.get("TZ_DC_JWNR") != null) {
					footer = dateMap.get("TZ_DC_JWNR").toString();
				}

				if (StringUtils.isEmpty(language)) {
					language = "ZHS";
				}

				String survey_mode = messageTextServiceImpl.getMessageTextWithLanguageCd("SURVEY_MODE", "1", language,
						"调查模式", "Survey");
				String survey_mode_desc = messageTextServiceImpl.getMessageTextWithLanguageCd("SURVEY_MODE_DESC", "1",
						language, "您提交的数据我们将会保存", "Save");

				if (StringUtils.isEmpty(strJGNR)) {
					strJGNR = messageTextServiceImpl.getMessageTextWithLanguageCd("SUBMIT_SUCCEED", "1", language,
							"提交成功!", "Submitted successfully!");
				}
				if (!StringUtils.isEmpty(accessType) && accessType.equals("P")) {
					try {
						strModeDesc = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_MODE_HTML",
								survey_mode, survey_mode_desc, path);
						strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_SUCCESS_HTML",
								header, strJGNR, footer, jumpUrl, time, strTitle, strModeDesc, path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						strModeDesc = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_MODE_M_HTML",
								survey_mode, survey_mode_desc, path);
						strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_SUCCESS_M_HTML",
								header, strJGNR, footer, jumpUrl, time, strTitle, strModeDesc, path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		} else {
			strReturn = strMsg;
		}

		return strReturn;
	}

}
