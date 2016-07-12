package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年6月14日 下午3:48:10 报名表配置 PDF打印模板设置
 *          PS:TZ_ONLINE_REG_COM:TZ_ONREG_PDF_STD
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormPdfClsServiceImpl")
public class AppFormPdfClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private GetSeqNum getSeqNum;

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		return null;
	}

	/* 配置 PDF打印模板设置 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		List<Object[]> sqlparm = new ArrayList<Object[]>();
		String sql = "";
		try {

			int dataLength = actData.length;
			String tplID = "";
			String fieldID = "";
			String fieldName = "";
			String pdffield1 = "";
			String pdffield2 = "";
			String pdffield3 = "";
			String strForm = "";
			String typeFlag = "";

			String tpPdfStatus = "";
			// 模板PDF文件名
			String fileName = "";
			// 模板PDF文件路径
			String filePath = "";
			Map<String, Object> mapData = null;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				strForm = actData[num];
				//// System.out.println("strForm：" + strForm);
				// 解析json
				jacksonUtil.json2Map(strForm);
				typeFlag = jacksonUtil.getString("typeFlag");
				mapData = jacksonUtil.getMap("data");

				if ("FIELD".equals(typeFlag)) {
					tplID = String.valueOf(mapData.get("tplID"));
					fieldID = String.valueOf(mapData.get("fieldID"));
					fieldName = String.valueOf(mapData.get("fieldName"));
					pdffield1 = String.valueOf(mapData.get("pdffield1"));
					// //System.out.println("pdffield1:" + pdffield1);
					if (pdffield1 == null || pdffield1.equals("null")) {
						pdffield1 = "";
					}
					pdffield2 = String.valueOf(mapData.get("pdffield2"));
					if (pdffield2 == null || pdffield2.equals("null")) {
						pdffield2 = "";
					}
					pdffield3 = String.valueOf(mapData.get("pdffield3"));
					if (pdffield3 == null || pdffield3.equals("null")) {
						pdffield3 = "";
					}
					// //System.out.println("pdffield1："+pdffield1);
					// //System.out.println("pdffield2："+pdffield2);
					// //System.out.println("pdffield3："+pdffield3);
					sqlparm.add(new Object[] { tplID, fieldID, fieldName, pdffield1, pdffield2, pdffield3 });
				} else if ("TPL".equals(typeFlag)) {
					fileName = String.valueOf(mapData.get("fileName"));
					tplID = String.valueOf(mapData.get("tplID"));
					tpPdfStatus = String.valueOf(mapData.get("tpPdfStatus"));
					filePath = String.valueOf(mapData.get("filePath"));

				}
			}

			// System.out.println("tpPdfStatus：" + tpPdfStatus);
			// 删除模板字段原来对应报名表信息表
			Object[] args = new Object[] { tplID };
			Object[] insertargs = null;
			sql = "DELETE FROM PS_TZ_APP_PDFFIELD_T WHERE TZ_APP_TPL_ID = ? ";
			// //System.out.println("sql：" + sql);
			jdbcTemplate.update(sql, args);

			args = new Object[] { tplID, filePath, fileName, tpPdfStatus };
			sql = "INSERT INTO PS_TZ_APP_PDFFIELD_T (TZ_APP_TPL_ID,TZ_FIELD_PATH,TZ_FIELD_NAME,TZ_FIELD_STATUS) VALUES (?,?,?,?)";
			// System.out.println("sql：" + sql);
			jdbcTemplate.update(sql, args);

			// 删除模板字段原来对应报名表信息表
			args = new Object[] { tplID };
			sql = "DELETE FROM PS_TZ_APP_PDFFIELDITEM_T WHERE TZ_APP_TPL_ID = ? ";
			// //System.out.println("sql：" + sql);
			jdbcTemplate.update(sql, args);

			sql = "INSERT INTO PS_TZ_APP_PDFFIELDITEM_T (TZ_APP_PDFF_ID,TZ_APP_TPL_ID,TZ_XXX_BH,TZ_XXX_MC,TZ_APP_PDF_FIELD) VALUES (?,?,?,?,?)";
			// //System.out.println("size：" + sqlparm.size());
			String newTplId = "";
			String pdffile = "";
			for (int i = 0; i < sqlparm.size(); i++) {

				args = sqlparm.get(i);
				if ((String) args[3] == null || ((String) args[3]).equals("")) {
					pdffile = "";
				} else {
					newTplId = "" + getSeqNum.getSeqNum("PS_TZ_APP_PDFFIELDITEM_T", "TZ_APP_PDFF_ID");
					pdffile = (String) args[3];
					insertargs = new Object[] { newTplId, (String) args[0], (String) args[1], (String) args[2],
							pdffile };
					jdbcTemplate.update(sql, insertargs);
				}

				if ((String) args[4] == null || ((String) args[4]).equals("")) {
					pdffile = "";
				} else {
					newTplId = "" + getSeqNum.getSeqNum("PS_TZ_APP_PDFFIELDITEM_T", "TZ_APP_PDFF_ID");
					pdffile = (String) args[4];
					insertargs = new Object[] { newTplId, (String) args[0], (String) args[1], (String) args[2],
							pdffile };
					jdbcTemplate.update(sql, insertargs);
				}

				if ((String) args[5] == null || ((String) args[5]).equals("")) {
					pdffile = "";
				} else {
					newTplId = "" + getSeqNum.getSeqNum("PS_TZ_APP_PDFFIELDITEM_T", "TZ_APP_PDFF_ID");
					pdffile = (String) args[5];
					insertargs = new Object[] { newTplId, (String) args[0], (String) args[1], (String) args[2],
							(String) pdffile };
					jdbcTemplate.update(sql, insertargs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 获取报名表模板信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("tplID")) {
				String strtplID = jacksonUtil.getString("tplID");
				if (strtplID != null && !"".equals(strtplID)) {
					String jgName = "";
					String tplName = "";
					String filePath = "";
					String fileName = "";
					String status = "";
					String pageSql = "select a.TZ_JG_NAME,b.TZ_APP_TPL_MC from PS_TZ_JG_BASE_T a,PS_TZ_APPTPL_DY_T b where a.TZ_JG_ID = b.TZ_JG_ID and b.TZ_APP_TPL_ID= ?";
					try {
						// 加载模板ID 模板名称 以及 机构名称
						Map<String, Object> map = jdbcTemplate.queryForMap(pageSql, new Object[] { strtplID });
						if (map != null) {
							jgName = map.get("TZ_JG_NAME") == null ? "" : String.valueOf(map.get("TZ_JG_NAME"));
							tplName = map.get("TZ_APP_TPL_MC") == null ? "" : String.valueOf(map.get("TZ_APP_TPL_MC"));
						}

						// 加载模板PDF 文件路径
						pageSql = "select TZ_FIELD_PATH,TZ_FIELD_NAME,TZ_FIELD_STATUS from PS_TZ_APP_PDFFIELD_T  where TZ_APP_TPL_ID= ?";
						map = jdbcTemplate.queryForMap(pageSql, new Object[] { strtplID });
						if (map != null) {
							filePath = map.get("TZ_FIELD_PATH") == null ? "" : String.valueOf(map.get("TZ_FIELD_PATH"));
							fileName = map.get("TZ_FIELD_NAME") == null ? "" : String.valueOf(map.get("TZ_FIELD_NAME"));
							status = map.get("TZ_FIELD_STATUS") == null ? ""
									: String.valueOf(map.get("TZ_FIELD_STATUS"));
						}

						Map<String, Object> mapData = new HashMap<String, Object>();
						mapData.put("jgName", jgName);
						mapData.put("tplName", tplName);
						mapData.put("tplID", strtplID);
						mapData.put("filePath", filePath);
						mapData.put("fileName", fileName);
						mapData.put("downfileName", fileName);
						mapData.put("tpPdfStatus", status);
						if (status == null || status.trim().equals("")) {
							mapData.put("tpPdfStatus", "A");
						}
						if (fileName == null || fileName.trim().equals("")) {
							mapData.put("flag", "Y");
						} else {
							mapData.put("flag", "N");
						}

						returnJsonMap.put("formData", mapData);

						strRet = jacksonUtil.Map2json(returnJsonMap);
					} catch (Exception e) {
						errMsg[0] = "1";
						errMsg[1] = "无法获取组件信息";
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取组件信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 获取报名明PDF打印模板配置表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);

		String tplID = String.valueOf(jacksonUtil.getString("tplID"));

		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try {
			int total = 0;
			// 总数 自己计算
			String sql = "SELECT TZ_XXX_BH,TZ_XXX_MC ,TZ_APP_PDF_FIELD FROM PS_TZ_APP_PDFFIELDITEM_T  WHERE TZ_APP_TPL_ID= ? ";
			List<?> listData = jdbcTemplate.queryForList(sql, new Object[] { tplID });
			String TZ_XXX_BH = "";
			// String TZ_XXX_SLID = "";
			String TZ_XXX_MC = "";
			String TZ_APP_PDF_FIELD = "";

			Hashtable<String, Map<String, Object>> ht = new Hashtable<String, Map<String, Object>>();
			Map<String, Object> mapData = null;
			Map<String, Object> mapTemp = null;

			for (Object objData : listData) {

				mapData = (Map<String, Object>) objData;
				TZ_XXX_BH = String.valueOf(mapData.get("TZ_XXX_BH"));
				// TZ_XXX_SLID = String.valueOf(mapData.get("TZ_XXX_SLID"));
				TZ_XXX_MC = String.valueOf(mapData.get("TZ_XXX_MC"));
				TZ_APP_PDF_FIELD = String.valueOf(mapData.get("TZ_APP_PDF_FIELD"));
				if (TZ_APP_PDF_FIELD == null) {
					TZ_APP_PDF_FIELD = "";
				}

				if (ht.get(TZ_XXX_BH) == null) {
					mapTemp = new HashMap<String, Object>();
					mapTemp.put("tplID", tplID);
					mapTemp.put("fieldID", TZ_XXX_BH);
					mapTemp.put("fieldName", TZ_XXX_MC);
					mapTemp.put("pdffield1", TZ_APP_PDF_FIELD);
					ht.put(TZ_XXX_BH, mapTemp);
				} else {
					if ((ht.get(TZ_XXX_BH)).get("pdffield2") == null) {
						(ht.get(TZ_XXX_BH)).put("pdffield2", TZ_APP_PDF_FIELD);
					} else if ((ht.get(TZ_XXX_BH)).get("pdffield3") == null) {
						(ht.get(TZ_XXX_BH)).put("pdffield3", TZ_APP_PDF_FIELD);
					}
				}

			}
			total = ht.size();
			Enumeration<Map<String, Object>> en2 = ht.elements();
			while (en2.hasMoreElements()) {
				listJson.add(en2.nextElement());
			}
			mapRet.replace("total", total);
			mapRet.replace("root", listJson);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 其他相关函数 loadAppFormFields:加载报名表模板字段
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzOther(String oprType, String strParams, String[] errMsg) {
		// 返回值;
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		JacksonUtil jacksonUtil = new JacksonUtil();

		// //System.out.println("oprType：" + oprType);

		// //System.out.println("strParams：" + strParams);
		try {
			// 加载报名表模板字段
			if ("loadAppFormFields".equals(oprType)) {
				jacksonUtil.json2Map(strParams);
				// 报名表导出模版编号;
				String tplID = jacksonUtil.getString("tplID");
				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				String strXxxBh = "";
				String strXxxMc = "";
				String sql = "SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID= ? AND TZ_XXX_CCLX IN ('D','S','L','R')";
				List<?> listData = jdbcTemplate.queryForList(sql, new Object[] { tplID });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;

					strXxxBh = String.valueOf(mapData.get("TZ_XXX_BH"));
					strXxxMc = String.valueOf(mapData.get("TZ_XXX_MC"));
					//
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("tplID", tplID);
					mapJson.put("fieldID", strXxxBh);
					mapJson.put("fieldName", strXxxMc);
					mapJson.put("pdffield1", "");
					mapJson.put("pdffield2", "");
					mapJson.put("pdffield3", "");
					listJson.add(mapJson);
				}

				String sqlAppIns = "SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_FORM_FIELD_V WHERE TZ_APP_TPL_ID= ? AND TZ_XXX_CCLX = 'R'";
				List<?> listDataAppIns = jdbcTemplate.queryForList(sqlAppIns, new Object[] { tplID });
				for (Object objData : listDataAppIns) {

					Map<String, Object> mapData = (Map<String, Object>) objData;

					strXxxBh = String.valueOf(mapData.get("TZ_XXX_BH"));
					strXxxMc = String.valueOf(mapData.get("TZ_XXX_MC"));
					//
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("tplID", tplID);
					mapJson.put("fieldID", strXxxBh);
					mapJson.put("fieldName", strXxxMc);
					mapJson.put("pdffield1", "");
					mapJson.put("pdffield2", "");
					mapJson.put("pdffield3", "");
					listJson.add(mapJson);
				}
				mapRet.replace("root", listJson);
			}
			// 加载PDF模板信息项
			else if ("loadAppFormPdf".equals(oprType)) {
				jacksonUtil.json2Map(strParams);
				// 报名表导出模版编号;
				String tplID = jacksonUtil.getString("tplID");
				String filePath = jacksonUtil.getString("filePath");
				// String storDate = jacksonUtil.getString("storDate");
				Resource resource = new ClassPathResource("conf/cookieSession.properties");
				Properties cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
				String webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");

				filePath = System.getProperty(webAppRootKey) + filePath;
				// //System.out.println("tplID：" + tplID);

				// //System.out.println("fileName：" + fileName);
				// //System.out.println("storDate：" + storDate);

				String temp = null;

				String[] strActData = null;
				Map<String, Object> mapData = null;
				List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("storDates");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

				// //System.out.println("jsonArray：" + jsonArray.size());

				String fieldID = ""; // 信息项编号
				String fieldName = "";// 信息项名称
				if (jsonArray != null && jsonArray.size() > 0) {
					strActData = new String[jsonArray.size()];
					for (int num = 0; num < jsonArray.size(); num++) {
						strActData[num] = jacksonUtil.Map2json(jsonArray.get(num));
						// //System.out.println("strActData：" +
						// strActData[num]);
					}

					// 解析加载PDF模板信息项
					String[] fields = this.split(this.getPdfFileFields(filePath), ";");
					Map<String, Object> mapJson = null;
					// 合并
					if (fields != null && fields.length > 0) {
						for (int i = 0; i < strActData.length; i++) {
							jacksonUtil.json2Map(strActData[i]);

							mapData = jacksonUtil.getMap("data");
							fieldID = String.valueOf(mapData.get("fieldID"));
							fieldName = String.valueOf(mapData.get("fieldName"));
							// //System.out.println("fieldID：" + fieldID);
							mapJson = new HashMap<String, Object>();
							mapJson.put("tplID", tplID);
							mapJson.put("fieldID", fieldID);
							mapJson.put("fieldName", fieldName);

							int index = 0;
							for (int j = 0; j < fields.length; j++) {
								temp = this.getTZ_XXX_BH(fields[j]);
								//System.out.println(fields[j] + "---->" + temp + "<-----" + fieldID);
								if (temp.equals(fieldID)) {
									index = index + 1;
									if (index == 1) {
										mapJson.put("pdffield1", fields[j]);
									} else if (index == 2) {
										mapJson.put("pdffield2", fields[j]);
									} else if (index == 3) {
										mapJson.put("pdffield3", fields[j]);
									} else {
										break;
									}
								}
							}
							if (mapJson.get("pdffield1") != null || mapJson.get("pdffield2") != null
									|| mapJson.get("pdffield3") != null) {
								listJson.add(mapJson);
							}
						}
					}
				}
				mapRet.replace("root", listJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}

	private String getTZ_XXX_BH(String str) {
		// 表单1[0].#subform[4].TZ_1124[0]

		str = str.substring(str.lastIndexOf(".") + 1, str.length());
		if (str.indexOf("[") != -1) {
			str = str.substring(0, str.indexOf("["));
		}
		return str;
	}

	private String[] split(String str, String sep) {
		int size = 0;
		Vector<String> v = new Vector<String>();
		int pos = -1;
		while (!str.equals("")) {
			pos = str.indexOf(sep);
			if (pos != -1) {
				v.add(str.substring(0, pos));
				str = str.substring(pos + sep.length());
				if (str.equalsIgnoreCase("")) {
					v.add("");
				}
			} else {
				v.add(str);
				str = "";
			}
		}

		size = v.size();
		String[] array = new String[size];
		for (int i = 0; i < size; i++) {
			array[i] = v.elementAt(i).toString();
		}
		return array;
	}

	private String getPdfFileFields(String file) {
		String fields = "";
		PdfReader reader = null;
		ByteArrayOutputStream bos = null;
		PdfStamper ps = null;
		AcroFields s = null;
		try {
			reader = new PdfReader(file);
			bos = new ByteArrayOutputStream();
			ps = new PdfStamper(reader, bos);
			s = ps.getAcroFields();
			int i = 1;
			for (Iterator it = s.getFields().keySet().iterator(); it.hasNext(); ++i) {
				String name = (String) it.next();
				// //System.out.println("name=" + name);
				fields = fields + name + ";";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				bos.close();
				reader.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// //System.out.println("fields：" + fields);
		return fields;
	}

	/* 删除 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		// String strRet = "{}";
		boolean success = false;
		JacksonUtil jacksonUtil = new JacksonUtil();
		String sql = "";
		try {

			int dataLength = actData.length;
			String tplID = "";
			String strForm = "";

			Map<String, Object> mapData = null;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				strForm = actData[num];
				// System.out.println("strForm：" + strForm);
				// 解析json
				jacksonUtil.json2Map(strForm);
				tplID = jacksonUtil.getString("TplID");
			}

			// 加载模板PDF 文件路径
			sql = "select TZ_FIELD_PATH from PS_TZ_APP_PDFFIELD_T  where TZ_APP_TPL_ID= ?";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { tplID });
			String filePath = "";
			if (map != null) {
				filePath = map.get("TZ_FIELD_PATH") == null ? "" : String.valueOf(map.get("TZ_FIELD_PATH"));
			}
			// 删除文件
			this.DeleteFile(filePath);

			// 删除模板字段原来对应报名表信息表
			Object[] args = new Object[] { tplID };
			sql = "DELETE FROM PS_TZ_APP_PDFFIELD_T WHERE TZ_APP_TPL_ID = ? ";
			jdbcTemplate.update(sql, args);

			// 删除模板字段原来对应报名表信息表
			args = new Object[] { tplID };
			sql = "DELETE FROM PS_TZ_APP_PDFFIELDITEM_T WHERE TZ_APP_TPL_ID = ? ";
			jdbcTemplate.update(sql, args);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", success);
		return jacksonUtil.Map2json(mapRet);

	}

	private boolean DeleteFile(String parentPath) {
		String parentRealPath = this.getRealPath(parentPath);

		File serverFile = new File(parentRealPath);

		if (!serverFile.exists()) {
			return true;
		}

		if (serverFile.isFile()) {
			if (serverFile.delete()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取给定文件夹、文件的绝对路径；
	 * 
	 * @param path
	 * @return
	 */
	private String getRealPath(String path) {
		// 服务器路径
		String webAppRootKey = "";
		try {
			Resource resource = new ClassPathResource("conf/cookieSession.properties");
			Properties cookieSessioinProps = null;
			cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
			webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
		} catch (IOException e) {

			e.printStackTrace();
		}
		return webAppRootKey + path;
	}

}
