package com.tranzvision.gd.util.cms.manager.main;

import java.util.List;

import com.tranzvision.gd.util.cms.entity.main.ArticleImage;
import com.tranzvision.gd.util.cms.entity.main.Attachment;
import com.tranzvision.gd.util.cms.entity.main.ChannelTpl;
import com.tranzvision.gd.util.cms.entity.main.CmsContent;
import com.tranzvision.gd.util.cms.page.Pagination;

public interface ArticleMng {
	/**
	 * 根据ID获取文章
	 * 
	 * @param id
	 * @return
	 */
	public CmsContent findArticleById(String id, String chnlid);

	/**
	 * 根据文章ID，获取文章相关的图片集
	 * 
	 * @param id
	 * @return
	 */
	public List<ArticleImage> findArticleImagesById(String id);

	/**
	 * 根据文章ID，获取文章相关的附件集
	 * 
	 * @param id
	 * @return
	 */
	public List<Attachment> findArticleAttachmentsById(String id);

	/**
	 * 分页查询
	 * 
	 * @param channelIds
	 * @param orderBy
	 * @param pageNo
	 * @param count
	 * @param filters
	 * @return
	 */
	public Pagination getPageByChannelIdsForTag(String channelIds, int orderBy, int pageNo, int count, String filters);

	/**
	 * 根据channelID获取栏目下的文章记录
	 * 
	 * @param channelIds
	 *            栏目ID
	 * @param orderBy
	 *            排序
	 * @param first
	 *            首记录
	 * @param count
	 *            显示记录数
	 * @return
	 */
	public List<CmsContent> getListByChannelIdsForTag(String channelIds, int orderBy, int first, int count);
	
	
	/**
	 * 根据文章ID获取对应的文章列表
	 * 
	 * @param ids
	 * @param orderBy
	 * @return
	 */
	public List<CmsContent> getListByIdsForTag(String ids, int orderBy);
	
	/**
	 * 查询当前栏目被那些模板文件引用
	 * 
	 * @param chnlId
	 * @return
	 */
	public List<ChannelTpl> findChanTpl(String chnlId);
	
	/**
	 * 在栏目、模板关系表中添加栏目模板关联关系
	 * 
	 * @param insertTpl
	 */
	public void addChnlTpl(List<ChannelTpl> insertTpl);
}
