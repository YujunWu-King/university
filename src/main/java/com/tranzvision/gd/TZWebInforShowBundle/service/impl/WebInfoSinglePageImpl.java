package com.tranzvision.gd.TZWebInforShowBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * 招生站点获取栏目下随机一篇文章
 *
 */
@Service("com.tranzvision.gd.TZWebInforShowBundle.service.impl.WebInfoSinglePageImpl")
public class WebInfoSinglePageImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strAreaId = "";
			String artContent = "";
			
			strSiteId = jacksonUtil.getString("siteId");
			strAreaId = jacksonUtil.getString("areaId");

			if(strSiteId!=null&&!"".equals(strSiteId)&&strAreaId!=null&&!"".equals(strAreaId)){
				String artSql = "SELECT A.TZ_ART_CONENT FROM PS_TZ_ART_REC_TBL A INNER JOIN PS_TZ_LM_NR_GL_T B ON(A.TZ_ART_ID=B.TZ_ART_ID AND B.TZ_SITE_ID=?) INNER JOIN PS_TZ_SITEI_AREA_T C ON (B.TZ_COLU_ID = C.TZ_COLU_ID AND C.TZ_SITEI_ID=B.TZ_SITE_ID AND C.TZ_AREA_ID=?) ORDER BY RAND() LIMIT 1";
				artContent = jdbcTemplate.queryForObject(artSql, new Object[] { strSiteId,strAreaId }, "String");
			}
			
			artContent = artContent==null?"":artContent;
			
			return artContent;
			
		} catch (Exception e) {
			return e.toString();
		}
		
	}
}