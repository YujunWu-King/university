package com.tranzvision.gd.TZSystemVariableMgBundle.service.impl;

import java.util.ArrayList;

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
	private JacksonUtil jacksonUtil;
	@Autowired
	private PsTzSysvarTMapper psTzSysvarTMapper;
	@Autowired
	private FliterForm fliterForm;
	
	/* 系统变量列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_SYSVARID", "ASC" } };
		fliterForm.orderByArr = orderByArr;

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_SYSVARID", "TZ_SYSVARNAME", "TZ_EFFFLG" };
		String jsonString = "";

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

		if (obj == null || obj.length == 0) {
			strRet = "{\"total\":0,\"root\":[]}";
		} else {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				jsonString = jsonString + ",{\"systemVarId\":\"" + rowList[0] + "\",\"systemVarName\":\"" + rowList[1]
						+ "\",\"isValid\":\"" + rowList[2] + "\"}";
			}
			if (!"".equals(jsonString)) {
				jsonString = jsonString.substring(1);
			}

			strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
		}

		return strRet;
	}
	
	/* 删除系统变量 */
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
