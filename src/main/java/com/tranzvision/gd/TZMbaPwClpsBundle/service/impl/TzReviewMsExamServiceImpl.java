package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

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
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzZdcsDcAetMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzZdcsDcAet;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsksTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsksTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.PsTzMpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTblKey;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/****
 * MBA材料面试评审-面试规则-考生查看
 * 
 * @author tzhjl
 * @since 2017-3-14
 */
@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewMsExamServiceImpl")
public class TzReviewMsExamServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzMsPsksTblMapper psTzMsPsksTblMapper;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TZGDObject TzGDObject;
	@Autowired
	private GetSeqNum GetSeqNum;
	@Autowired
	private PsTzZdcsDcAetMapper psTzZdcsDcAetMapper;
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	@Autowired
	private PsTzMpPwKsTblMapper psTzMpPwKsTblMapper;

	/***
	 * 
	 * @param comParams
	 * @param numLimit
	 * @param numStart
	 * @param errorMsg
	 * @return
	 */

	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		String judgeList = "";
		String judgeGroupName = "";

		// String pwsql = "SELECT group_concat(B.TZ_DLZH_ID) AS TZ_DLZH_ID FROM
		// PS_TZ_MP_PW_KS_TBL A, PS_TZ_AQ_YHXX_TBL B WHERE
		// A.TZ_PWEI_OPRID=B.OPRID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=?
		// AND A.TZ_APP_INS_ID=? GROUP BY
		// A.TZ_APPLY_PC_ID,A.TZ_CLASS_ID,A.TZ_APP_INS_ID";
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_APP_INS_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_APP_INS_ID", "TZ_MSPS_PWJ_PC",
					"TZ_LUQU_ZT", "OPRID", "TZ_REALNAME", "TZ_GENDER", "TZ_MSH_ID","TZ_CLPS_GR_NAME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classId", rowList[0]);
					mapList.put("batchId", rowList[1]);
					mapList.put("appInsId", rowList[2]);
					/*
					 * judgeList = sqlQuery.queryForObject(pwsql, new Object[] {
					 * rowList[0], rowList[1], rowList[2] }, "String");
					 */
				 /*	judgeGroupName = sqlQuery.queryForObject(TzGDObject.getSQLText("SQL.TZMbaPwClps.TZ_MSPS_KS_JUGROP"),
							new Object[] { rowList[1], rowList[0], rowList[2] }, "String");*/
					mapList.put("judgeGroup", judgeList);
					mapList.put("ksOprId", rowList[3]);
					mapList.put("passState", rowList[4]);
					mapList.put("ksOprId", rowList[5]);
					mapList.put("ksName", rowList[6]);
					mapList.put("gender", rowList[7]);
					mapList.put("mshId", rowList[8]);
					mapList.put("judgeGroupName", rowList[9]);
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);

	}

	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();
		Long appinsId = (long) 0;
		String ksName = "";
		int count = 0;

		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			String sql3="SELECT  TZ_DQPY_ZT FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			
			String appState=sqlQuery.queryForObject(sql3, new Object[] { classId, batchId }, "String");
			System.out.println("appState:"+appState);
			if ("A".equals(appState)) {
				errMsg[0] = "1";
				errMsg[1] = "当前批次：评审进行中，不能删除考生!";		
			}else{
				for (int i = 2; i < actData.length; i++) {
					// 表单内容
					String strForm = actData[i];
					// 解析 json
					jacksonUtil.json2Map(strForm);
					appinsId = Long.valueOf(jacksonUtil.getString("appInsId"));
					ksName = jacksonUtil.getString("ksName");
					String sql = "SELECT COUNT(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_APP_INS_ID=?";
					count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, appinsId }, "Integer");
					if (count > 0) {
						PsTzMsPsksTbl psTzMsPsksTbl = new PsTzMsPsksTbl();
						psTzMsPsksTbl.setTzClassId(classId);
						psTzMsPsksTbl.setTzApplyPcId(batchId);
						psTzMsPsksTbl.setTzAppInsId(appinsId);

						psTzMsPsksTblMapper.deleteByPrimaryKey(psTzMsPsksTbl);

					} else {

					}
					
					String sql1 = "SELECT COUNT(1) from PS_TZ_MP_PW_KS_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_APP_INS_ID=?";
					int count1 = sqlQuery.queryForObject(sql1, new Object[] { classId, batchId, appinsId }, "Integer");
					if (count1 > 0) {
						
						PsTzMpPwKsTblKey  PsTzMpPwKsTblKey=new PsTzMpPwKsTblKey();
						PsTzMpPwKsTblKey.setTzAppInsId(appinsId);
						PsTzMpPwKsTblKey.setTzClassId(classId);
						PsTzMpPwKsTblKey.setTzApplyPcId(batchId);
		
						psTzMpPwKsTblMapper.deleteByPrimaryKey(PsTzMpPwKsTblKey);

					} else {

					}

				}
			}
		

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();
		Long appinsId = (long) 0;
		String ksName = "";
		String passState = "";
		int count = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			for (int i = 2; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				appinsId = Long.valueOf(jacksonUtil.getString("appInsId"));
				ksName = jacksonUtil.getString("ksName");
				passState = jacksonUtil.getString("passState");
				String sql = "SELECT COUNT(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_APP_INS_ID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, appinsId }, "Integer");
				if (count > 0) {
					PsTzMsPsksTbl psTzMsPsksTbl = new PsTzMsPsksTbl();
					psTzMsPsksTbl.setTzClassId(classId);
					psTzMsPsksTbl.setTzApplyPcId(batchId);
					psTzMsPsksTbl.setTzAppInsId(appinsId);
					psTzMsPsksTbl.setTzLuquZt(passState);
					psTzMsPsksTbl.setRowLastmantOprid(Oprid);
					psTzMsPsksTbl.setRowLastmantDttm(nowdate);

					psTzMsPsksTblMapper.updateByPrimaryKeySelective(psTzMsPsksTbl);

				} else {

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * 导出选中的考生信息
	 * 
	 * @param strType
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {

			switch (strType) {
			case "EXPORT":
				strRet = this.tzExportExcelFile(strParams, errorMsg);
				break;
			case "DOWNLOAD":
				strRet = this.tzDownloadExpFile(strParams, errorMsg);
				break;
			case "getSearchSql":
				strRet = this.tzGetSearchSql(strParams, errorMsg);
				break;
			case "delExpExcel":
				strRet = this.delExpExcel(strParams, errorMsg);

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
			// TODO: handle exception
		}

		return strRet;
	}

	// 根据生日计算年纪
	public int getAge(Date dateOfBirth) {
		int age = 0;
		Calendar born = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (dateOfBirth != null) {
			now.setTime(new Date());
			born.setTime(dateOfBirth);
			if (born.after(now)) {
				throw new IllegalArgumentException("年龄不能超过当前日期");
			}
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
			int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
			System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
			if (nowDayOfYear < bornDayOfYear) {
				age -= 1;
			}
		}
		return age;
	}

	/**
	 * 导出考生评议数据
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzExportExcelFile(String strParams, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String fileName = jacksonUtil.getString("fileName");

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String downloadPath = getSysHardCodeVal.getDownloadPath();

			// excel存储路径
			String eventExcelPath = "/material/xlsx";

			// 完整的存储路径
			String expDirPath = downloadPath + eventExcelPath + "/" + getDateNow();
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);

			/* 生成运行控制ID */
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String s_dt = dateFormate.format(new Date());
			String runCntlId = "MSPSKS" + s_dt + "_" + GetSeqNum.getSeqNum("TZ_REVIEW_CL_COM", "CLPSKS_EXPORT");

			// 与自动初筛导出共用参数表
			PsTzZdcsDcAet psTzZdcsDcAet = new PsTzZdcsDcAet();
			psTzZdcsDcAet.setRunCntlId(runCntlId);
			psTzZdcsDcAet.setTzClassId(classId);
			psTzZdcsDcAet.setTzBatchId(batchId);
			psTzZdcsDcAet.setTzRelUrl(expDirPath);
			psTzZdcsDcAet.setTzJdUrl(absexpDirPath);
			psTzZdcsDcAet.setTzParamsStr(strParams);
			psTzZdcsDcAetMapper.insert(psTzZdcsDcAet);

			String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			BaseEngine tmpEngine = TzGDObject.createEngineProcess(currentOrgId, "TZ_MSPSKS_EXP_PROC");
			// 指定调度作业的相关参数
			EngineParameters schdProcessParameters = new EngineParameters();

			schdProcessParameters.setBatchServer("");
			schdProcessParameters.setCycleExpression("");
			schdProcessParameters.setLoginUserAccount(currentAccountId);
			schdProcessParameters.setPlanExcuteDateTime(new Date());
			schdProcessParameters.setRunControlId(runCntlId);

			// 调度作业
			tmpEngine.schedule(schdProcessParameters);

			// 进程实例id;
			int processinstance = tmpEngine.getProcessInstanceID();
			if(processinstance>0){
				
				PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
				psTzExcelDrxxT.setProcessinstance(processinstance);
				psTzExcelDrxxT.setTzComId("TZ_REVIEW_MS_COM");
				psTzExcelDrxxT.setTzPageId("TZ_MSPS_KS_STD");
				// 存放班级ID-批次ID
				psTzExcelDrxxT.setTzDrLxbh(classId + "-" + batchId);
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



		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 下载导出文件
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzDownloadExpFile(String strParams, String[] errorMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("filePath", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 下载导出excel
			String filePath = "";
			String strProcInsId = jacksonUtil.getString("procInsId");
			if (!"".equals(strProcInsId) && strProcInsId != null) {
				int procInsId = Integer.parseInt(strProcInsId);

				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
				if (psTzExcelDattT != null) {
					filePath = psTzExcelDattT.getTzFwqFwlj();
					if (!"".equals(filePath) && filePath != null) {
						filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
								+ request.getContextPath() + filePath;
					}
				}
			}
			mapRet.put("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误。" + e.getMessage();
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 可配置搜索sql
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzGetSearchSql(String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("searchSql", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			/* 可配置搜索查询语句 */
			String[] resultFldArray = { "TZ_CLASS_ID","TZ_APPLY_PC_ID","TZ_APP_INS_ID","OPRID","TZ_REALNAME","TZ_CLPS_GR_NAME" };

			String[][] orderByArr = null;

			String searchSql = fliterForm.getQuerySQL(resultFldArray, orderByArr, strParams, errorMsg);

			rtnMap.replace("searchSql", searchSql);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}

		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}

	/**
	 * 删除 excel
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private String delExpExcel(String strParams, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			List<Map<String, Object>> delData = (List<Map<String, Object>>) jacksonUtil.getList("data");

			if (delData != null && delData.size() > 0) {
				for (Map<String, Object> delMap : delData) {

					String strProcInsId = delMap.get("procInsId") == null ? "" : delMap.get("procInsId").toString();
					System.out.println("strProcInsId：" + strProcInsId);
					if (!"".equals(strProcInsId) && strProcInsId != null) {
						int procInsId = Integer.parseInt(strProcInsId);

						PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
						if (psTzExcelDattT != null) {
							String filePath = psTzExcelDattT.getTzFwqFwlj();
							if (!"".equals(filePath) && filePath != null) {
								filePath = request.getServletContext().getRealPath(filePath);

								File file = new File(filePath);
								if (file.exists() && file.isFile()) {
									file.delete();
								}
							}
						}
						psTzExcelDrxxTMapper.deleteByPrimaryKey(procInsId);
						psTzExcelDattTMapper.deleteByPrimaryKey(procInsId);
					}
				}
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
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

}
