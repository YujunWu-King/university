package com.tranzvision.gd.TZApplicationCenterBundle.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartJSSDKSign;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tranzvision.gd.util.base.AnalysisSysVar;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzUserregMbTMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzUserregMbT;

/**
 * 录取通知书html5展示
 * 
 * @para 录取通知书html
 * @ret 报名表对应的解析证书模板后的html或静态html
 * @author YTT
 * @since 2017-01-22
 */
@Controller
@RequestMapping(value = { "/admission" })
public class TzAppAdmissionController {

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private SqlQuery sqlQuery1;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzUserregMbTMapper psTzUserregMbTMapper;
	@Autowired
	private TzWeChartJSSDKSign tzWeChartJSSDKSign;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private HttpServletRequest request;

	// 生成录取通知书html
	@RequestMapping(value = { "/{orgid}/{siteid}/{oprid}/{tzAppInsID}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String viewQrcodeAdmission(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid,
			@PathVariable(value = "oprid") String oprid, @PathVariable(value = "tzAppInsID") String tzAppInsID,
			String[] errMsg) {
		try {
			orgid = orgid.toLowerCase();
			String strRet = "";
			String tzCertMergHtml = "";

			String staticHtmlDirSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			// 录取通知书静态html路径——"/statics/css/website/m/html"
			String staticHtmlDir = sqlQuery1.queryForObject(staticHtmlDirSql, new Object[] { "TZ_GD_CERT_STCHTML_DIR" },
					"String");

			String dir = request.getServletContext().getRealPath(staticHtmlDir);
			String fileName = tzAppInsID + ".html";

			String filePath = "";
			if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				filePath = dir + File.separator + fileName;
			} else {
				filePath = dir + fileName;
			}

			// 判断静态文件是否已存在
			File file = new File(filePath);
			if (!file.exists()) {

				// 【0】查询录取状态
				String tzLuquStaSql = "SELECT TZ_LUQU_ZT FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_APP_INS_ID=?";
				String tzLuquSta = sqlQuery1.queryForObject(tzLuquStaSql, new Object[] { tzAppInsID }, "String");

				if (tzLuquSta == "LQ") {// 条件录取
					// 【1】查询证书模板id
					String tzCertTplIdSql = "SELECT B.TZ_CERT_TMPL_ID FROM PS_TZ_APP_INS_T A,PS_TZ_PRJ_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_MODAL_ID";
					String tzCertTplId = sqlQuery1.queryForObject(tzCertTplIdSql, new Object[] { tzAppInsID },
							"String");

					// 【2】获取证书模板默认套打模板html
					String tzCertMergHtmlSql = "SELECT TZ_CERT_MERG_HTML1 FROM PS_TZ_CERTTMPL_TBL WHERE TZ_CERT_TMPL_ID=? AND TZ_JG_ID=? AND TZ_USE_FLAG='Y'";
					tzCertMergHtml = sqlQuery1.queryForObject(tzCertMergHtmlSql, new Object[] { tzCertTplId, orgid },
							"String");

					// 【3】解析系统变量、返回解析后的html
					int syavarStartIndex = tzCertMergHtml.indexOf("[SYSVAR-");
					while (syavarStartIndex != -1) {
						int syavarEndIndex = tzCertMergHtml.indexOf(']', syavarStartIndex);
						String sysvarId = tzCertMergHtml.substring(syavarStartIndex + 8, syavarEndIndex);
						System.out.println(sysvarId);
						String[] sysVarParam = { orgid, siteid, oprid, tzAppInsID };
						AnalysisSysVar analysisSysVar = new AnalysisSysVar();
						analysisSysVar.setM_SysVarID(sysvarId);
						analysisSysVar.setM_SysVarParam(sysVarParam);
						Object sysvarValue = analysisSysVar.GetVarValue();
						System.out.println((String) sysvarValue);
						System.out.println("[SYSVAR-" + sysvarId + "]");
						tzCertMergHtml = tzCertMergHtml.replace("[SYSVAR-" + sysvarId + "]", (String) sysvarValue);

						System.out.println(tzCertMergHtml);
						syavarStartIndex = tzCertMergHtml.indexOf("[SYSVAR-");
					}
				} else {
					tzCertMergHtml = "<html style='font-size:40px'>抱歉，该考生未录取，无法查看录取通知书</html>";
				}
				// 【4】生成静态录取通知书html
				boolean bl = this.staticFile(tzCertMergHtml, dir, fileName, errMsg);
				if (!bl) {
					errMsg[0] = "1";
					errMsg[1] = "静态化html失败！";
				}
				// }else{tzCertMergHtml="该学员还未录取！";}
			} else {
				strRet = request.getScheme() + "://" + request.getServerName() + ":"
						+ String.valueOf(request.getServerPort()) + request.getContextPath() + staticHtmlDir;

				if ((strRet.lastIndexOf(File.separator) + 1) != strRet.length()) {
					strRet = strRet + File.separator + fileName;
				} else {
					strRet = strRet + fileName;
				}

				response.sendRedirect(strRet);
			}

			strRet = tzCertMergHtml;
			return strRet;

		} catch (Exception e) {
			errMsg[0] = "2";
			errMsg[1] = "晒录取通知书异常！";
			return "";
		}
	}
	
	
	/***
	 * 生成微信签名信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "wxSign", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String wxSign(HttpServletRequest request, HttpServletResponse response) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("result", "");
		try{
			String url = request.getParameter("url");
			
			String appId = getHardCodePoint.getHardCodePointVal("TZ_WX_CORPID");
			String secret = getHardCodePoint.getHardCodePointVal("TZ_WX_SECRET");
			String wxType = getHardCodePoint.getHardCodePointVal("TZ_WX_TYPE");
			
			Map<String,String> signMap = tzWeChartJSSDKSign.sign(appId, secret, wxType, url);

			if(signMap != null){
				jsonMap.replace("result", "success");
				jsonMap.put("appId", appId);
				jsonMap.put("timestamp", signMap.get("timestamp"));
				jsonMap.put("nonceStr", signMap.get("nonceStr"));
				jsonMap.put("signature", signMap.get("signature"));
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.replace("result", "failure");
		}
		return jacksonUtil.Map2json(jsonMap);
	}
	
	
	

	public boolean staticFile(String strReleasContent, String dir, String fileName, String[] errMsg) {
		try {
			System.out.println(dir);
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			String filePath = "";
			if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				filePath = dir + File.separator + fileName;
			} else {
				filePath = dir + fileName;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strReleasContent);
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "3";
			errMsg[1] = "静态化文件时异常！";
			return false;
		}
	}

}
