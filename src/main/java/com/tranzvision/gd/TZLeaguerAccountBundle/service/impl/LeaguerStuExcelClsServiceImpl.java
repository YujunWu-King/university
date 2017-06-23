package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzBmbDceTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsprcsrqstMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzBmbDceT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang
 * 报名管理-导出到Excel
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.LeaguerStuExcelClsServiceImpl")
public class LeaguerStuExcelClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzBmbDceTMapper psTzBmbDceTMapper;
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;
	@Autowired
	private TZGDObject tZGDObject;
	
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{"TZ_STARTTIME_CHAR","DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_DR_TASK_DESC", "TZ_REALNAME", "TZ_STARTTIME_CHAR", "PROCESSINSTANCE", "DESCR", "TZ_FWQ_FWLJ" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("fileName", rowList[0]);
					mapList.put("oprName", rowList[1]);
					mapList.put("oprTime", rowList[2]);
					mapList.put("processInstance", rowList[3]);
					mapList.put("applicationEngineStatus", rowList[4]);
					if(rowList[5] != null && !"".equals(rowList[5])){
						mapList.put("fileUrl", request.getContextPath() + rowList[5]);
					}else{
						mapList.put("fileUrl", "");
					}
					
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strRet;
		}
		int processinstance = 0;
		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 报名表模板ID;
				String appFormModalID = "";
				//String appFormModalID = jacksonUtil.getString("appFormModalID");
				// Excel导出模板ID;
				String excelTpl = jacksonUtil.getString("excelTpl");
				// Excel名称;
				String excelName = jacksonUtil.getString("excelName");
				String strResultSource = jacksonUtil.getString("resultSource");
				String searchSql = jacksonUtil.getString("searchSql");
				appFormModalID = jdbcTemplate.queryForObject("SELECT TZ_APP_MODAL_ID FROM PS_TZ_EXPORT_TMP_T WHERE TZ_EXPORT_TMP_ID = ?",
						new Object[] { excelTpl }, "String");
				
				List<String> oprIdArray = new ArrayList<String>();
				List<Map<String, Object>> oprList = null;
				if ("A".equals(strResultSource))
				{
					oprIdArray = (List<String>)jacksonUtil.getList("applicantsList");
				} else
				{
					oprList = jdbcTemplate.queryForList(searchSql);
					if (oprList != null && oprList.size() > 0)
					{
						for (int i102 = 0; i102 < oprList.size(); i102++)
						{
							String oprId = oprList.get(i102).get("OPRID").toString();
							oprIdArray.add(oprId);
						}

					}
				}
				String strOprIdList = "";
				String strAppInsId = "";
				int dcCount = 0;
				int i = 0;
				
				for(i = 0; i < oprIdArray.size(); i++){
					if("".equals(strOprIdList)){
						strOprIdList = oprIdArray.get(i);
					}else{
						strOprIdList = strOprIdList + "," + oprIdArray.get(i);
					}
					dcCount = dcCount + 1;
					/*
					List<Map<String, Object>> list = null;
					try {
						System.out.println(oprIdArray.get(i));
						list = jdbcTemplate.queryForList("SELECT TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?",
								new Object[] { oprIdArray.get(i) });
						
						if (list != null && list.size() > 0) {
							for (int i101 = 0; i101 < list.size(); i101++) {
								strAppInsId =  list.get(i101).get("TZ_APP_INS_ID").toString();
								System.out.println("报名表实例" + strAppInsId);
								if(!"null".equals(strAppInsId) && !"".equals(strAppInsId))
								{
									if("".equals(strAppInsIdList)){
										strAppInsIdList = strAppInsId;
									}else{
										strAppInsIdList = strAppInsIdList + "," + strAppInsId;
									}
									dcCount = dcCount + 1;
								}
							}
						}

					} catch (Exception e) {	
						System.out.println(e.toString());
					}*/
					
				}
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				String downloadPath = getSysHardCodeVal.getDownloadPath();
				String expDirPath = downloadPath + "/" + orgid + "/" + getDateNow() + "/" + "EXPORTBMBEXCEL";
				String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
				
				
				/*生成运行控制ID*/
				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			    String s_dt = dateFormate.format(new Date());
				String runCntlId = "BMBXLS" + s_dt + "_" + getSeqNum.getSeqNum("TZ_BMGL_BMBSH_COM", "DCE_AE");
				
				PsTzBmbDceT psTzBmbDceT = new PsTzBmbDceT();
				psTzBmbDceT.setRunCntlId(runCntlId);
				psTzBmbDceT.setTzAppTplId(appFormModalID);
				psTzBmbDceT.setTzExportTmpId(excelTpl);
				psTzBmbDceT.setTzAudList(strOprIdList);
				psTzBmbDceT.setTzExcelName(excelName);
				psTzBmbDceT.setTzRelUrl(expDirPath);
				psTzBmbDceT.setTzJdUrl(absexpDirPath);
				psTzBmbDceTMapper.insert(psTzBmbDceT);
				
				//processinstance = getSeqNum.getSeqNum("TZ_EXCEL_DRXX_T", "PROCESSINSTANCE");
				//processinstance = getSeqNum.getSeqNum("PSPRCSRQST", "PROCESSINSTANCE");
				String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
				String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				BaseEngine tmpEngine = tZGDObject.createEngineProcess(currentOrgId, "TZ_GD_EXCEL_DB2");
				
				processinstance=tmpEngine.getProcessInstanceID();
				PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
				psTzExcelDrxxT.setProcessinstance(processinstance);
				psTzExcelDrxxT.setTzComId("TZ_BMGL_BMBSH_COM");
				psTzExcelDrxxT.setTzPageId("TZ_EXP_EXCEL_STD");
				psTzExcelDrxxT.setTzDrLxbh("1");
				psTzExcelDrxxT.setTzDrTaskDesc(excelName); 
				psTzExcelDrxxT.setTzStartDtt(new Date());
				psTzExcelDrxxT.setTzDrTotalNum(dcCount);
				psTzExcelDrxxT.setOprid(oprid);
				psTzExcelDrxxT.setTzIsViewAtt("Y");
				psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);
				
				int numSeq = getSeqNum.getSeqNum("TZ_GD_DCE_AE", "TZ_EXCEL_ID");
				String strExcelID = oprid + "_" + s_dt + "_" + String.valueOf(numSeq);
				PsTzExcelDattT psTzExcelDattT = new PsTzExcelDattT();
				psTzExcelDattT.setProcessinstance(processinstance);
				psTzExcelDattT.setTzSysfileName(strExcelID);
				psTzExcelDattT.setTzFileName(excelName);
				psTzExcelDattT.setTzCfLj("A");
				psTzExcelDattT.setTzFjRecName("TZ_APP_CC_T");
				psTzExcelDattT.setTzFwqFwlj(""); 
				psTzExcelDattTMapper.insert(psTzExcelDattT);

				Psprcsrqst psprcsrqst = new Psprcsrqst();
				psprcsrqst.setPrcsinstance(processinstance);
				psprcsrqst.setRunId(runCntlId);
				psprcsrqst.setOprid(oprid);
				psprcsrqst.setRundttm(new Date());
				psprcsrqst.setRunstatus("5");
				psprcsrqstMapper.insert(psprcsrqst);
				
				//TzGdBmgDcExcelClass tzGdBmgDcExcelClass = new TzGdBmgDcExcelClass();
				//tzGdBmgDcExcelClass.tzGdDcBmbExcel(runCntlId);
				//this.tzGdDceAe(runCntlId, processinstance,expDirPath,absexpDirPath);

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

		} catch (Exception e) {
			e.printStackTrace();
			
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			
			Psprcsrqst psprcsrqst = psprcsrqstMapper.selectByPrimaryKey(processinstance);
			if(psprcsrqst != null){
				psprcsrqst.setRunstatus("10");
				psprcsrqstMapper.updateByPrimaryKey(psprcsrqst);
			}
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
	
	
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
 		String strReturn = "{}";
		if(actData == null || actData.length == 0 ){
			return strReturn;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		for(int i = 0; i<actData.length; i++){
			String strComInfo = actData [i];
			jacksonUtil.json2Map(strComInfo);
			String AEId = jacksonUtil.getString("processInstance");
			if(AEId != null && !"".equals(AEId)){
				int processinstance = Integer.parseInt(AEId);
				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(processinstance);
				if(psTzExcelDattT != null){
					String lj = psTzExcelDattT.getTzFwqFwlj();
					if(lj != null && !"".equals(lj)){
						lj = request.getServletContext().getRealPath(lj);

						File file = new File(lj);
						if(file.exists() && file.isFile()){
							file.delete();
						}
					}
				}
				psTzExcelDrxxTMapper.deleteByPrimaryKey(processinstance);
				psTzExcelDattTMapper.deleteByPrimaryKey(processinstance);
				psprcsrqstMapper.deleteByPrimaryKey(processinstance);
				
			}
		}
		return strReturn;
	}
	
}
