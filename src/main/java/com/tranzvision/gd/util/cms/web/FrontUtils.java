package com.tranzvision.gd.util.cms.web;

import static com.tranzvision.gd.util.cms.web.Constants.TPL_SUFFIX;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.tranzvision.gd.util.cms.entity.main.CmsChannel;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;
import freemarker.core.Environment;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * 前台工具类
 * 
 * @author WRL
 * 
 */
public class FrontUtils {

	/**
	 * 传入参数，列表样式。
	 */
	public static final String PARAM_STYLE_LIST = "styleList";

	/**
	 * 站点编号
	 */
	public static final String SITE_ID = "siteId";

	/**
	 * 栏目对象
	 */
	public static final String CHANNEL = "channel";

	/**
	 * 栏目对象
	 */
	public static final String TYPE = "type";

	/**
	 * 页码
	 */
	public static final String PAGE_NO = "pageNo";

	/**
	 * 专题页编号
	 */
	public static final String SPE_ID = "speId";

	/**
	 * 分区编号
	 */
	public static final String DIV_ID = "divId";

	/**
	 * 菜单对象
	 */
	public static final String MENU = "menu";

	/**
	 * 页面没有找到
	 */
	public static final String PAGE_NOT_FOUND = "tpl.pageNotFound";
	/**
	 * 操作成功页面
	 */
	public static final String SUCCESS_PAGE = "tpl.successPage";
	/**
	 * 操作失败页面
	 */
	public static final String ERROR_PAGE = "tpl.errorPage";
	/**
	 * 信息提示页面
	 */
	public static final String MESSAGE_PAGE = "tpl.messagePage";
	/**
	 * 系统资源路径
	 */
	public static final String RES_SYS = "resSys";
	/**
	 * 模板资源路径
	 */
	public static final String RES_TPL = "res";
	/**
	 * 模板资源表达式
	 */
	public static final String RES_EXP = "${res}";
	/**
	 * 部署路径
	 */
	public static final String BASE = "base";
	/**
	 * 站点
	 */
	public static final String SITE = "site";

	/**
	 * 内容编号
	 */
	public static final String ART_ID = "contentId";

	/**
	 * 用户
	 */
	public static final String USER = "user";

	/**
	 * 页面完整地址
	 */
	public static final String LOCATION = "location";
	/**
	 * 页面翻页地址
	 */
	public static final String HREF = "href";
	/**
	 * href前半部（相对于分页）
	 */
	public static final String HREF_FORMER = "hrefFormer";
	/**
	 * href后半部（相对于分页）
	 */
	public static final String HREF_LATTER = "hrefLatter";

	/**
	 * 总条数
	 */
	public static final String COUNT = "count";
	/**
	 * 起始条数
	 */
	public static final String FIRST = "first";
	/**
	 * 传入参数，系统预定义翻页。
	 */
	public static final String PARAM_SYS_PAGE = "sysPage";

	/**
	 * 返回页面
	 */
	public static final String RETURN_URL = "returnUrl";

	/**
	 * 国际化参数
	 */
	public static final String ARGS = "args";

	/**
	 * 为前台模板设置分页相关数据
	 * 
	 * @param pageNo
	 * @param href
	 * @param urlFormer
	 * @param urlLatter
	 * @param map
	 */
	public static void frontPageData(int pageNo, String href, String hrefFormer, String hrefLatter,
			Map<String, Object> map) {
		map.put(PAGE_NO, pageNo);
		map.put(HREF, href);
		map.put(HREF_FORMER, hrefFormer);
		map.put(HREF_LATTER, hrefLatter);
	}

	/*
	 * public static void includePagination(Map<String, TemplateModel> params,
	 * Environment env) throws TemplateException, IOException { String sysPage =
	 * DirectiveUtils.getString(PARAM_SYS_PAGE, params); if
	 * (!StringUtils.isBlank(sysPage)) { CmsTemplate tpl = new
	 * TemplateMngImpl().findTplById(sysPage); Template template =
	 * FreeMarkertUtils.getStyleTpl(tpl.getContent(), tpl.getSiteId() +
	 * tpl.getId()); env.include(template); } else { // 没有包含分页 } }
	 */

