package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportStuAdmissionsProcessImpl")
public class ImportStuAdmissionsProcessImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;

	/**
	 * 保存考生录取流程导入的数据
	 * zhanglang
	 * 2017-03-31
	 */
	@Override
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
			//查询OPRID SQL
			String sqlSelectOprId = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MSH_ID=?";
			//查询SQL
			String opridExistsSql = "SELECT 'Y' FROM " + targetTbl + " WHERE OPRID=?";
			//查询转换值SQL
			String transValSql = "select TZ_ZHZ_ID from PS_TZ_PT_ZHZXX_TBL where TZ_ZHZJH_ID=? and TZ_ZHZ_DMS=? and TZ_EFF_STATUS='A'";
			
			//校验数据正确性
			String validCode = "0";
			String validMsg = "";
			if(data != null && data.size()>0){
				int i = 0;
				int count = 0;//用于统计错误条数，最多显示N条
				for(i=0; i<data.size(); i++ ){
					//OPRID实际传过来的是面试申请号
					String strInterviewAppId = ((String)data.get(i).get("OPRID"));
					if(strInterviewAppId!=null&&!"".equals(strInterviewAppId)){
						//查询面试申请号对应的OPRID
						String oprId = sqlQuery.queryForObject(sqlSelectOprId,new Object[]{strInterviewAppId}, "String");
						if(oprId==null || "".equals(oprId)){
							validCode = "1";
							validMsg = validMsg + "第"+ (i+1) +"行，系统中没有找到面试申请号为"+ strInterviewAppId +"的考生;";
							count ++;
						}
					}else{
						validCode = "1";
						validMsg = validMsg + "第"+ (i+1) +"行，面试申请编号为空;";
						count ++;
					}
					
					if(count >= 10){
						validMsg = validMsg + "......";
						break;
					}
				}
			}else{
				validCode = "1";
				validMsg = validMsg + "导入数据条数为0;";
			}
			
			
			if (!"0".equals(validCode)){
				errMsg[0] = validCode;
				errMsg[1] = validMsg + ",数据导入失败，请重新导入。";
			}else{
				for(Map<String,Object> dataMap : data){
					//更新SQL
					String updateSql = "update "+targetTbl +" set ";
					String updateSet = "";
					
					//插入SQL
					String insertSql = "insert into " +targetTbl;
					String insertCol = "";
					String insertVal = "";

					//OPRID实际传过来的是面试申请号
					String strInterviewAppId = ((String)dataMap.get("OPRID"));
					
					if(strInterviewAppId!=null && !"".equals(strInterviewAppId)){
						//查询面试申请号对应的OPRID
						String oprId = sqlQuery.queryForObject(sqlSelectOprId,new Object[]{strInterviewAppId}, "String");
						if(oprId!=null&&!"".equals(oprId)){
							insertCol = "OPRID";
							insertVal = "'"+ oprId +"'";
							
							for(String key : dataMap.keySet()){
								if(!"OPRID".equals(key) 
										&& dataMap.get(key) != null 
										&& !"".equals(dataMap.get(key).toString())){
									String val = dataMap.get(key).toString(); 
									
									//转换值定义
									String transValDefn = ""; 
									switch(key){
										case "TZ_TJLQZG": //条件录取资格（面试结果）
											transValDefn = "TZ_TJLQZG";
											break;
										case "TZ_LKQ_TJLQZG": //联考前条件录取资格
											transValDefn = "TZ_LKQ_TJLQZG";
											break;
										case "TZ_YLQ_ZG":	//预录取资格
											transValDefn = "TZ_YLQ_ZG";
											break;
										case "TZ_ZSLQ_ZG":	//正式录取资格
											transValDefn = "TZ_ZSLQ_ZG";
											break;
										case "TZ_TJLQZG_XM":	//条件录取资格项目（面试结果）
											transValDefn = "TZ_LQZG_XM";
											break;
										case "TZ_LKQ_TJLQZG_XM":	//联考前条件录取资格项目
											transValDefn = "TZ_LQZG_XM";
											break;
										case "TZ_YLQ_ZG_XM":	//预录取资格项目
											transValDefn = "TZ_LQZG_XM";
											break;
										case "TZ_ZSLQ_ZG_XM":	//正式录取资格项目
											transValDefn = "TZ_LQZG_XM";
											break;
										case "TZ_MSJG_PC":	//获取面试结果批次
											transValDefn = "TZ_MSPS_PC";
											break;
										case "TZ_MSPS_PC":	//面试批次
											transValDefn = "TZ_MSPS_PC";
											break;
										case "TZ_RX_QK":	//入学情况
											transValDefn = "TZ_RX_QK";
											break;
										case "TZ_RX_XM":	//入学项目
											transValDefn = "TZ_LQZG_XM";
											break;
									}
									
									if(!"".equals(transValDefn)){
										/*取转换值*/
										val = sqlQuery.queryForObject(transValSql, new Object[]{ transValDefn, val }, "String");
										if(val == null || "".equals(val)){
											continue;
										}
									}
									
									
									if("".equals(updateSet)){
										updateSet = key + "='" + val + "'";
									}else{
										updateSet = updateSet + "," + key + "='" + val + "'";
									}
									
									if("".equals(insertCol)){
										insertCol = key;
										insertVal = "'" + val + "'"; 
									}else{
										insertCol = insertCol + "," + key;
										insertVal = insertVal + ",'" + val + "'"; 
									}
									
								}
							}

							int rtn = 0;
							String exists = sqlQuery.queryForObject(opridExistsSql, new Object[]{ oprId }, "String");
							if("Y".equals(exists)){
								if(!"".equals(updateSet)){
									updateSql = updateSql + updateSet + " where OPRID=?";
									rtn = sqlQuery.update(updateSql, new Object[]{ oprId });
								}
							}else{
								insertSql = insertSql + "(" + insertCol + ") values(" + insertVal + ")";
								rtn = sqlQuery.update(insertSql);
							}
							
							if(rtn>0){
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
	
}
