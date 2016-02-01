package com.tranzvision.gd.TZControlSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author WRL TZ_KJ_GL_PKG:TZ_KJ_GL_CLS
 * 
 * 控件定义
 */
@Service("com.tranzvision.gd.TZControlSetBundle.service.impl.ComsListClsServiceImpl")
public class ComsListClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	/* 查询控件列表 */
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
			String[] resultFldArray = {"TZ_COM_ID", "TZ_COM_MC", "TZ_COM_MC_EN", "TZ_COM_JSLJ", "TZ_COM_ICONLJ", "TZ_EFFEXP_ZT"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();

					mapList.put("kjID", rowList[0]);
					mapList.put("kjName", rowList[1]);
					mapList.put("kjEnName", rowList[2]);
					mapList.put("kjJsUrl", rowList[3]);
					mapList.put("kjPtUrl", rowList[4]);
					mapList.put("kjState", rowList[5]);
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
	
	/***
	 * 删除控件
	 */
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
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 控件id
				String kjID = jacksonUtil.getString("kjID");
				if(StringUtils.isNotEmpty(kjID)){
					//删除指定控件id的控件信息
					sqlQuery.update("DELETE FROM PS_TZ_COM_DY_T WHERE TZ_COM_ID=?", new Object[] { kjID });
					//删除指定控件id的控件信息(双语表)
					sqlQuery.update("DELETE FROM PS_TZ_COM_DY_ENG WHERE TZ_COM_ID=?", new Object[] { kjID });
					//删除指定控件id的控件用途信息
					sqlQuery.update("DELETE FROM PS_TZ_COM_YT_T WHERE TZ_COM_ID=?", new Object[] { kjID });
					//删除指定控件id的控件校验规则信息
					sqlQuery.update("DELETE FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID=?", new Object[] { kjID });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
