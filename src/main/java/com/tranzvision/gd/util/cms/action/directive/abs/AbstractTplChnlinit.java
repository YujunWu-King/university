package com.tranzvision.gd.util.cms.action.directive.abs;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.manager.main.ArticleMng;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;

import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 用于建立栏目与模板之间的调用关系(即栏目A出现在那个模板中)
 * 
 * @author WRL
 *
 */
public abstract class AbstractTplChnlinit implements TemplateDirectiveModel {

	/**
	 * 输入参数，栏目ID。允许多个栏目ID，用","分开。和channelPath之间二选一，ID优先级更高。
	 */
	public static final String PARAM_CHANNEL_ID = "channelId";
	public static final String PARAM_FILTERS_ID = "filters";
	
	/**
	 * 获取参数栏目ID
	 * 
	 * @param params
	 * @return
	 * @throws TemplateException
	 */
	protected String getChannelIds(Map<String, TemplateModel> params)
			throws TemplateException {
		String ids = DirectiveUtils.getString(PARAM_CHANNEL_ID, params);
		if(StringUtils.isBlank(ids)){
			return null;
		}

		return ids;
	}
	
	/**
	 * 获取参数栏目ID
	 * 
	 * @param params
	 * @return
	 * @throws TemplateException
	 */
	protected String getFilters(Map<String, TemplateModel> params)
			throws TemplateException {
		String filters = DirectiveUtils.getString(PARAM_FILTERS_ID, params);
		if(StringUtils.isBlank(filters)){
			return null;
		}

		return filters;
	}
	
	protected ArticleMng articleMng;
}
