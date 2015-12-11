package com.tranzvision.gd.util.cms.manager.main;

import java.util.List;

import com.tranzvision.gd.util.cms.entity.main.ArticleImage;
import com.tranzvision.gd.util.cms.entity.main.Attachment;
import com.tranzvision.gd.util.cms.entity.main.CmsContent;

public interface ArticleMng {
	/**
	 * 根据ID获取文章
	 * 
	 * @param id
	 * @return
	 */
	public CmsContent findArticleById(String id,String chnlid);
	
	/**
	 * 根据文章ID，获取文章相关的图片集
	 * 
	 * @param id
	 * @return
	 */
	public List<ArticleImage> findArticleImagesById(String id);
	
	/**
	 * 根据文章ID，获取文章相关的附件集
	 * @param id
	 * @return
	 */
	public List<Attachment> findArticleAttachmentsById(String id);
}
