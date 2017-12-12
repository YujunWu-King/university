package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
////import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author xzx
 * @since 2017-12-8
 *
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.ZkzPrintWord")
public class ZkzPrintWord {

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/**
	 * 打印pdf准考证2017-9-22
	 * 
	 * @param classID
	 * @param batchID
	 * @param instanceID
	 * @return
	 */

	public String createPdf(String classID, String batchID, String instanceID) {
		// 通过hardcode定义打印模板ID
		String sqlZkzTempID = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		String pdfmbID = jdbcTemplate.queryForObject(sqlZkzTempID, new Object[] { "TZ_ZKZTEMP_ID_URL" }, "String");
		// 通过模板ID获取模板路径/print/singlepdf/sem/2017127/template/5/2017127.pdf
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String sqlSelectTplURL = "SELECT TZ_DYMB_PDF_URL FROM PS_TZ_DYMB_T WHERE TZ_JG_ID=? AND TZ_DYMB_ID=?";
		Map<String, Object> mapDataMbURL = jdbcTemplate.queryForMap(sqlSelectTplURL, new Object[] { orgid, pdfmbID });
		String zkzTempUrl = mapDataMbURL.get("TZ_DYMB_PDF_URL") == null ? "" : mapDataMbURL.get("TZ_DYMB_PDF_URL").toString();
		// 拼装模板全路径,包含文件名
		zkzTempUrl = request.getServletContext().getRealPath(zkzTempUrl);
		// 下载文件路径 /statics/download/zkz/20171208/
		String filepath = getSysHardCodeVal.getDownloadPath() + "/zkz/"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/";

		// 用于返回值
		// String filepathandName1 = request.getContextPath() + filepath;//
		// /university/statics/download/zkz/20171208/
		// 下载文件路径
		String filepathandName = filepath;
		// 拼装下载文件全路径
		filepath = request.getServletContext().getRealPath(filepath);

		// 检测文件夹是否存在，如果不存在，创建文件夹
		File dir = new File(filepath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 添加"/" 后缀
		if (!StringUtils.endsWith(filepath, "/")) {
			filepath = filepath + "/";
		}
		// 定义下载文件名称
		String fileNamePdf = "zkz_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
		// 下载文件路径,包含文件名
		filepathandName = filepathandName + fileNamePdf;
		// 拼装下载全路径,包含文件名
		String urlPdf = filepath + fileNamePdf;
		// -end
		// 模板路径
		// String templatePath = "E:\\tmp.pdf";
		// 生成的新文件路径
		// String urlPdf = "E:\\new.pdf";

		PdfReader reader;
		FileOutputStream out;
		ByteArrayOutputStream bos;
		PdfStamper stamper;

		try {
			out = new FileOutputStream(urlPdf);
			// 输出流
			reader = new PdfReader(zkzTempUrl);// 读取pdf模板
			bos = new ByteArrayOutputStream();
			stamper = new PdfStamper(reader, bos);
			AcroFields form = stamper.getAcroFields();

			// 获取模板字段需要的数据
			Map<String, Object> dataMap = new HashMap<String, Object>();
			getDatas(dataMap, classID, batchID, instanceID, pdfmbID);
			// System.out.println(dataMap);
			for (String key : dataMap.keySet()) {
				String value = (String) dataMap.get(key);
				// System.out.println("key= " + key + " value=" + value);
				// 给模板字段赋值
				form.setField(key, value);
			}
			// 如果为false那么生成的PDF文件还能编辑
			stamper.setFormFlattening(true);
			// 添加图片start
			// Image image = Image.getInstance("E:\\tmp.jpg");
			String sqlSelectOprid = "SELECT ROW_ADDED_OPRID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
			Map<String, Object> mapDataOprid = jdbcTemplate.queryForMap(sqlSelectOprid, new Object[] { instanceID });
			String strOPRID = mapDataOprid.get("ROW_ADDED_OPRID") == null ? "" : mapDataOprid.get("ROW_ADDED_OPRID").toString();
			// 照片
			String sqlSelectImageUrl = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
			Map<String, Object> imgMap = jdbcTemplate.queryForMap(sqlSelectImageUrl, new Object[] { strOPRID });

			String titleImageUrl = "";
			if (imgMap != null) {
				String tzAttAUrl = (String) imgMap.get("TZ_ATT_A_URL");
				String sysImgName = (String) imgMap.get("TZ_ATTACHSYSFILENA");
				if (tzAttAUrl != null && !"".equals(tzAttAUrl) && sysImgName != null && !"".equals(sysImgName)) {
					if (tzAttAUrl.lastIndexOf("/") + 1 == tzAttAUrl.length()) {
						titleImageUrl = tzAttAUrl + sysImgName;
					} else {
						titleImageUrl = tzAttAUrl + "/" + sysImgName;
					}
					Image image = null;
					try {
						titleImageUrl = request.getServletContext().getRealPath(titleImageUrl);
						image = Image.getInstance(titleImageUrl);
					} catch (FileNotFoundException e) {
						titleImageUrl = "/statics/css/website/m/images/defaultPhone.png";
						titleImageUrl = request.getServletContext().getRealPath(titleImageUrl);
						image = Image.getInstance(titleImageUrl);
					}
					// image.scaleToFit(93, 120);
					// image.scaleAbsolute(75, 92);
					image.scaleAbsolute(71, 81);
					PdfContentByte content = null;
					int pageCount = reader.getNumberOfPages();// 获取PDF页数
					System.out.println("pageCount=" + pageCount);
					content = stamper.getOverContent(pageCount);
					// image.setAbsolutePosition(335, 380);
					image.setAbsolutePosition(339, 385);
					content.addImage(image);
					// 添加图片end
				}
			}

			stamper.close();
			Document doc = new Document();
			PdfCopy copy = new PdfCopy(doc, out);
			doc.open();
			PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
			copy.addPage(importPage);
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return filepathandName;
	}

	/**
	 * 获取pdf模板需要的数据
	 * @param dataMap
	 * @param classID
	 * @param batchID
	 * @param instanceID
	 * @param pdfmbID
	 */
	private void getDatas(Map<String, Object> dataMap, String classID, String batchID, String instanceID, String pdfmbID) {
		// -------- 动态获取数据-------------
		// 通过打印模板ID得到导入数据模板ID
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String sqlSelectTplID = "SELECT TZ_DYMB_DRMB_ID FROM PS_TZ_DYMB_T WHERE TZ_JG_ID=? AND TZ_DYMB_ID=?";
		Map<String, Object> mapDataSjmbID = jdbcTemplate.queryForMap(sqlSelectTplID, new Object[] { orgid, pdfmbID });
		String strtTplID = mapDataSjmbID.get("TZ_DYMB_DRMB_ID") == null ? "" : mapDataSjmbID.get("TZ_DYMB_DRMB_ID").toString();
		// 通过导入数据模板ID得到导入表
		String sqlSelectSjb = "SELECT TZ_TARGET_TBL FROM TZ_IMP_TPL_DFN_T WHERE TZ_TPL_ID=?";
		Map<String, Object> mapDataSjb = jdbcTemplate.queryForMap(sqlSelectSjb, new Object[] { strtTplID });
		String strSjb = mapDataSjb.get("TZ_TARGET_TBL") == null ? "" : mapDataSjb.get("TZ_TARGET_TBL").toString();
		// 通过pdf打印映射关系表得到打印模板字段
		String sqlSelectZd = "SELECT GROUP_CONCAT(TZ_DYMB_FIELD_ID) TZ_DYMB_FIELD_ID FROM PS_TZ_DYMB_YS_T WHERE TZ_DYMB_FIELD_QY=1 AND TZ_JG_ID=? AND TZ_DYMB_ID=?";
		Map<String, Object> mapDataZd = jdbcTemplate.queryForMap(sqlSelectZd, new Object[] { orgid, pdfmbID });
		String strZd = mapDataZd.get("TZ_DYMB_FIELD_ID") == null ? "" : mapDataZd.get("TZ_DYMB_FIELD_ID").toString();
		// 判断得到的字段是否是自己新加的，如果是新加的单独处理，否则统一处理
		String[] splits = strZd.split(",");
		for (int i = 0; i < splits.length; i++) {
			String field = splits[i];
			String sqlSelectPdfZd = "SELECT TZ_DYMB_FIELD_PDF FROM PS_TZ_DYMB_YS_T WHERE TZ_DYMB_FIELD_QY=1 AND TZ_JG_ID=? AND TZ_DYMB_ID=? AND TZ_DYMB_FIELD_ID=?";
			Map<String, Object> mapDataPdfZd = jdbcTemplate.queryForMap(sqlSelectPdfZd, new Object[] { orgid, pdfmbID, field });
			String strPdfZd = mapDataPdfZd.get("TZ_DYMB_FIELD_PDF") == null ? "" : mapDataPdfZd.get("TZ_DYMB_FIELD_PDF").toString();
			switch (field) {
			case "TZ_KSXM":// 考生姓名
			case "TZ_KSXB":// 考生性别
			case "TZ_SFZH":// 身份证号
				// 姓名、性别
				String sqlSelectOprid = "SELECT ROW_ADDED_OPRID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
				Map<String, Object> mapDataOprid = jdbcTemplate.queryForMap(sqlSelectOprid, new Object[] { instanceID });
				String strOPRID = mapDataOprid.get("ROW_ADDED_OPRID") == null ? "" : mapDataOprid.get("ROW_ADDED_OPRID").toString();

				String sqlSelectName = "SELECT TZ_REALNAME,(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL X WHERE X.TZ_ZHZJH_ID='TZ_GENDER' AND X.TZ_ZHZ_ID=A.TZ_GENDER)TZ_GENDER, NATIONAL_ID FROM PS_TZ_REG_USER_T A WHERE OPRID=?";
				Map<String, Object> mapDataName = jdbcTemplate.queryForMap(sqlSelectName, new Object[] { strOPRID });
				if ("TZ_KSXM".equals(field) && mapDataName != null) {
					String strName = mapDataName.get("TZ_REALNAME") == null ? "" : mapDataName.get("TZ_REALNAME").toString();
					dataMap.put(strPdfZd, strName);
				} else if ("TZ_KSXB".equals(field) && mapDataName != null) {
					String strSex = mapDataName.get("TZ_GENDER") == null ? "" : mapDataName.get("TZ_GENDER").toString();
					dataMap.put(strPdfZd, strSex);
				}else if ("TZ_SFZH".equals(field) && mapDataName != null) {
					String strSfz = mapDataName.get("NATIONAL_ID") == null ? "" : mapDataName.get("NATIONAL_ID").toString();
					dataMap.put(strPdfZd, strSfz);
				}
				break;
			case "TZ_KSBH0":// 考生编号后四位
				String sqlSelectKsbh = "SELECT TZ_STU_NUM_LAST4 FROM TZ_IMP_LKBM_TBL WHERE TZ_APP_INS_ID=?";
				Map<String, Object> mapDataKsbh = jdbcTemplate.queryForMap(sqlSelectKsbh, new Object[] { instanceID });
				if (mapDataKsbh != null) {
					String strKsbh = mapDataKsbh.get("TZ_STU_NUM_LAST4") == null ? "" : mapDataKsbh.get("TZ_STU_NUM_LAST4").toString();
					dataMap.put(strPdfZd, strKsbh);
				}
				break;
			case "TZ_ZKZ_HEAD":// 入学年份+考试名称
			case "TZ_BKXM":// 报考项目（取班级名称）
				// 入学年份、班级名称
				String sqlSelectDate = "SELECT TZ_CLASS_NAME, TZ_RX_DT FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
				Map<String, Object> mapDataDate = jdbcTemplate.queryForMap(sqlSelectDate, new Object[] { classID });
				// 考试名称
				String sqlSelectKsmc = "SELECT TZ_KSMC FROM TZ_IMP_LKBM_TBL WHERE TZ_APP_INS_ID=?";
				Map<String, Object> mapDataKsmc = jdbcTemplate.queryForMap(sqlSelectKsmc, new Object[] { instanceID });
				if ("TZ_ZKZ_HEAD".equals(field)) {
					String strDateYear = "";
					String strKsmc = "";
					if(mapDataDate != null){
						strDateYear  = mapDataDate.get("TZ_RX_DT") == null ? "" : mapDataDate.get("TZ_RX_DT").toString();
						String[] split = strDateYear.split("-");
						strDateYear = split[0];
					}
					if(mapDataKsmc != null){
						strKsmc = mapDataKsmc.get("TZ_KSMC") == null ? "" : mapDataKsmc.get("TZ_KSMC").toString();
					}
					StringBuilder sb = new StringBuilder();
					sb.append(strDateYear).append("年入学清华 MBA ").append(strKsmc);
					dataMap.put(strPdfZd, sb.toString());
				} else if ("TZ_BKXM".equals(field) && mapDataDate != null) {
					String strDateClass = mapDataDate.get("TZ_CLASS_NAME") == null ? "" : mapDataDate.get("TZ_CLASS_NAME").toString();
					dataMap.put(strPdfZd, strDateClass);
				}
				break;

			default:
				// 导入表中的字段
				StringBuffer sqlTable = null;
				sqlTable = new StringBuffer("SELECT ");
				sqlTable.append(field).append(" FROM ").append(strSjb).append(" ");
				String sqlSelectDrZd = sqlTable + "WHERE TZ_APP_INS_ID=? ";
				Map<String, Object> mapDataDrZd = jdbcTemplate.queryForMap(sqlSelectDrZd, new Object[] { instanceID });
				if (mapDataDrZd != null) {
					String strDrZd = mapDataDrZd.get(field) == null ? "" : mapDataDrZd.get(field).toString();
					dataMap.put(strPdfZd, strDrZd);
				}
				break;
			}
		}

	}
}
