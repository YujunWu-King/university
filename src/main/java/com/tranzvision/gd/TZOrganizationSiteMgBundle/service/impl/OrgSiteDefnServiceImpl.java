package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiColuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMenuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiTempTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 站点模板设置， 原PS类：TZ_GD_ZDDY_PKG:TZ_ZDGL_COM_CLS
 * 描述：高端产品-站点管理-站点管理基本信息页面
 * @author tang
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteDefnServiceImpl")
@SuppressWarnings("unchecked")
public class OrgSiteDefnServiceImpl extends FrameworkImpl{
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	@Autowired
	private PsTzSiteiTempTMapper psTzSiteiTempTMapper;
	@Autowired
	private PsTzSiteiColuTMapper psTzSiteiColuTMapper;
	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;
	@Autowired
	private PsTzSiteiMenuTMapper psTzSiteiMenuTMapper;
	
	
	/* 新增机构站点信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("siteId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_DEFN_T", "TZ_SITEI_ID"));
				
				String siteLanguage = jacksonUtil.getString("siteLanguage");
				String siteName = jacksonUtil.getString("siteName");
				String enabled = jacksonUtil.getString("enabled");
				String siteIntroduce = jacksonUtil.getString("siteIntroduce");
				String picPrefix = jacksonUtil.getString("picPrefix");
				String staPrefix = jacksonUtil.getString("staPrefix");
				String attPrefix = jacksonUtil.getString("attPrefix");
				String attAccessPrefix = jacksonUtil.getString("attAccessPrefix");
				String viewPrefix = jacksonUtil.getString("viewPrefix");
				String viewAccessPrefix = jacksonUtil.getString("viewAccessPrefix");
				
				String siteHomeProgram = jacksonUtil.getString("siteHomeProgram");
				String siteLoginProgram = jacksonUtil.getString("siteLoginProgram");
				String siteRegistProgram = jacksonUtil.getString("siteRegistProgram");
				
				String skinname = jacksonUtil.getString("skinname");
				String skincode = jacksonUtil.getString("skincode");
				String skinstor = jacksonUtil.getString("skinstor");
				
				String indexSaveCode = jacksonUtil.getString("indexSaveCode");
				String indexPubCode = jacksonUtil.getString("indexPubCode");
				String loginSaveCode = jacksonUtil.getString("loginSaveCode"); 
				String loginPubCode = jacksonUtil.getString("loginPubCode");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = new PsTzSiteiDefnTWithBLOBs();
				psTzSiteiDefnT.setTzSiteiId(siteId);
				psTzSiteiDefnT.setTzSiteLang(siteLanguage);
				psTzSiteiDefnT.setTzSiteiName(siteName);
				psTzSiteiDefnT.setTzSiteiEnable(enabled);
				psTzSiteiDefnT.setTzSiteiDescr(siteIntroduce);
				psTzSiteiDefnT.setTzImgStor(picPrefix);
				psTzSiteiDefnT.setTzImgView(staPrefix);
				psTzSiteiDefnT.setTzAttsStor(attPrefix);
				psTzSiteiDefnT.setTzAttsView(attAccessPrefix);
				psTzSiteiDefnT.setTzVideoStor(viewPrefix);
				psTzSiteiDefnT.setTzVideoView(viewAccessPrefix);
				
				psTzSiteiDefnT.setTzHomeHandPro(siteHomeProgram);
				psTzSiteiDefnT.setTzLoginHandPro(siteLoginProgram);
				psTzSiteiDefnT.setTzRegisHandPro(siteRegistProgram);

				psTzSiteiDefnT.setTzSkinName(skinname);
				psTzSiteiDefnT.setTzSkinCode(skincode);
				psTzSiteiDefnT.setTzSkinStor(skinstor);
				
				psTzSiteiDefnT.setTzIndexSavecode(indexSaveCode);
				psTzSiteiDefnT.setTzIndexPubcode(indexPubCode);
				psTzSiteiDefnT.setTzLonginSavecode(loginSaveCode);
				psTzSiteiDefnT.setTzLonginPubcode(loginPubCode);
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzSiteiDefnT.setTzAddedOprid(oprid);
				psTzSiteiDefnT.setTzAddedDttm(new Date());
				psTzSiteiDefnT.setTzLastmantOprid(oprid);
				psTzSiteiDefnT.setTzLastmantDttm(new Date());
				int i = psTzSiteiDefnTMapper.insert(psTzSiteiDefnT);
				if (i > 0) {
					returnJsonMap.replace("siteId", siteId);
					//生成样式文件;
					try{
						String websitePath = getSysHardCodeVal.getWebsiteCssPath();
						String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request); 
						String dirPath = "";
						if(skinstor == null || "".equals(skinstor)){
							dirPath = websitePath + "/" + orgid.toLowerCase() + "/" + siteId;
						}else{
							dirPath = websitePath + "/" + orgid.toLowerCase() + "/" + siteId + "/" + skinstor;
						}
						
						String parentRealPath = request.getServletContext().getRealPath(dirPath);
	
						File dir = new File(parentRealPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						
						String filePath = "";
						if(orgid != null && !"".equals(orgid)){
							filePath = dir + "/" +  "style_" + orgid.toLowerCase() + ".css";
						}else{
							filePath = dir + "/" +  "style.css";
						}
						
						File file = new File(filePath);
						if (!file.exists()) {
							file.createNewFile();
						}
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(skincode);
						bw.close();
					}catch(Exception e){
						errMsg[0] = "1";
						errMsg[1] = "皮肤样式文件生成失败";
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构站点信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 修改机构站点信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("siteId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				
				String siteLanguage = jacksonUtil.getString("siteLanguage");
				String siteName = jacksonUtil.getString("siteName");
				String enabled = jacksonUtil.getString("enabled");
				String siteIntroduce = jacksonUtil.getString("siteIntroduce");
				String picPrefix = jacksonUtil.getString("picPrefix");
				String staPrefix = jacksonUtil.getString("staPrefix");
				String attPrefix = jacksonUtil.getString("attPrefix");
				String attAccessPrefix = jacksonUtil.getString("attAccessPrefix");
				String viewPrefix = jacksonUtil.getString("viewPrefix");
				String viewAccessPrefix = jacksonUtil.getString("viewAccessPrefix");
				
				String siteHomeProgram = jacksonUtil.getString("siteHomeProgram");
				String siteLoginProgram = jacksonUtil.getString("siteLoginProgram");
				String siteRegistProgram = jacksonUtil.getString("siteRegistProgram");
				
				String skinname = jacksonUtil.getString("skinname");
				String skincode = jacksonUtil.getString("skincode");
				String skinstor = jacksonUtil.getString("skinstor");
				
				String indexSaveCode = jacksonUtil.getString("indexSaveCode");
				//String indexPubCode = jacksonUtil.getString("indexPubCode");
				String loginSaveCode = jacksonUtil.getString("loginSaveCode"); 
				//String loginPubCode = jacksonUtil.getString("loginPubCode");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = new PsTzSiteiDefnTWithBLOBs();
				psTzSiteiDefnT.setTzSiteiId(siteId);
				psTzSiteiDefnT.setTzSiteLang(siteLanguage);
				psTzSiteiDefnT.setTzSiteiName(siteName);
				psTzSiteiDefnT.setTzSiteiEnable(enabled);
				psTzSiteiDefnT.setTzSiteiDescr(siteIntroduce);
				psTzSiteiDefnT.setTzImgStor(picPrefix);
				psTzSiteiDefnT.setTzImgView(staPrefix);
				psTzSiteiDefnT.setTzAttsStor(attPrefix);
				psTzSiteiDefnT.setTzAttsView(attAccessPrefix);
				psTzSiteiDefnT.setTzVideoStor(viewPrefix);
				psTzSiteiDefnT.setTzVideoView(viewAccessPrefix);
				
				psTzSiteiDefnT.setTzHomeHandPro(siteHomeProgram);
				psTzSiteiDefnT.setTzLoginHandPro(siteLoginProgram);
				psTzSiteiDefnT.setTzRegisHandPro(siteRegistProgram);

				psTzSiteiDefnT.setTzSkinName(skinname);
				psTzSiteiDefnT.setTzSkinCode(skincode);
				psTzSiteiDefnT.setTzSkinStor(skinstor);
				
				psTzSiteiDefnT.setTzIndexSavecode(indexSaveCode);
				//psTzSiteiDefnT.setTzIndexPubcode(indexPubCode);
				psTzSiteiDefnT.setTzLonginSavecode(loginSaveCode);
				//psTzSiteiDefnT.setTzLonginPubcode(loginPubCode);
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzSiteiDefnT.setTzLastmantOprid(oprid);
				psTzSiteiDefnT.setTzLastmantDttm(new Date());
				int i = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
				if (i > 0) {
					returnJsonMap.replace("siteId", siteId);
					//生成样式文件;
					try{
						String websitePath = getSysHardCodeVal.getWebsiteCssPath();
						//得到当前站点属于的机构;
						String orgid = "";
						psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(siteId);
						orgid = psTzSiteiDefnT.getTzJgId();
						if(orgid == null || "".equals(orgid)){
							orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request); 
						}
						String dirPath = "";
						if("".equals(skinstor)){
							dirPath = websitePath + "/" + orgid.toLowerCase() + "/" + siteId + "/" ;
						}else{
							dirPath = websitePath + "/" + orgid.toLowerCase() + "/" + siteId + "/" + skinstor;
						}
						
						String parentRealPath = request.getServletContext().getRealPath(dirPath);
	
						File dir = new File(parentRealPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						
						String filePath = dir + File.separator +  "style_" + orgid.toLowerCase() + ".css";
						File file = new File(filePath);
						if (!file.exists()) {
							file.createNewFile();
						}
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(skincode);
						bw.close();
					}catch(Exception e){
						errMsg[0] = "1";
						errMsg[1] = "皮肤样式文件生成失败";
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "机构站点信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id;
				String siteId = jacksonUtil.getString("siteId");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(siteId);

				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("siteId", siteId);
				jsonMap.put("siteName", psTzSiteiDefnT.getTzSiteiName());
				jsonMap.put("enabled", psTzSiteiDefnT.getTzSiteiEnable());
				jsonMap.put("siteIntroduce", psTzSiteiDefnT.getTzSiteiDescr());
				jsonMap.put("picPrefix", psTzSiteiDefnT.getTzImgStor());
				jsonMap.put("staPrefix", psTzSiteiDefnT.getTzImgView());
				jsonMap.put("attPrefix", psTzSiteiDefnT.getTzAttsStor());
				jsonMap.put("attAccessPrefix", psTzSiteiDefnT.getTzAttsView());
				jsonMap.put("viewPrefix", psTzSiteiDefnT.getTzVideoStor());
				jsonMap.put("viewAccessPrefix", psTzSiteiDefnT.getTzVideoView());
				
				jsonMap.put("siteHomeProgram", psTzSiteiDefnT.getTzHomeHandPro());
				jsonMap.put("siteLoginProgram", psTzSiteiDefnT.getTzLoginHandPro());
				jsonMap.put("siteRegistProgram", psTzSiteiDefnT.getTzRegisHandPro());
				
				jsonMap.put("skinname", psTzSiteiDefnT.getTzSkinName());
				jsonMap.put("skincode", psTzSiteiDefnT.getTzSkinCode());
				jsonMap.put("skinstor", psTzSiteiDefnT.getTzSkinStor());
				
				jsonMap.put("siteLanguage", psTzSiteiDefnT.getTzSiteLang());
				jsonMap.put("indexSaveCode", psTzSiteiDefnT.getTzIndexSavecode());
				jsonMap.put("indexPubCode", psTzSiteiDefnT.getTzIndexPubcode());
				jsonMap.put("loginSaveCode", psTzSiteiDefnT.getTzLonginSavecode());
				jsonMap.put("loginPubCode", psTzSiteiDefnT.getTzLonginPubcode());

				returnJsonMap.replace("formData", jsonMap);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/* 列表查询 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		if (jacksonUtil.containsKey("queryID")) {
			try {
				int queryID = jacksonUtil.getInt("queryID");
				switch (queryID) {
				// 皮肤设置;
				case 1:
					break;
				// 站点模板集合;
				case 2:
					strRet = this.queryTemplateList(comParams, numLimit, numStart, errorMsg);
					break;
				// 站点栏目集合;
				case 3:
					strRet = this.queryColumnList(comParams, numLimit, numStart, errorMsg);
					break;
				// 站点区域集合
				case 4:
					strRet = this.queryAreaList(comParams, numLimit, numStart, errorMsg);
					break;
				// 站点菜单集合
				case 5:
					strRet = this.queryMenuList(comParams, numLimit, numStart, errorMsg);
					break;
				default:
					break;
				}

			} catch (Exception e) {
				errorMsg[0] = "1";
				errorMsg[1] = "查询列表参数有误";
			}
		}
		return strRet;
	}
	
	
	/* 删除站点模板中的数据 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if (jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("siteId")
						&& jacksonUtil.containsKey("deleteList")) {
					//删除数据;
					String siteId = jacksonUtil.getString("siteId");
		            int queryID = jacksonUtil.getInt("queryID");
					
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("deleteList");
		            int i = 0;
					switch (queryID) {
					// 皮肤设置;
					case 1:
						break;
					// 站点模板集合;
					case 2:
						for(i = 0; i<list.size();i++){
							String templateId = (String) list.get(i).get("templateId");
							PsTzSiteiTempTKey psTzSiteiTempTKey= new PsTzSiteiTempTKey();
							psTzSiteiTempTKey.setTzSiteiId(siteId);
							psTzSiteiTempTKey.setTzTempId(templateId);
							psTzSiteiTempTMapper.deleteByPrimaryKey(psTzSiteiTempTKey);
						}
						break;
					// 站点栏目集合;
					case 3:
						for(i = 0; i<list.size();i++){
							String lm_id = (String) list.get(i).get("lm_id");
							PsTzSiteiColuTKey psTzSiteiColuTKey= new PsTzSiteiColuTKey();
							psTzSiteiColuTKey.setTzSiteiId(siteId);
							psTzSiteiColuTKey.setTzColuId(lm_id);
							psTzSiteiColuTMapper.deleteByPrimaryKey(psTzSiteiColuTKey);
						}
						break;
					// 站点区域集合
					case 4:
						for(i = 0; i<list.size();i++){
							String areaid = (String) list.get(i).get("areaid");
							PsTzSiteiAreaTKey psTzSiteiAreaTKey= new PsTzSiteiAreaTKey();
							psTzSiteiAreaTKey.setTzSiteiId(siteId);
							psTzSiteiAreaTKey.setTzAreaId(areaid);
							psTzSiteiAreaTMapper.deleteByPrimaryKey(psTzSiteiAreaTKey);
						}
						break;
					// 站点菜单集合
					case 5:
						for(i = 0; i<list.size();i++){
							String menuid = (String) list.get(i).get("menuid");
							PsTzSiteiMenuTKey psTzSiteiMenuTKey= new PsTzSiteiMenuTKey();
							psTzSiteiMenuTKey.setTzSiteiId(siteId);
							psTzSiteiMenuTKey.setTzMenuId(menuid);
							psTzSiteiMenuTMapper.deleteByPrimaryKey(psTzSiteiMenuTKey);
							
							String deleteSQL = "DELETE FROM PS_TZ_SITEI_MNPF_T WHERE TZ_SITEI_ID=? AND TZ_MENU_ID=?";
							jdbcTemplate.update(deleteSQL,new Object[]{siteId,menuid});
						}
						
						break;
					default:
						break;
					}
		            
				}
			}
		} catch (Exception e) {

		}
		return strRet;
	}

	
	/*站点模板集合列表*/
	public String queryTemplateList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String siteId = jacksonUtil.getString("siteId");
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_TEMP_T WHERE TZ_SITEI_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT A.TZ_TEMP_ID,A.TZ_TEMP_STATE,A.TZ_TEMP_NAME,B.TZ_ZHZ_DMS TZ_TEMP_TYPE,A.TZ_TEMP_PCCODE,A.TZ_TEMP_MSCODE FROM PS_TZ_SITEI_TEMP_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_TEMP_TYPE' AND TZ_EFF_STATUS='A') B on A.TZ_TEMP_TYPE = B.TZ_ZHZ_ID WHERE TZ_SITEI_ID=? limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT A.TZ_TEMP_ID,A.TZ_TEMP_STATE,A.TZ_TEMP_NAME,B.TZ_ZHZ_DMS TZ_TEMP_TYPE,A.TZ_TEMP_PCCODE,A.TZ_TEMP_MSCODE FROM PS_TZ_SITEI_TEMP_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_TEMP_TYPE' AND TZ_EFF_STATUS='A') B on A.TZ_TEMP_TYPE = B.TZ_ZHZ_ID WHERE TZ_SITEI_ID=?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}

			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("templateId", list.get(i).get("TZ_TEMP_ID"));
					jsonMap.put("templateName", list.get(i).get("TZ_TEMP_NAME"));
					jsonMap.put("templateType", list.get(i).get("TZ_TEMP_TYPE"));
					jsonMap.put("templatePCCode", list.get(i).get("TZ_TEMP_PCCODE"));
					jsonMap.put("templateMBCode", list.get(i).get("TZ_TEMP_MSCODE"));
					jsonMap.put("templateState", list.get(i).get("TZ_TEMP_STATE"));
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/*站点栏目列表*/
	public String queryColumnList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String siteId = jacksonUtil.getString("siteId");
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT TZ_COLU_ID,TZ_COLU_NAME,TZ_COLU_TYPE,TZ_CONT_TYPE,TZ_COLU_STATE FROM PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=? limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT TZ_COLU_ID,TZ_COLU_NAME,TZ_COLU_TYPE,TZ_CONT_TYPE,TZ_COLU_STATE FROM PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}
			String zhzSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'";
			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					
					String lm_lx = (String) list.get(i).get("TZ_COLU_TYPE");
					lm_lx = jdbcTemplate.queryForObject(zhzSQL, new Object[]{"TZ_ZDLM_LX",lm_lx},"String");
					
					String lm_nrlx = (String) list.get(i).get("TZ_CONT_TYPE");
					lm_nrlx = jdbcTemplate.queryForObject(zhzSQL, new Object[]{"TZ_ZD_NRLX",lm_nrlx},"String");
					
					String lm_yxzt = (String) list.get(i).get("TZ_COLU_STATE");
					lm_yxzt = jdbcTemplate.queryForObject(zhzSQL, new Object[]{"TZ_COLUMN_STATE",lm_yxzt},"String");
					
					jsonMap.put("lm_id", list.get(i).get("TZ_COLU_ID"));
					jsonMap.put("lm_name", list.get(i).get("TZ_COLU_NAME"));
					jsonMap.put("lm_lx", lm_lx);
					jsonMap.put("lm_nrlx", lm_nrlx);
					jsonMap.put("lm_yxzt", lm_yxzt);
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/*站点区域列表*/
	public String queryAreaList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String siteId = jacksonUtil.getString("siteId");
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT TZ_AREA_ID,TZ_AREA_NAME,TZ_AREA_TYPE_ID,TZ_AREA_POSITION FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=? ORDER BY TZ_AREA_ID ASC limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT TZ_AREA_ID,TZ_AREA_NAME,TZ_AREA_TYPE_ID,TZ_AREA_POSITION FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=? ORDER BY TZ_AREA_ID ASC";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}

			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					String zhzSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'";
					String strAreaPosition = (String) list.get(i).get("TZ_AREA_POSITION");
					strAreaPosition = jdbcTemplate.queryForObject(zhzSQL, new Object[]{"TZ_AREA_POSITION",strAreaPosition},"String");
					
					String strAreaTypeId = (String) list.get(i).get("TZ_AREA_TYPE_ID");
					String areaTypeNmaeSQL = "select TZ_AREA_TYPE_NAME from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
					String strAreaTypeName = jdbcTemplate.queryForObject(areaTypeNmaeSQL, new Object[]{siteId,strAreaTypeId},"String");
					
					jsonMap.put("areaid", list.get(i).get("TZ_AREA_ID"));
					jsonMap.put("areaname", list.get(i).get("TZ_AREA_NAME"));
					jsonMap.put("areatypeid", strAreaTypeName);
					jsonMap.put("areaposition", strAreaPosition);
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/*站点菜单列表*/
	public String queryMenuList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String siteId = jacksonUtil.getString("siteId");
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_MENU_T WHERE TZ_SITEI_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT TZ_MENU_ID,TZ_MENU_NAME,TZ_MENU_TYPE_ID FROM PS_TZ_SITEI_MENU_T WHERE TZ_SITEI_ID=? ORDER BY TZ_MENU_ID ASC limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT TZ_MENU_ID,TZ_MENU_NAME,TZ_MENU_TYPE_ID FROM PS_TZ_SITEI_MENU_T WHERE TZ_SITEI_ID=? ORDER BY TZ_MENU_ID ASC";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}

			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();

					String strMenuTypeId = (String) list.get(i).get("TZ_MENU_TYPE_ID");
					String menuTypeNmaeSQL = "select TZ_MENU_TYPE_NAME from PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID=? and TZ_MENU_TYPE_ID=?";
					String strMenuTypeName = jdbcTemplate.queryForObject(menuTypeNmaeSQL, new Object[]{siteId,strMenuTypeId},"String");
					
					jsonMap.put("menuid", list.get(i).get("TZ_MENU_ID"));
					jsonMap.put("menuname", list.get(i).get("TZ_MENU_NAME"));
					jsonMap.put("menutypename", strMenuTypeName);
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
