package com.tranzvision.gd.util.cms.action.directive;
/** 
* @author  caoy
* @version 创建时间：2016年9月9日 上午10:49:25 
* 类说明   内容列表标签
*/

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;
import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_LIST;
import static com.tranzvision.gd.util.cms.web.FrontUtils.PARAM_STYLE_LIST;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.tranzvision.gd.util.cms.action.directive.abs.AbstractContentDirective;
import com.tranzvision.gd.util.cms.entity.main.CmsContent;
import com.tranzvision.gd.util.cms.web.FrontUtils;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.InvokeType;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ContentListDirective extends AbstractContentDirective {

	/**
	 * 输入参数，文章ID。允许多个文章ID，用","分开。排斥其他所有筛选参数。
	 */
	public static final String PARAM_IDS = "ids";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String init = FrontUtils.getType(env);
		if (StringUtils.equals("init", init)) {
			// TODO 初始化关联关系
			super.init(env, params);
			return;
		}
		List<CmsContent> list = getList(params, env);

		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		paramWrap.put(OUT_LIST, DEFAULT_WRAPPER.wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		InvokeType type = DirectiveUtils.getInvokeType(params);
		String listStyle = DirectiveUtils.getString(PARAM_STYLE_LIST, params);
		if (InvokeType.userDefined == type) {
			/*
			 * 注销 目前JAVA版本 不支持tpl模板调用类型
			 * 
			 * if (StringUtils.isBlank(listStyle)) { throw new
			 * ParamsRequiredException(PARAM_STYLE_LIST); }
			 * FrontUtils.includeTpl(listStyle, env);
			 */
		} else if (InvokeType.body == type) {
			body.render(env.getOut());
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@SuppressWarnings("unchecked")
	protected List<CmsContent> getList(Map<String, TemplateModel> params, Environment env) throws TemplateException {
		String ids = DirectiveUtils.getStringIds(PARAM_IDS, params);
		if (ids != null) {
			return articleMng.getListByIdsForTag(ids, getOrderBy(params));
		} else {
			return (List<CmsContent>) super.getData(params, env);
		}
	}

	@Override
	protected boolean isPage() {
		return false;
	}
}
