package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportMaterialEvaRstImpl")
public class ImportMaterialEvaRstImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;

	/**
	 * 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE TZ_IMP_CLPS_TBL SET TZ_RESULT=?,TZ_RESULT_CODE=? WHERE TZ_APP_INS_ID=?";
			String updateRelatedSql = "UPDATE PS_TZ_CLPS_KSH_TBL SET TZ_MSHI_ZGFLG=? WHERE TZ_APP_INS_ID=?";
			
			//插入SQL
			String insertSql = "INSERT INTO TZ_IMP_CLPS_TBL(TZ_APP_INS_ID,TZ_RESULT,TZ_RESULT_CODE) VALUES(?,?,?)";
			
			//开始保存数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					
					String strAppInsId = ((String)data.get(i).get("TZ_APP_INS_ID"));
					String strResult = (String)data.get(i).get("TZ_RESULT");
					String strResultCode = (String)data.get(i).get("TZ_RESULT_CODE");
					
					if(strAppInsId!=null&&!"".equals(strAppInsId)){
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{strAppInsId}, "String");
						
						if(dataExist!=null){
							//更新模式
							sqlQuery.update(updateSql, new Object[]{strResult,strResultCode,strAppInsId});
						}else{
							//新增模式
							sqlQuery.update(insertSql, new Object[]{strAppInsId,strResult,strResultCode});
						}
						
						//更新材料评审考生表
						String interviewEvaQualification = "";
						if(strResultCode!=null){
							if("有".equals(strResultCode)){
								interviewEvaQualification = "Y";
							}
							if("无".equals(strResultCode)){
								interviewEvaQualification = "N";
							}
						}
						
						if(!interviewEvaQualification.equals("")){
							sqlQuery.update(updateRelatedSql, new Object[]{interviewEvaQualification,strAppInsId});
						}
						
					}else{
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
		return strRet;
	}

	@Override
	public void tzValidate(List<Map<String, Object>> data, List<String> fields, String targetTbl, Object[] result,
			String[] errMsg) {
	}
	
}
