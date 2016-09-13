package com.tranzvision.gd.util.cms.action.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;
import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_LIST;
import static com.tranzvision.gd.util.cms.web.FrontUtils.PARAM_STYLE_LIST;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.tranzvision.gd.util.cms.action.directive.abs.AbstractContentDirective;
import com.tranzvision.gd.util.cms.page.Pagination;
import com.tranzvision.gd.util.cms.web.FrontUtils;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.InvokeType;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author caoy
 * @version 创建时间：2016年9月13日 上午10:35:54 类说明
 */
public class ArticlePageDirective extends AbstractContentDirective {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String init = FrontUtils.getType(env);
		if (StringUtils.equals("init", init)) {
			// TODO 初始化关联关系
			super.init(env, params);
			return;
		}
		Pagination page = (Pagination) super.getData(params, env);

		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		paramWrap.put(OUT_PAGINATION, DEFAULT_WRAPPER.wrap(page));
		paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(page.getList()));

		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);

		InvokeType type = DirectiveUtils.getInvokeType(params);
		String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);

		if (InvokeType.userDefined == type) {
			/*
			 * 注销 目前JAVA版本 不支持tpl模板调用类型
			 * if (StringUtils.isBlank(listStyle)) { 
			 * 	throw new ParamsRequiredException(PARAM_STYLE_LIST); 
			 * }
			 * FrontUtils.includeTpl(listStyle, env);
			 * FrontUtils.includePagination(params, env);
			 */
		} else if (InvokeType.body == type) {
			if (body != null) {
				body.render(env.getOut());
				// FrontUtils.includePagination(params, env);  目前不支持 sysPage 样式
			}
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

	}

	@Override
	protected boolean isPage() {
		return true;
	}
}