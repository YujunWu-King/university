package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportPermission2ContinueImpl")
public class ImportPermission2ContinueImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;

	/**
	 * 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectOprId = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MSH_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE PS_TZ_REG_USER_T SET TZ_ALLOW_APPLY=? WHERE OPRID=?";
			
			//开始保存数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					
					//OPRID实际传过来的是面试申请号,不另外建表存储导入数据
					String strInterviewAppId = ((String)data.get(i).get("OPRID"));
					String strPerm2Ctn = ((String)data.get(i).get("TZ_ALLOW_APPLY"));
					
					if(strInterviewAppId!=null&&!"".equals(strInterviewAppId)){
						
						//查询面试申请号对应的OPRID
						String oprId = sqlQuery.queryForObject(sqlSelectOprId,new Object[]{strInterviewAppId}, "String");
						
						if(oprId!=null&&!"".equals(oprId)){
							//更新面试评审考生表
							if(strPerm2Ctn!=null&&"是".equals(strPerm2Ctn)){
								strPerm2Ctn = "Y";
							}else{
								strPerm2Ctn = "N";
							}
							
							//更新模式
							int rst = sqlQuery.update(updateSql, new Object[]{strPerm2Ctn,oprId});
							if(rst>0){
								result[1] = ++result[1];
							}
						}else{
							continue;
						}
						
					}else{
						continue;
					}					
					
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
		// TODO Auto-generated method stub
	}
	
}
