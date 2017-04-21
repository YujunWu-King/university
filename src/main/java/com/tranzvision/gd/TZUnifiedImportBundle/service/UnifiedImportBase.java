package com.tranzvision.gd.TZUnifiedImportBundle.service;

import java.util.List;
import java.util.Map;

/**
 * 统一导入数据通用接口
 * 
 */
public interface UnifiedImportBase {

	/*
	 * 校验统一导入数据的方法
	 * data:保存的数据
	 * fields:保存数据字段
	 * targetTbl:目标表
	 * result:校验结果{校验是否成功boolean，校验提示信息String}
	 * */
	public void tzValidate(List<Map<String, Object>> data ,List<String> fields, String targetTbl ,Object[] result, String[] errMsg);
	
	/*
	 * 保存统一导入数据的方法
	 * data:保存的数据
	 * fields:保存数据字段
	 * targetTbl:目标表
	 * result:导入结果{导入总数，成功数量}
	 * */
	public String tzSave(List<Map<String, Object>> data ,List<String> fields, String targetTbl ,int[] result, String[] errMsg);
}
