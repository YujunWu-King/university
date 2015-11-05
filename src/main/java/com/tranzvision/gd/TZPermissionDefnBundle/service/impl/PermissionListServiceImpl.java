package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsClassDefnMapper;
import com.tranzvision.gd.util.base.PaseJsonUtil;

import net.sf.json.JSONObject;

/*
 * 许可权定义， 原PS类：TZ_GD_PLST_PKG:TZ_GD_PERMLIST_CLS
 * @author tang
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionListServiceImpl")
public class PermissionListServiceImpl extends FrameworkImpl {
	
	@Autowired
	private PsClassDefnMapper psClassDefnMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	
	/* 查询许可权列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "CLASSID", "ASC" } };
			fliterForm.orderByArr = orderByArr;

			// json数据要的结果字段;
			String[] resultFldArray = { "CLASSID", "CLASSDEFNDESC" };
			String jsonString = "";

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

			if (obj == null || obj.length == 0) {
				strRet = "{\"total\":0,\"root\":[]}";
			} else {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					jsonString = jsonString + ",{\"permID\":\"" + rowList[0] + "\",\"permDesc\":\"" + rowList[1]
							+ "\"}";
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

	/* 删除许可权信息 */
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
				// 提交信息
				String strForm = actData[num];

				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				
				// 许可权ID;
				String sPermID = CLASSJson.getString("permID");
				if (sPermID != null && !"".equals(sPermID)) {
					/*删除该许可权ID下的所有信息*/
					psClassDefnMapper.deleteByPrimaryKey(sPermID);
					//删除组件权限;
					String sql = "DELETE FROM PS_TZ_AQ_COMSQ_TBL WHERE CLASSID=?";
					try{
						jdbcTemplate.update(sql,sPermID);
					}catch(DataAccessException e){
						
					}
					//删除role下的权限;
					try{
						sql = "DELETE FROM PSROLECLASS WHERE CLASSID=?";
						jdbcTemplate.update(sql,sPermID);
					}catch(DataAccessException e){
						
					}
					
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
