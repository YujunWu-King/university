package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

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

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.BatchProcessDetailsImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsDcAetMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsDcAet;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 招生线索导出
 * @author LuYan 2017-10-13
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueExpImpServiceImpl")
public class TzClueExpImpServiceImpl extends FrameworkImpl {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private PsTzXsxsDcAetMapper psTzXsxsDcAetMapper;
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private BatchProcessDetailsImpl batchProcessDetailsImpl;
	
	
	/* 导出历史记录列表 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		try {
			jacksonUtil.json2Map(strParams);
			
			// 排序字段
			String[][] orderByArr = new String[][] {{"TZ_STARTTIME","DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_DR_LXBH", "TZ_DLZH_ID", "TZ_DR_TASK_DESC", "TZ_STARTTIME", "PROCESSINSTANCE",
					"PROCESS_STATUS","TZ_REALNAME"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);

			if (obj != null && obj.length > 0) {

					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classBatch", rowList[0]);
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
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}
	
	
	/* 删除导出历史记录数据 */
	@SuppressWarnings("unchecked")
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			if (actData.length == 0 || actData == null) {
				return strRet;
			}

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				List<Map<String,Object>> delData = (List<Map<String, Object>>) jacksonUtil.getList("data");
				
				if(delData != null && delData.size()>0){
					for(Map<String,Object> delMap : delData){
						String strProcInsId = delMap.get("procInsId") == null ? "": delMap.get("procInsId").toString();
						if(!"".equals(strProcInsId) && strProcInsId != null){
							int procInsId = Integer.parseInt(strProcInsId);
							
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
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	
	public String tzOther(String operateType, String strParams, String[] errMsg) {
		String strRet = "";

		try {
			//获取可配置搜索语句
			if("getSearchSql".equals(operateType)){
				strRet=this.getSearchSql(strParams,errMsg);
			}
			
			/*导出招生线索信息*/
			if("EXPORT".equals(operateType)){
				strRet = this.exportExcelFile(strParams, errMsg);
			}
			
			/*下载导出数据*/
			if("DOWNLOAD".equals(operateType)){
				strRet = this.downloadExpFile(strParams, errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	
	/*获取可配置搜索语句*/
	public String getSearchSql(String strParams,String[] errorMsg) {
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("searchSql", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			/*可配置搜索查询语句*/
			String[] resultFldArray = {"TZ_LEAD_ID"};
			
			String[][] orderByArr=null;
			
			String searchSql = fliterForm.getQuerySQL(resultFldArray,orderByArr,strParams,errorMsg);
			
			rtnMap.replace("searchSql", searchSql);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/*导出招生线索信息*/
	private String exportExcelFile(String strParams, String[] errorMsg){
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			jacksonUtil.json2Map(strParams);
			
			String exportType = jacksonUtil.getString("exportType");
			String fileName = jacksonUtil.getString("fileName");
			String searchSql = jacksonUtil.getString("searchSql");
			
			if("default".equals(searchSql)) {
				if("ZSXS".equals(exportType)) {
					searchSql = "SELECT TZ_LEAD_ID FROM PS_TZ_ZSXS_INFO_VW2 WHERE TZ_JG_ID='" + orgId + "' AND TZ_LEAD_STATUS<>'G' AND TZ_ZR_OPRID2 IN ('" + oprid + "','NEXT')";
				} else if("MYXS".equals(exportType)) {
					searchSql = "SELECT TZ_LEAD_ID FROM PS_TZ_XSXS_INFO_VW WHERE TZ_JG_ID='" + orgId + "' AND TZ_LEAD_STATUS<>'G' AND TZ_ZR_OPRID2 ='" + oprid +"'";
				}
			}
			
			Map<String, Object> mapParams = new HashMap<String,Object>();
			mapParams.put("searchSql", searchSql);
			strParams = jacksonUtil.Map2json(mapParams);
			
			
			String downloadPath = getSysHardCodeVal.getDownloadPath();
			
			// excel存储路径
			String eventExcelPath = "/xsxs/xlsx";
			
			// 完整的存储路径
			String expDirPath = downloadPath + eventExcelPath + "/" + getDateNow();
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
			
			/*生成运行控制ID*/
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		    String s_dt = dateFormate.format(new Date());
			String runCntlId = "XSXS" + s_dt + "_" + getSeqNum.getSeqNum("TZ_XSXS_DRDC_COM", "XSXS_EXPORT");
			
			//参数表
			PsTzXsxsDcAet psTzXsxsDcAet = new PsTzXsxsDcAet();
			psTzXsxsDcAet.setRunCntlId(runCntlId);
			psTzXsxsDcAet.setTzType(exportType);
			psTzXsxsDcAet.setTzRelUrl(expDirPath);
			psTzXsxsDcAet.setTzJdUrl(absexpDirPath);
			psTzXsxsDcAet.setTzParamsStr(strParams);
			psTzXsxsDcAetMapper.insert(psTzXsxsDcAet);
			
			String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			BaseEngine tmpEngine = tzSQLObject.createEngineProcess(currentOrgId, "TZ_XSXS_EXP_PROC");
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
				psTzExcelDrxxT.setTzComId("TZ_XSXS_DRDC_COM");
				psTzExcelDrxxT.setTzPageId("TZ_XSXS_DRDC_STD");
				psTzExcelDrxxT.setTzDrLxbh(exportType);
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
	
	
	/*下载导出文件*/
	private String downloadExpFile(String strParams, String[] errorMsg){
		Map<String,Object> mapRet = new HashMap<String,Object>();
		mapRet.put("filePath", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			//下载导出excel
			String filePath = "";
			String strProcInsId = jacksonUtil.getString("procInsId");
			if(!"".equals(strProcInsId) && strProcInsId != null){
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
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误。"+e.getMessage();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	private String getDateNow() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		return (new StringBuilder()).append(year).append(month).append(day).toString();
	}
}
