package com.tranzvision.gd.TZZnxQfBundle.service.impl;

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
import com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.AyalysisMbSysVar;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * tang 站内信预览
 *
 */
@Service("com.tranzvision.gd.TZZnxQfBundle.service.impl.TzZnxPreviewClsServiceImpl")
public class TzZnxPreviewClsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Autowired
	private AyalysisMbSysVar ayalysisMbSysVar;


	@Override
	public String tzOther(String OperateType, String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		String type = "";
		if (jacksonUtil.containsKey("type")) {
			type = jacksonUtil.getString("type");
		}

		String returnString = "";
		if ("previewZnx".equals(type)) {
			returnString = this.previewZnx(comParams, errorMsg);
		}

		if ("checkZnxAudience".equals(type)) {
			returnString = this.checkZnxAudience(comParams, errorMsg);
		}
		return returnString;
	}

	/* 从定义页面预览短信 */
	private String previewZnx(String comParams, String[] errorMsg) {
		// 返回;
		Map<String, Object> returnMap1 = new HashMap<>();
		Map<String, Object> returnMap2 = new HashMap<>();
		returnMap1.put("formData", returnMap2);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		// 机构编号;
		String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 当前预览的页面数字;
		String viewNum = jacksonUtil.getString("viewNumber");
		// 手动输入的邮箱;
		String AudienceOprID = jacksonUtil.getString("AudienceOprID");
		if (AudienceOprID == null) {
			AudienceOprID = "";
		}
		// 邮件主题;
		String znxlTheme = jacksonUtil.getString("znxlTheme");
		// 获取邮件中配置的模板;
		String znxTmpId = jacksonUtil.getString("znxTmpId");
		// 邮件内容;
		String znxContent = jacksonUtil.getString("znxContent");
		// 解析后内容;
		String znxContentHtml = znxContent;
		// 手动输入邮箱数组;
		String[] arr_str_input_oprid = null;
		if (AudienceOprID != null && !"".equals(AudienceOprID)) {
			arr_str_input_oprid = AudienceOprID.split(",");
		}

		String audID = "";
		if (jacksonUtil.containsKey("audID")) {
			audID = jacksonUtil.getString("audID");
		}
		// 当选择了听众作为收件人时;
		if ((audID == null || "".equals(audID)) && arr_str_input_oprid != null && arr_str_input_oprid.length > 0) {
			// 创建听众;
			audID = createTaskServiceImpl.createAudience("", orgID, "邮件群发", "QFRW");

			for (int num_aud = 0; num_aud < arr_str_input_oprid.length; num_aud++) {
				String oprid = arr_str_input_oprid[num_aud];
				if (oprid != null && !"".equals(oprid)) {
					String name = jdbcTemplate.queryForObject(
							"select c.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL c where c.OPRID=?", new Object[] { oprid },
							"String");
					if (name == null) {
						name = "";
					}
					createTaskServiceImpl.addAudCy(audID, name, "", "", "", "", "", "", oprid, "", "", "");
				}

			}

		}

		int showNum = 0;
		String currentOprID = "";
		String oprName = "";
		if (viewNum != null && !"".equals(viewNum) && StringUtils.isNumeric(viewNum) && arr_str_input_oprid != null
				&& arr_str_input_oprid.length > 0) {
			showNum = Integer.valueOf(viewNum);

			currentOprID = arr_str_input_oprid[showNum - 1];
			oprName = jdbcTemplate.queryForObject("select c.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL c where c.OPRID=?",
					new Object[] { currentOprID }, "String");

			if (znxTmpId != null && !"".equals(znxTmpId)) {
				String audCyId = jdbcTemplate.queryForObject(
						"select TZ_AUDCY_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and OPRID=? limit 0,1",
						new Object[] { audID, currentOprID }, "String");
				String ymbId = jdbcTemplate.queryForObject(
						"select TZ_YMB_ID from PS_TZ_ZNXTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?",
						new Object[] { orgID, znxTmpId }, "String");

				ArrayList<String[]> arrayList = ayalysisMbSysVar.ayalyMbVar(orgID, ymbId, audID, audCyId);

				if (arrayList != null && arrayList.size() > 0) {
					for (int i = 0; i < arrayList.size(); i++) {
						String[] str = arrayList.get(i);
						String name = str[0];
						String value = str[1];
						
						znxContentHtml = znxContentHtml.replace(name, value);
					}
				}
			}

		} else {
			oprName = "";
		}

		int audCyrTotal = 0;
		if (arr_str_input_oprid != null) {
			audCyrTotal = arr_str_input_oprid.length;
		}
		returnMap2.put("oprName", oprName);
		returnMap2.put("AudienceOprID", AudienceOprID);
		returnMap2.put("znxTmpId", znxTmpId);
		returnMap2.put("znxlTheme", znxlTheme);
		returnMap2.put("znxContent", znxContent);
		returnMap2.put("znxContentHtml", znxContentHtml);
		returnMap2.put("audCyrTotal", audCyrTotal);
		returnMap2.put("currentPageNum", viewNum);
		returnMap2.put("AudID", audID);

		returnMap1.replace("formData", returnMap2);
		return jacksonUtil.Map2json(returnMap1);
	}

	// 检查是否合格;
	private String checkZnxAudience(String comParams, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		// 站内信提交的是所有的收件人字段里的值包括听众id和考生oprid;
		String totalAudience = jacksonUtil.getString("totalAudience");
		String really_totalAudienceOprID = "";
		if (totalAudience != null && !"".equals(totalAudience)) {
			String[] array_totalAudience = totalAudience.split(",");

			for (int i = 0; i < array_totalAudience.length; i++) {
				String audId = array_totalAudience[i];
				int count = jdbcTemplate.queryForObject(
						"select count(1) from PS_TZ_AUD_DEFN_T a,PS_TZ_AUD_LIST_T b where a.TZ_AUD_ID=b.TZ_AUD_ID and a.TZ_AUD_ID=?",
						new Object[] { audId }, "Integer");
				// 是听众
				if (count > 0) {

					String sql = "select a.OPRID FROM PS_TZ_AUD_LIST_T a, PS_TZ_AQ_YHXX_TBL c where a.OPRID=c.OPRID and a.TZ_AUD_ID=? and a.TZ_DXZT<>'N'";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { audId });
					if (list != null) {
						for (int j = 0; j < list.size(); j++) {
							String oprid = (String) list.get(j).get("OPRID");

							if (!"".equals(oprid)) {
								if ("".equals(really_totalAudienceOprID)) {
									really_totalAudienceOprID = oprid;
								} else {
									really_totalAudienceOprID = really_totalAudienceOprID + "," + oprid;
								}
							}

						}
					}

				} else {
					// 直接是考生
					if ("".equals(really_totalAudienceOprID)) {
						really_totalAudienceOprID = audId;
					} else {
						really_totalAudienceOprID = really_totalAudienceOprID + "," + audId;
					}
				}
			}

		}
		returnMap.put("totalAudienceOprID", really_totalAudienceOprID);
		return jacksonUtil.Map2json(returnMap);
	}
}
