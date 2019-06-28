package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormExportCls;
import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.PdfPrintbyModel;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*打包批处理方法*/
@Service
public class TzGdBmglFSZLEngineCls extends BaseEngine {

	public void OnExecute() throws Exception {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		String runControlId = this.getRunControlID();
		int processinstance = 0;
		processinstance = jdbcTemplate.queryForObject(
				"SELECT PRCSINSTANCE FROM PSPRCSRQST where RUN_ID = ? limit 0,1", new Object[] { runControlId },
				"Integer");

		
		PdfPrintbyModel pdfPrintbyModel = new PdfPrintbyModel();
		try {
			//web根目录;
			//String webRoot = this.getClass().getClassLoader().getResource("/").getPath();
			Resource resource = new ClassPathResource("conf/cookieSession.properties");
			Properties cookieSessioinProps = null;
			cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
			String webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
			String webRoot = System.getProperty(webAppRootKey);
			
			//查看当前路径是以什么分割的;
			String systemFileSeparatorType = "";
			if("/".equals(File.separator)){
				systemFileSeparatorType = "U";
			}
			if("\\".equals(File.separator)){
				systemFileSeparatorType = "W";
			}
			
			
			if((webRoot.lastIndexOf("/") + 1 != webRoot.length()) && "U".equals(systemFileSeparatorType)){
				webRoot = webRoot + "/";
			}
			
			if((webRoot.lastIndexOf("\\") + 1 != webRoot.length()) && "W".equals(systemFileSeparatorType)){
				webRoot = webRoot + "\\";
			}

			AppFormExportCls appFormExportCls = new AppFormExportCls();

			String strAppIns = "";
			String fjlj = "";
			String relLj = "";
			String packDir = "";
			String packRelLj = "";
			String ID = "";
			String[] appids = null;
			
			String appInsSQL = "select TZ_AUD_LIST,TZ_JD_URL,TZ_REL_URL from PS_TZ_BMB_DCE_T WHERE RUN_CNTL_ID=? limit 0,1";
			Map<String, Object> appInsMap = jdbcTemplate.queryForMap(appInsSQL, new Object[] { runControlId });
			if (appInsMap == null) {
				//没有打包下载对应的值，则结束进程;
				jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
						new Object[] { "9", processinstance });
				jdbcTemplate.execute("commit");
				return;
			} else {
				strAppIns = (String) appInsMap.get("TZ_AUD_LIST");
				fjlj = (String) appInsMap.get("TZ_JD_URL");
				relLj = (String) appInsMap.get("TZ_REL_URL");
				//没有打包下载对应的值，则结束进程;
				if( strAppIns == null || "".equals(strAppIns)
						|| fjlj == null || "".equals(fjlj)
						|| relLj == null || "".equals(relLj)){
					jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
							new Object[] { "9", processinstance });
					jdbcTemplate.execute("commit");
					return;
				}
				
				if("/".equals(File.separator)){
					ID = fjlj.split("/")[fjlj.split("/").length-1];

					if (fjlj.lastIndexOf("/") + 1 != fjlj.length()) {
						fjlj = fjlj + "/";
					}
				}
				if("\\".equals(File.separator)){
					ID = fjlj.split("\\\\")[fjlj.split("\\\\").length-1];

					if (fjlj.lastIndexOf("\\") + 1 != fjlj.length()) {
						fjlj = fjlj + "\\";
					}
				}
				packRelLj = relLj.substring(0, relLj.lastIndexOf("/"));
				 
				packDir = fjlj + "..";
				
				 
				jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
						new Object[] { "7", processinstance });
				jdbcTemplate.execute("commit");
			}

			appids = strAppIns.split(";");

			for (int i = 0; i < appids.length; i++) {
				// 生产报名表pdf文件;
				String appInsID = appids[i];
				String OPRID = jdbcTemplate.queryForObject(
						"SELECT OPRID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ?",
						new Object[] { Long.parseLong(appInsID) }, "String");
				String relName = jdbcTemplate.queryForObject(
						"SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?", new Object[] { OPRID }, "String");
				
				String strMshId = jdbcTemplate.queryForObject(
						"SELECT TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?", new Object[] { OPRID }, "String");
				
				String pdfType = jdbcTemplate.queryForObject(" select TZ_PDF_TYPE from PS_TZ_APP_INS_T a,PS_TZ_APPTPL_DY_T b where a.TZ_APP_TPL_ID=b.TZ_APP_TPL_ID and a.TZ_APP_INS_ID=?", new Object[]{Long.parseLong(appInsID)},"String");
				
				if (relName != null && !"".equals(relName)) {
					relName = relName.replaceAll(" ", "_");
				} else {
					relName = "";
				}

				//String tFile = request.getServletContext().getRealPath(fjlj + "/" + appInsID + "_" + relName);
				//String tFile = fjlj + appInsID + "_" + relName;
				String tFile = fjlj + strMshId + "_" + relName;
				File tF = new File(tFile);
				if (!tF.exists()) {
					tF.mkdirs();
				}
				
				//html转PDF；
				// appFormExportClsServiceImpl.generatePdf(ID+ "/" + appInsID+"_"+relName, relName + "_报名表.pdf", appInsID,"A");
				if("HPDF".equals(pdfType)){
					if("/".equals(File.separator)){
						//appFormExportCls.generatePdf(ID + "/" + appInsID + "_" + relName, relName + "_报名表.pdf", appInsID, "A");
						appFormExportCls.generatePdf(ID + "/" + strMshId + "_" + relName, relName + "_报名表.pdf", appInsID, "A");
					}
					
					if("\\".equals(File.separator)){
						//appFormExportCls.generatePdf(ID + "\\" + appInsID + "_" + relName, relName + "_报名表.pdf", appInsID, "A");
						appFormExportCls.generatePdf(ID + "\\" + strMshId + "_" + relName, relName + "_报名表.pdf", appInsID, "A");
					}
					
				}
				if("TPDF".equals(pdfType)){
					pdfPrintbyModel.createPdf(tFile, appInsID, "A");
				}

				int file_count = 1;

				// 将考生的材料复制;
				String str_attachfilename = "", str_attachfile = "";
				String sqlPackage = "SELECT A.ATTACHUSERFILE,A.ATTACHSYSFILENAME,A.TZ_ACCESS_PATH,B.TZ_XXX_MC FROM PS_TZ_FSZL_T A,PS_TZ_FORM_ATT2_T B WhERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_XXX_BH =A.ATTACHSYSFILENAME AND A.TZ_APP_INS_ID =? ORDER BY A.ROW_ADDED_DTTM";
				List<Map<String, Object>> packList = jdbcTemplate.queryForList(sqlPackage,
						new Object[] { appInsID});
				if (packList != null && packList.size() > 0) {
					for (int j = 0; j < packList.size(); j++) {
						str_attachfilename = (String) packList.get(j).get("ATTACHSYSFILENAME");
						str_attachfile = (String) packList.get(j).get("TZ_XXX_MC");
						String sFile = (String) packList.get(j).get("TZ_ACCESS_PATH");
						
						if (str_attachfilename != null && !"".equals(str_attachfilename) && str_attachfile != null
								&& !"".equals(str_attachfile) && sFile != null && !"".equals(sFile)) {
							str_attachfile.replaceAll("/", "_");
							str_attachfile.replaceAll("\\\\", "_");
							
							//sFile = request.getServletContext().getRealPath(sFile);
							sFile = webRoot + sFile;
							/*
							if("/".equals(File.separator)){
								sFile = webRoot + "../.." + sFile;
							}
							if("\\".equals(File.separator)){
								sFile = webRoot + "..\\.." + sFile.replaceAll("/", "\\\\");
							}
							*/

							File attFile = new File(sFile);
							if(attFile.exists()){
								sFile = attFile.getAbsolutePath();
							}else{
								continue;
							}
							
							if("/".equals(File.separator)){
								if (sFile.lastIndexOf("/") + 1 == sFile.length()) {
									sFile = sFile + str_attachfilename;
								} else {
									sFile = sFile + "/" + str_attachfilename;
								}
								this.fileChannelCopy(sFile, tFile + "/" + file_count + "_" + str_attachfile);
								file_count++;
							}
							
							if("\\".equals(File.separator)){
								if (sFile.lastIndexOf("\\") + 1 == sFile.length()) {
									sFile = sFile + str_attachfilename;
								} else {
									sFile = sFile +"\\" + str_attachfilename;
								}
								this.fileChannelCopy(sFile, tFile + "\\" + file_count + "_" + str_attachfile);
								file_count++;
							}
							
							
						}
					}
				}

			}

			ArrayList<String> sourcePathArr = new ArrayList<>();

			//String sourceDir = request.getServletContext().getRealPath(fjlj);
			String sourceDir = fjlj;

			sourcePathArr.add(sourceDir);
			//String packDir2 = request.getServletContext().getRealPath(packDir);
			String packDir2 = packDir;
			
			if("/".equals(File.separator)){
				if (packDir2.lastIndexOf("/") == packDir2.length()) {
					packDir2 = packDir2 + ID + ".rar";
				} else {
					packDir2 = packDir2 + "/" + ID + ".rar";
				}
			}
			
			if("\\".equals(File.separator)){
				if (packDir2.lastIndexOf("\\") == packDir2.length()) {
					packDir2 = packDir2 + ID + ".rar";
				} else {
					packDir2 = packDir2 + "\\" + ID + ".rar";
				}
			}
			

			// 打包;
			this.createZip(sourcePathArr, packDir2);

			jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
					new Object[] { "9", processinstance });

			jdbcTemplate.update(
					"UPDATE PS_TZ_EXCEL_DATT_T SET TZ_SYSFILE_NAME = ?,TZ_FWQ_FWLJ=? WHERE PROCESSINSTANCE=?",
					new Object[] { ID + ".rar", packRelLj, processinstance });
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
					new Object[] { "10", processinstance });
		}
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
	 * 
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public void createZip(ArrayList<String> sourcePathArr, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		try {
			if (sourcePathArr != null && sourcePathArr.size() > 0) {
				fos = new FileOutputStream(zipPath);
				zos = new ZipOutputStream(fos);

				for (int i = 0; i < sourcePathArr.size(); i++) {
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
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				for (File f : files) {
					writeZip(f, parentPath, zos);
				}
			} else {
				FileInputStream fis = null;
				DataInputStream dis = null;
				try {
					fis = new FileInputStream(file);
					dis = new DataInputStream(new BufferedInputStream(fis));
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (dis != null) {
							dis.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
