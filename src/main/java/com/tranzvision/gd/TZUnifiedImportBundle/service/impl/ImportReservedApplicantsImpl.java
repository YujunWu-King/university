package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportReservedApplicantsImpl")
public class ImportReservedApplicantsImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
			//查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM PS_TZ_QYNDBBL_KS_T WHERE TZ_PRJ_ID=? AND TZ_KSSSYEAR=? AND TZ_MSH_ID=?";

			//插入SQL
			String insertSql = "INSERT INTO TZ_IMP_MSPS_TBL(TZ_PRJ_ID,TZ_KSSSYEAR,TZ_MSH_ID) VALUES(?,?,?)";
			
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			//开始保存数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					
					//TZ_PRJ_ID实际提交的是项目名称
					String TZ_PRJ_NAME = ((String)data.get(i).get("TZ_PRJ_ID"));
					String TZ_KSSSYEAR = ((String)data.get(i).get("TZ_KSSSYEAR"));
					String TZ_MSH_ID = ((String)data.get(i).get("TZ_MSH_ID"));
					
					if(TZ_PRJ_NAME!=null&&!"".equals(TZ_PRJ_NAME)){
						//查询项目编号
						String TZ_PRJ_ID = sqlQuery.queryForObject("SELECT TZ_PRJ_ID FROM PS_TZ_PRJ_INF_T WHERE TZ_JG_ID=? AND TZ_PRJ_NAME=? LIMIT 0,1", 
								new Object[]{orgId,TZ_PRJ_NAME}, "String");
						
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{TZ_PRJ_ID,TZ_KSSSYEAR,TZ_MSH_ID}, "String");
						
						if(dataExist==null){
							//新增模式
							sqlQuery.update(insertSql, new Object[]{TZ_PRJ_NAME,TZ_KSSSYEAR,TZ_MSH_ID});
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
		try {

			ArrayList<String> resultMsg = new ArrayList<String>();
			
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			//开始校验数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){

					//TZ_PRJ_ID实际提交的是项目名称
					String TZ_PRJ_NAME = ((String)data.get(i).get("TZ_PRJ_ID"));
					String TZ_KSSSYEAR = ((String)data.get(i).get("TZ_KSSSYEAR"));
					String TZ_MSH_ID = ((String)data.get(i).get("TZ_MSH_ID"));
					
					if(TZ_PRJ_NAME==null||"".equals(TZ_PRJ_NAME)
							||TZ_KSSSYEAR==null||"".equals(TZ_KSSSYEAR)
							||TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
						result[0] = false;
						resultMsg.add("第["+(i+1)+"]行项目名称、所属年份、面试申请号都不能为空");
					}else{
						//查询项目编号
						String TZ_PRJ_ID = sqlQuery.queryForObject("SELECT TZ_PRJ_ID FROM PS_TZ_PRJ_INF_T WHERE TZ_JG_ID=? AND TZ_PRJ_NAME=? LIMIT 0,1", 
								new Object[]{orgId,TZ_PRJ_NAME}, "String");
						
						if(TZ_PRJ_ID==null||"".equals(TZ_PRJ_ID)){
							result[0] = false;
							resultMsg.add("第["+(i+1)+"]行找不到该项目");
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
