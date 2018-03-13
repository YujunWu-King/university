package com.tranzvision.gd.TZWebSiteInfoBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

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
		//只有设置了听众的活动才需要登录，否则允许匿名查看和报名， zhanglang修改， 2018-03-13
		if (siteId != null && !siteId.equals("")) {
			
			//活动发布对象，A-无限制，B-听众
			String sql = "select TZ_PROJECT_LIMIT from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
			String artLimitType = jdbcTemplate.queryForObject(sql, new Object[] { artId }, "String");
			
			// 如果用户未登录 直接 跳到登录页面
			if ("B".equals(artLimitType) 
					&& (oprid == null || oprid.equals(""))) {
				// 根据siteId得到机构ID
				sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String jgid = jdbcTemplate.queryForObject(sql, new Object[] { siteId }, "String");

				String contextUrl = request.getContextPath();
				String fg = "/";
				if (!contextUrl.endsWith(fg)) {
					contextUrl = contextUrl + fg;
				}
				contextUrl = contextUrl + "user/login/" + jgid + fg + siteId;
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

			// 检查是否有听众控制Mabc2017.02.09
			String AudError = "N";
			//查询语句修改，卢艳，2017-12-1
			String sqlAud = "SELECT C.TZ_PROJECT_LIMIT,(SELECT 'Y' FROM PS_TZ_ART_AUDIENCE_T A,PS_TZ_AUD_LIST_T B WHERE A.TZ_ART_ID=C.TZ_ART_ID AND A.TZ_AUD_ID=B.TZ_AUD_ID AND B.TZ_DXZT='A' AND B.OPRID=? LIMIT 0,1) AUDFLG FROM PS_TZ_ART_REC_TBL C WHERE C.TZ_ART_ID=?";
			Map<String, Object> mapAud = jdbcTemplate.queryForMap(sqlAud, new Object[] { oprid, artId });
			if (mapAud != null) {

				// 发布对象
				String pubFlg = (String) mapAud.get("TZ_PROJECT_LIMIT");
				// 是否当前人存在于该听众
				String audFlg = (String) mapAud.get("AUDFLG");

				if ("B".equals(pubFlg) && !"Y".equals(audFlg)) {
					// 如果设置发布对象为听众，不在听众中，不能访问
					strRet = " 您无权限查看";
					AudError = "Y";
				} else {
					AudError = "N";
				}

			} else {

				AudError = "N";
			}

			if ("N".equals(AudError)) {

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
							
							/*********************如果外部链接为活动链接引用，需要处理form参数，张浪添加，20170419***********************/
							int actIndex = outurl.indexOf("/dispatcher?");  //判断外部链接是否为活动引用
							int actIndex2 = outurl.indexOf("classid=art_view");
							int actIndex3 = outurl.indexOf("operatetype=HTML");
							if(actIndex > 0 && actIndex2 > 0 && actIndex3 > 0){
								/**
								 * 防止进入死循环
								 */
								String currentUrl; //当前URL
								if("".equals(request.getQueryString())){
									currentUrl = request.getRequestURL().toString();
								}else{
									currentUrl = request.getRequestURL().toString()+"?"+request.getQueryString();
								}
								
								boolean isNotSame = this.isNotSameUrl(outurl, currentUrl);
								if(!isNotSame){
									strRet = "外部链接定义不能定义为当前活动发布连接";
								}else{
									int fromIndex = outurl.indexOf("from=m");
									if("m".equals(from)){
										if(fromIndex < 0){
											outurl = outurl + "&from=m";
										}
									}else{
										if(fromIndex >= 0){
											outurl = outurl.replace("&from=m", "").replace("?from=m&", "?").replace("?from=m", "");
										}
									}
									strRet = "<script type=\"text/javascript\">;location.href=\"" + outurl + "\"</script>";
								}
							}
							/*********************如果外部链接为活动链接引用，需要处理form参数，张浪添加，20170419***********************/
						}
					}else{
						/* 不需要根据发布时间顾虑*/
						String htmlSQL = "select TZ_ART_NEWS_DT,TZ_ART_CONENT_SCR,TZ_ART_CONENT_SCR,TZ_ART_SJ_CONT_SCR from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";
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
	
	
	
	/**
	 * 判断是否为同一个活动的发布url
	 * @param url1
	 * @param url2
	 * @return
	 */
	private boolean isNotSameUrl(String url1,String url2){
		boolean notSameUrl = false;
		//去除form参数
		String strUrl1 = url1.replace("&from=m", "").replace("?from=m", "");
		String strUrl2 = url2.replace("&from=m", "").replace("?from=m", "");
		
		if(strUrl1.equals(strUrl2)){
			notSameUrl = false;
		}else{
			String[] urlParamsArr = strUrl1.split("[?|&]");
			int i;
			for(i=0; i<urlParamsArr.length; i++){
				int index = strUrl2.indexOf(urlParamsArr[i]);
				if(index < 0){
					//不是同一个活动发布url
					notSameUrl = true;
					break;
				}
			}
		}
		return notSameUrl;
	}
}
