package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

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
	private HttpServletRequest request;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private PsTzClsBatchTMapper psTzClsBatchTMapper;

	@Autowired
	private PsTzClassInfTMapper psTzClassInfTMapper;
	
	@Autowired
	private PsTzMsyySetTblMapper psTzMsyySetTblMapper;
	
	@Autowired
	private PsTzMssjArrTblMapper psTzMssjArrTblMapper;

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
					map.put("batchStartDate", dateSimpleDateFormat.format(psTzClsBatchT.getTzStartDt()));
					map.put("batchEndDate", dateSimpleDateFormat.format(psTzClsBatchT.getTzEndDt()));
					
					PsTzMsyySetTbl psTzMsyySetTbl = new PsTzMsyySetTbl();
					PsTzMsyySetTblKey psTzMsyySetTblKey = new PsTzMsyySetTblKey();
					
					psTzMsyySetTblKey.setTzClassId(str_classID);
					psTzMsyySetTblKey.setTzBatchId(str_batchID);
					psTzMsyySetTbl = psTzMsyySetTblMapper.selectByPrimaryKey(psTzMsyySetTblKey);
					if(psTzMsyySetTbl != null){
						map.put("openDate", dateSimpleDateFormat.format(psTzMsyySetTbl.getTzOpenDt()));
						map.put("openTime", timeSimpleDateFormat.format(psTzMsyySetTbl.getTzOpenTm()));
						map.put("closeDate", dateSimpleDateFormat.format(psTzMsyySetTbl.getTzCloseDt()));
						map.put("closeTime", timeSimpleDateFormat.format(psTzMsyySetTbl.getTzCloseTm()));
						map.put("openStatus", psTzMsyySetTbl.getTzOpenSta());
						map.put("frontView", psTzMsyySetTbl.getTzShowFront());
						map.put("descr", psTzMsyySetTbl.getTzDescr());
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
					sql = "SELECT TZ_MS_PLAN_SEQ,TZ_MSYY_COUNT,TZ_MS_DATE,date_format(TZ_START_TM,'%H:%i') as TZ_START_TM,date_format(TZ_END_TM,'%H:%i') as TZ_END_TM,TZ_MS_LOCATION,TZ_MS_ARR_DEMO,TZ_MS_PUB_STA,TZ_MS_OPEN_STA FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? ORDER BY TZ_MS_DATE ASC limit ?,?";
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}

	
	@SuppressWarnings("unchecked")
	private String saveMsArrInfo(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			
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
			List<Map<String, Object>> removeData = null;
			removeData = (List<Map<String, Object>>) jacksonUtil.getList("removerecs");
			for(Map<String, Object> removeMap: removeData){
				String strClassID = removeMap.get("classID")==null? null : String.valueOf(removeMap.get("classID"));
				String strBatchID = removeMap.get("batchID")==null? null : String.valueOf(removeMap.get("batchID"));
				String msJxNo = removeMap.get("msJxNo")==null? null : String.valueOf(removeMap.get("msJxNo"));
				
				psTzMssjArrTblKey.setTzClassId(strClassID);
				psTzMssjArrTblKey.setTzBatchId(strBatchID);
				psTzMssjArrTblKey.setTzMsPlanSeq(msJxNo);
				psTzMssjArrTblMapper.deleteByPrimaryKey(psTzMssjArrTblKey);
			}	
			
			rtnMap.replace("success", "success");
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
			rtnMap.replace("success", "fail");
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
			rtnMap.put("success", "success");
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
			rtnMap.put("success", "success");
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
	@SuppressWarnings("null")
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
			
			rtnMap.put("result", "success");
			rtnMap.put("audIDs",audIDs);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
}



