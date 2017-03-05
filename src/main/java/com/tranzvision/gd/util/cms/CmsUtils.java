package com.tranzvision.gd.util.cms;

import static com.tranzvision.gd.util.cms.web.FrontUtils.PAGE_NO;
import static com.tranzvision.gd.util.cms.web.FrontUtils.ART_ID;
import static com.tranzvision.gd.util.cms.web.FrontUtils.CHANNEL;
import static com.tranzvision.gd.util.cms.web.FrontUtils.SITE_ID;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cms.action.directive.ArticleAttachmentDirective;
import com.tranzvision.gd.util.cms.action.directive.ArticleImageDirective;
import com.tranzvision.gd.util.cms.action.directive.ArticlePageDirective;
import com.tranzvision.gd.util.cms.action.directive.ContentDirective;
import com.tranzvision.gd.util.cms.action.directive.ContentListDirective;
import com.tranzvision.gd.util.cms.action.directive.MenuDirective;
import com.tranzvision.gd.util.cms.action.directive.MenuListDirective;
import com.tranzvision.gd.util.cms.action.directive.MenuListRecallDirective;
import com.tranzvision.gd.util.cms.action.directive.TextCutDirective;
import com.tranzvision.gd.util.cms.entity.main.CmsChannel;
import com.tranzvision.gd.util.cms.entity.main.CmsMenu;
import com.tranzvision.gd.util.cms.entity.main.CmsTemplate;
import com.tranzvision.gd.util.cms.manager.main.ChannelMng;
import com.tranzvision.gd.util.cms.manager.main.ChannelMngImpl;
import com.tranzvision.gd.util.cms.manager.main.MenuMng;
import com.tranzvision.gd.util.cms.manager.main.MenuMngImpl;

/**
 * CMS工具类，提供用户接口 解析文章发布和活动发布内容；
 * 
 * @author WRL update by tangmm
 * 
 */
public class CmsUtils {
	// 解析文章内容；
	// 解析文章内容；
	public String content(String siteId, String chnlId, String id, String contentPath) {

		if (StringUtils.isBlank(id)) {
			return "文章编号为空";
		}
		ChannelMng channelMng = new ChannelMngImpl();
		// 模板实例,通过站点和栏目去查找对应栏目的模版信息
		CmsTemplate tpl = channelMng.findChnlContentTpl(siteId, chnlId);
		if (tpl == null) {
			return "未找到对应的模版";
		}
		// 栏目信息
		CmsChannel channel = channelMng.findById(siteId, chnlId);
		if (channel == null) {
			return "未找到对应的栏目";
		}

		String jgId = "";
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			jgId = jdbcTemplate.queryForObject(sql, new Object[] { siteId }, String.class);
		} catch (Exception e) {
			jgId = "";
		}

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("CmsContent", new ContentDirective());
		root.put("text_cut", new TextCutDirective());
		root.put("ContentImage", new ArticleImageDirective());
		root.put("ContentAtts", new ArticleAttachmentDirective());
		root.put(CHANNEL, channel);
		root.put(ART_ID, id);
		root.put(SITE_ID, siteId);
		String path = channelMng.getSitePath(siteId);
		root.put("CONTEXTPATH", path);
		root.put("TzUniversityContextPath", contentPath);
		root.put("CmsMenu", new MenuDirective());

		String strRandom = String.valueOf(10 * Math.random());
		
		String style = "<link href=\"" + contentPath + "/statics/css/website/orgs/" + jgId.toLowerCase() + "/"
				+ siteId + "/style_" + jgId.toLowerCase() + ".css?v=" + strRandom
				+ "\" rel=\"stylesheet\" type=\"text/css\" />";
		root.put("style", style);
		
