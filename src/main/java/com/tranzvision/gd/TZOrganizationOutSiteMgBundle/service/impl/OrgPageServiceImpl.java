package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.cms.CmsBean;
import com.tranzvision.gd.util.cms.CmsUtils;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年10月9日 上午1:05:26 类说明
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.OrgPageServiceImpl")
public class OrgPageServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;

	@Override
	public String tzGetHtmlContent(String strParams) {
		// 返回值;
		String strRet = "";

		String siteId = request.getParameter("siteId");
		String menuId = request.getParameter("menuId");
		String pageNo = request.getParameter("pageNo");
		if (siteId != null && !"".equals(siteId) && menuId != null && !"".equals(menuId) && pageNo != null
				&& !"".equals(pageNo)) {
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
}
