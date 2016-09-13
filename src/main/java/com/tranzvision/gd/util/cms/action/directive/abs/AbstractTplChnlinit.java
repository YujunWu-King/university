package com.tranzvision.gd.util.cms.action.directive.abs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.tranzvision.gd.util.cms.entity.main.ChannelTpl;
import com.tranzvision.gd.util.cms.manager.main.ArticleMng;
import com.tranzvision.gd.util.cms.manager.main.ArticleMngImpl;
import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;
import freemarker.core.Environment;
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
	

	@SuppressWarnings("unchecked")
	private void process(String channelIds, String tplId){
		if(StringUtils.isBlank(channelIds) || StringUtils.isBlank(tplId)){
			return;
		}
		String[] arrChnlid = channelIds.split(",");
		List<ChannelTpl> insertTpl = new ArrayList<ChannelTpl>();
		
		for(String chnlid: arrChnlid){
			if (articleMng == null) {
				articleMng = new ArticleMngImpl();
			}
			List<ChannelTpl> ctpls = (List<ChannelTpl>) articleMng.findChanTpl(chnlid);
			if(ctpls != null && ctpls.size() < 1){
				//在关联关系表中不存在栏目与模板的关联关系
				insertTpl.add(new ChannelTpl(chnlid,tplId));
			}else{
				int i = 0;
				for(ChannelTpl ctpl: ctpls){
					if(StringUtils.equals(tplId, ctpl.getTplid())){
						i++;
						break;
					}
				}
				if(i < 1){
					insertTpl.add(new ChannelTpl(chnlid,tplId));
				}
			}
		}
		articleMng.addChnlTpl(insertTpl);
	}

	/**
	 * 初始化栏目与模板的关联关系 栏目被那个模板文件引用
	 * 
	 * @param env
	 * @param params
	 * @throws TemplateException
	 */
	protected void init(Environment env, Map<String, TemplateModel> params) throws TemplateException {
		// 参数文件
		String channelIds = getChannelIds(params);
		if (StringUtils.isBlank(channelIds)) {
			return;
		}
		String tplid = env.getTemplate().getName();
		this.process(channelIds, tplid);
	}

	/**
	 * 获取参数栏目ID
	 * 
	 * @param params
	 * @return
	 * @throws TemplateException
	 */
	protected String getChannelIds(Map<String, TemplateModel> params) throws TemplateException {
		String ids = DirectiveUtils.getString(PARAM_CHANNEL_ID, params);
		if (StringUtils.isBlank(ids)) {
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
	protected String getFilters(Map<String, TemplateModel> params) throws TemplateException {
		String filters = DirectiveUtils.getString(PARAM_FILTERS_ID, params);
		if (StringUtils.isBlank(filters)) {
			return null;
		}

		return filters;
	}

	protected ArticleMng articleMng;
}
