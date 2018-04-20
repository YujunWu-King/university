package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.Nestable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 责任人选择
 * @author LuYan 2017-10-11
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzPersonChooseServiceImpl")
public class TzPersonChooseServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	
	
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			//排序字段如果没有不要赋值
			String[][] orderFldArray = new String[][] {{ "TZ_STAFF_TYPE", "ASC" }};
			//json数据要的结果字段
			String[] resultFldArray = {"OPRID","TZ_DLZH_ID","TZ_REALNAME","TZ_MOBILE","TZ_EMAIL"};
			
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderFldArray, strParams, numLimit, numStart, errorMsg);
			
			if(obj!=null && obj.length>0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				
				for(int i=0;i<list.size();i++) {
					String[] rowList = list.get(i);
					Map<String,Object> mapList = new HashMap<String,Object>();
					mapList.put("oprid", rowList[0]);
					mapList.put("dlzhId", rowList[1]);
					mapList.put("name", rowList[2]);
					mapList.put("mobile", rowList[3]);
					mapList.put("email", rowList[4]);
					
					listData.add(mapList);
				}
				
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
				
				strRet = jacksonUtil.Map2json(mapRet);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;		
	}
}
