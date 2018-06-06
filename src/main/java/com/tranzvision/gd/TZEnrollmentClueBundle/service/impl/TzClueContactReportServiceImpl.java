package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
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
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzLxbgDfnTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzLxbgFileTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsLxbgTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzLxbgDfnT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzLxbgFileT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzLxbgFileTKey;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsLxbgTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueContactReportServiceImpl")
public class TzClueContactReportServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzXsxsLxbgTMapper psTzXsxsLxbgTMapper;
	@Autowired
	private PsTzLxbgDfnTMapper psTzLxbgDfnTMapper;
	@Autowired
	private PsTzLxbgFileTMapper psTzLxbgFileTMapper;
	
	
	/**
	 * 根据联系报告编号查询对应附件信息
	 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		String strReturn = "";
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("total", 0);
		mapRet.put("root", listData);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String cctRptId = jacksonUtil.getString("LXBGID");
			
			if(cctRptId != null && !"".equals(cctRptId)){
				String sql = "select TZ_SEQNUM as attachmentID,TZ_ATTACHSYSFILENA as attachmentSysName,TZ_ATTACHFILE_NAME as attachmentName,TZ_ATT_ACC_PATH as attachmentUrl from PS_TZ_LXBG_FILE_T where TZ_CALLREPORT_ID=? order by TZ_SEQNUM limit ?,?";
				listData = sqlQuery.queryForList(sql, new Object[]{ cctRptId, numStart, numLimit });
				
				if(listData == null){
					listData = new ArrayList<Map<String,Object>>();
				}
				
				// 获取总数;
				int numTotal = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_LXBG_FILE_T WHERE TZ_CALLREPORT_ID=?",
						new Object[] { cctRptId }, "Integer");
				
				mapRet.replace("total", numTotal);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误" + e.toString();
		}
					
		strReturn = jacksonUtil.Map2json(mapRet);
		return strReturn;
	}
	
	
	
	@Override
	public String tzQuery(String strParams, String[] errorMsg) {
		
		String strReturn = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("reports", new ArrayList<Map<String,Object>>());
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String leadID = jacksonUtil.getString("leadID");
			
			List<Map<String,Object>> reportList = new ArrayList<Map<String,Object>>(); 
			
			String sql = "select A.TZ_CALLREPORT_ID,B.TZ_CALL_SUBJECT,B.TZ_CALL_INFO,date_format(B.TZ_CALL_DATE,'%Y-%m-%d') as TZ_CALL_DATE,date_format(B.ROW_ADDED_DTTM,'%Y-%m-%d %H:%i') as ROW_ADDED_DTTM,ROW_ADDED_OPRID from PS_TZ_XSXS_LXBG_T A join PS_TZ_LXBG_DFN_T B on(A.TZ_CALLREPORT_ID=B.TZ_CALLREPORT_ID) where TZ_LEAD_ID=? ORDER BY B.ROW_ADDED_DTTM DESC";
			List<Map<String,Object>> reptList = sqlQuery.queryForList(sql, new Object[]{ leadID });
			if(reptList != null){
				for(Map<String,Object> reptMap: reptList){
					Map<String,Object> reportMap = new HashMap<String,Object>();
					
					String reptId = reptMap.get("TZ_CALLREPORT_ID") == null ? "" : reptMap.get("TZ_CALLREPORT_ID").toString();
					String subject = reptMap.get("TZ_CALL_SUBJECT") == null ? "" : reptMap.get("TZ_CALL_SUBJECT").toString();
					String reptContent = reptMap.get("TZ_CALL_INFO") == null ? "" : reptMap.get("TZ_CALL_INFO").toString();
					String callDate = reptMap.get("TZ_CALL_DATE") == null ? "" : reptMap.get("TZ_CALL_DATE").toString();
					String addDate = reptMap.get("ROW_ADDED_DTTM") == null ? "" : reptMap.get("ROW_ADDED_DTTM").toString();
					String addOprid = reptMap.get("ROW_ADDED_OPRID") == null ? "" : reptMap.get("ROW_ADDED_OPRID").toString();
					
					String fileSql = "select TZ_ATT_ACC_PATH as path,TZ_ATTACHFILE_NAME as name from PS_TZ_LXBG_FILE_T where TZ_CALLREPORT_ID=?";
					List<Map<String,Object>> fileList = sqlQuery.queryForList(fileSql, new Object[]{ reptId });
					if(fileList == null){
						fileList = new ArrayList<Map<String,Object>>();
					}
					
					String name = sqlQuery.queryForObject("select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?", new Object[]{ addOprid }, "String");
					
					reportMap.put("reportID", reptId);
					reportMap.put("title", subject);
					reportMap.put("date", callDate);
					reportMap.put("addedDttm", addDate);
					reportMap.put("detail", reptContent);
					reportMap.put("files", fileList);
					reportMap.put("addedOprName", name);
					
					reportList.add(reportMap);
				}
				rtnMap.replace("reports", reportList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误" + e.toString();
		}
		
		strReturn = jacksonUtil.Map2json(rtnMap);
		return strReturn;
	}
	
	
	
	@Override
	public String tzUpdate(String[] actData, String[] errorMsg) {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("reportId", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String dtFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			long reportId = 0;
			boolean isNew = false;
			for(String actDataStr: actData){
				jacksonUtil.json2Map(actDataStr);
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String,Object> dataMap = jacksonUtil.getMap("data");
				
				//联系报告基本信息
				if("BASIC".equals(typeFlag)){
					String leadID = dataMap.get("leadID").toString();
		            String reptId = dataMap.get("LXBGID").toString();
		            String title = dataMap.get("title").toString();
		            String detail = dataMap.get("detail").toString();
		            Date date = dataMap.get("date") == null ? null 
		            		: dateSimpleDateFormat.parse(dataMap.get("date").toString());
		            
		            if("NEXT".equals(reptId)){
		            	reportId = Long.valueOf(getSeqNum.getSeqNum("TZ_LXBG_DFN_T", "TZ_CALLREPORT_ID"));
		            	isNew = true;
		            }else{
		            	reportId = Long.valueOf(reptId);
		            }
		            
		            //线索与联系报告关联表
		            String sql = "select 'Y' from PS_TZ_XSXS_LXBG_T where TZ_LEAD_ID=? and TZ_CALLREPORT_ID=?";
		            String exists = sqlQuery.queryForObject(sql, new Object[]{ leadID, reportId }, "String");
		            int rtn;
		            if("Y".equals(exists)){
		            	rtn = 1;
		            }else{
		            	PsTzXsxsLxbgTKey psTzXsxsLxbgTKey = new PsTzXsxsLxbgTKey();
			            psTzXsxsLxbgTKey.setTzLeadId(leadID);
			            psTzXsxsLxbgTKey.setTzCallreportId(reportId);
			            rtn = psTzXsxsLxbgTMapper.insertSelective(psTzXsxsLxbgTKey);
		            }
		            
		            if(rtn > 0){
		            	//联系报告定义
		            	PsTzLxbgDfnT psTzLxbgDfnT = psTzLxbgDfnTMapper.selectByPrimaryKey(reportId);
		            	if(psTzLxbgDfnT != null){
		            		psTzLxbgDfnT.setTzCallSubject(title);
		            		psTzLxbgDfnT.setTzCallInfo(detail);
		            		psTzLxbgDfnT.setTzCallDate(date);
		            		psTzLxbgDfnT.setRowLastmantOprid(oprid);
		            		psTzLxbgDfnT.setRowLastmantDttm(new Date());
		            		psTzLxbgDfnTMapper.updateByPrimaryKeyWithBLOBs(psTzLxbgDfnT);
		            	}else{
		            		psTzLxbgDfnT = new PsTzLxbgDfnT();
		            		psTzLxbgDfnT.setTzCallreportId(reportId);
		            		psTzLxbgDfnT.setTzCallSubject(title);
		            		psTzLxbgDfnT.setTzCallInfo(detail);
		            		psTzLxbgDfnT.setTzCallDate(date);
		            		psTzLxbgDfnT.setRowAddedOprid(oprid);
		            		psTzLxbgDfnT.setRowAddedDttm(new Date());
		            		psTzLxbgDfnT.setRowLastmantOprid(oprid);
		            		psTzLxbgDfnT.setRowLastmantDttm(new Date());
		            		psTzLxbgDfnTMapper.insertSelective(psTzLxbgDfnT);
		            	}
		            }
				}
				
				//附件信息
				if("ATTACH".equals(typeFlag)){
					String attachmentID = dataMap.get("attachmentID").toString();
		            String attachmentSysName = dataMap.get("attachmentSysName").toString();
		            String attachmentName = dataMap.get("attachmentName").toString();
		            String attachmentUrl = dataMap.get("attachmentUrl").toString();
		            
		            if("".equals(attachmentID) || "NEXT".equals(attachmentID)){
		            	int sqlNum = getSeqNum.getSeqNum("TZ_LXBG_FILE_T", "TZ_SEQNUM");
		            	PsTzLxbgFileT psTzLxbgFileT = new PsTzLxbgFileT();
		            	psTzLxbgFileT.setTzCallreportId(reportId);
		            	psTzLxbgFileT.setTzSeqnum(sqlNum);
		            	psTzLxbgFileT.setTzAttachsysfilena(attachmentSysName);
		            	psTzLxbgFileT.setTzAttachfileName(attachmentName);
		            	psTzLxbgFileT.setTzAttAccPath(attachmentUrl);
		            	psTzLxbgFileT.setRowAddedOprid(oprid);
		            	psTzLxbgFileT.setRowAddedDttm(new Date());
		            	psTzLxbgFileT.setRowLastmantOprid(oprid);
		            	psTzLxbgFileT.setRowLastmantDttm(new Date());
		            	
		            	psTzLxbgFileTMapper.insertSelective(psTzLxbgFileT);
		            }
				}
			}
			if(isNew){
				rtnMap.replace("reportId", reportId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误" + e.toString();
		}
		
		return jacksonUtil.Map2json(rtnMap);
	}
	
	
	@Override
	public String tzDelete(String[] actData, String[] errorMsg) {
		String strReturn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			for(String actDataStr: actData){
				jacksonUtil.json2Map(actDataStr);
				String attachmentID = jacksonUtil.getString("attachmentID");
				String attachmentSysName = jacksonUtil.getString("attachmentSysName");
				String attachmentUrl = jacksonUtil.getString("attachmentUrl");
				
				String sql = "select TZ_CALLREPORT_ID from PS_TZ_LXBG_FILE_T where TZ_SEQNUM=? and TZ_ATTACHSYSFILENA=?";
        		Long reportId = sqlQuery.queryForObject(sql, new Object[]{ attachmentID, attachmentSysName }, "Long");
        		if(reportId != null && reportId > 0){

    				PsTzLxbgFileTKey psTzLxbgFileTKey = new PsTzLxbgFileTKey();
    				psTzLxbgFileTKey.setTzCallreportId(reportId);
    				psTzLxbgFileTKey.setTzSeqnum(Integer.valueOf(attachmentID));
    				psTzLxbgFileTMapper.deleteByPrimaryKey(psTzLxbgFileTKey);
    				
    				try {
    					File file = new File(attachmentUrl);
    					if(file.exists()){
    						file.delete();
    					}
					} catch (Exception fe) {
						fe.printStackTrace();
					}
    			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误" + e.toString();
		}
		return strReturn;
	}
	
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strReturn = "";
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if("rm".equals(oprType)){
				String leadID = jacksonUtil.getString("leadID");
				String strReportId = jacksonUtil.getString("LXBGID");
				long reportId = Long.valueOf(strReportId);
				
				PsTzXsxsLxbgTKey psTzXsxsLxbgTKey = new PsTzXsxsLxbgTKey();
	            psTzXsxsLxbgTKey.setTzLeadId(leadID);
	            psTzXsxsLxbgTKey.setTzCallreportId(reportId);
	            int rtn = psTzXsxsLxbgTMapper.deleteByPrimaryKey(psTzXsxsLxbgTKey);
	            if(rtn > 0){
	            	//删除联系报告定义
	            	rtn = psTzLxbgDfnTMapper.deleteByPrimaryKey(reportId);
	            	if(rtn > 0){
	            		//删除附件信息
	            		String sql = "select TZ_SEQNUM,TZ_ATT_ACC_PATH from PS_TZ_LXBG_FILE_T where TZ_CALLREPORT_ID=?";
	            		List<Map<String,Object>> fileList = sqlQuery.queryForList(sql, new Object[]{ reportId });
	            		if(fileList != null){
	            			for(Map<String,Object> fileMap: fileList){
	            				int seqNum = Integer.valueOf(fileMap.get("TZ_SEQNUM").toString());
	            				String filePath = fileMap.get("TZ_ATT_ACC_PATH").toString();
	            				
	            				PsTzLxbgFileTKey psTzLxbgFileTKey = new PsTzLxbgFileTKey();
	            				psTzLxbgFileTKey.setTzCallreportId(reportId);
	            				psTzLxbgFileTKey.setTzSeqnum(seqNum);
	            				psTzLxbgFileTMapper.deleteByPrimaryKey(psTzLxbgFileTKey);
	            				
	            				try {
	            					File file = new File(filePath);
	            					if(file.exists()){
	            						file.delete();
	            					}
								} catch (Exception fe) {
									fe.printStackTrace();
								}
	            			}
	            		}
	            	}
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误" + e.toString();
		}
		return strReturn;
	}
}
