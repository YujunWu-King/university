package com.tranzvision.gd.TZStuCertificateBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

//所有邮件、短信发送的参数都以各自创建TZ_AUDCYUAN_T表听众数据中的听众id和听众成员id为参数;
public class EmlSmsGetParamter {
	
    //校友卡查看URL
	public String getSchoolCardUrl(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_HUOD_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String tz_seqnum = jdbcTemplate.queryForObject(sql, String.class,new Object[] { audId, audCyId });
			String TZ_ZHSH_URL=jdbcTemplate.queryForObject("select TZ_ZHSH_URL from PS_TZ_CERT_IMGS_TBL where TZ_SEQNUM=?", String.class,new Object[]{tz_seqnum});
			TZ_ZHSH_URL=TZ_ZHSH_URL==null?"":TZ_ZHSH_URL.toString();
			return TZ_ZHSH_URL;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
   //校友卡查看二维码URL
	public String getSchoolCardEwm(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_HUOD_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String tz_seqnum = jdbcTemplate.queryForObject(sql,String.class,new Object[] { audId, audCyId });
			String tz_zhsh_image=jdbcTemplate.queryForObject("select TZ_ZHSH_IMAGE from PS_TZ_CERT_IMGS_TBL where TZ_SEQNUM=?", String.class,new Object[]{tz_seqnum});
			tz_zhsh_image=tz_zhsh_image==null?"":tz_zhsh_image.toString();
			String imageUrl=jdbcTemplate.queryForObject("SELECT B.TZ_ATT_A_URL FROM PS_TZ_CERT_IMGS_TBL A ,PS_TZ_CERTIMAGE_TBL B WHERE A.TZ_SEQNUM = ? AND A.TZ_ZHSH_IMAGE = B.TZ_ATTACHSYSFILENA", String.class,new Object[]{tz_seqnum});
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			String serv = "http://" + request.getServerName() + ":" + request.getServerPort();
			String url=serv+imageUrl+tz_zhsh_image;
		    return url;
		} catch (Exception e) { 
			e.printStackTrace();
			return "";
		}
	}
	   //校友卡管理URL
		public String getSchoolCardGlUrl(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String sql = "select TZ_HUOD_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String tz_seqnum = jdbcTemplate.queryForObject(sql, String.class,new Object[] { audId, audCyId });
				String TZ_ZHGL_URL=jdbcTemplate.queryForObject("select TZ_ZHGL_URL from PS_TZ_CERT_IMGS_TBL where TZ_SEQNUM=?", String.class,new Object[]{tz_seqnum});
				TZ_ZHGL_URL=TZ_ZHGL_URL==null?"":TZ_ZHGL_URL.toString();
				return TZ_ZHGL_URL;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	   //校友卡管理二维码URL
		public String getSchoolCardGlEwm(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String sql = "select TZ_HUOD_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String tz_seqnum = jdbcTemplate.queryForObject(sql, String.class,new Object[] { audId, audCyId });
				String tz_zhgl_image=jdbcTemplate.queryForObject("select TZ_ZHGL_IMAGE from PS_TZ_CERT_IMGS_TBL where TZ_SEQNUM=?", String.class,new Object[]{tz_seqnum});
				tz_zhgl_image=tz_zhgl_image==null?"":tz_zhgl_image.toString();
				String imageUrl=jdbcTemplate.queryForObject("SELECT B.TZ_ATT_A_URL FROM PS_TZ_CERT_IMGS_TBL A ,PS_TZ_CERTIMAGE_TBL B WHERE A.TZ_SEQNUM = ? AND A.TZ_ZHSH_IMAGE = B.TZ_ATTACHSYSFILENA", String.class,new Object[]{tz_seqnum});
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes()).getRequest();
				String serv = "http://" + request.getServerName() + ":" + request.getServerPort();
				String url=serv+imageUrl+tz_zhgl_image; 
			    return url;
			} catch (Exception e) { 
				e.printStackTrace();
				return "";
			}
		}
		//校友卡颁发机构
		public String getSchoolCardJgmc(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String sql = "select TZ_HUOD_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String tz_seqnum = jdbcTemplate.queryForObject(sql, String.class,new Object[] { audId, audCyId });
				Map<String,Object> map=jdbcTemplate.queryForMap("SELECT TZ_JG_ID,TZ_CERT_TMPL_ID,TZ_CERT_TYPE_ID FROM PS_TZ_CERT_INFO_TBL WHERE TZ_SEQNUM=?", new Object[]{tz_seqnum});
				String strJgId=map.get("TZ_JG_ID").toString();
				String strCertTmplId=map.get("TZ_CERT_TMPL_ID").toString();
				//String strCertTypeId=map.get("TZ_CERT_TYPE_ID").toString();
				String strCertJgName=jdbcTemplate.queryForObject("select TZ_CERT_JG_NAME from PS_TZ_CERTTMPL_V where TZ_JG_ID=? and TZ_CERT_TMPL_ID=?", String.class,new Object[]{strJgId,strCertTmplId});
				return strCertJgName;
			} catch (Exception e) { 
				e.printStackTrace();
				return "";
			}
		}
	   //校友卡类型名称
		public String getCertTypeName(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String sql = "select TZ_HUOD_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String tz_seqnum = jdbcTemplate.queryForObject(sql, String.class,new Object[] { audId, audCyId });
				Map<String,Object> map=jdbcTemplate.queryForMap("SELECT TZ_JG_ID,TZ_CERT_TYPE_ID FROM PS_TZ_CERT_INFO_TBL WHERE TZ_SEQNUM=?", new Object[]{tz_seqnum});
				String strJgId=map.get("TZ_JG_ID").toString();
				String strCertTypeId=map.get("TZ_CERT_TYPE_ID").toString();
				String strCertTypeName=jdbcTemplate.queryForObject("select TZ_CERT_TYPE_NAME from PS_TZ_CERT_TYPE_TBL where TZ_JG_ID=? and TZ_CERT_TYPE_ID=?", String.class,new Object[]{strJgId,strCertTypeId});
				return strCertTypeName;
			} catch (Exception e) { 
				e.printStackTrace();
				return "";
			}
		}
		
		
		
}
