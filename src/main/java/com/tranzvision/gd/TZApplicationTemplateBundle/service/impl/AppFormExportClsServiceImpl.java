package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcTKey;
import com.tranzvision.gd.util.ExecuteShell.ExecuteShellComand;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author WRL 报名表实例导出
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormExportClsServiceImpl")
public class AppFormExportClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;

	@Autowired
	private PsTzAppCcTMapper psTzAppCcTMapper;

	@Autowired
	private TZGDObject tzGdObject;

	Map<String, String> comMap = new HashMap<String, String>();


	@Override
	public String tzGetJsonData(String strParams) {
		String code = "0";
		String msg = "";
		
		String url = "";
		String title = "";
		String insid = "";
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		/*用户是否属于某个机构*/
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if (StringUtils.isBlank(orgId)) {
			code = "1";
			msg = "您当前没有机构，不能参与报名表打印！";
		}
		/*报名表实例是否存在*/
		if(StringUtils.equals("0", code)){
			jacksonUtil.json2Map(strParams);
			insid = jacksonUtil.getString("insid");
			
			if (StringUtils.isBlank(insid)) {
				code = "1";
				msg = "参数-报名表实例编号为空！";
			}
		}
		/*报名表是否进行了报名表导出设置*/
		if(StringUtils.equals("0", code)){
			String sqlOprid = "SELECT OPRID,TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? ORDER BY OPRID LIMIT 1";

			Map<String, Object> workMap = sqlQuery.queryForMap(sqlOprid, new Object[] { insid });
			String oprid = workMap.get("OPRID") == null ? "" : String.valueOf(workMap.get("OPRID"));
			String classid = workMap.get("TZ_CLASS_ID") == null ? "" : String.valueOf(workMap.get("TZ_CLASS_ID"));
			
			String classSql = "SELECT TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
			String className = sqlQuery.queryForObject(classSql, new Object[] { classid }, "String");
			
			
			String sqlRealName = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?";
			String userName = sqlQuery.queryForObject(sqlRealName, new Object[] { oprid }, "String");
			
			//报名表模板编号、模板名称 
			String sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ? LIMIT 0,1";
			String tplid =  sqlQuery.queryForObject(sql, new Object[] { insid },"String");
			
			//title
			title = className + "-" + userName;
			
			//最终生成文件路径
			String parentPath = "/bmb/export/" + orgId + "/" + tplid + "/";
			String header = request.getServletContext().getRealPath(parentPath) + "header.html";
			String footer = request.getServletContext().getRealPath(parentPath) + "footer.html";
			
			File headerFile = new File(header);
			File footerFile = new File(footer);
			if (!headerFile.exists() || !footerFile.exists()) {
				code = "1";
				msg = "对应的报名表未进行报名表导出设置！";
			}else{
				url = this.createPdf(tplid,insid,title,"");
				if(StringUtils.isBlank(url)){
					code = "1";
					msg = "导出PDF报名表失败！";
				}
			}
		}

		String filename = title + ".pdf";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("code", code);
		mapRet.put("msg", msg);
		mapRet.put("url", url);
		mapRet.put("filename", filename);
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	/**
	 * 创建报名表实例的PDF格式文件
	 * 
	 * @param tplid
	 * @param insid
	 * @param title
	 * @param filename
	 * @return
	 */
	private String createPdf(String tplid,String insid,String title,String filename) {
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		//最终生成文件路径
		String parentPath = "/bmb/export/" + orgid + "/" + tplid + "/";
		String path = parentPath + insid + "/";

		//HTML格式报名表字符串
		String formHtml = this.CreateHtml(insid, tplid, title);
		
		try {
			String htmlFileName = tplid + "_" + insid + ".html";
			
			fileManageServiceImpl.DeleteFile(path, htmlFileName);
			boolean isSuccess = fileManageServiceImpl.CreateFile(path, htmlFileName, formHtml.getBytes());
			if(isSuccess){
				String pdfFileName = tplid + "_" + insid + ".pdf";
				fileManageServiceImpl.DeleteFile(path, pdfFileName);
				
				String wkh = getSysHardCodeVal.getWkHtml2Pdf();
				
				String[] command = new String[7];
				command[0] = wkh;
				command[1] = "--header-html";
				command[2] = request.getServletContext().getRealPath(parentPath) + "header.html";
				command[3] = "--footer-html";
				command[4] = request.getServletContext().getRealPath(parentPath) + "footer.html";
				command[5] = request.getServletContext().getRealPath(path) + htmlFileName;
				if(StringUtils.isBlank(filename)){
					command[6] = request.getServletContext().getRealPath(path) + pdfFileName;
				}else{
					String pdfPath = filename.substring(0,filename.lastIndexOf("/"));
					pdfPath = request.getServletContext().getRealPath(pdfPath);
					File dir = new File(pdfPath);
					// System.out.println(dir.getAbsolutePath());
					if (!dir.exists()) {
						dir.mkdirs();
					}
					command[6] = request.getServletContext().getRealPath(filename);
				}
				
				ExecuteShellComand shellComand = new ExecuteShellComand();
				String retval = shellComand.executeCommand(command);
//				System.out.println(" create PDF Logs:    " + retval);
				if(retval.toUpperCase().trim().endsWith("DONE")){
					return path + pdfFileName;
				}else{
					return "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将报名表转换成HTML字符串
	 * 
	 * @param insid	报名表实例编号
	 * @param tplid	报名表模板编号
	 * @param title	HTML文件的Title
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String CreateHtml(String insid, String tplid,String title) {

		String sql = "SELECT TZ_XXX_BH,TZ_XXX_NO,TZ_XXX_MC,TZ_COM_LMC,TZ_XXX_CCLX,TZ_XXX_DRQ_BZ,TZ_XXX_SRQ_BZ FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = '' AND TZ_IS_DOWNLOAD <> 'N' AND TZ_COM_LMC NOT IN ('Separator') ORDER BY TZ_ORDER,TZ_LINE_NUM,TZ_LINE_ORDER";
		List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplid });
		StringBuffer html = new StringBuffer("");

		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;

			String xxxBh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
			String xxxNo = result.get("TZ_XXX_NO") == null ? "" : String.valueOf(result.get("TZ_XXX_NO"));
			String xxxMc = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
			String xxxLmc = result.get("TZ_COM_LMC") == null ? "" : String.valueOf(result.get("TZ_COM_LMC"));
			String xxxCclx = result.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(result.get("TZ_XXX_CCLX"));
			String drqFlag = result.get("TZ_XXX_DRQ_BZ") == null ? "" : String.valueOf(result.get("TZ_XXX_DRQ_BZ"));
			String srqFlag = result.get("TZ_XXX_SRQ_BZ") == null ? "" : String.valueOf(result.get("TZ_XXX_SRQ_BZ"));

			if (StringUtils.equals("Y", drqFlag)) {
				//多行容器
				String dhContent = this.getDHContent(insid, tplid, xxxBh, xxxMc, xxxLmc);
				html.append(dhContent);
			} else if (StringUtils.equals("Y", srqFlag)) {
				//组合框类型
				String singleContent = this.getSingleContent(insid, tplid, xxxBh, xxxNo, xxxMc, xxxLmc, xxxCclx);
				html.append(singleContent);
			} else {
				//简单类型
				String content = this.getContent(insid, tplid, xxxBh, xxxNo, xxxMc, xxxLmc, xxxCclx);
				html.append(content);
			}
		}

		String formHtml = "";
		try {
			String htm = html.toString();
			htm = htm.replace("$", "\\$");
			formHtml = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_APP_FORM_EXPORT_HTML",request.getContextPath(), title, htm,insid);
		} catch (TzSystemException e) {
			e.printStackTrace();
			formHtml = "";
		}
		formHtml = formHtml.replaceAll("/university/", "/");
		formHtml = formHtml.replaceAll("/statics/", "../../../../../statics/");
		return formHtml;
	}

	/**
	 * 获取简单控件的HTML代码
	 * 
	 * @param insid		报名表实例编号
	 * @param tplid		报名表模板编号
	 * @param xxxBh		信息项编号(主要用于取值,例如，多行容器编号 + 信息项编号、信息项编号)
	 * @param xxxNo		信息项编号(主要用于取描述信息)
	 * @param xxxMc		信息项名称
	 * @param xxxLmc	信息项类名称
	 * @param xxxCclx	信息项存储类型
	 * @param i			序号
	 * @return			简单控件对应的HTML源码
	 */
	private String getContent(String insid, String tplid, String xxxBh, String xxxNo, String xxxMc, String xxxLmc, String xxxCclx) {
		//控件的源HTML
		String comHtml = "";

		if (!comMap.containsKey(xxxLmc)) {
			String sql = "SELECT TZ_COM_EXPHTML FROM PS_TZ_COM_DY_T WHERE TZ_COM_JSLJ REGEXP BINARY  '" + xxxLmc + ".js$' limit 0,1";
			comHtml = sqlQuery.queryForObject(sql, new Object[] {}, "String");
			comMap.put(xxxLmc, comHtml);
		} else {
			comHtml = comMap.get(xxxLmc);
		}
		
		String val = this.getXxxVal(insid, tplid, xxxBh, xxxNo, xxxMc, xxxLmc, xxxCclx);

		try {
			if (StringUtils.isNotBlank(comHtml)) {
				comHtml = tzGdObject.getText(comHtml, xxxMc, val);
			} else {
				comHtml = "";
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
			comHtml = "";
		}
		return comHtml;
	}

		 
	/**
	 * 获取组合控件的HTML源码
	 * 
	 * @param insid		报名表实例编号
	 * @param tplid		报名表模板编号
	 * @param xxxBh		信息项编号(主要用于取值,例如，多行容器编号 + 信息项编号、信息项编号)
	 * @param xxxNo		信息项编号(主要用于取描述信息)
	 * @param xxxMc		信息项名称
	 * @param xxxLmc	信息项类名称
	 * @param xxxCclx	信息项存储类型
	 * @param i			序号
	 * @return			组合控件的HTML源码
	 */
	@SuppressWarnings("unchecked")
	private String getSingleContent(String insid, String tplid, String xxxBh, String xxxNo, String xxxMc, String xxxLmc, String xxxCclx) {
		//控件的源HTML
		String comHtml = "";
		if (!comMap.containsKey(xxxLmc)) {
			String sql = "SELECT TZ_COM_EXPHTML FROM PS_TZ_COM_DY_T WHERE TZ_COM_JSLJ REGEXP BINARY  '" + xxxLmc + ".js$' limit 0,1";
			comHtml = sqlQuery.queryForObject(sql, new Object[] {}, "String");
			comMap.put(xxxLmc, comHtml);
		} else {
			comHtml = comMap.get(xxxLmc);
		}
//		System.out.println(StringUtils.countMatches(comHtml, "%bind"));
		int length = StringUtils.countMatches(comHtml, "%bind");
		
		String sql = "SELECT TZ_XXX_BH,TZ_XXX_NO,TZ_XXX_MC,TZ_COM_LMC,TZ_XXX_CCLX FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_IS_DOWNLOAD <> 'N' AND TZ_COM_LMC NOT IN ('Separator') ORDER BY TZ_LINE_ORDER";
		List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplid, xxxBh});
		
		String[] values = new String[length];
		values[0] = xxxMc;
		
		int j = 1;
		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;

			String xxxBh1 = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
			String xxxNo1 = result.get("TZ_XXX_NO") == null ? "" : String.valueOf(result.get("TZ_XXX_NO"));
			String xxxMc1 = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
			String xxxLmc1 = result.get("TZ_COM_LMC") == null ? "" : String.valueOf(result.get("TZ_COM_LMC"));
			String xxxCclx1 = result.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(result.get("TZ_XXX_CCLX"));
			
			String value = this.getXxxVal(insid,tplid,xxxBh1,xxxNo1,xxxMc1,xxxLmc1,xxxCclx1);
			values[j] = value;
			j++;
		}

		try {
			if (StringUtils.isNotBlank(comHtml)) {
				comHtml = tzGdObject.getText(comHtml, values);
			} else {
				comHtml = "";
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
			comHtml = "";
		}
		return comHtml;
	}
	/**
	 * 获取组合控件的HTML源码
	 * 
	 * @param insid		报名表实例编号
	 * @param tplid		报名表模板编号
	 * @param xxxBh		信息项编号(主要用于取值,例如，多行容器编号 + 信息项编号、信息项编号)
	 * @param xxxNo		信息项编号(主要用于取描述信息)
	 * @param xxxMc		信息项名称
	 * @param xxxLmc	信息项类名称
	 * @param xxxCclx	信息项存储类型
	 * @param i			序号
	 * @return			组合控件的HTML源码
	 */
	@SuppressWarnings("unchecked")
	private String getSingleContentInDH(String insid, String tplid, String parentXxxBh, String xxxBh, String xxxNo, String xxxMc,
			String xxxLmc, String xxxCclx, int line) {
		//控件的源HTML
		String comHtml = "";
		if (!comMap.containsKey(xxxLmc)) {
			String sql = "SELECT TZ_COM_EXPHTML FROM PS_TZ_COM_DY_T WHERE TZ_COM_JSLJ REGEXP BINARY  '" + xxxLmc + ".js$' limit 0,1";
			comHtml = sqlQuery.queryForObject(sql, new Object[] {}, "String");
			comMap.put(xxxLmc, comHtml);
		} else {
			comHtml = comMap.get(xxxLmc);
		}
//		System.out.println(StringUtils.countMatches(comHtml, "%bind"));
		int length = StringUtils.countMatches(comHtml, "%bind");
		
		
//		String maxLineSql = "SELECT MAX(TZ_LINE_ORDER) FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH IN (SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_SRQ_BZ = 'Y') limit 0,1";
//		int length = sqlQuery.queryForObject(maxLineSql, new Object[] { tplid, tplid}, "Integer");
//		length = length + 2;
		
		String sql = "SELECT TZ_XXX_BH,TZ_XXX_NO,TZ_XXX_MC,TZ_COM_LMC,TZ_XXX_CCLX FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_LINE_NUM = ? AND TZ_XXX_SRQ_BZ = '' AND TZ_XXX_BH REGEXP BINARY ? AND TZ_IS_DOWNLOAD <> 'N' AND TZ_COM_LMC NOT IN ('Separator') ORDER BY TZ_LINE_ORDER";
		List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplid, parentXxxBh, line, "^" + xxxBh});
		
		String[] values = new String[length];
		values[0] = xxxMc;
		
		int j = 1;
		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;

			String xxxBh1 = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
			String xxxNo1 = result.get("TZ_XXX_NO") == null ? "" : String.valueOf(result.get("TZ_XXX_NO"));
			String xxxMc1 = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
			String xxxLmc1 = result.get("TZ_COM_LMC") == null ? "" : String.valueOf(result.get("TZ_COM_LMC"));
			String xxxCclx1 = result.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(result.get("TZ_XXX_CCLX"));
			
			String value = this.getXxxVal(insid,tplid,xxxBh1,xxxNo1,xxxMc1,xxxLmc1,xxxCclx1);
			values[j] = value;
			j++;
		}

		try {
			if (StringUtils.isNotBlank(comHtml)) {
				comHtml = tzGdObject.getText(comHtml, values);
			} else {
				comHtml = "";
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
			comHtml = "";
		}
		return comHtml;
	}
	
	/**
	 * 获取简单信息项的值
	 * 
	 * @param insid		报名表实例编号
	 * @param tplid		报名表模板编号
	 * @param xxxBh		信息项编号(主要用于取值,例如，多行容器编号 + 信息项编号、信息项编号)
	 * @param xxxNo		信息项编号(主要用于取描述信息)
	 * @param xxxMc		信息项名称
	 * @param xxxLmc	信息项类名称
	 * @param xxxCclx	信息项存储类型
	 * @return			信息的值
	 */
	@SuppressWarnings("unchecked")
	private String getXxxVal(String insid, String tplid, String xxxBh, String xxxNo, String xxxMc, String xxxLmc,String xxxCclx) {
		//TODO 对于多行文本
		
		String val = "";
		if (StringUtils.equals(xxxCclx, "D")) {
			// 可选值
			String li = "<li><input type=\"%bind(:1)\" disabled=\"disabled\" %bind(:2)/>&nbsp;&nbsp;<label>%bind(:3)&nbsp;&nbsp;&nbsp;&nbsp;%bind(:4)</label></li>";

			String type = "radio";
			if (StringUtils.equals(xxxLmc, "Check")) {
				type = "checkbox";
			}
			
//			String sql = "SELECT TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_IS_CHECKED,(SELECT TZ_KXZ_QT_BZ FROM PS_TZ_APPXXX_KXZ_T B WHERE A.TZ_XXX_BH = B.TZ_XXX_BH AND B.TZ_APP_TPL_ID=? limit 0,1) AS 'TZ_KXZ_QT_BZ' FROM PS_TZ_APP_DHCC_T A WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
			String sql = "SELECT C.TZ_APP_S_TEXT,C.TZ_KXX_QTZ,C.TZ_IS_CHECKED,K.TZ_KXZ_QT_BZ FROM PS_TZ_APPXXX_KXZ_T K LEFT JOIN PS_TZ_APP_DHCC_T C ON K.TZ_XXX_BH = C.TZ_XXX_BH AND K.TZ_XXXKXZ_MC = C.TZ_XXXKXZ_MC WHERE K.TZ_APP_TPL_ID = ? AND C.TZ_APP_INS_ID = ? AND K.TZ_XXX_BH = ? ORDER BY K.TZ_ORDER;";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplid, insid, xxxBh });
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String kxzMs = result.get("TZ_APP_S_TEXT") == null ? "" : String.valueOf(result.get("TZ_APP_S_TEXT"));
				String kxzOthVal = result.get("TZ_KXX_QTZ") == null ? "" : String.valueOf(result.get("TZ_KXX_QTZ"));
				String kxzIsChecked = result.get("TZ_IS_CHECKED") == null ? "" : String.valueOf(result.get("TZ_IS_CHECKED"));
				String kxzQtBz = result.get("TZ_KXZ_QT_BZ") == null ? "" : String.valueOf(result.get("TZ_KXZ_QT_BZ"));

				String checked = "";
				if (StringUtils.equals("Y", kxzIsChecked)) {
					checked = "checked=\"checked\"";
				}
				if (!StringUtils.equals("Y", kxzQtBz)) {
					kxzOthVal = "";
				}
				
				String liHtml = "";
				try {
					liHtml = tzGdObject.getText(li, type, checked, kxzMs, kxzOthVal);
				} catch (TzSystemException e) {
					e.printStackTrace();
					liHtml = "";
				}
				val = val + liHtml;
			}
		} else if (StringUtils.equals(xxxCclx, "S")) {
			// 短文本
			PsTzAppCcTKey psTzAppCcTKey = new PsTzAppCcTKey();
			psTzAppCcTKey.setTzAppInsId(Long.parseLong(insid));
			psTzAppCcTKey.setTzXxxBh(xxxBh);

			PsTzAppCcT psTzAppCcT = psTzAppCcTMapper.selectByPrimaryKey(psTzAppCcTKey);
			if (psTzAppCcT == null) {
				return val;
			}
			String s_Text = psTzAppCcT.getTzAppSText();
			String l_Text = psTzAppCcT.getTzAppLText();

			if (StringUtils.contains("Select,CompanyNature,Degree,Diploma", xxxLmc)) {
				// "下拉框"、"学历"、"学位"、"公司性质"控件取可选值描述
				if (StringUtils.isNotBlank(s_Text)) {
					String kxzMsSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
					val = sqlQuery.queryForObject(kxzMsSql, new Object[] { tplid, xxxNo, s_Text }, "String");
				} else {
					val = "";
				}
			} else if (StringUtils.contains("bmrBatch,bmrMajor,bmrClass", xxxLmc)) {
				val = l_Text;
			} else if (StringUtils.equals("CheckBox", xxxLmc)) {
				if (StringUtils.equals("Y", s_Text)) {
					val = "checked=\"checked\"";
				} else {
					val = "";
				}
			}else if (StringUtils.equals("bmrPhoto", xxxLmc)){
				val = s_Text;
			} else if (StringUtils.contains(xxxBh, "com_todate")) {
				if (StringUtils.equals("Y", s_Text)) {
					val = "至今";
				} else {
					val = "";
				}
			} else {
				val = s_Text;
			}
			val = val == null ? "" : val;
		} else if (StringUtils.equals(xxxCclx, "L")) {
			// 长文本
			if (StringUtils.equals("TextExplain", xxxLmc)) {
				String kxzMsSql = "SELECT TZ_TITLE FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";
				val = sqlQuery.queryForObject(kxzMsSql, new Object[] { tplid, xxxNo }, "String");
			} else {
				String sql = "SELECT TZ_APP_L_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
				val = sqlQuery.queryForObject(sql, new Object[] { insid, xxxBh }, "String");
				if(val == null){
					return "";
				}
				val = val.replaceAll("\\n", "<br>");
			}
		}else if(StringUtils.equals("Page", xxxLmc)){
			val = xxxMc;
		}
		return val;
	}

	/**
	 * 获取多行容器控件HTML源码
	 * 
	 * @param insid		报名表实例编号
	 * @param tplid		报名表模板编号
	 * @param xxxBh		信息项编号
	 * @param xxxMc		信息项名称
	 * @param xxxLmc	信息项类名称
	 * @param i			序号
	 * @return			多行容器HTML源码
	 */
	@SuppressWarnings("unchecked")
	private String getDHContent(String insid, String tplid, String xxxBh, String xxxMc, String xxxLmc) {
		//控件的源HTML
		String comHtml = "";
		if (!comMap.containsKey(xxxLmc)) {
			String sql = "SELECT TZ_COM_EXPHTML FROM PS_TZ_COM_DY_T WHERE TZ_COM_JSLJ REGEXP BINARY  '" + xxxLmc + ".js$' limit 0,1";
			comHtml = sqlQuery.queryForObject(sql, new Object[] {}, "String");
			comMap.put(xxxLmc, comHtml);
		} else {
			comHtml = comMap.get(xxxLmc);
		}
		

		String maxLineSql = "SELECT MAX(TZ_LINE_NUM) FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ?";
		int maxLine = sqlQuery.queryForObject(maxLineSql, new Object[] { tplid, xxxBh }, "Integer");
		String sql = "";
		if (StringUtils.contains("DHContainer,LayoutControls", xxxLmc)) {
			sql = "SELECT TZ_XXX_BH,TZ_XXX_NO,TZ_XXX_MC,TZ_COM_LMC,TZ_XXX_CCLX,TZ_XXX_SRQ_BZ FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_LINE_NUM = ? AND TZ_IS_DOWNLOAD <> 'N' AND TZ_XXX_SRQ_BZ <> '' AND TZ_COM_LMC NOT IN ('Separator') ORDER BY TZ_LINE_ORDER";
		}else{
			sql = "SELECT TZ_XXX_BH,TZ_XXX_NO,TZ_XXX_MC,TZ_COM_LMC,TZ_XXX_CCLX,TZ_XXX_SRQ_BZ FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_LINE_NUM = ? AND TZ_IS_DOWNLOAD <> 'N' AND TZ_COM_LMC NOT IN ('Separator') ORDER BY TZ_LINE_ORDER";
		}
		
		StringBuffer val = new StringBuffer("");
		val.append("<div class=\"content_para\">");

		for (int j = 0; j <= maxLine; j++) {

			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplid, xxxBh, j});
			
			StringBuffer para = new StringBuffer("");
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String xxxBh1 = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
				String xxxNo1 = result.get("TZ_XXX_NO") == null ? "" : String.valueOf(result.get("TZ_XXX_NO"));
				String xxxMc1 = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
				String xxxLmc1 = result.get("TZ_COM_LMC") == null ? "" : String.valueOf(result.get("TZ_COM_LMC"));
				String xxxCclx1 = result.get("TZ_XXX_CCLX") == null ? "" : String.valueOf(result.get("TZ_XXX_CCLX"));
				String srqFlag1 = result.get("TZ_XXX_SRQ_BZ") == null ? "" : String.valueOf(result.get("TZ_XXX_SRQ_BZ"));
				
				String isHas = this.isHas(insid, tplid, xxxBh, xxxBh1, srqFlag1);
				if(!StringUtils.equals("Y", isHas)){
					//改行信息项未在客户端填写
					break;
				}
				
				if (StringUtils.equals("Y", srqFlag1)) {
					String singleContent = "";
					if (StringUtils.contains("DHContainer,LayoutControls", xxxLmc)) {
						singleContent = this.getSingleContentInDH(insid, tplid,xxxBh, xxxBh1, xxxNo1, xxxMc1, xxxLmc1, xxxCclx1, j);
					}else{
						singleContent = this.getSingleContent(insid, tplid, xxxBh1, xxxNo1, xxxMc1, xxxLmc1, xxxCclx1);
					}

					if (StringUtils.isNoneBlank(singleContent)) {
						para.append(singleContent);
					}
				} else {
					String content = this.getContent(insid, tplid, xxxBh1, xxxNo1, xxxMc1, xxxLmc1, xxxCclx1);
					if (StringUtils.isNoneBlank(content)) {
						para.append(content);
					}
				}
			}
			if(j > 0 && para.length() > 0){
				val.append("<div class=\"padding_div\"></div>");
			}
			val.append(para.toString());
			
		}
		val.append("<br style=\"clear:both\"></div>");
		
		try {
			if (StringUtils.isNotBlank(comHtml)) {
				comHtml = tzGdObject.getText(comHtml,xxxMc, val.toString());
			} else {
				comHtml = "";
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
			comHtml = "";
		}
		return comHtml;
	}
	
	/**
	 * 判断多行容器中，改信息项是否被考生填写
	 * @param insid		实例编号
	 * @param tplid		模板编号
	 * @param xxxBh		信息项编号
	 * @param srqFlag	是否组合控件
	 * @param srqFlag1 
	 * @return
	 */
	private String isHas(String insid, String tplid, String parentXxxBh,String xxxBh,String srqFlag){
		String isHas = "N";

		if(StringUtils.equals("Y", srqFlag)){
			String sqlCC = "SELECT 'Y' FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH IN (SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH REGEXP BINARY ?) LIMIT 0,1";
			String sqlDH = "SELECT 'Y' FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH IN (SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH REGEXP BINARY ?) LIMIT 0,1";
			
			isHas = sqlQuery.queryForObject(sqlCC, new Object[] {insid,tplid,parentXxxBh,"^" + xxxBh}, "String");
			if(!StringUtils.equals(isHas, "Y")){
				isHas = sqlQuery.queryForObject(sqlDH, new Object[] {insid,tplid,parentXxxBh,"^" + xxxBh}, "String");
			}
		}else{
			String sqlCC = "SELECT 'Y' FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
			String sqlDH = "SELECT 'Y' FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
			
			isHas = sqlQuery.queryForObject(sqlCC, new Object[] {insid,xxxBh}, "String");
			if(!StringUtils.equals(isHas, "Y")){
				isHas = sqlQuery.queryForObject(sqlDH, new Object[] {insid,xxxBh}, "String");
			}
		}
		
		return isHas;
	}
	
	/**
	 * 生产PDF文件
	 * @param path		PDF文件存放路径
	 * @param filename	文件名称
	 * @param insid		实例编号
	 * @param type 		类型("A":报名表，  "B": 推荐信)
	 * @return			是否创建PDF成功
	 */
	public boolean generatePdf(String path,String filename,String insid,String type){
		//报名表打包文件路径
		String pdfPath = "";
		String bmbPackRarDir = getSysHardCodeVal.getBmbPackRarDir();
		if(StringUtils.endsWith(bmbPackRarDir, "/")){
			pdfPath = bmbPackRarDir + path;
		}else{
			pdfPath = bmbPackRarDir + "/" + path;
		}
		
		if(!StringUtils.endsWith(filename.toUpperCase(), ".PDF")){
			filename = filename + ".pdf";
		}
		
		if(StringUtils.endsWith(pdfPath, "/")){
			filename = pdfPath + filename;
		}else{
			filename = pdfPath + "/" + filename;
		}
		String bmbInsId = insid;
		if(StringUtils.equals("B", type)){
			String sqlTjx = "SELECT TZ_APP_INS_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_TJX_APP_INS_ID = ? LIMIT 1";
			bmbInsId = sqlQuery.queryForObject(sqlTjx, new Object[] { insid }, "String");
		}

		String sqlOprid = "SELECT OPRID,TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ? ORDER BY OPRID LIMIT 1";

		Map<String, Object> workMap = sqlQuery.queryForMap(sqlOprid, new Object[] { bmbInsId });
		String oprid = workMap.get("OPRID") == null ? "" : String.valueOf(workMap.get("OPRID"));
		String classid = workMap.get("TZ_CLASS_ID") == null ? "" : String.valueOf(workMap.get("TZ_CLASS_ID"));
		
		String classSql = "SELECT TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
		String className = sqlQuery.queryForObject(classSql, new Object[] { classid }, "String");
		
		
		String sqlRealName = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = ?";
		String userName = sqlQuery.queryForObject(sqlRealName, new Object[] { oprid }, "String");
		
		//报名表模板编号 
		String sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ? LIMIT 0,1";
		String tplid =  sqlQuery.queryForObject(sql, new Object[] { insid },"String");
		//title
		String title = className + "-" + userName;
		
		String url = this.createPdf(tplid, insid, title, filename);
		
		if(StringUtils.isBlank(url)){
			return false;
		}else{
			return true;
		}
	}
}
