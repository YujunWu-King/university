package com.tranzvision.gd.TZWebSiteInfoBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 内容预览；原：TZ_GD_ARTGL:ArtPreview
 * 
 * @author tang
 * @since 2015-11-26
 */
@Service("com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl.ArtPreviewServiceImpl")
public class ArtPreviewServiceImpl extends FrameworkImpl {
	@Autowired 
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		// 返回值;
		String strRet = "";
		
		String siteId = request.getParameter("siteId");
		String columnId = request.getParameter("columnId");
		String artId = request.getParameter("artId");
		String from = request.getParameter("from");
		if(siteId != null && !"".equals(siteId)
				&& columnId != null && !"".equals(columnId)
				&& artId != null && !"".equals(artId)){
			//查看是否是外部链接;
			String sql = "SELECT TZ_ART_TYPE1,TZ_OUT_ART_URL FROM PS_TZ_ART_REC_TBL WHERE TZ_ART_ID = ?";
			Map< String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{artId});
			if(map != null){
				String artType = (String) map.get("TZ_ART_TYPE1");
				if("B".equals(artType)){
					String outurl = (String) map.get("TZ_OUT_ART_URL");
					if(outurl == null || "".equals(outurl)){
						strRet = "未定义外部链接";
					}else{
						strRet = "<script type=\"text/javascript\">;location.href=\""+outurl+"\"</script>";
					}
				}else{
					String htmlSQL = "";
					if("m".equals(from)){
						htmlSQL= "select TZ_ART_SJ_HTML from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";
					}else{
						htmlSQL = "select TZ_ART_HTML from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";
					}
					strRet = jdbcTemplate.queryForObject(htmlSQL,new Object[]{siteId,columnId,artId},"String");
				}
				
			}else{
				strRet = "参数错误，请联系系统管理员";
			}
		}
		
		return strRet;
	}
}
