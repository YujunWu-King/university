package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportPreAdmissionCopyImpl")
public class ImportPreAdmissionCopyImpl implements UnifiedImportBase {
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private UnifiedImportImpl unifiedImportImpl;

	@Override
	public void tzValidate(List<Map<String, Object>> data, List<String> fields, String targetTbl, Object[] result,
			String[] errMsg) {
		try {

			ArrayList<String> resultMsg = new ArrayList<String>();

			// 开始校验数据
			if (data != null && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					String TZ_APP_INS_ID = ((String) data.get(i).get("TZ_APP_INS_ID"));

					if (TZ_APP_INS_ID == null || "".equals(TZ_APP_INS_ID)) {
						result[0] = false;
						resultMsg.add("第[" + (i + 1) + "]行报名表编号不能为空");
					} else {
						// 检查报名表编号是否存在
						String TZ_APP_INS_ID_EXIST = sqlQuery.queryForObject(
								"SELECT 'Y' FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?", new Object[] { TZ_APP_INS_ID },
								"String");
						if (!"Y".equals(TZ_APP_INS_ID_EXIST)) {
							result[0] = false;
							resultMsg.add("第[" + (i + 1) + "]行报名表编号不存在");
						}
					}
				}
			} else {
				resultMsg.add("您没有导入任何数据！");
			}

			result[1] = String.join("，", (String[]) resultMsg.toArray(new String[resultMsg.size()]));

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
	}
		
	

	@Override
	public String tzSave(List<Map<String, Object>> data, List<String> fields, String targetTbl, int[] result,
			String[] errMsg) {
		System.out.println("data==============="+data.size());

		return unifiedImportImpl.tzSave(data, fields, targetTbl, result, errMsg);
	}

}
