package com.tranzvision.gd.TZWebSelectorBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 班级选择器
 * 
 * @author caoy
 *
 */

@Service("com.tranzvision.gd.TZWebSelectorBundle.service.impl.ClassClsServiceImpl")
public class ClassClsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TZGDObject tzGdObject;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Override
	// 选择班级;
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		System.out.println("strParams:" + strParams);
		// String skinId = "";
		String language = "";
		String classId = "";
		String className = "";
		String pageID = "";
		String linkId = "";
		if (jacksonUtil.containsKey("siteId")) {
			String siteId = jacksonUtil.getString("siteId");
			// 根据站点id查询;
			String sql = "select TZ_SITE_LANG,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { siteId });
			language = map.get("TZ_SITE_LANG") == null ? "" : map.get("TZ_SITE_LANG").toString();
			// skinId = map.get("TZ_SKIN_ID") == null ? "" :
			// map.get("TZ_SKIN_ID").toString();
			System.out.println("siteId:" + siteId);
			if (StringUtils.isEmpty(language)) {
				language = "ZHS";
			}
			System.out.println("language:" + language);
			if (jacksonUtil.containsKey("TZ_PROV_ID")) {
				pageID = jacksonUtil.getString("TZ_PROV_ID");
			}
			if (jacksonUtil.containsKey("linkId")) {
				linkId = jacksonUtil.getString("linkId");
			}

			if (jacksonUtil.containsKey("classId")) {
				classId = jacksonUtil.getString("classId");

				System.out.println("pageID:" + pageID);
				System.out.println("linkId" + linkId);
				System.out.println("classId:" + classId);

				// 根据classId 找到 同一报名表模版下的 可以报名的班级
				PsTzClassInfT psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(classId);
				if (psTzClassInfT != null) {

					StringBuffer sb = new StringBuffer();
					sb.append("SELECT TZ_CLASS_ID,TZ_CLASS_NAME FROM  PS_TZ_CLASS_INF_T ");
					sb.append("where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) ");
					sb.append(
							"AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' AND TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL ");
					sb.append("And TZ_APP_MODAL_ID=? AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL ");
					sb.append(
							"AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() ");
					sb.append(
							"AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() ");

					List<Map<String, Object>> classList = jdbcTemplate.queryForList(sb.toString(),
							new Object[] { siteId, psTzClassInfT.getTzJgId(), psTzClassInfT.getTzAppModalId() });
					String classInfo = "";
					if (classList != null && classList.size() > 0) {
						for (int i = 0; i < classList.size(); i++) {
							classId = (String) classList.get(i).get("TZ_CLASS_ID");
							className = (String) classList.get(i).get("TZ_CLASS_NAME");
							try {
								classInfo = classInfo
										+ tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_CLASS_A_HTML", "CLASS_RD",
												classId, className, "CLASS_RD" + i);
							} catch (TzSystemException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					String sureBtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
							"SURE", language, "确定", "Sure");
					String cancelBtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
							"CANCEL", language, "取消", "Cancel");
					String title = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
							"CHOOSECLASS", language, "选择报考方向", "Choose CLass");

					String error = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
							"NOCHOOSE", language, "请选择报考方向！", "Please Choose CLass!");
					// 统一接口URL;
					String tzGeneralURL = request.getContextPath() + "/dispatcher";
					try {
						return tzGdObject.getHTMLText("HTML.TZWebSelectorBundle.TZ_CLASS_SELECT_HTML",true, classInfo,
								pageID, request.getContextPath(), title, sureBtn, cancelBtn, "CLASS_RD", error, linkId,
								tzGeneralURL);
					} catch (TzSystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "";
					}
				} else {
					return "";
				}

			} else {
				return "";
			}
		} else {
			return "";
		}
	}
}
