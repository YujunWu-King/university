package com.tranzvision.gd.TZBaseBundle.service;

import java.util.List;

import com.tranzvision.gd.TZBaseBundle.service.impl.ConditionBean;

/**
 * 
 * ClassName: FliterFormForClassService
 * 
 * @author caoy
 * @version 1.0 Create Time: 2019年11月21日 上午11:45:52 Description:
 *          可配置查询使用Class查询的接口类
 */
public interface FliterFormForClassService {

	/**
	 * 
	 * Description:可配置查询使用Class查询的方法 Create Time: 2019年11月21日 上午11:47:11
	 * 
	 * @author caoy
	 * @param resultFld
	 * @param orderBy
	 * @param conditionList
	 * @param numLimit
	 * @param numStart
	 * @param maxNum 如果为0，不显示最大条数
	 * @param errorMsg
	 * @return
	 */
	public String[] searchFilter(String resultFld, String orderBy, List<ConditionBean> conditionList,
			int numLimit, int numStart,int maxNum, String[] errorMsg);
}
