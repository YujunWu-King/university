/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service;

import java.util.Map;

/**
 * 站点操作类方法定义接口
 * 
 * @author SHIHUA
 * @since 2015-12-14
 */
public interface TzSiteActionService {

	/**
	 * 站点区域保存
	 * 
	 * @param mapActData
	 * @param errMsg
	 * @return String
	 */
	public String tzSaveArea(Map<String, Object> mapActData, String[] errMsg);

	/**
	 * 站点区域代码发布
	 * 
	 * @param mapActData
	 * @param errMsg
	 * @return
	 */
	public String tzReleaseArea(Map<String, Object> mapActData, String[] errMsg);

	/**
	 * 获取站点区域的HTML代码
	 * 
	 * @param strParams
	 * @return
	 */
	public String tzGetHtmlContent(String strParams);

}
