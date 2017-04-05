package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import com.tranzvision.gd.util.base.TzSystemException;

public interface TzClmsZddfInterface {

	
	/**
	 * 
	 * 自动标签接口
	 * 
	 * @param classId
	 *            班级ID
	 * @param batchId
	 *            批次ID
	 * @return
	 * @throws TzSystemException
	 */
	public boolean AutoCalculate(String classId, String batchId);
}



