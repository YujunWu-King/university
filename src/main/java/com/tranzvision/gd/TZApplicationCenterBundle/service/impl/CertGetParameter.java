package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.tranzvision.gd.util.sql.SqlQuery;

/**

* @author YTT
* @Time 2017-2-14
* @param 机构id,站点id,申请人oprid,报名表编号
* @return 晒录取通知书证书模板中的系统变量解析值

*/
public class CertGetParameter {
	@Autowired
	private SqlQuery sqlQuery;	
	
	// 清华MBA录取通知书-分享缩略图;
	public String getCertLogo(String[] para) {
		String jgId=para[0];
		String siteId=para[1];
		String oprid=para[2];
		String appIns=para[3];

		String CertLogoSql="SELECT CONCAT(A.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA) FROM PS_TZ_CERTIMAGE_TBL A,PS_TZ_CERTTMPL_TBL B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND B.TZ_JG_ID=? AND B.TZ_CERT_TMPL_ID=(SELECT B.TZ_CERT_TMPL_ID FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID)";
		String CertLogo= sqlQuery.queryForObject(CertLogoSql, new Object[] {jgId,appIns}, "String");

		return CertLogo;
	}
	
	// 清华MBA录取通知书-个人照片url;
	public String getCertImg(String[] para) {
		String jgId=para[0];
		String siteId=para[1];
		String oprid=para[2];
		String appIns=para[3];
		
		String CertImgSql="SELECT CONCAT(B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA) FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.OPRID=?";
		String CertImg=sqlQuery.queryForObject(CertImgSql, new Object[] {oprid}, "String");
		return CertImg;
	}
	
	// 清华MBA录取通知书-姓名;
	public String getCertNAME(String[] para) {
		String jgId=para[0];
		String siteId=para[1];
		String oprid=para[2];
		String appIns=para[3];
		
		String CertNameSql="SELECT B.TZ_REALNAME from PS_TZ_APP_INS_T A,PS_TZ_REG_USER_T B WHERE A.TZ_APP_INS_ID = ? AND B.OPRID=A.ROW_ADDED_OPRID";
		String CertName=sqlQuery.queryForObject(CertNameSql, new Object[] {appIns}, "String");
		return CertName;
	}
	
	// 清华MBA录取通知书-入学年份;
	public String getCertYEAR(String[] para) {
		String jgId=para[0];
		String siteId=para[1];
		String oprid=para[2];
		String appIns=para[3];
		String CertYearSql="SELECT YEAR(TZ_START_DT) from PS_TZ_CLASS_INF_T C,PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE C.TZ_JG_ID=? AND A.TZ_APP_INS_ID=?  AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID AND A.TZ_APP_TPL_ID=C.TZ_APP_MODAL_ID AND C.TZ_PRJ_ID=B.TZ_PRJ_ID";
		String CertYear=sqlQuery.queryForObject(CertYearSql, new Object[] {jgId,appIns}, "String");
		return CertYear;
	}
	
	// 项目名称;
	public String getPrgName(String[] para) {
		String jgId=para[0];
		String siteId=para[1];
		String oprid=para[2];
		String appIns=para[3];
		
		String PrgNameSql="SELECT B.TZ_PRJ_NAME FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID";
		String PrgName=sqlQuery.queryForObject(PrgNameSql, new Object[] {appIns}, "String");
		return PrgName;
	}
}
