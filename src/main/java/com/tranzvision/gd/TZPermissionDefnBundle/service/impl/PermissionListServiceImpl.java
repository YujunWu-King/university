package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsClassDefnMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 许可权定义， 原PS类：TZ_GD_PLST_PKG:TZ_GD_PERMLIST_CLS
 * @author tang
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionListServiceImpl")
public class PermissionListServiceImpl extends FrameworkImpl {
	
	@Autowired
	private PsClassDefnMapper psClassDefnMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询许可权列表 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "CLASSID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "CLASSID", "CLASSDEFNDESC" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			
			if (obj != null && obj.length > 0) {
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("permID", rowList[0]);
					mapList.put("permDesc", rowList[1]);
					
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

	/* 删除许可权信息 */
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
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 许可权ID;
				String sPermID = jacksonUtil.getString("permID");
				if (sPermID != null && !"".equals(sPermID)) {
					/*删除该许可权ID下的所有信息*/
					psClassDefnMapper.deleteByPrimaryKey(sPermID);
					//删除组件权限;
					String sql = "DELETE FROM PS_TZ_AQ_COMSQ_TBL WHERE CLASSID=?";
					jdbcTemplate.update(sql,new Object[]{sPermID});
					
					//删除role下的权限;
					sql = "DELETE FROM PSROLECLASS WHERE CLASSID=?";
					jdbcTemplate.update(sql,new Object[]{sPermID});
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
