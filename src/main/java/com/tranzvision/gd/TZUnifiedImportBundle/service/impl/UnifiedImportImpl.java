package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZImportTplMgBundle.service.impl.ImportTplInfoImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 统一Excel导入数据解析和导入处理程序
 * 
 * @author 叶少威
 * @since 2017-01-21
 */
@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.UnifiedImportImpl")
public class UnifiedImportImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private ImportTplInfoImpl importTplInfoImpl;
	@Autowired
	private ApplicationContext ctx;

	
	/*获取导入模板信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		return importTplInfoImpl.tzQuery(strParams, errMsg);
	}
	
	/*加载导入模板字段列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		return importTplInfoImpl.tzQueryList(comParams, numLimit, numStart, errorMsg);
	}
	
	/**
	 * 处理导入的数据入口
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			
			String tplId = jacksonUtil.getString("tplId");
			
			//查询模板信息
			String sqlTpl = "SELECT TZ_TARGET_TBL,TZ_JAVA_CLASS FROM TZ_IMP_TPL_DFN_T WHERE TZ_TPL_ID=?"; 
			Map<String, Object> tplInfo = sqlQuery.queryForMap(sqlTpl, new Object[]{tplId});
			String targetTbl = "",javaClass="";
			if(tplInfo!=null){
				targetTbl = (String)tplInfo.get("TZ_TARGET_TBL");
				javaClass = (String)tplInfo.get("TZ_JAVA_CLASS");
			}
			
			//导入字段
			List<String> fieldList = (List<String>) jacksonUtil.getList("fields");
			
			//导入数据;
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) jacksonUtil.getList("data");
			
			//导入结果
			int[] result=new int[]{dataList.size(),0};
			
			if(javaClass!=null&&!"".equals(javaClass)){
				UnifiedImportBase obj = (UnifiedImportBase) ctx.getBean(javaClass);
				strRet = obj.tzSave(dataList,fieldList, targetTbl ,result, errMsg);
				this.tzWritingLog(tplId,result);
			}else{
				strRet = this.tzSave(dataList,fieldList, targetTbl ,result, errMsg);
				this.tzWritingLog(tplId,result);
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}
		return strRet;
	}

	/**
	 * 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
		
			//查询主键
			String sqlKeys = "SELECT COLUMN_NAME FROM TZ_SCHEMA_COLUMNS_VW WHERE TABLE_NAME=? AND COLUMN_KEY='PRI'"; 
			List<Map<String, Object>> keys = sqlQuery.queryForList(sqlKeys, new Object[]{targetTbl});
			
			//查询和更新WHERE子句
			String whereClause = " WHERE ";
			if (keys != null && keys.size()>0){
				for(int i=0;i<keys.size();i++){
					if(whereClause.equals(" WHERE ")){
						whereClause = whereClause+keys.get(i).get("COLUMN_NAME")+"=?";
					}else{
						whereClause = whereClause+" AND "+keys.get(i).get("COLUMN_NAME")+"=?";
					}
					
				}
			}
			
			//更新Set子句、插入Fields子句、插入Values子句
			StringBuffer setClause = null;
			StringBuffer fieldsClause = null;
			StringBuffer valuesClause = null;
			
			for(int i=0;i<fields.size();i++){
				if(setClause==null){
					setClause = (new StringBuffer(fields.get(i))).append("=?");
				}else{
					setClause.append(",").append(fields.get(i)).append("=?");
				}
				
				if(fieldsClause==null){
					fieldsClause = new StringBuffer(fields.get(i));
				}else{
					fieldsClause.append(",").append(fields.get(i));
				}
				
				if(valuesClause==null){
					valuesClause = new StringBuffer("?");
				}else{
					valuesClause.append(",").append("?");
				}
			}
			
			//查询SQL
			StringBuffer sqlSelectByKey = new StringBuffer("SELECT 'Y' FROM ").append(targetTbl).append(" ")
					.append(whereClause);
			
			//更新SQL
			StringBuffer updateSql = new StringBuffer("UPDATE ").append(targetTbl).append(" SET ")
					.append(setClause).append(" ").append(whereClause);
			
			//插入SQL
			StringBuffer insertSql = new StringBuffer("INSERT INTO ").append(targetTbl).append("(")
					.append(fieldsClause).append(") VALUES(").append(valuesClause).append(")");
			
			//开始保存数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					//检查数据中主键是否存在
					Boolean keyValueExist = true;
					Object[] keySearchArgs = new Object[keys.size()];
					
					for(int j=0;j<keys.size();j++){
						String tmpKeyValue = (String)data.get(i).get(keys.get(j).get("COLUMN_NAME"));
						if(tmpKeyValue!=null&&!"".equals(tmpKeyValue)){
							keySearchArgs[j]=tmpKeyValue;
						}else{
							keyValueExist = false;
							break;
						}
					}
					
					if(!keyValueExist){
						continue;
					}
					
					//查询数据是否存在
					String dataExist = sqlQuery.queryForObject(sqlSelectByKey.toString(), keySearchArgs, "String");

					//新增和更新参数
					Object[] insertArgs = new Object[fields.size()];
					for(int j=0;j<fields.size();j++){
						
						insertArgs[j] = (String)data.get(i).get(fields.get(j));
					}
					Object[] updateArgs = ArrayUtils.addAll(insertArgs, keySearchArgs);
					
					if(dataExist!=null){
						//更新模式
						sqlQuery.update(updateSql.toString(), updateArgs);
					}else{
						//新增模式
						sqlQuery.update(insertSql.toString(), insertArgs);
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
	
	public void tzWritingLog(String tplId,int[] result){		
		String sql = "INSERT INTO TZ_IMP_LOG_TBL(TZ_TPL_ID,OPRID,DATETIME,TZ_TOTAL,TZ_SUCCESS,TZ_FAILURE) VALUES(?,?,?,?,?,?)";
		sqlQuery.update(sql,new Object[]{tplId,tzLoginServiceImpl.getLoginedManagerOprid(request),new Date(),result[0],result[1],result[0]-result[1]});
	}
}
