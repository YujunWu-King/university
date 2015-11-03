package com.tranzvision.gd.TZHardCodeMgBundle.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZHardCodeMgBundle.dao.PsCmbcHardcdPntMapper;
import com.tranzvision.gd.TZHardCodeMgBundle.model.PsCmbcHardcdPnt;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

import net.sf.json.JSONObject;
/*
 * Hardcode点定义， 原PS类：TZ_GD_HARDCODE_PKG:TZ_GD_HARDCODE_CLS
 * @author tang
 */
@Service("com.tranzvision.gd.TZHardCodeMgBundle.service.impl.HardCdPntServiceImpl")
public class HardCdPntServiceImpl extends FrameworkImpl {
	@Autowired
	private PsCmbcHardcdPntMapper psCmbcHardcdPntMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 加载HardCode列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		String strRet = "";
		FliterForm fliterForm = new FliterForm();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "CMBC_HARDCODE_PNT", "ASC" } };
		fliterForm.orderByArr = orderByArr;

		// json数据要的结果字段;
		String[] resultFldArray = { "CMBC_HARDCODE_PNT", "CMBC_DESCR254", "CMBC_HARDCODE_VAL" };
		String jsonString = "";

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, comParams,
				numLimit, numStart, errorMsg);

		if (obj == null || obj.length == 0) {
			strRet = "{\"total\":0,\"root\":[]}";
		} else {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				jsonString = jsonString + ",{\"hardCodeName\":\"" + rowList[0]
						+ "\",\"hardCodeDesc\":\"" + rowList[1]+ "\",\"hardCodeValue\":\"" + rowList[2] + "\"}";
			}
			if (!"".equals(jsonString)) {
				jsonString = jsonString.substring(1);
			}

			strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
		}

		return strRet;
	}
	
	
	/* 获取HardCode定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);

			if (CLASSJson.containsKey("hardCodeName")) {
				// hardcode ID;
				String hardCodeName = CLASSJson.getString("hardCodeName");
				PsCmbcHardcdPnt psCmbcHardcdPnt=  psCmbcHardcdPntMapper.selectByPrimaryKey(hardCodeName);
				if (psCmbcHardcdPnt != null) {
					strRet = "{\"hardCodeName\":\"" + TZUtility.transFormchar(psCmbcHardcdPnt.getCmbcHardcodePnt())
							+ "\",\"hardCodeDesc\":\"" + TZUtility.transFormchar(psCmbcHardcdPnt.getCmbcDescr254())
							+ "\",\"hardCodeValue\":\"" + TZUtility.transFormchar(psCmbcHardcdPnt.getCmbcHardcodeVal())
							+ "\",\"hardCodeDetailDesc\":\"" + TZUtility.transFormchar(psCmbcHardcdPnt.getCmbcDescr1000())
							+ "\"}";
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该hardcode数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该hardcode数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	@Override
	/* 新增hardcode方法 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// hardCode名称;
				String hardCodeName = CLASSJson.getString("hardCodeName");
				// hardCode描述;
				String hardCodeDesc = CLASSJson.getString("hardCodeDesc");
				String hardCodeValue = CLASSJson.getString("hardCodeValue");
				String hardCodeDetailDesc = CLASSJson.getString("hardCodeDetailDesc");

				String sql = "select COUNT(1) from PS_CMBC_HARDCD_PNT WHERE CMBC_HARDCODE_PNT=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { hardCodeName }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "HardCode点：" + hardCodeName + ",已经存在";
				} else {
					PsCmbcHardcdPnt psCmbcHardcdPnt = new PsCmbcHardcdPnt();
					psCmbcHardcdPnt.setCmbcHardcodePnt(hardCodeName);
					psCmbcHardcdPnt.setCmbcDescr254(hardCodeDesc);
					psCmbcHardcdPnt.setCmbcHardcodeVal(hardCodeValue);
					psCmbcHardcdPnt.setCmbcDescr1000(hardCodeDetailDesc);
					psCmbcHardcdPntMapper.insert(psCmbcHardcdPnt);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	@Override
	/* 修改hardcode方法 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// hardCode名称;
				String hardCodeName = CLASSJson.getString("hardCodeName");
				// hardCode描述;
				String hardCodeDesc = CLASSJson.getString("hardCodeDesc");
				String hardCodeValue = CLASSJson.getString("hardCodeValue");
				String hardCodeDetailDesc = CLASSJson.getString("hardCodeDetailDesc");

				String sql = "select COUNT(1) from PS_CMBC_HARDCD_PNT WHERE CMBC_HARDCODE_PNT=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { hardCodeName }, "Integer");
				if (count > 0) {
					PsCmbcHardcdPnt psCmbcHardcdPnt = new PsCmbcHardcdPnt();
					psCmbcHardcdPnt.setCmbcHardcodePnt(hardCodeName);
					psCmbcHardcdPnt.setCmbcDescr254(hardCodeDesc);
					psCmbcHardcdPnt.setCmbcHardcodeVal(hardCodeValue);
					psCmbcHardcdPnt.setCmbcDescr1000(hardCodeDetailDesc);
					psCmbcHardcdPntMapper.updateByPrimaryKeyWithBLOBs(psCmbcHardcdPnt);
					
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

	
	// 功能说明：删除Hardcode定义;
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

					JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
					// hardcode ID;
					String hardCodeName = CLASSJson.getString("hardCodeName");
					if (hardCodeName != null && !"".equals(hardCodeName)) {
						psCmbcHardcdPntMapper.deleteByPrimaryKey(hardCodeName);
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
