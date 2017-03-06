package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

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

	/**
	 * 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM TZ_IMP_MSPS_TBL WHERE TZ_APP_INS_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE TZ_IMP_MSPS_TBL SET TZ_TIME=?,TZ_ADDRESS=?,TZ_RESULT=?,TZ_RESULT_CODE=? WHERE TZ_APP_INS_ID=?";
			String updateRelatedSql = "UPDATE PS_TZ_MSPS_KSH_TBL SET TZ_LUQU_ZT=? WHERE TZ_APP_INS_ID=?";
			
			//插入SQL
			String insertSql = "INSERT INTO TZ_IMP_MSPS_TBL(TZ_APP_INS_ID,TZ_TIME,TZ_ADDRESS,TZ_RESULT,TZ_RESULT_CODE) VALUES(?,?,?,?,?)";
			
			//开始保存数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					
					String strAppInsId = ((String)data.get(i).get("TZ_APP_INS_ID"));
					String strTime = ((String)data.get(i).get("TZ_TIME"));
					String strAddress = ((String)data.get(i).get("TZ_ADDRESS"));
					String strResult = (String)data.get(i).get("TZ_RESULT");
					String strResultCode = (String)data.get(i).get("TZ_RESULT_CODE");
					
					if(strAppInsId!=null&&!"".equals(strAppInsId)){
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{strAppInsId}, "String");
						
						if(dataExist!=null){
							//更新模式
							sqlQuery.update(updateSql, new Object[]{strTime,strAddress,strResult,strResultCode,strAppInsId});
						}else{
							//新增模式
							sqlQuery.update(insertSql, new Object[]{strAppInsId,strTime,strAddress,strResult,strResultCode});
						}
						
						//更新面试评审考生表
						String interviewEvaResult = "";
						if(strResultCode!=null){
							if("录取".equals(strResultCode)){
								interviewEvaResult = "LQ";
							}
							if("递补".equals(strResultCode)){
								interviewEvaResult = "DB";
							}
							if("待定".equals(strResultCode)){
								interviewEvaResult = "DD";
							}
							if("拒绝".equals(strResultCode)){
								interviewEvaResult = "JJ";
							}
						}
						
						if(!interviewEvaResult.equals("")){
							sqlQuery.update(updateRelatedSql, new Object[]{interviewEvaResult,strAppInsId});
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
	
}
