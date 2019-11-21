package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 原PS类：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_YJDX_CLS
 * 
 * @author tang 报名管理-报名表审核邮件短信发送相关类
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglYjdxClsServiceImpl")
public class TzGdBmglYjdxClsServiceImpl extends FrameworkImpl {
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private GetSeqNum getSeqNum;
	/* 添加听众 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String audID = "";
		List<String> lists = new ArrayList<String>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return audID;
		}
		String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String strType = jacksonUtil.getString("type");
				String strPsnType = "";
				try{
					//上海大学人员类型，A-学生，B-教师 E:线索
					strPsnType = jacksonUtil.getString("psnType");
				}catch(Exception e){
					
				}
				boolean bMultiType = false;
				boolean bDCRMType = false;
				if ("DJZL".equals(strType)) {
					audID = createTaskServiceImpl.createAudience("", str_jg_id, "报名表递交资料审核", "JSRW");
				}

				if ("TJX".equals(strType)) {
					audID = createTaskServiceImpl.createAudience("", str_jg_id, "推荐信未完全提交提醒", "JSRW");
				}

				if ("MULTI".equals(strType)) {
					audID = createTaskServiceImpl.createAudience("", str_jg_id, "报名表审核批量发送邮件", "JSRW");
					bMultiType = true;
				}

				if ("DCRM".equals(strType)) {
					audID = createTaskServiceImpl.createAudience("", str_jg_id, "报名表审核批量发送邮件", "DCRM");
					bDCRMType = true;
				}
				String sOprID = "";
				if (bDCRMType) {
					long sAppInsID = 0;
					String bmrGuid = "";
					// 群发邮件添加听众;
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");

					if (list != null && list.size() > 0) {
						//一次性取完list.size步长
						int audCyId = getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID",list.size(),0)-list.size();
						for (int num_1 = 0; num_1 < list.size(); num_1++) {
							Map<String, Object> map = list.get(num_1);
							bmrGuid = (String) map.get("bmrGuid");
							String strName = "",mainMobilePhone = "", backupMobilePhone = "", mainEmail = "", backupEmail = "",wechat = "";
							String basicSQL = "";
							System.out.println("strPsnType:" + strPsnType);
							if("A".equals(strPsnType)){
								basicSQL = "select tzms_oprid,tzms_name,tzms_phone,tzms_email from tzms_stu_defn_tBase where tzms_stu_defn_tId = ?";
								Map<String,Object> basicMap = jdbcTemplate.queryForMap(basicSQL, new Object[] { bmrGuid });
								if(basicMap!=null){
									sOprID = basicMap.get("tzms_oprid") == null ? "" : String.valueOf(basicMap.get("tzms_oprid"));
									strName = basicMap.get("tzms_name") == null ? "" : String.valueOf(basicMap.get("tzms_name"));
									mainMobilePhone = basicMap.get("tzms_phone") == null ? "" : String.valueOf(basicMap.get("tzms_phone"));
									mainEmail = basicMap.get("tzms_email") == null ? "" : String.valueOf(basicMap.get("tzms_email"));
									
									createTaskServiceImpl.addAudCyForDynamics2(audID, strName, "", mainMobilePhone, backupMobilePhone,mainEmail, backupEmail, wechat, sOprID, "", strPsnType, String.valueOf(sAppInsID),bmrGuid,audCyId);
									audCyId++;
								}	
							}else if("B".equals(strPsnType)){
								basicSQL = "select tzms_oprid,tzms_name,tzms_phone,tzms_email from tzms_tea_defn_tBase where tzms_tea_defn_tId = ?";
								Map<String,Object> basicMap = jdbcTemplate.queryForMap(basicSQL, new Object[] { bmrGuid });
								if(basicMap!=null){
									sOprID = basicMap.get("tzms_oprid") == null ? "" : String.valueOf(basicMap.get("tzms_oprid"));
									strName = basicMap.get("tzms_name") == null ? "" : String.valueOf(basicMap.get("tzms_name"));
									mainMobilePhone = basicMap.get("tzms_phone") == null ? "" : String.valueOf(basicMap.get("tzms_phone"));
									mainEmail = basicMap.get("tzms_email") == null ? "" : String.valueOf(basicMap.get("tzms_email"));
									
									createTaskServiceImpl.addAudCyForDynamics2(audID, strName, "", mainMobilePhone, backupMobilePhone,mainEmail, backupEmail, wechat, sOprID, "", strPsnType, String.valueOf(sAppInsID),bmrGuid,audCyId);
									audCyId++;
								}
							}else if("E".equals(strPsnType)){
								basicSQL = "select fullname,emailaddress1,mobilephone from lead where leadid=?";
								Map<String,Object> basicMap = jdbcTemplate.queryForMap(basicSQL, new Object[] { bmrGuid });
								if(basicMap!=null){
									strName = basicMap.get("fullname") == null ? "" : String.valueOf(basicMap.get("fullname"));
									mainMobilePhone = basicMap.get("mobilephone") == null ? "" : String.valueOf(basicMap.get("mobilephone"));
									mainEmail = basicMap.get("emailaddress1") == null ? "" : String.valueOf(basicMap.get("emailaddress1"));
									
									createTaskServiceImpl.addAudCyForDynamics2(audID, strName, "", mainMobilePhone, backupMobilePhone,mainEmail, backupEmail, wechat, "", "", strPsnType, String.valueOf(sAppInsID),bmrGuid,audCyId);
									audCyId++;
								}
							}
						}
					}
				} else {
					if (bMultiType) {
						long sAppInsID = 0;
						// 群发邮件添加听众;
						@SuppressWarnings("unchecked")
						List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");

						if (list != null && list.size() > 0) {
							//一次性取完list.size步长
							int audCyId = getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID",list.size(),0)-list.size();
							for (int num_1 = 0; num_1 < list.size(); num_1++) {
								Map<String, Object> map = list.get(num_1);
								sOprID = (String) map.get("oprID");
								sAppInsID = Long.parseLong((String) map.get("appInsID"));

								// System.out.println();

								if (sOprID != null && !"".equals(sOprID) && sAppInsID != 0) {
									/* 为听众添加成员:姓名，称谓，报名人联系方式 */
									/*String strName = jdbcTemplate.queryForObject(
											tzGdObject.getSQLText("SQL.TZApplicationVerifiedBundle.strName"),
											new Object[] { sOprID }, "String");*/
									String strName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? limit 0,1",new Object[]{sOprID},"String");

