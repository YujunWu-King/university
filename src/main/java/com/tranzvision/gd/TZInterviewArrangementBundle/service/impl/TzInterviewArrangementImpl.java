package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClassInfTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBatchTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBatchT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBatchTKey;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMssjArrTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMsyySetTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMssjArrTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMssjArrTblKey;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyySetTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyySetTblKey;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzMszgTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMszgT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 批次面试时间安排
 * @author zhang lang 
 * 原PS：TZ_GD_MS_ARR_PKG:TZ_GD_MS_CAL_ARR_CLS
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewArrangementImpl")
public class TzInterviewArrangementImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzClsBatchTMapper psTzClsBatchTMapper;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;
	
	@Autowired
	private PsTzMsyySetTblMapper psTzMsyySetTblMapper;
	
	@Autowired
	private PsTzMssjArrTblMapper psTzMssjArrTblMapper;
	
	@Autowired
	private PsTzMszgTMapper psTzMszgTMapper;
	

	/*
	 * 获取指定班级批次信息
	 * @see com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl#tzQuery(java.lang.String, java.lang.String[])
	 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String str_classID = jacksonUtil.getString("classID");
			String str_batchID = jacksonUtil.getString("batchID");
			
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);

			if (null != str_classID && !"".equals(str_classID) && null != str_batchID && !"".equals(str_batchID)) {

				PsTzClsBatchT psTzClsBatchT = new PsTzClsBatchT();
				PsTzClsBatchTKey psTzClsBatchTKey = new PsTzClsBatchTKey();
				psTzClsBatchTKey.setTzClassId(str_classID);
				psTzClsBatchTKey.setTzBatchId(str_batchID);

				psTzClsBatchT = psTzClsBatchTMapper.selectByPrimaryKey(psTzClsBatchTKey);

				PsTzClassInfT psTzClassInfT = new PsTzClassInfT();
				psTzClassInfT = psTzClassInfTMapper.selectByPrimaryKey(str_classID);
				
				if (psTzClsBatchT != null) {
					Map<String, Object> map = new HashMap<>();

					map.put("classID", str_classID);
					map.put("className", psTzClassInfT.getTzClassName());
					map.put("batchID", str_batchID);
					map.put("batchName", psTzClsBatchT.getTzBatchName());
					if(psTzClsBatchT.getTzStartDt() != null){
						map.put("batchStartDate", dateSimpleDateFormat.format(psTzClsBatchT.getTzStartDt()));
					}
					if(psTzClsBatchT.getTzEndDt() != null){
						map.put("batchEndDate", dateSimpleDateFormat.format(psTzClsBatchT.getTzEndDt()));
					}
					
					PsTzMsyySetTblKey psTzMsyySetTblKey = new PsTzMsyySetTblKey();
					psTzMsyySetTblKey.setTzClassId(str_classID);
					psTzMsyySetTblKey.setTzBatchId(str_batchID);
					
					PsTzMsyySetTbl psTzMsyySetTbl = psTzMsyySetTblMapper.selectByPrimaryKey(psTzMsyySetTblKey);
					if(psTzMsyySetTbl != null){
						map.put("openDate", dateSimpleDateFormat.format(psTzMsyySetTbl.getTzOpenDt()));
						map.put("openTime", timeSimpleDateFormat.format(psTzMsyySetTbl.getTzOpenTm()));
						map.put("closeDate", dateSimpleDateFormat.format(psTzMsyySetTbl.getTzCloseDt()));
						map.put("closeTime", timeSimpleDateFormat.format(psTzMsyySetTbl.getTzCloseTm()));
						map.put("openStatus", psTzMsyySetTbl.getTzOpenSta());
						map.put("frontView", psTzMsyySetTbl.getTzShowFront());
						map.put("descr", psTzMsyySetTbl.getTzDescr());
						map.put("material", psTzMsyySetTbl.getTzMaterial());
					}

					strRtn = jacksonUtil.Map2json(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRtn;
	}


	/*
	 * 获取指定班级批次下面试安排信息
	 * @see com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl#tzQueryList(java.lang.String, int, int, java.lang.String[])
	 */
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
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");

			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if(null != classID && !"".equals(classID) && null != batchID && !"".equals(batchID)){
				//查询面试安排总数
				String sql = "SELECT COUNT(*) FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[]{classID, batchID}, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsPlanAppoPersonCount");
					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[]{classID, batchID, start, limit});

					for(Map<String,Object> mapData : listData){
						Map<String,Object> mapJson = new HashMap<String,Object>();
						
						mapJson.put("classID", classID);
						mapJson.put("batchID", batchID);
						mapJson.put("msJxNo", mapData.get("TZ_MS_PLAN_SEQ"));
						mapJson.put("maxPerson", mapData.get("TZ_MSYY_COUNT"));
						mapJson.put("msDate", mapData.get("TZ_MS_DATE"));
						mapJson.put("bjMsStartTime", mapData.get("TZ_START_TM"));
						mapJson.put("bjMsEndTime", mapData.get("TZ_END_TM"));
						mapJson.put("msLocation", mapData.get("TZ_MS_LOCATION"));
						mapJson.put("msXxBz", mapData.get("TZ_MS_ARR_DEMO"));
						mapJson.put("msOpenStatus", mapData.get("TZ_MS_OPEN_STA"));
						mapJson.put("releaseOrUndo", mapData.get("TZ_MS_PUB_STA"));
						mapJson.put("appoPerson", mapData.get("TZ_YY_COUNT"));
						
						listJson.add(mapJson);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
			case "saveMsArrInfo":
				//保存批次面试预约安排 
				strRet = this.saveMsArrInfo(strParams,errorMsg);
				break;
			case "publish":
				//发布、撤销
				strRet = this.publish(strParams,errorMsg);
				break;
			case "publishpl":
				//发布选中记录
				strRet = this.publishpl(strParams,errorMsg);
				break;
			case "revokepl":
				//撤销选中记录
				strRet = this.revokepl(strParams, errorMsg);
				break;
			case "queryAudIDsArr":
				strRet = this.queryAudIDsArr(strParams, errorMsg);
				break;
			case "getModalId":
				strRet = this.tzGetModalId(strParams, errorMsg);
				break;
			case "exportMsPlan":
				strRet = this.tzExportMsPlan(strParams, errorMsg);
				break;
			case "importMsPlan":
				strRet = this.tzImportMsPlan(strParams, errorMsg);
				break;
			case "publishInterview":
				//面试发布预约
				strRet = this.publishInterview(strParams, errorMsg);
				break;
			case "exportKsMd":
				strRet = this.tzExportKsMd(strParams, errorMsg);
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
	 * 发布面试预约
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String publishInterview(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			long tzAppInsId = 0;
			String TZ_JG_ID = "";
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String batchName = jacksonUtil.getString("batchName");
			String material = jacksonUtil.getString("material");
			
			
			/*PsTzMsyySetTblKey pmstk  = new PsTzMsyySetTblKey();
			pmstk.setTzClassId(classID);
			pmstk.setTzBatchId(batchID);
			PsTzMsyySetTbl ptmst = psTzMsyySetTblMapper.selectByPrimaryKey(pmstk);
			ptmst.setTzMaterial(material);
			psTzMsyySetTblMapper.updateByPrimaryKeyWithBLOBs(ptmst);*/
			
			/*//从cookie中获取机构id
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("tzmo")) {
					TZ_JG_ID = cookie.getValue();
				}
			}
			*/
			TZ_JG_ID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			String sql1 = "select TZ_MS_PLAN_SEQ,OPRID from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=?";
			
			String sql2 = "select A.TZ_APP_INS_ID from PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B\n" + 
					" where A.OPRID=? and A.TZ_CLASS_ID =B.TZ_CLASS_ID and B.TZ_JG_ID=?\n" + 
					"order by A.TZ_APP_INS_ID desc limit 1";
			
			String sql3 = "select * from PS_TZ_MSSJ_ARR_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and TZ_MS_PLAN_SEQ=?";
			
			//String sql4 = "select DISTINCT TZ_MS_PLAN_SEQ from PS_TZ_MSYY_KS_TBL  where TZ_CLASS_ID=? and TZ_BATCH_ID=?;";
			
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql1, new Object[] {classID, batchID});
			String msDate = "";
			String msLocation = "";
			String bjMsStartTime = "";
			String msXxBz = "";
			String oprID = "";
			String TZ_MS_PLAN_SEQ = "";
			
			if(list != null && list.size() != 0) {
				for (Map<String, Object> map : list) {
					oprID = map.get("OPRID") + "";
					TZ_MS_PLAN_SEQ = map.get("TZ_MS_PLAN_SEQ") + "";
					
					//查询报名表编号
					String tzAppInsId_ = jdbcTemplate.queryForObject(sql2, new Object[] {oprID, TZ_JG_ID}, "String");
					if(tzAppInsId_ != null) {
						tzAppInsId = Long.valueOf(tzAppInsId_);
					}else {
						System.out.println("TZ_MS_PLAN_SEQ:" + TZ_MS_PLAN_SEQ);
						System.out.println("oprID:" + oprID);
					}
					//查询面试安排信息
					Map<String, Object> map2 = jdbcTemplate.queryForMap(sql3, new Object[] {classID, batchID, TZ_MS_PLAN_SEQ});
					msDate = map2.get("TZ_MS_DATE") + "";
					msLocation = map2.get("TZ_MS_LOCATION") + "";
					bjMsStartTime = map2.get("TZ_START_TM") + "";
					msXxBz = map2.get("TZ_MS_ARR_DEMO") + "";
					
					boolean flag = true;
					//根据报名表编号查询
					PsTzMszgT ptt = psTzMszgTMapper.selectByPrimaryKey(tzAppInsId);
					if(ptt == null) {
						//如果为空，新建一个
						ptt = new PsTzMszgT();
						ptt.setTzAppInsId(tzAppInsId);
						flag = false;
					}
					ptt.setTzResultCode("是");
					ptt.setTzAddress(msLocation);
					ptt.setTzDate(msDate);
					ptt.setTzMaterial(material);
					ptt.setTzMsBatch(batchName);
					ptt.setTzTime(bjMsStartTime);
					ptt.setTzRemark(msXxBz);
					
					if(flag) {
						psTzMszgTMapper.updateByPrimaryKey(ptt);
					}else {
						psTzMszgTMapper.insert(ptt);
					}
					
				}
			}
			
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

	
	@SuppressWarnings("unchecked")
	private String saveMsArrInfo(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String clearAllTimeArr = jacksonUtil.getString("clearAllTimeArr");//清除所有时间安排
			
			
			Map<String, Object> formData = null;
			formData = jacksonUtil.getMap("formData");
			
			PsTzMsyySetTbl psTzMsyySetTbl = new PsTzMsyySetTbl();
			PsTzMsyySetTblKey psTzMsyySetTblKey = new PsTzMsyySetTblKey();
			
			psTzMsyySetTblKey.setTzClassId(classID);
			psTzMsyySetTblKey.setTzBatchId(batchID);
			psTzMsyySetTbl = psTzMsyySetTblMapper.selectByPrimaryKey(psTzMsyySetTblKey);
			
			
			Date openDate = formData.get("openDate") == null ? null
					: dateSimpleDateFormat.parse(String.valueOf(formData.get("openDate")));
			Date openTime = formData.get("openTime") == null ? null
					: timeSimpleDateFormat.parse(String.valueOf(formData.get("openTime")));
			Date closeDate = formData.get("closeDate") == null ? null
					: dateSimpleDateFormat.parse(String.valueOf(formData.get("closeDate")));
			Date closeTime = formData.get("closeTime") == null ? null
					: timeSimpleDateFormat.parse(String.valueOf(formData.get("closeTime")));
			
			
			if(psTzMsyySetTbl != null){
				psTzMsyySetTbl.setTzOpenDt(openDate);
				psTzMsyySetTbl.setTzOpenTm(openTime);
				psTzMsyySetTbl.setTzCloseDt(closeDate);
				psTzMsyySetTbl.setTzCloseTm(closeTime);
				psTzMsyySetTbl.setTzOpenSta(String.valueOf(formData.get("openStatus")));
				psTzMsyySetTbl.setTzShowFront(String.valueOf(formData.get("frontView")));
				psTzMsyySetTbl.setTzDescr(String.valueOf(formData.get("descr")));
				psTzMsyySetTbl.setTzMaterial(String.valueOf(formData.get("material")));
				psTzMsyySetTblMapper.updateByPrimaryKeyWithBLOBs(psTzMsyySetTbl);
			}else{
				psTzMsyySetTbl = new PsTzMsyySetTbl(); 
				psTzMsyySetTbl.setTzClassId(classID);
				psTzMsyySetTbl.setTzBatchId(batchID);
				psTzMsyySetTbl.setTzOpenDt(openDate);
				psTzMsyySetTbl.setTzOpenTm(openTime);
				psTzMsyySetTbl.setTzCloseDt(closeDate);
				psTzMsyySetTbl.setTzCloseTm(closeTime);
				psTzMsyySetTbl.setTzOpenSta(String.valueOf(formData.get("openStatus")));
				psTzMsyySetTbl.setTzShowFront(String.valueOf(formData.get("frontView")));
				psTzMsyySetTbl.setTzDescr(String.valueOf(formData.get("descr")));
				psTzMsyySetTbl.setTzMaterial(String.valueOf(formData.get("material")));
				psTzMsyySetTblMapper.insert(psTzMsyySetTbl);
			}
			
			PsTzMssjArrTbl psTzMssjArrTbl = new PsTzMssjArrTbl();
			PsTzMssjArrTblKey psTzMssjArrTblKey = new PsTzMssjArrTblKey();
			
			//修改时间安排
			List<Map<String, Object>> updateData = null;
			//Map<String, Object> updateMap = new HashMap<String, Object>();
			updateData = (List<Map<String, Object>>) jacksonUtil.getList("updaterecs");
			for(Map<String, Object> updateMap: updateData){
				
				String strClassID = updateMap.get("classID")==null? null : String.valueOf(updateMap.get("classID"));
				String strBatchID = updateMap.get("batchID")==null? null : String.valueOf(updateMap.get("batchID"));
				String msJxNo = updateMap.get("msJxNo")==null? null : String.valueOf(updateMap.get("msJxNo"));
				
				if(!"".equals(strClassID) && !"".equals(strBatchID) && !"".equals(msJxNo)){
					Short maxPerson = updateMap.get("maxPerson")==null? null : Short.valueOf(String.valueOf(updateMap.get("maxPerson")));
					Date msDate = updateMap.get("msDate")==null? null : dateSimpleDateFormat.parse(String.valueOf(updateMap.get("msDate")));
					Date bjMsStartTime = updateMap.get("bjMsStartTime")==null? null : timeSimpleDateFormat.parse(String.valueOf(updateMap.get("bjMsStartTime")));
					Date bjMsEndTime = updateMap.get("bjMsStartTime")==null? null : timeSimpleDateFormat.parse(String.valueOf(updateMap.get("bjMsEndTime")));
					String msLocation = updateMap.get("msLocation")==null? null : String.valueOf(updateMap.get("msLocation"));
					String msXxBz = updateMap.get("msXxBz")==null? null : String.valueOf(updateMap.get("msXxBz"));
					String releaseOrUndo = updateMap.get("releaseOrUndo")==null? null : String.valueOf(updateMap.get("releaseOrUndo"));
					
					psTzMssjArrTblKey.setTzClassId(strClassID);
					psTzMssjArrTblKey.setTzBatchId(strBatchID);
					psTzMssjArrTblKey.setTzMsPlanSeq(msJxNo);
					psTzMssjArrTbl = psTzMssjArrTblMapper.selectByPrimaryKey(psTzMssjArrTblKey);
					
					if(psTzMssjArrTbl != null){
						psTzMssjArrTbl.setTzMsyyCount(maxPerson);
						psTzMssjArrTbl.setTzMsDate(msDate);
						psTzMssjArrTbl.setTzStartTm(bjMsStartTime);
						psTzMssjArrTbl.setTzEndTm(bjMsEndTime);
						psTzMssjArrTbl.setTzMsLocation(msLocation);
						psTzMssjArrTbl.setTzMsArrDemo(msXxBz);
						psTzMssjArrTbl.setTzMsPubSta(releaseOrUndo);
						psTzMssjArrTblMapper.updateByPrimaryKeyWithBLOBs(psTzMssjArrTbl);
					}else{
						psTzMssjArrTbl = new PsTzMssjArrTbl();
						psTzMssjArrTbl.setTzClassId(strClassID);
						psTzMssjArrTbl.setTzBatchId(strBatchID);
						psTzMssjArrTbl.setTzMsPlanSeq(msJxNo);
						psTzMssjArrTbl.setTzMsyyCount(maxPerson);
						psTzMssjArrTbl.setTzMsDate(msDate);
						psTzMssjArrTbl.setTzStartTm(bjMsStartTime);
						psTzMssjArrTbl.setTzEndTm(bjMsEndTime);
						psTzMssjArrTbl.setTzMsLocation(msLocation);
						psTzMssjArrTbl.setTzMsArrDemo(msXxBz);
						psTzMssjArrTbl.setTzMsPubSta(releaseOrUndo);
						psTzMssjArrTblMapper.insert(psTzMssjArrTbl);
					}
				}else{
					continue;
				}
			}
			
			//删除的时间安排
			if("ALL".equals(clearAllTimeArr)){
				//删除所有
				String sql = "delete from PS_TZ_MSSJ_ARR_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=?";
				jdbcTemplate.update(sql, new Object[]{ classID, batchID });
				//删除预约考生
				String delStuSql = "delete from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=?";
				jdbcTemplate.update(delStuSql, new Object[]{ classID, batchID });
				jdbcTemplate.execute("commit");
			}else{
				List<Map<String, Object>> removeData = null;
				removeData = (List<Map<String, Object>>) jacksonUtil.getList("removerecs");
				for(Map<String, Object> removeMap: removeData){
					String strClassID = removeMap.get("classID")==null? null : String.valueOf(removeMap.get("classID"));
					String strBatchID = removeMap.get("batchID")==null? null : String.valueOf(removeMap.get("batchID"));
					String msJxNo = removeMap.get("msJxNo")==null? null : String.valueOf(removeMap.get("msJxNo"));
					
					psTzMssjArrTblKey.setTzClassId(strClassID);
					psTzMssjArrTblKey.setTzBatchId(strBatchID);
					psTzMssjArrTblKey.setTzMsPlanSeq(msJxNo);
					int rtn = psTzMssjArrTblMapper.deleteByPrimaryKey(psTzMssjArrTblKey);
					if(rtn != 0){
						String delStuSql = "delete from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and TZ_MS_PLAN_SEQ=?";
						jdbcTemplate.update(delStuSql, new Object[]{ classID, batchID, msJxNo });
						jdbcTemplate.execute("commit");
					}
				}	
			}
			rtnMap.replace("result", "success");
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
			rtnMap.replace("result", "fail");
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	//发布、撤销
	private String publish(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String msJxNo = jacksonUtil.getString("msJxNo");
			String releaseOrUndo = jacksonUtil.getString("releaseOrUndo");//发布状态
			
			PsTzMssjArrTbl psTzMssjArrTbl = new PsTzMssjArrTbl();
			PsTzMssjArrTblKey psTzMssjArrTblKey = new PsTzMssjArrTblKey();
			
			psTzMssjArrTblKey.setTzClassId(classID);
			psTzMssjArrTblKey.setTzBatchId(batchID);
			psTzMssjArrTblKey.setTzMsPlanSeq(msJxNo);
			psTzMssjArrTbl = psTzMssjArrTblMapper.selectByPrimaryKey(psTzMssjArrTblKey);
			
			if(psTzMssjArrTbl != null){
				psTzMssjArrTbl.setTzMsPubSta(releaseOrUndo);
				int rtn = psTzMssjArrTblMapper.updateByPrimaryKey(psTzMssjArrTbl);
				if(rtn == 0){
					errorMsg[0] = "1";
					errorMsg[1] = "发布失败";
				}else{
					rtnMap.replace("success", "success");
				}
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "发布失败，时间安排不存在";
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	//发布选中记录
	@SuppressWarnings("unchecked")
	private String publishpl(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			PsTzMssjArrTbl psTzMssjArrTbl = new PsTzMssjArrTbl();
			PsTzMssjArrTblKey psTzMssjArrTblKey = new PsTzMssjArrTblKey();
			
			List<Map<String, Object>> publishList = (List<Map<String, Object>>) jacksonUtil.getList("pubrecs");
			for(Map<String, Object> publishMap: publishList){
				String classID = publishMap.get("classID")==null? null : String.valueOf(publishMap.get("classID"));
				String batchID = publishMap.get("batchID")==null? null : String.valueOf(publishMap.get("batchID"));
				String msJxNo = publishMap.get("msJxNo")==null? null : String.valueOf(publishMap.get("msJxNo"));
				String releaseOrUndo = publishMap.get("releaseOrUndo")==null? null : String.valueOf(publishMap.get("releaseOrUndo"));
				
				psTzMssjArrTblKey.setTzClassId(classID);
				psTzMssjArrTblKey.setTzBatchId(batchID);
				psTzMssjArrTblKey.setTzMsPlanSeq(msJxNo);
				psTzMssjArrTbl = psTzMssjArrTblMapper.selectByPrimaryKey(psTzMssjArrTblKey);
				
				if(psTzMssjArrTbl != null){
					psTzMssjArrTbl.setTzMsPubSta(releaseOrUndo);
					psTzMssjArrTblMapper.updateByPrimaryKey(psTzMssjArrTbl);
				}
			}
			rtnMap.replace("success", "success");
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	//撤销选中记录
	@SuppressWarnings("unchecked")
	private String revokepl(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			PsTzMssjArrTbl psTzMssjArrTbl = new PsTzMssjArrTbl();
			PsTzMssjArrTblKey psTzMssjArrTblKey = new PsTzMssjArrTblKey();
			
			List<Map<String, Object>> publishList = (List<Map<String, Object>>) jacksonUtil.getList("revrecs");
			for(Map<String, Object> publishMap: publishList){
				String classID = publishMap.get("classID")==null? null : String.valueOf(publishMap.get("classID"));
				String batchID = publishMap.get("batchID")==null? null : String.valueOf(publishMap.get("batchID"));
				String msJxNo = publishMap.get("msJxNo")==null? null : String.valueOf(publishMap.get("msJxNo"));
				String releaseOrUndo = publishMap.get("releaseOrUndo")==null? null : String.valueOf(publishMap.get("releaseOrUndo"));
				
				psTzMssjArrTblKey.setTzClassId(classID);
				psTzMssjArrTblKey.setTzBatchId(batchID);
				psTzMssjArrTblKey.setTzMsPlanSeq(msJxNo);
				psTzMssjArrTbl = psTzMssjArrTblMapper.selectByPrimaryKey(psTzMssjArrTblKey);
				
				if(psTzMssjArrTbl != null){
					psTzMssjArrTbl.setTzMsPubSta(releaseOrUndo);
					psTzMssjArrTblMapper.updateByPrimaryKey(psTzMssjArrTbl);
				}
			}
			rtnMap.replace("success", "success");
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 查询听众
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String queryAudIDsArr(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			
			String sql = "SELECT TZ_AUD_ID FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
			List<Map<String,Object>> audList = jdbcTemplate.queryForList(sql, new Object[]{ classID,batchID });
			
			int size = audList.size();
			String[] audIDs = new String[size];
			int i = 0;
			for(Map<String,Object> audMap : audList){
				String audID = String.valueOf(audMap.get("TZ_AUD_ID"));
				audIDs[i] = audID;
				i++;
			}
			
			rtnMap.replace("result", "success");
			rtnMap.put("audIDs",audIDs);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 查询班级报名表
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzGetModalId(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String modalId = "";
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			
			String sql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			Map<String,Object> appMap = jdbcTemplate.queryForMap(sql, new Object[]{ classID });
			
			if(appMap != null){
				modalId = appMap.get("TZ_APP_MODAL_ID").toString();
			}
			
			rtnMap.replace("result", "success");
			rtnMap.put("modalId",modalId);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 导出面试考生名单
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String tzExportKsMd(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("fileUrl", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			//查询面试安排总数
			String sql = "SELECT COUNT(*) FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APPLY_PC_ID IN (SELECT TZ_BATCH_ID FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?)";
			int total = sqlQuery.queryForObject(sql, new Object[] { classID, batchID, classID }, "int");
			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					String downloadPath = getSysHardCodeVal.getDownloadPath();
					String expDirPath = downloadPath + "/" + orgid + "/" + getDateNow() + "/" + "interviewExpExcel";
					String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
					
					// 表头
					List<String[]> dataCellKeys = new ArrayList<String[]>();
					dataCellKeys.add(new String[]{ "interview-mshid", "面试申请号" });
					dataCellKeys.add(new String[]{ "interview-name", "姓名" });
					dataCellKeys.add(new String[]{ "interview-class", "报考班级" });
					dataCellKeys.add(new String[]{ "interview-batch", "申请面试批次" });
					dataCellKeys.add(new String[]{ "interview-email", "邮箱" });
					dataCellKeys.add(new String[]{ "interview-phone", "手机" });
					dataCellKeys.add(new String[]{ "interview-yyStatus", "预约状态" });
					dataCellKeys.add(new String[]{ "interview-tag", "标签" });
					
					sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APPLY_PC_ID IN (SELECT TZ_BATCH_ID FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?) order by TZ_APP_INS_ID ";
					List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { classID, batchID, classID});
				
					for (Map<String, Object> mapData : listData) {
						Map<String, Object> mapDataex = new HashMap<String, Object>();
						
						// 报名表实例ID
						String appIns = String.valueOf(mapData.get("TZ_APP_INS_ID"));
	
						// 查询姓名
						sql = "SELECT OPRID,TZ_REALNAME,TZ_MSH_ID,TZ_EMAIL,TZ_MOBILE FROM PS_TZ_AQ_YHXX_TBL A WHERE exists(SELECT 'X' FROM PS_TZ_FORM_WRK_T B,PS_TZ_MSPS_KSH_TBL C WHERE B.TZ_APP_INS_ID=C.TZ_APP_INS_ID and C.TZ_CLASS_ID=? and C.TZ_APPLY_PC_ID=? and B.TZ_APP_INS_ID=? and B.OPRID=A.OPRID)";
						Map<String, Object> yhxxMap = sqlQuery.queryForMap(sql, new Object[] { classID, batchID, appIns });
						
						String oprid = "";
						String name = "";
						String interviewAppId = "";
						String mobile = "";
						String email = "";
						if (yhxxMap != null) {
							oprid = yhxxMap.get("OPRID") == null ? "" : yhxxMap.get("OPRID").toString();
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
	
						//预约状态
						sql = "select 'Y' from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and OPRID=? limit 1";
						String yySta = sqlQuery.queryForObject(sql, new Object[]{ classID, batchID, oprid }, "String");
						String yyStatus = "未预约";
						if("Y".equals(yySta)){
							yyStatus = "已预约";
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
						if (yhxxMap != null) {
							mapDataex.put("interview-mshid", interviewAppId);
							mapDataex.put("interview-name", name);
							mapDataex.put("interview-class",className);
							mapDataex.put("interview-batch", batchName);
							mapDataex.put("interview-email", email);
							mapDataex.put("interview-phone", mobile);
							mapDataex.put("interview-yyStatus",yyStatus);
							mapDataex.put("interview-tag", strLabel);
						}
						dataList.add(mapDataex);
						
				}
					
					/* 将文件上传之前，先重命名该文件 */
					Date dt = new Date();
					SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
					String sDttm = datetimeFormate.format(dt);
					String strUseFileName = "MSKSMD_"+sDttm + "_" + classID + batchID + "." + "xlsx"; 
					
					ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
					boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
					if (rst) {
						String urlExcel = request.getContextPath() + excelHandle.getExportExcelPath();
						rtnMap.replace("fileUrl", urlExcel);
					}
			}	
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	/**
	 * 导出面试安排
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String tzExportMsPlan(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("fileUrl", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			
			if(jacksonUtil.containsKey("exportData")){
				String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				String downloadPath = getSysHardCodeVal.getDownloadPath();
				String expDirPath = downloadPath + "/" + orgid + "/" + getDateNow() + "/" + "interviewExpExcel";
				String absexpDirPath = request.getServletContext().getRealPath(expDirPath);

				List<Map<String,Object>> exportList = (List<Map<String, Object>>) jacksonUtil.getList("exportData");
				
				// 表头
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				dataCellKeys.add(new String[]{ "interview-data", "面试日期" });
				dataCellKeys.add(new String[]{ "interview-start-time", "开始时间" });
				dataCellKeys.add(new String[]{ "interview-end-time", "结束时间" });
				dataCellKeys.add(new String[]{ "interview-limit", "最多预约人数" });
				
				if(exportList != null && exportList.size() > 0){
					dataCellKeys.add(new String[]{ "interview-yy-count", "已预约人数" });
				}
				dataCellKeys.add(new String[]{ "interview-location", "面试地点" });
				dataCellKeys.add(new String[]{ "interview-notes", "备注" });

				// 数据
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				
				if(exportList != null && exportList.size() > 0){
					for(Map<String,Object> expMap: exportList){
						Map<String, Object> mapData = new HashMap<String, Object>();
						String msJxNo = expMap.get("msJxNo").toString();
						
						String sql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsPlanInfo");
						Map<String,Object> msPlanMap = jdbcTemplate.queryForMap(sql, new Object[]{ classID, batchID, msJxNo });
						if(msPlanMap != null){
							mapData.put("interview-data", msPlanMap.get("TZ_MS_DATE"));
							mapData.put("interview-start-time", msPlanMap.get("TZ_START_TM"));
							mapData.put("interview-end-time", msPlanMap.get("TZ_END_TM"));
							mapData.put("interview-limit", Integer.parseInt(msPlanMap.get("TZ_MSYY_COUNT").toString()));
							mapData.put("interview-yy-count", Integer.parseInt(msPlanMap.get("TZ_YY_COUNT").toString()));
							mapData.put("interview-location", msPlanMap.get("TZ_MS_LOCATION"));
							mapData.put("interview-notes", msPlanMap.get("TZ_MS_ARR_DEMO"));
							
							dataList.add(mapData);
						}
					}
				}else{
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("interview-data", "");
					mapData.put("interview-start-time", "");
					mapData.put("interview-end-time", "");
					mapData.put("interview-limit", "");
					mapData.put("interview-location", "");
					mapData.put("interview-notes", "");
					
					dataList.add(mapData);
				}
				
				/* 将文件上传之前，先重命名该文件 */
				Date dt = new Date();
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String sDttm = datetimeFormate.format(dt);
				String strUseFileName = "MSPLAN_"+sDttm + "_" + classID + batchID + "." + "xlsx"; 
				
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
				if (rst) {
					String urlExcel = request.getContextPath() + excelHandle.getExportExcelPath();
					rtnMap.replace("fileUrl", urlExcel);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	/**
	 * 导入面试安排
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzImportMsPlan(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		rtnMap.put("message", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date dateNow = new Date();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			String message = "";
			jacksonUtil.json2Map(strParams);
			String dateFormatStr = getSysHardCodeVal.getDateFormat();
			String timeFormatStr = getSysHardCodeVal.getTimeHMFormat();
			SimpleDateFormat DateFormat = new SimpleDateFormat(dateFormatStr);
			SimpleDateFormat TimeFormat = new SimpleDateFormat(timeFormatStr);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			String strPath = jacksonUtil.getString("path");
			String strFileName = jacksonUtil.getString("sysFileName");
			
			
			String dataFilePath = strPath + strFileName;
			List<String> dataListCellKeys = new ArrayList<String>();

			ExcelHandle excelHandle = new ExcelHandle(request);
			excelHandle.readExcel(dataFilePath, dataListCellKeys, false);
			ArrayList<Map<String, Object>> listData = excelHandle.getExcelListData();

			int count = 0;
			Map<String, String> titleMap = new HashMap<String, String>();
			if(listData != null && listData.size() > 0){
				for(Map<String, Object> lineMap: listData){
					count++;
					if(count==1){
						//标题行
						for (String key : lineMap.keySet()) {  
							String titleValue = (String) lineMap.get(key);
							
							switch (titleValue) {
							case "面试日期":
								titleMap.put(key, "TZ_MS_DATE");
								break;
							case "开始时间":
								titleMap.put(key, "TZ_START_TM");
								break;
							case "结束时间":
								titleMap.put(key, "TZ_END_TM");
								break;
							case "最多预约人数":
								titleMap.put(key, "TZ_MSYY_COUNT");
								break;
							case "面试地点":
								titleMap.put(key, "TZ_MS_LOCATION");
								break;
							case "备注":
								titleMap.put(key, "TZ_MS_ARR_DEMO");
								break;
							default:
								break;
							}
						}  
					}else{
						if(count==2 && titleMap.keySet().size() < 6){
							message += "没有解析到有效标题行，请按导入模板重新导入。";
							errorMsg[0] = "1";
							errorMsg[1] = message;
							break;
						}
						//解析excel数据行
						String interviewDate = "";
						String startTime = "";
						String endTime = "";
						String msLocation = "";
						String maxPerson = "0";
						String notes = "";
						
						Date msDate=null,
						 msStartTime=null,
						 msEndTime=null;
						
						for (String key : titleMap.keySet()) {  
							String tableColumn = titleMap.get(key);
							String value = lineMap.get(key) == null ? "" : lineMap.get(key).toString();
							
							switch (tableColumn) {
							case "TZ_MS_DATE":
								if(lineMap.get(key) instanceof Date){
									msDate = (Date) lineMap.get(key);
								}
								//如果导入的日期为yyyy/MM/dd格式改为yyyy-mm-dd
								interviewDate = value.replaceAll("/", "-");
								break;
							case "TZ_START_TM":
								if(lineMap.get(key) instanceof Date){
									msStartTime = (Date) lineMap.get(key);
								}
								startTime = value;
								break;
							case "TZ_END_TM":
								if(lineMap.get(key) instanceof Date){
									msEndTime = (Date) lineMap.get(key);
								}
								endTime = value;
								break;
							case "TZ_MSYY_COUNT":
								maxPerson = value;
								break;
							case "TZ_MS_LOCATION":
								msLocation = value;
								break;
							case "TZ_MS_ARR_DEMO":
								notes = value;
								break;
							default:
								break;
							}
						}
						
						if(!"".equals(interviewDate) && !"".equals(startTime) 
								&& !"".equals(endTime) && !"".equals(maxPerson)){
							short maxYyCount = 0;
							try {
								if(msDate == null) msDate = DateFormat.parse(interviewDate);
								if(msStartTime == null) msStartTime = TimeFormat.parse(startTime);
								if(msEndTime == null) msEndTime = TimeFormat.parse(endTime);
								maxYyCount = Short.parseShort(maxPerson);
							} catch (Exception e) {
								//e.printStackTrace();
								message += "第"+count+"行数据不正确导入失败。";
								continue;
							}
							
							String msJxNo = String.valueOf(getSeqNum.getSeqNum("TZ_MSSJ_ARR_TBL", "TZ_MS_PLAN_SEQ"));
							PsTzMssjArrTbl psTzMssjArrTbl = new PsTzMssjArrTbl();
							psTzMssjArrTbl.setTzClassId(classID);
							psTzMssjArrTbl.setTzBatchId(batchID);
							psTzMssjArrTbl.setTzMsPlanSeq(msJxNo);
							
							psTzMssjArrTbl.setTzMsDate(msDate);
							psTzMssjArrTbl.setTzStartTm(msStartTime);
							psTzMssjArrTbl.setTzEndTm(msEndTime);
							
							psTzMssjArrTbl.setTzMsLocation(msLocation);
							psTzMssjArrTbl.setTzMsyyCount(maxYyCount);
							psTzMssjArrTbl.setTzMsArrDemo(notes);
							psTzMssjArrTbl.setTzMsOpenSta("Y");//默认开启
							psTzMssjArrTbl.setTzMsPubSta("N");//默认未发布
							
							psTzMssjArrTbl.setRowAddedOprid(oprid);
							psTzMssjArrTbl.setRowAddedDttm(dateNow);
							psTzMssjArrTbl.setRowLastmantOprid(oprid);
							psTzMssjArrTbl.setRowLastmantDttm(dateNow);
							
							int rst =psTzMssjArrTblMapper.insert(psTzMssjArrTbl);
							if(rst > 0){
							}else{
								message += "第"+count+"行数据导入失败。";
							}
						}else{
							message += "第"+count+"行数据不正确导入失败。";
							continue;
						}
					}
				}
				rtnMap.replace("result", "success");
				rtnMap.replace("message", message);
			}

			//删除临时文件
			File file = new File(dataFilePath);  
		    if (file.isFile() && file.exists()) {  
		        file.delete();
		    } 
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误："+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
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
}



