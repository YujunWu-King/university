package com.tranzvision.gd.util.cms.action.directive;

import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_BEAN;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.tranzvision.gd.util.cms.entity.main.CmsMenu;
import com.tranzvision.gd.util.cms.manager.main.MenuMng;
import com.tranzvision.gd.util.cms.manager.main.MenuMngImpl;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/** 
* @author  caoy
* @version 创建时间：2016年9月27日 下午9:19:48 
* 类说明   单个菜单的信息
*/
public class MenuDirective implements TemplateDirectiveModel {
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
		

		String id = DirectiveUtils.getString(PARAM_ID, params);
		String siteId = DirectiveUtils.getString(PARAM_SITE_ID, params);
		MenuMng menuMng = new MenuMngImpl(siteId);
		System.out.println("id："+id);
		System.out.println("siteId："+siteId);
		
		System.out.println("aaaaaaaaaaaaaaa");
		CmsMenu menu = menuMng.findMenu(id, siteId);
		
		System.out.println(menu.getTitleUrl());

		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_BEAN, DEFAULT_WRAPPER.wrap(menu));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
}

