package com.tranzvision.gd.TZWebSiteInfoBundle.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;

/**
 * 查看发布内容；原：TZ_GD_ARTGL:ArtView
 * 
 * @author tang
 * @since 2015-11-26
 */
@Service("com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtViewServiceImpl")
public class ArtViewServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzGetHtmlContent(String strParams) {
		// 返回值;
		String strRet = "";

		String siteId = request.getParameter("siteId");
		String columnId = request.getParameter("columnId");
		String artId = request.getParameter("artId");
		String from = request.getParameter("from");
		
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		// 校验 用户是否已经登录，如果未登录 则 跳到登录页面，用户登录完成以后在跳转回来 by caoy 2017-3-3

		if (siteId != null && !siteId.equals("")) {
			// 如果用户未登录 直接 跳到登录页面
			if (oprid == null || oprid.equals("")) {
				// 根据siteId得到机构ID
				String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String jgid = jdbcTemplate.queryForObject(sql, new Object[] { siteId }, "String");

				String contextUrl = request.getContextPath();
				/// user/login/sem/72
				String fg = "/";
				if (!contextUrl.endsWith(fg)) {
					contextUrl = contextUrl + fg;
				}
				contextUrl = contextUrl + "user/login/" + jgid + fg + siteId;
				// classid=art_view&operatetype=HTML&siteId=72&columnId=417&artId=518
				String code = "classid=art_view___" + columnId + "___" + artId;

				
				contextUrl = contextUrl + "?" + code;
				StringBuffer html = new StringBuffer();
				html.append("<html><head><title></title></head>");
				html.append("<script language='javascript'>document.location = '");
				html.append(contextUrl);
				html.append("'</script></body></html>");
				return html.toString();
			}
		}

		if (siteId != null && !"".equals(siteId) && columnId != null && !"".equals(columnId) && artId != null
				&& !"".equals(artId)) {

			// 检查是否有听众控制Mabc2019.02.09
			String AudError = "N";
			String sqlAud = "select C.TZ_PROJECT_LIMIT,'Y' AUDFLG from PS_TZ_ART_AUDIENCE_T A,PS_TZ_AUD_LIST_T B,PS_TZ_ART_REC_TBL C WHERE B.TZ_DXZT='A' AND A.TZ_AUD_ID=B.TZ_AUD_ID AND A.TZ_ART_ID=C.TZ_ART_ID AND B.OPRID=? AND A.TZ_ART_ID=? LIMIT 1";
			Map<String, Object> mapAud = jdbcTemplate.queryForMap(sqlAud, new Object[] { oprid, artId });
			if (mapAud != null) {

				// 发布对象
				String pubFlg = (String) mapAud.get("TZ_PROJECT_LIMIT");
				// 是否当前人存在于该听众
				String audFlg = (String) mapAud.get("AUDFLG");

				if (pubFlg == "B" && audFlg != "Y") {
					// 如果设置发布对象为听众，不在听众中，不能访问
					strRet = " 您无权限查看";
					AudError = "Y";
				} else {
					AudError = "N";
				}

			} else {

				AudError = "N";
			}

			if (AudError == "N") {

				// 查看是否是外部链接;
				String sql = "SELECT TZ_ART_TYPE1,TZ_OUT_ART_URL FROM PS_TZ_ART_REC_TBL WHERE TZ_ART_ID = ?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { artId });
				if (map != null) {
					String artType = (String) map.get("TZ_ART_TYPE1");
					if ("B".equals(artType)) {
						String outurl = (String) map.get("TZ_OUT_ART_URL");
						if (outurl == null || "".equals(outurl)) {
							strRet = "未定义外部链接";
						} else {
							strRet = "<script type=\"text/javascript\">;location.href=\"" + outurl + "\"</script>";
						}
					}else{
						String htmlSQL = "select TZ_ART_NEWS_DT,TZ_ART_CONENT_SCR,TZ_ART_CONENT_SCR,TZ_ART_SJ_CONT_SCR from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=? and TZ_ART_NEWS_DT <= now()";
						Map< String, Object> contentMap = jdbcTemplate.queryForMap(htmlSQL,new Object[]{siteId,columnId,artId});
						if(contentMap == null){
							strRet = "当前时间不可查看该内容";
						}else{
							if("m".equals(from)){
								strRet = (String) contentMap.get("TZ_ART_SJ_CONT_SCR");
							}else{
								strRet = (String) contentMap.get("TZ_ART_CONENT_SCR");
							}
							
						}
					}
					
				}else{
					strRet = "参数错误，请联系系统管理员";
				}
			}

		}

		return strRet;
	}
}
