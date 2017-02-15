package com.tranzvision.gd.TZClmsCsBiaoqzBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.dao.PsTzBiaoqzTMapper;
//import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 功能说明：标签组管理相关类
 * @author zxw
 * 2016-2-9
 */
@Service("com.tranzvision.gd.TZClmsCsBiaoqzBundle.service.impl.BqzManageImpl")
public class BqzManageImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzBiaoqzTMapper PsTzBiaoqzTMapper;
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询标签组列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_BIAOQZ_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_JG_ID", "TZ_BIAOQZ_ID", "TZ_BIAOQZ_NAME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgId", rowList[0]);
					mapList.put("bqzID", rowList[1]);
					mapList.put("bqzName", rowList[2]);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
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
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;
				String sbqzID = jacksonUtil.getString("bqzID");
				//PsTzBiaoqzTMapper.deleteByPrimaryKey(sbqzID);
				// int updateByPrimaryKey(PsTzBiaoqzT record);
				String bqzPageSql = "DELETE FROM PS_TZ_BIAOQZ_T WHERE TZ_BIAOQZ_ID=?";
				jdbcTemplate.update(bqzPageSql,new Object[]{sbqzID});		
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}