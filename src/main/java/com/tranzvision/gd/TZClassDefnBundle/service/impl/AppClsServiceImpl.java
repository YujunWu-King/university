package com.tranzvision.gd.TZClassDefnBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassDefnBundle.dao.PsTzAppclsTblMapper;
import com.tranzvision.gd.TZClassDefnBundle.model.PsTzAppclsTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 类方法定义， 原PS类：TZ_GD_APPCLS:TZ_LIST
 * @author tang
 */
@Service("com.tranzvision.gd.TZClassDefnBundle.service.impl.AppClsServiceImpl")
public class AppClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzAppclsTblMapper psTzAppclsTblMapper;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private JacksonUtil jacksonUtil;

	/* 查询类定义列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_APPCLS_ID", "ASC" } };
			fliterForm.orderByArr = orderByArr;

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_APPCLS_ID", "TZ_DESCR100" };
			String jsonString = "";

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

			if (obj == null || obj.length == 0) {
				strRet = "{\"total\":0,\"root\":[]}";
			} else {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					jsonString = jsonString + ",{\"appClassId\":\"" + rowList[0] + "\",\"appClassDesc\":\"" + rowList[1]
							+ "\"}";
				}

				if (!"".equals(jsonString)) {
					jsonString = jsonString.substring(1);
				}

				strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}

	/* 获取类定义信息 */
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			//JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			jacksonUtil.json2Map(strParams);
			
			if (jacksonUtil.containsKey("appClassId")) {
				// 类方法ID;
				String appClassId = jacksonUtil.getString("appClassId");
				PsTzAppclsTbl psTzAppclsTbl = psTzAppclsTblMapper.selectByPrimaryKey(appClassId);
				if (psTzAppclsTbl != null) {
					strRet = "{\"formData\":{\"appClassId\":\"" + TZUtility.transFormchar(psTzAppclsTbl.getTzAppclsId())
							+ "\",\"appClassDesc\":\"" + TZUtility.transFormchar(psTzAppclsTbl.getTzDescr100())
							+ "\",\"appClassName\":\"" + TZUtility.transFormchar(psTzAppclsTbl.getTzAppclsName())
							+ "\",\"appClassPath\":\"" + TZUtility.transFormchar(psTzAppclsTbl.getTzAppclsPath())
							+ "\",\"appClassMehtod\":\"" + TZUtility.transFormchar(psTzAppclsTbl.getTzAppclsMethod())
							+ "\"}}";
				} else {
					errMsg[0] = "1";
					errMsg[1] = "请选择类定义";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择类定义";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 新增类方法 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String appClassId = (String) infoData.get("appClassId");
				String appClassDesc = (String) infoData.get("appClassDesc");
				String appClassName = (String) infoData.get("appClassName");
				String appClassPath = (String) infoData.get("appClassPath");
				String appClassMehtod = (String) infoData.get("appClassMehtod");

				String sql = "select COUNT(1) from PS_TZ_APPCLS_TBL WHERE TZ_APPCLS_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { appClassId }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "类ID:" + appClassId + ",已经存在";
				} else {
					PsTzAppclsTbl psTzAppclsTbl = new PsTzAppclsTbl();
					psTzAppclsTbl.setTzAppclsId(appClassId);
					psTzAppclsTbl.setTzDescr100(appClassDesc);
					psTzAppclsTbl.setTzAppclsName(appClassName);
					psTzAppclsTbl.setTzAppclsPath(appClassPath);
					psTzAppclsTbl.setTzAppclsMethod(appClassMehtod);
					/** TODO %userid **/
					psTzAppclsTbl.setRowAddedDttm(new Date());
					psTzAppclsTbl.setRowAddedOprid("TZ_7");
					psTzAppclsTbl.setRowLastmantDttm(new Date());
					psTzAppclsTbl.setRowLastmantOprid("TZ_7");
					psTzAppclsTblMapper.insert(psTzAppclsTbl);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 修改类定义信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				String appClassId = (String) infoData.get("appClassId");
				String appClassDesc = (String) infoData.get("appClassDesc");
				String appClassName = (String) infoData.get("appClassName");
				String appClassPath = (String) infoData.get("appClassPath");
				String appClassMehtod = (String) infoData.get("appClassMehtod");

				String sql = "select COUNT(1) from PS_TZ_APPCLS_TBL WHERE TZ_APPCLS_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { appClassId }, "Integer");
				if (count > 0) {
					PsTzAppclsTbl psTzAppclsTbl = new PsTzAppclsTbl();
					psTzAppclsTbl.setTzAppclsId(appClassId);
					psTzAppclsTbl.setTzDescr100(appClassDesc);
					psTzAppclsTbl.setTzAppclsName(appClassName);
					psTzAppclsTbl.setTzAppclsPath(appClassPath);
					psTzAppclsTbl.setTzAppclsMethod(appClassMehtod);
					/** TODO %userid **/
					psTzAppclsTbl.setRowLastmantDttm(new Date());
					psTzAppclsTbl.setRowLastmantOprid("TZ_7");
					psTzAppclsTblMapper.updateByPrimaryKeySelective(psTzAppclsTbl);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "类ID:" + appClassId + "不存在";
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	// 功能说明：删除类定义;
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
				//提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类定义ID;
				String tzAppclsId = jacksonUtil.getString("appClassId");
				if (tzAppclsId != null && !"".equals(tzAppclsId)) {
					psTzAppclsTblMapper.deleteByPrimaryKey(tzAppclsId);
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
