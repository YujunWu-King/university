package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxJSSDKSign;
import javax.servlet.http.HttpServletRequest;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import java.util.Map;

/**
 * 
 * @author YTT
 * @Time 2017-2-14
 * @paramtersm 机构id,站点id,申请人oprid,报名表编号
 * @return 晒录取通知书证书模板中的系统变量解析值
 * 
 */
public class CertGetParameter {

	// 清华MBA录取通知书-分享缩略图;
	public String getCertLogo(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String CertLogoSql = "SELECT CONCAT(A.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA) FROM PS_TZ_CERTIMAGE_TBL A,PS_TZ_CERTTMPL_TBL B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND B.TZ_JG_ID=? AND B.TZ_CERT_TMPL_ID=(SELECT B.TZ_CERT_TMPL_ID FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID)";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertLogo = jdbcTemplate.queryForObject(CertLogoSql, String.class, new Object[] { jgId, appIns });
			String ServerUrlSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String ServerUrl = jdbcTemplate.queryForObject(ServerUrlSql, String.class, new Object[] { "TZ_SERVER_URL" });

			/*HttpServletRequest httpServletRequest = (HttpServletRequest) getSpringBeanUtil
					.getSpringBeanByID("httpServletRequest");
			CertLogo=  httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":"
					+ String.valueOf(httpServletRequest.getServerPort()) + httpServletRequest.getContextPath()+CertLogo;*/
			CertLogo= ServerUrl+CertLogo;
			
			return CertLogo;
		
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 清华MBA录取通知书-个人照片url;
	public String getCertImg(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String CertImgSql = "SELECT CONCAT(B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA) FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.OPRID=?";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertImg = jdbcTemplate.queryForObject(CertImgSql, String.class, new Object[] { oprid });
			return CertImg;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 清华MBA录取通知书-姓名;
	public String getCertNAME(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String CertNameSql = "SELECT B.TZ_REALNAME from PS_TZ_APP_INS_T A,PS_TZ_REG_USER_T B WHERE A.TZ_APP_INS_ID = ? AND B.OPRID=A.ROW_ADDED_OPRID";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertName = jdbcTemplate.queryForObject(CertNameSql, String.class, new Object[] { appIns });
			return CertName;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 清华MBA录取通知书-入学年份;
	public String getCertYEAR(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String CertYearSql = "SELECT YEAR(A.TZ_START_DT) from PS_TZ_CLASS_INF_T A,PS_TZ_FORM_WRK_T B WHERE B.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String CertYear = jdbcTemplate.queryForObject(CertYearSql, String.class, new Object[] { appIns });
			return CertYear;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 项目名称;
	public String getPrgName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String PrgNameSql = "SELECT B.TZ_PRJ_NAME FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID";

			String jgId = paramters[0];
			String siteId = paramters[1];
			String oprid = paramters[2];
			String appIns = paramters[3];

			String PrgName = jdbcTemplate.queryForObject(PrgNameSql, String.class, new Object[] { appIns });
			return PrgName;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 企业Id / 公众号appId;
	public String getWxCorpid(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String WxCorpidSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String WxCorpid = jdbcTemplate.queryForObject(WxCorpidSql, String.class, new Object[] { "TZ_WX_CORPID" });
			return WxCorpid;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	//微信 ServerUrl;
	public String getServerUrl(String[] paramters) {
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

				String ServerUrlSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				String ServerUrl = jdbcTemplate.queryForObject(ServerUrlSql, String.class, new Object[] { "TZ_SERVER_URL" });

				return ServerUrl;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	
}
