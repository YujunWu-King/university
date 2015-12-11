package com.tranzvision.gd.util.cms.action.directive;

import static com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils.OUT_BEAN;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.tranzvision.gd.util.cms.action.directive.abs.AbstractTplChnlinit;
import com.tranzvision.gd.util.cms.entity.main.CmsContent;
import com.tranzvision.gd.util.cms.manager.main.ArticleMng;
import com.tranzvision.gd.util.cms.manager.main.ArticleMngImpl;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 内容对象标签
 * 
 * @author WRL
 * 
 */
public class ContentDirective extends AbstractTplChnlinit implements TemplateDirectiveModel {

	/**
	 * 输入参数，文章ID。
	 */
	public static final String PARAM_ID = "id";

	/**
	 * 输入参数，栏目ID。
	 */
	public static final String PARAM_CHANNEL_ID = "channelId";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		ArticleMng articleMng = new ArticleMngImpl();

		String id = DirectiveUtils.getString(PARAM_ID, params);
		String chln_id = DirectiveUtils.getString(PARAM_CHANNEL_ID, params);

		CmsContent content = articleMng.findArticleById(id,chln_id);

		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_BEAN, DEFAULT_WRAPPER.wrap(content));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
}