									String mainMobilePhone = "", backupMobilePhone = "", mainEmail = "",
											backupEmail = "", wechat = "";
									Map<String, Object> lxfsMap = jdbcTemplate.queryForMap(
											"SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_DH,TZ_CY_DH,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_TXDZ,TZ_CY_TXDZ,TZ_WEIXIN,TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?",
											new Object[] { sOprID });
									if (lxfsMap != null) {
										mainMobilePhone = (String) lxfsMap.get("TZ_ZY_SJ");
										backupMobilePhone = (String) lxfsMap.get("TZ_CY_SJ");
										// mainPhone =
										// (String)lxfsMap.get("TZ_ZY_DH");
										// backupPhone =
										// (String)lxfsMap.get("TZ_CY_DH");
										mainEmail = (String) lxfsMap.get("TZ_ZY_EMAIL");
										backupEmail = (String) lxfsMap.get("TZ_CY_EMAIL");
										// mainAddress =
										// (String)lxfsMap.get("TZ_ZY_TXDZ");
										// backupAddress =
										// (String)lxfsMap.get("TZ_CY_TXDZ");
										wechat = (String) lxfsMap.get("TZ_WEIXIN");
										// skype =
										// (String)lxfsMap.get("TZ_SKYPE");
									}
									createTaskServiceImpl.addAudCy2(audID, strName, "", mainMobilePhone,
											backupMobilePhone, mainEmail, backupEmail, wechat, sOprID, "", "",
											String.valueOf(sAppInsID),audCyId);
									audCyId++;

								}

							}
						}

					} else {
						// 单发邮件添加听众;
						String sAppInsID = "";
						sOprID = jacksonUtil.getString("oprID");
						sAppInsID = jacksonUtil.getString("appInsID");
						/* 为听众添加成员:姓名，称谓，报名人联系方式 */
						/*String strName = jdbcTemplate.queryForObject(
								tzGdObject.getSQLText("SQL.TZApplicationVerifiedBundle.strName"),
								new Object[] { sOprID }, "String");*/
						String strName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? limit 0,1",new Object[]{sOprID},"String");

						String mainMobilePhone = "", backupMobilePhone = "", mainEmail = "", backupEmail = "",
								wechat = "";
						Map<String, Object> lxfsMap = jdbcTemplate.queryForMap(
								"SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_DH,TZ_CY_DH,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_TXDZ,TZ_CY_TXDZ,TZ_WEIXIN,TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?",
								new Object[] { sOprID });
						if (lxfsMap != null) {
							mainMobilePhone = (String) lxfsMap.get("TZ_ZY_SJ");
							backupMobilePhone = (String) lxfsMap.get("TZ_CY_SJ");
							// mainPhone = (String)lxfsMap.get("TZ_ZY_DH");
							// backupPhone = (String)lxfsMap.get("TZ_CY_DH");
							mainEmail = (String) lxfsMap.get("TZ_ZY_EMAIL");
							backupEmail = (String) lxfsMap.get("TZ_CY_EMAIL");
							// mainAddress = (String)lxfsMap.get("TZ_ZY_TXDZ");
							// backupAddress =
							// (String)lxfsMap.get("TZ_CY_TXDZ");
							wechat = (String) lxfsMap.get("TZ_WEIXIN");
							// skype = (String)lxfsMap.get("TZ_SKYPE");
						}
						createTaskServiceImpl.addAudCy(audID, strName, "", mainMobilePhone, backupMobilePhone,
								mainEmail, backupEmail, wechat, sOprID, "", "", sAppInsID);

					}
				}
