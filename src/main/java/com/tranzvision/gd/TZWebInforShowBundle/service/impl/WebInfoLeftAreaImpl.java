package com.tranzvision.gd.TZWebInforShowBundle.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * 招生站点获取左侧区域一和区域二内容：友情链接和二维码
 *
 */
@Service("com.tranzvision.gd.TZWebInforShowBundle.service.impl.WebInfoLeftAreaImpl")
public class WebInfoLeftAreaImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			
			StringBuffer areaContent = new StringBuffer("");
			
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			
			String artLeft1Content = "";
			String artLeft1OuterHtml = "";
			
			String artLeft2Content = "";
			String artLeft2OuterHtml = "";
			
			
			strSiteId = jacksonUtil.getString("siteId");

			if(strSiteId!=null&&!"".equals(strSiteId)){
				String left1Sql = "SELECT A.TZ_ART_CONENT,C.TZ_AREA_PUBCODE FROM PS_TZ_ART_REC_TBL A INNER JOIN PS_TZ_LM_NR_GL_T B ON(A.TZ_ART_ID=B.TZ_ART_ID AND B.TZ_SITE_ID = ? AND B.TZ_ART_PUB_STATE='Y') INNER JOIN PS_TZ_SITEI_AREA_T C ON (B.TZ_COLU_ID = C.TZ_COLU_ID AND C.TZ_SITEI_ID=B.TZ_SITE_ID AND C.TZ_AREA_POSITION = 'L1') LIMIT 1";
				Map<String, Object> left1ContentMap = jdbcTemplate.queryForMap(left1Sql,new Object[] { strSiteId });
				if(left1ContentMap!=null){
					artLeft1Content = (String) left1ContentMap.get("TZ_ART_CONENT");
					artLeft1OuterHtml = (String) left1ContentMap.get("TZ_AREA_PUBCODE");
				}
				
				String left2Sql = "SELECT A.TZ_ART_CONENT,C.TZ_AREA_PUBCODE FROM PS_TZ_ART_REC_TBL A INNER JOIN PS_TZ_LM_NR_GL_T B ON(A.TZ_ART_ID=B.TZ_ART_ID AND B.TZ_SITE_ID = ? AND B.TZ_ART_PUB_STATE='Y') INNER JOIN PS_TZ_SITEI_AREA_T C ON (B.TZ_COLU_ID = C.TZ_COLU_ID AND C.TZ_SITEI_ID=B.TZ_SITE_ID AND C.TZ_AREA_POSITION = 'L2') LIMIT 1";
				Map<String, Object> left2ContentMap = jdbcTemplate.queryForMap(left2Sql,new Object[] { strSiteId });
				if(left2ContentMap!=null){
					artLeft2Content = (String) left2ContentMap.get("TZ_ART_CONENT");
					artLeft2OuterHtml = (String) left2ContentMap.get("TZ_AREA_PUBCODE");
				}
				
			}

		    if(artLeft1OuterHtml!=null){
		    	int left1DivIndex = artLeft1OuterHtml.indexOf(">");
		    	if(left1DivIndex>-1){
		    		areaContent = areaContent.append(artLeft1OuterHtml.substring(0, left1DivIndex+1));
		    		areaContent = areaContent.append(artLeft1Content==null?"":artLeft1Content);
		    		areaContent = areaContent.append("</div>");
		    	}
		    }
		    
		    if(artLeft2OuterHtml!=null){
		    	int left2DivIndex = artLeft2OuterHtml.indexOf(">");
		    	if(left2DivIndex>-1){
		    		areaContent = areaContent.append(artLeft2OuterHtml.substring(0, left2DivIndex+1));
		    		areaContent = areaContent.append(artLeft2Content==null?"":artLeft2Content);
		    		areaContent = areaContent.append("</div>");
		    	}
		    }
		    
			return areaContent.toString();
			
		} catch (Exception e) {
			System.out.println(e);
			return e.toString();
		}
		
	}
}