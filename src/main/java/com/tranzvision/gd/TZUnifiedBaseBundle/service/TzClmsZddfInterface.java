package com.tranzvision.gd.TZUnifiedBaseBundle.service;

import com.tranzvision.gd.util.base.TzSystemException;

/**
 * 
 * @author GXD
 * @since 2017-4-6
 */

public interface TzClmsZddfInterface {

	
	/**
	 * 
	 * 自动筛选接口
	 * 
	 * @param TZ_APP_ID
	 *            报名表ID
	 * @param TZ_SCORE_ID
	 *            成绩单ID
	 * @param TZ_SCORE_ITEM
	 *            成绩项ID
	 * @return 
	 * @throws TzSystemException
	 */
	public float AutoCalculate(String TZ_APP_ID,String TZ_SCORE_ID,String TZ_SCORE_ITEM);
	
}



