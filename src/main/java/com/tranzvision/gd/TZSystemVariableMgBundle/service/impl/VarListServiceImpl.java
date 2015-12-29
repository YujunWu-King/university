package com.tranzvision.gd.TZSystemVariableMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSystemVariableMgBundle.dao.PsTzSysvarTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 * 功能说明：高端产品-系统变量管理；
 * 原PS类：TZ_GD_SYSVAR_PKG:TZ_GD_VARLIST_CLS
 */
@Service("com.tranzvision.gd.TZSystemVariableMgBundle.service.impl.VarListServiceImpl")
public class VarListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzSysvarTMapper psTzSysvarTMapper;
	@Autowired
	private FliterForm fliterForm;
	
	/* 系统变量列表 */
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
		String[][] orderByArr = new String[][] { { "TZ_SYSVARID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_SYSVARID", "TZ_SYSVARNAME", "TZ_EFFFLG" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null){
			
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("systemVarId", rowList[0]);
				mapList.put("systemVarName", rowList[1]);
				mapList.put("isValid", rowList[2]);
				
				listData.add(mapList);
			}
			
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 删除系统变量 */
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
				// 主题 ID;
				String sysVarID = jacksonUtil.getString("systemVarId");
				if (sysVarID != null && !"".equals(sysVarID)) {
					psTzSysvarTMapper.deleteByPrimaryKey(sysVarID);
					
					String deleteSQL = "DELETE FROM PS_TZ_SV_CHAIN WHERE TZ_SYSVARID=?";
					jdbcTemplate.update(deleteSQL,new Object[]{sysVarID});
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
