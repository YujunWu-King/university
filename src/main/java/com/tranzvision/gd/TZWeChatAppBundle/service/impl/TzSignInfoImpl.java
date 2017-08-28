package com.tranzvision.gd.TZWeChatAppBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatAppBundle.dao.SignInfoTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;

@Service("com.tranzvision.gd.TZWeChatAppBundle.service.impl.TzSignInfoImpl")
public class TzSignInfoImpl extends FrameworkImpl
{

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SignInfoTMapper signInfoTMapper;

	
	//签到 列表显示
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		System.out.println("==进入签到信息tzQueryList()方法==");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		returnMap.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{"ID","DESC"}};
			// json数据要的结果字段;
			String[] resultFldArray = {"ID","OPENID","IBEACON_NAME","SIGN_TIME","SIGN_ACCURACY","NICK_NAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("id", rowList[0]);
					mapList.put("openid", rowList[1]);
					mapList.put("ibeaconName", rowList[2]);
					mapList.put("signTime", rowList[3]);
					mapList.put("signAccuracy", rowList[4]);
					mapList.put("nickName", rowList[5]);
					listData.add(mapList);
				}

				returnMap.replace("total", obj[0]);
				returnMap.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jacksonUtil.Map2json(returnMap) ;
	}


	//删除 
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		
		System.out.println("==进入签到信息管理tzDelete()方法==");
		try{
			for(int i=0;i<actData.length;i++){
				System.out.println("actData:"+actData[i]);
				Map<String,Object>paramMap=new HashMap<String,Object>();
				JacksonUtil jsonUtil=new JacksonUtil();
				jsonUtil.json2Map(actData[i]);
				paramMap=jsonUtil.getMap();
				if(paramMap.get("id")!=null){
					signInfoTMapper.deleteByPrimaryKey(Integer.parseInt(paramMap.get("id").toString()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0]="1";
			errMsg[1]=e.toString();
		}
		return null;
	}
}
