package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMsArrDceAetMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsArrDceAet;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewExpExcelImpl")
public class TzInterviewExpExcelImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private TZGDObject tZGDObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	
	@Autowired
	private PsTzMsArrDceAetMapper psTzMsArrDceAetMapper; 


	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {new String[]{"TZ_START_DTT","DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_DR_TASK_DESC", "TZ_DLZH_ID", "TZ_REALNAME", "PROCESSINSTANCE", "TZ_START_DTT", "TZ_FWQ_FWLJ","TZ_JOB_YXZT"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("fileName", rowList[0]);
					mapList.put("loginId", rowList[1]);
					mapList.put("oprName", rowList[2]);
					mapList.put("processInstance", rowList[3]);
					mapList.put("oprTime", rowList[4]);
					mapList.put("fileUrl", rowList[5]);
					mapList.put("engineStatus", rowList[6]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			for(int i=0; i<actData.length; i++){
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);
				
				String excelName = jacksonUtil.getString("excelName");

				String downloadPath = getSysHardCodeVal.getDownloadPath();
				String expDirPath = downloadPath + "/" + orgid + "/" + getDateNow() + "/" + "interviewExpExcel";
				String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
				
				/*生成运行控制ID*/
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			    String s_dtm = datetimeFormate.format(new Date());
				String runCntlId = "MSARR_" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");
				
				
				PsTzMsArrDceAet psTzMsArrDceAet = new PsTzMsArrDceAet();
				psTzMsArrDceAet.setRunId(runCntlId);
				psTzMsArrDceAet.setTzRelUrl(expDirPath);
				psTzMsArrDceAet.setTzJdUrl(absexpDirPath);
				psTzMsArrDceAet.setTzExpParamsStr(actForm);
				psTzMsArrDceAetMapper.insert(psTzMsArrDceAet);
				
				
				BaseEngine tmpEngine = tZGDObject.createEngineProcess("ADMIN", "TZ_MSARR_EXP_EXCEL_PROC");
				//指定调度作业的相关参数
				EngineParameters schdProcessParameters = new EngineParameters();

				schdProcessParameters.setBatchServer("SEM_GD_001");
				schdProcessParameters.setCycleExpression("");
				schdProcessParameters.setLoginUserAccount("Admin");
				schdProcessParameters.setPlanExcuteDateTime(new Date());
				schdProcessParameters.setRunControlId(runCntlId);
				
				//调度作业
				tmpEngine.schedule(schdProcessParameters);
				
				// 进程id;
				int processinstance = sqlQuery.queryForObject("SELECT TZ_JCSL_ID FROM TZ_JC_SHLI_T where TZ_YUNX_KZID = ? limit 0,1", new Object[] { runCntlId },"Integer");
				
				PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
				psTzExcelDrxxT.setProcessinstance(processinstance);
				psTzExcelDrxxT.setTzComId("TZ_MS_ARR_MG_COM");
				psTzExcelDrxxT.setTzPageId("TZ_MS_ARR_EXP_STD");
				psTzExcelDrxxT.setTzDrLxbh("1");
				psTzExcelDrxxT.setTzDrTaskDesc(excelName);
				psTzExcelDrxxT.setTzStartDtt(new Date());
				psTzExcelDrxxT.setOprid(oprid);
				psTzExcelDrxxT.setTzIsViewAtt("Y");
				psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);
				
				
				PsTzExcelDattT psTzExcelDattT = new PsTzExcelDattT();
				psTzExcelDattT.setProcessinstance(processinstance);
				psTzExcelDattT.setTzCfLj("A");
				psTzExcelDattTMapper.insert(psTzExcelDattT);
			}
			rtnMap.replace("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			rtnMap.replace("result", "fail");
		}
		strRtn = jacksonUtil.Map2json(rtnMap);
		return strRtn;
	}


	
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			for(int i=0; i<actData.length; i++){
				String actForm = actData[i];
				jacksonUtil.json2Map(actForm);
				
				if(jacksonUtil.containsKey("processInstance")){
					int processInstance = Integer.valueOf(jacksonUtil.getString("processInstance"));
					//删除EXCEL批量导入信息表
					PsTzExcelDrxxT psTzExcelDrxxT = psTzExcelDrxxTMapper.selectByPrimaryKey(processInstance);
					if(psTzExcelDrxxT != null){
						psTzExcelDrxxTMapper.deleteByPrimaryKey(processInstance);
					}
					//删除Excel附件表
					PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(processInstance);
					if(psTzExcelDattT != null){
					
						psTzExcelDattTMapper.deleteByPrimaryKey(processInstance);
					}
				}
			}
			rtnMap.replace("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			rtnMap.replace("result", "fail");
		}
		strRtn = jacksonUtil.Map2json(rtnMap);
		return strRtn;
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
