package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportInterviewEvaRstImpl")
public class ImportInterviewEvaRstImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private UnifiedImportImpl unifiedImportImpl;

	/**
	 * 面试结果 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data, List<String> fields, String targetTbl, int[] result,
			String[] errMsg) {
		return unifiedImportImpl.tzSave(data, fields, targetTbl, result, errMsg);
		/*String strRet = "";
		try {

			// 查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM TZ_IMP_MSJG_TBL WHERE TZ_APP_INS_ID=?";

			// 更新SQL
			String updateSql = "UPDATE TZ_IMP_MSJG_TBL SET TZ_RESULT_CODE=? WHERE TZ_APP_INS_ID=?";
			// String updateRelatedSql = "UPDATE PS_TZ_MSPS_KSH_TBL SET
			// TZ_LUQU_ZT=? WHERE TZ_APP_INS_ID=?";

			// 插入SQL
			String insertSql = "INSERT INTO TZ_IMP_MSJG_TBL(TZ_APP_INS_ID,TZ_RESULT_CODE) VALUES(?,?)";

			// 开始保存数据
			if (data != null && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {

					String strAppInsId = ((String) data.get(i).get("TZ_APP_INS_ID"));
					String TZ_RESULT_CODE = ((String) data.get(i).get("TZ_RESULT_CODE"));

					if (strAppInsId != null && !"".equals(strAppInsId)) {
						// 查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey, new Object[] { strAppInsId },
								"String");

						if (dataExist != null) {
							// 更新模式
							sqlQuery.update(updateSql, new Object[] { TZ_RESULT_CODE, strAppInsId });
						} else {
							// 新增模式
							sqlQuery.update(insertSql, new Object[] { strAppInsId, TZ_RESULT_CODE });
						}

						// 更新面试评审考生表
						
						  String interviewEvaResult = "";
						  if(strResultCode!=null){
						  if("录取".equals(strResultCode)){ interviewEvaResult =
						  "LQ"; } if("递补".equals(strResultCode)){
						  interviewEvaResult = "DB"; }
						  if("待定".equals(strResultCode)){ interviewEvaResult =
						  "DD"; } if("拒绝".equals(strResultCode)){
						  interviewEvaResult = "JJ"; } }
						  
						  if(!interviewEvaResult.equals("")){
						  sqlQuery.update(updateRelatedSql, new
						  Object[]{interviewEvaResult,strAppInsId}); }
						 

					} else {
						continue;
					}

					result[1] = ++result[1];
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet; */
	}

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

}
