package com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZResourceCollectionMgBundle.dao.PsTzPtZyjhTblMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang; 
 * 功能说明：资源集合管理管理相关类;
 * 原PS类：TZ_GD_RESSET_PKG:TZ_GD_RESSET_LIST_CLS
 */
@Service("com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl.ResSetListServiceImpl")
public class ResSetListServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private PsTzPtZyjhTblMapper psTzPtZyjhTblMapper;

	/* 查询资源集合列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		FliterForm fliterForm = new FliterForm();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_ZYJH_ID", "ASC" } };
		fliterForm.orderByArr = orderByArr;

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_ZYJH_ID", "TZ_ZYJH_MC" };
		String jsonString = "";

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

		if (obj == null || obj.length == 0) {
			strRet = "{\"total\":0,\"root\":[]}";
		} else {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				jsonString = jsonString + ",{\"resSetID\":\"" + rowList[0] + "\",\"resSetDesc\":\"" + rowList[1]
						+ "\"}";
			}
			if (!"".equals(jsonString)) {
				jsonString = jsonString.substring(1);
			}

			strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
		}

		return strRet;
	}

	/* 删除资源集合信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 资源集合ID;
				String resSetID = jacksonUtil.getString("resSetID");
				if (resSetID != null && !"".equals(resSetID)) {
					psTzPtZyjhTblMapper.deleteByPrimaryKey(resSetID);
					String sql = "DELETE FROM PS_TZ_PT_ZYXX_TBL WHERE TZ_ZYJH_ID=?";
					jdbcTemplate.update(sql, new Object[] { resSetID });
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
