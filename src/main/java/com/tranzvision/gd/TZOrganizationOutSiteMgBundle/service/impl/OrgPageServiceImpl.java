package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.cms.CmsBean;
import com.tranzvision.gd.util.cms.CmsUtils;


/**
 * @author caoy
 * @version 创建时间：2016年10月9日 上午1:05:26 类说明
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.OrgPageServiceImpl")
public class OrgPageServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	final String filterPattern = "[<>{}\\[\\];\\&]";

	@Override
	public String tzGetHtmlContent(String strParams) {
		// 返回值;
		String strRet = "";

		String siteId = request.getParameter("siteId");
		String menuId = request.getParameter("menuId");
		String pageNo = request.getParameter("pageNo");
		
		
		
		
		if (siteId != null && !"".equals(siteId) && menuId != null && !"".equals(menuId) && pageNo != null
				&& !"".equals(pageNo)) {
			siteId = siteId.replaceAll(filterPattern,"");
			menuId = menuId.replaceAll(filterPattern,"");
			pageNo = pageNo.replaceAll(filterPattern,"");
			CmsBean cm = null;
			String contentPath = request.getContextPath();
			CmsUtils cu = new CmsUtils();
			cm = cu.menuPage(siteId, menuId, contentPath, pageNo);
			if (cm != null) {
				strRet = cm.getHtml();
			}
		}
		return strRet;
	}

//	public static String HTMLEncode(String aText) {
//		final StringBuilder result = new StringBuilder();
//		final StringCharacterIterator iterator = new StringCharacterIterator(aText);
//		char character = iterator.current();
//
//		while (character != CharacterIterator.DONE) {
//			if (character == '<') {
//				result.append("&lt;");
//			} else if (character == '>') {
//				result.append("&gt;");
//			} else if (character == '&') {
//				result.append("&amp;");
//			} else if (character == '\"') {
//				result.append("&quot;");
//			} else {
//				// the char is not a special one
//				// add it to the result as is
//				result.append(character);
//			}
//			character = iterator.next();
//		}
//		return result.toString();
//	}

}