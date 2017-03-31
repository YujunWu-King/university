package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsprcsrqstMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzEmlTaskAetMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzEmlTaskAet;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 邮件短信发送；原：TZ_GD_COM_EMLSMS_APP:emlCommon
 * 
 * @author tang
 * @since 2015-11-30
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.EmlCommonServiceImpl")
public class EmlCommonServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	@Autowired
	private emlPreviewServiceImpl emlPreviewServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;
	@Autowired
	private PsTzEmlTaskAetMapper psTzEmlTaskAetMapper;
	@Autowired
	private TZGDObject tZGDObject;

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();

		// //System.out.println("strParams=" + strParams);

		try {
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			List<?> tmpNames = jacksonUtil.getList("tmpNames");
			Map<String, Object> tmpNamesMap = new HashMap<>();
			ArrayList<Map<String, Object>> listData = new ArrayList<>();
			tmpNamesMap.put("tmpNames", listData);

			if (tmpNames != null) {
				// modity by caoy 2016-6-6 加载发件人信息
				if (tmpNames.size() == 1 && ((String) tmpNames.get(0)).equals("sender")) {
					// 获取当前登录人自己的邮箱

					//String oprid = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
					//自己的邮箱先不加载，都通过邮箱服务器配置;
					Map<String, Object> jsonMap = null;
					String selfEmail = null;
					/*

					String sql = "select TZ_EMAIL from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID = ? ";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { oprid });
					if (map != null) {
						selfEmail = (String) map.get("TZ_EMAIL");
						// //System.out.println("selfEmail=" + selfEmail);
						if (selfEmail != null && !selfEmail.equals("")) {
							jsonMap = new HashMap<>();
							jsonMap.put("tmpId", map.get("TZ_EMAIL"));
							jsonMap.put("tmpName", map.get("TZ_EMAIL"));
							listData.add(jsonMap);
						}
					}*/


					String sql = "select TZ_EMLSERV_ID,TZ_EML_ADDR100 from PS_TZ_EMLS_DEF_TBL where TZ_JG_ID=? ";
					// System.out.println("sql=" + sql);

					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { jgId });
					// //System.out.println("jgId:"+jgId);
					// //System.out.println("list:"+list);
					// //System.out.println("list:"+list.size());

					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							if (!((String) list.get(i).get("TZ_EML_ADDR100")).equals(selfEmail)) {
								jsonMap = new HashMap<>();
								jsonMap.put("tmpId", list.get(i).get("TZ_EMLSERV_ID"));
								jsonMap.put("tmpName", list.get(i).get("TZ_EML_ADDR100"));
								listData.add(jsonMap);
							}
						}
					}

				} else {
					for (int i = 0; i < tmpNames.size(); i++) {
						String tmpId = (String) tmpNames.get(i);
						////System.out.println("tmpId=" + tmpId);
						String sql = "select TZ_TMPL_ID,TZ_TMPL_NAME from PS_TZ_EMALTMPL_TBL where TZ_JG_ID=? AND TZ_TMPL_ID=? AND TZ_USE_FLAG='Y'";
						Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { jgId, tmpId });
						Map<String, Object> jsonMap = new HashMap<>();
						if (map != null) {
							jsonMap.put("tmpId", map.get("TZ_TMPL_ID"));
							jsonMap.put("tmpName", map.get("TZ_TMPL_NAME"));
						}
						listData.add(jsonMap);
					}
				}
				tmpNamesMap.replace("tmpNames", listData);
				mapRet.replace("formData", tmpNamesMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String tmpId = jacksonUtil.getString("tmpId");
			String audienceId = jacksonUtil.getString("audienceId");

			// 发件人;
			String senderEmail = "";
			// 邮件主题;
			String emailTheme = "";
			// 邮件内容;
			String emailContent = "";
			// 元模板ID；
			String ymbId = "";
			String emailSQL = "select A.TZ_EML_ADDR100,B.TZ_MAL_SUBJUECT,B.TZ_MAL_CONTENT,TZ_YMB_ID from PS_TZ_EMLS_DEF_TBL A , PS_TZ_EMALTMPL_TBL B  WHERE B.TZ_JG_ID=? AND B.TZ_TMPL_ID=? AND B.TZ_EMLSERV_ID = A.TZ_EMLSERV_ID";
			Map<String, Object> emailMap = jdbcTemplate.queryForMap(emailSQL, new Object[] { jgId, tmpId });
			if (emailMap != null) {
				senderEmail = (String) emailMap.get("TZ_EML_ADDR100");
				emailTheme = (String) emailMap.get("TZ_MAL_SUBJUECT");
				emailContent = (String) emailMap.get("TZ_MAL_CONTENT");
				ymbId = (String) emailMap.get("TZ_YMB_ID");
			}

			// 收件人;
			int count = 0;
			String addresseeEmail = "";
			String zyMail = "";
			String audCyId = "";
			String mainEmailSQL = "select TZ_ZY_EMAIL,TZ_AUDCY_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(mainEmailSQL, new Object[] { audienceId });
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					count++;
					audCyId = (String) list.get(i).get("TZ_AUDCY_ID");
					zyMail = (String) list.get(i).get("TZ_ZY_EMAIL");
					if (zyMail != null && !"".equals(zyMail)) {
						addresseeEmail = addresseeEmail + ";" + zyMail;
					}
				}
			}

			if (!"".equals(addresseeEmail)) {
				addresseeEmail = addresseeEmail.substring(1);
			}

			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("senderEmail", senderEmail);
			jsonMap.put("AddresseeEmail", addresseeEmail);
			jsonMap.put("emailTheme", emailTheme);
			// 查看是否单个收件人，如果只有一个则直接解析邮件;
			if (count == 1) {
				ArrayList<String[]> arrayList = emlPreviewServiceImpl.ayalyMbVar(jgId, ymbId, audienceId, audCyId);
				for (int i = 0; i < arrayList.size(); i++) {
					String[] str = arrayList.get(i);

					String name = str[0];
					String value = str[1];
					emailContent = emailContent.replaceAll(name, value);
				}
			}
			jsonMap.put("emailContent", emailContent);
			returnJsonMap.replace("formData", jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 发送邮件 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> map = new HashMap<>();
		map.put("success", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if (jacksonUtil.containsKey("jgId") && jacksonUtil.containsKey("audienceId")
						&& jacksonUtil.containsKey("data")) {
					// 机构ID;
					String jgId = jacksonUtil.getString("jgId");
					// 发送的听众Id;
					String audienceId = jacksonUtil.getString("audienceId");
					// 发送信息;
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					if (dataMap != null) {
						// 模板ID;
						String tmpId = (String) dataMap.get("emailTmp");
						// 发送人;
						String senderEmail = (String) dataMap.get("senderEmail");

						// 抄送;
						String ccAddresseeEmail = (String) dataMap.get("ccAddresseeEmail");
						// 密送
						String bcAddresseeEmail = (String) dataMap.get("bcAddresseeEmail");
						// 主题;
						String emailTheme = (String) dataMap.get("emailTheme");
						// 内容;
						String emailContent = (String) dataMap.get("emailContent");
						// 附件;
						String fjLj = (String) dataMap.get("fjLj");
						String fjMc = (String) dataMap.get("fjMc");

						String taskId = createTaskServiceImpl.createTaskIns(jgId, tmpId, "MAL", "A");
						if (taskId == null || "".equals(taskId)) {
							map.replace("success", "创建邮件发送任务失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
						}

						////System.out.println("taskId:" + taskId);
						
						
						boolean bl = true;

						// modity by caoy 2016-6-6 加载发件人信息
						// 包含@说明是 自己的邮箱
						if (senderEmail != null && !"".equals(senderEmail)) {
							if (senderEmail.indexOf("@") != -1) {
								bl = createTaskServiceImpl.updateEmailSender(taskId, senderEmail, "");
							} else {
								bl = createTaskServiceImpl.updateEmailSender(taskId, senderEmail);
							}
						}
						if (bl == false) {
							map.replace("success", "更新邮件发送人失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
						}

						// 更新任务关联的听众ID;
						createTaskServiceImpl.updateAudId(taskId, audienceId);
						// 添加抄送;
						if (ccAddresseeEmail != null && !"".equals(ccAddresseeEmail)) {
							bl = createTaskServiceImpl.addCCAddr(taskId, ccAddresseeEmail);
						}
						if (bl == false) {
							map.replace("success", "添加抄送失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
						}
						// 添加密送;
						if (bcAddresseeEmail != null && !"".equals(bcAddresseeEmail)) {
							bl = createTaskServiceImpl.addBCAddr(taskId, bcAddresseeEmail);
						}
						if (bl == false) {
							map.replace("success", "添加抄送失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
						}
						// 更新主题;
						bl = createTaskServiceImpl.updateEmailSendTitle(taskId, emailTheme);
						if (bl == false) {
							map.replace("success", "更新邮件主题失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
						}
						// 更新内容;
						bl = createTaskServiceImpl.updateEmailSendContent(taskId, emailContent);
						if (bl == false) {
							map.replace("success", "更新邮件内容失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
						}
						// 写附件表;
						if (fjLj != null && !"".equals(fjLj) && fjMc != null && !"".equals(fjMc)) {
							bl = createTaskServiceImpl.addAttach(taskId, fjMc, fjLj);
							if (bl == false) {
								map.replace("success", "更新邮件附件失败");
								strRet = jacksonUtil.Map2json(map);
								return strRet;
							}
						}
						
						//查询什么时候跑AE,未定义则默认大于等于5条时跑AE;
						int aeCount = 5;
						String aeCountString = jdbcTemplate.queryForObject(" select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_EMAIL_CALL_AE_COUNT'", "String");
						if(aeCountString != null && !"".equals(aeCountString) && StringUtils.isNumeric(aeCountString)){
							aeCount = Integer.valueOf(aeCountString);
						}
						
						int AudCount = jdbcTemplate.queryForObject("select COUNT(TZ_AUDCY_ID) from PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID = ?", new Object[]{audienceId},"Integer" );
						if(AudCount < aeCount){
							sendSmsOrMalServiceImpl.send(taskId, "");
						}else{

							//int processInstance = getSeqNum.getSeqNum("PS_TZ_DXYJFSRW_TBL", "PROCESSINSTANCE");
							int processInstance = getSeqNum.getSeqNum("PSPRCSRQST", "PROCESSINSTANCE");
							//当前用户;
							String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
							/*生成运行控制ID*/
							SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
						    String s_dtm = datetimeFormate.format(new Date());
							String runCntlId = "EMAIL" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");
							
							Psprcsrqst psprcsrqst = new Psprcsrqst();
							psprcsrqst.setPrcsinstance(processInstance);
							psprcsrqst.setRunId(runCntlId);
							psprcsrqst.setOprid(currentOprid);
							psprcsrqst.setRundttm(new Date());
							psprcsrqst.setRunstatus("5");
							psprcsrqstMapper.insert(psprcsrqst);
							
							PsTzEmlTaskAet psTzEmlTaskAet = new PsTzEmlTaskAet();
							psTzEmlTaskAet.setRunId(runCntlId);
							psTzEmlTaskAet.setTzEmlSmsTaskId(taskId);
							psTzEmlTaskAetMapper.insert(psTzEmlTaskAet);
							
							String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
							String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
							
							BaseEngine tmpEngine = tZGDObject.createEngineProcess(currentOrgId, "TZGD_SEND_EMAIL_AE");
							//指定调度作业的相关参数
							EngineParameters schdProcessParameters = new EngineParameters();

							schdProcessParameters.setBatchServer("");
							schdProcessParameters.setCycleExpression("");
							schdProcessParameters.setLoginUserAccount(currentAccountId);
							schdProcessParameters.setPlanExcuteDateTime(new Date());
							schdProcessParameters.setRunControlId(runCntlId);
							
							//调度作业
							tmpEngine.schedule(schdProcessParameters);
						}

					} else {
						map.replace("success", "参数有误");
					}
				} else {
					map.replace("success", "参数有误");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(map);
		return strRet;
	}

}
