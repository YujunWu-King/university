package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsprcsrqstMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTxLjAetMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxyjQfDyTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzDxyjqfTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjqftxrzTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTxLjAet;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyTWithBLOBs;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjqfTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxrzT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzEmlTaskAet;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/*
 * TZ_GK_EDM_PKG:TZ_GK_EDM_CLS
 * 
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzGkEdmClsServiceImpl")
public class TzGkEdmClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzYjqftxrzTMapper psTzYjqftxrzTMapper;
	
	@Autowired
	private PsTzDxyjqfTblMapper psTzDxyjqfTblMapper;
	
	@Autowired
	private analysisBounceServiceImpl analysisBounceServiceImpl;
	
	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;

	@Autowired
	private TZGDObject tZGDObject;
	
	@Autowired
	private PsTxLjAetMapper psTxLjAetMapper;
	
	/* 获取form及grid中的数据 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		// 邮件群发批次编号;
		String strEmailID = jacksonUtil.getString("emailID");

		List<Map<String, Object>> gridDataList = new ArrayList<>();

		int i = 0;

		String[] zhiBiaoMc = { "发送总数", "发送成功数", "硬退数", "软退数", "总打开数", "净打开数", "总点击数", "净点击数", "退订数" };
		String[] zhiBiaoDesc = { "FSZS", "FSCGS", "YTS", "RTS", "ZDKS", "JDKS", "ZDJS", "JDJS", "TDS" };
		List<Object> zhiBiaoNum = new ArrayList<>();
		List<Object> zhiBiaoPercent = new ArrayList<>();

		// 发送总数,发送成功数,硬退数,软退数,总打开数,净打开数,总点击数,净点击数,退订数;
		double strFszsNum = 0, strFsCgsNum = 0, strHardNum = 0, strSoftNum = 0, strTotalOpenNum = 0, strJDkNum = 0,
				strJClickNum = 0, strTDNum = 0, cilckNum = 0;
		// 发送总数;
		strFszsNum = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in(select TZ_EML_SMS_TASK_ID  from PS_TZ_DXYJFSRW_TBL where TZ_RWTJ_DT in(select max(TZ_RWTJ_DT) from PS_TZ_DXYJFSRW_TBL where TZ_MLSM_QFPC_ID=?))",
				new Object[] { strEmailID }, "Double");
		// 发送成功数;
		strFsCgsNum = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in(select TZ_EML_SMS_TASK_ID  from PS_TZ_DXYJFSRW_TBL where TZ_RWTJ_DT in(select max(TZ_RWTJ_DT) from PS_TZ_DXYJFSRW_TBL where TZ_MLSM_QFPC_ID=?)) and TZ_FS_ZT='SUC'",
				new Object[] { strEmailID }, "Double");

		strHardNum = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_YJQFTXJL_T where TZ_MLSM_QFPC_ID=? and TZ_TX_TYPE='H'",
				new Object[] { strEmailID }, "Double");

		strSoftNum = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_YJQFTXJL_T where TZ_MLSM_QFPC_ID=? and TZ_TX_TYPE='S'",
				new Object[] { strEmailID }, "Double");

		strTotalOpenNum = jdbcTemplate.queryForObject("select count(1) from PS_TZ_YJQFDKYJJL_T where TZ_MLSM_QFPC_ID=?",
				new Object[] { strEmailID }, "Double");

		// 净打开;
		strJDkNum = jdbcTemplate.queryForObject(
				"select count(1) from (select distinct  TZ_EMAIL from PS_TZ_YJQFDKYJJL_T where TZ_MLSM_QFPC_ID=?) A",
				new Object[] { strEmailID }, "Double");
		// 净点击;
		try {
			strJClickNum = jdbcTemplate.queryForObject(
					"select sum(TZ_EMAIL) from (select  count(distinct TZ_EMAIL) TZ_EMAIL from PS_TZ_YJQFDJJL_T where TZ_MLSM_QFPC_ID=? group by TZ_RED_URL) A",
					new Object[] { strEmailID }, "Double");
		} catch (Exception e) {
			strJClickNum = 0;
		}
		// 点击数;
		cilckNum = jdbcTemplate.queryForObject("select  count(1) from PS_TZ_YJQFDJJL_T where TZ_MLSM_QFPC_ID=?",
				new Object[] { strEmailID }, "Double");
		// 退订数;
		strTDNum = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_YJQFTD_TBL where TZ_MLSM_QFPC_ID=?  AND TZ_QXDY_FLAG='Y'",
				new Object[] { strEmailID }, "Double");

		zhiBiaoNum.add(strFszsNum);
		zhiBiaoNum.add(strFsCgsNum);
		zhiBiaoNum.add(strHardNum);
		zhiBiaoNum.add(strSoftNum);
		zhiBiaoNum.add(strTotalOpenNum);
		zhiBiaoNum.add(strJDkNum);
		zhiBiaoNum.add(cilckNum);
		zhiBiaoNum.add(strJClickNum);
		zhiBiaoNum.add(strTDNum);

		// 计算指标的百分比;
		double strFszsPercent, strFsCgsPercent, strHardPercent, strSoftPercent, strTotalOpenPercent, strJDkPercent,
				strTotalClickPercent, strJClickPercent, strTDPercent;
		if (strFszsNum != 0) {
			// 发送总数是百分之百;
			strFszsPercent = 1d;
			strFsCgsPercent = strFsCgsNum / strFszsNum;
			strHardPercent = strHardNum / strFszsNum;
			strSoftPercent = strSoftNum / strFszsNum;

			zhiBiaoPercent.add(strFszsPercent);
			zhiBiaoPercent.add(strFsCgsPercent);
			zhiBiaoPercent.add(strHardPercent);
			zhiBiaoPercent.add(strSoftPercent);

			if (strFsCgsNum != 0) {
				strTotalOpenPercent = strTotalOpenNum / strFsCgsNum;
				strJDkPercent = strJDkNum / strFsCgsNum;
				strTotalClickPercent = cilckNum / strFsCgsNum;
				strJClickPercent = strJClickNum / strFsCgsNum;
				strTDPercent = strTDNum / strFsCgsNum;

				zhiBiaoPercent.add(strTotalOpenPercent);
				zhiBiaoPercent.add(strJDkPercent);
				zhiBiaoPercent.add(strTotalClickPercent);
				zhiBiaoPercent.add(strJClickPercent);
				zhiBiaoPercent.add(strTDPercent);
			} else {
				zhiBiaoPercent.add(0);
				zhiBiaoPercent.add(0);
				zhiBiaoPercent.add(0);
				zhiBiaoPercent.add(0);
				zhiBiaoPercent.add(0);
			}
		} else {
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
			zhiBiaoPercent.add(0);
		}

		double numPercent;
		String zbxPercent = "";

		for (i = 0; i < zhiBiaoMc.length; i++) {
			double tempPercent = Double.parseDouble(String.valueOf(zhiBiaoPercent.get(i))) * 100d;
			if (tempPercent != (int) tempPercent) {
				BigDecimal d5 = new BigDecimal(tempPercent);
				numPercent = d5.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				zbxPercent = numPercent + "%";
			} else {
				zbxPercent = (int) tempPercent + "%";
			}

			Map<String, Object> gridDataMap = new HashMap<>();
			gridDataMap.put("zhiBiaoName", zhiBiaoMc[i]);
			gridDataMap.put("zhiBiaoNum", String.valueOf(zhiBiaoNum.get(i)));
			gridDataMap.put("zhiBiaoPercent", zbxPercent);
			gridDataMap.put("zhiBiaoDesc", zhiBiaoDesc[i]);
			gridDataList.add(gridDataMap);

		}

		Map<String, Object> retrunMap = new HashMap<>();
		retrunMap.put("total", gridDataList.size());
		retrunMap.put("root", gridDataList);

		return jacksonUtil.Map2json(retrunMap);

	}

	/* 功能描述：EDM统计页面 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		// 邮件群发批次编号;
		String strEmailID = jacksonUtil.getString("emailID");

		// 邮件主题,邮件内容,发件人,发送模式;
		String strMailSub, strMailCont, strMailSender, strSendTime, strSendModel;
		Map<String, Object> map = jdbcTemplate.queryForMap(
				"select TZ_MAL_SUBJUECT,TZ_MAL_CONTENT,TZ_EMAIL_SENDER,TZ_SEND_MODEL from PS_TZ_DXYJQF_DY_T where TZ_MLSM_QFPC_ID=?",
				new Object[] { strEmailID });
		strMailSub = String.valueOf(map.get("TZ_MAL_SUBJUECT"));
		strMailCont = String.valueOf(map.get("TZ_MAL_CONTENT"));
		strMailSender = String.valueOf(map.get("TZ_EMAIL_SENDER"));
		strSendModel = String.valueOf(map.get("TZ_SEND_MODEL"));

		int index = strMailSender.indexOf("@");
		if (index > 0) {
			//strMailSender = strMailSender;
		} else {
			strMailSender = jdbcTemplate.queryForObject(
					"select TZ_EML_ADDR100 from PS_TZ_EMLS_DEF_TBL where TZ_EMLSERV_ID=?",
					new Object[] { strMailSender }, "String");
		}

		// 获取发送时间;
		strSendTime = jdbcTemplate.queryForObject(
				"select DATE_FORMAT(TZ_FS_DT,'%Y-%m-%d %H:%i') TZ_FS_DT from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in (select TZ_EML_SMS_TASK_ID from PS_TZ_DXYJFSRW_TBL where TZ_MLSM_QFPC_ID=?) LIMIT 0,1",
				new Object[] { strEmailID }, "String");

		String strRecPerson = "", strEmail = "";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(
				"SELECT TZ_ZY_EMAIL FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID IN (SELECT TZ_AUDIENCE_ID FROM PS_TZ_DXYJFSRW_TBL A WHERE A.TZ_MLSM_QFPC_ID=?)", new Object[] { strEmailID });
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				strEmail = String.valueOf(list.get(i).get("TZ_ZY_EMAIL"));
				if ("".equals(strRecPerson)) {
					strRecPerson = strEmail;
				} else {
					strRecPerson = strRecPerson + "; " + strEmail;
				}
			}
		}
		/*
		if ("NOR".equals(strSendModel)) {
			// 收件人(从收件人表里获得);
			List<Map<String, Object>> list = jdbcTemplate.queryForList(
					"select TZ_EMAIL from PS_TZ_DXYJQFSJR_T where TZ_MLSM_QFPC_ID=?", new Object[] { strEmailID });
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					strEmail = String.valueOf(list.get(i).get("TZ_EMAIL"));
					if ("".equals(strRecPerson)) {
						strRecPerson = strEmail;
					} else {
						strRecPerson = strRecPerson + "; " + strEmail;
					}
				}
			}

			// 收件人(从听众表里获得);
		} else {
			// 以Excel方式导入;
			List<Map<String, Object>> list = jdbcTemplate.queryForList(
					"select distinct TZ_ZY_EMAIL from PS_TZ_AUDCYUAN_T where  TZ_AUDCY_ID in(select TZ_AUDCY_ID from PS_TZ_MLSM_DRNR_T where TZ_MLSM_QFPC_ID=?)",
					new Object[] { strEmailID });
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					strEmail = String.valueOf(list.get(i).get("TZ_ZY_EMAIL"));
					if ("".equals(strRecPerson)) {
						strRecPerson = strEmail;
					} else {
						strRecPerson = strRecPerson + "; " + strEmail;
					}
				}
			}
		}
		*/
		
		// 收件人不需要显示全，只需要显示一行,末尾加上省略号;
		if (strRecPerson.length() > 100) {
			strRecPerson = strRecPerson.substring(0, 118);
			strRecPerson = strRecPerson + "......";
		}

		Map<String, Object> formDataMap = new HashMap<>();
		formDataMap.put("emailID", strEmailID);
		formDataMap.put("emailSubject", strMailSub);
		formDataMap.put("emailSender", strMailSender);
		formDataMap.put("emailReceiver", strRecPerson);
		formDataMap.put("sendTime", strSendTime);
		formDataMap.put("emailContent", strMailCont);

		String dispalyFunnel = "";

		ArrayList<Map<String, Object>> strFunnelDateArr = new ArrayList<>();
		// 发送总数;
		int totalCount = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in(select TZ_EML_SMS_TASK_ID  from PS_TZ_DXYJFSRW_TBL where TZ_RWTJ_DT in(select max(TZ_RWTJ_DT) from PS_TZ_DXYJFSRW_TBL where TZ_MLSM_QFPC_ID=?))",
				new Object[] { strEmailID }, "Integer");
		if (totalCount < 1) {
			dispalyFunnel = "N";
		} else {
			dispalyFunnel = "Y";

			// 发送成功数;
			int successCount = jdbcTemplate.queryForObject(
					"select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID in(select TZ_EML_SMS_TASK_ID  from PS_TZ_DXYJFSRW_TBL where TZ_RWTJ_DT in(select max(TZ_RWTJ_DT) from PS_TZ_DXYJFSRW_TBL where TZ_MLSM_QFPC_ID=?)) and TZ_FS_ZT='SUC'",
					new Object[] { strEmailID }, "Integer");

			// 净打开;
			int jOpenCount = jdbcTemplate.queryForObject(
					"select count(1) from (select distinct  TZ_EMAIL from PS_TZ_YJQFDKYJJL_T where TZ_MLSM_QFPC_ID=?) A",
					new Object[] { strEmailID }, "Integer");
			// 净点击;
			int jClickCount = 0;
			try {
				jClickCount = jdbcTemplate.queryForObject(
						"select sum(TZ_EMAIL) from (select  count(distinct TZ_EMAIL) TZ_EMAIL from PS_TZ_YJQFDJJL_T where TZ_MLSM_QFPC_ID=? group by TZ_RED_URL) A",
						new Object[] { strEmailID }, "Integer");
			} catch (Exception e) {
				jClickCount = 0;
			}

			// 退订数;
			int tdCount = jdbcTemplate.queryForObject(
					"select count(1) from PS_TZ_YJQFTD_TBL where TZ_MLSM_QFPC_ID=?  AND TZ_QXDY_FLAG='Y'",
					new Object[] { strEmailID }, "Integer");

			// 退信数;
			int txCount = jdbcTemplate.queryForObject("select count(1) from PS_TZ_YJQFTXJL_T where TZ_MLSM_QFPC_ID=?",
					new Object[] { strEmailID }, "Integer");

			Map<String, Object> map1 = new HashMap<>();
			map1.put("name", "发送总数");
			map1.put("count", totalCount);
			strFunnelDateArr.add(map1);

			Map<String, Object> map2 = new HashMap<>();
			map2.put("name", "发送成功数");
			map2.put("count", successCount);
			strFunnelDateArr.add(map2);

			Map<String, Object> map3 = new HashMap<>();
			map3.put("name", "净打开数");
			map3.put("count", jOpenCount);
			strFunnelDateArr.add(map3);

			Map<String, Object> map4 = new HashMap<>();
			map4.put("name", "净点击数");
			map4.put("count", jClickCount);
			strFunnelDateArr.add(map4);

			Map<String, Object> map5 = new HashMap<>();
			map5.put("name", "退订数");
			map5.put("count", tdCount);
			strFunnelDateArr.add(map5);

			Map<String, Object> map6 = new HashMap<>();
			map6.put("name", "退信数");
			map6.put("count", txCount);
			strFunnelDateArr.add(map6);

		}

		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("formData", formDataMap);
		returnMap.put("dispalyFunnel", dispalyFunnel);
		returnMap.put("funenlDate", strFunnelDateArr);

		return jacksonUtil.Map2json(returnMap);

	}
	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		Map<String, Object> map = new HashMap<>();
		map.put("TZ_MLSM_QFPC_ID", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		for(int i = 0; i<actData.length;i++){
			String formData = actData[i];
			jacksonUtil.json2Map(formData);
			String dxyjQfpcID = jacksonUtil.getString("emailID");
			
			int processInstance = getSeqNum.getSeqNum("PSPRCSRQST", "PROCESSINSTANCE");
			// 生成运行控制ID
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String s_dtm = datetimeFormate.format(new Date());
			String runCntlId = "TX_" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			Psprcsrqst psprcsrqst = new Psprcsrqst();
			psprcsrqst.setPrcsinstance(processInstance);
			psprcsrqst.setRunId(runCntlId);
			psprcsrqst.setOprid(currentOprid);
			psprcsrqst.setRundttm(new Date());
			psprcsrqst.setRunstatus("5");
			psprcsrqstMapper.insert(psprcsrqst);
			
			PsTxLjAet psTxLjAet = new PsTxLjAet();
			
			String portalUrlSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_LOG_URL'";
			String TZ_PORTAL_URL = jdbcTemplate.queryForObject(portalUrlSql, "String");
			if(TZ_PORTAL_URL != null && !"".equals(TZ_PORTAL_URL)){
				if(TZ_PORTAL_URL.indexOf("/") != (TZ_PORTAL_URL.length()-1)){
					TZ_PORTAL_URL = TZ_PORTAL_URL + "/";
				}
			}
			
			String dirPath = TZ_PORTAL_URL + "linkfiles/mailLog/";
			dirPath = request.getServletContext().getRealPath(dirPath);
			File tF = new File(dirPath);
			if (!tF.exists()) {
				tF.mkdirs();
			}
			
			psTxLjAet.setPrcsinstance(processInstance);
			psTxLjAet.setTzAbsltUrl(dirPath);
			psTxLjAet.setTzRelUrl(TZ_PORTAL_URL + "linkfiles/mailLog/");
			psTxLjAetMapper.insert(psTxLjAet);
			
			PsTzDxyjqfTbl psTzDxyjqfTbl = new PsTzDxyjqfTbl();
			psTzDxyjqfTbl.setRunCntlId(runCntlId);
			psTzDxyjqfTbl.setTzMlsmQfpcId(dxyjQfpcID);
			psTzDxyjqfTblMapper.insert(psTzDxyjqfTbl);
			
			PsTzYjqftxrzT psTzYjqftxrzT = new PsTzYjqftxrzT();
			psTzYjqftxrzT.setTzMlsmQfpcId(dxyjQfpcID);
			psTzYjqftxrzT.setPrcsinstance(processInstance);
			psTzYjqftxrzT.setTzTxaeDttm(new Date());
			psTzYjqftxrzTMapper.insert(psTzYjqftxrzT);

			try {
				String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
				String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				BaseEngine tmpEngine = tZGDObject.createEngineProcess(currentOrgId, "TZ_MAILTX_AE");
				// 指定调度作业的相关参数
				EngineParameters schdProcessParameters = new EngineParameters();

				schdProcessParameters.setBatchServer("");
				schdProcessParameters.setCycleExpression("");
				schdProcessParameters.setLoginUserAccount(currentAccountId);
				schdProcessParameters.setPlanExcuteDateTime(new Date());
				
				schdProcessParameters.setRunControlId(runCntlId);

				// 调度作业
				tmpEngine.schedule(schdProcessParameters);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			
			String sql = "SELECT TZ_EML_SMS_TASK_ID,TZ_EMLSERV_ID FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=?";
			List<Map<String , Object>> list = jdbcTemplate.queryForList(sql, new Object[]{dxyjQfpcID});
			if(list != null && list.size() > 0){
				for(int j = 0; j < list.size(); j++){
					String mailServId = (String)list.get(j).get("TZ_EMLSERV_ID");
					analysisBounceServiceImpl.analysisBounceByMailServId(dxyjQfpcID, mailServId, processInstance);
				}
			}
			*/
			map.replace("TZ_MLSM_QFPC_ID", dxyjQfpcID);
		}
		return jacksonUtil.Map2json(map);
	}
	
	
}
