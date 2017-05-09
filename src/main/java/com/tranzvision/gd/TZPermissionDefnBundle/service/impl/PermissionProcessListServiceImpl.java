package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsClassDefnMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 许可权定义进程类
 * @author wangdi
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionProcessListServiceImpl")
public class PermissionProcessListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;


	/* 根据许可权编号批量查找进程定义 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String strComID = jacksonUtil.getString("permID");
			System.out.println("=====permID=====" + strComID);
			if (strComID != null && !"".equals(strComID)) {

				// 进程名称，进程描述
				String processName = "", processDesc = "";
				// 序号;
				int numOrder = 0;
				// 页面注册信息列表sql;
				String sqlPageList = "";
				String totalSQL = "";
				//查询组件下的页面列表
				Object[] obj = null;
				if (numLimit == 0) {
					sqlPageList = "SELECT TZ_JC_MC,TZ_JC_MS FROM TZ_JINC_DY_T WHERE TZ_ZCZJ_ID = ?";
					obj = new Object[] { strComID };
				} else {
					sqlPageList = "SELECT TZ_JC_MC,TZ_JC_MS FROM TZ_JINC_DY_T WHERE TZ_ZCZJ_ID = ? limit ?,?";
					obj = new Object[] { strComID, numStart, numLimit };
				}

				int total = 0;
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlPageList, obj);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						processName = (String) list.get(i).get("TZ_JC_MC");
						processDesc = (String) list.get(i).get("TZ_JC_MS");

						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("comID", strComID);
						mapList.put("processName", processName);
						mapList.put("processDesc", processDesc);
						listData.add(mapList);
					}

					totalSQL = "SELECT COUNT(1) FROM TZ_JINC_DY_T WHERE TZ_ZCZJ_ID=?";
					total = jdbcTemplate.queryForObject(totalSQL,new Object[] { strComID },"Integer");
					mapRet.replace("total", total);
					mapRet.replace("root", listData);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件页面信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}
}
