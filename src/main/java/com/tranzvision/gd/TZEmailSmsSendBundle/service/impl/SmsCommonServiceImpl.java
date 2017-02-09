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
 * 通用短信发送；原：TZ_GD_COM_EMLSMS_APP:smsCommon
 * 
 * @author zhanglang
 * @since 2017-02-08
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SmsCommonServiceImpl")
public class SmsCommonServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private emlPreviewServiceImpl emlPreviewServiceImpl;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;
	
	@Autowired
	private TZGDObject tZGDObject;
	
	@Autowired
	private PsTzEmlTaskAetMapper psTzEmlTaskAetMapper;
	
	/**
	 * 查询发送短信表单
	 */
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

			// 短信内容;
			String smsContent = "";
			// 元模板ID；
			String ymbId = "";
			
			String smsSQL = "select TZ_YMB_ID,TZ_SMS_CONTENT from PS_TZ_SMSTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
			Map<String, Object> smsMap = jdbcTemplate.queryForMap(smsSQL, new Object[] { jgId, tmpId });
			if (smsMap != null) {
				smsContent = (String) smsMap.get("TZ_SMS_CONTENT");
				ymbId = (String) smsMap.get("TZ_YMB_ID");
			}

			// 收件人;
			int count = 0;
			String addresseeSms = "";
			String zyMobile = "";
			String audCyId = "";
			String mainMobileSQL = "select TZ_ZY_SJ,TZ_AUDCY_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(mainMobileSQL, new Object[] { audienceId });
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					count++;
					audCyId = (String) list.get(i).get("TZ_AUDCY_ID");
					zyMobile = (String) list.get(i).get("TZ_ZY_SJ");
					if (zyMobile != null && !"".equals(zyMobile)) {
						addresseeSms = addresseeSms + ";" + zyMobile;
					}
				}
			}

			if (!"".equals(addresseeSms)) {
				addresseeSms = addresseeSms.substring(1);
			}

			// 查看是否单个收信人，如果只有一个则直接解析短信;
			if (count == 1) {
				ArrayList<String[]> arrayList = emlPreviewServiceImpl.ayalyMbVar(jgId, ymbId, audienceId, audCyId);
				for (int i = 0; i < arrayList.size(); i++) {
					String[] str = arrayList.get(i);

					String name = str[0];
					String value = str[1];
					smsContent = smsContent.replaceAll(name, value);
				}
			}
			
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("AddresseeSms", addresseeSms);
			jsonMap.put("smsContent", smsContent);

			returnJsonMap.replace("formData", jsonMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/**
	 * 查询短信模板列表
	 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			List<?> tmpNames = jacksonUtil.getList("tmpNames");
			
			Map<String, Object> tmpNamesMap = new HashMap<>();
			ArrayList<Map<String, Object>> listData = new ArrayList<>();
			tmpNamesMap.put("tmpNames", listData);

			if (tmpNames != null) {
				for (int i = 0; i < tmpNames.size(); i++) {
					String tmpId = (String) tmpNames.get(i);

					String sql = "select TZ_TMPL_ID,TZ_TMPL_NAME from PS_TZ_SMSTMPL_TBL where TZ_JG_ID=? AND TZ_TMPL_ID=? AND TZ_USE_FLAG='Y'";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { jgId, tmpId });
					Map<String, Object> jsonMap = new HashMap<>();
					if (map != null) {
						jsonMap.put("tmpId", map.get("TZ_TMPL_ID"));
						jsonMap.put("tmpName", map.get("TZ_TMPL_NAME"));
					}
					listData.add(jsonMap);
				}

				tmpNamesMap.replace("tmpNames", listData);
				mapRet.replace("formData", tmpNamesMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	/**
	 * 发送短信
	 */
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
						String tmpId = (String) dataMap.get("smsTmp");
						// 内容;
						String smsContent = (String) dataMap.get("smsContent");
						
						//创建短信发送任务
						String taskId = createTaskServiceImpl.createTaskIns(jgId, tmpId, "SMS", "A");
						if (taskId == null || "".equals(taskId)) {
							map.replace("success", "创建短信发送任务失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
						}
						// 更新任务关联的听众ID;
						createTaskServiceImpl.updateAudId(taskId, audienceId);
						
						boolean bl = true;
						// 更新内容;
						bl = createTaskServiceImpl.updateSmsSendContent(taskId, smsContent);
						if (bl == false) {
							map.replace("success", "更新短信内容失败");
							strRet = jacksonUtil.Map2json(map);
							return strRet;
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
							int processInstance = getSeqNum.getSeqNum("PSPRCSRQST", "PROCESSINSTANCE");
							//当前用户;
							String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
							/*生成运行控制ID*/
							SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
						    String s_dtm = datetimeFormate.format(new Date());
							String runCntlId = "SMS" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");
							
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
							
							BaseEngine tmpEngine = tZGDObject.createEngineProcess("ADMIN", "TZGD_SEND_EMAIL_AE");
							//指定调度作业的相关参数
							EngineParameters schdProcessParameters = new EngineParameters();

							schdProcessParameters.setBatchServer("");
							schdProcessParameters.setCycleExpression("");
							schdProcessParameters.setLoginUserAccount("Admin");
							schdProcessParameters.setPlanExcuteDateTime(new Date());
							schdProcessParameters.setRunControlId(runCntlId);
							
							//调度作业
							tmpEngine.schedule(schdProcessParameters);
						}
						map.replace("success", "短信已发送");
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
			errMsg[1] = "发送失败"+e.toString();
			map.replace("success", "发送失败");
		}

		strRet = jacksonUtil.Map2json(map);
		return strRet;
	}
}
