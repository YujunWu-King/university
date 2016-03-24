package com.tranzvision.gd.TZRecommendationBundle.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * PS:TZ_GD_TJX_PKG:TZ_TO_TJX_CLS
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZRecommendationBundle.service.impl.TzToTjxClsServiceImpl")
public class TzToTjxClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;

	/*
	 * 功能描述： 点击邮件链接 跳转到推荐信报名表 参数说明： + &strTjxId 推荐信报名表ID + &strOprid 发送人ID +
	 * &strModal 推荐信报名表模板编号
	 */
	public String toReference(String strTjxId, String strOprid) {
		String strRtn = "";

		long numAppinsId = 0;
		String strTjxYx = "";
		String strTjxType = "";
		long numTjxAppinsId = 0;
		String contextPath = request.getContextPath();
		try {
			
			if (strTjxId != null && !"".equals(strTjxId) && strOprid != null && !"".equals(strOprid)) {
				Map<String, Object> map = jdbcTemplate.queryForMap(
						"select TZ_APP_INS_ID,TZ_MBA_TJX_YX,TZ_TJX_TYPE,TZ_TJX_APP_INS_ID from PS_TZ_KS_TJX_TBL where TZ_REF_LETTER_ID=? and OPRID=? limit 0,1",
						new Object[] { strTjxId, strOprid });
				if (map != null) {
					try{
						numAppinsId = Long.parseLong( map.get("TZ_APP_INS_ID").toString());
					}catch(Exception e){
						numAppinsId = 0L;
					}
					
					strTjxYx = (String) map.get("TZ_MBA_TJX_YX");
					strTjxType = (String) map.get("TZ_TJX_TYPE");
					try{
						numTjxAppinsId = Long.parseLong(map.get("TZ_TJX_APP_INS_ID").toString());
					}catch(Exception e){
						numTjxAppinsId = 0L;
					}
				}

				String str_tjx_mb_id = "", str_bmb_mb_id = "";
				str_bmb_mb_id = jdbcTemplate.queryForObject(
						"SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=? limit 0,1",
						new Object[] { numAppinsId }, "String");

				String str_tjx_sx = "";
				// 推荐信有效-------------------------------;
				if (!"Y".equals(strTjxYx)) {
					// 推荐信无效;
					if ("E".equals(strTjxType)) {
						// 英文;
						str_tjx_mb_id = jdbcTemplate.queryForObject(
								"SELECT TZ_ENG_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=? limit 0,1",
								new Object[] { str_bmb_mb_id }, "String");
						str_tjx_sx = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET",
								"REF_FAIL", "ENG", "", "");

						strRtn = tzGdObject.getHTMLText("HTML.TZRecommendationBundle.TZ_GD_TJX_ERROR_HTML", str_tjx_sx,contextPath);
						return strRtn;

					} else {
						// 中文;
						str_tjx_mb_id = jdbcTemplate.queryForObject(
								"SELECT TZ_CHN_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=? limit 0,1",
								new Object[] { str_bmb_mb_id }, "String");
						str_tjx_sx = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET",
								"REF_FAIL", "ZHS", "", "");

						strRtn = tzGdObject.getHTMLText("HTML.TZRecommendationBundle.TZ_GD_TJX_ERROR_HTML",	str_tjx_sx,contextPath);
						return strRtn;
					}
				} else {
					if ("E".equals(strTjxType)) {
						// 英文;
						str_tjx_mb_id = jdbcTemplate.queryForObject(
								"SELECT TZ_ENG_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=? limit 0,1",
								new Object[] { str_bmb_mb_id }, "String");
					} else {
						// 中文;
						str_tjx_mb_id = jdbcTemplate.queryForObject(
								"SELECT TZ_CHN_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=? limit 0,1",
								new Object[] { str_bmb_mb_id }, "String");
					}
				}

				// 报名表ID不存在，错误---------------------;
				if (numAppinsId < 0) {
					strRtn = tzGdObject.getHTMLText("HTML.TZRecommendationBundle.TZ_GD_TJX_ERROR_HTML","非法操作",contextPath);
					return strRtn;
				}
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				// 如果推荐信报名表ID不存在，生成-----------;
				if (numTjxAppinsId <= 0) {
					numTjxAppinsId = getSeqNum.getSeqNum("TZ_APP_INS_T", "TZ_APP_INS_ID");
					PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
					psTzAppInsT.setTzAppInsId(numTjxAppinsId);
					psTzAppInsT.setTzAppTplId(str_tjx_mb_id);
					psTzAppInsT.setRowAddedDttm(new Date());
					psTzAppInsT.setRowAddedOprid(oprid);
					psTzAppInsT.setRowLastmantDttm(new Date());
					psTzAppInsT.setRowLastmantOprid(oprid);
					psTzAppInsTMapper.insert(psTzAppInsT);

					jdbcTemplate.update("update PS_TZ_KS_TJX_TBL set TZ_TJX_APP_INS_ID=? where TZ_REF_LETTER_ID=?",
							new Object[] { numTjxAppinsId, strTjxId });

				}
				
				//跳转到推荐信报名表的链接;
				//String strTzUrl = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?", new Object[]{"TZ_TJX_URL"},"String");
				String strTzUrl = request.getContextPath() + "/dispatcher?classid=appId&TZ_APP_INS_ID=" + numTjxAppinsId + "&TZ_REF_LETTER_ID=" + strTjxId;
				strRtn = tzGdObject.getHTMLText("HTML.TZRecommendationBundle.TZ_GD_TJX_TRANS_HTML",strTzUrl);
				
			}else{
				strRtn = tzGdObject.getHTMLText("HTML.TZRecommendationBundle.TZ_GD_TJX_ERROR_HTML","参数不完整",contextPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			strRtn = "参数错误";
		}
		return strRtn;
	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.parseJson2Map(strParams);

			String strTjxId = "";
			String strOprid = "";
			/* 报名表应用编号 */
			String str_appId = request.getParameter("classid");
			if (str_appId != null && !"".equals(str_appId)) {
				strTjxId = request.getParameter("TZ_REF_LETTER_ID");
				strOprid = request.getParameter("OPRID");
			} else {
				strTjxId = jacksonUtil.getString("TZ_REF_LETTER_ID");
				strOprid = jacksonUtil.getString("OPRID");
			}

			strRtn = this.toReference(strTjxId, strOprid);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRtn;
	}

}
