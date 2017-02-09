package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

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
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemAreaTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemColuTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemDefnTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemMenuTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemSkinTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemTempTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemAreaTKey;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemColuTKey;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemDefnTWithBLOBs;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMenuTKey;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinTKey;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemTempTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 站点模板设置， 原PS类：TZ_GD_DZMB_PKG:TZ_ZDMB_COM_SLS
 * 描述：高端产品-站点模板设置-站点模板基本信息页面
 * @author tang
 */
@SuppressWarnings("unchecked")
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelDefnServiceImpl")
public class TemplateModelDefnServiceImpl extends FrameworkImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzSitemDefnTMapper psTzSitemDefnTMapper;
	@Autowired
	private PsTzSitemSkinTMapper psTzSitemSkinTMapper;
	@Autowired
	private PsTzSitemTempTMapper psTzSitemTempTMapper;
	@Autowired
	private PsTzSitemColuTMapper psTzSitemColuTMapper;
	@Autowired
	private PsTzSitemAreaTMapper psTzSitemAreaTMapper;
	@Autowired
	private PsTzSitemMenuTMapper psTzSitemMenuTMapper;

	/* 新增站点模板信息 */
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
				String skinAddress = jacksonUtil.getString("skinAddress");
				String siteHomeProgram = jacksonUtil.getString("siteHomeProgram");
				String siteLoginProgram = jacksonUtil.getString("siteLoginProgram");
				String siteRegistProgram = jacksonUtil.getString("siteRegistProgram");
				String indexInitCode = jacksonUtil.getString("indexInitCode");
				String loginInitCode = jacksonUtil.getString("loginInitCode");
				String siteId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEM_DEFN_T", "TZ_SITEM_ID"));

				PsTzSitemDefnTWithBLOBs psTzSitemDefnT = new PsTzSitemDefnTWithBLOBs();
				psTzSitemDefnT.setTzSitemId(siteId);
				psTzSitemDefnT.setTzJgbh("");
				psTzSitemDefnT.setTzSiteLang(siteLanguage);
				psTzSitemDefnT.setTzSitemName(siteName);
				psTzSitemDefnT.setTzSitemEnable(enabled);
				psTzSitemDefnT.setTzSitemDescr(siteIntroduce);
				psTzSitemDefnT.setTzImgStor(picPrefix);
				psTzSitemDefnT.setTzImgView(staPrefix);
				psTzSitemDefnT.setTzAttsStor(attPrefix);
				psTzSitemDefnT.setTzAttsView(attAccessPrefix);
				psTzSitemDefnT.setTzVideoStor(viewPrefix);
				psTzSitemDefnT.setTzVideoView(viewAccessPrefix);
				psTzSitemDefnT.setTzSkinStor(skinAddress);
				psTzSitemDefnT.setTzHomeHandPro(siteHomeProgram);
				psTzSitemDefnT.setTzLoginHandPro(siteLoginProgram);
				psTzSitemDefnT.setTzRegisHandPro(siteRegistProgram);
				psTzSitemDefnT.setTzIndexInitcode(indexInitCode);
				psTzSitemDefnT.setTzLonginInitcode(loginInitCode);
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzSitemDefnT.setTzAddedOprid(oprid);
				psTzSitemDefnT.setTzAddedDttm(new Date());
				psTzSitemDefnT.setTzLastmantOprid(oprid);
				psTzSitemDefnT.setTzLastmantDttm(new Date());
				int i = psTzSitemDefnTMapper.insert(psTzSitemDefnT);
				if (i > 0) {
					returnJsonMap.replace("siteId", siteId);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "站点信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 修改站点模板信息 */
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
				String skinAddress = jacksonUtil.getString("skinAddress");
				String siteHomeProgram = jacksonUtil.getString("siteHomeProgram");
				String siteLoginProgram = jacksonUtil.getString("siteLoginProgram");
				String siteRegistProgram = jacksonUtil.getString("siteRegistProgram");
				String indexInitCode = jacksonUtil.getString("indexInitCode");
				String loginInitCode = jacksonUtil.getString("loginInitCode");

				PsTzSitemDefnTWithBLOBs psTzSitemDefnT = new PsTzSitemDefnTWithBLOBs();
				psTzSitemDefnT.setTzSitemId(siteId);
				psTzSitemDefnT.setTzJgbh("");
				psTzSitemDefnT.setTzSiteLang(siteLanguage);
				psTzSitemDefnT.setTzSitemName(siteName);
				psTzSitemDefnT.setTzSitemEnable(enabled);
				psTzSitemDefnT.setTzSitemDescr(siteIntroduce);
				psTzSitemDefnT.setTzImgStor(picPrefix);
				psTzSitemDefnT.setTzImgView(staPrefix);
				psTzSitemDefnT.setTzAttsStor(attPrefix);
				psTzSitemDefnT.setTzAttsView(attAccessPrefix);
				psTzSitemDefnT.setTzVideoStor(viewPrefix);
				psTzSitemDefnT.setTzVideoView(viewAccessPrefix);
				psTzSitemDefnT.setTzSkinStor(skinAddress);
				psTzSitemDefnT.setTzHomeHandPro(siteHomeProgram);
				psTzSitemDefnT.setTzLoginHandPro(siteLoginProgram);
				psTzSitemDefnT.setTzRegisHandPro(siteRegistProgram);
				psTzSitemDefnT.setTzIndexInitcode(indexInitCode);
				psTzSitemDefnT.setTzLonginInitcode(loginInitCode);
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzSitemDefnT.setTzLastmantOprid(oprid);
				psTzSitemDefnT.setTzLastmantDttm(new Date());
				int i = psTzSitemDefnTMapper.updateByPrimaryKeySelective(psTzSitemDefnT);
				if (i > 0) {
					returnJsonMap.replace("siteId", siteId);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "站点信息更新保存失败";
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

				PsTzSitemDefnTWithBLOBs psTzSitemDefnT = psTzSitemDefnTMapper.selectByPrimaryKey(siteId);

				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("siteId", siteId);
				jsonMap.put("siteName", psTzSitemDefnT.getTzSitemName());
				jsonMap.put("enabled", psTzSitemDefnT.getTzSitemEnable());
				jsonMap.put("siteIntroduce", psTzSitemDefnT.getTzSitemDescr());
				jsonMap.put("picPrefix", psTzSitemDefnT.getTzImgStor());
				jsonMap.put("staPrefix", psTzSitemDefnT.getTzImgView());
				jsonMap.put("attPrefix", psTzSitemDefnT.getTzAttsStor());
				jsonMap.put("attAccessPrefix", psTzSitemDefnT.getTzAttsView());
				jsonMap.put("viewPrefix", psTzSitemDefnT.getTzVideoStor());
				jsonMap.put("viewAccessPrefix", psTzSitemDefnT.getTzVideoView());
				jsonMap.put("skinAddress", psTzSitemDefnT.getTzSkinStor());
				jsonMap.put("siteHomeProgram", psTzSitemDefnT.getTzHomeHandPro());
				jsonMap.put("siteLoginProgram", psTzSitemDefnT.getTzLoginHandPro());
				jsonMap.put("siteRegistProgram", psTzSitemDefnT.getTzRegisHandPro());
				jsonMap.put("siteLanguage", psTzSitemDefnT.getTzSiteLang());
				jsonMap.put("indexInitCode", psTzSitemDefnT.getTzIndexInitcode());
				jsonMap.put("loginInitCode", psTzSitemDefnT.getTzLonginInitcode());

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
					strRet = this.querySkinList(comParams, numLimit, numStart, errorMsg);
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
						for(i = 0; i<list.size();i++){
							String skinId = (String) list.get(i).get("skinId");
							PsTzSitemSkinTKey psTzSitemSkinTKey= new PsTzSitemSkinTKey();
							psTzSitemSkinTKey.setTzSitemId(siteId);
							psTzSitemSkinTKey.setTzSkinId(skinId);
							psTzSitemSkinTMapper.deleteByPrimaryKey(psTzSitemSkinTKey);
						}
						break;
					// 站点模板集合;
					case 2:
						for(i = 0; i<list.size();i++){
							String templateId = (String) list.get(i).get("templateId");
							PsTzSitemTempTKey psTzSitemTempTKey= new PsTzSitemTempTKey();
							psTzSitemTempTKey.setTzSitemId(siteId);
							psTzSitemTempTKey.setTzTempId(templateId);
							psTzSitemTempTMapper.deleteByPrimaryKey(psTzSitemTempTKey);
						}
						break;
					// 站点栏目集合;
					case 3:
						for(i = 0; i<list.size();i++){
							String lm_id = (String) list.get(i).get("lm_id");
							PsTzSitemColuTKey psTzSitemColuTKey = new PsTzSitemColuTKey();
							psTzSitemColuTKey.setTzSitemId(siteId);
							psTzSitemColuTKey.setTzColuId(lm_id);
							psTzSitemColuTMapper.deleteByPrimaryKey(psTzSitemColuTKey);
						}
						break;
					// 站点区域集合
					case 4:
						for(i = 0; i<list.size();i++){
							String areaid = (String) list.get(i).get("areaid");
							PsTzSitemAreaTKey psTzSitemAreaTKey = new PsTzSitemAreaTKey();
							psTzSitemAreaTKey.setTzSitemId(siteId);
							psTzSitemAreaTKey.setTzAreaId(areaid);
							psTzSitemAreaTMapper.deleteByPrimaryKey(psTzSitemAreaTKey);
						}
						break;
					// 站点菜单集合
					case 5:
						for(i = 0; i<list.size();i++){
							String menuid = (String) list.get(i).get("menuid");
							PsTzSitemMenuTKey psTzSitemMenuTKey = new PsTzSitemMenuTKey();
							psTzSitemMenuTKey.setTzSitemId(siteId);
							psTzSitemMenuTKey.setTzMenuId(menuid);
							psTzSitemMenuTMapper.deleteByPrimaryKey(psTzSitemMenuTKey);
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

	/*皮肤设置列表*/
	public String querySkinList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
		returnJsonMap.put("root",arraylist );
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String siteId = jacksonUtil.getString("siteId");
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_SKIN_T WHERE TZ_SITEM_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT A.TZ_SKIN_ID,B.TZ_ZHZ_DMS TZ_SKIN_STATE,A.TZ_SKIN_NAME,A.TZ_SKIN_CODE FROM PS_TZ_SITEM_SKIN_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_SKIN_STATE' AND TZ_EFF_STATUS='A') B on A.TZ_SKIN_STATE = B.TZ_ZHZ_ID WHERE TZ_SITEM_ID=? limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT A.TZ_SKIN_ID,B.TZ_ZHZ_DMS TZ_SKIN_STATE,A.TZ_SKIN_NAME,A.TZ_SKIN_CODE FROM PS_TZ_SITEM_SKIN_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_SKIN_STATE' AND TZ_EFF_STATUS='A') B on A.TZ_SKIN_STATE = B.TZ_ZHZ_ID WHERE TZ_SITEM_ID=?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}

			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("skinId", list.get(i).get("TZ_SKIN_ID"));
					jsonMap.put("skinName", list.get(i).get("TZ_SKIN_NAME"));
					jsonMap.put("skinStatus", list.get(i).get("TZ_SKIN_STATE"));
					jsonMap.put("skinCode", list.get(i).get("TZ_SKIN_CODE"));
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
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_TEMP_T WHERE TZ_SITEM_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT A.TZ_TEMP_ID,A.TZ_TEMP_STATE,A.TZ_TEMP_NAME,B.TZ_ZHZ_DMS TZ_TEMP_TYPE,A.TZ_TEMP_PCCODE,A.TZ_TEMP_MSCODE FROM PS_TZ_SITEM_TEMP_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_TEMP_TYPE' AND TZ_EFF_STATUS='A') B on A.TZ_TEMP_TYPE = B.TZ_ZHZ_ID WHERE TZ_SITEM_ID=? limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT A.TZ_TEMP_ID,A.TZ_TEMP_STATE,A.TZ_TEMP_NAME,B.TZ_ZHZ_DMS TZ_TEMP_TYPE,A.TZ_TEMP_PCCODE,A.TZ_TEMP_MSCODE FROM PS_TZ_SITEM_TEMP_T A left join (SELECT TZ_ZHZ_ID, TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_TEMP_TYPE' AND TZ_EFF_STATUS='A') B on A.TZ_TEMP_TYPE = B.TZ_ZHZ_ID WHERE TZ_SITEM_ID=?";
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
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_COLU_T WHERE TZ_SITEM_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT TZ_COLU_ID,TZ_COLU_NAME,TZ_COLU_TYPE,TZ_CONT_TYPE, TZ_ART_TYPE_ID FROM PS_TZ_SITEM_COLU_T where TZ_SITEM_ID=? limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT TZ_COLU_ID,TZ_COLU_NAME,TZ_COLU_TYPE,TZ_CONT_TYPE, TZ_ART_TYPE_ID FROM PS_TZ_SITEM_COLU_T where TZ_SITEM_ID=?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}

			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					String zhzSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'";
					String zhzSQL1="SELECT TZ_ART_TYPE_NAME FROM PS_TZ_ART_TYPE_VW WHERE TZ_ART_TYPE_ID=?";
					String lm_lx = (String) list.get(i).get("TZ_COLU_TYPE");
					lm_lx = jdbcTemplate.queryForObject(zhzSQL, new Object[]{"TZ_ZDLM_LX",lm_lx},"String");
					//String lm_nrlx = (String) list.get(i).get("TZ_CONT_TYPE");
					
					String lm_nrlx = (String) list.get(i).get("TZ_ART_TYPE_ID");
					System.out.println(lm_nrlx);
					
					//修改添加 活动类型
					lm_nrlx = jdbcTemplate.queryForObject(zhzSQL1, new Object[]{lm_nrlx},"String");
					System.out.println(lm_nrlx);
					jsonMap.put("lm_id", list.get(i).get("TZ_COLU_ID"));
					jsonMap.put("lm_name", list.get(i).get("TZ_COLU_NAME"));
					jsonMap.put("lm_lx", lm_lx);
					jsonMap.put("lm_nrlx", lm_nrlx);
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
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_AREA_T WHERE TZ_SITEM_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT TZ_AREA_ID,TZ_AREA_NAME,TZ_AREA_TYPE_ID,TZ_AREA_POSITION FROM PS_TZ_SITEM_AREA_T WHERE TZ_SITEM_ID=? ORDER BY TZ_AREA_ID ASC limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT TZ_AREA_ID,TZ_AREA_NAME,TZ_AREA_TYPE_ID,TZ_AREA_POSITION FROM PS_TZ_SITEM_AREA_T WHERE TZ_SITEM_ID=? ORDER BY TZ_AREA_ID ASC";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}

			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					String zhzSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'";
					String strAreaPosition = (String) list.get(i).get("TZ_AREA_POSITION");
					strAreaPosition = jdbcTemplate.queryForObject(zhzSQL, new Object[]{"TZ_AREA_POSITION",strAreaPosition},"String");
					
					String strAreaTypeId = (String) list.get(i).get("TZ_AREA_TYPE_ID");
					String areaTypeNmaeSQL = "select TZ_AREA_TYPE_NAME from PS_TZ_SITEM_ATYP_T where TZ_SITEM_ID=? and TZ_AREA_TYPE_ID=?";
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
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_MENU_T WHERE TZ_SITEM_ID=?";
			int total = jdbcTemplate.queryForObject(totalSQL, new Object[] { siteId }, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if (numLimit > 0) {
				sql = "SELECT TZ_MENU_ID,TZ_MENU_NAME,TZ_MENU_TYPE_ID FROM PS_TZ_SITEM_MENU_T WHERE TZ_SITEM_ID=? ORDER BY TZ_MENU_ID ASC limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId, numStart, numLimit });
			} else {
				sql = "SELECT TZ_MENU_ID,TZ_MENU_NAME,TZ_MENU_TYPE_ID FROM PS_TZ_SITEM_MENU_T WHERE TZ_SITEM_ID=? ORDER BY TZ_MENU_ID ASC";
				list = jdbcTemplate.queryForList(sql, new Object[] { siteId });
			}

			
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> jsonMap = new HashMap<String, Object>();

					String strMenuTypeId = (String) list.get(i).get("TZ_MENU_TYPE_ID");
					String menuTypeNmaeSQL = "select TZ_MENU_TYPE_NAME from PS_TZ_SITEM_MTYP_T where TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=?";
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
