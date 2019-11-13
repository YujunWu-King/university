package com.tranzvision.gd.util.base;

import com.tranzvision.gd.util.adfs.TZAdfsLoginObject;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 类方法定义解析
 * @author 张浪
 * 2019-02-27
 */
public class AnalysisClsMethod {

	private TZGDObject tzGDObject;
	private SqlQuery sqlQuery;
	
	private String m_clsDefID;
	
	
	
	/*
	 * JAVA类方法参数类型定义，需要与参数m_arglist类型保持
	 * eg: new String[] { "String", "String", "Object" };
	 */
	private String[] m_paramTypes = null;
	
	/*
	 * JAVA类方法参数
	 * eg: new Object[] { m_WflInstanceID, m_WflStpInsID, errorMsg }
	 */
	private Object[] m_arglist = null;
	
	

	//DLL应用程序类解析接口
	private static final String dllcls_webserv_url = "/ISV/tzsvc/services/AnalysisDLLAttachService.asmx/analysisDLLAppCls";
	
	
	
	/**
	 * 设置类方法参数
	 * @param paramTypes
	 * @param arglist
	 */
	public void setJavaClsParameter(String[] paramTypes, Object[] arglist) {
		this.m_paramTypes = paramTypes;
		this.m_arglist = arglist;
	}
	



	public AnalysisClsMethod(String clsDefID) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		m_clsDefID = clsDefID;
	}

	
	

	/**
	 * 检查参数类型，DLL类只能传入基本类型
	 * @return
	 */
	private boolean checkTypeValid(){
		boolean checkOK = true;
		
		for(String type : m_paramTypes){
			if("long".equals(type) 
					|| "Long".equals(type) 
					|| "int".equals(type) 
					|| "Integer".equals(type) 
					|| "float".equals(type) 
					|| "Float".equals(type) 
					|| "double".equals(type) 
					|| "Double".equals(type)
					|| "boolean".equals(type)
					|| "Boolean".equals(type)
					|| "String".equals(type)){
				//do nothing
			}else{
				checkOK = false;
				break;
			}
		}
		
		return checkOK;
	}
	
	
	


	/**
	 * 执行类方法
	 * @return
	 * @throws TzException
	 */
	public Object execute() throws TzException {
		Object retrunObj = null;
		
		Map<String,Object> appclsMap = sqlQuery.queryForMap("select tzms_appcls_type,tzms_appcls_path,tzms_appcls_method from tzms_appcls_t where tzms_appcls_tid=?", 
				new Object[]{ m_clsDefID });
		
		if(appclsMap != null){
			String appclsType = appclsMap.get("tzms_appcls_type") == null ? "" : appclsMap.get("tzms_appcls_type").toString();
			String appcls_path = appclsMap.get("tzms_appcls_path") == null ? "" : appclsMap.get("tzms_appcls_path").toString();
			String appcls_method = appclsMap.get("tzms_appcls_method") == null ? "" : appclsMap.get("tzms_appcls_method").toString();
			
			
			if(StringUtils.isNotEmpty(appclsType) 
					&& StringUtils.isNotEmpty(appcls_path) 
					&& StringUtils.isNotEmpty(appcls_method)){
				
				try{
					if(m_paramTypes == null || m_arglist == null){
						throw new TzException("类方法参数错误");
					}
					
					if("1".equals(appclsType)){	 //java类
						
						retrunObj = ObjectDoMethod.TzLoad(appcls_path, appcls_method, m_paramTypes, m_arglist);
						
					}else if("2".equals(appclsType)){	//DLL类
						
						//检查参数类型，只能为基本类型
						if(checkTypeValid() == false){
							throw new TzException("参数类型错误，DLL类方法只能传入基本类型");
						}
						
						JacksonUtil jacksonUtil = new JacksonUtil();
						TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
						
						String dynURL = "";
						String dynUserName = "";
						String dynUserPswd = "";
						String dynDomain = "";
						try{
							dynURL = tzGDObject.getHardCodeVal("TZ_DYNAMICS_LOGIN_URL");
							dynUserName = tzGDObject.getHardCodeVal("TZ_DYNAMICS_USER_NAME");
							dynUserPswd = tzGDObject.getHardCodeVal("TZ_DYNAMICS_USER_PSWD");
							dynDomain = tzGDObject.getHardCodeVal("TZ_DYNAMICS_DOMAIN");
						}catch (NullPointerException e) {
							e.printStackTrace();
							throw new TzException("Dynamics登录信息未配置");
						}
						
						tzADFSObject.setDynamicsLoginPrarameters(dynURL,dynUserName,dynUserPswd,-1);
						
						//请求URL
						String url = dynDomain + dllcls_webserv_url;
						
						Map<String,Object> parametersMap = new HashMap<String,Object>();
						for(int i=0; i<m_arglist.length; i++){
							parametersMap.put("key" + i, m_arglist[i]);
						}
						
						//POST数据
						Map<String,Object> dataMap = new HashMap<String,Object>();
						String postDataString = "";
						dataMap.put("appClsId", m_clsDefID);
						dataMap.put("parameters", jacksonUtil.Map2json(parametersMap));
						postDataString = jacksonUtil.Map2json(dataMap); 
						
						if(tzADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData(url, postDataString) == true){
							String result = tzADFSObject.getWebAPIResult();
							jacksonUtil.json2Map(result);
							
							if(jacksonUtil.containsKey("d")){
								jacksonUtil.json2Map(jacksonUtil.getString("d"));
								
								if(jacksonUtil.containsKey("code") && "0".equals(jacksonUtil.getString("code"))){
									//成功
									return jacksonUtil.getString("content");
								}else{
									String errorMessage = jacksonUtil.getString("msg");
									throw new TzException(errorMessage);
								}
							}
						}else{
							String errorMessage = tzADFSObject.getErrorMessage();
							throw new TzException(errorMessage);
						}
					}
				}catch (TzException e) {
					throw e;
				}catch (Exception e) {
					e.printStackTrace();
					throw new TzException("【系统错误】应用程序类执行错误，类定义编号：" + m_clsDefID + ",请联系系统管理员", e);
				}
			}else{
				throw new TzException("【系统配置错误】应用程序类定义错误，类定义编号：" + m_clsDefID);
			}
		}else{
			throw new TzException("【系统配置错误】应用程序类定义不存在，类定义编号：" + m_clsDefID);
		}
		
		return retrunObj;
	}
	
}
