package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormLabelTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormZlshTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFrmMorinfTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormLabelTKey;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshTKey;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFrmMorinfT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFrmMorinfTKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZLabelSetBundle.dao.PsTzLabelDfnTMapper;
import com.tranzvision.gd.TZLabelSetBundle.model.PsTzLabelDfnT;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzMszgTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTblKey;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMszgT;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPskshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTblKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhccTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcTKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccTKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkTKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_AUDIT_CLS
 * 
 * @author tang 报名管理-报名表审核
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglAuditClsServiceImpl")
public class TzGdBmglAuditClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;
	@Autowired
	private PsTzFormLabelTMapper psTzFormLabelTMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzLabelDfnTMapper psTzLabelDfnTMapper;
	@Autowired
	private PsTzFormZlshTMapper psTzFormZlshTMapper;
	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	@Autowired
	private PsTzFrmMorinfTMapper psTzFrmMorinfTMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private PsTzAppCcTMapper psTzAppCcTMapper;
	@Autowired
	private PsTzAppDhccTMapper psTzAppDhccTMapper;
	@Autowired
	private PsTzMszgTMapper pstzMszgTMapper;
	@Autowired
	private PsTzMsPskshTblMapper psTzMsPskshTblMapper;

	/* 获取报名人信息 */
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("classID") && jacksonUtil.containsKey("oprID")) {
				// 班级编号;
				String strClassID = jacksonUtil.getString("classID");
				// 报名人编号;
				String strOprID = jacksonUtil.getString("oprID");

				// 班级名称，所属项目，所属项目名称;
				String strClassName = "";

				String attUrlSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
				String str_att_p_url = jdbcTemplate.queryForObject(attUrlSQL, new Object[] { "TZ_AFORM_FILE_DIR" },
						"String");
				if (str_att_p_url == null || "".equals(str_att_p_url)) {
					str_att_p_url = "";
				}

				// 获取班级名称、所属项目，所属项目名称，报名表模板ID;
				String sql = "SELECT TZ_CLASS_NAME,TZ_PRJ_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strClassID });
				if (map != null) {
					strClassName = (String) map.get("TZ_CLASS_NAME");
					// strProjectID = (String) map.get("TZ_PRJ_ID");
				}

				// 报名人信息：报名表编号，审批状态，类别，姓名，出生日期，报名表提交状态，备注，所属分组;
				long strAppInsID = 0L;
				String strSpState = "", strColorType = "", strStuName = "", strBirthDate = "", strSubmitState = "",
						strRemark = "", strShortRemark = "";
				Map<String, Object> bmrMap = jdbcTemplate.queryForMap(
						"SELECT TZ_APP_INS_ID,TZ_FORM_SP_STA,TZ_COLOR_SORT_ID ,TZ_REMARK ,TZ_REMARK_SHORT FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=? limit 0,1",
						new Object[] { strClassID, strOprID });
				if (bmrMap != null) {
					strAppInsID = Long.valueOf(bmrMap.get("TZ_APP_INS_ID").toString());
					strSpState = (String) bmrMap.get("TZ_FORM_SP_STA");
					strColorType = (String) bmrMap.get("TZ_COLOR_SORT_ID");
					strRemark = (String) bmrMap.get("TZ_REMARK");
					strShortRemark = (String) bmrMap.get("TZ_REMARK_SHORT");
				}

				Map<String, Object> nameMap = jdbcTemplate.queryForMap(
						"SELECT TZ_REALNAME ,DATE_FORMAT(BIRTHDATE,'%y-%m-%d %H:%i:%s') BIRTHDATE FROM PS_TZ_REG_USER_T WHERE OPRID=?",
						new Object[] { strOprID });
				if (nameMap != null) {
					strStuName = (String) nameMap.get("TZ_REALNAME");
					strBirthDate = (String) nameMap.get("BIRTHDATE");
					if (strStuName == null || "".equals(strStuName)) {
						strStuName = jdbcTemplate.queryForObject(
								"SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[] { strOprID },
								"String");
					}
				}

				String str_app_tpl_id = ""; /* 报名表模版编号 */
				String str_app_tpl_json = ""; /* 报名表模版Json */
				String str_app_ins_json = ""; /* 报名表实例Json */
				int num_max_tjr_id = 0;
				String str_app_lang = ""; /* 报名表语言 */

				Map<String, Object> appMap = jdbcTemplate.queryForMap(
						"SELECT TZ_APP_FORM_STA,TZ_APP_TPL_ID,TZ_APPINS_JSON_STR FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?",
						new Object[] { strAppInsID });
				if (appMap != null) {
					strSubmitState = (String) appMap.get("TZ_APP_FORM_STA");
					str_app_tpl_id = (String) appMap.get("TZ_APP_TPL_ID");
					str_app_ins_json = (String) appMap.get("TZ_APPINS_JSON_STR");
				}

				Map<String, Object> apptplDyMap = jdbcTemplate.queryForMap(
						"SELECT TZ_APPTPL_JSON_STR,TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?",
						new Object[] { str_app_tpl_id });
				if (apptplDyMap != null) {
					str_app_tpl_json = (String) apptplDyMap.get("TZ_APPTPL_JSON_STR");
					str_app_lang = (String) apptplDyMap.get("TZ_APP_TPL_LAN");
				}

				try {
					num_max_tjr_id = jdbcTemplate.queryForObject(
							"SELECT MAX(CAST(TZ_TJR_ID as SIGNED)) TZ_TJR_ID FROM PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID = ? AND TZ_MBA_TJX_YX = 'Y' ",
							new Object[] { strAppInsID }, "Integer");
				} catch (Exception e) {
					num_max_tjr_id = 0;
				}

				if (str_app_tpl_json == null || "".equals(str_app_tpl_json)) {
					str_app_tpl_json = "{}";
				}
				if (str_app_ins_json == null || "".equals(str_app_ins_json)) {
					str_app_ins_json = "{}";
				}

				// 报名人标签;
				String strTagID = "";
				ArrayList<String> strTagList = new ArrayList<>();
				List<Map<String, Object>> list = jdbcTemplate.queryForList(
						"SELECT TZ_LABEL_ID FROM PS_TZ_FORM_LABEL_T WHERE TZ_APP_INS_ID=?",
						new Object[] { strAppInsID });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						strTagID = (String) list.get(i).get("TZ_LABEL_ID");
						strTagList.add(strTagID);
					}
				}

				// 报名人联系方式;

				String mainMobilePhone = "", backupMobilePhone = "", mainPhone = "", backupPhone = "", mainEmail = "",
						backupEmail = "", mainAddress = "", backupAddress = "", wechat = "", skype = "";
				String oprid = jdbcTemplate.queryForObject(
						"select OPRID from PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=? limit 0,1",
						new Object[] { strAppInsID }, "String");

				if (oprid != null && !"".equals(oprid)) {
					String lxfsSQL = "SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_DH,TZ_CY_DH,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_TXDZ,TZ_CY_TXDZ,TZ_WEIXIN,TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?";
					Map<String, Object> lxfsMap = jdbcTemplate.queryForMap(lxfsSQL, new Object[] { oprid });
					if (lxfsMap != null) {
						mainMobilePhone = (String) lxfsMap.get("TZ_ZY_SJ");
						backupMobilePhone = (String) lxfsMap.get("TZ_CY_SJ");
						mainPhone = (String) lxfsMap.get("TZ_ZY_DH");
						backupPhone = (String) lxfsMap.get("TZ_CY_DH");
						mainEmail = (String) lxfsMap.get("TZ_ZY_EMAIL");
						backupEmail = (String) lxfsMap.get("TZ_CY_EMAIL");
						mainAddress = (String) lxfsMap.get("TZ_ZY_TXDZ");
						backupAddress = (String) lxfsMap.get("TZ_CY_TXDZ");
						wechat = (String) lxfsMap.get("TZ_WEIXIN");
						skype = (String) lxfsMap.get("TZ_SKYPE");
					}
				}

				String sqlClue = "select TZ_LEAD_ID from PS_TZ_XSXS_BMB_T where TZ_APP_INS_ID = ?";
				String clueId = jdbcTemplate.queryForObject(sqlClue,
						new Object[] { strAppInsID },"String");
				
				Map<String, Object> jsonMap = new HashMap<>();
				jsonMap.put("clueID", clueId);
				
				jsonMap.put("classID", strClassID);
				jsonMap.put("oprID", strOprID);
				jsonMap.put("stuName", strStuName);
				jsonMap.put("birthDate", strBirthDate);
				jsonMap.put("className", strClassName);
				jsonMap.put("appInsID", strAppInsID);
				jsonMap.put("appFormState", strSubmitState);
				jsonMap.put("auditState", strSpState);
				jsonMap.put("colorType", strColorType);
				jsonMap.put("tag", strTagList);
				jsonMap.put("remark", strRemark);
				jsonMap.put("mainMobilePhone", mainMobilePhone);
				jsonMap.put("backupMobilePhone", backupMobilePhone);
				jsonMap.put("mainPhone", mainPhone);
				jsonMap.put("backupPhone", backupPhone);
				jsonMap.put("mainEmail", mainEmail);
				jsonMap.put("backupEmail", backupEmail);
				jsonMap.put("mainAddress", mainAddress);
				jsonMap.put("backupAddress", backupAddress);
				jsonMap.put("wechat", wechat);
				jsonMap.put("skype", skype);
				jsonMap.put("shortRemark", strShortRemark);
				jsonMap.put("appTplJsonStr", str_app_tpl_json);
				jsonMap.put("appInsJsonStr", str_app_ins_json);
				jsonMap.put("maxTjrId", num_max_tjr_id);
				jsonMap.put("tjxAttrUrl", str_att_p_url);
				jsonMap.put("appLang", str_app_lang);
				returnJsonMap.replace("formData", jsonMap);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择报名人";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 获取递交资料检查列表、更多信息等 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String returnString = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			if (jacksonUtil.containsKey("classID") && jacksonUtil.containsKey("oprID")) {
				// 班级编号;
				String strClassID = jacksonUtil.getString("classID");
				// oprID;
				String strOprID = jacksonUtil.getString("oprID");

				// 查询类型 ;
				boolean bl = false;
				String strQueryType = jacksonUtil.getString("queryType");
				if ("FILE".equals(strQueryType)) {
					returnString = this.tzQueryFileCheckList(strClassID, strOprID, numLimit, numStart, errorMsg);
					bl = true;
				}
				if ("BMLC".equals(strQueryType)) {
					returnString = this.tzQueryBmlcList(strClassID, strOprID, numLimit, numStart, errorMsg);
					bl = true;
				}
				if ("REFLETTER".equals(strQueryType)) {
					returnString = this.tzQueryRefLetterList(strClassID, strOprID, numLimit, numStart, errorMsg);
					bl = true;
				}
				if ("ATTACHMENT".equals(strQueryType)) {
					returnString = this.tzQueryAttachmentList(strClassID, strOprID, numLimit, numStart, errorMsg);
					bl = true;
				}
				if ("ATTACHMENT2".equals(strQueryType)) {
					// FILETYPE;
					int FILETYPE = jacksonUtil.getInt("FILETYPE");
					returnString = this.tzQueryAttachmentList2(strClassID, strOprID,FILETYPE, numLimit, numStart, errorMsg);
					bl = true;
				}
				if (bl == false) {
					errorMsg[0] = "1";
					errorMsg[1] = "请求错误";
					return jacksonUtil.Map2json(mapRet);
				} else {
					return returnString;
				}
			} else {
				return jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	//查询复试资料
	private String tzQueryAttachmentList2(String strClassID, String strOprID, int fILETYPE, int numLimit, int numStart,
			String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		int total = 0;
		try {

			// 报名表编号;
			long appInsId = 0L;
			appInsId = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
					new Object[] { strClassID, strOprID }, "Long");

			String TZ_TITLE = "", TZ_COM_LMC = "";

			String strfileDate = "";
			int numFileID = 0;
			
			numFileID = numFileID + 1;

			String fileSql = "SELECT A.ATTACHUSERFILE,A.ATTACHSYSFILENAME,A.TZ_ACCESS_PATH,B.TZ_XXX_MC FROM PS_TZ_FSZL_T A,PS_TZ_FORM_ATT2_T B WhERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH =A.ATTACHSYSFILENAME AND A.TZ_APP_INS_ID =? and A.FILETYPE=? ORDER BY A.ROW_ADDED_DTTM";
			List<Map<String, Object>> list2 = jdbcTemplate.queryForList(fileSql,
					new Object[] { appInsId, fILETYPE});
			if (list2 != null && list2.size() > 0) {
				String fileName = "", sysFileName = "", accessUrl = "";
				strfileDate = "";
				//int fileNum = 1;
				for (int j = 0; j < list2.size(); j++) {
					fileName = (String) list2.get(j).get("TZ_XXX_MC");
					sysFileName = (String) list2.get(j).get("ATTACHSYSFILENAME");
					accessUrl = (String) list2.get(j).get("TZ_ACCESS_PATH");

					if (fileName != null && !"".equals(fileName) && sysFileName != null
							&& !"".equals(sysFileName) && accessUrl != null && !"".equals(accessUrl)) {
						if (accessUrl.lastIndexOf("/") + 1 == accessUrl.length()) {
							accessUrl = request.getContextPath() + accessUrl + sysFileName;
						} else {
							accessUrl = request.getContextPath() + accessUrl + "/" + sysFileName;
						}
					} else {
						accessUrl = "";
					}
					if (fileName == null) {
						fileName = "";
					}

					//if (fileNum == 1) {
						strfileDate = tzGdObject.getHTMLText(
								"HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML",fileName,
								accessUrl, String.valueOf(numFileID));
					/*} else {
						strfileDate = strfileDate + "<br>"
								+ tzGdObject.getHTMLText(
										"HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML",fileName,
										accessUrl, String.valueOf(numFileID));
					}*/

					// xuhao = xuhao + 1;
					//fileNum = fileNum + 1;
					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("appInsId", appInsId);
					jsonmap.put("strfileDate", strfileDate);
					jsonmap.put("xuhao", numFileID);
					jsonmap.put("TZ_XXX_BH", sysFileName);
					jsonmap.put("TZ_XXX_MC", TZ_TITLE);
					jsonmap.put("fileLinkName", "");
					jsonmap.put("TZ_COM_LMC", TZ_COM_LMC);
					total++;
					listData.add(jsonmap);
				}
			}

			strfileDate = "";
			String fileName = "", sysFileName = "", accessUrl = "";
			/* 后台管理员上传的附件lastIndexOf */
			/*TZ_COM_LMC = "imagesUpload";
			String fileSql2 = "SELECT A.TZ_XXX_BH,ATTACHUSERFILE,ATTACHSYSFILENAME,C.TZ_XXX_MC,A.TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T A ,PS_TZ_FORM_ATT2_T C WhERE A.TZ_APP_INS_ID =? AND A.TZ_XXX_BH NOT IN (SELECT TEMP.TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_T TEMP left join PS_TZ_APP_XXXPZ_T APP on TEMP.TZ_APP_TPL_ID = APP.TZ_APP_TPL_ID AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH AND TEMP.TZ_APP_TPL_ID = ?) AND A.TZ_XXX_BH=C.TZ_XXX_BH AND C.TZ_APP_INS_ID = ? ORDER BY A.TZ_XXX_BH";
			List<Map<String, Object>> list3 = jdbcTemplate.queryForList(fileSql2,
					new Object[] { appInsId, TZ_APP_TPL_ID, appInsId });
			if (list3 != null && list3.size() > 0) {
				for (int k = 0; k < list3.size(); k++) {
					TZ_XXX_BH = (String) list3.get(k).get("TZ_XXX_BH");
					fileName = (String) list3.get(k).get("ATTACHUSERFILE");
					sysFileName = (String) list3.get(k).get("ATTACHSYSFILENAME");
					TZ_TITLE = (String) list3.get(k).get("TZ_XXX_MC");
					accessUrl = (String) list3.get(k).get("TZ_ACCESS_PATH");

					if (fileName == null) {
						fileName = "";
					}

					if (fileName != null && !"".equals(fileName) && sysFileName != null && !"".equals(sysFileName)
							&& accessUrl != null && !"".equals(accessUrl)) {
						if (accessUrl.lastIndexOf("/") + 1 == accessUrl.length()) {
							accessUrl = request.getContextPath() + accessUrl + sysFileName;
						} else {
							accessUrl = request.getContextPath() + accessUrl + "/" + sysFileName;
						}
					} else {
						accessUrl = "";
					}

					strfileDate = tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML", fileName, accessUrl, String.valueOf(numFileID));

					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("appInsId", appInsId);
					jsonmap.put("strfileDate", strfileDate);
					jsonmap.put("xuhao", numFileID);
					jsonmap.put("TZ_XXX_BH", TZ_XXX_BH);
					jsonmap.put("TZ_XXX_MC", TZ_TITLE);
					jsonmap.put("fileLinkName", "");
					jsonmap.put("TZ_COM_LMC", TZ_COM_LMC);
					total++;
					listData.add(jsonmap);
				}
			}*/
			/* 后台管理员上传的附件end */
			mapRet.replace("total", total);
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 获取递交资料审核信息列表 */
	public String tzQueryFileCheckList(String strClassID, String strOprID, int numLimit, int numStart,
			String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 递交资料编号，内容简介，备注，审核状态，审核不通过原因，报名表编号;
			String strFileID = "", strContentIntro = "", strRemark = "", strAuditState = "", strFailedReason = "";
			long strAppInsID = 0L;
			strAppInsID = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
					new Object[] { strClassID, strOprID }, "Long");
			String sql = "";
			List<Map<String, Object>> list;
			if (numLimit > 0) {
				sql = "SELECT TZ_SBMINF_ID,TZ_CONT_INTRO,TZ_REMARK FROM PS_TZ_CLS_DJZL_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { strClassID, numStart, numLimit });
			} else {
				sql = "SELECT TZ_SBMINF_ID,TZ_CONT_INTRO,TZ_REMARK FROM PS_TZ_CLS_DJZL_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM";
				list = jdbcTemplate.queryForList(sql, new Object[] { strClassID });
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					strAuditState = "";
					strFailedReason = "";
					strFileID = (String) list.get(i).get("TZ_SBMINF_ID");
					strContentIntro = (String) list.get(i).get("TZ_CONT_INTRO");
					strRemark = (String) list.get(i).get("TZ_REMARK");
					Map<String, Object> zlshMap = jdbcTemplate.queryForMap(
							"SELECT TZ_ZL_AUDIT_STATUS,TZ_AUDIT_NOPASS_RS FROM PS_TZ_FORM_ZLSH_T WHERE TZ_APP_INS_ID=? AND TZ_SBMINF_ID=?",
							new Object[] { strAppInsID, strFileID });
					if (zlshMap != null) {
						strAuditState = (String) zlshMap.get("TZ_ZL_AUDIT_STATUS");
						strFailedReason = (String) zlshMap.get("TZ_AUDIT_NOPASS_RS");
					}
					if (strAuditState == null) {
						strAuditState = "";
					}

					if (strFailedReason == null) {
						strFailedReason = "";
					}

					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("classID", strClassID);
					jsonmap.put("oprID", strOprID);
					jsonmap.put("appInsID", strAppInsID);
					jsonmap.put("fileID", strFileID);
					jsonmap.put("intro", strContentIntro);
					jsonmap.put("remark", strRemark);
					jsonmap.put("auditState", strAuditState);
					jsonmap.put("failedReason", strFailedReason);
					listData.add(jsonmap);

				}
			}
			// 获取总数;
			int numTotal = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_CLS_DJZL_T WHERE TZ_CLASS_ID=?",
					new Object[] { strClassID }, "Integer");
			mapRet.replace("total", numTotal);
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 报名流程设置列表 */
	public String tzQueryBmlcList(String strClassID, String strOprID, int numLimit, int numStart, String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 报名表编号;
			long strAppInsID = 0L;
			strAppInsID = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
					new Object[] { strClassID, strOprID }, "Long");
			String sql = "";
			List<Map<String, Object>> list;
			if (numLimit > 0) {
				sql = "SELECT TZ_APPPRO_ID,TZ_APPPRO_NAME FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { strClassID, numStart, numLimit });
			} else {
				sql = "SELECT TZ_APPPRO_ID,TZ_APPPRO_NAME FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM";
				list = jdbcTemplate.queryForList(sql, new Object[] { strClassID });
			}
			String str_lcid = "", str_lcname = "";
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					str_lcid = (String) list.get(i).get("TZ_APPPRO_ID");
					str_lcname = (String) list.get(i).get("TZ_APPPRO_NAME") == null ? ""
							: (String) list.get(i).get("TZ_APPPRO_NAME");

					String str_color_id = "", str_fb_desc = "";
					Map<String, Object> approRstMap = jdbcTemplate.queryForMap(
							"SELECT TZ_APPPRO_HF_BH,TZ_APPPRO_RST FROM PS_TZ_APPPRO_RST_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APP_INS_ID=?",
							new Object[] { strClassID, str_lcid, strAppInsID });
					if (approRstMap != null) {
						str_color_id = (String) approRstMap.get("TZ_APPPRO_HF_BH");
						str_fb_desc = (String) approRstMap.get("TZ_APPPRO_RST") == null ? ""
								: (String) approRstMap.get("TZ_APPPRO_RST");
					}

					Map<String, Object> clBmMap = new HashMap<>();
					if (str_color_id == null || "".equals(str_color_id)) {
						clBmMap = jdbcTemplate.queryForMap(
								"SELECT TZ_APPPRO_COLOR,TZ_CLS_RESULT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_WFB_DEFALT_BZ='on'",
								new Object[] { strClassID, str_lcid });
					} else {
						clBmMap = jdbcTemplate.queryForMap(
								"SELECT TZ_APPPRO_COLOR,TZ_CLS_RESULT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH=?",
								new Object[] { strClassID, str_lcid, str_color_id });
					}
					if (clBmMap != null) {
						str_color_id = (String) clBmMap.get("TZ_APPPRO_COLOR");
						str_fb_desc = (String) clBmMap.get("TZ_CLS_RESULT") == null ? ""
								: (String) clBmMap.get("TZ_CLS_RESULT");
						str_fb_desc = str_fb_desc.replaceAll("<p>", "");
						str_fb_desc = str_fb_desc.replaceAll("</p>", "");
					}
					if (str_color_id == null) {
						str_color_id = "";
					}
					if (str_fb_desc == null) {
						str_fb_desc = "";
					}

					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("classID", strClassID);
					jsonmap.put("oprID", strOprID);
					jsonmap.put("bmlcID", str_lcid);
					jsonmap.put("bmb_id", strAppInsID);
					jsonmap.put("bmlcName", str_lcname);
					jsonmap.put("colorCode", str_color_id + ",," + str_fb_desc);
					listData.add(jsonmap);

				}
			}
			// 获取总数;
			int numTotal = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_CLS_DJZL_T WHERE TZ_CLASS_ID=?",
					new Object[] { strClassID }, "Integer");
			mapRet.replace("total", numTotal);
			mapRet.replace("root", listData);
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 获取推荐信信息列表 */
	public String tzQueryRefLetterList(String strClassID, String strOprID, int numLimit, int numStart,
			String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		int numTotal = 0;
		try {
			long strAppInsID = 0L;
			String strRefLetterID = "";
			long strRefLetterAppInsID = 0L;
			String strRefLetterPerId = "";
			String strRefLetterPerName = "";
			String str_tjr_gname = "";
			String str_tjr_title = "";
			String str_name_suff = "";
			String strRefLetterPerEmail = "";
			String strRefLetterPerPhoneArea = "";
			String strRefLetterPerPhone = "";
			String strRefLetterPerSex = "";
			String strRefLetterPerSexDesc = "";

			String strRefLetterState = "";
			String str_refLetterSysFile = "";
			String str_refLetterUserFile = "";

			String blankSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
			String str_none_blank = jdbcTemplate.queryForObject(blankSql, new Object[] { "TZ_REF_TITLE_NONE_BLANK" },
					"String");

			strAppInsID = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
					new Object[] { strClassID, strOprID }, "Long");

			String sql = "SELECT TZ_REF_LETTER_ID,TZ_TJX_APP_INS_ID,TZ_TJR_ID,TZ_TJX_TITLE,TZ_REFERRER_NAME,TZ_REFERRER_GNAME,TZ_EMAIL,TZ_PHONE_AREA,TZ_PHONE,TZ_GENDER,ATTACHSYSFILENAME,ATTACHUSERFILE,TZ_ACCESS_PATH FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID= ? AND OPRID = ? AND TZ_MBA_TJX_YX = 'Y' ORDER BY TZ_TJR_ID";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strAppInsID, strOprID });
			strRefLetterAppInsID = 0L;
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					strRefLetterID = (String) list.get(i).get("TZ_REF_LETTER_ID");
					if (list.get(i).get("TZ_TJX_APP_INS_ID") != null) {
						strRefLetterAppInsID = Long.parseLong(list.get(i).get("TZ_TJX_APP_INS_ID").toString());
					} else {
						strRefLetterAppInsID = 0L;
					}
					String str_att_a_url = "";
					String str_att_p_url = "";
					strRefLetterPerId = (String) list.get(i).get("TZ_TJR_ID");
					str_tjr_title = (String) list.get(i).get("TZ_TJX_TITLE");
					strRefLetterPerName = (String) list.get(i).get("TZ_REFERRER_NAME");
					str_tjr_gname = (String) list.get(i).get("TZ_REFERRER_GNAME");
					strRefLetterPerEmail = (String) list.get(i).get("TZ_EMAIL");
					strRefLetterPerPhoneArea = (String) list.get(i).get("TZ_PHONE_AREA");
					strRefLetterPerPhone = (String) list.get(i).get("TZ_PHONE");
					strRefLetterPerSex = (String) list.get(i).get("TZ_GENDER");
					str_refLetterSysFile = (String) list.get(i).get("ATTACHSYSFILENAME");
					str_refLetterUserFile = (String) list.get(i).get("ATTACHUSERFILE");
					str_att_p_url = (String) list.get(i).get("TZ_ACCESS_PATH");

					str_name_suff = "";
					if (str_tjr_title != null && !"".equals(str_tjr_title) && !str_tjr_title.equals(str_none_blank)) {
						str_name_suff = str_tjr_title;
					}

					if (str_tjr_gname != null && !"".equals(str_tjr_gname)) {
						if (str_name_suff != null && !"".equals(str_name_suff)) {
							str_name_suff = str_name_suff + " " + str_tjr_gname;
						} else {
							str_name_suff = str_tjr_gname;
						}
					}

					if (str_name_suff != null && !"".equals(str_name_suff)) {
						strRefLetterPerName = str_name_suff + " " + strRefLetterPerName;
					}

					if (strRefLetterPerPhone != null && !"".equals(strRefLetterPerPhone)
							&& strRefLetterPerPhoneArea != null && !"".equals(strRefLetterPerPhoneArea)) {
						strRefLetterPerPhone = strRefLetterPerPhoneArea + "-" + strRefLetterPerPhone;
					}

					if (str_refLetterSysFile != null && !"".equals(str_refLetterSysFile)
							&& str_refLetterUserFile != null && !"".equals(str_refLetterUserFile)
							&& str_att_p_url != null && !"".equals(str_att_p_url)) {

						if (str_att_p_url.lastIndexOf("/") + 1 != str_att_p_url.length()) {
							str_att_a_url = str_att_p_url + "/" + str_refLetterSysFile;
						} else {
							str_att_a_url = str_att_p_url + str_refLetterSysFile;
						}
					}

					numTotal = numTotal + 1;
					strRefLetterState = jdbcTemplate.queryForObject(
							"SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T  WHERE TZ_APP_INS_ID = ?",
							new Object[] { strRefLetterAppInsID }, "String");

					strRefLetterPerSexDesc = jdbcTemplate.queryForObject(
							"select TZ_ZHZ_DMS from PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_GENDER' AND TZ_ZHZ_ID = ? AND TZ_EFF_STATUS = 'A'",
							new Object[] { strRefLetterPerSex }, "String");

					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("oprID", strOprID);
					jsonmap.put("appInsId", strAppInsID);
					jsonmap.put("refLetterId", strRefLetterID);
					jsonmap.put("refLetterAppInsId", strRefLetterAppInsID);
					jsonmap.put("refLetterPerId", strRefLetterPerId);
					jsonmap.put("refLetterPerName", strRefLetterPerName);
					jsonmap.put("refLetterPerEmail", strRefLetterPerEmail);
					jsonmap.put("refLetterPerPhone", strRefLetterPerPhone);
					jsonmap.put("refLetterPerSex", strRefLetterPerSexDesc);
					jsonmap.put("refLetterSysFile", str_refLetterSysFile);
					jsonmap.put("refLetterUserFile", str_refLetterUserFile);
					jsonmap.put("refLetterState", strRefLetterState);
					jsonmap.put("refLetterAurl", str_att_a_url);
					jsonmap.put("refLetterPurl", str_att_p_url);
					listData.add(jsonmap);

				}
			}
			mapRet.replace("total", numTotal);
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 获取附件列表 */
	public String tzQueryAttachmentList(String strClassID, String strOprID, int numLimit, int numStart,
			String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		int total = 0;
		try {

			// 报名表编号;
			long appInsId = 0L;
			appInsId = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
					new Object[] { strClassID, strOprID }, "Long");

			String TZ_XXX_BH = "", TZ_TITLE = "", TZ_COM_LMC = "", TZ_APP_TPL_ID = "";

			TZ_APP_TPL_ID = jdbcTemplate.queryForObject(
					"SELECT C.TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T C WHERE C.TZ_APP_INS_ID=?", new Object[] { appInsId },
					"String");

			String strfileDate = "";
			String sql = "SELECT DISTINCT TEMP.TZ_XXX_NO,APP.TZ_XXX_MC,APP.TZ_COM_LMC  FROM PS_TZ_TEMP_FIELD_T TEMP ,PS_TZ_APP_XXXPZ_T APP WHERE TEMP.TZ_APP_TPL_ID = APP.TZ_APP_TPL_ID AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH AND APP.TZ_XXX_CCLX='F' AND APP.TZ_APP_TPL_ID = ? ORDER BY TEMP.TZ_XXX_NO";
			int numFileID = 0;
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { TZ_APP_TPL_ID });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					TZ_XXX_BH = (String) list.get(i).get("TZ_XXX_NO");
					TZ_TITLE = (String) list.get(i).get("TZ_XXX_MC");
					TZ_COM_LMC = (String) list.get(i).get("TZ_COM_LMC");
					if (!"AttachmentUpload".equals(TZ_COM_LMC) && !"imagesUpload".equals(TZ_COM_LMC)) {
						continue;
					}
					numFileID = numFileID + 1;

					String fileName = "", sysFileName = "", accessUrl = "";
					strfileDate = "";
					int fileNum = 1;
					// int xuhao = 1;

					String fileSql = "SELECT ATTACHUSERFILE,ATTACHSYSFILENAME,TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T WhERE TZ_APP_INS_ID =? AND FILETYPE <> 1 AND TZ_XXX_BH IN (SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO=?) ORDER BY TZ_INDEX";
					List<Map<String, Object>> list2 = jdbcTemplate.queryForList(fileSql,
							new Object[] { appInsId, TZ_APP_TPL_ID, TZ_XXX_BH });
					if (list2 != null && list2.size() > 0) {
						for (int j = 0; j < list2.size(); j++) {
							fileName = (String) list2.get(j).get("ATTACHUSERFILE");
							sysFileName = (String) list2.get(j).get("ATTACHSYSFILENAME");
							accessUrl = (String) list2.get(j).get("TZ_ACCESS_PATH");

							if (fileName != null && !"".equals(fileName) && sysFileName != null
									&& !"".equals(sysFileName) && accessUrl != null && !"".equals(accessUrl)) {
								if (accessUrl.lastIndexOf("/") + 1 == accessUrl.length()) {
									accessUrl = request.getContextPath() + accessUrl + sysFileName;
								} else {
									accessUrl = request.getContextPath() + accessUrl + "/" + sysFileName;
								}
							} else {
								accessUrl = "";
							}
							if (fileName == null) {
								fileName = "";
							}

							if (fileNum == 1) {
								strfileDate = tzGdObject.getHTMLText(
										"HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML",fileName,
										accessUrl, String.valueOf(numFileID));
							} else {
								strfileDate = strfileDate + "<br>"
										+ tzGdObject.getHTMLText(
												"HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML",fileName,
												accessUrl, String.valueOf(numFileID));
							}

							// xuhao = xuhao + 1;
							fileNum = fileNum + 1;
						}
					}

					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("appInsId", appInsId);
					jsonmap.put("strfileDate", strfileDate);
					jsonmap.put("xuhao", numFileID);
					jsonmap.put("TZ_XXX_BH", "BMBFILE");
					jsonmap.put("TZ_XXX_MC", TZ_TITLE);
					jsonmap.put("fileLinkName", "");
					jsonmap.put("TZ_COM_LMC", TZ_COM_LMC);
					total++;
					listData.add(jsonmap);
				}
			}

			strfileDate = "";
			String fileName = "", sysFileName = "", accessUrl = "";
			/* 后台管理员上传的附件lastIndexOf */
			TZ_COM_LMC = "imagesUpload";
			String fileSql2 = "SELECT A.TZ_XXX_BH,ATTACHUSERFILE,ATTACHSYSFILENAME,C.TZ_XXX_MC,A.TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T A ,PS_TZ_FORM_ATT2_T C WhERE A.TZ_APP_INS_ID =? AND FILETYPE <> 1 AND A.TZ_XXX_BH NOT IN (SELECT TEMP.TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_T TEMP left join PS_TZ_APP_XXXPZ_T APP on TEMP.TZ_APP_TPL_ID = APP.TZ_APP_TPL_ID AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH AND TEMP.TZ_APP_TPL_ID = ?) AND A.TZ_XXX_BH=C.TZ_XXX_BH AND C.TZ_APP_INS_ID = ? ORDER BY A.TZ_XXX_BH";
			List<Map<String, Object>> list3 = jdbcTemplate.queryForList(fileSql2,
					new Object[] { appInsId, TZ_APP_TPL_ID, appInsId });
			if (list3 != null && list3.size() > 0) {
				for (int k = 0; k < list3.size(); k++) {
					TZ_XXX_BH = (String) list3.get(k).get("TZ_XXX_BH");
					fileName = (String) list3.get(k).get("ATTACHUSERFILE");
					sysFileName = (String) list3.get(k).get("ATTACHSYSFILENAME");
					TZ_TITLE = (String) list3.get(k).get("TZ_XXX_MC");
					accessUrl = (String) list3.get(k).get("TZ_ACCESS_PATH");

					if (fileName == null) {
						fileName = "";
					}

					if (fileName != null && !"".equals(fileName) && sysFileName != null && !"".equals(sysFileName)
							&& accessUrl != null && !"".equals(accessUrl)) {
						if (accessUrl.lastIndexOf("/") + 1 == accessUrl.length()) {
							accessUrl = request.getContextPath() + accessUrl + sysFileName;
						} else {
							accessUrl = request.getContextPath() + accessUrl + "/" + sysFileName;
						}
					} else {
						accessUrl = "";
					}

					strfileDate = tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML", fileName, accessUrl, String.valueOf(numFileID));

					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("appInsId", appInsId);
					jsonmap.put("strfileDate", strfileDate);
					jsonmap.put("xuhao", numFileID);
					jsonmap.put("TZ_XXX_BH", TZ_XXX_BH);
					jsonmap.put("TZ_XXX_MC", TZ_TITLE);
					jsonmap.put("fileLinkName", "");
					jsonmap.put("TZ_COM_LMC", TZ_COM_LMC);
					total++;
					listData.add(jsonmap);
				}
			}
			/* 后台管理员上传的附件end */
			mapRet.replace("total", total);
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	/*
	 * private String getUrlImages(long appInsId, String TZ_XXX_BH, String
	 * sysFileName, String[] errorMsg) { String urlReturn = "";
	 * 
	 * String attUrlSQL =
	 * "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
	 * String fileUrl = jdbcTemplate.queryForObject(attUrlSQL, new Object[] {
	 * "TZ_AFORM_FILE_DIR" }, "String"); if ((fileUrl.lastIndexOf("/") + 1) !=
	 * fileUrl.length()) { fileUrl = fileUrl + "/"; } urlReturn = fileUrl +
	 * sysFileName; return urlReturn; }
	 * 
	 * private String getFiles(long appInsId, String TZ_XXX_BH, String
	 * sysFileName, String[] errorMsg) { String urlReturn = "";
	 * 
	 * String attUrlSQL =
	 * "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
	 * String fileUrl = jdbcTemplate.queryForObject(attUrlSQL, new Object[] {
	 * "TZ_AFORM_FILE_DIR" }, "String"); if(fileUrl != null &&
	 * !"".equals(fileUrl)){ if ((fileUrl.lastIndexOf("/") + 1) !=
	 * fileUrl.length()) { fileUrl = fileUrl + "/"; } }else{ fileUrl = ""; }
	 * 
	 * urlReturn = "http://" + request.getServerName() + ":" +
	 * request.getServerPort() + request.getContextPath() + fileUrl +
	 * sysFileName; return urlReturn; }
	 */

	/*
	 * private String getRefLetterFiles(long strAppInsID,String sysFileName) {
	 * String urlReturn = "";
	 * 
	 * 
	 * String attUrlSQL =
	 * "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
	 * String fileUrl = jdbcTemplate.queryForObject(attUrlSQL, new Object[] {
	 * "TZ_AFORM_FILE_DIR" }, "String"); if ((fileUrl.lastIndexOf("/") + 1) !=
	 * fileUrl.length()) { fileUrl = fileUrl + "/"; }
	 * 
	 * urlReturn = "http://" + request.getServerName() + ":" +
	 * request.getServerPort() + request.getContextPath() + fileUrl +
	 * sysFileName; return urlReturn; }
	 */

	/* 修改学生类别信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strRet;
		}

		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String strAppInsID = "";
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				if (jacksonUtil.containsKey("appInsID")) {
					strAppInsID = jacksonUtil.getString("appInsID");
				}

				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");

				if ("STU".equals(strFlag)) {
					/* 报名人信息 */
					strRet = this.tzEditStuInfo(infoData, errMsg);
				}
				if ("FILE".equals(strFlag)) {
					/* 递交资料审核列表信息 */
					strRet = this.tzEditFileCheckInfo(infoData, errMsg);
				}
				if ("REFLETTER".equals(strFlag)) {
					/* 推荐信列表信息 */
					strRet = this.tzEditRefLetterInfo(infoData, errMsg);
				}
				if ("ADDREFLETTER".equals(strFlag)) {
					/* 推荐信列表信息 */
					strRet = this.tzAddRefLetterInfo(infoData, errMsg);
				}
				if ("CONTACT".equals(strFlag)) {
					/* 联系方式信息 */
					strRet = this.tzEditContactInfo(strAppInsID, infoData, errMsg);
				}
				if ("MORE".equals(strFlag)) {
					/* 更多信息 */
					strRet = this.tzEditMoreInfo(strAppInsID, infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 其它操作数据的方法 */
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		if ("getMoreInfoItems".equals(oprType)) {
			try {
				String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				jacksonUtil.json2Map(strParams);
				// 报名表编号;
				String strAppInsID = jacksonUtil.getString("appInsID");
				String str_sx_id = "", str_sx_name = "", str_sx_type = "";
				String sql = "SELECT TZ_ATTRIBUTE_ID,TZ_ATTRIBUTE_NAME,TZ_CONTROL_TYPE FROM PS_TZ_FORM_ATTR_T  WHERE TZ_JG_ID=? AND TZ_IS_USED='Y'";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { str_jg_id });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						str_sx_id = (String) list.get(i).get("TZ_ATTRIBUTE_ID");
						str_sx_name = (String) list.get(i).get("TZ_ATTRIBUTE_NAME");
						str_sx_type = (String) list.get(i).get("TZ_CONTROL_TYPE");

						String str_value = jdbcTemplate.queryForObject(
								"SELECT TZ_ATTRIBUTE_VALUE FROM PS_TZ_FRM_MORINF_T WHERE TZ_APP_INS_ID=? AND TZ_ATTRIBUTE_ID=? LIMIT 0,1",
								new Object[] { Long.parseLong(strAppInsID), str_sx_id }, "String");
						Map<String, Object> map1 = new HashMap<>();
						map1.put("show_name", str_sx_name);
						map1.put("fldType", str_sx_type);
						map1.put("zd_name", str_value);

						mapRet.put(str_sx_id, map1);
					}
				}
			} catch (Exception e) {
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
		}
			
		if ("PWD".equals(oprType)) {
				try {
					jacksonUtil.json2Map(strParams);
					// 报名表实例编号
					Long numAppInsId = 0L;
					// 报名表编号;
					String strAppTjxInsID = jacksonUtil.getString("appInsID");
					String strAppTjxPwd = jacksonUtil.getString("password");
					
					numAppInsId = Long.parseLong(strAppTjxInsID);
					//System.out.println("numAppInsId" + numAppInsId);
					//System.out.println("password" + strAppTjxPwd);
					// 密码用MD5加密存储
					if (strAppTjxPwd != null && !strAppTjxPwd.equals("")) {
						strAppTjxPwd = Sha3DesMD5.md5(strAppTjxPwd);
						PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
						psTzAppInsT.setTzAppInsId(numAppInsId);
						psTzAppInsT.setTzPwd(strAppTjxPwd);
						psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
					}
				}catch (Exception e) {
					errorMsg[0] = "1";
					errorMsg[1] = "修改密码失败" + e.toString();
				}
			}
		if("queryFile".equals(oprType)) {
			return queryFile(strParams);
		}

		Map<String, Object> mapRet2 = new HashMap<String, Object>();
		mapRet2.put("formData", mapRet);

		return jacksonUtil.Map2json(mapRet2);
	}

	//学生端查询复试资料
	private String queryFile(String strParams) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		int total = 0;
		try {
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 报名表编号;
			long appInsId = 0L;
			appInsId = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
					new Object[] { classId, oprid }, "Long");

			String TZ_TITLE = "", TZ_COM_LMC = "";

			String strfileDate = "";
			int numFileID = 0;
			
			numFileID = numFileID + 1;

			String fileSql = "SELECT A.ATTACHUSERFILE,A.ATTACHSYSFILENAME,A.TZ_ACCESS_PATH,B.TZ_XXX_MC FROM PS_TZ_FSZL_T A,PS_TZ_FORM_ATT2_T B WhERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH =A.ATTACHSYSFILENAME AND A.TZ_APP_INS_ID =? and A.FILETYPE=1 ORDER BY A.ROW_ADDED_DTTM";
			List<Map<String, Object>> list2 = jdbcTemplate.queryForList(fileSql,
					new Object[] { appInsId});
			if (list2 != null && list2.size() > 0) {
				String fileName = "", sysFileName = "", accessUrl = "";
				strfileDate = "";
				//int fileNum = 1;
				for (int j = 0; j < list2.size(); j++) {
					fileName = (String) list2.get(j).get("TZ_XXX_MC");
					sysFileName = (String) list2.get(j).get("ATTACHSYSFILENAME");
					accessUrl = (String) list2.get(j).get("TZ_ACCESS_PATH");

					if (fileName != null && !"".equals(fileName) && sysFileName != null
							&& !"".equals(sysFileName) && accessUrl != null && !"".equals(accessUrl)) {
						if (accessUrl.lastIndexOf("/") + 1 == accessUrl.length()) {
							accessUrl = request.getContextPath() + accessUrl + sysFileName;
						} else {
							accessUrl = request.getContextPath() + accessUrl + "/" + sysFileName;
						}
					} else {
						accessUrl = "";
					}
					if (fileName == null) {
						fileName = "";
					}

					//if (fileNum == 1) {
						strfileDate = tzGdObject.getHTMLText(
								"HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML",fileName,
								accessUrl, String.valueOf(numFileID));
					/*} else {
						strfileDate = strfileDate + "<br>"
								+ tzGdObject.getHTMLText(
										"HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML",fileName,
										accessUrl, String.valueOf(numFileID));
					}*/

					// xuhao = xuhao + 1;
					//fileNum = fileNum + 1;
					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("appInsId", appInsId);
					jsonmap.put("strfileDate", strfileDate);
					jsonmap.put("xuhao", numFileID);
					jsonmap.put("TZ_XXX_BH", sysFileName);
					jsonmap.put("TZ_XXX_MC", TZ_TITLE);
					jsonmap.put("fileLinkName", "");
					jsonmap.put("TZ_COM_LMC", TZ_COM_LMC);
					jsonmap.put("fileName", fileName);
					total++;
					listData.add(jsonmap);
				}
			}

			strfileDate = "";

			mapRet.replace("total", total);
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	// 流程预览;
	@Override
	public String tzGetHtmlContent(String strParams) {
		String returnHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");

			String oprId = "";
			if (jacksonUtil.containsKey("oprId")) {
				oprId = jacksonUtil.getString("oprId");
			} else {
				oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
			}

			// 根据siteid得到机构id;
			String str_jg_id = "";
			String strCssDir = "";
			String strSiteId = "";
			// language;
			String language = "";

			str_jg_id = jdbcTemplate.queryForObject("select TZ_JG_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?",
					new Object[] { classId }, "String");

			String siteSQL = "select TZ_SITEI_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=?  and TZ_SITEI_ENABLE='Y' limit 0,1";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { str_jg_id });
			if (siteMap != null) {
				strSiteId = (String) siteMap.get("TZ_SITEI_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();

				if ("".equals(skinstor) || skinstor == null) {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + "style_" + str_jg_id.toLowerCase() + ".css";
				} else {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + skinstor + "/" + "style_" + str_jg_id.toLowerCase() + ".css";
				}
			}

			// 步骤;
			String step = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "104", language, "步骤",
					"step");

			// 查看该人员是否已经申请了该班级的报名表;
			long TZ_SORT_NUM = 0;
			String TZ_APPPRO_ID = "", TZ_APPPRO_NAME = "", TZ_APPPRO_HF_BH = "", TZ_APPPRO_RST = "";
			String bmlcStepClass = "", TZ_BJ_BM_LC_STEP_DIV = "";
			long TZ_APP_INS_ID = jdbcTemplate.queryForObject(
					"select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where TZ_CLASS_ID=? and OPRID=?",
					new Object[] { classId, oprId }, "Long");
			List<Map<String, Object>> list;
			String sql = "";
			if (TZ_APP_INS_ID > 0) {
				sql = "select a.TZ_SORT_NUM,a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME,b.TZ_APPPRO_HF_BH,b.TZ_APPPRO_RST from PS_TZ_CLS_BMLC_T a left join (select * from PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID=? and TZ_CLASS_ID=?) b on a.TZ_APPPRO_ID=b.TZ_APPPRO_ID where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
				list = jdbcTemplate.queryForList(sql, new Object[] { TZ_APP_INS_ID, classId, classId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						TZ_SORT_NUM = (long) list.get(i).get("TZ_SORT_NUM");
						TZ_APPPRO_ID = (String) list.get(i).get("TZ_APPPRO_ID");
						TZ_APPPRO_NAME = (String) list.get(i).get("TZ_APPPRO_NAME");
						TZ_APPPRO_HF_BH = (String) list.get(i).get("TZ_APPPRO_HF_BH");
						TZ_APPPRO_RST = (String) list.get(i).get("TZ_APPPRO_RST");

						String timelineClass = "timeline-yuan2_i";

						if (TZ_APPPRO_HF_BH == null || "".equals(TZ_APPPRO_HF_BH)) {
							TZ_APPPRO_RST = jdbcTemplate.queryForObject(
									"select TZ_APPPRO_CONTENT from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=? and TZ_WFB_DEFALT_BZ='on'",
									new Object[] { classId, TZ_APPPRO_ID }, "String");
							timelineClass = "timeline-yuan1_i";
							if (TZ_APPPRO_RST == null) {
								TZ_APPPRO_RST = "";
							}
						} else {
							// 如果有值，查看是不是默认回复语;
							int isDef = jdbcTemplate.queryForObject(
									"select count(1) from PS_TZ_CLS_BMLCHF_T where TZ_WFB_DEFALT_BZ='on' and TZ_CLASS_ID=? and TZ_APPPRO_HF_BH=?",
									new Object[] { classId, TZ_APPPRO_HF_BH }, "Integer");
							if (isDef > 0) {
								timelineClass = "timeline-yuan1_i";
							}
						}
						if ((TZ_SORT_NUM % 2) == 1) {
							bmlcStepClass = "indexbm-lc-step1";
						} else {
							bmlcStepClass = "indexbm-lc-step2";
						}
						TZ_BJ_BM_LC_STEP_DIV = TZ_BJ_BM_LC_STEP_DIV + tzGdObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_BJ_BM_LC_STEP_DIV",bmlcStepClass,
								step + "&nbsp;" + TZ_SORT_NUM, TZ_APPPRO_NAME, TZ_APPPRO_RST, timelineClass);
					}

				}
			} else {
				String bmlcSql2 = "select a.TZ_SORT_NUM,a.TZ_APPPRO_NAME,b.TZ_APPPRO_CONTENT from PS_TZ_CLS_BMLC_T a LEFT JOIN (select * from PS_TZ_CLS_BMLCHF_T where TZ_WFB_DEFALT_BZ='on') b ON  a.TZ_CLASS_ID=b.TZ_CLASS_ID and a.TZ_APPPRO_ID=b.TZ_APPPRO_ID where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
				list = jdbcTemplate.queryForList(bmlcSql2, new Object[] { classId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						TZ_SORT_NUM = (int) list.get(i).get("TZ_SORT_NUM");
						TZ_APPPRO_NAME = (String) list.get(i).get("TZ_APPPRO_NAME") == null ? ""
								: (String) list.get(i).get("TZ_APPPRO_NAME");
						TZ_APPPRO_RST = (String) list.get(i).get("TZ_APPPRO_CONTENT") == null ? ""
								: (String) list.get(i).get("TZ_APPPRO_CONTENT");

						if ((TZ_SORT_NUM % 2) == 1) {
							bmlcStepClass = "indexbm-lc-step1";
						} else {
							bmlcStepClass = "indexbm-lc-step2";
						}

						TZ_BJ_BM_LC_STEP_DIV = TZ_BJ_BM_LC_STEP_DIV + tzGdObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_BJ_BM_LC_STEP_DIV",bmlcStepClass,
								step + "&nbsp;" + TZ_SORT_NUM, TZ_APPPRO_NAME, TZ_APPPRO_RST, "timeline-yuan1_i");

					}

				}
			}

			if (TZ_BJ_BM_LC_STEP_DIV != null && !"".equals(TZ_BJ_BM_LC_STEP_DIV)) {
				returnHtml = tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_BMLC_PREVEIW_HTML", strCssDir, TZ_BJ_BM_LC_STEP_DIV);
			} else {
				returnHtml = tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_BMLC_PREVEIW_HTML", strCssDir, "未配置流程");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return returnHtml;
	}

	/* 删除上传附件 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strReturn = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strReturn;
		}

		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String TZ_XXX_BH = jacksonUtil.getString("TZ_XXX_BH");
				if (TZ_XXX_BH != null && !"".equals(TZ_XXX_BH)) {
					this.deleteFiles(TZ_XXX_BH, errMsg);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strReturn;
	}

	/* 删除上传附件 */
	private boolean deleteFiles(String TZ_XXX_BH, String[] errMsg) {
		boolean bolRet = true;
		try {
			jdbcTemplate.update("DELETE FROM PS_TZ_APPFATTACH_T WHERE ATTACHSYSFILENAME=?", new Object[] { TZ_XXX_BH });
			jdbcTemplate.update("DELETE FROM PS_TZ_FORM_ATT2_T WHERE TZ_XXX_BH=?", new Object[] { TZ_XXX_BH });
			jdbcTemplate.update("DELETE FROM PS_TZ_FORM_ATT_T WHERE ATTACHSYSFILENAME=?", new Object[] { TZ_XXX_BH });
			//删除复试资料
			jdbcTemplate.update("DELETE FROM PS_TZ_FSZL_T WHERE ATTACHSYSFILENAME=?", new Object[] { TZ_XXX_BH });
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return bolRet;
	}

	/* 修改报名人信息 */
	@SuppressWarnings("unchecked")
	private String tzEditStuInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		try {
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			// 班级编号;
			String strClassID = (String) infoData.get("classID");
			// oprid;
			String strOprID = (String) infoData.get("oprID");
			// 报名表编号;
			long strAppInsID = Long.parseLong((String) infoData.get("appInsID"));
			// 颜色类别编号;
			String strColorType = (String) infoData.get("colorType");
			// 报名表状态;
			String strAppFormState = (String) infoData.get("appFormState");
			// 评审状态;
			String strAuditState = (String) infoData.get("auditState");
			// 标签;
			ArrayList<String> arrTag = new ArrayList<>();
			if (infoData.get("tag") != null && !"".equals(infoData.get("tag"))) {
				arrTag = (ArrayList<String>) infoData.get("tag");
			}

			//如果是mpacc机构，项目是全日制的，审核通过时，向PS_TZ_MSPS_KSH_TBL插入数据,
			if("MPACC".equals(orgid.toUpperCase())) {
				//查询项目分类
				String sql = "SELECT A.TZ_PRJ_TYPE_NAME FROM PS_TZ_PRJ_TYPE_T A,PS_TZ_CLASS_INF_T B,PS_TZ_PRJ_INF_T C WHERE A.TZ_PRJ_TYPE_ID = C.TZ_PRJ_TYPE_ID AND C.TZ_PRJ_ID = B.TZ_PRJ_ID AND B.TZ_CLASS_ID=?";
				String TZ_PRJ_TYPE_NAME = jdbcTemplate.queryForObject(sql, new Object[] {strClassID}, "String");
				if("全日制".equals(TZ_PRJ_TYPE_NAME)) {
					if("A".equals(strAuditState)) {
						PsTzMsPskshTblKey key = new PsTzMsPskshTblKey();
						key.setTzClassId(strClassID);
						key.setTzApplyPcId("");
						key.setTzAppInsId(strAppInsID);
						PsTzMsPskshTbl psTzMsPskshTbl = psTzMsPskshTblMapper.selectByPrimaryKey(key);
						if(psTzMsPskshTbl == null) {
							psTzMsPskshTbl = new PsTzMsPskshTbl();
							psTzMsPskshTbl.setTzClassId(strClassID);
							psTzMsPskshTbl.setTzApplyPcId("");
							psTzMsPskshTbl.setTzAppInsId(strAppInsID);
							psTzMsPskshTblMapper.insertSelective(psTzMsPskshTbl);
						}					
					}else {
						PsTzMsPskshTblKey key = new PsTzMsPskshTblKey();
						key.setTzClassId(strClassID);
						key.setTzApplyPcId("");
						key.setTzAppInsId(strAppInsID);
						psTzMsPskshTblMapper.deleteByPrimaryKey(key);
					}
				}
			}
			//如果是MF机构，审核通过时，向PS_TZ_MSPS_KSH_TBL插入数据,
			if("MF".equals(orgid.toUpperCase())) {
				if("A".equals(strAuditState)) {
					PsTzMsPskshTblKey key = new PsTzMsPskshTblKey();
					key.setTzClassId(strClassID);
					key.setTzApplyPcId("");
					key.setTzAppInsId(strAppInsID);
					PsTzMsPskshTbl psTzMsPskshTbl = psTzMsPskshTblMapper.selectByPrimaryKey(key);
					if(psTzMsPskshTbl == null) {
						psTzMsPskshTbl = new PsTzMsPskshTbl();
						psTzMsPskshTbl.setTzClassId(strClassID);
						psTzMsPskshTbl.setTzApplyPcId("");
						psTzMsPskshTbl.setTzAppInsId(strAppInsID);
						psTzMsPskshTblMapper.insertSelective(psTzMsPskshTbl);
					}
				}else {
					PsTzMsPskshTblKey key = new PsTzMsPskshTblKey();
					key.setTzClassId(strClassID);
					key.setTzApplyPcId("");
					key.setTzAppInsId(strAppInsID);
					psTzMsPskshTblMapper.deleteByPrimaryKey(key);
				}
			}
			
			// 备注;
			String strRemark = (String) infoData.get("remark");
			// 短备注;
			String strShortRemark = (String) infoData.get("shortRemark");

			PsTzFormWrkTKey psTzFormWrkTKey = new PsTzFormWrkTKey();
			psTzFormWrkTKey.setTzClassId(strClassID);
			psTzFormWrkTKey.setOprid(strOprID);
			PsTzFormWrkT psTzFormWrkT = psTzFormWrkTMapper.selectByPrimaryKey(psTzFormWrkTKey);
			if (psTzFormWrkT != null) {
				psTzFormWrkT.setTzFormSpSta(strAuditState);
				psTzFormWrkT.setTzColorSortId(strColorType);
				psTzFormWrkT.setTzRemark(strRemark);
				psTzFormWrkT.setTzRemarkShort(strShortRemark);
				psTzFormWrkT.setRowLastmantDttm(new Date());
				psTzFormWrkT.setRowLastmantOprid(oprid);
				psTzFormWrkTMapper.updateByPrimaryKeySelective(psTzFormWrkT);
			}
			
			
			// 根据评审状态 修改/插入   表 TZ_IMP_MSZG_TBL中的TZ_RESULT_CODE字段
			// 首先根据报名表编号查询PsTzMSZGINFO对象是否已经存在
			/*
			PsTzMszgT p = pstzMszgTMapper.selectByPrimaryKey(strAppInsID);
			String sql = "select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T A, PS_TZ_APP_CC_T B ,PS_TZ_FORM_WRK_T C\n" + 
					"where A.TZ_BATCH_ID=B.TZ_APP_S_TEXT \n" + 
					"AND A.TZ_CLASS_ID=C.TZ_CLASS_ID\n" + 
					"AND B.TZ_APP_INS_ID=C.TZ_APP_INS_ID\n" + 
					"AND B.TZ_XXX_BH='grxxTZ_grxx_26CC_Batch'\n" + 
					"AND B.TZ_APP_INS_ID=?";
			
			String TZ_BATCH_NAME = jdbcTemplate.queryForObject(sql, new Object[] { strAppInsID }, "String");
			//如果对象不存在，做一个插入操作
			if(p == null) {	
				p = new PsTzMszgT();
				p.setTzAppInsId(strAppInsID);
				if("A".equals(strAuditState)) {
					p.setTzResultCode("是");
					p.setTzMsBatch(TZ_BATCH_NAME);
				}else if("B".equals(strAuditState)) {
					p.setTzResultCode("否");
					p.setTzMsBatch(null);
				}else if("N".equals(strAuditState)){
					p.setTzResultCode("等待");
					p.setTzMsBatch(null);
				}
				
				pstzMszgTMapper.insert(p);
			}else {	//如果对象存在，则做一个修改操作
				if("A".equals(strAuditState)) {
					p.setTzResultCode("是");
					p.setTzMsBatch(TZ_BATCH_NAME);
				}else if("B".equals(strAuditState)) {
					p.setTzResultCode("否");
					p.setTzMsBatch(null);
				}else if("N".equals(strAuditState)){
					p.setTzResultCode("等待");
					p.setTzMsBatch(null);
				}
				pstzMszgTMapper.updateByPrimaryKey(p);
			}*/
			
			

			// 报名实例表;
			PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(strAppInsID);
			if (psTzAppInsT != null) {
				if (!strAppFormState.equals(psTzAppInsT.getTzAppFormSta())) {
					psTzAppInsT.setTzAppFormSta(strAppFormState);
					//更改报名表最后一页的完成状态
					String updateSubmitPageStateSql= "UPDATE PS_TZ_APP_COMP_TBL SET TZ_HAS_COMPLETE=? WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH = (select a.TZ_XXX_BH from  PS_TZ_APP_XXXPZ_T a,PS_TZ_APP_INS_T b where a.TZ_APP_TPL_ID=b.TZ_APP_TPL_ID and b.TZ_APP_INS_ID=PS_TZ_APP_COMP_TBL.TZ_APP_INS_ID and  a.TZ_COM_LMC = 'Page' order by a.TZ_PAGE_NO desc limit 0,1)";
					if ("U".equals(strAppFormState)) {
						psTzAppInsT.setTzAppSubDttm(new Date());
						jdbcTemplate.update(updateSubmitPageStateSql, new Object[]{"Y",strAppInsID});
					}else{
						psTzAppInsT.setTzAppSubDttm(null);
						jdbcTemplate.update(updateSubmitPageStateSql, new Object[]{"N",strAppInsID});
					}
					psTzAppInsT.setRowLastmantDttm(new Date());
					psTzAppInsT.setRowLastmantOprid(oprid);
					psTzAppInsTMapper.updateByPrimaryKey(psTzAppInsT);
				}
			}

			// 报名人标签表;
			jdbcTemplate.update("DELETE FROM PS_TZ_FORM_LABEL_T WHERE TZ_APP_INS_ID=?", new Object[] { strAppInsID });

			String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			if (arrTag != null && arrTag.size() > 0) {
				for (int i = 0; i < arrTag.size(); i++) {
					String strTag = arrTag.get(i);
					if (strTag != null && !"".equals(strTag)) {

						int strTagExist = 0;
						String strTagNameExist = "";
						strTagExist = jdbcTemplate.queryForObject(
								"SELECT count(1) FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID=? AND TZ_LABEL_ID=?",
								new Object[] { str_jg_id, strTag }, "Integer");
						if (strTagExist > 0) {
							PsTzFormLabelTKey psTzFormLabelTKey = new PsTzFormLabelTKey();
							psTzFormLabelTKey.setTzAppInsId(strAppInsID);
							psTzFormLabelTKey.setTzLabelId(strTag);
							psTzFormLabelTMapper.insert(psTzFormLabelTKey);
						} else {
							String strLabelID = "";
							strTagNameExist = jdbcTemplate.queryForObject(
									"SELECT TZ_LABEL_ID FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID=? AND TZ_LABEL_NAME=? AND TZ_LABEL_STATUS='Y' limit 0,1",
									new Object[] { str_jg_id, strTag }, "String");
							if (strTagNameExist != null && !"".equals(strTagNameExist)) {
								/* 存在同名的标签 */
								strLabelID = strTagNameExist;
							} else {
								strLabelID = "00000000"
										+ String.valueOf(getSeqNum.getSeqNum("TZ_LABEL_DFN_T", "TZ_LABEL_ID"));
								strLabelID = strLabelID.substring(strLabelID.length() - 8, strLabelID.length());
								PsTzLabelDfnT psTzLabelDfnT = new PsTzLabelDfnT();
								psTzLabelDfnT.setTzLabelId(strLabelID);
								psTzLabelDfnT.setTzLabelName(strTag);
								psTzLabelDfnT.setTzLabelDesc(strTag);
								psTzLabelDfnT.setTzJgId(str_jg_id);
								psTzLabelDfnT.setTzLabelStatus("Y");
								psTzLabelDfnT.setRowAddedDttm(new Date());
								psTzLabelDfnT.setRowAddedOprid(oprid);
								psTzLabelDfnT.setRowLastmantDttm(new Date());
								psTzLabelDfnT.setRowLastmantOprid(oprid);
								psTzLabelDfnTMapper.insert(psTzLabelDfnT);
							}

							PsTzFormLabelTKey psTzFormLabelTKey = new PsTzFormLabelTKey();
							psTzFormLabelTKey.setTzAppInsId(strAppInsID);
							psTzFormLabelTKey.setTzLabelId(strLabelID);
							psTzFormLabelTMapper.insert(psTzFormLabelTKey);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 修改报名人递交资料审核列表信息 */
	private String tzEditFileCheckInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			// 班级编号;
			String strClassID = (String) infoData.get("classID");
			// oprid;
			String strOprID = (String) infoData.get("oprID");
			// 资料编号;
			String strFileID = (String) infoData.get("fileID");
			// 审核状态;
			String strAuditState = (String) infoData.get("auditState");
			// 审核不通过原因;
			String strFailedReason = (String) infoData.get("failedReason");

			long strAppInsID = 0L;
			strAppInsID = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
					new Object[] { strClassID, strOprID }, "Long");

			PsTzFormZlshTKey psTzFormZlshTkey = new PsTzFormZlshTKey();
			psTzFormZlshTkey.setTzAppInsId(strAppInsID);
			psTzFormZlshTkey.setTzSbminfId(strFileID);
			PsTzFormZlshT psTzFormZlshT = psTzFormZlshTMapper.selectByPrimaryKey(psTzFormZlshTkey);
			if (psTzFormZlshT != null) {
				psTzFormZlshT.setTzZlAuditStatus(strAuditState);
				psTzFormZlshT.setTzAuditNopassRs(strFailedReason);
				psTzFormZlshTMapper.updateByPrimaryKeySelective(psTzFormZlshT);
			} else {
				psTzFormZlshT = new PsTzFormZlshT();
				psTzFormZlshT.setTzAppInsId(strAppInsID);
				psTzFormZlshT.setTzSbminfId(strFileID);
				psTzFormZlshT.setTzZlAuditStatus(strAuditState);
				psTzFormZlshT.setTzAuditNopassRs(strFailedReason);
				psTzFormZlshTMapper.insert(psTzFormZlshT);
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 修改报名人推荐信列表信息 */
	private String tzEditRefLetterInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			// 报名表实例ID;
			long strAppInsId = Long.valueOf(infoData.get("appInsId").toString());
			// 推荐信id;
			String strRefLetterId = (String) infoData.get("refLetterId");
			// 报名人oprID;
			String strOprId = (String) infoData.get("oprID");
			// 推荐人实例ID;
			long strRefLetterAppInsId = 0L;
			try {
				strRefLetterAppInsId = Long.valueOf(infoData.get("refLetterAppInsId").toString());
			} catch (Exception e1) {
				strRefLetterAppInsId = 0L;
			}

			// 系统文件名;
			String strRefLetterSysFile = (String) infoData.get("refLetterSysFile");

			// 用户文件名;
			String strRefLetterUserFile = (String) infoData.get("refLetterUserFile");

			String strRefLetterState = (String) infoData.get("refLetterState");

			// String strRefLetterAurl = (String) infoData.get("refLetterAurl");
			String strRefLetterPurl = (String) infoData.get("refLetterPurl");
			System.out.println("=========strRefLetterPurl========>" + strRefLetterPurl);

			if (strAppInsId > 0 && strRefLetterId != null && !"".equals(strRefLetterId) && strOprId != null
					&& !"".equals(strOprId)) {
				PsTzKsTjxTbl psTzKsTjxTbl = psTzKsTjxTblMapper.selectByPrimaryKey(strRefLetterId);
				if (psTzKsTjxTbl != null) {
					// 原附件信息;
					// String ySysName = psTzKsTjxTbl.getAttachsysfilename();

					psTzKsTjxTbl.setTzAppInsId(strAppInsId);
					psTzKsTjxTbl.setTzRefLetterId(strRefLetterId);
					psTzKsTjxTbl.setTzTjxAppInsId(strRefLetterAppInsId);
					psTzKsTjxTbl.setOprid(strOprId);
					psTzKsTjxTbl.setAttachsysfilename(strRefLetterSysFile);
					psTzKsTjxTbl.setAttachuserfile(strRefLetterUserFile);
					psTzKsTjxTbl.setTzAccessPath(strRefLetterPurl);
					String attAUrl = "";
					if (strRefLetterPurl != null && !"".equals(strRefLetterPurl)) {
						attAUrl = request.getServletContext().getRealPath(strRefLetterPurl);
					}
					psTzKsTjxTbl.setTzAttAUrl(attAUrl);
					psTzKsTjxTbl.setRowLastmantOprid(oprid);
					psTzKsTjxTbl.setRowLastmantDttm(new Date());
					psTzKsTjxTblMapper.updateByPrimaryKeySelective(psTzKsTjxTbl);
					/*
					 * if(strRefLetterSysFile != null &&
					 * !"".equals(strRefLetterSysFile) &&
					 * !strRefLetterSysFile.equals(ySysName)){ PsTzFormAttT
					 * psTzFormAttT = new PsTzFormAttT();
					 * psTzFormAttT.setTzAppInsId(strAppInsId);
					 * psTzFormAttT.setTzXxxBh(strRefLetterSysFile);
					 * psTzFormAttT.setTzIndex(1);
					 * psTzFormAttT.setTzAccessPath(strRefLetterPurl);
					 * psTzFormAttT.setAttachsysfilename(strRefLetterSysFile);
					 * psTzFormAttT.setAttachuserfile(strRefLetterUserFile);
					 * psTzFormAttT.setRowAddedDttm(new Date());
					 * psTzFormAttT.setRowAddedOprid(oprid);
					 * psTzFormAttT.setRowLastmantDttm(new Date());
					 * psTzFormAttT.setRowLastmantOprid(oprid);
					 * psTzFormAttTMapper.insert(psTzFormAttT); }
					 */
				} else {
					errMsg[0] = "1";
					errMsg[1] = "推荐人信息错误，请联系系统管理员";
				}

				if (strRefLetterAppInsId > 0) {
					PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(strRefLetterAppInsId);
					if (psTzAppInsT != null) {
						psTzAppInsT.setTzAppInsId(strRefLetterAppInsId);
						psTzAppInsT.setTzAppFormSta(strRefLetterState);
						psTzAppInsT.setRowLastmantOprid(oprid);
						psTzAppInsT.setRowLastmantDttm(new Date());
						psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
					} else {
						psTzAppInsT = new PsTzAppInsT();
						psTzAppInsT.setTzAppInsId(strRefLetterAppInsId);
						psTzAppInsT.setTzAppFormSta(strRefLetterState);
						psTzAppInsT.setRowAddedDttm(new Date());
						psTzAppInsT.setRowAddedOprid(oprid);
						psTzAppInsT.setRowLastmantOprid(oprid);
						psTzAppInsT.setRowLastmantDttm(new Date());
						psTzAppInsTMapper.insert(psTzAppInsT);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 添加推荐信信息 */
	private String tzAddRefLetterInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			// 报名表实例ID;
			long strAppInsId = Long.parseLong((String) infoData.get("appInsId"));
			// 推荐信id;
			String str_seq1 = String.valueOf((int) (Math.random() * 10000000));
			String str_seq2 = "000000000000000"
					+ String.valueOf(getSeqNum.getSeqNum("TZ_KS_TJX_TBL", "TZ_REF_LETTER_ID"));
			str_seq2 = str_seq2.substring(str_seq2.length() - 15, str_seq2.length());
			String strTjxId = str_seq1 + str_seq2;
			String strRefLetterId = strTjxId;
			// 报名人oprID;
			String strOprId = (String) infoData.get("oprID");
			// 推荐人实例ID;
			long strRefLetterAppInsId = 0L;

			/* 推荐人姓名 */
			String str_name = (String) infoData.get("r_name");

			/* 推荐人名字 */
			String str_gname = (String) infoData.get("r_gname");

			/* 推荐人称呼 */
			String str_title = (String) infoData.get("r_title");

			/* 推荐人单位 */
			String str_company = (String) infoData.get("r_company");

			/* 推荐人职务 */
			String str_post = (String) infoData.get("r_post");

			/* 推荐人手机 */
			String str_phone_no = (String) infoData.get("r_phone_no");

			/* 推荐人区号 */
			String str_phone_area = (String) infoData.get("r_phone_area");

			/* 推荐人邮箱 */
			String str_email = (String) infoData.get("r_email");

			/* 推荐人关系 */
			String str_relation = (String) infoData.get("r_relation");

			/* 推荐人性别 */
			String str_sex = (String) infoData.get("r_sex");

			/* 推荐人语言 */
			String str_language = (String) infoData.get("r_language");

			/* 推荐人备用字段1 */
			String str_by1 = (String) infoData.get("r_by1");

			/* 推荐人备用字段1 */
			String str_by2 = (String) infoData.get("r_by2");

			/* 推荐人备用字段1 */
			String str_by3 = (String) infoData.get("r_by3");

			/* 推荐人备用字段1 */
			String str_by4 = (String) infoData.get("r_by4");

			/* 推荐人备用字段1 */
			String str_by5 = (String) infoData.get("r_by5");

			/* 推荐人编号 */
			String str_tjr_id = (String) infoData.get("r_xh");

			/* 序号 */
			String str_xh = (String) infoData.get("txjSuffix");

			/* 是否增加了行 */

			/* 序号 */
			String str_addLineFlag = (String) infoData.get("lineAddFlag");

			int num_xh = Integer.parseInt(str_xh);

			String str_Suffix = "";
			if (num_xh > 0) {
				str_Suffix = "_" + num_xh;
			}

			/* 报名人JSON */
			String str_app_ins_json_str = (String) infoData.get("appInsJsonStr");

			/* 推荐信ItemID */
			String str_tjx_itemId = (String) infoData.get("refItemId");

			/* 推荐信附件 */
			String strRefLetterSysFile = (String) infoData.get("sysfile");
			String strRefLetterUserFile = (String) infoData.get("usefile");
			/* 附件存放地址 */
			// String strRefLetterPurl = (String)infoData.get("path");
			String strRefLetterAurl = (String) infoData.get("accessPath");

			String str_app_tpl_id = "";
			String str_app_tpl_lan = "";

			/* 如果推荐人语言为空，则去报名表的语言 */
			if (str_language == null || "".equals(str_language)) {
				str_app_tpl_id = jdbcTemplate.queryForObject(
						"SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?",
						new Object[] { strAppInsId }, "String");
				str_app_tpl_lan = jdbcTemplate.queryForObject(
						"SELECT TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?",
						new Object[] { str_app_tpl_id }, "String");
				if ("ENG".equals(str_app_tpl_lan)) {
					str_language = "E";
				} else {
					str_language = "C";
				}
			}

			String str_title_cn = "", str_title_en = "";
			Map<String, Object> zhzxxMap = jdbcTemplate.queryForMap(
					"SELECT TZ_ZHZ_DMS,TZ_ZHZ_CMS FROM ps_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = ? AND TZ_ZHZ_ID = ?",
					new Object[] { "TZ_APP_REF_TITLE", str_title });
			if (zhzxxMap != null) {
				str_title_cn = (String) zhzxxMap.get("TZ_ZHZ_DMS");
				str_title_en = (String) zhzxxMap.get("TZ_ZHZ_CMS");
			}
			if ("E".equals(str_language)) {
				str_title = str_title_en;
			} else {
				str_title = str_title_cn;
			}

			if (strAppInsId > 0 && strRefLetterId != null && !"".equals(strRefLetterId) && strOprId != null
					&& !"".equals(strOprId)) {
				PsTzKsTjxTbl psTzKsTjxTbl = new PsTzKsTjxTbl();
				psTzKsTjxTbl.setTzRefLetterId(strRefLetterId);
				psTzKsTjxTbl.setTzAppInsId(strAppInsId);
				psTzKsTjxTbl.setTzRefLetterId(strRefLetterId);
				psTzKsTjxTbl.setTzTjxAppInsId(strRefLetterAppInsId);
				psTzKsTjxTbl.setOprid(strOprId);
				psTzKsTjxTbl.setTzMbaTjxYx("Y");
				psTzKsTjxTbl.setTzTjrId(str_tjr_id);
				psTzKsTjxTbl.setTzTjxTitle(str_title);
				psTzKsTjxTbl.setTzReferrerName(str_name);
				psTzKsTjxTbl.setTzReferrerGname(str_gname);
				psTzKsTjxTbl.setTzCompCname(str_company);
				psTzKsTjxTbl.setTzPosition(str_post);
				psTzKsTjxTbl.setTzPhone(str_phone_no);
				psTzKsTjxTbl.setTzPhoneArea(str_phone_area);
				psTzKsTjxTbl.setTzEmail(str_email);
				psTzKsTjxTbl.setTzTjrGx(str_relation);
				psTzKsTjxTbl.setTzGender(str_sex);
				psTzKsTjxTbl.setTzTjxType(str_language);
				psTzKsTjxTbl.setTzReflettertype("S");
				psTzKsTjxTbl.setTzTjxYl1(str_by1);
				psTzKsTjxTbl.setTzTjxYl2(str_by2);
				psTzKsTjxTbl.setTzTjxYl3(str_by3);
				psTzKsTjxTbl.setTzTjxYl4(str_by4);
				psTzKsTjxTbl.setTzTjxYl5(str_by5);

				psTzKsTjxTbl.setAttachsysfilename(strRefLetterSysFile);
				psTzKsTjxTbl.setAttachuserfile(strRefLetterUserFile);
				psTzKsTjxTbl.setTzAttAUrl(strRefLetterAurl + "/" + strRefLetterSysFile);

				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzKsTjxTbl.setRowAddedOprid(oprid);
				psTzKsTjxTbl.setRowAddedDttm(new Date());

				psTzKsTjxTbl.setRowLastmantOprid(oprid);
				psTzKsTjxTbl.setRowLastmantDttm(new Date());
				psTzKsTjxTblMapper.insert(psTzKsTjxTbl);

				/* 保存到数据库? TODO */
				/* 先查看数据库是否存在 */

				/* 更新报名表实例表 */
				if (str_app_ins_json_str != null && !"".equals(str_app_ins_json_str)
						&& !"{}".equals(str_app_ins_json_str)) {
					PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(strAppInsId);
					if (psTzAppInsT != null) {
						psTzAppInsT.setTzAppInsId(strAppInsId);
						psTzAppInsT.setTzAppinsJsonStr(str_app_ins_json_str);
						psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);

						/* 同步更新报名表存储表 */

						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_title" + str_Suffix, str_title, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_name" + str_Suffix, str_name, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_gname" + str_Suffix, str_gname, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_company" + str_Suffix, str_company, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_post" + str_Suffix, str_post, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_phone_no" + str_Suffix, str_phone_no, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_phone_area" + str_Suffix, str_phone_area,
								"");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_email" + str_Suffix, str_email, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_relation" + str_Suffix, str_relation, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_by1" + str_Suffix, str_by1, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_by2" + str_Suffix, str_by2, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_by3" + str_Suffix, str_by3, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_by4" + str_Suffix, str_by4, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_by5" + str_Suffix, str_by5, "");
						this.saveAppInsInfo(strAppInsId, str_tjx_itemId + "r_xh" + str_Suffix,
								String.valueOf((num_xh + 1)), "");

						String str_sex_M = jdbcTemplate.queryForObject(
								"select if(B.TZ_ZHZ_CMS IS NULL ,A.TZ_ZHZ_CMS,B.TZ_ZHZ_CMS) TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (select * from PS_TZ_PT_ZHZXX_LNG where TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID = B.TZ_ZHZ_ID WHERE A.TZ_EFF_STATUS='A' AND A.TZ_ZHZJH_ID = 'TZ_TJX_SEX' AND A.TZ_ZHZ_ID = 'M'",
								new Object[] { str_app_tpl_lan }, "String");
						String str_sex_F = jdbcTemplate.queryForObject(
								"select if(B.TZ_ZHZ_CMS IS NULL ,A.TZ_ZHZ_CMS,B.TZ_ZHZ_CMS) TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (select * from PS_TZ_PT_ZHZXX_LNG where TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID = B.TZ_ZHZ_ID WHERE A.TZ_EFF_STATUS='A' AND A.TZ_ZHZJH_ID = 'TZ_TJX_SEX' AND A.TZ_ZHZ_ID = 'F'",
								new Object[] { str_app_tpl_lan }, "String");

						String str_lang_C = jdbcTemplate.queryForObject(
								"select if(B.TZ_ZHZ_CMS IS NULL ,A.TZ_ZHZ_CMS,B.TZ_ZHZ_CMS) TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (select * from PS_TZ_PT_ZHZXX_LNG where TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID = B.TZ_ZHZ_ID WHERE A.TZ_EFF_STATUS='A' AND A.TZ_ZHZJH_ID = 'TZ_TJX_LANG' AND A.TZ_ZHZ_ID = 'C'",
								new Object[] { str_app_tpl_lan }, "String");
						String str_lang_E = jdbcTemplate.queryForObject(
								"select if(B.TZ_ZHZ_CMS IS NULL ,A.TZ_ZHZ_CMS,B.TZ_ZHZ_CMS) TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (select * from PS_TZ_PT_ZHZXX_LNG where TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID = B.TZ_ZHZ_ID WHERE A.TZ_EFF_STATUS='A' AND A.TZ_ZHZJH_ID = 'TZ_TJX_LANG' AND A.TZ_ZHZ_ID = 'E'",
								new Object[] { str_app_tpl_lan }, "String");

						if ("M".equals(str_sex)) {
							this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_sex" + str_Suffix, "M", str_sex_M,
									"Y");
							this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_sex" + str_Suffix, "F", str_sex_F,
									"N");
						} else {
							if ("F".equals(str_sex)) {
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_sex" + str_Suffix, "M", str_sex_M,
										"N");
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_sex" + str_Suffix, "F", str_sex_F,
										"Y");
							} else {
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_sex" + str_Suffix, "M", str_sex_M,
										"N");
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_sex" + str_Suffix, "F", str_sex_F,
										"N");
							}
						}

						if ("E".equals(str_language)) {
							this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_language" + str_Suffix, "E",
									str_lang_E, "Y");
							this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_language" + str_Suffix, "C",
									str_lang_C, "N");
						} else {
							if ("C".equals(str_language)) {
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_language" + str_Suffix, "E",
										str_lang_E, "N");
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_language" + str_Suffix, "C",
										str_lang_C, "Y");
							} else {
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_language" + str_Suffix, "E",
										str_lang_E, "N");
								this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_language" + str_Suffix, "C",
										str_lang_C, "N");
							}
						}

						this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_way" + str_Suffix, "S", "发送邮件", "Y");
						this.saveAppInsInfo2(strAppInsId, str_tjx_itemId + "r_way" + str_Suffix, "U", "上传附件", "N");

						if ("Y".equals(str_addLineFlag)) {
							jdbcTemplate.update(
									"UPDATE PS_TZ_APP_DHHS_T SET TZ_XXX_LINE = TZ_XXX_LINE + 1 WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?",
									new Object[] { strAppInsId, str_tjx_itemId });
						}
					}

				}
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 修改联系方式信息 */
	private String tzEditContactInfo(String strAppInsID, Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		try {
			String mainMobilePhone = "", backupMobilePhone = "", mainPhone = "", backupPhone = "", mainEmail = "",
					backupEmail = "", mainAddress = "", backupAddress = "", wechat = "", skype = "";
			if (strAppInsID != null && !"".equals(strAppInsID)) {
				mainMobilePhone = (String) infoData.get("mainMobilePhone");
				backupMobilePhone = (String) infoData.get("backupMobilePhone");
				mainPhone = (String) infoData.get("mainPhone");
				backupPhone = (String) infoData.get("backupPhone");
				mainEmail = (String) infoData.get("mainEmail");
				backupEmail = (String) infoData.get("backupEmail");
				mainAddress = (String) infoData.get("mainAddress");
				backupAddress = (String) infoData.get("backupAddress");
				wechat = (String) infoData.get("wechat");
				skype = (String) infoData.get("skype");

				String oprid = jdbcTemplate.queryForObject(
						"select OPRID from PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=? limit 0,1",
						new Object[] { strAppInsID }, "String");
				if (oprid != null && !"".equals(oprid)) {
					PsTzLxfsInfoTblKey psTzLxfsInfoTblKey = new PsTzLxfsInfoTblKey();
					psTzLxfsInfoTblKey.setTzLxfsLy("ZCYH");
					psTzLxfsInfoTblKey.setTzLydxId(oprid);
					PsTzLxfsInfoTbl psTzLxfsInfoTbl = psTzLxfsInfoTblMapper.selectByPrimaryKey(psTzLxfsInfoTblKey);
					if (psTzLxfsInfoTbl != null) {
						psTzLxfsInfoTbl.setTzLxfsLy("ZCYH");
						psTzLxfsInfoTbl.setTzLydxId(oprid);
						psTzLxfsInfoTbl.setTzZySj(mainMobilePhone);
						psTzLxfsInfoTbl.setTzCySj(backupMobilePhone);
						psTzLxfsInfoTbl.setTzZyDh(mainPhone);
						psTzLxfsInfoTbl.setTzCyDh(backupPhone);
						psTzLxfsInfoTbl.setTzZyEmail(mainEmail);
						psTzLxfsInfoTbl.setTzCyEmail(backupEmail);
						psTzLxfsInfoTbl.setTzZyTxdz(mainAddress);
						psTzLxfsInfoTbl.setTzCyTxdz(backupAddress);
						psTzLxfsInfoTbl.setTzWeixin(wechat);
						psTzLxfsInfoTbl.setTzSkype(skype);
						psTzLxfsInfoTblMapper.updateByPrimaryKeySelective(psTzLxfsInfoTbl);
					} else {
						psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
						psTzLxfsInfoTbl.setTzLxfsLy("ZCYH");
						psTzLxfsInfoTbl.setTzLydxId(oprid);
						psTzLxfsInfoTbl.setTzZySj(mainMobilePhone);
						psTzLxfsInfoTbl.setTzCySj(backupMobilePhone);
						psTzLxfsInfoTbl.setTzZyDh(mainPhone);
						psTzLxfsInfoTbl.setTzCyDh(backupPhone);
						psTzLxfsInfoTbl.setTzZyEmail(mainEmail);
						psTzLxfsInfoTbl.setTzCyEmail(backupEmail);
						psTzLxfsInfoTbl.setTzZyTxdz(mainAddress);
						psTzLxfsInfoTbl.setTzCyTxdz(backupAddress);
						psTzLxfsInfoTbl.setTzWeixin(wechat);
						psTzLxfsInfoTbl.setTzSkype(skype);
						psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 修改更多信息 */
	private String tzEditMoreInfo(String strAppInsID, Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		try {
			String strOrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String strParamID = "";
			String strTempParamValue = "";
			String sql = "SELECT TZ_ATTRIBUTE_ID FROM PS_TZ_FORM_ATTR_T  WHERE TZ_JG_ID=? AND TZ_IS_USED='Y'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strOrgID });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					strParamID = (String) list.get(i).get("TZ_ATTRIBUTE_ID");
					strTempParamValue = (String) infoData.get(strParamID);
					PsTzFrmMorinfTKey psTzFrmMorinfTKey = new PsTzFrmMorinfTKey();
					psTzFrmMorinfTKey.setTzAppInsId(Long.parseLong(strAppInsID));
					psTzFrmMorinfTKey.setTzAttributeId(strParamID);
					PsTzFrmMorinfT psTzFrmMorinfT = psTzFrmMorinfTMapper.selectByPrimaryKey(psTzFrmMorinfTKey);
					if (psTzFrmMorinfT != null) {
						psTzFrmMorinfT.setTzAppInsId(Long.parseLong(strAppInsID));
						psTzFrmMorinfT.setTzAttributeId(strParamID);
						psTzFrmMorinfT.setTzAttributeValue(strTempParamValue);
						psTzFrmMorinfTMapper.updateByPrimaryKeySelective(psTzFrmMorinfT);
					} else {
						psTzFrmMorinfT = new PsTzFrmMorinfT();
						psTzFrmMorinfT.setTzAppInsId(Long.parseLong(strAppInsID));
						psTzFrmMorinfT.setTzAttributeId(strParamID);
						psTzFrmMorinfT.setTzAttributeValue(strTempParamValue);
						psTzFrmMorinfTMapper.insert(psTzFrmMorinfT);
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = "保存失败，请确认是否新添加个性化属性而没有刷新报名表审批页面数据。";
		}

		return strRet;
	}

	@Override
	/* 发送推荐信未完全提交提醒邮件; */
	public String tzGetJsonData(String strParams) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("audienceId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			// 人员id;
			String str_oprid = jacksonUtil.getString("OPRID");
			// 班级id;
			String str_bj_id = jacksonUtil.getString("bj_id");

			String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			long str_bmb_id = 0L;
			String str_name = "", str_email = "";
			str_bmb_id = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=? limit 0,1",
					new Object[] { str_bj_id, str_oprid }, "Long");
			Map<String, Object> map = jdbcTemplate.queryForMap(
					"SELECT TZ_REALNAME,TZ_EMAIL FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? AND TZ_JG_ID=? limit 0,1",
					new Object[] { str_oprid, str_jg_id });
			if (map != null) {
				str_name = (String) map.get("TZ_REALNAME");
				str_email = (String) map.get("TZ_EMAIL");
			}

			/* 邮件短信发送任务类 */
			// 创建邮件发送听众;
			String crtAudi = createTaskServiceImpl.createAudience("", str_jg_id, "推荐信催促邮件", "TJXC");
			if (crtAudi != null && !"".equals(crtAudi)) {
				createTaskServiceImpl.addAudCy(crtAudi, str_name, str_name, "", "", str_email, str_email, "", str_oprid,
						"", "", String.valueOf(str_bmb_id));
				mapRet.replace("audienceId", crtAudi);
			}
		} catch (Exception e) {
			mapRet.replace("audienceId", "");
		}
		return jacksonUtil.Map2json(mapRet);
	}

	private void saveAppInsInfo(long strAppInsID, String str_item_id, String str_value_s, String str_value_l) {
		PsTzAppCcTKey psTzAppCcTKey = new PsTzAppCcTKey();
		psTzAppCcTKey.setTzAppInsId(strAppInsID);
		psTzAppCcTKey.setTzXxxBh(str_item_id);
		PsTzAppCcT psTzAppCcT = psTzAppCcTMapper.selectByPrimaryKey(psTzAppCcTKey);
		if (psTzAppCcT != null) {
			psTzAppCcT.setTzAppInsId(strAppInsID);
			psTzAppCcT.setTzXxxBh(str_item_id);
			psTzAppCcT.setTzAppLText(str_value_l);
			psTzAppCcT.setTzAppSText(str_value_s);
			psTzAppCcTMapper.updateByPrimaryKeySelective(psTzAppCcT);
		} else {
			psTzAppCcT = new PsTzAppCcT();
			psTzAppCcT.setTzAppInsId(strAppInsID);
			psTzAppCcT.setTzXxxBh(str_item_id);
			psTzAppCcT.setTzAppLText(str_value_l);
			psTzAppCcT.setTzAppSText(str_value_s);
			psTzAppCcTMapper.insert(psTzAppCcT);
		}
	}

	private void saveAppInsInfo2(long strAppInsID, String str_item_id, String str_code, String str_txt,
			String str_isChecked) {
		PsTzAppDhccTKey psTzAppDhccTKey = new PsTzAppDhccTKey();
		psTzAppDhccTKey.setTzAppInsId(strAppInsID);
		psTzAppDhccTKey.setTzXxxBh(str_item_id);
		psTzAppDhccTKey.setTzXxxkxzMc(str_code);

		PsTzAppDhccT psTzAppDhccT = psTzAppDhccTMapper.selectByPrimaryKey(psTzAppDhccTKey);
		if (psTzAppDhccT != null) {
			psTzAppDhccT.setTzAppInsId(strAppInsID);
			psTzAppDhccT.setTzXxxBh(str_item_id);
			psTzAppDhccT.setTzXxxkxzMc(str_code);
			psTzAppDhccT.setTzAppSText(str_txt);
			psTzAppDhccT.setTzIsChecked(str_isChecked);
			psTzAppDhccTMapper.updateByPrimaryKeySelective(psTzAppDhccT);
		} else {
			psTzAppDhccT = new PsTzAppDhccT();
			psTzAppDhccT.setTzAppInsId(strAppInsID);
			psTzAppDhccT.setTzXxxBh(str_item_id);
			psTzAppDhccT.setTzXxxkxzMc(str_code);
			psTzAppDhccT.setTzAppSText(str_txt);
			psTzAppDhccT.setTzIsChecked(str_isChecked);
			psTzAppDhccTMapper.insert(psTzAppDhccT);
		}
	}

}
