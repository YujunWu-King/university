package com.tranzvision.gd.TZWebSiteInfoBundle.service.impl;

import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.cms.CmsUtils;

@Service("com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml")
public class ArtContentHtml {
	/*得到解析后的内容*/
	public String getContentHtml(String siteid,String columnId ,String artId){
		String html = "";
		CmsUtils cmsUtils = new CmsUtils();
		html = cmsUtils.content(siteid, columnId, artId);
		return html;
	}
}
