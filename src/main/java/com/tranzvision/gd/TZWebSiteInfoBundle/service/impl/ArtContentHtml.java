package com.tranzvision.gd.TZWebSiteInfoBundle.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.cms.CmsBean;
import com.tranzvision.gd.util.cms.CmsUtils;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml")
public class ArtContentHtml {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	/*得到解析后的内容*/
	public String getContentHtml(String siteid,String columnId ,String artId){
		String contentPath = request.getContextPath();
		String html = "";
		CmsUtils cmsUtils = new CmsUtils();
		html = cmsUtils.content(siteid, columnId, artId,contentPath);
		return html;
	}
	
	/*静态化栏目*/
	
	public boolean staticSiteInfoByChannel(String siteid,String columnId){
		boolean br = true;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.TZ_MENU_ID,A.TZ_MENU_TYPE FROM PS_TZ_SITEI_MENU_T A,PS_TZ_ASSCHNL_T B ");
		sb.append("WHERE A.TZ_TEMP_ID = B.TZ_TEMP_ID AND B.TZ_COLU_ID=? ");
		List<Map<String, Object>> thisMenu = jdbcTemplate.queryForList(sb.toString(), new Object[] { columnId });

		if (thisMenu != null && thisMenu.size() > 0) {

			sb = new StringBuffer(
					"select TZ_MENU_ID,TZ_SITEI_ID,TZ_MENU_NAME,TZ_MENU_TYPE,TZ_MENU_LEVEL,TZ_MENU_XH,");
			sb.append("ifnull(TZ_F_MENU_ID,\"\") TZ_F_MENU_ID,");
			sb.append("ifnull(TZ_D_MENU_ID,\"\") TZ_D_MENU_ID,");
			sb.append("ifnull(TZ_MENU_PATH,\"\") TZ_MENU_PATH,");
			sb.append("ifnull(TZ_TEMP_ID,\"\") TZ_TEMP_ID,");
			sb.append("ifnull(TZ_PAGE_NAME,\"\") TZ_PAGE_NAME");
			sb.append(" from PS_TZ_SITEI_MENU_T");
			sb.append(" where TZ_SITEI_ID=? ");
			List<Map<String, Object>> menu = jdbcTemplate.queryForList(sb.toString(), new Object[] { siteid });

			Map<String, Object> mapNode = null;
			for (Object objNode : thisMenu) {
				mapNode = (Map<String, Object>) objNode;
				this.menu(mapNode.get("TZ_MENU_ID").toString(), mapNode.get("TZ_MENU_TYPE").toString(), siteid,
						menu);
			}
		}
		return br;
	}
	
	/*静态化栏目*/
	private boolean menu(String menuId, String menuType, String siteId, List<Map<String, Object>> menuList) {
		String contentPath = request.getContextPath();
		boolean br = false;
		
		try {

			// A:PAGE B:BOOK
			CmsUtils cu = new CmsUtils();
			CmsBean bean = null;
			if (menuType.equals("A")) {
				bean = cu.menuPage(siteId, menuId, contentPath, menuList);
			} else {
				bean = cu.menuBook(siteId, menuId, menuList);
			}

			if (bean != null) {
				String strFileName = bean.getHtmlName();
				String menuHtml = bean.getHtml();
				String strFilePath = bean.getPath();
				if (menuHtml != null && !menuHtml.equals("") && strFileName != null && !strFileName.equals("")
						&& strFilePath != null && !strFilePath.equals("")) {
					// 更新文件
					String dir = getSysHardCodeVal.getWebsiteEnrollPath();
					dir = request.getServletContext().getRealPath(dir);
					dir = dir + File.separator + strFilePath;
					br = this.staticFile(menuHtml, dir, strFileName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return br;
	}
	
	public boolean staticFile(String strReleasContent, String dir, String fileName) {
		try {
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			String filePath = "";
			if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				filePath = dir + File.separator + fileName;
			} else {
				filePath = dir + fileName;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strReleasContent);
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
			
}
