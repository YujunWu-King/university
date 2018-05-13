package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMsapAudTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsapAudTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPskshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 批次面试时间安排
 * 
 * @author zhang lang 原PS：TZ_GD_MS_ARR_PKG:TZ_GD_MS_SETSTU_CLS
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewSetStudentImpl")
public class TzInterviewSetStudentImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetHardCodePoint getHardCodePoint;

	@Autowired
	private PsTzMsPskshTblMapper psTzMspsKshTblMapper;

	@Autowired
	private PsTzMsapAudTblMapper psTzMsapAudTblMapper;

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String type = jacksonUtil.getString("TYPE");
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");

			if (null != classID && !"".equals(classID) && null != batchID && !"".equals(batchID)) {
				// 面试安排学生列表store
				if ("STULIST".equals(type)) {
					int start = Integer.parseInt(request.getParameter("start"));
					int limit = Integer.parseInt(request.getParameter("limit"));

					// 查询面试安排总数
					String sql = "SELECT COUNT(*) FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APPLY_PC_ID IN (SELECT TZ_BATCH_ID FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?)";
					int total = sqlQuery.queryForObject(sql, new Object[] { classID, batchID, classID }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
					if (total > 0) {
						sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APPLY_PC_ID IN (SELECT TZ_BATCH_ID FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?) order by TZ_APP_INS_ID LIMIT ? , ?";
						List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { classID, batchID, classID, start, limit });

						for (Map<String, Object> mapData : listData) {
							Map<String, Object> mapJson = new HashMap<String, Object>();
							// 报名表实例ID
							String appIns = String.valueOf(mapData.get("TZ_APP_INS_ID"));

							// 查询姓名
							sql = "SELECT TZ_REALNAME,TZ_MSH_ID,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL A WHERE exists(SELECT 'X' FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=? and OPRID=A.OPRID)";
							Map<String, Object> yhxxMap = sqlQuery.queryForMap(sql, new Object[] { appIns });
							
							String name = "";
							String interviewAppId = "";
							String mobile = "";
							String email = "";
							if (yhxxMap != null) {
								name = yhxxMap.get("TZ_REALNAME") == null ? "" : yhxxMap.get("TZ_REALNAME").toString();
								interviewAppId = yhxxMap.get("TZ_MSH_ID") == null ? ""
										: yhxxMap.get("TZ_MSH_ID").toString();
								mobile = yhxxMap.get("TZ_MOBILE") == null ? "" : yhxxMap.get("TZ_MOBILE").toString();
								email = yhxxMap.get("TZ_EMAIL") == null ? "" : yhxxMap.get("TZ_EMAIL").toString();
							}
							
							
							sql = "select TZ_CLASS_NAME,TZ_BATCH_NAME from PS_TZ_FORM_WRK_T A join PS_TZ_CLASS_INF_T B on(A.TZ_CLASS_ID=B.TZ_CLASS_ID) left join PS_TZ_CLS_BATCH_T C on(A.TZ_BATCH_ID=C.TZ_BATCH_ID) where A.TZ_APP_INS_ID=?";
							Map<String, Object> clsMap = sqlQuery.queryForMap(sql, new Object[] { appIns });
							String className = "";
							String batchName = "";
							if(clsMap != null){
								className = clsMap.get("TZ_CLASS_NAME") == null ? "" : clsMap.get("TZ_CLASS_NAME").toString();
								batchName = clsMap.get("TZ_BATCH_NAME") == null ? "" : clsMap.get("TZ_BATCH_NAME").toString();
							}

							// 面试资格
							sql = "SELECT B.TZ_ZHZ_DMS FROM PS_TZ_CLPS_KSH_TBL A,PS_TZ_PT_ZHZXX_TBL B,PS_TZ_CLS_BATCH_T C WHERE A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_BATCH_ID AND A.TZ_MSHI_ZGFLG=B.TZ_ZHZ_ID AND B.TZ_EFF_STATUS ='A' AND B.TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND A.TZ_CLASS_ID=? AND A.TZ_APP_INS_ID=? ORDER BY CONVERT(A.TZ_APPLY_PC_ID,SIGNED) DESC";
							String msZgFlag = sqlQuery.queryForObject(sql, new Object[] { classID, appIns },
									"String");
							if ("".equals(msZgFlag) || msZgFlag == null) {
								sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND TZ_EFF_STATUS ='A' AND TZ_ZHZ_ID='W'";
								msZgFlag = sqlQuery.queryForObject(sql, "String");
							}

							// 标签
							String strLabel = "";
							sql = "SELECT TZ_LABEL_NAME FROM PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B WHERE A.TZ_LABEL_ID=B.TZ_LABEL_ID AND TZ_APP_INS_ID=?";
							List<Map<String, Object>> labelList = sqlQuery.queryForList(sql,
									new Object[] { appIns });
							for (Map<String, Object> mapLabel : labelList) {
								String label = String.valueOf(mapLabel.get("TZ_LABEL_NAME"));
								if (!"".equals(label) && label != null) {
									strLabel = strLabel == "" ? label : strLabel + "； " + label;
								}
							}

							mapJson.put("classID", classID);
							mapJson.put("batchID", batchID);
							mapJson.put("appId", appIns);
							mapJson.put("stuName", name);
							mapJson.put("msZGFlag", msZgFlag);
							
							mapJson.put("className", className);
							mapJson.put("batchName", batchName);

							mapJson.put("mobile", mobile);
							mapJson.put("email", email);

							mapJson.put("label", strLabel);
							mapJson.put("interviewAppId", interviewAppId);

							listJson.add(mapJson);
						}
						mapRet.replace("total", total);
						mapRet.replace("root", listJson);
					}
				} else {
					// 面试听众store
					if ("AUD".equals(type)) {
						String sql = "SELECT COUNT(*) FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
						int total = sqlQuery.queryForObject(sql, new Object[] { classID, batchID }, "int");

						ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
						if (total > 0) {
							sql = "SELECT TZ_AUD_ID FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
							List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
									new Object[] { classID, batchID });

							for (Map<String, Object> mapData : listData) {
								Map<String, Object> mapJson = new HashMap<String, Object>();
								// 听众ID
								String audID = String.valueOf(mapData.get("TZ_AUD_ID"));

								// 查询姓名
								sql = "SELECT TZ_AUD_NAM FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=?";
								String audName = sqlQuery.queryForObject(sql, new Object[] { audID }, "String");

								mapJson.put("id", audID);
								mapJson.put("desc", audName);

								listJson.add(mapJson);
							}
							mapRet.replace("total", total);
							mapRet.replace("root", listJson);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	/**
	 * 保存听众
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("success", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			for (int i = 0; i < actData.length; i++) {
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);

				String classID = jacksonUtil.getString("classID");
				String batchID = jacksonUtil.getString("batchID");
				List<String> audIdList = (List<String>) jacksonUtil.getList("audIDs");

				String whereIn = "";
				for (String audID : audIdList) {
					if ("".equals(whereIn)) {
						whereIn = "'" + audID + "'";
					} else {
						whereIn = whereIn + ",'" + audID + "'";
					}
				}

				String sql;
				if ("".equals(whereIn)) {
					sql = "SELECT TZ_AUD_ID FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
				} else {
					sql = "SELECT TZ_AUD_ID FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_AUD_ID NOT IN("
							+ whereIn + ")";
				}

				List<Map<String, Object>> delAudIdList = sqlQuery.queryForList(sql,
						new Object[] { classID, batchID });
				for (Map<String, Object> delAudMap : delAudIdList) {
					String delAudId = String.valueOf(delAudMap.get("TZ_AUD_ID"));

					// 删除听众成员
					sql = "SELECT OPRID FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=?";
					List<Map<String, Object>> audCyList = sqlQuery.queryForList(sql, new Object[] { delAudId });

					for (Map<String, Object> audCyMap : audCyList) {
						Long appInsId;
						String audOprid = audCyMap.get("OPRID") == null ? "" : audCyMap.get("OPRID").toString();

						sql = "select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where TZ_CLASS_ID=? and OPRID=?";
						Map<String, Object> appMap = sqlQuery.queryForMap(sql, new Object[] { classID, audOprid });
						if (appMap != null) {
							appInsId = appMap.get("TZ_APP_INS_ID") == null ? 0
									: Long.valueOf(appMap.get("TZ_APP_INS_ID").toString());

							if (appInsId > 0) {
								PsTzMsPskshTblKey psTzMspsKshTblKey = new PsTzMsPskshTblKey();
								psTzMspsKshTblKey.setTzClassId(classID);
								psTzMspsKshTblKey.setTzApplyPcId(batchID);
								psTzMspsKshTblKey.setTzAppInsId(appInsId);

								PsTzMsPskshTbl psTzMspsKshTbl = psTzMspsKshTblMapper
										.selectByPrimaryKey(psTzMspsKshTblKey);

								// 且不再其他听众里面
								String inOtherAud = "";
								if (!"".equals(whereIn)) {
									sql = "SELECT 'Y' FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID IN(" + whereIn
											+ ") and OPRID=? limit 1";
									inOtherAud = sqlQuery.queryForObject(sql, new Object[] { audOprid }, "String");
								}

								if (psTzMspsKshTbl != null && !"Y".equals(inOtherAud)) {
									psTzMspsKshTblMapper.deleteByPrimaryKey(psTzMspsKshTblKey);
								}
							}
						}
					}

					// 删除面试听众
					PsTzMsapAudTblKey psTzMsapAudTblKey = new PsTzMsapAudTblKey();
					psTzMsapAudTblKey.setTzClassId(classID);
					psTzMsapAudTblKey.setTzBatchId(batchID);
					psTzMsapAudTblKey.setTzAudId(delAudId);
					psTzMsapAudTblMapper.deleteByPrimaryKey(psTzMsapAudTblKey);
				}
			}
			rtnMap.replace("success", "success");
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			rtnMap.replace("success", "fail");
		}
		strRtn = jacksonUtil.Map2json(rtnMap);
		return strRtn;
	}

	/**
	 * 删除面试考生
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("success", "");
		rtnMap.put("notAllow", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		String notAllowStu = "";
		int count = 0;
		try {
			for (int i = 0; i < actData.length; i++) {
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);

				String classID = jacksonUtil.getString("classID");
				String batchID = jacksonUtil.getString("batchID");
				Long appId = Long.valueOf(jacksonUtil.getString("appId"));
				
				//如果考生被面试评委抽取，则不允许删除
				String sql = "select count(1) from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
				int pwCount = sqlQuery.queryForObject(sql, new Object[]{ classID, batchID, appId }, "int");
				
				if(pwCount > 0){
					if(count < 10){
						String stuName = jacksonUtil.getString("stuName");
						if(notAllowStu == ""){
							notAllowStu = stuName;
						}else{
							notAllowStu = notAllowStu + "," + stuName;
						}
					}
					count ++;
				}else{
					PsTzMsPskshTblKey psTzMspsKshTblKey = new PsTzMsPskshTblKey();
					psTzMspsKshTblKey.setTzClassId(classID);
					psTzMspsKshTblKey.setTzApplyPcId(batchID);
					psTzMspsKshTblKey.setTzAppInsId(appId);

					PsTzMsPskshTbl psTzMspsKshTbl = psTzMspsKshTblMapper.selectByPrimaryKey(psTzMspsKshTblKey);
					if (psTzMspsKshTbl != null) {
						psTzMspsKshTblMapper.deleteByPrimaryKey(psTzMspsKshTblKey);
					}
				}
			}
			if(count >= 10){
				notAllowStu = notAllowStu + ",...";
			}
			rtnMap.replace("success", "success");
			rtnMap.replace("notAllow", notAllowStu);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			rtnMap.replace("success", "fail");
		}
		strRtn = jacksonUtil.Map2json(rtnMap);
		return strRtn;
	}

	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
			case "tzSendEmailSmsToStu":
				// 保存批次面试预约安排
				strRet = this.tzSendEmailSmsToStu(strParams, errorMsg);
				break;
			case "tzAddAudience":
				// 保存批次面试预约安排
				strRet = this.tzAddAudience(strParams, errorMsg);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}

	/**
	 * 添加听众
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String tzAddAudience(String strParams, String[] errorMsg) {
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);

			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			List<String> audIdList = (List<String>) jacksonUtil.getList("audIDs");

			for (String audID : audIdList) {

				String sql = "SELECT 'Y' FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_AUD_ID=?";
				String isExists = sqlQuery.queryForObject(sql, new Object[] { classID, batchID, audID }, "String");

				if ("Y".equals(isExists)) {
					// do nothing
				} else {
					PsTzMsapAudTblKey psTzMsapAudTblKey = new PsTzMsapAudTblKey();
					psTzMsapAudTblKey.setTzClassId(classID);
					psTzMsapAudTblKey.setTzBatchId(batchID);
					psTzMsapAudTblKey.setTzAudId(audID);
					int rtn = psTzMsapAudTblMapper.insert(psTzMsapAudTblKey);

					if (rtn != 0) {
						// 插入听成成员
						sql = "SELECT OPRID FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=? and TZ_DXZT='A'";
						List<Map<String, Object>> audCyList = sqlQuery.queryForList(sql, new Object[] { audID });

						for (Map<String, Object> audCyMap : audCyList) {
							long appInsId;

							String audOprid = audCyMap.get("OPRID") == null ? "" : audCyMap.get("OPRID").toString();

							sql = "select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where TZ_CLASS_ID=? and OPRID=?";
							Map<String, Object> appMap = sqlQuery.queryForMap(sql,
									new Object[] { classID, audOprid });

							if (appMap != null) {
								appInsId = appMap.get("TZ_APP_INS_ID") == null ? 0
										: Long.valueOf(appMap.get("TZ_APP_INS_ID").toString());

								if (appInsId > 0) {
									PsTzMsPskshTblKey psTzMspsKshTblKey = new PsTzMsPskshTblKey();
									psTzMspsKshTblKey.setTzClassId(classID);
									psTzMspsKshTblKey.setTzApplyPcId(batchID);
									psTzMspsKshTblKey.setTzAppInsId(appInsId);

									PsTzMsPskshTbl psTzMspsKshTbl = psTzMspsKshTblMapper
											.selectByPrimaryKey(psTzMspsKshTblKey);
									if (psTzMspsKshTbl == null) {
										psTzMspsKshTbl = new PsTzMsPskshTbl();
										psTzMspsKshTbl.setTzClassId(classID);
										psTzMspsKshTbl.setTzApplyPcId(batchID);
										psTzMspsKshTbl.setTzAppInsId(appInsId);
										psTzMspsKshTbl.setRowAddedOprid(oprid);
										psTzMspsKshTbl.setRowAddedDttm(new Date());
										psTzMspsKshTbl.setRowLastmantOprid(oprid);
										psTzMspsKshTbl.setRowLastmantDttm(new Date());

										psTzMspsKshTblMapper.insert(psTzMspsKshTbl);
									}
								}
							}
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRtn;
	}

	/**
	 * 给选中考生发送面试预约邮件
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String tzSendEmailSmsToStu(String strParams, String[] errorMsg) {
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String sendType = jacksonUtil.getString("sendType");
			List<Map<String, Object>> selStuList = (List<Map<String, Object>>) jacksonUtil.getList("stuList");

			String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String title = "面试预约通知" + ("SMS".equals(sendType) ? "短信" : "邮件");
			// 创建邮件发送听众
			String crtAudi = createTaskServiceImpl.createAudience("", jgid, title, "JSRW");

			if (!"".equals(crtAudi)) {

				// 添加听众成员
				for (Map<String, Object> stuDataMap : selStuList) {

					String classId = stuDataMap.get("classID") == null ? "" : String.valueOf(stuDataMap.get("classID"));
					String appId = stuDataMap.get("appId") == null ? "" : String.valueOf(stuDataMap.get("appId"));
					String stuName = stuDataMap.get("stuName") == null ? "" : String.valueOf(stuDataMap.get("stuName"));

					String oprid = "";
					String sql = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND TZ_APP_INS_ID=?";
					oprid = sqlQuery.queryForObject(sql, new Object[] { classId, appId }, "String");

					sql = "SELECT TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_SJ,TZ_CY_SJ FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?";
					Map<String, Object> mapBmrInfo = sqlQuery.queryForMap(sql, new Object[] { appId });

					String mainEmail = "";
					String cyEmail = "";
					String mainPhone = "";
					String cyPhone = "";
					if (null != mapBmrInfo) {
						mainEmail = mapBmrInfo.get("TZ_ZY_EMAIL") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_ZY_EMAIL"));
						cyEmail = mapBmrInfo.get("TZ_CY_EMAIL") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_CY_EMAIL"));

						mainPhone = mapBmrInfo.get("TZ_ZY_SJ") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_ZY_SJ"));
						cyPhone = mapBmrInfo.get("TZ_CY_SJ") == null ? "" : String.valueOf(mapBmrInfo.get("TZ_CY_SJ"));
					}

					if ("".equals(mainEmail) && !"".equals(oprid)) {
						sql = "SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?";
						mainEmail = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
					}
					if ("".equals(mainPhone) && !"".equals(oprid)) {
						sql = "SELECT TZ_ZY_SJ FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?";
						mainPhone = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
					}

					if (("EML".equals(sendType) && !"".equals(mainEmail))
							|| ("SMS".equals(sendType) && !"".equals(mainPhone)) && !"".equals(oprid)) {
						createTaskServiceImpl.addAudCy(crtAudi, stuName, stuName, mainPhone, cyPhone, mainEmail,
								cyEmail, "", oprid, "", "", appId);
					}
				}

				// 模板
				String tmpName = "";
				try {
					if (sendType.equals("SMS")) {
						tmpName = getHardCodePoint.getHardCodePointVal("TZ_GD_MS_MSARRC_SMSTML");
					} else {
						tmpName = getHardCodePoint.getHardCodePointVal("TZ_GD_MS_MSARRC_EMLTML");
					}
				} catch (NullPointerException e) {
					tmpName = "";
				}

				if ("".equals(tmpName) || tmpName == null) {
					errorMsg[0] = "1";
					errorMsg[1] = "未配置邮件模板";
				} else {
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("tmpName", tmpName);
					mapRet.put("audienceId", crtAudi);
					strRtn = jacksonUtil.Map2json(mapRet);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRtn;
	}
}
