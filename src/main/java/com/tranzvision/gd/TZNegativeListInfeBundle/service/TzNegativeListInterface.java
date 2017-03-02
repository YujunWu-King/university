package com.tranzvision.gd.TZNegativeListInfeBundle.service;

import com.tranzvision.gd.util.base.TzSystemException;

/**
 * 
 * @author tzhjl
 * @since 2017-02-8
 */

public interface TzNegativeListInterface {
	/**
	 * 
	 * 自动生成负面清单接口
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
	public boolean makeNegativeList(String classId, String batchId, String labelId);

}
