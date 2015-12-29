package com.tranzvision.gd.TZResourceCollectionMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	private PsTzPtZyjhTblMapper psTzPtZyjhTblMapper;
	@Autowired
	private FliterForm fliterForm;

	/* 查询资源集合列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_ZYJH_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_ZYJH_ID", "TZ_ZYJH_MC" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("resSetID", rowList[0]);
				mapList.put("resSetDesc", rowList[1]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/* 删除资源集合信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
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
