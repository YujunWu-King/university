package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMsyyKsTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTblKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 查看预约考生类
 * @author zhang lang
 *
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewAppoStuImpl")
public class TzInterviewAppoStuImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PsTzMsyyKsTblMapper psTzMsyyKsTblMapper;
	
	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;
	
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
				String sql = "SELECT COUNT(*) FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[]{classID, batchID}, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = "SELECT TZ_MS_PLAN_SEQ,OPRID FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? ORDER BY TZ_MS_PLAN_SEQ LIMIT ?,?";
					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[]{classID, batchID, start, limit});

					for(Map<String,Object> mapData : listData){
						Map<String,Object> mapJson = new HashMap<String,Object>();
						//报名表实例ID
						String msPlanSeq = String.valueOf(mapData.get("TZ_MS_PLAN_SEQ"));
						String oprid = String.valueOf(mapData.get("OPRID"));
						
						String name = "";
						String email = "";
						String mobile = "";
						String interviewAppID = "";
						sql = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE,TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
						Map<String,Object> infoMap = jdbcTemplate.queryForMap(sql, new Object[]{ oprid });
						if(infoMap != null){
							name = infoMap.get("TZ_REALNAME").toString();
							email = infoMap.get("TZ_EMAIL").toString();
							mobile = infoMap.get("TZ_MOBILE").toString();
							interviewAppID = infoMap.get("TZ_MSH_ID").toString();
						}
						
						
						String sort = "";
						String appInsID = "";
						sql = "SELECT TZ_COLOR_SORT_ID,TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?";
						Map<String,Object> wrkMap = jdbcTemplate.queryForMap(sql, new Object[]{ classID, oprid });
						if(wrkMap != null){
							sort = wrkMap.get("TZ_COLOR_SORT_ID") == null ? "" : wrkMap.get("TZ_COLOR_SORT_ID").toString();
							appInsID = wrkMap.get("TZ_APP_INS_ID").toString(); 
						}
						
						String msDate = "",
							   msStartTime = "",
							   msEndTime = "";
						sql = "SELECT TZ_MS_DATE,date_format(TZ_START_TM,'%H:%i') as TZ_START_TM,date_format(TZ_END_TM,'%H:%i') as TZ_END_TM FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
						Map<String,Object> msDateMap= jdbcTemplate.queryForMap(sql, new Object[]{classID, batchID, msPlanSeq});
						if(msDateMap != null){
							msDate = String.valueOf(msDateMap.get("TZ_MS_DATE"));
							msStartTime = String.valueOf(msDateMap.get("TZ_START_TM"));
							msEndTime = String.valueOf(msDateMap.get("TZ_END_TM"));
						}
						
						mapJson.put("classID", classID);
						mapJson.put("batchID", batchID);
						mapJson.put("msPlanSeq", msPlanSeq);
						mapJson.put("oprid", oprid);
						mapJson.put("name", name);
						mapJson.put("appInsID", appInsID);
						
						mapJson.put("email", email);
						mapJson.put("mobile", mobile);
						mapJson.put("interviewAppID", interviewAppID);
						
						mapJson.put("sort", sort);
						mapJson.put("msDate", msDate);
						mapJson.put("msStartTime", msStartTime);
						mapJson.put("msEndTime", msEndTime);
						
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
	
	
	/**
	 * 保存修改的预约考生
	 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			for(int i=0; i<actData.length; i++){
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);
				
				String classID = jacksonUtil.getString("classID");
				String oprid = jacksonUtil.getString("oprid");
				String sort = jacksonUtil.getString("sort");
				
				//报名工作表
				PsTzFormWrkTKey psTzFormWrkTKey = new PsTzFormWrkTKey();
				psTzFormWrkTKey.setTzClassId(classID);
				psTzFormWrkTKey.setOprid(oprid);
				
				PsTzFormWrkT psTzFormWrkT = psTzFormWrkTMapper.selectByPrimaryKey(psTzFormWrkTKey);
				if(psTzFormWrkT != null){
					psTzFormWrkT.setTzColorSortId(sort);
					psTzFormWrkTMapper.updateByPrimaryKeySelective(psTzFormWrkT);
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
	 * 保存删除的预约考生
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			for(int i=0; i<actData.length; i++){
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);
				
				String classID = jacksonUtil.getString("classID");
				String batchID = jacksonUtil.getString("batchID");
				String msPlanSeq = jacksonUtil.getString("msPlanSeq");
				String oprid = jacksonUtil.getString("oprid");
				
				
				PsTzMsyyKsTblKey psTzMsyyKsTblKey = new PsTzMsyyKsTblKey(); 
				psTzMsyyKsTblKey.setTzClassId(classID);
				psTzMsyyKsTblKey.setTzBatchId(batchID);
				psTzMsyyKsTblKey.setTzMsPlanSeq(msPlanSeq);
				psTzMsyyKsTblKey.setOprid(oprid);
				
				PsTzMsyyKsTbl psTzMsyyKsTbl = psTzMsyyKsTblMapper.selectByPrimaryKey(psTzMsyyKsTblKey);
				if(psTzMsyyKsTbl != null){
					psTzMsyyKsTblMapper.deleteByPrimaryKey(psTzMsyyKsTblKey);
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
}
