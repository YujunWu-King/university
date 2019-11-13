package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaSysVar;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * 工作流摘要类
 * @author 张浪	2019-01-11
 *
 */
public class TzWflAbstract {

	private SqlQuery sqlQuery;
	
	//工作流实例编号
	private String m_WflInstanceID;
	
	//步骤实例编号
	private String m_WflStpInsID;
	
	//业务流程定义编号
	private String m_BusProcessDefID;
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
		
		
	
	/**
	 * 构造方法
	 * @param wflInsID_IN		工作流实例编号
	 * @param wflStpInsID_IN	步骤实例编号
	 * @throws TzException
	 */
	public TzWflAbstract(String wflInsID_IN, String wflStpInsID_IN) throws TzException {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		if(StringUtils.isNotEmpty(wflInsID_IN) 
				&& StringUtils.isNotEmpty(wflStpInsID_IN)){
			
			m_WflInstanceID = wflInsID_IN;
			m_WflStpInsID = wflStpInsID_IN;
			
			//根据工作流实例编号获取业务流程编号
			String wflBusproDefnID = sqlQuery.queryForObject("select tzms_wfcldn_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[]{ wflInsID_IN }, "String");
			
			m_BusProcessDefID = wflBusproDefnID;
		}else{
			throw new TzException("【系统运行错误】工作流摘要类的构造函数调用失败。原因：没有提供正确的参数。");
		}
	}


	
	
	
	/**
	 * 获取业务流程摘要信息
	 * @return
	 */
	public String getAbstractString(){
		String abstractString = "";
		
		//按顺序拼接摘要信息
		List<Map<String,Object>> sysVarList = sqlQuery.queryForList("select tzms_sysvar_uniqueid from tzms_wflabs_t where tzms_wfcldn_uniqueid=? and tzms_absdpyflg=1 order by tzms_grpordseq", 
				new Object[]{ m_BusProcessDefID });
		
		if(sysVarList != null && sysVarList.size() > 0){
			for(Map<String,Object> sysVarMap: sysVarList){
				String sysVarID = sysVarMap.get("tzms_sysvar_uniqueid") == null ? "" : sysVarMap.get("tzms_sysvar_uniqueid").toString();
				
				if(!"".equals(sysVarID)){
					//解析系统变量
					try{						
						/*系统变量解析开始----START*/
						AnalysisDynaSysVar analysisSysVar = new AnalysisDynaSysVar();
						analysisSysVar.setM_SysVarID(sysVarID);
						
						//业务数据ID
						String wflDateRecId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
								new Object[]{ m_WflInstanceID }, "String");
						if(wflDateRecId == null) wflDateRecId = "";
						
						//设置参数
						String[] sysVarParam = { m_WflInstanceID, m_WflStpInsID, wflDateRecId };
						analysisSysVar.setM_SysVarParam(sysVarParam);
						
						String sysVarValue = (String)analysisSysVar.GetVarValue();
						/*系统变量解析开始----END*/
						
						
						if(StringUtils.isNotEmpty(sysVarValue)){
							if("".equals(abstractString)){
								abstractString = sysVarValue;
							}else{
								abstractString += "，" + sysVarValue;
							}
						}
					}catch (Exception e) {
						//do nothing
						logger.error("解析工作流摘要信息失败，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息", e);
					}
				}
			}
		}
		
		return abstractString;
	}
}
