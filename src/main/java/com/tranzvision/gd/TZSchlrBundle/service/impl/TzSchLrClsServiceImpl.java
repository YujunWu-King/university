package com.tranzvision.gd.TZSchlrBundle.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * @author LDD 奖学金录入
 */
@Service("com.tranzvision.gd.TZSchlrBundle.service.impl.TzSchLrClsServiceImpl")
public class TzSchLrClsServiceImpl extends FrameworkImpl {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private FliterForm fliterForm;
	
	/*获取用户账号信息列表*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams,int numLimit, int numStart, String[] errorMsg){
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//排序字段如果没有不要赋值
			//String[][] orderByArr=new String[][]{{"ROW_ADDED_DTTM","DESC"}};
			String[][] orderByArr=new String[][]{};
			//json数据要的结果字段;
			String[] resultFldArray = { "TZ_SCHLR_ID","TZ_SCHLR_NAME", "TZ_STATE", "TZ_DC_WJ_ID","TZ_DC_WJ_KSRQ","TZ_DC_WJ_JSRQ"};
					
			//可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit,numStart, errorMsg);
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("TZ_SCHLR_ID", rowList[0]);
					mapList.put("TZ_SCHLR_NAME", rowList[1]);
					mapList.put("TZ_STATE", rowList[2]);
					mapList.put("TZ_DC_WJ_ID", rowList[3]);
					mapList.put("TZ_DC_WJ_KSRQ", rowList[4]);
					mapList.put("TZ_DC_WJ_JSRQ", rowList[5]);
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

	
   
	
	
}
