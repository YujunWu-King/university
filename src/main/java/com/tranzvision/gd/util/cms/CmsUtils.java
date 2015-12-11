package com.tranzvision.gd.util.cms;

import static com.tranzvision.gd.util.cms.web.FrontUtils.ART_ID;
import static com.tranzvision.gd.util.cms.web.FrontUtils.CHANNEL;
import static com.tranzvision.gd.util.cms.web.FrontUtils.SITE_ID;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.action.directive.ArticleAttachmentDirective;
import com.tranzvision.gd.util.cms.action.directive.ArticleImageDirective;
import com.tranzvision.gd.util.cms.action.directive.ContentDirective;
import com.tranzvision.gd.util.cms.action.directive.TextCutDirective;
import com.tranzvision.gd.util.cms.entity.main.CmsChannel;
import com.tranzvision.gd.util.cms.entity.main.CmsTemplate;
import com.tranzvision.gd.util.cms.manager.main.ChannelMng;
import com.tranzvision.gd.util.cms.manager.main.ChannelMngImpl;

/**
 * CMS工具类，提供用户接口 解析文章发布和活动发布内容；
 * 
 * @author WRL update by tangmm
 * 
 */
public class CmsUtils {
	// 解析文章内容；
	// 解析文章内容；
	public String content(String siteId, String chnlId, String id) {

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

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("CmsContent", new ContentDirective());
		root.put("text_cut", new TextCutDirective());
		root.put("ContentImage", new ArticleImageDirective());
	    root.put("ContentAtts", new ArticleAttachmentDirective());
		root.put(CHANNEL, channel);
		root.put(ART_ID, id);
		root.put(SITE_ID, siteId);

		/* 获得模版内容 */
		String tplSource = tpl.getPcContent();
		if (StringUtils.isBlank(tplSource)) {
			return null;
		}
		String tplName = tpl.getId();

		StringWriter out = new StringWriter();
		FreeMarkertUtils.processTemplate(tplSource, tplName, root, out);

		return out.toString();
	}
}