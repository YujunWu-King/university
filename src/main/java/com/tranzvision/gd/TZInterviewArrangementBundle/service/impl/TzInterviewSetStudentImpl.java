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
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMspsKshTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsapAudTblKey;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 批次面试时间安排
 * @author zhang lang 
 * 原PS：TZ_GD_MS_ARR_PKG:TZ_GD_MS_SETSTU_CLS
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewSetStudentImpl")
public class TzInterviewSetStudentImpl extends FrameworkImpl{
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
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
	private PsTzMspsKshTblMapper psTzMspsKshTblMapper;
	
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

			if(null != classID && !"".equals(classID) && null != batchID && !"".equals(batchID)){
				//面试安排学生列表store
				if("STULIST".equals(type)){
					//查询面试安排总数
					String sql = "SELECT COUNT(*) FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APPLY_PC_ID IN (SELECT TZ_BATCH_ID FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?)";
					int total = jdbcTemplate.queryForObject(sql, new Object[]{classID, batchID, classID}, "int");
	
					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
					if (total > 0) {
						sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APPLY_PC_ID IN (SELECT TZ_BATCH_ID FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?)";
						List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[]{classID, batchID, classID});
	
						for(Map<String,Object> mapData : listData){
							Map<String,Object> mapJson = new HashMap<String,Object>();
							//报名表实例ID
							String appIns = String.valueOf(mapData.get("TZ_APP_INS_ID"));
							
							//查询姓名
							sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=(SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND TZ_APP_INS_ID=? limit 1)";
							String name = jdbcTemplate.queryForObject(sql, new Object[]{classID, appIns}, "String");
							//面试资格
							sql = "SELECT B.TZ_ZHZ_DMS FROM PS_TZ_CLPS_KSH_TBL A,PS_TZ_PT_ZHZXX_TBL B,PS_TZ_CLS_BATCH_T C WHERE A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_BATCH_ID AND A.TZ_MSHI_ZGFLG=B.TZ_ZHZ_ID AND B.TZ_EFF_STATUS ='A' AND B.TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND A.TZ_CLASS_ID=? AND A.TZ_APP_INS_ID=? ORDER BY CONVERT(A.TZ_APPLY_PC_ID,SIGNED) DESC";
							String msZgFlag = jdbcTemplate.queryForObject(sql, new Object[]{classID, appIns}, "String");
							if("".equals(msZgFlag) || msZgFlag==null){
								sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_MSHI_ZGFLG' AND TZ_EFF_STATUS ='A' AND TZ_ZHZ_ID='W'";
								msZgFlag = jdbcTemplate.queryForObject(sql, "String");
							}
							
							//标签
							String strLabel = "";
							sql = "SELECT TZ_LABEL_NAME FROM PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B WHERE A.TZ_LABEL_ID=B.TZ_LABEL_ID AND TZ_APP_INS_ID=?";
							List<Map<String, Object>> labelList = jdbcTemplate.queryForList(sql, new Object[]{appIns});
							for(Map<String, Object> mapLabel: labelList){
								String label = String.valueOf(mapLabel.get("TZ_LABEL_NAME"));
								if(!"".equals(label) && label != null){
									strLabel = strLabel == ""? label : strLabel+ "； " +label;
								}
							}
							
							mapJson.put("classID", classID);
							mapJson.put("batchID", batchID);
							mapJson.put("appId", appIns);
							mapJson.put("stuName", name);
							mapJson.put("msZGFlag", msZgFlag);
							mapJson.put("city", "");//城市，取值待定
							mapJson.put("country", "");//国家，取值待定
							mapJson.put("label", strLabel);
							
							listJson.add(mapJson);
						}
						mapRet.replace("total", total);
						mapRet.replace("root", listJson);
					}
				}else{
					//面试听众store
					if("AUD".equals(type)){
						String sql = "SELECT COUNT(*) FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
						int total = jdbcTemplate.queryForObject(sql, new Object[]{classID, batchID}, "int");
		
						ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
						if (total > 0) {
							sql = "SELECT TZ_AUD_ID FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
							List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[]{classID, batchID});
		
							for(Map<String,Object> mapData : listData){
								Map<String,Object> mapJson = new HashMap<String,Object>();
								//听众ID
								String audID = String.valueOf(mapData.get("TZ_AUD_ID"));
								
								//查询姓名
								sql = "SELECT TZ_AUD_NAM FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=?";
								String audName = jdbcTemplate.queryForObject(sql, new Object[]{audID}, "String");
								
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
		}catch(Exception e){
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
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			for(int i=0; i<actData.length; i++){
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);
				
				String classID = jacksonUtil.getString("classID");
				String batchID = jacksonUtil.getString("batchID");
				List<String> audIdList = (List<String>) jacksonUtil.getList("audIDs");
				
				String whereIn = "";
				for(String audID : audIdList){
					if("".equals(whereIn)){
						whereIn = "'"+audID+"'";
					}else{
						whereIn = whereIn + ",'"+audID+"'";
					}
				}
				
				String sql; 
				if("".equals(whereIn)){
					sql = "SELECT TZ_AUD_ID FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
				}else{
					sql = "SELECT TZ_AUD_ID FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_AUD_ID NOT IN("+ whereIn +")";
				}
				
				List<Map<String,Object>> delAudIdList = jdbcTemplate.queryForList(sql, new Object[]{classID,batchID});
				for(Map<String,Object> delAudMap : delAudIdList){
					String delAudId = String.valueOf(delAudMap.get("TZ_AUD_ID"));
					
					//删除听众成员
					sql = "SELECT TZ_LYDX_ID FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=? AND TZ_LXFS_LY='ZSBM'";
					List<Map<String,Object>> audCyList = jdbcTemplate.queryForList(sql, new Object[]{delAudId});
					
					for(Map<String,Object> audCyMap : audCyList){
						Long appInsId;
						try{
							appInsId = Long.valueOf(audCyMap.get("TZ_LYDX_ID").toString());
						}catch(NumberFormatException nE){
							continue;
						}
						
						PsTzMspsKshTblKey psTzMspsKshTblKey = new PsTzMspsKshTblKey();
						psTzMspsKshTblKey.setTzClassId(classID);
						psTzMspsKshTblKey.setTzApplyPcId(batchID);
						psTzMspsKshTblKey.setTzAppInsId(appInsId);
						
						PsTzMspsKshTbl psTzMspsKshTbl = psTzMspsKshTblMapper.selectByPrimaryKey(psTzMspsKshTblKey);
						
						//且不再其他听众里面
						sql = "SELECT 'Y' FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID IN("+ whereIn +") AND TZ_LXFS_LY='ZSBM' limit 1";
						String inOtherAud = jdbcTemplate.queryForObject(sql, "String");
						
						if(psTzMspsKshTbl != null && !"Y".equals(inOtherAud)){
							psTzMspsKshTblMapper.deleteByPrimaryKey(psTzMspsKshTblKey);
						}
					}
					
					//删除面试听众
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
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("success", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			for(int i=0; i<actData.length; i++){
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);
				
				String classID = jacksonUtil.getString("classID");
				String batchID = jacksonUtil.getString("batchID");
				Long appId = Long.valueOf(jacksonUtil.getString("appId"));
				
				PsTzMspsKshTblKey psTzMspsKshTblKey = new PsTzMspsKshTblKey();
				psTzMspsKshTblKey.setTzClassId(classID); 
				psTzMspsKshTblKey.setTzApplyPcId(batchID);
				psTzMspsKshTblKey.setTzAppInsId(appId);
				
				PsTzMspsKshTbl  psTzMspsKshTbl = psTzMspsKshTblMapper.selectByPrimaryKey(psTzMspsKshTblKey);
				if(psTzMspsKshTbl != null){
					psTzMspsKshTblMapper.deleteByPrimaryKey(psTzMspsKshTblKey);
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
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "tzSendEmailToSelStu":
					//保存批次面试预约安排 
					strRet = this.tzSendEmailToSelStu(strParams,errorMsg);
					break;
				case "tzAddAudience":
					//保存批次面试预约安排 
					strRet = this.tzAddAudience(strParams,errorMsg);
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
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String tzAddAudience(String strParams, String[] errorMsg){
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			List<String> audIdList = (List<String>) jacksonUtil.getList("audIDs");
			
			for(String audID : audIdList){
				
				String sql = "SELECT 'Y' FROM PS_TZ_MSAP_AUD_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_AUD_ID=?";
				String isExists = jdbcTemplate.queryForObject(sql, new Object[]{classID,batchID,audID}, "String");
				
				if("Y".equals(isExists)){
					//do nothing
				}else{
					PsTzMsapAudTblKey psTzMsapAudTblKey = new PsTzMsapAudTblKey();
					psTzMsapAudTblKey.setTzClassId(classID);
					psTzMsapAudTblKey.setTzBatchId(batchID);
					psTzMsapAudTblKey.setTzAudId(audID);
					int rtn = psTzMsapAudTblMapper.insert(psTzMsapAudTblKey);
					
					if(rtn != 0){
						//插入听成成员
						sql = "SELECT TZ_LYDX_ID FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=? AND TZ_LXFS_LY='ZSBM'";
						List<Map<String,Object>> audCyList = jdbcTemplate.queryForList(sql, new Object[]{audID});
						
						for(Map<String,Object> audCyMap : audCyList){
							Long appInsId;
							try{
								appInsId = Long.valueOf(audCyMap.get("TZ_LYDX_ID").toString());
							}catch(NumberFormatException nE){
								continue;
							}
							PsTzMspsKshTblKey psTzMspsKshTblKey = new PsTzMspsKshTblKey();
							psTzMspsKshTblKey.setTzClassId(classID);
							psTzMspsKshTblKey.setTzApplyPcId(batchID);
							psTzMspsKshTblKey.setTzAppInsId(appInsId);
							
							PsTzMspsKshTbl psTzMspsKshTbl = psTzMspsKshTblMapper.selectByPrimaryKey(psTzMspsKshTblKey);
							if(psTzMspsKshTbl==null){
								psTzMspsKshTbl = new PsTzMspsKshTbl();
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
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRtn;
	}
	
	
	
	/**
	 * 给选中考生发送面试预约邮件
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String tzSendEmailToSelStu(String strParams, String[] errorMsg){
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			List<Map<String,Object>> selStuList = (List<Map<String, Object>>) jacksonUtil.getList("stuList");
			
			String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			// 创建邮件发送听众
			String crtAudi = createTaskServiceImpl.createAudience("",jgid,"面试预约通知邮件", "JSRW");

			if (!"".equals(crtAudi)) {

				// 添加听众成员
				for (Map<String,Object> stuDataMap : selStuList) {

					String classId = stuDataMap.get("classID") == null? "" 
							: String.valueOf(stuDataMap.get("classID"));
					String appId = stuDataMap.get("appId") == null? "" 
							: String.valueOf(stuDataMap.get("appId"));
					String stuName = stuDataMap.get("stuName") == null? "" 
							: String.valueOf(stuDataMap.get("stuName"));
					
					String oprid = "";
					String sql = "SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND TZ_APP_INS_ID=?";
					oprid = sqlQuery.queryForObject(sql,new Object[] { classId, appId },"String");

					
					sql = "SELECT TZ_ZY_EMAIL,TZ_CY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?";
					Map<String, Object> mapBmrInfo = sqlQuery.queryForMap(sql, new Object[] { appId });

					String mainEmail = "";
					String cyEmail = "";
					if (null != mapBmrInfo) {
						mainEmail = mapBmrInfo.get("TZ_ZY_EMAIL") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_ZY_EMAIL"));
						cyEmail = mapBmrInfo.get("TZ_CY_EMAIL") == null ? ""
								: String.valueOf(mapBmrInfo.get("TZ_CY_EMAIL"));
					}
					if("".equals(mainEmail) && !"".equals(oprid)){
						sql = "SELECT TZ_EMAIL FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
						mainEmail = sqlQuery.queryForObject(sql,new Object[] { oprid },"String");
					}
					
					if(!"".equals(mainEmail) && !"".equals(oprid)){
						createTaskServiceImpl.addAudCy(crtAudi,stuName, "", "", "", mainEmail, cyEmail, "", oprid, "","",appId);
					}
				}
				
				//邮件模板
				String emailTmpName = getHardCodePoint.getHardCodePointVal("TZ_GD_MS_MSARRC_EMLTML");
				
				if("".equals(emailTmpName) || emailTmpName == null){
					errorMsg[0] = "1";
					errorMsg[1] = "未配置邮件模板";
				}else{
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("EmailTmpName", emailTmpName);
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