	/**
	 * 获得模板路径。不对模板文件进行本地化处理。
	 * 
	 * @param solution
	 *            方案路径
	 * @param dir
	 *            模板目录。不本地化处理。
	 * @param name
	 *            模板名称。不本地化处理。
	 * @return
	 */
	public static String getTplPath(String solution, String dir, String name) {
		return solution + "/" + dir + "/" + name + TPL_SUFFIX;
	}

	/**
	 * 标签中获得站点编号
	 * 
	 * @param env
	 * @return
	 * @throws TemplateException
	 */
	public static String getSiteID(Environment env) throws TemplateException {
		TemplateModel siteId = env.getGlobalVariable(SITE_ID);
		if (siteId == null)
			return null;
		if (siteId instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) siteId).getAsString();
		} else {
			throw new TemplateModelException("'" + SITE_ID + "' not found in DataModel.");
		}
	}

	public static String getParamChnlId(Environment env) throws TemplateException {
		TemplateModel model = env.getGlobalVariable(CHANNEL);
		if (model instanceof AdapterTemplateModel) {
			CmsChannel channel = (CmsChannel) ((AdapterTemplateModel) model).getAdaptedObject(CmsChannel.class);
			if (channel == null)
				return null;
			return channel.getId();
		} else {
			throw new TemplateModelException("'" + CHANNEL + "' not found in DataModel");
		}
	}

	/**
	 * 获取当前是不是初始化操作
	 * 
	 * @param env
	 * @return
	 * @throws TemplateException
	 */
	public static String getType(Environment env) throws TemplateException {
		TemplateModel type = env.getGlobalVariable(TYPE);
		if (type == null)
			return null;
		if (type instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) type).getAsString();
		} else {
			throw new TemplateModelException("'" + TYPE + "' not found in DataModel.");
		}
	}

	/**
	 * 标签中获得页码
	 * 
	 * @param env
	 * @return
	 * @throws TemplateException
	 */
	public static int getPageNo(Environment env) throws TemplateException {
		TemplateModel pageNo = env.getGlobalVariable(PAGE_NO);
		if (pageNo == null)
			return 1;
		if (pageNo instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) pageNo).getAsNumber().intValue();
		} else {
			throw new TemplateModelException("'" + PAGE_NO + "' not found in DataModel.");
		}
	}

	public static int getFirst(Map<String, TemplateModel> params) throws TemplateException {
		Integer first = DirectiveUtils.getInt(FIRST, params);
		if (first == null || first <= 0) {
			return 0;
		} else {
			return first - 1;
		}
	}

	/**
	 * 标签中包含用户预定义列表样式模板
	 * 
	 * @param listStyle
	 * @param env
	 * @throws IOException
	 * @throws TemplateException
	 */
	// public static void includeTpl(String listStyle, Environment env) throws
	// IOException, TemplateException {
	//
	// CmsTemplate tpl = new TemplateMngImpl().findTplById(listStyle);
	//
	// Template template = FreeMarkertUtils.getStyleTpl(tpl.getContent(),
	// tpl.getSiteId()+tpl.getId());
	//
	// env.include(template);
	// }

	/**
	 * 标签参数中获得条数。
	 * 
	 * @param params
	 * @return 如果不存在，或者小于等于0，或者大于100则返回100；否则返回条数。
	 * @throws TemplateException
	 */
	public static int getCount(Map<String, TemplateModel> params) throws TemplateException {
		Integer count = DirectiveUtils.getInt(COUNT, params);
		if (count == null || count <= 0 || count >= 100) {
			return 100;
		} else {
			return count;
		}
	}

	public static String getSpeId(Environment env) throws TemplateModelException {
		TemplateModel speid = env.getGlobalVariable(SPE_ID);
		if (speid == null)
			return null;
		if (speid instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) speid).getAsString();
		} else {
			throw new TemplateModelException("'" + SPE_ID + "' not found in DataModel.");
		}
	}

	public static String getDivId(Environment env) throws TemplateModelException {
		TemplateModel divid = env.getGlobalVariable(DIV_ID);
		if (divid == null)
			return null;
		if (divid instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) divid).getAsString();
		} else {
			throw new TemplateModelException("'" + DIV_ID + "' not found in DataModel.");
		}
	}

	/* 清除内容中的HTML标签 */
	public static String replaceHtml(String html) {
		String regEx = "<.+?>"; // 表示标签
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

}
