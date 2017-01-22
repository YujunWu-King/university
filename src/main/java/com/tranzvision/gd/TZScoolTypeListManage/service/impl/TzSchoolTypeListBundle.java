package com.tranzvision.gd.TZScoolTypeListManage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.TZScoolTypeListManage.dao.PsTzShoolLogTMapper;
import com.tranzvision.gd.TZScoolTypeListManage.model.PsTzShoolLogT;


@Service("com.tranzvision.gd.TZScoolTypeListManage.service.impl.TzSchoolTypeListBundle")
public class TzSchoolTypeListBundle extends FrameworkImpl{
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private  PsTzShoolLogTMapper PsTzShoolLogTMapper;

	/* 查询项目分类列表 */
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
		   
	
		String[][] orderByArr = new String[][] { { "TZ_SCHOOL_TYPEID", "ASC" } };

		// json数据要的结果字段;
		
		String[] resultFldArray = { "TZ_SCHOOL_TYPEID", "TZ_SCHOOL_TYPENAME", "TZ_SCHOOL_DEC", "TZ_SCHOOL_FLAG" ,"TZ_SCHOOL_ADDTIME",};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		
		
		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();

				mapList.put("typeID", rowList[0]);
				mapList.put("typeName", rowList[1]);
				mapList.put("typedec", rowList[2]);				
				mapList.put("typeFlag", rowList[3]);
				mapList.put("typeCreatime", rowList[4]);

				
				
				
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 删除院校类型 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 主题 ID;
				String typeID = jacksonUtil.getString("typeID");
				System.out.println(typeID);
				if (typeID != null && !"".equals(typeID)) {
					PsTzShoolLogTMapper.deleteByPrimaryKey(typeID);
					

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
