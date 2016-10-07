package com.tranzvision.gd.util.cms.action.directive.abs;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.tranzvision.gd.util.cms.entity.main.CmsContent;
import com.tranzvision.gd.util.cms.manager.main.ArticleMngImpl;
import com.tranzvision.gd.util.cms.web.FrontUtils;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author caoy
 * @version 创建时间：2016年9月9日 下午2:44:14 类说明 内容对象标签
 */
public abstract class AbstractContentDirective extends AbstractTplChnlinit implements TemplateDirectiveModel {

	/**
	 * 输入参数，排序方式。
	 * <ul>
	 * <li>0：发布时间降序
	 * <li>1：发布时间升序
	 * <li>2：无
	 * <li>3：无
	 * <li>4：发生时间升序
	 * <li>5：发生时间降序
	 * </ul>
	 */
	public static final String PARAM_ORDER_BY = "orderBy";

	/**
	 * 获取参数排序值
	 * 
	 * @param params
	 * @return
	 * @throws TemplateException
	 */
	protected int getOrderBy(Map<String, TemplateModel> params) throws TemplateException {
		Integer orderBy = DirectiveUtils.getInt(PARAM_ORDER_BY, params);
		if (orderBy == null) {
			return 0;
		} else {
			return orderBy;
		}
	}

	protected Object getData(Map<String, TemplateModel> params, Environment env) throws TemplateException {
		if (articleMng == null) {
			articleMng = new ArticleMngImpl();
		}
		int orderBy = getOrderBy(params);
		
		//System.out.println("orderBy:" + orderBy);
		int count = FrontUtils.getCount(params);

		String channelIds = getChannelIds(params);

		if (StringUtils.isBlank(channelIds)) {
			return new ArrayList<CmsContent>();
		}

		if (!StringUtils.startsWith(channelIds, "'")) {
			channelIds = "'" + channelIds;
		}
		if (!StringUtils.endsWith(channelIds, "'")) {
			channelIds = channelIds + "'";
		}
		channelIds = StringUtils.replace(channelIds, ",", "','");

		String filters = getFilters(params);
		
		//System.out.println("orderBy:" + orderBy);
		if (isPage()) {  //分页
			// String speid = FrontUtils.getSpeId(env);
			// String divid = FrontUtils.getDivId(env);
			int pageNo = FrontUtils.getPageNo(env);
			return articleMng.getPageByChannelIdsForTag(channelIds, orderBy, pageNo, count, filters);
		} else { //不分页
			int first = FrontUtils.getFirst(params);
			return articleMng.getListByChannelIdsForTag(channelIds, orderBy, first, count);
		}
	}

	abstract protected boolean isPage();

}
