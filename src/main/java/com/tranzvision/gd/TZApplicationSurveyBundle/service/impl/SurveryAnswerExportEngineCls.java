package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.zip.ZipUtil;

/**
 * 在线调查-导出调查结果
 * @author zhanglang
 * 2017-05-04
 */
public class SurveryAnswerExportEngineCls extends BaseEngine{

	@Override
	public void OnExecute() throws Exception {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		// 运行id;
		String runControlId = this.getRunControlID();
		int processinstance =  this.getProcessInstanceID();
		
		try{
			String strUseFileName = "";
			String atrFileName = "";
			String dattSql = "select TZ_SYSFILE_NAME,TZ_FILE_NAME from PS_TZ_EXCEL_DATT_T where PROCESSINSTANCE=?";
			Map<String,Object> dattMap = sqlQuery.queryForMap(dattSql, new Object[]{ processinstance });
			if(dattMap != null){
				strUseFileName = dattMap.get("TZ_SYSFILE_NAME").toString();
				atrFileName = dattMap.get("TZ_FILE_NAME").toString().replaceAll(" ", "_");
			}
			
			//导出参数
			Map<String,Object> paramsMap = sqlQuery.queryForMap("select TZ_DC_WJ_ID,TZ_REL_URL,TZ_JD_URL,TZ_DC_TYPE from PS_TZ_DCWJ_DC_AET where RUN_CNTL_ID=?", new Object[] { runControlId });

			if(paramsMap != null){
				String wjId = paramsMap.get("TZ_DC_WJ_ID").toString();
				String expDirPath = paramsMap.get("TZ_REL_URL").toString();
				String absexpDirPath = paramsMap.get("TZ_JD_URL").toString();
				String exportType = paramsMap.get("TZ_DC_TYPE").toString();
				
				if("B".equals(exportType)){
					//附件打包下载
					ZipUtil zipUtil = new ZipUtil();
					String zipFolder = atrFileName + wjId + processinstance;
					String zipPath = "";
					
					String relLj = expDirPath;
					String tarLj = absexpDirPath;
					if("/".equals(File.separator)){
						if (tarLj.lastIndexOf("/") + 1 != tarLj.length()) {
							tarLj = tarLj + "/";
						}
						if (relLj.lastIndexOf("/") + 1 != relLj.length()) {
							relLj = relLj + "/";
						}
					}
					if("\\".equals(File.separator)){
						if (tarLj.lastIndexOf("\\") + 1 != tarLj.length()) {
							tarLj = tarLj + "\\";
						}
						if (relLj.lastIndexOf("\\") + 1 != relLj.length()) {
							relLj = relLj + "\\";
						}
					}
					//打包目录，将要打包的文件都复制到该目录下
					zipPath = tarLj + zipFolder;
					File tF = new File(zipPath);
					if (!tF.exists()) {
						tF.mkdirs();
					}
					
					String insSql = "SELECT PERSON_ID,TZ_APP_INS_ID FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID=?";
					List<Map<String,Object>> wjInsList = sqlQuery.queryForList(insSql, new Object[]{ wjId });
					if(wjInsList != null && wjInsList.size() > 0){
						for(Map<String,Object> wjInsMap: wjInsList){
							//为每个问卷实例创建一个文件夹路径，路径命名： 问卷实例编号 + [面试申请号] + 姓名
							String oprid = wjInsMap.get("PERSON_ID") == null ? "" : wjInsMap.get("PERSON_ID").toString();
							String appInsId = wjInsMap.get("TZ_APP_INS_ID").toString();
							
							String wjInsFolder = "";
							String ryInfo = "";
							if("".equals(oprid) || "TZ_GUEST".equals(oprid)){
								ryInfo = "匿名用户";
							}else{
								String rySql = "select TZ_MSH_ID,TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?";
								Map<String,Object> ryMap = sqlQuery.queryForMap(rySql, new Object[]{ oprid });
								if(ryMap != null){
									String msSqh = ryMap.get("TZ_MSH_ID") == null ? "" : ryMap.get("TZ_MSH_ID").toString();
									String name = ryMap.get("TZ_REALNAME") == null ? "" : ryMap.get("TZ_REALNAME").toString();
									
									if(!"".equals(msSqh)){
										ryInfo = msSqh;
									}
									if(!"".equals(name)){
										name = name.replaceAll(" ", "_");
										if("".equals(ryInfo)){
											ryInfo = name;
										}else{
											ryInfo = msSqh + "_" + name;
										}
									}
								}
							}
							wjInsFolder = appInsId + "_" + ryInfo;
							
							String InsPath = zipPath + (File.separator) + wjInsFolder;
							File insF = new File(InsPath);
							if (!insF.exists()) {
								insF.mkdirs();
							}
							
							Map<String,Integer> userFileCountMap = new HashMap<String,Integer>();
							
							//查询问卷实例下所有附件并复制到打包目录下
							String attSql = "SELECT TZ_XXX_BH,TZ_INDEX, ATTACHSYSFILENAME, ATTACHUSERFILE,TZ_ATT_P_URL FROM PS_TZ_DC_WJATT_T A,PS_TZ_DC_WJATTCH_T B where TZ_APP_INS_ID=? and A.ATTACHSYSFILENAME=B.TZ_ATTACHSYSFILENA";
							List<Map<String,Object>> attaList = sqlQuery.queryForList(attSql, new Object[]{ appInsId });
							if(attaList != null && attaList.size() > 0){
								for(Map<String,Object> attaMap : attaList){
									//String itemId = attaMap.get("TZ_XXX_BH").toString();
									//String index = attaMap.get("TZ_INDEX").toString();
									String attaSystemFileName = attaMap.get("ATTACHSYSFILENAME").toString();
									String attaUserFileName = attaMap.get("ATTACHUSERFILE").toString();
									String path = attaMap.get("TZ_ATT_P_URL").toString();
									
									if("/".equals(File.separator)){
										if (path.lastIndexOf("/") + 1 == path.length()) {
											path = path + attaSystemFileName;
										} else {
											path = path + "/" + attaSystemFileName;
										}
									}
									
									if("\\".equals(File.separator)){
										if (path.lastIndexOf("\\") + 1 == path.length()) {
											path = path + attaSystemFileName;
										} else {
											path = path +"\\" + attaSystemFileName;
										}
									}
									
									//重新命名文件，防止文件名冲突，如果有相同文件名在文件名后加(1)、(2)、(3)....
									attaUserFileName = this.renameFile(userFileCountMap, attaUserFileName);
									
									//防止文件名相同，文件名前加上信息项编号
									//attaUserFileName = itemId + "_" + index + "_" + attaUserFileName;
									//复制文件至打包目录
									zipUtil.fileChannelCopy(path, InsPath + (File.separator) + attaUserFileName);
								}
							}
						}
						
						ArrayList<String> sourcePathArr = new ArrayList<>();
						sourcePathArr.add(zipPath);
						zipUtil.createZip(sourcePathArr, tarLj + strUseFileName);
						
						//打包完成后删除文件
						zipUtil.deleteDir(tF);
						try {
							sqlQuery.update("update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
									new Object[] { (relLj + strUseFileName), processinstance });
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
					
				}else{
					//导出调查结果Excel
					ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
					List<String[]> dataCellKeys = new ArrayList<String[]>();
					List<String[]> listDataFields = new ArrayList<String[]>();
					
					String sql = "select TZ_XXX_MC,TZ_XXX_CCLX,TZ_XXX_BH,TZ_COM_LMC from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? and TZ_XXX_CCLX<>'F' and TZ_XXX_CCLX is not null and (TZ_IS_DOWNLOAD is null or TZ_IS_DOWNLOAD <> 'N') order by TZ_ORDER";
					List<Map<String,Object>> wjItemsList =  sqlQuery.queryForList(sql, new Object[]{ wjId });
				
					//生成导出excel标题行
					dataCellKeys.add(new String[]{ "tz-name-20170504","姓名" });
					dataCellKeys.add(new String[]{ "tz-mssqh-20170504","面试申请号" });
					dataCellKeys.add(new String[]{ "tz-status-20170504","完成状态" });
					
					for(Map<String,Object> itemMap: wjItemsList){
						String itemName = itemMap.get("TZ_XXX_MC").toString();
						String storeType = itemMap.get("TZ_XXX_CCLX").toString();
						String itemId = itemMap.get("TZ_XXX_BH").toString();
						String className = itemMap.get("TZ_COM_LMC").toString();
						
						//如果是表格题，按子问题导出
						if("T".equals(storeType)){
							String bgSql = "select TZ_QU_CODE,TZ_QU_NAME from PS_TZ_DCWJ_BGZWT_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
							List<Map<String,Object>> bgtList =  sqlQuery.queryForList(bgSql, new Object[]{ wjId, itemId });
							
							for(Map<String,Object> bgtMap: bgtList){
								String subQueId = bgtMap.get("TZ_QU_CODE").toString();
								String subQueName = bgtMap.get("TZ_QU_NAME") == null ? "" 
										: bgtMap.get("TZ_QU_NAME").toString();
								
								
								dataCellKeys.add(new String[] { itemId + "-" + subQueId, subQueName });
							}
						}else{
							dataCellKeys.add(new String[] { itemId , itemName });
						}
						
						listDataFields.add(new String[] { itemId , storeType, className });
					}
					
					
					// 数据
					List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
					
					String cyrSql = "select TZ_APP_INS_ID,TZ_DC_WC_STA,PERSON_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=? order by TZ_APP_INS_ID";
					List<Map<String,Object>> cyrList =  sqlQuery.queryForList(cyrSql, new Object[]{ wjId });
					//循环所有调查参与人
					for(Map<String,Object> cyrMap: cyrList){
						Map<String, Object> mapData = new HashMap<String, Object>();
						
						String wjInsId = cyrMap.get("TZ_APP_INS_ID").toString();
						String wjInsSta = cyrMap.get("TZ_DC_WC_STA") == null ? "": cyrMap.get("TZ_DC_WC_STA").toString();
						String cyrOprid = cyrMap.get("PERSON_ID") == null ? "": cyrMap.get("PERSON_ID").toString();
						
						if(!"".equals(cyrOprid)){
							//非匿名调查，导出姓名和面试申请号
							String msSqh = "";
							String name = "";
							sql = "select TZ_REALNAME,TZ_MSH_ID from PS_TZ_AQ_YHXX_TBL where OPRID=? limit 1";
							Map<String,Object> yhxxMap = sqlQuery.queryForMap(sql, new Object[]{ cyrOprid });
							if(yhxxMap != null){
								name = yhxxMap.get("TZ_REALNAME") == null ? "" : yhxxMap.get("TZ_REALNAME").toString();
								msSqh = yhxxMap.get("TZ_MSH_ID") == null ? "" : yhxxMap.get("TZ_MSH_ID").toString();
							}
							mapData.put("tz-name-20170504", name);
							mapData.put("tz-mssqh-20170504", msSqh);
						}else{
							mapData.put("tz-name-20170504", "匿名");
							mapData.put("tz-mssqh-20170504", "");
						}
						
						if("0".equals(wjInsSta)){
							mapData.put("tz-status-20170504", "已完成");
						}else{
							mapData.put("tz-status-20170504", "未完成");
						}
						
						//导出在线调查问题
						for(String[] expField: listDataFields){
							/********************************
							*	信息项存储类型对应查询表
							*	S=>TZ_DC_CC_T.TZ_APP_S_TEXT
							*	L=>TZ_DC_CC_T.TZ_APP_L_TEXT
							*	D=>TZ_APP_DHCC_T
							*	T=>TZ_DCDJ_BGT_T
							********************************/
							
							String itemId = expField[0];
							String storeType = expField[1];
							String className = expField[2];
							String answerSql;
							
							switch(storeType){
							case "S":
								String answerS = "";
								//下拉框和量表题，取描述值
								if("ComboBox".equals(className) || "QuantifyQu".equals(className)){
									String optDescSql = "select B.TZ_XXXKXZ_MS from PS_TZ_DC_CC_T A,PS_TZ_DCWJ_XXKXZ_T B where A.TZ_XXX_BH=B.TZ_XXX_BH  and A.TZ_APP_S_TEXT=B.TZ_XXXKXZ_MC and B.TZ_DC_WJ_ID=? and A.TZ_XXX_BH=? and A.TZ_APP_INS_ID=?";
									answerS = sqlQuery.queryForObject(optDescSql, new Object[]{ wjId, itemId, wjInsId }, "String");
								}else{
									answerSql = "select TZ_APP_S_TEXT from PS_TZ_DC_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?"; 
									answerS = sqlQuery.queryForObject(answerSql, new Object[]{ wjInsId, itemId }, "String");
								}
								mapData.put(itemId, answerS);
								break;
							case "L":
								String answerL = "";
								answerSql = "select TZ_APP_L_TEXT from PS_TZ_DC_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?"; 
								answerL = sqlQuery.queryForObject(answerSql, new Object[]{ wjInsId, itemId }, "String");
								mapData.put(itemId, answerL);
								break;
							case "D":
								String answerD = "";
								answerSql = "select TZ_APP_S_TEXT,TZ_KXX_QTZ from PS_TZ_DC_DHCC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=? and TZ_IS_CHECKED='Y' order by TZ_XXXKXZ_MC";
								List<Map<String,Object>> xztList = sqlQuery.queryForList(answerSql, new Object[]{ wjInsId, itemId });
								for(Map<String,Object> xztMap: xztList){
									String value = xztMap.get("TZ_APP_S_TEXT")== null ? "":xztMap.get("TZ_APP_S_TEXT").toString();
									String otherVal = xztMap.get("TZ_KXX_QTZ")== null ? "":xztMap.get("TZ_KXX_QTZ").toString();
									
									if(!"".equals(answerD)){
										answerD = answerD + "，";
									}
									if(!"".equals(otherVal)){
										answerD = answerD + otherVal;
									}else{
										answerD = answerD + value;
									}
								}
								mapData.put(itemId, answerD);
								break;
							case "T":
								String bgSql = "select TZ_QU_CODE from PS_TZ_DCWJ_BGZWT_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
								List<Map<String,Object>> bgtList =  sqlQuery.queryForList(bgSql, new Object[]{ wjId, itemId });
								
								for(Map<String,Object> bgtMap: bgtList){
									String answerT = "";
									String subQueId = bgtMap.get("TZ_QU_CODE").toString();
									
									String subQueSql = "select group_concat(TZ_OPT_NAME SEPARATOR '，') from PS_TZ_DCDJ_BGT_T A,PS_TZ_DCWJ_BGKXZ_T B where B.TZ_DC_WJ_ID=? and TZ_APP_INS_ID=? and A.TZ_XXX_BH=B.TZ_XXX_BH and A.TZ_XXXKXZ_XXMC=B.TZ_OPT_CODE and B.TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=? and TZ_APP_S_TEXT='Y'";
									answerT = sqlQuery.queryForObject(subQueSql, new Object[]{ wjId, wjInsId, itemId, subQueId }, "String");
									
									mapData.put(itemId + "-" + subQueId, answerT);
								}
								break;
							}
						}
						dataList.add(mapData);
					}

					boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
					if (rst) {
						String urlExcel = excelHandle.getExportExcelPath();
						try {
							sqlQuery.update("update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
									new Object[] { urlExcel, processinstance });
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			this.logError("系统错误："+e.getMessage());
		}
	}
	
	
	/**
	 * 重新命名文件名，防止同名文件冲突
	 * @param fileMap
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("null")
	private String renameFile(Map<String,Integer> fileMap,String fileName){
		if(fileMap != null){
			if(fileMap.get(fileName) == null){
				fileMap.put(fileName, 1);
			}else{
				int index = fileMap.get(fileName);
				fileMap.replace(fileName, index+1);
				
				int lastIndex = fileName.lastIndexOf(".");
				if(lastIndex > 0){
					fileName = fileName.substring(0,lastIndex) 
							+ "("+ index +")" 
							+ fileName.substring(lastIndex,fileName.length());
				}
			}
		}else{
			fileMap.put(fileName, 1);
		}
		return fileName;
	}
}
