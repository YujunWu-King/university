package com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
/*
 * ldd 测试问卷管理列表
 */
@Service("com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzCsWjglListServiceImpl")
public class TzCsWjglListServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery sqlQuery;
	
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
			//String[][] orderByArr = new String[][]{{"TZ_DLZH_ID","ASC"}};
			String[][] orderByArr=new String[][]{};
			//json数据要的结果字段;
			String[] resultFldArray = { "TZ_CS_WJ_ID","TZ_CS_WJ_NAME", "TZ_DC_WJ_KSRQ", "TZ_DC_WJ_JSRQ", "TZ_STATE","TZ_DC_WJ_ID"};
					
			//可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit,numStart, errorMsg);
			
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("TZ_CS_WJ_ID", rowList[0]);
					mapList.put("TZ_STATE", rowList[4]);
					mapList.put("TZ_DC_WJ_ID", rowList[5]);
					if(!"".equals(rowList[5])){
						Map<String,Object> map=sqlQuery.queryForMap("select TZ_DC_WJBT,TZ_DC_WJ_KSRQ,TZ_DC_WJ_KSSJ from PS_TZ_DC_WJ_DY_T where TZ_DC_WJ_ID=?",new Object[]{rowList[5]});
						mapList.put("TZ_CS_WJ_NAME", String.valueOf(map.get("TZ_DC_WJBT")));
						mapList.put("TZ_DC_WJ_KSRQ", String.valueOf(map.get("TZ_DC_WJ_KSRQ")));
						mapList.put("TZ_DC_WJ_JSRQ", String.valueOf(map.get("TZ_DC_WJ_KSSJ")));
					}else{
						mapList.put("TZ_CS_WJ_NAME", rowList[1]);
						mapList.put("TZ_DC_WJ_KSRQ", rowList[2]);
						mapList.put("TZ_DC_WJ_JSRQ", rowList[3]);
					}
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