//				String PrjId = jdbcTemplate.queryForObject(
//						"SELECT TZ_PRJ_ID FROM ps_tz_class_inf_t WHERE TZ_CLASS_ID=? ", new Object[] { classId },
//						"String");
//				//招生通知类原模板id
//				String ymbId= getHardCodePoint.getHardCodePointVal("TZ_DYNAMICS_USER_NAME");
//				String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
//				
//				List<Map<String, Object>> emlTmplList = jdbcTemplate.queryForList(
//						"select TZ_TMPL_ID from ps_tz_emaltmpl_tbl where TZ_YMB_ID=? AND TZ_PRJ_ID=? AND TZ_JG_ID=?", new Object[] {ymbId,PrjId,jgId});
//				String emalTmpl="";
//				if(emlTmplList!=null){
//					for (int i = 0; i < emlTmplList.size(); i++) {
//						lists.add((String)emlTmplList.get(i).get("TZ_TMPL_ID"));
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
//		Map<String,Object>retMap = new HashMap<String,Object>();
//		if(lists!=null){
//			retMap.put("audID", audID);
//			retMap.put("tmpls", lists.toString());
//			return jacksonUtil.Map2json(mapData));;
//		}
		return audID;
	}

	// 获得递交资料料内容;
	/* 生成发送内容方法 */
	public String getDjzlContent(String audId, String audCyId) {

		String strDjzlList = "";
		try {
			String sAppInsID = jdbcTemplate.queryForObject(
					"SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?",
					new Object[] { audId, audCyId }, "String");
			String strClassID = jdbcTemplate.queryForObject(
					"SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?",
					new Object[] { Long.valueOf(sAppInsID) }, "String");
			String strLanguageId = tzLoginServiceImpl.getSysLanaguageCD(request);
			if ("ENG".equals(strLanguageId)) {
				strLanguageId = "ENG";
			} else {
				strLanguageId = "ZHS";
			}

			String sqlContent = "SELECT TZ_SBMINF_ID,TZ_CONT_INTRO,TZ_REMARK FROM PS_TZ_CLS_DJZL_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlContent, new Object[] { strClassID });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String strFileID = (String) list.get(i).get("TZ_SBMINF_ID");
					String strContentIntro = (String) list.get(i).get("TZ_CONT_INTRO");
					// String strRemark = (String)list.get(i).get("TZ_REMARK");

					String strAuditState = "", strAuditStateDesc = "", strFailedReason = "";
					Map<String, Object> map = jdbcTemplate.queryForMap(
							"SELECT TZ_ZL_AUDIT_STATUS,TZ_AUDIT_NOPASS_RS FROM PS_TZ_FORM_ZLSH_T A WHERE TZ_APP_INS_ID=? AND TZ_SBMINF_ID=?",
							new Object[] { sAppInsID, strFileID });
					if (map != null) {
						strAuditState = (String) map.get("TZ_ZL_AUDIT_STATUS");
						strFailedReason = (String) map.get("TZ_AUDIT_NOPASS_RS");

						strAuditStateDesc = jdbcTemplate.queryForObject(
								tzGdObject.getSQLText("SQL.TZApplicationVerifiedBundle.strAuditStateDesc"),
								new Object[] { strLanguageId, strAuditState, }, "String");

						strDjzlList = strDjzlList + "<tr>"
								+ tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_TR_TD_HTML",
										strContentIntro)
								+ tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_TR_TD_HTML",
										strAuditStateDesc)
								+ tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_TR_TD_HTML",
										strFailedReason)
								+ "</tr>";

					}
				}
			}

			return tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_CONTENT_HTML", strDjzlList);
		} catch (Exception e) {
			return e.toString();
		}

	}
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		if("queryTmpl".equals(oprType)){
			return queryTmpl(strParams,errorMsg);
		}
		return "";
	}

	private String queryTmpl(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		try {
			// 班级编号;
			String strClassID = jacksonUtil.getString("classId");
			String prjId = jdbcTemplate.queryForObject("SELECT TZ_PRJ_ID FROM ps_tz_class_inf_t WHERE TZ_CLASS_ID=? ", new Object[] { strClassID },"String");
			String ymbId= getHardCodePoint.getHardCodePointVal("TZ_ZSTZ_YMB");
			String jgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			List<Map<String, Object>> emlTmplList = jdbcTemplate.queryForList(
					"select TZ_TMPL_ID from ps_tz_emaltmpl_tbl where TZ_YMB_ID=? AND TZ_PRJ_ID=? AND TZ_JG_ID=?", new Object[] {ymbId,prjId,jgId});
			String emalTmpl="[]";
			List<String>lists =  new ArrayList<String>();
			if(emlTmplList!=null){
				for (int i = 0; i < emlTmplList.size(); i++) {
					lists.add((String)emlTmplList.get(i).get("TZ_TMPL_ID"));
				}
			}
			emalTmpl="[\""+ StringUtils.join(lists.toArray(), "\",\"")+"\"]";
			return emalTmpl;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "[]";
	}
}
