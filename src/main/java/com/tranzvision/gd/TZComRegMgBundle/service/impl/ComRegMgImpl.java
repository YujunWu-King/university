package com.tranzvision.gd.TZComRegMgBundle.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqComzcTblMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;


/************************************************
 *** 开发人：tang* 开发时间：2015-10-9 功能说明：功能组件注册管理相关类
 * 原ps类：TZ_GD_COMREGMG_PKG:TZ_GD_COMREGMG_CLS
 ************************************************/
@Service("com.tranzvision.gd.TZComRegMgBundle.service.impl.ComRegMgImpl")
public class ComRegMgImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private PsTzAqComzcTblMapper psTzAqComzcTblMapper;
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询组件注册管理列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_COM_ID", "ASC" } };
			fliterForm.orderByArr = orderByArr;

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_COM_ID", "TZ_COM_MC" };
			String jsonString = "";

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

			if (obj == null || obj.length == 0) {
				strRet = "{\"total\":0,\"root\":[]}";
			} else {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					jsonString = jsonString + ",{\"comID\":\"" + rowList[0] + "\",\"comName\":\"" + rowList[1] + "\"}";
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

	/* 删除组件注册信息 */
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
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				String sComID = jacksonUtil.getString("comID");
				psTzAqComzcTblMapper.deleteByPrimaryKey(sComID);
				
				String comPageSql = "DELETE FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=?";
				jdbcTemplate.update(comPageSql,new Object[]{sComID});		
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
