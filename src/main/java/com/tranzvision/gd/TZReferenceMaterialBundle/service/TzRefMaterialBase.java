package com.tranzvision.gd.TZReferenceMaterialBundle.service;

import java.util.Map;

/**
 * 参考资料展现统一接口
 * @author zhanglang
 * 2017-04-01
 */
public interface TzRefMaterialBase {

	/**
	 * 生成参考资料页面代码
	 * @param dataMap
	 * 	 	[{"classId":""}, 班级ID
	 * 		{"batchId":""}, 批次ID
	 * 		{"appInsId":""}, 报名表实例ID
	 * 		{"cjxId":""}] 成绩项ID
	 * @return
	 */
	public String genRefDataPage(Map<String,String> dataMap);
}