		String jsAndCss = "<link href=\"" + contentPath + "/statics/css/website/orgs/" + jgId.toLowerCase() + "/"
				+ siteId + "/style_" + jgId.toLowerCase() + ".css?v=" + strRandom
				+ "\" rel=\"stylesheet\" type=\"text/css\" />" + "<script type=\"text/javascript\" src=\"" + contentPath
				+ "/statics/js/lib/jquery/jquery.min.js\"></script>" + "<script type=\"text/javascript\" src=\""
				+ contentPath + "/statics/js/tranzvision/extjs/app/view/website/set/js/pagefunc.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + contentPath
				+ "/statics/js/tranzvision/extjs/app/view/website/set/js/pic_list.js\"></script>";
		root.put("scriptsAndcss", jsAndCss);

		/* 获得模版内容 */
		String tplSource = tpl.getPcContent();
		if (StringUtils.isBlank(tplSource)) {
			return null;
		}
		String tplName = tpl.getId();

		StringWriter out = new StringWriter();
		try {
			FreeMarkertUtils.processTemplate(tplSource, tplName, root, out);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		String contents = out.toString();

		return contents;
	}
	
	// 解析文章内容；tplType:如果是手机版，则为MS，如果为pc版本：则为PC
	public String content(String siteId, String chnlId, String id,String tplType, String contentPath) {

		if (StringUtils.isBlank(id)) {
			return "文章编号为空";
		}
		ChannelMng channelMng = new ChannelMngImpl();
		// 模板实例,通过站点和栏目去查找对应栏目的模版信息
		CmsTemplate tpl = channelMng.findChnlContentTpl(siteId, chnlId);
		if (tpl == null) {
			return "未找到对应的模版";
		}
		// 栏目信息
		CmsChannel channel = channelMng.findById(siteId, chnlId);
		if (channel == null) {
			return "未找到对应的栏目";
		}

		String jgId = "";
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			jgId = jdbcTemplate.queryForObject(sql, new Object[] { siteId }, String.class);
		} catch (Exception e) {
			jgId = "";
		}

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("CmsContent", new ContentDirective());
		root.put("text_cut", new TextCutDirective());
		root.put("ContentImage", new ArticleImageDirective());
		root.put("ContentAtts", new ArticleAttachmentDirective());
		root.put(CHANNEL, channel);
		root.put(ART_ID, id);
		root.put(SITE_ID, siteId);
		String path = channelMng.getSitePath(siteId);
		root.put("CONTEXTPATH", path);
		root.put("TzUniversityContextPath", contentPath);
		root.put("CmsMenu", new MenuDirective());

		String strRandom = String.valueOf(10 * Math.random());
		
		String style = "<link href=\"" + contentPath + "/statics/css/website/orgs/" + jgId.toLowerCase() + "/"
				+ siteId + "/style_" + jgId.toLowerCase() + ".css?v=" + strRandom
				+ "\" rel=\"stylesheet\" type=\"text/css\" />";
		root.put("style", style);
		
		String jsAndCss = "<link href=\"" + contentPath + "/statics/css/website/orgs/" + jgId.toLowerCase() + "/"
				+ siteId + "/style_" + jgId.toLowerCase() + ".css?v=" + strRandom
				+ "\" rel=\"stylesheet\" type=\"text/css\" />" + "<script type=\"text/javascript\" src=\"" + contentPath
				+ "/statics/js/lib/jquery/jquery.min.js\"></script>" + "<script type=\"text/javascript\" src=\""
				+ contentPath + "/statics/js/tranzvision/extjs/app/view/website/set/js/pagefunc.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"" + contentPath
				+ "/statics/js/tranzvision/extjs/app/view/website/set/js/pic_list.js\"></script>";
		root.put("scriptsAndcss", jsAndCss);

		/* 获得模版内容 */
		String tplSource;
		if("MS".equals(tplType)){
			tplSource = tpl.getMsContent();
		}else{
			tplSource = tpl.getPcContent();
		}
		if (StringUtils.isBlank(tplSource)) {
			return null;
		}
		String tplName = tpl.getId();

