package com.tranzvision.gd.TZJudgesTypeBundle.service.impl;

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
 * 
 * @author xzx
 * 功能说明：面试评委组管理列表页面;
 * 原PS类：
 */
@Service("com.tranzvision.gd.TZJudgesTypeBundle.service.impl.JudgesGroupMsServiceImpl")
public class JudgesGroupMsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 查询面试评委组列表 */
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
		String[][] orderByArr = new String[][] { { "TZ_CLPS_GR_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID", "TZ_CLPS_GR_ID", "TZ_CLPS_GR_NAME" , "TZ_ROLENAME" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("jgID", rowList[0]);
				mapList.put("jugGroupId", rowList[1]);
				mapList.put("jugGroupName", rowList[2]);
				mapList.put("roleName", rowList[3]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	/*删除*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
 
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String jugGroupId = "";
				// 内容页信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strUserInfo);
				// 获取面试评委组编号
				jugGroupId = jacksonUtil.getString("jugGroupId");
				
			    if(jugGroupId != null && !"".equals(jugGroupId)){
			    	
			    	String deleteSQL = "DELETE FROM PS_TZ_MSPS_GR_TBL WHERE TZ_CLPS_GR_ID=?";
			    	jdbcTemplate.update(deleteSQL, new Object[]{jugGroupId});
			    	
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


