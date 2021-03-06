package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS类：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_DBDL_CLS
 * 
 * @author tang 报名管理-材料批量打包下载
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglDbdlClsServiceImpl")
public class TzGdBmglDbdlClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	@Autowired
	private PsTzBmbDceTMapper psTzBmbDceTMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
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
			String[][] orderByArr = new String[][] {{"PROCESSINSTANCE","DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_DR_TASK_DESC", "OPRID", "TZ_STARTTIME_CHAR", "PROCESSINSTANCE", "DESCR" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("fileName", rowList[0]);
					mapList.put("czPerName", rowList[1]);
					mapList.put("bgTime", rowList[2]);
					mapList.put("AEId", rowList[3]);
					mapList.put("AEState", rowList[4]);
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

	// 修改;
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strComContent = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strComContent;
		}
		int processInstance = 0;
		String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			
			for (int num = 0; num < actData.length; num++) {
				
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 保存数据;
				String strAppInsIdList = jacksonUtil.getString("strAppId");

				//打包类型
				String strPackageType = jacksonUtil.getString("packageType");
				
				// 打包文件名称;
				String fileName = jacksonUtil.getString("zldbName");
				
				// strComContent = fileName;
				if (strAppInsIdList != null && !"".equals(strAppInsIdList)) {
					if (strAppInsIdList.lastIndexOf(";") + 1 == strAppInsIdList.length()) {
						strAppInsIdList = strAppInsIdList.substring(0, strAppInsIdList.length() - 1);
					}
					
					// 生产打包文件附件存放的路径;
					SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMdd");
					String s_dt = dateFormate.format(new Date());
					int XUHAO = getSeqNum.getSeqNum("TZ_ZLDB_AE", "TZ_PACKAGE_ID");
					String ID = "000000000" + String.valueOf(XUHAO);
					ID = ID.substring(ID.length() - 9, ID.length());
					ID = currentOprid+"_"+s_dt+"_"+ ID;

					String fjlj = "";
					//String packDir = "";
					//String websiteDir = getSysHardCodeVal.getWebsiteFileUploadPath();
					String websiteDir = getSysHardCodeVal.getBmbPackRarDir();
					if (websiteDir.lastIndexOf("/") + 1 == websiteDir.length()) {
						fjlj = websiteDir + ID;
						//packDir = websiteDir;
					} else {
						fjlj = websiteDir + "/" + ID;
						//packDir = websiteDir + "/";
					}

					/*生成运行控制ID*/
					SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				    String s_dtm = datetimeFormate.format(new Date());
					String runCntlId = "BMBCLDB" + s_dtm + "_" + getSeqNum.getSeqNum("TZ_BMGL_BMBSH_COM", "DCE_AE");
					
					PsTzBmbDceT psTzBmbDceT = new PsTzBmbDceT();
					psTzBmbDceT.setRunCntlId(runCntlId);
					psTzBmbDceT.setTzAudList(strAppInsIdList);
					psTzBmbDceT.setTzJdUrl(request.getServletContext().getRealPath(fjlj));
					psTzBmbDceT.setTzRelUrl(fjlj);
					psTzBmbDceTMapper.insert(psTzBmbDceT);
					
					String[] appids = strAppInsIdList.split(";");
					String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
					String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

					String strDbProgressName = "TZGD_DBDL_PROC_01";
					if("B".equals(strPackageType)){
						strDbProgressName = "TZGD_DBDL_PROC_02";
					}else if("C".equals(strPackageType)) {	//复试资料打包下载
						strDbProgressName = "TZGD_DBDL_PROC_03";
					}
					BaseEngine tmpEngine = tZGDObject.createEngineProcess(currentOrgId, strDbProgressName);
					
					//指定调度作业的相关参数
					EngineParameters schdProcessParameters = new EngineParameters();

					schdProcessParameters.setBatchServer("");
					schdProcessParameters.setCycleExpression("");
					schdProcessParameters.setLoginUserAccount(currentAccountId);
					schdProcessParameters.setPlanExcuteDateTime(new Date());
					schdProcessParameters.setRunControlId(runCntlId);
					
					//调度作业
					tmpEngine.schedule(schdProcessParameters);

					//processInstance = getSeqNum.getSeqNum("TZ_EXCEL_DRXX_T", "PROCESSINSTANCE");
					processInstance=tmpEngine.getProcessInstanceID();
					
					if(processInstance>0){
						PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
						psTzExcelDrxxT.setProcessinstance(processInstance);
						psTzExcelDrxxT.setTzComId("TZ_BMGL_BMBSH_COM");
						psTzExcelDrxxT.setTzPageId("TZ_BMGL_DBDL_STD");
						psTzExcelDrxxT.setTzDrLxbh("1");
						psTzExcelDrxxT.setTzDrTaskDesc(fileName);

						psTzExcelDrxxT.setTzStartDtt(new Date());
						psTzExcelDrxxT.setTzDrTotalNum(appids.length);
						psTzExcelDrxxT.setOprid(currentOprid);
						psTzExcelDrxxT.setTzIsViewAtt("Y");
						psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);

						

						PsTzExcelDattT psTzExcelDattT = new PsTzExcelDattT();
						psTzExcelDattT.setProcessinstance(processInstance);
						psTzExcelDattT.setTzSysfileName("");
						psTzExcelDattT.setTzFileName(fileName);
						psTzExcelDattT.setTzCfLj("A");
						psTzExcelDattT.setTzFjRecName("TZ_APPFATTACH_T");
						psTzExcelDattT.setTzFwqFwlj("");
						psTzExcelDattTMapper.insert(psTzExcelDattT);
						
						Psprcsrqst psprcsrqst = new Psprcsrqst();
						psprcsrqst.setPrcsinstance(processInstance);
						psprcsrqst.setRunId(runCntlId);
						psprcsrqst.setOprid(currentOprid);
						psprcsrqst.setRundttm(new Date());
						psprcsrqst.setRunstatus("5");
						psprcsrqstMapper.insert(psprcsrqst);
						
						
					}
					
					

					/*
					for (int i = 0; i < appids.length; i++) {
						// 生产报名表pdf文件;
						String appInsID = appids[i];
						String OPRID = jdbcTemplate.queryForObject(
								"SELECT OPRID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ?",
								new Object[] { Long.parseLong(appInsID) }, "String");
						String relName = jdbcTemplate.queryForObject(
								"SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?",
								new Object[] { OPRID }, "String");
						if(relName!=null && !"".equals(relName)){
							relName = relName.replaceAll(" ", "_");
						}else{
							relName = "";
						}

						String tFile = request.getServletContext().getRealPath(fjlj + "/" + appInsID+"_"+relName);
						File tF = new File(tFile);
						if (!tF.exists()) {
							tF.mkdirs();
						}
						appFormExportClsServiceImpl.generatePdf(ID+ "/" + appInsID+"_"+relName, relName + "_报名表.pdf", appInsID,"A");
						
						int file_count = 1;

						// 将考生的材料复制;
						String str_attachfilename = "", str_attachfile = "";
						String sqlPackage = "SELECT ATTACHSYSFILENAME, ATTACHUSERFILE,TZ_ACCESS_PATH  FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH IN (SELECT TEMP.TZ_XXX_BH  FROM PS_TZ_TEMP_FIELD_T TEMP , PS_TZ_APP_XXXPZ_T APP  WHERE TEMP.TZ_APP_TPL_ID = APP.TZ_APP_TPL_ID AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH AND APP.TZ_APP_TPL_ID = (SELECT C.TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T C WHERE C.TZ_APP_INS_ID=?) AND APP.TZ_IS_DOWNLOAD='Y') UNION SELECT ATTACHSYSFILENAME, ATTACHUSERFILE,TZ_ACCESS_PATH  FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH IN (SELECT TZ_XXX_BH FROM PS_TZ_FORM_ATT2_T WHERE TZ_APP_INS_ID=? )";
						List<Map<String, Object>> packList = jdbcTemplate.queryForList(sqlPackage,
								new Object[] { appInsID, appInsID, appInsID, appInsID });
						if (packList != null && packList.size() > 0) {
							for (int j = 0; j < packList.size(); j++) {
								str_attachfilename = (String) packList.get(j).get("ATTACHSYSFILENAME");
								str_attachfile = (String) packList.get(j).get("ATTACHUSERFILE");
								String sFile = (String) packList.get(j).get("TZ_ACCESS_PATH");;
								if (str_attachfilename != null && !"".equals(str_attachfilename)
										&& str_attachfile != null && !"".equals(str_attachfile)
										&& sFile != null && !"".equals(sFile)) {
									str_attachfile.replaceAll("/", "_");
									sFile = request.getServletContext().getRealPath(sFile);
									
									if(sFile.lastIndexOf(File.separator) + 1 == sFile.length()){
										sFile = sFile + str_attachfilename;
									}else{
										sFile = sFile + File.separator + str_attachfilename;
									}
									this.fileChannelCopy(sFile, tFile + File.separator + file_count+"_"+str_attachfile);
									file_count++;
								}
							}
						}

						// 生产推荐信xml文件;
						long TZ_TJX_APP_INS_ID = 0;
						//String TJR_TZ_TJX_APP_INS_ID = "", TJR_TZ_APP_TPL_ID = "";
						String tjxSql = "SELECT TZ_TJX_APP_INS_ID,B.TZ_APP_TPL_ID,TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL A, PS_TZ_APP_INS_T B WHERE A.TZ_APP_INS_ID =? AND TZ_MBA_TJX_YX='Y' AND A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U'";
						List<Map<String, Object>> tjxList = jdbcTemplate.queryForList(tjxSql,
								new Object[] { Long.parseLong(appInsID) });
						if (tjxList != null && tjxList.size() > 0) {
							for (int j = 0; j < tjxList.size(); j++) {
								try{
									TZ_TJX_APP_INS_ID = Long.parseLong( tjxList.get(j).get("TZ_TJX_APP_INS_ID").toString());
								}catch(Exception e){
									TZ_TJX_APP_INS_ID = 0L;
									continue;
								}
								

								String tzReferrer = (String)tjxList.get(j).get("TZ_REFERRER_NAME");
								appFormExportClsServiceImpl.generatePdf(ID+ "/" + appInsID+"_"+relName, relName + "_" + tzReferrer + "_推荐信.pdf", String.valueOf(TZ_TJX_APP_INS_ID),"B");
								
								// 将考生的推荐信材料复制;
								String str_attachfilename2 = "", str_attachfile2 = "";
								String sqlPackagetjx = "SELECT ATTACHSYSFILENAME, ATTACHUSERFILE,TZ_ACCESS_PATH  FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH IN (SELECT TEMP.TZ_XXX_BH  FROM PS_TZ_TEMP_FIELD_T TEMP , PS_TZ_APP_XXXPZ_T APP  WHERE TEMP.TZ_APP_TPL_ID = APP.TZ_APP_TPL_ID AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH AND APP.TZ_APP_TPL_ID = (SELECT C.TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T C WHERE C.TZ_APP_INS_ID=?) AND APP.TZ_IS_DOWNLOAD='Y') ";
								List<Map<String, Object>> packTjxList = jdbcTemplate.queryForList(sqlPackagetjx,
										new Object[] { TZ_TJX_APP_INS_ID, TZ_TJX_APP_INS_ID });
								if (packTjxList != null && packTjxList.size() > 0) {
									for (int k = 0; k < packTjxList.size(); k++) {
										str_attachfilename2 = (String) packTjxList.get(k).get("ATTACHSYSFILENAME");
										str_attachfile2 = (String) packTjxList.get(k).get("ATTACHUSERFILE");
										String sFile = (String) packTjxList.get(k).get("TZ_ACCESS_PATH");;
										if (str_attachfilename2 != null && !"".equals(str_attachfilename2)
												&& str_attachfile2 != null && !"".equals(str_attachfile2)
												&& sFile != null && !"".equals(sFile)) {
											str_attachfile2.replaceAll("/", "_");
											
											sFile = request.getServletContext().getRealPath(sFile);
											if(sFile.lastIndexOf(File.separator) + 1 == sFile.length()){
												sFile = sFile + str_attachfilename2;
											}else{
												sFile = sFile + File.separator + str_attachfilename2;
											}
											this.fileChannelCopy(sFile, tFile + File.separator + file_count+"_"+ str_attachfile2);
											file_count++;
										}
									}
								}

							}
						}

						// 如果是从后台上传的推荐信; 
						String sqlTjx2 = "SELECT ATTACHSYSFILENAME, ATTACHUSERFILE,TZ_ACCESS_PATH FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID =? AND TZ_MBA_TJX_YX='Y'";
						List<Map<String, Object>> tjxList2 = jdbcTemplate.queryForList(sqlTjx2,
								new Object[] { appInsID });
						if (tjxList2 != null && tjxList2.size() > 0) {
							for (int j = 0; j < tjxList2.size(); j++) {
								str_attachfilename = (String) tjxList2.get(j).get("ATTACHSYSFILENAME");
								str_attachfile = (String) tjxList2.get(j).get("ATTACHUSERFILE");
								String accessPath = (String) tjxList2.get(j).get("TZ_ACCESS_PATH");
								if (str_attachfilename != null && !"".equals(str_attachfilename)
										&& str_attachfile != null && !"".equals(str_attachfile)
										&& accessPath != null && !"".equals(accessPath)) {
									str_attachfile.replaceAll("/", "_");
									String sFile = request.getServletContext().getRealPath(accessPath);
									if(sFile.lastIndexOf(File.separator) + 1 == sFile.length()){
										sFile = sFile + str_attachfilename;
									}else{
										sFile = sFile + File.separator + str_attachfilename;
									}
									this.fileChannelCopy(sFile, tFile + File.separator + str_attachfile);
								}
							}
						}
					}
					
					
					ArrayList<String> sourcePathArr = new ArrayList<>();
					
					String sourceDir = request.getServletContext().getRealPath(fjlj);
					
			    	sourcePathArr.add(sourceDir);
			    	String packDir2 = request.getServletContext().getRealPath(packDir);
			    	if(packDir2.lastIndexOf(File.separator) == packDir2.length()){
			    		packDir2 = packDir2 + ID + ".rar";
			    	}else{
			    		packDir2 = packDir2 + File.separator + ID + ".rar";
			    	}
			    	
			    	//打包;
			        this.createZip(sourcePathArr,packDir2);
			        
			        Psprcsrqst psprcsrqst2 = psprcsrqstMapper.selectByPrimaryKey(processInstance);
					if(psprcsrqst2 != null){
						psprcsrqst2.setRunstatus("9");
						psprcsrqstMapper.updateByPrimaryKey(psprcsrqst2);
					}
					
					PsTzExcelDattT psTzExcelDattT2 = psTzExcelDattTMapper.selectByPrimaryKey(processInstance);
					if(psTzExcelDattT2 != null){
						psTzExcelDattT2.setTzFwqFwlj(packDir);
						psTzExcelDattT2.setTzSysfileName( ID + ".rar");
						psTzExcelDattTMapper.updateByPrimaryKey(psTzExcelDattT2);
					}
					*/
					
					
					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			
			Psprcsrqst psprcsrqst = psprcsrqstMapper.selectByPrimaryKey(processInstance);
			if(psprcsrqst != null){
				psprcsrqst.setRunstatus("10");
				psprcsrqstMapper.updateByPrimaryKey(psprcsrqst);
			}
		}

		return strComContent;
	}
	
	
	
	/* sFile:原文件地址，tFile目标文件地址 */
	public void fileChannelCopy(String sFile, String tFile) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;

		File s = new File(sFile);
		File t = new File(tFile);
		if (s.exists() && s.isFile()) {
			try {
				fi = new FileInputStream(s);
				fo = new FileOutputStream(t);
				in = fi.getChannel();// 得到对应的文件通道
				out = fo.getChannel();// 得到对应的文件通道
				in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fi.close();
					in.close();
					fo.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
	/**
     * 创建ZIP文件
     * @param sourcePath 文件或文件夹路径
     * @param zipPath 生成的zip文件存在路径（包括文件名）
     */
    public void createZip(ArrayList<String> sourcePathArr, String zipPath) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
        	if(sourcePathArr!=null && sourcePathArr.size() > 0){
        		fos = new FileOutputStream(zipPath);
                zos = new ZipOutputStream(fos);
                
        		for(int i = 0; i < sourcePathArr.size(); i++){
        			String sourcePath = sourcePathArr.get(i);
        			
                    writeZip(new File(sourcePath), "", zos);
        		}
        		
        	} 
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }

        }
    }
    
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if(file.exists()){
            if(file.isDirectory()){//处理文件夹
                parentPath+=file.getName()+File.separator;
                File [] files=file.listFiles();
                for(File f:files){
                    writeZip(f, parentPath, zos);
                }
            }else{
                FileInputStream fis=null;
                DataInputStream dis=null;
                try {
                    fis=new FileInputStream(file);
                    dis=new DataInputStream(new BufferedInputStream(fis));
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte [] content=new byte[1024];
                    int len;
                    while((len=fis.read(content))!=-1){
                        zos.write(content,0,len);
                        zos.flush();
                    }
                    
                
                } catch (FileNotFoundException e) {
                	e.printStackTrace();
                } catch (IOException e) {
                	e.printStackTrace();
                }finally{
                    try {
                        if(dis!=null){
                            dis.close();
                        }
                    }catch(IOException e){
                    	e.printStackTrace();
                    }
                }
            }
        }
    }  
    
    // 下载打包材料
 	public String tzQuery(String strParams, String[] errMsg) {
 		// 返回值;
 		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
 		JacksonUtil jacksonUtil = new JacksonUtil();
 		try {
 			errMsg [0] = "1";
 			errMsg [1] = "没有找到附件";
 			jacksonUtil.json2Map(strParams);
 			
 			String AEId = (String) jacksonUtil.getString("AEId");
 			if(AEId != null && !"".equals(AEId)){
 				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(Integer.parseInt(AEId));
 				if(psTzExcelDattT != null){
 					String fileUrl = psTzExcelDattT.getTzFwqFwlj();
 					String sysName = psTzExcelDattT.getTzSysfileName();
 					if(fileUrl != null && !"".equals(fileUrl)
 							&& sysName != null && !"".equals(sysName)){
 						String dir = request.getServletContext().getRealPath(fileUrl);
 						File file = new File(dir + File.separator + sysName );
 						if(file.exists() && file.isFile()){
 							errMsg [0] = "0";
 	 			 			errMsg [1] = "";
 							fileUrl = request.getContextPath() + fileUrl + "/" + sysName;
 							returnJsonMap.put("fileUrl", fileUrl);
 						}
 						
 					}
 				}
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 			errMsg[0] = "1";
 			errMsg[1] = e.toString();
 		}
 		return jacksonUtil.Map2json(returnJsonMap);
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
			String AEId = jacksonUtil.getString("AEId");
			if(AEId != null && !"".equals(AEId)){
				int processinstance = Integer.parseInt(AEId);
				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(processinstance);
				if(psTzExcelDattT != null){
					String lj = psTzExcelDattT.getTzFwqFwlj();
					String fileName = psTzExcelDattT.getTzSysfileName();
					if(lj != null && !"".equals(lj)
						&& fileName != null && !"".equals(fileName)){
						lj = request.getServletContext().getRealPath(lj);
						
						String dfile = fileName.substring(0, fileName.lastIndexOf(".rar"));
						String deleteFile = "";
						
						if(lj.lastIndexOf(File.separator) + 1 != lj.length()){
							deleteFile = lj + File.separator + dfile;
							lj = lj + File.separator + fileName;
						}else{
							deleteFile = lj + dfile;
							lj = lj + fileName;
						}
						File file = new File(lj);
						if(file.exists() && file.isFile()){
							file.delete();
						}
						
						
						if(dfile != null && !"".equals(dfile)){
							File deFile = new File(deleteFile);
							if(deFile.exists() && deFile.isDirectory()){
								deleteDir(deFile);
							}
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
 	
 	
 	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
