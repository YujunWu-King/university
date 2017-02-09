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
			
			//获取栏目编号
			String coluSQL = "SELECT TZ_COLU_ID FROM PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
			String coluId = jdbcTemplate.queryForObject(coluSQL, new Object[] { strSiteId,strAreaId }, "String");

			String artIdSql = "SELECT TZ_ART_ID FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID=? and TZ_COLU_ID=? ORDER BY RAND() LIMIT 1";
			String artId = jdbcTemplate.queryForObject(artIdSql, new Object[]{strSiteId,coluId}, "String");
			
			if(artId!=null){
				String artSql = "SELECT TZ_ART_CONENT FROM PS_TZ_ART_REC_TBL WHERE TZ_ART_ID=?";
				artContent = jdbcTemplate.queryForObject(artSql, new Object[] { artId }, "String");
			}
			
			return artContent;
			
		} catch (Exception e) {
			
		}
		return "没有显示的数据";
	}
}