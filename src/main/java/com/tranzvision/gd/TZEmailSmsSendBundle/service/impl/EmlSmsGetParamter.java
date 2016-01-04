package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

/**
 * PS类:  TZ_GD_COM_EMLSMS_APP:emlSmsGetParamter
 * @author tang
 * 邮件短信获得公共参数的功能
 * 
 */
public class EmlSmsGetParamter {
	//所有邮件、短信发送的参数都以各自创建TZ_AUDCYUAN_T表听众数据中的听众id和听众成员id为参数;
    public String getName (String[] paramters){
		try{
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil(); 
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_AUD_XM from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[]{audId,audCyId});
			return name;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
    
    //生成注册用户发送的邮件内容;
	public String createUrlforEnroll (String[] paramters){
		try{
			String audId = paramters[0];
			String audCyId = paramters[1];
			
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil(); 
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String opridSQL = "SELECT OPRID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String strOprId = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[]{audId,audCyId});
			if(strOprId != null && !"".equals(strOprId)){
				String jgSQL = "SELECT TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				String strOrgid = jdbcTemplate.queryForObject(jgSQL, String.class, new Object[]{strOprId});
				
				String langSQL = "SELECT TZ_SITE_LANG FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y'";
				String strLang = jdbcTemplate.queryForObject(langSQL, String.class, new Object[]{strOrgid});
			
				String tokenCodeSQL = "SELECT TZ_TOKEN_CODE FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=? AND TZ_TOKEN_TYPE='REG' AND TZ_EFF_FLAG='Y' ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
				String strTokenSign = jdbcTemplate.queryForObject(tokenCodeSQL, String.class, new Object[]{strOprId, strOrgid,});
				if(strTokenSign != null && !"".equals(strTokenSign)){
					String strActUrl = "http://localhost:8080/university/dispatcher";
					strActUrl = strActUrl + "?classid=enrollCls&tokensign=" + strTokenSign + "&orgid=" + strOrgid + "&lang=" + strLang + "&sen=2";
					return strActUrl;
				}else{
					return "";
				}
			}else{
				return "";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	//后台自助信息获取绑定邮箱的URL
	public String getBindEmailUrl(String[] paramters){
		try{
			String audId = paramters[0];
			String audCyId = paramters[1];
			HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			String serv = "http://"+ request.getServerName() + ":"+ request.getServerPort() + request.getContextPath();
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil(); 
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String opridSQL = "select TZ_TOKEN_CODE from PS_TZ_AUDCYUAN_T a,PS_TZ_DZYX_YZM_TBL b,PS_TZ_AQ_YHXX_TBL c where a.OPRID=c.OPRID and b.TZ_JG_ID=c.TZ_JG_ID and c.TZ_DLZH_ID= b.TZ_DLZH_ID and a.TZ_AUDIENCE_ID=? and a.TZ_AUDCY_ID=? and b.TZ_EFF_FLAG='Y' and b.TZ_TOKEN_TYPE='EDIT'";
			String tokencode = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[]{audId,audCyId});
			String bindEmailUrl = serv + "/dispatcher";
			bindEmailUrl = bindEmailUrl + "?classid=selfBindEmail&TZ_TOKEN_CODE=" + tokencode ;
			return bindEmailUrl;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	
}
