package com.tranzvision.gd.TZAutomaticTagBundle.service.impl;

import com.tranzvision.gd.util.base.TzSystemException;

/**
 * 
 * @author YTT
 * @since 2017-03-01
 * @description 自动标签Interface  
 * 
 */

public interface TZAutomaticTagInterface {
	/**
	 * 
	 * 自动生成自动标签接口
	 * 
	 * @param classId
	 *            班级ID
	 * @param batchId
	 *            批次ID
	 * @param labelId
	 *            标签ID
	 * @return
	 * @throws TzSystemException
	 */
	
	public boolean automaticTagList(String classId, String batchId, String labelId);
	
}
