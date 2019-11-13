package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzClientageInfo;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 该类用于提供查询指定用户的各类信息
 * @author 张浪	2019-01-11
 *
 */
public class TzWflUser {

	private SqlQuery sqlQuery;
	
	//当前委托人
	private String m_userId;
	
	//解析委托用户集合
	private Set<String> m_UserSet;
	
	//路径分隔符
	private String PATH_SEPARATOR = "-->";
	
	//路径
	private String m_Path;
	

	
	
	public String getM_userId() {
		return m_userId;
	}
	public void setM_userId(String m_userId) {
		this.m_userId = m_userId;
	}
	public Set<String> getM_UserSet() {
		return m_UserSet;
	}
	public void setM_UserSet(Set<String> m_UserSet) {
		this.m_UserSet = m_UserSet;
	}
	public String getM_Path() {
		return m_Path;
	}
	public void setM_Path(String m_Path) {
		this.m_Path = m_Path;
	}
	
	
	
	
	
	/**
	 * 构造函数
	 * @param m_userId
	 */
	public TzWflUser(String s_userId) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		m_userId = s_userId;
		m_Path = s_userId;
		
		m_UserSet = new HashSet<String>();
		m_UserSet.add(s_userId);
	}

	
	
	/**
	 * 根据委托人用户ID获取指定业务流程下的被委托人
	 * @param WflProDefnId	业务流程编号
	 * @param path			委托路径
	 */
	public List<TzClientageInfo> GetUserConsignedInfo(String WflProDefnId){
		//委托人列表
		List<TzClientageInfo> r_ClientageList = new ArrayList<TzClientageInfo>();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date currDate = null;
		try {
			currDate = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//查询委托人在改业务流程下的所有委托关系
		List<Map<String,Object>> ctgList = sqlQuery.queryForList("select tzms_csgdtl_tid,tzms_asgperson_userid,tzms_stdt,tzms_eddt from tzms_csgdtl_tBase where tzms_wfcldn_uniqueid=? and CAST(OwnerId as varchar(36))=?", 
				new Object[]{ WflProDefnId, m_userId });
		
		if(ctgList != null && ctgList.size() > 0){
			for(Map<String,Object> ctgMap : ctgList){
				String ctgOrderID = ctgMap.get("tzms_csgdtl_tid").toString();
				//被委托人
				String assignee = ctgMap.get("tzms_asgperson_userid") == null ? "" : ctgMap.get("tzms_asgperson_userid").toString();
				
				Date beginDate = (Date) ctgMap.get("tzms_stdt");
				Date endDate = (Date) ctgMap.get("tzms_eddt");
				
				if(StringUtils.isNotEmpty(assignee) 
						&& beginDate != null && endDate != null){

					//进行时间判断，确保只有当前日期有效的数据才会被返回
					if(currDate.compareTo(beginDate) >= 0 
							&& currDate.compareTo(endDate) <= 0){
						
						if(m_UserSet.contains(assignee)){
							//如果被委托人已解析过，退出继续解析下一个
							continue;
						}
						m_UserSet.add(assignee);
						m_Path += PATH_SEPARATOR + assignee;
						
						//查询被委托人是否将流程委托出去
						TzWflUser tzWflUser = new TzWflUser(assignee);
						tzWflUser.setM_UserSet(m_UserSet);
						tzWflUser.setM_Path(m_Path);
						List<TzClientageInfo> clientageList = tzWflUser.GetUserConsignedInfo(WflProDefnId);
						
						if(clientageList.size() > 0){
							r_ClientageList.addAll(clientageList);
						}else{
							TzClientageInfo l_Clientage = new TzClientageInfo();
							l_Clientage.setM_ConsignOrderID(ctgOrderID);
							l_Clientage.setM_BusProcessDefID(WflProDefnId);
							l_Clientage.setM_Assignee(assignee);
							l_Clientage.setM_Consigner(m_userId);
							l_Clientage.setM_BeginDate(beginDate);
							l_Clientage.setM_EndDate(endDate);
							l_Clientage.setM_ClientagePath(m_Path);
							
							r_ClientageList.add(l_Clientage);
						}
					}
				}
			}
		}
		
		return r_ClientageList;
	}
}
