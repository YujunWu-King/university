package com.tranzvision.gd.util.cms.manager.main;

import com.tranzvision.gd.util.cms.entity.main.CmsChannel;
import com.tranzvision.gd.util.cms.entity.main.CmsTemplate;

public interface ChannelMng {

	/**
	 * 获取栏目对象
	 * @param chnlId
	 * @return
	 */
	public CmsChannel findById(String siteId,String chnlId);

	/**
	 * 获取栏目的栏目模板
	 * @param chnlId
	 * @return
	 */
//	public CmsTemplate findChnlTpl(String chnlId);
	
	/**
	 * 找到站点路径
	 * @param siteId
	 * @return
	 */
	public String getSitePath(String siteId);
	
	/**
	 * 获取栏目的内容模板
	 * 若是单页栏目，获取栏目模板
	 * @param chnlId
	 * @return
	 */
	public CmsTemplate findChnlContentTpl(String siteId, String chnlId);
	
	/**
	 * 根据模板ID，获取模板对象
	 * @param tmpid
	 * @return
	 */
	public CmsTemplate findTplById(String tmpid);
}
