package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 线索管理报名表查看审核信息
 * @author LuYan 2017-11-21
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueBmbVerifyServiceImpl")
public class TzClueBmbVerifyServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private HttpServletRequest request;
	
	/* 获取报名人信息 */
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String contextPath = request.getContextPath();
					
			if (jacksonUtil.containsKey("classID") && jacksonUtil.containsKey("oprID")) {
				// 班级编号;
				String strClassID = jacksonUtil.getString("classID");
				// 报名人编号;
				String strOprID = jacksonUtil.getString("oprID");

				// 班级名称，所属项目，所属项目名称;
				String strClassName = "";

				// 获取班级名称，报名表模板ID;
				String sql = "SELECT TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strClassID });
				if (map != null) {
					strClassName = (String) map.get("TZ_CLASS_NAME");
				}

				// 报名人信息：报名表编号，审批状态，姓名，报名表提交状态，备注;
				long strAppInsID = 0L;
				String strSpState = "", strStuName = "", strSubmitState = "",
						strRemark = "", strShortRemark = "";
				Map<String, Object> bmrMap = jdbcTemplate.queryForMap(
						"SELECT TZ_APP_INS_ID,TZ_FORM_SP_STA,TZ_COLOR_SORT_ID,TZ_REMARK,TZ_REMARK_SHORT FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?",
						new Object[] { strClassID, strOprID });
				if (bmrMap != null) {
					strAppInsID = Long.valueOf(bmrMap.get("TZ_APP_INS_ID").toString());
					strSpState = (String) bmrMap.get("TZ_FORM_SP_STA");
					strRemark = (String) bmrMap.get("TZ_REMARK");
					strShortRemark = (String) bmrMap.get("TZ_REMARK_SHORT");
				}

				Map<String, Object> nameMap = jdbcTemplate.queryForMap(
						"SELECT TZ_REALNAME ,DATE_FORMAT(BIRTHDATE,'%y-%m-%d %H:%i:%s') BIRTHDATE FROM PS_TZ_REG_USER_T WHERE OPRID=?",
						new Object[] { strOprID });
				if (nameMap != null) {
					strStuName = (String) nameMap.get("TZ_REALNAME");
					if (strStuName == null || "".equals(strStuName)) {
						strStuName = jdbcTemplate.queryForObject(
								"SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[] { strOprID },"String");
					}
				}

				Map<String, Object> appMap = jdbcTemplate.queryForMap(
						"SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?", new Object[] { strAppInsID });
				if (appMap != null) {
					strSubmitState = (String) appMap.get("TZ_APP_FORM_STA");
				}

				// 报名人标签;
				String strTagID = "";
				ArrayList<String> strTagList = new ArrayList<>();
				List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT TZ_LABEL_ID FROM PS_TZ_FORM_LABEL_T WHERE TZ_APP_INS_ID=?",
						new Object[] { strAppInsID });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						strTagID = (String) list.get(i).get("TZ_LABEL_ID");
						strTagList.add(strTagID);
					}
				}

				//报名人照片地址
				String strPhotoUrl = "",strPhotoPath = "",strPhotoName = "";
				Map<String, Object> photoMap = jdbcTemplate.queryForMap(
						"SELECT B.TZ_ATT_A_URL,B.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A ,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA AND A.OPRID = ?",
						new Object[] { strOprID });
				if (photoMap != null) {
					strPhotoPath = (String) photoMap.get("TZ_ATT_A_URL");
					strPhotoName = (String) photoMap.get("TZ_ATTACHSYSFILENA");
					if(strPhotoPath!=null&&!"".equals(strPhotoPath)){
						strPhotoUrl = contextPath+strPhotoPath+strPhotoName;
					}
				}
				
				if(strPhotoUrl==null||"".equals(strPhotoUrl)){
					strPhotoUrl = contextPath+"/statics/images/tranzvision/mrtx02.jpg";
				}
				
				Map<String, Object> jsonMap = new HashMap<>();
				jsonMap.put("classID", strClassID);
				jsonMap.put("oprID", strOprID);
				jsonMap.put("stuName", strStuName);
				jsonMap.put("className", strClassName);
				jsonMap.put("appInsID", strAppInsID);
				jsonMap.put("appFormState", strSubmitState);
				jsonMap.put("auditState", strSpState);
				jsonMap.put("tag", strTagList);
				jsonMap.put("remark", strRemark);
				jsonMap.put("shortRemark", strShortRemark);
				jsonMap.put("photoUrl", strPhotoUrl);
				
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
				
				if ("REFLETTER".equals(strQueryType)) {
					returnString = this.tzQueryRefLetterList(strClassID, strOprID, numLimit, numStart, errorMsg);
					bl = true;
				}
				if ("ATTACHMENT".equals(strQueryType)) {
					returnString = this.tzQueryAttachmentList(strClassID, strOprID, numLimit, numStart, errorMsg);
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

			String strFileData = "";
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
					strFileData = "";
					int fileNum = 1;
					// int xuhao = 1;

					String fileSql = "SELECT ATTACHUSERFILE,ATTACHSYSFILENAME,TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T WhERE TZ_APP_INS_ID =? AND TZ_XXX_BH IN (SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO=?) ORDER BY TZ_INDEX";
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
								strFileData = tzGdObject.getHTMLText(
										"HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML",fileName,
										accessUrl, String.valueOf(numFileID));
							} else {
								strFileData = strFileData + "<br>"
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
					jsonmap.put("fileData", strFileData);
					jsonmap.put("xuhao", numFileID);
					jsonmap.put("TZ_XXX_BH", "BMBFILE");
					jsonmap.put("TZ_XXX_MC", TZ_TITLE);
					jsonmap.put("fileLinkName", "");
					jsonmap.put("TZ_COM_LMC", TZ_COM_LMC);
					total++;
					listData.add(jsonmap);
				}
			}

			strFileData = "";
			String fileName = "", sysFileName = "", accessUrl = "";
			/* 后台管理员上传的附件lastIndexOf */
			TZ_COM_LMC = "imagesUpload";
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

					strFileData = tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_IMAGELINK_HTML", fileName, accessUrl, String.valueOf(numFileID));

					Map<String, Object> jsonmap = new HashMap<>();
					jsonmap.put("appInsId", appInsId);
					jsonmap.put("fileData", strFileData);
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
	
	
}
