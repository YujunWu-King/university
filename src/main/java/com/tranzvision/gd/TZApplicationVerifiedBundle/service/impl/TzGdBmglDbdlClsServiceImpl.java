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
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsprcsrqstMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

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
	private GetSeqNum getSeqNum;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TzDealWithXMLServiceImpl tzDealWithXMLServiceImpl;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;

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
				String comParams = jacksonUtil.getString("strAppId");

				// 打包文件名称;
				String fileName = jacksonUtil.getString("zldbName");
				
				// strComContent = fileName;
				if (comParams != null && !"".equals(comParams)) {
					if (comParams.lastIndexOf(";") + 1 == comParams.length()) {
						comParams = comParams.substring(0, comParams.length() - 1);
					}
					String[] arrAppId = comParams.split(";");

					processInstance = getSeqNum.getSeqNum("TZ_EXCEL_DRXX_T", "PROCESSINSTANCE");
					PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
					psTzExcelDrxxT.setProcessinstance(processInstance);
					psTzExcelDrxxT.setTzComId("TZ_BMGL_BMBSH_COM");
					psTzExcelDrxxT.setTzPageId("TZ_BMGL_DBDL_STD");
					psTzExcelDrxxT.setTzDrLxbh("1");
					psTzExcelDrxxT.setTzDrTaskDesc(fileName);

					psTzExcelDrxxT.setTzStartDtt(new Date());
					psTzExcelDrxxT.setTzDrTotalNum(arrAppId.length);
					psTzExcelDrxxT.setOprid(currentOprid);
					psTzExcelDrxxT.setTzIsViewAtt("Y");
					psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);

					// 生产打包文件附件存放的路径;
					SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMdd");
					String s_dt = dateFormate.format(new Date());
					int XUHAO = getSeqNum.getSeqNum("TZ_ZLDB_AE", "TZ_PACKAGE_ID");
					String ID = "000000000" + String.valueOf(XUHAO);
					ID = ID.substring(ID.length() - 9, ID.length());

					String fjlj = "";
					String packDir = "";
					String websiteDir = getSysHardCodeVal.getWebsiteFileUploadPath();
					if (websiteDir.lastIndexOf("/") + 1 == websiteDir.length()) {
						fjlj = websiteDir + currentOprid + "/" + s_dt + "/" + "pack" + "/" + ID;
						packDir = websiteDir + currentOprid + "/" + s_dt + "/" + "pack";
					} else {
						fjlj = websiteDir + "/" + currentOprid + "/" + s_dt + "/" + "pack" + "/" + ID;
						packDir = websiteDir + "/" + currentOprid + "/" + s_dt + "/" + "pack";
					}

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
					psprcsrqst.setOprid(currentOprid);
					psprcsrqst.setRundttm(new Date());
					psprcsrqst.setRunstatus("7");
					psprcsrqstMapper.insert(psprcsrqst);

					String[] appids = comParams.split(";");
					for (int i = 0; i < appids.length; i++) {
						// 生产报名表xml文件;
						String appInsID = appids[i];
						String OPRID = jdbcTemplate.queryForObject(
								"SELECT OPRID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ?",
								new Object[] { Long.parseLong(appInsID) }, "String");
						String TZ_APP_TPL_ID = jdbcTemplate.queryForObject(
								"SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?",
								new Object[] { Long.parseLong(appInsID) }, "String");
						tzDealWithXMLServiceImpl.replaceXMLPulish(appInsID, OPRID, TZ_APP_TPL_ID, true,
								fjlj + "/" + appInsID, errMsg);
						String tFile = request.getServletContext().getRealPath(fjlj + "/" + appInsID);
						File tF = new File(tFile);
						if (!tF.exists()) {
							tF.mkdirs();
						}

						// 将考生的材料复制;
						String str_attachfilename = "", str_attachfile = "";
						String sqlPackage = "SELECT ATTACHSYSFILENAME, ATTACHUSERFILE  FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH IN (SELECT TEMP.TZ_XXX_BH  FROM PS_TZ_TEMP_FIELD_T TEMP , PS_TZ_APP_XXXPZ_T APP  WHERE TEMP.TZ_APP_TPL_ID = APP.TZ_APP_TPL_ID AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH AND APP.TZ_APP_TPL_ID = (SELECT C.TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T C WHERE C.TZ_APP_INS_ID=?) AND APP.TZ_IS_DOWNLOAD='Y') UNION SELECT ATTACHSYSFILENAME, ATTACHUSERFILE  FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH IN (SELECT TZ_XXX_BH FROM PS_TZ_FORM_ATT2_T WHERE TZ_APP_INS_ID=? )";
						List<Map<String, Object>> packList = jdbcTemplate.queryForList(sqlPackage,
								new Object[] { appInsID, appInsID, appInsID, appInsID });
						if (packList != null && packList.size() > 0) {
							for (int j = 0; j < packList.size(); j++) {
								str_attachfilename = (String) packList.get(j).get("ATTACHSYSFILENAME");
								str_attachfile = (String) packList.get(j).get("ATTACHUSERFILE");
								if (str_attachfilename != null && !"".equals(str_attachfilename)
										&& str_attachfile != null && !"".equals(str_attachfile)) {
									str_attachfile.replaceAll("/", "_");
									String sFile = "";
									this.fileChannelCopy(sFile, tFile + File.separator + str_attachfile);
								}
							}
						}

						// 生产推荐信xml文件;
						long TZ_TJX_APP_INS_ID = 0;
						String TJR_TZ_TJX_APP_INS_ID = "", TJR_TZ_APP_TPL_ID = "";
						String tjxSql = "SELECT TZ_TJX_APP_INS_ID,B.TZ_APP_TPL_ID,TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL A, PS_TZ_APP_INS_T B WHERE A.TZ_APP_INS_ID =? AND TZ_MBA_TJX_YX='Y' AND A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U'";
						List<Map<String, Object>> tjxList = jdbcTemplate.queryForList(tjxSql,
								new Object[] { Long.parseLong(appInsID) });
						if (tjxList != null && tjxList.size() > 0) {
							for (int j = 0; j < tjxList.size(); j++) {
								try{
									TZ_TJX_APP_INS_ID = Long.parseLong( tjxList.get(j).get("TZ_TJX_APP_INS_ID").toString());
								}catch(Exception e){
									TZ_TJX_APP_INS_ID = 0L;
								}
								
								TJR_TZ_TJX_APP_INS_ID = String.valueOf(TZ_TJX_APP_INS_ID);
								TJR_TZ_APP_TPL_ID = (String) tjxList.get(j).get("TZ_APP_TPL_ID");
								// tjrName =
								// (String)tjxList.get(j).get("TZ_REFERRER_NAME");
								tzDealWithXMLServiceImpl.replaceXMLPulish(TJR_TZ_TJX_APP_INS_ID, OPRID,
										TJR_TZ_APP_TPL_ID, true, fjlj + "/" + appInsID, errMsg);

								// 将考生的推荐信材料复制;
								String str_attachfilename2 = "", str_attachfile2 = "";
								String sqlPackagetjx = "SELECT ATTACHSYSFILENAME, ATTACHUSERFILE  FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH IN (SELECT TEMP.TZ_XXX_BH  FROM PS_TZ_TEMP_FIELD_T TEMP , PS_TZ_APP_XXXPZ_T APP  WHERE TEMP.TZ_APP_TPL_ID = APP.TZ_APP_TPL_ID AND TEMP.TZ_XXX_NO = APP.TZ_XXX_BH AND APP.TZ_APP_TPL_ID = (SELECT C.TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T C WHERE C.TZ_APP_INS_ID=?) AND APP.TZ_IS_DOWNLOAD='Y') ";
								List<Map<String, Object>> packTjxList = jdbcTemplate.queryForList(sqlPackagetjx,
										new Object[] { TZ_TJX_APP_INS_ID, TZ_TJX_APP_INS_ID, TZ_TJX_APP_INS_ID,
												TZ_TJX_APP_INS_ID });
								if (packTjxList != null && packTjxList.size() > 0) {
									for (int k = 0; k < packTjxList.size(); k++) {
										str_attachfilename2 = (String) packTjxList.get(k).get("ATTACHSYSFILENAME");
										str_attachfile2 = (String) packTjxList.get(k).get("ATTACHUSERFILE");
										if (str_attachfilename2 != null && !"".equals(str_attachfilename2)
												&& str_attachfile2 != null && !"".equals(str_attachfile2)) {
											str_attachfile2.replaceAll("/", "_");
											String sFile = "";

											this.fileChannelCopy(sFile, tFile + File.separator + str_attachfile2);
										}
									}
								}

							}
						}

						/* 如果是从后台上传的推荐信 */
						String sqlTjx2 = "SELECT ATTACHSYSFILENAME, ATTACHUSERFILE FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID =? AND TZ_MBA_TJX_YX='Y'";
						List<Map<String, Object>> tjxList2 = jdbcTemplate.queryForList(sqlTjx2,
								new Object[] { appInsID });
						if (tjxList2 != null && tjxList2.size() > 0) {
							for (int j = 0; j < tjxList2.size(); j++) {
								str_attachfilename = (String) tjxList2.get(j).get("ATTACHSYSFILENAME");
								str_attachfile = (String) tjxList2.get(j).get("ATTACHUSERFILE");
								if (str_attachfilename != null && !"".equals(str_attachfilename)
										&& str_attachfile != null && !"".equals(str_attachfile)) {
									str_attachfile.replaceAll("/", "_");
									String sFile = "";
									this.fileChannelCopy(sFile, tFile + File.separator + str_attachfile);
								}
							}
						}
					}
					
					
					ArrayList<String> sourcePathArr = new ArrayList<>();
					
					String sourceDir = request.getServletContext().getRealPath(fjlj);
					
			    	sourcePathArr.add(sourceDir);
			    	String packDir2 = request.getServletContext().getRealPath(packDir);
			    	packDir2 = packDir2 + File.separator + fileName.replaceAll("/", "") + "_" + ID + ".rar";
			        this.createZip(sourcePathArr,packDir2);
			        Psprcsrqst psprcsrqst2 = psprcsrqstMapper.selectByPrimaryKey(processInstance);
					if(psprcsrqst2 != null){
						psprcsrqst2.setRunstatus("9");
						psprcsrqstMapper.updateByPrimaryKey(psprcsrqst2);
					}
					
					PsTzExcelDattT psTzExcelDattT2 = psTzExcelDattTMapper.selectByPrimaryKey(processInstance);
					if(psTzExcelDattT2 != null){
						psTzExcelDattT2.setTzFwqFwlj(packDir);
						psTzExcelDattT2.setTzSysfileName(fileName.replaceAll("/", "") + "_" + ID + ".rar");
						psTzExcelDattTMapper.updateByPrimaryKey(psTzExcelDattT2);
					}
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
}
