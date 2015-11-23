package com.tranzvision.gd.TZEmailParameterBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 邮件服务器参数设置列表；原：TZ_GD_EMLSMSSET_PKG:TZ_GD_EMLSERMG_CLS
 * 
 * @author tang
 * @since 2015-11-19
 */
@Service("com.tranzvision.gd.TZEmailParameterBundle.service.impl.EmailParameterServiceImpl")
public class EmailParameterServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};
			fliterForm.orderByArr = orderByArr;

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_EMLSERV_ID", "TZ_EML_ADDR100", "TZ_JG_ID", "TZ_JG_NAME", "TZ_CHS_SNAME", "TZ_EML_ALIAS" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("emailservid", rowList[0]);
					mapList.put("emailaddr", rowList[1]);
					mapList.put("emailorg", rowList[2]);
					mapList.put("emailorgName", rowList[3]);
					mapList.put("chnsname", rowList[4]);
					mapList.put("emlalias", rowList[5]);

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
	
	/* 删除邮件服务器参数*/
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
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 参数id;
				String emailservid = jacksonUtil.getString("emailservid");
				if (emailservid != null && !"".equals(emailservid)) {
					//删除菜单;
					String deletesql = "DELETE FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_EMLSERV_ID=?";
					jdbcTemplate.update(deletesql, new Object[]{emailservid});
					
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
