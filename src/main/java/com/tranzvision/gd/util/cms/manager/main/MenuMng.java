package com.tranzvision.gd.util.cms.manager.main;

import java.util.List;
import com.tranzvision.gd.util.cms.entity.main.CmsMenu;
import com.tranzvision.gd.util.cms.entity.main.CmsTemplate;

/**
 * @author caoy
 * @version 创建时间：2016年9月6日 下午3:08:53 类说明
 */
public interface MenuMng {

	/**
	 * 查询指定站点下，指定菜单的子菜单项
	 * 
	 * @param id
	 * @param siteid
	 * @return
	 */
	public List<CmsMenu> findList(String siteId, String id);
	
	
	/**
	 * 找到站点路径
	 * @param siteId
	 * @return
	 */
	public String getSitePath(String siteId);

	/**
	 * 有子菜单 查询所有 的夫菜单
	 * 
	 * @param siteId
	 * @param id
	 * @return
	 */
	public List<CmsMenu> findRecallList(String siteId, String id);

	/**
	 * 获取当前菜单对象
	 * 
	 * @param id
	 * @param siteId
	 * @return
	 */
	public CmsMenu findMenu(String id, String siteId);

	/**
	 * 获取ROOT菜单对象
	 * 
	 * @param id
	 * @param siteId
	 * @return
	 */
	public CmsMenu findRootMenu(String siteId);

	/**
	 * 根据模板ID，获取模板对象
	 * 
	 * @param tmpid
	 * @return
	 */
	public CmsTemplate findTplById(String tmpid);

}
