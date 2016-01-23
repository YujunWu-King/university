package com.tranzvision.gd.TZWebSiteInfoBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.cms.CmsUtils;

@Service("com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml")
public class ArtContentHtml {
	@Autowired
	private HttpServletRequest request;
	
	/*得到解析后的内容*/
	public String getContentHtml(String siteid,String columnId ,String artId){
		String contentPath = request.getContextPath();
		String html = "";
		CmsUtils cmsUtils = new CmsUtils();
		html = cmsUtils.content(siteid, columnId, artId,contentPath);
		return html;
	}
}
