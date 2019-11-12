package com.tranzvision.gd.TZWeakPasswordBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeakPasswordBundle.dao.PsTzWeakPasswordMapper;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 弱密码管理类
 */
@Service("com.tranzvision.gd.TZWeakPasswordBundle.service.impl.WeakPwdManageImpl")
public class WeakPwdManageImpl extends FrameworkImpl {
	
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsTzWeakPasswordMapper psTzWeakPasswordMapper;
	
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_PWD_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_PWD_ID", "TZ_PWD_VAL"};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			@SuppressWarnings("unchecked")
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("TZ_PWD_ID", rowList[0]);
				mapList.put("TZ_PWD_VAL", rowList[1]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				String tzPwdId = jacksonUtil.getString("TZ_PWD_ID");
				int i = psTzWeakPasswordMapper.deleteByPrimaryKey(tzPwdId);
				if(i <= 0){
					errMsg[0] = "1";
					errMsg[1] = "删除失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
