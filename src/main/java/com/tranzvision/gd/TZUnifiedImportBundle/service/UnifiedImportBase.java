package com.tranzvision.gd.TZUnifiedImportBundle.service;

import java.util.List;
import java.util.Map;

/**
 * 统一导入数据通用接口
 * 
 */
public interface UnifiedImportBase {

	/*
	 * 保存统一导入数据的方法
	 * data:保存的数据
	 * fields:保存数据字段
	 * targetTbl:目标表
	 * result:导入结果
	 * */
	public String tzSave(List<Map<String, Object>> data ,List<String> fields, String targetTbl ,int[] result, String[] errMsg);
}
