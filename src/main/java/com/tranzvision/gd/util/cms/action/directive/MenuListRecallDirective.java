package com.tranzvision.gd.util.cms.action.directive;

import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_BEAN;
import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_LIST;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.entity.main.CmsMenu;
import com.tranzvision.gd.util.cms.manager.main.MenuMng;
import com.tranzvision.gd.util.cms.manager.main.MenuMngImpl;
import com.tranzvision.gd.util.cms.web.FrontUtils;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author caoy
 * @version 创建时间：2016年9月29日 下午6:11:26 类说明 有子菜单找到 父菜单的 的menu回溯标签
 */
public class MenuListRecallDirective implements TemplateDirectiveModel {
	/**
	 * 当前菜单ID
	 */
	public static final String PARAM_ID = "id";

	/**
	 * 站点ID
	 */
	public static final String PARAM_SITE_ID = "siteid";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVar2, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String init = FrontUtils.getType(env);
		if (StringUtils.equals("init", init)) {
			return;
		}
		List<CmsMenu> list = getList(params, env);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@SuppressWarnings("unchecked")
	private List<CmsMenu> getList(Map params, Environment env) throws TemplateException {
		String id = DirectiveUtils.getString(PARAM_ID, params);
		String siteId = FrontUtils.getSiteID(env);
		MenuMng menuMng = new MenuMngImpl(siteId);

		return menuMng.findRecallList(siteId, id);
	}
}
