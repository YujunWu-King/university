package com.tranzvision.gd.TZExportTplMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZExportTplMgBundle.dao.TzExpTplDfnTMapper;
import com.tranzvision.gd.TZExportTplMgBundle.model.TzExpTplDfnT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 导出模板管理
 * @author shaweyet
 */
@Service("com.tranzvision.gd.TZExportTplMgBundle.service.impl.ExportTplMgImpl")
public class ExportTplMgImpl extends FrameworkImpl {
	@Autowired
	private TzExpTplDfnTMapper TzExpTplDfnTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	
	/* 加载导出模板列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_TPL_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_TPL_ID", "TZ_TPL_NAME", "TZ_JAVA_CLASS"};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
				numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("tplId", rowList[0]);
				mapList.put("tplName", rowList[1]);
				mapList.put("javaClass", rowList[3]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	/* 获取导出模板信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("tplId")) {
				String tplId = jacksonUtil.getString("tplId");
				TzExpTplDfnT tzExpTplDfnT=  TzExpTplDfnTMapper.selectByPrimaryKey(tplId);
				if (tzExpTplDfnT != null) {
					returnJsonMap.put("tplId", tzExpTplDfnT.getTzTplId());
					returnJsonMap.put("tplName", tzExpTplDfnT.getTzTplName());
					returnJsonMap.put("javaClass", tzExpTplDfnT.getTzJavaClass());
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该导出模板不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该导出模板不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	
	//@Override
	/* 新增hardcode方法 
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// hardCode名称;
				String hardCodeName = jacksonUtil.getString("hardCodeName");
				// hardCode描述;
				String hardCodeDesc = jacksonUtil.getString("hardCodeDesc");
				String hardCodeValue = jacksonUtil.getString("hardCodeValue");
				String hardCodeDetailDesc = jacksonUtil.getString("hardCodeDetailDesc");

				String sql = "select COUNT(1) from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { hardCodeName }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "HardCode点：" + hardCodeName + ",已经存在";
				} else {
					TzExpTplDfnT tzExpTplDfnT = new TzExpTplDfnT();
					tzExpTplDfnT.setTzHardcodePnt(hardCodeName);
					tzExpTplDfnT.setTzDescr254(hardCodeDesc);
					tzExpTplDfnT.setTzHardcodeVal(hardCodeValue);
					tzExpTplDfnT.setTzDescr1000(hardCodeDetailDesc);
					TzExpTplDfnTMapper.insert(tzExpTplDfnT);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}*/
	
	//@Override
	/* 修改hardcode方法 
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// hardCode名称;
				String hardCodeName = jacksonUtil.getString("hardCodeName");
				// hardCode描述;
				String hardCodeDesc = jacksonUtil.getString("hardCodeDesc");
				String hardCodeValue = jacksonUtil.getString("hardCodeValue");
				String hardCodeDetailDesc = jacksonUtil.getString("hardCodeDetailDesc");

				String sql = "select COUNT(1) from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { hardCodeName }, "Integer");
				if (count > 0) {
					TzExpTplDfnT tzExpTplDfnT = new TzExpTplDfnT();
					tzExpTplDfnT.setTzHardcodePnt(hardCodeName);
					tzExpTplDfnT.setTzDescr254(hardCodeDesc);
					tzExpTplDfnT.setTzHardcodeVal(hardCodeValue);
					tzExpTplDfnT.setTzDescr1000(hardCodeDetailDesc);
					TzExpTplDfnTMapper.updateByPrimaryKeyWithBLOBs(tzExpTplDfnT);
					
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新的HardCode点：" + hardCodeName + ",不存在";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
*/
	
	// 功能说明：删除Hardcode定义;
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					//提交信息
					String strForm = actData[num];
					jacksonUtil.json2Map(strForm);
					// hardcode ID;
					String hardCodeName = jacksonUtil.getString("hardCodeName");
					if (hardCodeName != null && !"".equals(hardCodeName)) {
						TzExpTplDfnTMapper.deleteByPrimaryKey(hardCodeName);
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