		StringWriter out = new StringWriter();
		try {
			FreeMarkertUtils.processTemplate(tplSource, tplName, root, out);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		String contents = out.toString();

		return contents;
	}

	/**
	 * PAGE菜单菜单静态化
	 * 
	 * @param siteId
	 * @param id
	 * @return
	 */
	public CmsBean menuPage(String siteId, String id, String contentPath, String pageNo) {
		System.out.println("PAGE Creeate Star");
		long l = System.currentTimeMillis();
		if (StringUtils.isBlank(siteId)) {
			return null;
		}
		MenuMng menuMng = new MenuMngImpl(siteId);

		System.out.println("time:" + (System.currentTimeMillis() - l));
		// 根据ID得到菜单信息
		System.out.println("bbbbbbbbbb");
		CmsMenu menu = menuMng.findMenu(id, siteId);
		
//		if (menu == null) {
//			menu = menuMng.findRootMenu(siteId);
//		}
		if (menu == null) {
			return null;
		}
		System.out.println("menu:" + menu.getName());
		// 得到模版信息
		if (menu.getTmpId() != null && !menu.getTmpId().equals("")) {
			CmsTemplate tpl = menuMng.findTplById(menu.getTmpId());
			if (tpl != null && menu.getPageName() != null && !menu.getPageName().equals("")) {
				String jgId = "";
				try {
					GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
					JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
					String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					jgId = jdbcTemplate.queryForObject(sql, new Object[] { siteId }, String.class);
				} catch (Exception e) {
					jgId = "";
				}
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("CmsContent", new ContentDirective());
				root.put("text_cut", new TextCutDirective());
				root.put("ContentImage", new ArticleImageDirective());
				root.put("MenuListRecall", new MenuListRecallDirective());
				root.put("ContentAtts", new ArticleAttachmentDirective());
				root.put("CmsArticleList", new ContentListDirective()); // 1.文章列表标签（无分页）
				root.put("CmsContentPage", new ArticlePageDirective()); // 2.文章分页列表标签
				root.put("CmsMenu", new MenuDirective());
				root.put("menu", menu);
				String path = menuMng.getSitePath(siteId);
				root.put("CONTEXTPATH", path);
				root.put(SITE_ID, siteId);
				root.put(PAGE_NO, Integer.parseInt(pageNo));
				System.out.println("PAGE_NO:" + pageNo);
				root.put("TzUniversityContextPath", contentPath);

				// System.out.println("path:" + path);
				// System.out.println("contentPath:" + contentPath);

				String strRandom = String.valueOf(10 * Math.random());
				String jsAndCss = "<link href=\"" + contentPath + "/statics/css/website/orgs/" + jgId.toLowerCase()
						+ "/" + siteId + "/style_" + jgId.toLowerCase() + ".css?v=" + strRandom
						+ "\" rel=\"stylesheet\" type=\"text/css\" />" + "<script type=\"text/javascript\" src=\""
						+ contentPath + "/statics/js/lib/jquery/jquery.min.js\"></script>"
						+ "<script type=\"text/javascript\" src=\"" + contentPath
						+ "/statics/js/tranzvision/extjs/app/view/website/set/js/pagefunc.js\"></script>"
						+ "<script type=\"text/javascript\" src=\"" + contentPath
						+ "/statics/js/tranzvision/extjs/app/view/website/set/js/pic_list.js\"></script>";

				root.put("scriptsAndcss", jsAndCss);

				String tplSource = tpl.getPcContent();
				// System.out.println("tplSource:" + tplSource);
				if (StringUtils.isBlank(tplSource)) {
					return null;
				}
				String tplName = tpl.getId();
				StringWriter out = new StringWriter();
				try {
					FreeMarkertUtils.processTemplate(tplSource, tplName, root, out);
				} catch(Exception e) {
					e.printStackTrace();
					return null;
				}

				CmsBean bean = new CmsBean();
				bean.setHtml(out.toString());
				bean.setPath(menu.getStaticpath());
				bean.setHtmlName(menu.getPageName());
				return bean;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * PAGE菜单菜单静态化
	 * 
	 * @param siteId
	 * @param id
	 * @return
	 */
	public CmsBean menuPage(String siteId, String id, String contentPath, List<Map<String, Object>> menuList,
			String pageNo) {
		if (StringUtils.isBlank(siteId)) {
			return null;
		}
		MenuMng menuMng = new MenuMngImpl(menuList);
		// 根据ID得到菜单信息
		
		CmsMenu menu = menuMng.findMenu(id, siteId);
//		if (menu == null) {
//			menu = menuMng.findRootMenu(siteId);
//		}
		if (menu == null) {
			return null;
		}
		// 得到模版信息
		if (menu.getTmpId() != null && !menu.getTmpId().equals("")) {
			CmsTemplate tpl = menuMng.findTplById(menu.getTmpId());

			if (tpl != null && menu.getPageName() != null && !menu.getPageName().equals("")) {
				String jgId = "";
				try {
					GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
					JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
					String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					jgId = jdbcTemplate.queryForObject(sql, new Object[] { siteId }, String.class);
				} catch (Exception e) {
					jgId = "";
				}
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("CmsContent", new ContentDirective());
				root.put("text_cut", new TextCutDirective());
				root.put("ContentImage", new ArticleImageDirective());
				root.put("MenuListRecall", new MenuListRecallDirective());
				root.put("ContentAtts", new ArticleAttachmentDirective());
				root.put("CmsArticleList", new ContentListDirective()); // 1.文章列表标签（无分页）
				root.put("CmsContentPage", new ArticlePageDirective()); // 2.文章分页列表标签
				root.put("menu", menu);
				root.put("CmsMenu", new MenuDirective());
				String path = menuMng.getSitePath(siteId);
				root.put("CONTEXTPATH", path);
				root.put(SITE_ID, siteId);
				root.put("TzUniversityContextPath", contentPath);
				root.put(PAGE_NO, Integer.parseInt(pageNo));

				String strRandom = String.valueOf(10 * Math.random());
				String jsAndCss = "<link href=\"" + contentPath + "/statics/css/website/orgs/" + jgId.toLowerCase()
						+ "/" + siteId + "/style_" + jgId.toLowerCase() + ".css?v=" + strRandom
						+ "\" rel=\"stylesheet\" type=\"text/css\" />" + "<script type=\"text/javascript\" src=\""
						+ contentPath + "/statics/js/lib/jquery/jquery.min.js\"></script>"
						+ "<script type=\"text/javascript\" src=\"" + contentPath
						+ "/statics/js/tranzvision/extjs/app/view/website/set/js/pagefunc.js\"></script>"
						+ "<script type=\"text/javascript\" src=\"" + contentPath
						+ "/statics/js/tranzvision/extjs/app/view/website/set/js/pic_list.js\"></script>";

				root.put("scriptsAndcss", jsAndCss);

				String tplSource = tpl.getPcContent();

				if (StringUtils.isBlank(tplSource)) {
					return null;
				}
				String tplName = tpl.getId();
				StringWriter out = new StringWriter();
				try {
					FreeMarkertUtils.processTemplate(tplSource, tplName, root, out);
				} catch(Exception e) {
					e.printStackTrace();
					return null;
				}

				CmsBean bean = new CmsBean();
				bean.setHtml(out.toString());
				bean.setPath(menu.getStaticpath());
				bean.setHtmlName(menu.getPageName());
				return bean;

			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * BOOK菜单菜单静态化
	 * 
	 * @param siteId
	 * @param id
	 *            如果不传，说明是跟节点
	 * @return
	 */
	public CmsBean menuBook(String siteId, String id, List<Map<String, Object>> menuList) {

		if (StringUtils.isBlank(siteId)) {
			return null;
		}
		MenuMng menuMng = new MenuMngImpl(siteId);
		// 根据ID得到菜单信息
		CmsMenu menu = menuMng.findMenu(id, siteId);
		if (menu == null) {
			menu = menuMng.findRootMenu(siteId);
		}
		if (menu == null) {
			return null;
		}
		// 得到模版信息
		if (menu.getTmpId() != null && !menu.getTmpId().equals("")) {
			CmsTemplate tpl = menuMng.findTplById(menu.getTmpId());
			if (tpl != null && menu.getPageName() != null && !menu.getPageName().equals("")) {
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("CmsMenuList", new MenuListDirective());
				root.put("menu", menu);
				root.put(SITE_ID, siteId);
				root.put("CmsMenu", new MenuDirective());

				String tplSource = tpl.getPcContent();
				if (StringUtils.isBlank(tplSource)) {
					return null;
				}
				String tplName = tpl.getId();

				StringWriter out = new StringWriter();
				try {
					FreeMarkertUtils.processTemplate(tplSource, tplName, root, out);
				} catch(Exception e) {
					e.printStackTrace();
					return null;
				}
				CmsBean bean = new CmsBean();
				bean.setHtml(out.toString());
				bean.setPath(menu.getStaticpath());
				bean.setHtmlName(menu.getPageName());

				return bean;
			} else {
				CmsBean bean = new CmsBean();
				// bean.setHtml(out.toString());
				bean.setPath(menu.getStaticpath());
				bean.setHtmlName(menu.getPageName());
				return bean;
			}
		} else {
			CmsBean bean = new CmsBean();
			// bean.setHtml(out.toString());
			bean.setPath(menu.getStaticpath());
			bean.setHtmlName(menu.getPageName());
			return bean;
		}
	}

	public CmsBean menuBook(String siteId, String id) {

		if (StringUtils.isBlank(siteId)) {
			return null;
		}
		MenuMng menuMng = new MenuMngImpl(siteId);
		// 根据ID得到菜单信息
		CmsMenu menu=menuMng.findMenu(id, siteId);

		if (menu == null) {
			menu = menuMng.findRootMenu(siteId);
		}
		if (menu == null) {
			return null;
		}
		System.out.println("menuPage:" + menu.getPageName());
		// 得到模版信息
		if (menu.getTmpId() != null && !menu.getTmpId().equals("")) {
			CmsTemplate tpl = menuMng.findTplById(menu.getTmpId());
			// System.out.println("tpl:" + tpl);
			if (tpl != null && menu.getPageName() != null && !menu.getPageName().equals("")) {
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("CmsMenuList", new MenuListDirective());
				root.put("menu", menu);
				root.put(SITE_ID, siteId);
				root.put("CmsMenu", new MenuDirective());

				String tplSource = tpl.getPcContent();
				// System.out.println("tplSource:" + tplSource);
				if (StringUtils.isBlank(tplSource)) {
					return null;
				}
				String tplName = tpl.getId();

				StringWriter out = new StringWriter();
				try {
					FreeMarkertUtils.processTemplate(tplSource, tplName, root, out);
				} catch(Exception e) {
					e.printStackTrace();
					return null;
				}
				CmsBean bean = new CmsBean();
				bean.setHtml(out.toString());
				bean.setPath(menu.getStaticpath());
				bean.setHtmlName(menu.getPageName());

				return bean;
			} else {
				CmsBean bean = new CmsBean();
				// bean.setHtml(out.toString());
				bean.setPath(menu.getStaticpath());
				bean.setHtmlName(menu.getPageName());
				return bean;
			}
		} else {
			CmsBean bean = new CmsBean();
			// bean.setHtml(out.toString());
			bean.setPath(menu.getStaticpath());
			bean.setHtmlName(menu.getPageName());
			return bean;
		}
	}

}