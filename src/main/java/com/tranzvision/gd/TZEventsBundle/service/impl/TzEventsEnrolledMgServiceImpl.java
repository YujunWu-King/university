/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.BatchProcessDetailsImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzHdBmrdcAetMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzHdbmrClueTMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzLxfsinfoTblMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzHdBmrdcAet;
import com.tranzvision.gd.TZEventsBundle.model.PsTzHdbmrClueTKey;
import com.tranzvision.gd.TZEventsBundle.model.PsTzLxfsinfoTbl;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsBmbTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 活动报名人管理列表页面操作类，原PS：TZ_GD_BMRGL_PKG:TZ_GD_BMRGL_CLS
 * 
 * @author SHIHUA
 * @since 2016-02-24
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsEnrolledMgServiceImpl")
public class TzEventsEnrolledMgServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private MySqlLockService mySqlLockService;

	@Autowired
	private PsTzNaudlistTMapper psTzNaudlistTMapper;

	@Autowired
	private PsTzLxfsinfoTblMapper psTzLxfsinfoTblMapper;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzHdBmrdcAetMapper psTzHdBmrdcAetMapper;
	
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;

	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	
	@Autowired
	private BatchProcessDetailsImpl batchProcessDetailsImpl;
	
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	
	@Autowired
	private PsTzHdbmrClueTMapper psTzHdbmrClueTMapper;
	
	@Autowired
	private PsTzXsxsBmbTMapper psTzXsxsBmbTMapper;
	

	/**
	 * 查询活动下的报名人信息
	 * 
	 * @param strParams
	 * @param numLimit
	 * @param numStart
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("type") 
					&& "expHistory".equals(jacksonUtil.getString("type"))){
				// 排序字段
				String[][] orderByArr = new String[][] {{"TZ_STARTTIME","DESC"}};

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_ART_ID", "TZ_DLZH_ID", "TZ_DR_TASK_DESC", "TZ_STARTTIME", "PROCESSINSTANCE",
						"PROCESS_STATUS","TZ_REALNAME"};

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

				if (obj != null && obj.length > 0) {

					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);

						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("actId", rowList[0]);
						mapList.put("loginId", rowList[1]);
						mapList.put("fileName", rowList[2]);
						mapList.put("bgTime", rowList[3]);
						mapList.put("procInsId", rowList[4]);
						mapList.put("procState", rowList[5]);
						mapList.put("czPerName", rowList[6]);
						
						String stateDescr = batchProcessDetailsImpl.getBatchProcessStatusDescsr(rowList[5]);
						mapList.put("procStaDescr", stateDescr);
						
						listData.add(mapList);
					}

					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);

				}
			}else{
				String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				// 排序字段
				String[][] orderByArr = new String[][] {{"TZ_REG_TIME","DESC"}};

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_CYR_NAME", "TZ_ZY_SJ", "TZ_ZY_EMAIL", "TZ_NREG_STAT", "TZ_ZXBM_LY",
						"TZ_BMCY_ZT", "TZ_BZSM", "TZ_GD_BOOLEAN", "TZ_REG_TIME_CHAR", "TZ_HD_QDM", "TZ_ZXBM_XXX_001",
						"TZ_ZXBM_XXX_002", "TZ_ZXBM_XXX_003", "TZ_ZXBM_XXX_004", "TZ_ZXBM_XXX_005", "TZ_ZXBM_XXX_006",
						"TZ_ZXBM_XXX_007", "TZ_ZXBM_XXX_008", "TZ_ZXBM_XXX_009", "TZ_ZXBM_XXX_010", "TZ_ZXBM_XXX_011",
						"TZ_ZXBM_XXX_012", "TZ_ZXBM_XXX_013", "TZ_ZXBM_XXX_014", "TZ_ZXBM_XXX_015", "TZ_ZXBM_XXX_016",
						"TZ_ZXBM_XXX_017", "TZ_ZXBM_XXX_018", "TZ_ZXBM_XXX_019", "TZ_ZXBM_XXX_020", "TZ_BMZT_DESC",
						"TZ_BMQD_DESC", "TZ_QDZT_DESC", "TZ_ART_ID", "TZ_HD_BMR_ID", "TZ_MSSQH" };

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

				if (obj != null && obj.length > 0) {

					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);

						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("TZ_CYR_NAME", rowList[0]);
						mapList.put("TZ_ZY_SJ", rowList[1]);
						mapList.put("TZ_ZY_EMAIL", rowList[2]);
						mapList.put("applyStatus", rowList[3]);
						mapList.put("channel", rowList[4]);
						mapList.put("signStatus", rowList[5]);
						mapList.put("remark", rowList[6]);
						mapList.put("isReg", rowList[7]);
						mapList.put("applyTime", rowList[8]);
						mapList.put("signCode", rowList[9]);
						mapList.put("TZ_ZXBM_XXX_001", rowList[10]);
						mapList.put("TZ_ZXBM_XXX_002", rowList[11]);
						mapList.put("TZ_ZXBM_XXX_003", rowList[12]);
						mapList.put("TZ_ZXBM_XXX_004", rowList[13]);
						mapList.put("TZ_ZXBM_XXX_005", rowList[14]);
						mapList.put("TZ_ZXBM_XXX_006", rowList[15]);
						mapList.put("TZ_ZXBM_XXX_007", rowList[16]);
						mapList.put("TZ_ZXBM_XXX_008", rowList[17]);
						mapList.put("TZ_ZXBM_XXX_009", rowList[18]);
						mapList.put("TZ_ZXBM_XXX_010", rowList[19]);
						mapList.put("TZ_ZXBM_XXX_011", rowList[20]);
						mapList.put("TZ_ZXBM_XXX_012", rowList[21]);
						mapList.put("TZ_ZXBM_XXX_013", rowList[22]);
						mapList.put("TZ_ZXBM_XXX_014", rowList[23]);
						mapList.put("TZ_ZXBM_XXX_015", rowList[24]);
						mapList.put("TZ_ZXBM_XXX_016", rowList[25]);
						mapList.put("TZ_ZXBM_XXX_017", rowList[26]);
						mapList.put("TZ_ZXBM_XXX_018", rowList[27]);
						mapList.put("TZ_ZXBM_XXX_019", rowList[28]);
						mapList.put("TZ_ZXBM_XXX_020", rowList[29]);
						mapList.put("applyStatusDesc", rowList[30]);
						mapList.put("channelDesc", rowList[31]);
						mapList.put("signStatusDesc", rowList[32]);
						mapList.put("activityId", rowList[33]);
						mapList.put("applicantsId", rowList[34]);
						mapList.put("msApplyNo", rowList[35]);
						
						
						//活动线索
						String sql = "select TZ_LEAD_ID from PS_TZ_HDBMR_CLUE_T A where TZ_HD_BMR_ID=? and exists(select 'Y' from PS_TZ_XSXS_INFO_T where TZ_LEAD_ID=A.TZ_LEAD_ID and TZ_JG_ID=?) limit 0,1";
						String leadId = sqlQuery.queryForObject(sql, new Object[]{ rowList[34], orgId }, "String");
						if(leadId == null){
							leadId = "";
						}
						
						mapList.put("leadId", leadId);
						if(!"".equals(leadId)){
							mapList.put("haveClue", '是');
						}else{
							mapList.put("haveClue", '否');
						}

						listData.add(mapList);
					}

					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);

	}

	/**
	 * 活动报名人信息修改
	 * 
	 * @param actData
	 * @param errorMsg
	 * @return
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 活动编号
				String strActivityId = jacksonUtil.getString("activityId");
				// 报名人编号
				String strBmrId = jacksonUtil.getString("applicantsId");
				// 报名渠道
				String strBmqd = jacksonUtil.getString("channel");
				// 签到状态
				String signStatus = jacksonUtil.getString("signStatus");
				// 备注
				String strBz = jacksonUtil.getString("remark");
				// Email
				String strEmail = jacksonUtil.getString("TZ_ZY_EMAIL");
				// Mobile
				String strMobile = jacksonUtil.getString("TZ_ZY_SJ");

				String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckBmrEmail");
				String strCheckEmailResult = sqlQuery.queryForObject(sql,
						new Object[] { strActivityId, strEmail, strBmrId }, "String");

				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckBmrMobile");
				String strCheckMobileResult = sqlQuery.queryForObject(sql,
						new Object[] { strActivityId, strMobile, strBmrId }, "String");

				if ("Y".equals(strCheckEmailResult) || "Y".equals(strCheckMobileResult)) {
					/* 如果电话或邮箱重复，该条记录不修改 */
				} else {

					sql = "select TZ_ZXBM_XXX_ID from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=?";
					List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { strActivityId });

					// 活动报名表
					PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
					psTzNaudlistT.setTzArtId(strActivityId);
					psTzNaudlistT.setTzHdBmrId(strBmrId);
					psTzNaudlistT.setTzZxbmLy(strBmqd);
					psTzNaudlistT.setTzBmcyZt(signStatus);
					psTzNaudlistT.setTzBzsm(strBz);

					// 联系方式表
					PsTzLxfsinfoTbl psTzLxfsinfoTbl = new PsTzLxfsinfoTbl();
					psTzLxfsinfoTbl.setTzLxfsLy("HDBM");
					psTzLxfsinfoTbl.setTzLydxId(strBmrId);

					for (Map<String, Object> mapData : listData) {
						String strXXX_ID = mapData.get("TZ_ZXBM_XXX_ID") == null ? ""
								: String.valueOf(mapData.get("TZ_ZXBM_XXX_ID"));

						String strXXXVal = jacksonUtil.getString(strXXX_ID);
						switch (strXXX_ID) {
						case "TZ_ZY_SJ":
							psTzLxfsinfoTbl.setTzZySj(strMobile);
							break;
						case "TZ_ZY_EMAIL":
							psTzLxfsinfoTbl.setTzZyEmail(strEmail);
							break;

						case "TZ_CYR_NAME":
							psTzNaudlistT.setTzCyrName(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_001":
							psTzNaudlistT.setTzZxbmXxx001(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_002":
							psTzNaudlistT.setTzZxbmXxx002(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_003":
							psTzNaudlistT.setTzZxbmXxx003(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_004":
							psTzNaudlistT.setTzZxbmXxx004(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_005":
							psTzNaudlistT.setTzZxbmXxx005(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_006":
							psTzNaudlistT.setTzZxbmXxx006(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_007":
							psTzNaudlistT.setTzZxbmXxx007(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_008":
							psTzNaudlistT.setTzZxbmXxx008(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_009":
							psTzNaudlistT.setTzZxbmXxx009(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_010":
							psTzNaudlistT.setTzZxbmXxx010(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_011":
							psTzNaudlistT.setTzZxbmXxx011(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_012":
							psTzNaudlistT.setTzZxbmXxx012(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_013":
							psTzNaudlistT.setTzZxbmXxx013(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_014":
							psTzNaudlistT.setTzZxbmXxx014(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_015":
							psTzNaudlistT.setTzZxbmXxx015(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_016":
							psTzNaudlistT.setTzZxbmXxx016(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_017":
							psTzNaudlistT.setTzZxbmXxx017(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_018":
							psTzNaudlistT.setTzZxbmXxx018(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_019":
							psTzNaudlistT.setTzZxbmXxx019(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_020":
							psTzNaudlistT.setTzZxbmXxx020(strXXXVal);
							break;

						}

					}

					sql = "select 'Y' from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_HD_BMR_ID=?";
					String recExists = sqlQuery.queryForObject(sql, new Object[] { strActivityId, strBmrId }, "String");
					if ("Y".equals(recExists)) {
						psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
					}

					sql = "select 'Y' from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='HDBM' and TZ_LYDX_ID=?";
					recExists = sqlQuery.queryForObject(sql, new Object[] { strBmrId }, "String");
					if ("Y".equals(recExists)) {
						psTzLxfsinfoTblMapper.updateByPrimaryKeySelective(psTzLxfsinfoTbl);
					} else {
						psTzLxfsinfoTblMapper.insertSelective(psTzLxfsinfoTbl);
					}

				}

			}

			errorMsg[0] = "0";
			errorMsg[1] = "保存成功。";

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "保存失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 报名、撤销报名和批量设置签到状态
	 * 
	 * @param strType
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			// 活动编号
			String strActivityId = "";
			if(jacksonUtil.containsKey("activityId")){
				strActivityId = jacksonUtil.getString("activityId");
			}

			String sql = "";

			switch (strType) {
			case "setStatus":
				// 批量设置报名签到状态

				// 签到状态
				String signStatus = jacksonUtil.getString("signStatus");

				// 报名人信息
				List<?> listBmrIds = jacksonUtil.getList("bmrIds");
				for (Object obj : listBmrIds) {
					String tzHdBmrId = obj == null ? "" : String.valueOf(obj);
					if ("".equals(tzHdBmrId)) {
						continue;
					}

					PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
					psTzNaudlistT.setTzArtId(strActivityId);
					psTzNaudlistT.setTzHdBmrId(tzHdBmrId);
					psTzNaudlistT.setTzBmcyZt(signStatus);
					psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);

				}

				break;
			case "setRegSta":
				// 报名、撤销报名
				String strRegStatus = jacksonUtil.getString("applyStatus");
				String strBmrId = jacksonUtil.getString("applicantsId");

				// 活动席位数
				sql = "select TZ_XWS from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
				int numXW = sqlQuery.queryForObject(sql, new Object[] { strActivityId }, "int");

				// 查询报名人数前就要锁表，不然同时报名的话，就可能超过允许报名的人数
				mySqlLockService.lockRow(sqlQuery, "TZ_NAUDLIST_T");

				// 已报名人数
				sql = "select COUNT(1) from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_NREG_STAT='1'";
				int numYbm = sqlQuery.queryForObject(sql, new Object[] { strActivityId }, "int");

				// 取出报名最早的状态为“等待”的报名人
				sql = "select TZ_HD_BMR_ID from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_NREG_STAT='4' order by TZ_REG_TIME asc limit 0,1";
				String waitBmr = sqlQuery.queryForObject(sql, new Object[] { strActivityId }, "String");

				// 报名状态为1-已报名，4-等候
				if ("1".equals(strRegStatus) || "4".equals(strRegStatus)) {

					PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
					psTzNaudlistT.setTzArtId(strActivityId);
					psTzNaudlistT.setTzHdBmrId(strBmrId);
					psTzNaudlistT.setTzNregStat("3");
					int cancelRst = psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
					if (cancelRst > 0) {
						errorMsg[0] = "0";
						errorMsg[1] = "撤销报名成功";

						if (null != waitBmr && !"".equals(waitBmr)) {
							if ("1".equals(strRegStatus)) {
								// 将等待的报名人设置为已报名
								psTzNaudlistT.setTzArtId(strActivityId);
								psTzNaudlistT.setTzHdBmrId(waitBmr);
								psTzNaudlistT.setTzNregStat("1");
								psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
							}
						}

					} else {
						errorMsg[0] = "0";
						errorMsg[1] = "撤销报名失败";
					}

				} else if ("2".equals(strRegStatus) || "3".equals(strRegStatus)) {
					// 报名状态为2-未报名、3-已撤销;
					String mbDesc = "";
					PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
					psTzNaudlistT.setTzArtId(strActivityId);
					psTzNaudlistT.setTzHdBmrId(strBmrId);

					if (numYbm < numXW) {
						// 已报名数小于席位数，则允许报名
						psTzNaudlistT.setTzNregStat("1");
						mbDesc = "报名成功";
					} else {
						// 否则进入等待队列
						psTzNaudlistT.setTzNregStat("4");
						mbDesc = "报名成功，报名席位数已满，进入等候状态";
					}

					int bmRst = psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
					if (bmRst > 0) {
						errorMsg[0] = "0";
						errorMsg[1] = mbDesc;
					} else {
						errorMsg[0] = "1";
						errorMsg[1] = "报名失败";
					}

				}

				// 解锁
				mySqlLockService.unlockRow(sqlQuery);

				break;
			case "creaColumn":
				// 动态获取报名信息列

				sql = "select TZ_ZXBM_XXX_ID,TZ_ZXBM_XXX_NAME,TZ_ZXBM_XXX_ZSXS from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=? order by TZ_PX_XH asc";
				List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { strActivityId });

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

				for (Map<String, Object> mapData : listData) {
					String colId = mapData.get("TZ_ZXBM_XXX_ID") == null ? ""
							: String.valueOf(mapData.get("TZ_ZXBM_XXX_ID"));
					String colName = mapData.get("TZ_ZXBM_XXX_NAME") == null ? ""
							: String.valueOf(mapData.get("TZ_ZXBM_XXX_NAME"));
					String etype = mapData.get("TZ_ZXBM_XXX_ZSXS") == null ? ""
							: String.valueOf(mapData.get("TZ_ZXBM_XXX_ZSXS"));

					ArrayList<Map<String, Object>> listItemJson = new ArrayList<Map<String, Object>>();

					switch (etype) {
					case "1":
						break;
					case "2":
						sql = "select TZ_XXX_TRANS_ID,TZ_XXX_TRANS_NAME from PS_TZ_XXX_TRANS_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? order by TZ_PX_XH asc";
						List<Map<String, Object>> listItems = sqlQuery.queryForList(sql,
								new Object[] { strActivityId, colId });

						for (Map<String, Object> mapItem : listItems) {
							String tValue = mapItem.get("TZ_XXX_TRANS_ID") == null ? ""
									: String.valueOf(mapItem.get("TZ_XXX_TRANS_ID"));
							String tvDesc = mapItem.get("TZ_XXX_TRANS_NAME") == null ? ""
									: String.valueOf(mapItem.get("TZ_XXX_TRANS_NAME"));

							Map<String, Object> mapItemJson = new HashMap<String, Object>();
							mapItemJson.put("transID", tValue);
							mapItemJson.put("transName", tvDesc);

							listItemJson.add(mapItemJson);

						}

						break;
					}

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("columnId", colId);
					mapJson.put("columnName", colName);
					mapJson.put("editType", etype);
					mapJson.put("transVal", listItemJson);

					listJson.add(mapJson);
				}

				mapRet.put("appColumn", listJson);

				strRet = jacksonUtil.Map2json(mapRet);

				break;
			case "EXPORT":
				/*活动报名人信息导出改用批处理
				// 导出报名人信息
				List<?> listBmrIdsExport = new ArrayList<String>();
				
				//导出选中报名人
				if(jacksonUtil.containsKey("bmrIds")){
					listBmrIdsExport = jacksonUtil.getList("bmrIds");
				}
				//导出搜索结果报名人
				if(jacksonUtil.containsKey("searchSql")){
					String searchSql = jacksonUtil.getString("searchSql");
					
					List<Map<String,Object>> bmrList = sqlQuery.queryForList(searchSql);
					
					List<Object> bmrIdList = new ArrayList<Object>();
					for(Map<String,Object> bmrMap: bmrList){
						Object[] bmrIdArr = bmrMap.values().toArray();
						if(bmrIdArr.length > 0){
							bmrIdList.add(bmrIdArr[0]);
						}
					}
					
					listBmrIdsExport = bmrIdList;
				}

				String filepath = this.exportApplyInfo(strActivityId, listBmrIdsExport, errorMsg);

				mapRet.put("fileUrl", filepath);

				strRet = jacksonUtil.Map2json(mapRet);
				*/
				
				strRet = this.exportBmrInfoBatch(strParams, errorMsg);
				
				break;
				
			case "getSearchSql":
				/*可配置搜索查询语句*/
				String[] resultFldArray = {"TZ_HD_BMR_ID"};
				
				String[][] orderByArr=null;
				
				String searchSql = fliterForm.getQuerySQL(resultFldArray,orderByArr,strParams,errorMsg);
				
				mapRet.put("SQL", searchSql);
				strRet = jacksonUtil.Map2json(mapRet);
				break;
			case "DOWNLOAD":
				//下载导出excel
				String filePath = "";
				String strProcInsId = jacksonUtil.getString("procInsId");
				if(!"".equals(strProcInsId)){
					int procInsId = Integer.parseInt(strProcInsId);
					
					PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
					if(psTzExcelDattT != null){
						filePath = psTzExcelDattT.getTzFwqFwlj();
						if(!"".equals(filePath) && filePath != null){
							filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath() + filePath;
						}
					}
				}
				mapRet.put("filePath", filePath);
				strRet = jacksonUtil.Map2json(mapRet);
				
				break;
			case "tzCreateClue":
				strRet = this.tzCreateClue(strParams, errorMsg);
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
	 * 导出报名人信息至Excel
	 * 
	 * @param strHdId
	 * @param listBmrIds
	 * @param errorMsg
	 * @return 生成的excel文件路径
	 */
	@SuppressWarnings("unused")
	private String exportApplyInfo(String strHdId, List<?> listBmrIds, String[] errorMsg) {
		String strRet = "";

		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();

			// 活动报名信息excel存储路径
			String eventExcelPath = "/events/xlsx";

			// 完整的存储路径
			String fileDirPath = fileBasePath + eventExcelPath;

			// 生成数据
			// 表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			List<String[]> listDataFields = new ArrayList<String[]>();
			String sql = "SELECT TZ_ZXBM_XXX_ID,TZ_ZXBM_XXX_NAME,TZ_ZXBM_XXX_ZSXS FROM PS_TZ_ZXBM_XXX_T WHERE TZ_ART_ID=? ORDER BY TZ_PX_XH";
			List<Map<String, Object>> listHeaderFields = sqlQuery.queryForList(sql, new Object[] { strHdId });
			for (Map<String, Object> mapField : listHeaderFields) {
				// 信息项ID
				String strXxxId = mapField.get("TZ_ZXBM_XXX_ID") == null ? ""
						: String.valueOf(mapField.get("TZ_ZXBM_XXX_ID"));
				// 控件类型
				String strKjType = mapField.get("TZ_ZXBM_XXX_ZSXS") == null ? ""
						: String.valueOf(mapField.get("TZ_ZXBM_XXX_ZSXS"));
				// 信息项名称
				String strXxxName = mapField.get("TZ_ZXBM_XXX_NAME") == null ? ""
						: String.valueOf(mapField.get("TZ_ZXBM_XXX_NAME"));

				dataCellKeys.add(new String[] { strXxxId, strXxxName });

				listDataFields.add(new String[] { strXxxId, strKjType });

			}

			dataCellKeys.add(new String[] { "bmzt", "报名状态" });
			dataCellKeys.add(new String[] { "bmqd", "报名渠道" });
			dataCellKeys.add(new String[] { "qdzt", "签到状态" });

			// 数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			int num = listBmrIds.size();
			for (int i = 0; i < num; i++) {

				String bmrid = String.valueOf(listBmrIds.get(i));

				Map<String, Object> mapData = new HashMap<String, Object>();

				// 循环信息项，对于控件类型为2-下拉框，取描述值
				for (String[] idAry : listDataFields) {
					String fldName = idAry[0];
					sql = "SELECT " + fldName
							+ " FROM PS_TZ_NAUDLIST_T A LEFT JOIN (SELECT * FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='HDBM') B ON (A.TZ_HD_BMR_ID=B.TZ_LYDX_ID) WHERE A.TZ_ART_ID=? AND A.TZ_HD_BMR_ID=?";
					String fldVal = sqlQuery.queryForObject(sql, new Object[] { strHdId, bmrid }, "String");

					// 控件类型为2-下拉框，取描述值
					if ("2".equals(idAry[1])) {
						sql = "SELECT TZ_XXX_TRANS_NAME FROM PS_TZ_XXX_TRANS_T WHERE TZ_ART_ID=? AND TZ_ZXBM_XXX_ID=? AND TZ_XXX_TRANS_ID=?";
						fldVal = sqlQuery.queryForObject(sql, new Object[] { strHdId, fldName, fldVal }, "String");
					}

					mapData.put(fldName, fldVal);

				}

				// 报名渠道、签到状态;
				sql = "SELECT TZ_NREG_STAT,TZ_ZXBM_LY,TZ_BMCY_ZT FROM  PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_HD_BMR_ID=?";
				Map<String, Object> mapqdzt = sqlQuery.queryForMap(sql, new Object[] { strHdId, bmrid });
				String strBmqd = "";
				String strQdzt = "";
				String strBmzt = "";
				if (mapqdzt != null) {
					strBmqd = mapqdzt.get("TZ_ZXBM_LY") == null ? "" : String.valueOf(mapqdzt.get("TZ_ZXBM_LY"));
					strQdzt = mapqdzt.get("TZ_BMCY_ZT") == null ? "" : String.valueOf(mapqdzt.get("TZ_BMCY_ZT"));
					strBmzt = mapqdzt.get("TZ_NREG_STAT") == null ? "" : String.valueOf(mapqdzt.get("TZ_NREG_STAT"));

					/* 报名渠道、签到状态取转换值配置 */
					sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BMR_APPLY_QD' AND TZ_ZHZ_ID=?";
					strBmqd = sqlQuery.queryForObject(sql, new Object[] { strBmqd }, "String");
					sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_MBRGL_CYZT' AND TZ_ZHZ_ID=?";
					strQdzt = sqlQuery.queryForObject(sql, new Object[] { strQdzt }, "String");
					sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BMR_APPLY_STA' AND TZ_ZHZ_ID=?";
					strBmzt = sqlQuery.queryForObject(sql, new Object[] { strBmzt }, "String");
				}

				mapData.put("bmzt", strBmzt);
				mapData.put("bmqd", strBmqd);
				mapData.put("qdzt", strQdzt);

				dataList.add(mapData);

			}

			// 生成本次导出的文件名
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Random random = new Random();
			int max = 999999999;
			int min = 100000000;
			String fileName = simpleDateFormat.format(new Date()) + "_" + oprid.toUpperCase() + "_"
					+ String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";

			ExcelHandle excelHandle = new ExcelHandle(request, fileDirPath, orgid, "apply");
			boolean rst = excelHandle.export2Excel(fileName, dataCellKeys, dataList);
			if (rst) {
				//System.out.println("---------生成的excel文件路径----------");
				strRet = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + excelHandle.getExportExcelPath();
				//System.out.println(strRet);
			} else {
				System.out.println("导出失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

		return strRet;
	}
	
	
	private String exportBmrInfoBatch(String strParams, String[] errorMsg){
		
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try{
			jacksonUtil.json2Map(strParams);
			
			// 活动编号
			String activityId = jacksonUtil.getString("activityId");
			String fileName = jacksonUtil.getString("fileName");
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String downloadPath = getSysHardCodeVal.getDownloadPath();
			
			// 活动报名信息excel存储路径
			String eventExcelPath = "/events/xlsx";
			
			// 完整的存储路径
			String expDirPath = downloadPath + eventExcelPath + "/" + getDateNow();
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
			
			/*生成运行控制ID*/
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		    String s_dt = dateFormate.format(new Date());
			String runCntlId = "HDBMR" + s_dt + "_" + getSeqNum.getSeqNum("TZ_GD_BMRGL_COM", "BMR_EXPORT");
			
			
			PsTzHdBmrdcAet psTzHdBmrdcAet = new PsTzHdBmrdcAet();
			psTzHdBmrdcAet.setRunCntlId(runCntlId);
			psTzHdBmrdcAet.setTzArtId(activityId);
			psTzHdBmrdcAet.setTzRelUrl(expDirPath);
			psTzHdBmrdcAet.setTzJdUrl(absexpDirPath);
			psTzHdBmrdcAet.setTzParamsStr(strParams);
			psTzHdBmrdcAetMapper.insert(psTzHdBmrdcAet);
			
			
			String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			BaseEngine tmpEngine = tzGDObject.createEngineProcess(currentOrgId, "TZ_HDBMR_EXP_PROC");
			//指定调度作业的相关参数
			EngineParameters schdProcessParameters = new EngineParameters();

			schdProcessParameters.setBatchServer("");
			schdProcessParameters.setCycleExpression("");
			schdProcessParameters.setLoginUserAccount(currentAccountId);
			schdProcessParameters.setPlanExcuteDateTime(new Date());
			schdProcessParameters.setRunControlId(runCntlId);
			
			//调度作业
			tmpEngine.schedule(schdProcessParameters);
			
			// 进程实例id;
			int processinstance = tmpEngine.getProcessInstanceID();
			
			if(processinstance>0){

				PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
				psTzExcelDrxxT.setProcessinstance(processinstance);
				psTzExcelDrxxT.setTzComId("TZ_GD_BMRGL_COM");
				psTzExcelDrxxT.setTzPageId("TZ_GD_BMRGL_STD");
				//存放活动ID
				psTzExcelDrxxT.setTzDrLxbh(activityId);
				psTzExcelDrxxT.setTzDrTaskDesc(fileName); 
				psTzExcelDrxxT.setTzStartDtt(new Date());
				psTzExcelDrxxT.setOprid(oprid);
				psTzExcelDrxxT.setTzIsViewAtt("Y");
				psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);
				
				
				// 生成本次导出的文件名
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				Random random = new Random();
				int max = 999999999;
				int min = 100000000;
				String sysFileName = simpleDateFormat.format(new Date()) + "_" + oprid.toUpperCase() + "_"
						+ String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";
				
				PsTzExcelDattT psTzExcelDattT = new PsTzExcelDattT();
				psTzExcelDattT.setProcessinstance(processinstance);
				psTzExcelDattT.setTzSysfileName(sysFileName);
				psTzExcelDattT.setTzFileName(fileName);
				psTzExcelDattT.setTzCfLj("A");
				psTzExcelDattT.setTzFjRecName("");
				psTzExcelDattT.setTzFwqFwlj(""); 
				psTzExcelDattTMapper.insert(psTzExcelDattT);
				
				
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

		return strRet;
	}
	
	
	/***
	 * 删除导出历史记录
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				//进程实例ID
				int procInsId = Integer.parseInt(jacksonUtil.getString("procInsId"));
				
				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
				if(psTzExcelDattT != null){
					String filePath = psTzExcelDattT.getTzFwqFwlj();
					if(!"".equals(filePath) && filePath != null){
						filePath = request.getServletContext().getRealPath(filePath);
						
						File file = new File(filePath);
						if(file.exists() && file.isFile()){
							file.delete();
						}
					}
				}
				
				psTzExcelDrxxTMapper.deleteByPrimaryKey(procInsId);
				psTzExcelDattTMapper.deleteByPrimaryKey(procInsId);
			}

			errMsg[0] = "0";
			errMsg[1] = "保存成功。";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "保存失败。" + e.getMessage();
		}

		return strRet;
	}
	
	
	/**
	 * 创建日期目录名
	 * 
	 * @return
	 */
	private String getDateNow() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		return (new StringBuilder()).append(year).append(month).append(day).toString();
	}
	
	
	
	/**
	 * 创建线索
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String tzCreateClue(String strParams, String[] errorMsg){
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("existsLead", "");
		
		jacksonUtil.json2Map(strParams);
		
		String activityId = jacksonUtil.getString("activityId");
		List<String> bmrIds = (List<String>) jacksonUtil.getList("bmrIds");
		
		String orgId = "";
		int count = 0;
		if(jacksonUtil.containsKey("orgId")){
			orgId = jacksonUtil.getString("orgId");
		}else{
			orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		}
		
		String currOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		String hasLeadName = "";
		if(bmrIds != null && bmrIds.size() > 0){
			for(String bmrId: bmrIds){
				String sql = "select A.TZ_CYR_NAME,A.OPRID,B.TZ_ZY_SJ,B.TZ_ZY_EMAIL from PS_TZ_NAUDLIST_T A left join PS_TZ_LXFSINFO_TBL B on(B.TZ_LXFS_LY='HDBM' and B.TZ_LYDX_ID=A.TZ_HD_BMR_ID) where TZ_ART_ID=? and TZ_HD_BMR_ID=?";
				Map<String,Object> bmrMap = sqlQuery.queryForMap(sql, new Object[]{ activityId, bmrId });
				
				if(bmrMap != null){
					String name = bmrMap.get("TZ_CYR_NAME") == null ? "" : bmrMap.get("TZ_CYR_NAME").toString();
					String oprid = bmrMap.get("OPRID") == null ? "" : bmrMap.get("OPRID").toString();
					String mobile = bmrMap.get("TZ_ZY_SJ") == null ? "" : bmrMap.get("TZ_ZY_SJ").toString();
					String email = bmrMap.get("TZ_ZY_EMAIL") == null ? "" : bmrMap.get("TZ_ZY_EMAIL").toString();
					
					String leadSql = "select TZ_LEAD_ID from PS_TZ_HDBMR_CLUE_T A where TZ_HD_BMR_ID=? and exists(select 'Y' from PS_TZ_XSXS_INFO_T where TZ_LEAD_ID=A.TZ_LEAD_ID and TZ_JG_ID=?) limit 0,1";
					String leadId = sqlQuery.queryForObject(leadSql, new Object[]{ bmrId, orgId }, "String");
					
					if(leadId != null && !"".equals(leadId)){
						count ++;
						if(count < 10){
							if("".equals(hasLeadName)){
								hasLeadName = name;
							}else{
								hasLeadName += "，" + name;
							}
						}
					}else{
						String TZ_LEAD_ID = String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
						
						PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = new PsTzXsxsInfoTWithBLOBs();
						psTzXsxsInfoT.setTzLeadId(TZ_LEAD_ID);
						psTzXsxsInfoT.setTzJgId(orgId);

						psTzXsxsInfoT.setTzRsfcreateWay("E"); /*营销活动*/
						psTzXsxsInfoT.setTzLeadStatus("A");
						
						psTzXsxsInfoT.setTzRealname(name);
						psTzXsxsInfoT.setTzKhOprid(oprid);
						psTzXsxsInfoT.setTzEmail(email);
						psTzXsxsInfoT.setTzMobile(mobile);
						psTzXsxsInfoT.setRowAddedDttm(new Date());
						psTzXsxsInfoT.setRowAddedOprid(currOprid);
						psTzXsxsInfoT.setRowLastmantDttm(new Date());
						psTzXsxsInfoT.setRowLastmantOprid(currOprid);
						
						int rtn = psTzXsxsInfoTMapper.insert(psTzXsxsInfoT);
						
						if(rtn > 0){
							PsTzHdbmrClueTKey psTzHdbmrClueTKey = new PsTzHdbmrClueTKey();
							psTzHdbmrClueTKey.setTzHdBmrId(bmrId);
							psTzHdbmrClueTKey.setTzLeadId(TZ_LEAD_ID);
							
							psTzHdbmrClueTMapper.insert(psTzHdbmrClueTKey);
							
							//线索关联报名表
							sql = "select max(TZ_APP_INS_ID) as TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where OPRID=?";
							Long appInsId = sqlQuery.queryForObject(sql, new Object[]{ oprid }, "Long");
							if(appInsId != null && appInsId > 0){
								PsTzXsxsBmbT psTzXsxsBmbT = new PsTzXsxsBmbT(); 
								psTzXsxsBmbT.setTzLeadId(TZ_LEAD_ID);
								psTzXsxsBmbT.setTzAppInsId(appInsId);
								psTzXsxsBmbT.setRowAddedDttm(new Date());
								psTzXsxsBmbT.setRowAddedOprid(currOprid);
								psTzXsxsBmbT.setRowLastmantDttm(new Date());
								psTzXsxsBmbT.setRowLastmantOprid(currOprid);
								
								psTzXsxsBmbTMapper.insert(psTzXsxsBmbT);
							}
						}
					}
				}
			}
			
			if(count >= 10){
				hasLeadName += "等共" + count + "人";
			}else if(count > 0){
				hasLeadName += "共" + count + "人";
			}
			rtnMap.replace("existsLead", hasLeadName);
		}
		return jacksonUtil.Map2json(rtnMap);
	}
}
