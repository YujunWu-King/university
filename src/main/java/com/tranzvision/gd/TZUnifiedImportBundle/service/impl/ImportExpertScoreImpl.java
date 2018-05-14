package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportExpertScoreImpl")
public class ImportExpertScoreImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;

	/**
	 * 保存导入的数据（专家打分使用面试成绩项ID）
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectOprId = "SELECT DISTINCT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MSH_ID=?";
			//查询班级ID、批次ID、实例ID
			//String sql = "SELECT DISTINCT TZ_CLASS_ID,TZ_BATCH_ID,TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=?";
			//查询成绩项ID
			//String sqlScoreId = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
			String sqlScoreId = "SELECT TZ_SCOREMS_INS_ID FROM PS_TZ_CS_STU_VW WHERE OPRID=?";
			
			//更新SQL
			String updateSql = "UPDATE PS_TZ_CJX_TBL SET TZ_SCORE_NUM=?,TZ_SCORE_DFGC=? WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
			
			//开始保存数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					
					String mshId = ((String)data.get(i).get("TZ_MSH_ID"));
					String score = ((String)data.get(i).get("TZ_MOBILE"));
					
					if(mshId!=null&&!"".equals(mshId)){
						
						//查询面试申请号对应的OPRID
						String oprId = sqlQuery.queryForObject(sqlSelectOprId,new Object[]{mshId}, "String");
						
						if(oprId!=null&&!"".equals(oprId)){
							//查询班级ID、批次ID、实例ID
							//Map<String, Object> mapData = new HashMap<String, Object>();
							//mapData=sqlQuery.queryForMap(sql, new Object[]{ oprId });
							//String classId =mapData.get("TZ_CLASS_ID")==null?"":mapData.get("TZ_CLASS_ID").toString();
							//String batchId =mapData.get("TZ_BATCH_ID")==null?"":mapData.get("TZ_BATCH_ID").toString();
							//String appId=mapData.get("TZ_APP_INS_ID")==null?"":mapData.get("TZ_APP_INS_ID").toString();
						
							//查询成绩项ID
							String scoreId = sqlQuery.queryForObject(sqlScoreId,new Object[]{oprId}, "String");
							if(scoreId!=null&&!"".equals(scoreId)){
								String isExist = "SELECT COUNT(1) FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID='ZYXX'";
								int count = sqlQuery.queryForObject(isExist, new Object[] { scoreId }, "Integer");
								String scoreDfgc = "职业形象及配合度|"+score+"分";
								if (count > 0) {
									sqlQuery.update(updateSql, new Object[]{score,scoreDfgc,scoreId,"ZYXX"});
									//String strUpdateSql = "UPDATE PS_TZ_CJX_TBL SET TZ_SCORE_NUM='" + score	+ "',TZ_SCORE_DFGC='"+scoreDfgc+"' WHERE TZ_SCORE_INS_ID='" + scoreId + "'AND TZ_SCORE_ITEM_ID='" + "ZYXX" + "'";
									//sqlQuery.update(strUpdateSql, new Object[] {});
								}else{
									String strInsertSql = "INSERT INTO PS_TZ_CJX_TBL(TZ_SCORE_INS_ID,TZ_SCORE_ITEM_ID,TZ_SCORE_NUM,TZ_SCORE_DFGC) VALUES(?,?,?,?)";
									sqlQuery.update(strInsertSql, new Object[] { scoreId, "ZYXX", score,scoreDfgc });
								}
								
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
		try {

			ArrayList<String> resultMsg = new ArrayList<String>();
			
			//开始校验数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					//OPRID实际传过来的是面试申请号,不另外建表存储导入数据
					//String TZ_MSH_ID = ((String)data.get(i).get("OPRID"));
					String TZ_MSH_ID = ((String)data.get(i).get("TZ_MSH_ID"));
					
					if(TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
						result[0] = false;
						resultMsg.add("第["+(i+1)+"]行面试申请号不能为空");
					}else{
						//检查面试申请号是否存在用户信息表中
						String TZ_MSH_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MSH_ID=?", 
								new Object[]{TZ_MSH_ID}, "String");
						if(!"Y".equals(TZ_MSH_ID_EXIST)){
							result[0] = false;
							resultMsg.add("第["+(i+1)+"]行面试申请号不存在");
						}
					}
				}
			}else{
				resultMsg.add("您没有导入任何数据！");
			}
			
			result[1] = String.join("，", (String[])resultMsg.toArray(new String[resultMsg.size()]));
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
	}
	
}
