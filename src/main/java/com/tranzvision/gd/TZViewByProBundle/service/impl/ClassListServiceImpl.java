/**
 * 
 */
package com.tranzvision.gd.TZViewByProBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**  
 		* ClassName: ClassListServiceImpl
 		* @author 陈斯  zhongcg迁移
 		* @version 1.0 
 		* Create Time: 2018年12月18日 下午7:45:03 
 		* Description: 项目下的批次信息
*/
@Service("com.tranzvision.gd.TZViewByProBundle.service.impl.ClassListServiceImpl")
public class ClassListServiceImpl extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;
	/**
	 * 
	* Description:项目下的批次列表
	* Create Time: 2018年12月19日 下午3:35:41
	* @author 陈斯 
	* @param comParams
	* @param numLimit
	* @param numStart
	* @param errorMsg
	* @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_CLASS_NAME", "TZ_BATCH_ID", "TZ_BATCH_NAME", "TZ_APPLY_STATUS","TZ_RX_DT", "TZ_NUM_APPLICANTING","TZ_NUM_SUBMITTED","TZ_NUM_PASS"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classID", rowList[0]);
					mapList.put("className", rowList[1]);
					mapList.put("batchID", rowList[2]);
					mapList.put("batchName", rowList[3]);
					mapList.put("applyStatus", rowList[4]);
					mapList.put("admissionDate", rowList[5]);
					mapList.put("applicantingNumber", rowList[6]);
					mapList.put("submittedNumber", rowList[7]);
					mapList.put("passNumber", rowList[8]);
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
}
