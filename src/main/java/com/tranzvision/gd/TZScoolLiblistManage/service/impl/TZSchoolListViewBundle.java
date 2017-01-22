package com.tranzvision.gd.TZScoolLiblistManage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZScoolLiblistManage.dao.PsTzPayLogTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZScoolLiblistManage.service.impl.TZSchoolListViewBundle")
public class TZSchoolListViewBundle  extends FrameworkImpl {
	@Autowired
	private  SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private  PsTzPayLogTMapper PsTzPayLogTMapper;
	@Autowired
	private FliterForm fliterForm;

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
		String[][] orderByArr = new String[][] { { "TZ_JG_ID", "ASC" } };

		// json数据要的结果字段;
		
		String[] resultFldArray = { "TZ_JG_ID", "TZ_SCHOOL_NAME", "TZ_SCHOOL_NAMEENG", "COUNTRY" ,"TZ_ZGBM","CITY","tz_hemisphere","tz_state","decstring","tz_school_type","tz_school_natrue","TZ_SCHOOL_LEVEL",};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		
		
		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("orgaID", rowList[0]);
				mapList.put("chinaName", rowList[1]);
				mapList.put("engName", rowList[2]);
				
				mapList.put("country", rowList[3]);
				mapList.put("mainDeart", rowList[4]);
				mapList.put("city", rowList[5]);
				mapList.put("hemiHere", rowList[6]);
				mapList.put("state", rowList[7]);
				mapList.put("adddec", rowList[8]);
				
				mapList.put("type", rowList[9]);
				mapList.put("attriBute", rowList[10]);
				mapList.put("level", rowList[11]);

				
				
				
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 删除院校库 */
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
				String orgaID = jacksonUtil.getString("orgaID");
				System.out.println(orgaID);
				if (orgaID != null && !"".equals(orgaID)) {
					PsTzPayLogTMapper.deleteByPrimaryKey(orgaID);
					

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




	
	
	


