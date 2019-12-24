package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcBgtKxzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcBgtZwtTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcXxJygzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcXxxKxzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcXxxPzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtZwtT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxJygzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxPzTWithBLOBs;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComDyTMapper;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyTWithBLOBs;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyEngMapper;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyTMapper;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEng;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEngKey;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author caoy
 * @version 创建时间：2016年8月2日 下午5:45:07 类说明 报名表模板编辑引擎程序
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.TempEditorEngineImpl")
public class TempEditorEngineImpl {

	@Autowired
	private PsTzDcDyTMapper psTzDcDyTMapper;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private PsTzJygzDyEngMapper psTzJygzDyEngMapper;

	@Autowired
	private PsTzDcBgtKxzTMapper psTzDcBgtKxzTMapper;
	@Autowired
	private PsTzDcXxxKxzTMapper psTzDcXxxKxzTMapper;

	@Autowired
	private PsTzDcXxJygzTMapper psTzDcXxJygzTMapper;

	@Autowired
	private PsTzDcBgtZwtTMapper psTzDcBgtZwtTMapper;

	@Autowired
	private PsTzDcXxxPzTMapper psTzDcXxxPzTMapper;

	@Autowired
	private PsTzComDyTMapper psTzComDyTMapper;

	@Autowired
	private PsTzJygzDyTMapper psTzJygzDyTMapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private GdKjComServiceImpl gdKjComService;

	@Autowired
	private TZGDObject tzGdObject;

	/* 获取模板设计页面HTML */
	public String init(String tplId, String insId) {
		System.out.println("==engine==init执行");
		JacksonUtil jacksonUtil = new JacksonUtil();
		String tplName = "";
		String language = "";
		String sql = "SELECT TZ_APP_TPL_MC,TZ_APP_TPL_LAN FROM PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID = ?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { tplId });
		if (map != null) {
			tplName = map.get("TZ_APP_TPL_MC") == null?"":String.valueOf(map.get("TZ_APP_TPL_MC"));
			language = map.get("TZ_APP_TPL_LAN") == null ? "" : String.valueOf(map.get("TZ_APP_TPL_LAN"));
		}
		
		////// logger.info("tplName=" + tplName);
		// 1、读取控件注册信息，显示报名表模板编辑页面左侧工具条;
		String componentData = this.getComponentData(tplId);

		/*
		 * 2、读取报名表模板配置信息，若有配置信息，则根据控件的JS类路径加载JS文件（存在则不再重复加载），把控件类存储到类管理器中，
		 * 并调用类的初始化方法_init， 控件类调用中_gethtml生成控件实例,传递参数
		 * id、data（控件的属性数据集,包含配置数据），将生成的控件实例存入类实例管理器中（_data）
		 * 
		 * 3、点击、拖拽报名表左侧工具条中的控件按钮，加载JS类、生成实例
		 * 
		 * 4、点击右侧区域的控件实例，调用其类中的_edit方法，传递参数 id、data参数,显示编辑页面；
		 * 
		 * 5、控件编辑页面修改字段的值后，需要将值立即同步到类实例对象中 若属性字段为level0级别的，则属性编辑框需要添加键盘事件
		 * onkeyup=“SurveyBuild.saveAttr('id','attrName',val)”；
		 * 若属性字段为level1级别的字段，则属性编辑框需要添加键盘事件
		 * onkeyup=“SurveyBuild.saveLevel1Attr('id','attrName1'，'attrName2',val)
		 * ”；
		 * 若上述两个字段不能满足赋值要求，则可调用SurveyBuild.getComponentInstance(ID)获取控件实例，在进行赋值
		 * 若属性字段值修改后需要同步修改右侧控件预览信息，则需要控件自行实现
		 * 
		 * 6、离开当前编辑页面（添加控件实例、修改其他控件实例）、保存当前模板时，需调用_validator方法校验当前编辑的控件是否校验通过，
		 * 若不通过则不能进行当前操作；
		 * 
		 * 7、校验规则的列表、设置页面、设置页面参数设置到_data中、校验列表页面参数赋值到_data中;
		 * 
		 * 8、保存当前控件时会把类实例对象中的_data数据集传递到后台进行保存;
		 */

		String contextUrl = request.getContextPath();
		String tzGeneralURL = contextUrl + "/dispatcher";

		if (StringUtils.isBlank(language)) {
			language = gdKjComService.getLoginLanguage(request, response);
		}

		// logger.info("language=" + language);
		String msgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response, "TZGD_SURVEY_MSGSET",
				language);

		// logger.info("msgSet=" + msgSet);
		jacksonUtil.json2Map(msgSet);
		if (jacksonUtil.containsKey(language)) {
			Map<String, Object> msgLang = jacksonUtil.getMap(language);
			msgSet = jacksonUtil.Map2json(msgLang);
		}
		System.out.println("temp==msgSet:=="+msgSet);
		String tplHtml = "";
		Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
		Matcher mc = CRLF.matcher(componentData);
		if (mc.find()) {
			componentData = mc.replaceAll("\\\\n");
		}
		componentData = componentData.replace(" ", "");
		componentData = componentData.replace("\\", "\\\\");
		componentData = componentData.replace("$", "\\$");
		//logger.info("componentData=" + componentData);
		componentData = componentData.replaceAll("\\$", "~");
		try {
			// GetHTMLText(HTML.TZ_SURVEY_TEMPLATE_HTML, &strTname, &tplId,
			// TZ_ESCAPE_CHAR(&str_componentData), &tzGeneralURL, &strMsgSet,
			// "TPL");

			tplHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_TEMPLATE_HTML", tplName, tplId,
					componentData, tzGeneralURL, msgSet, "TPL", request.getContextPath(), contextUrl);
			// logger.info("tplHtml=" + tplHtml);
			// tplHtml = tplHtml.replaceAll("\\~", "\\$");
		} catch (TzSystemException e) {
			e.printStackTrace();
			tplHtml = "";
		}
		return tplHtml;
	}

	/*
	 * 获取当前模板对应的控件列表以及模板的基本信息以及信息项
	 */



 String getComponentData(String tplId) {

	 
	 System.out.println("==getComponentData()执行==");
		JacksonUtil jacksonUtil = new JacksonUtil();
		// "%BIND(:1)":[%BIND(:2)]
		ArrayList<Map<String, Object>> comDfn = this.getComDfn(tplId);

		String sqlTplData = "SELECT TZ_APPTPL_JSON_STR FROM PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID = ?";
		String tplData = jdbcTemplate.queryForObject(sqlTplData, new Object[] { tplId }, "String");
		Map<String, Object> comData = new HashMap<String, Object>();
		if (StringUtils.isBlank(tplData)) {
			comData.put("componentInstance", new HashMap<String,Object>());
			System.out.println("===sqlTplData:=="+tplData);
		}
		else{
			jacksonUtil.json2Map(tplData);
			System.out.println("===sqlTplData:=="+tplData);
			comData.put("componentInstance", jacksonUtil.getMap());
		}
		// "%bind(:1)":%bind(:2)
		comData.put("componentDfn", comDfn);
		
		return jacksonUtil.Map2json(comData);
	}

	/*
	 * 获取模板相关控件
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Map<String, Object>> getComDfn(String tplId) {
		System.out.println("==getComDfn()执行==");
		String contextUrl = request.getContextPath();
		// 语言;
		String sqlLang = "SELECT TZ_APP_TPL_LAN FROM PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID = ?";
		String language = jdbcTemplate.queryForObject(sqlLang, new Object[] { tplId }, "String");
		// REM 1、根据模板用途获得控件列表;

		String sqlYt = "SELECT * FROM PS_TZ_COM_YT_T WHERE TZ_COM_YT_ID = ? AND TZ_QY_BZ = 'Y' ORDER BY TZ_ORDER ASC";
		List<Map<String,Object>> resultlist = jdbcTemplate.queryForList(sqlYt, new Object[] { "SRVY" });

		System.out.println("=====resultlist:===");
		System.out.println(new JacksonUtil().List2json((ArrayList<?>) resultlist));
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		String componentId = "";
		PsTzComDyTWithBLOBs psTzComDyT = null;
		String componentName = "";
		String displayArea = "";
		String jsfileUrl = "";
		String[] arrLj = null;
		String className = "";
		String iconPath = "";
		Map<String, Object> mapOptJson = null;
		String sqlRules = "";
		List<?> rulelist = null;
		Map<String, Object> mapRuleRet = null;
		Map<String, Object> rul = null;
		PsTzJygzDyT psTzJygzDyT = null;
		String ruleId = "";
		String ruleName = "";
		String className1 = "";
		String messages = "";
		String isEnable = "";
		Map<String, Object> ruleJson = null;
		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;
			componentId = result.get("TZ_COM_ID") == null ? "" : String.valueOf(result.get("TZ_COM_ID"));
			psTzComDyT = psTzComDyTMapper.selectByPrimaryKey(componentId);
			if (psTzComDyT != null) {
				/* 当前控件校验规则 -- BEGIN */
				componentName = psTzComDyT.getTzComMc();
				displayArea = result.get("TZ_COM_LX_ID") == null ? "" : String.valueOf(result.get("TZ_COM_LX_ID"));
				jsfileUrl = contextUrl + psTzComDyT.getTzComJslj();
				arrLj = jsfileUrl.split("/");
				className = StringUtils.substringBeforeLast(arrLj[arrLj.length - 1], ".js");
				iconPath = contextUrl + psTzComDyT.getTzComIconlj();

				mapOptJson = new HashMap<String, Object>();
				mapOptJson.put("componentId", componentId);
				mapOptJson.put("componentName", componentName);
				mapOptJson.put("displayArea", displayArea);
				mapOptJson.put("jsfileUrl", jsfileUrl);
				mapOptJson.put("className", className);
				mapOptJson.put("iconPath", iconPath);

				// REM 2、获取当前控件的校验规则;
				sqlRules = "SELECT * FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? ORDER BY TZ_ORDER ASC";
				rulelist = jdbcTemplate.queryForList(sqlRules, new Object[] { componentId });
				mapRuleRet = new HashMap<String, Object>();

				for (Object rule : rulelist) {
					rul = (Map<String, Object>) rule;
					ruleId = rul.get("TZ_JYGZ_ID") == null ? "" : String.valueOf(rul.get("TZ_JYGZ_ID"));
					psTzJygzDyT = psTzJygzDyTMapper.selectByPrimaryKey(ruleId);

					ruleName = psTzJygzDyT.getTzJygzMc();
					className1 = psTzJygzDyT.getTzJygzJslmc();
					messages = psTzJygzDyT.getTzJygzTsxx();
					if (!StringUtils.equals(language, "ZHS")) {
						PsTzJygzDyEngKey psTzJygzDyEngKey = new PsTzJygzDyEngKey();
						psTzJygzDyEngKey.setLanguageCd(language);
						psTzJygzDyEngKey.setTzJygzId(ruleId);
						PsTzJygzDyEng psTzJygzDyEng = psTzJygzDyEngMapper.selectByPrimaryKey(psTzJygzDyEngKey);
						messages = psTzJygzDyEng.getTzJygzTsxx();
					}
					isEnable = rul.get("TZ_QY_BZ") == null ? "" : String.valueOf(rul.get("TZ_QY_BZ"));

					ruleJson = new HashMap<String, Object>();
					ruleJson.put("ruleId", ruleId);
					ruleJson.put("ruleName", ruleName);
					ruleJson.put("className", className1);
					ruleJson.put("isEnable", isEnable);
					ruleJson.put("messages", messages);
					mapRuleRet.put(className1, ruleJson);
				}
				mapOptJson.put("rules", mapRuleRet);
				/* 当前控件校验规则 -- END */
				listData.add(mapOptJson);
			}
		}
		System.out.println("==listData:==");
		System.out.println(new JacksonUtil().List2json(listData));
		return listData;
	}

	public String saveTpl(String strTid, Map<String, Object> mapData, String userID, String[] errMsg) {
		System.out.println("==saveTpl()执行==");
		System.out.println("==mapData:=="+new JacksonUtil().Map2json(mapData));
		String strRet = "{}";
		Object o = null;
		JacksonUtil jacksonUtil = new JacksonUtil();
		String successFlag = "0";
		String strMsg = "";
		if (!StringUtils.isBlank(strTid) && mapData != null) {

			/* -------模板基本信息更新------- Begin */
			PsTzDcDyTWithBLOBs psTzDcDyT = new PsTzDcDyTWithBLOBs();

			psTzDcDyT.setTzAppTplId(strTid);

			/* 机构ID */
			if (mapData.containsKey("deptId")) {
				o = mapData.get("deptId");
				psTzDcDyT.setTzJgId(String.valueOf(o));
			}
			/* 模板名称 */
			if (mapData.containsKey("tplName")) {
				o = mapData.get("tplName");
				psTzDcDyT.setTzAppTplMc(String.valueOf(o));
			}
			/* 模板语言 */
			if (mapData.containsKey("lang")) {
				o = mapData.get("lang");
				psTzDcDyT.setTzAppTplLan(String.valueOf(o));
			}
			/* 模板类型 */
			if (mapData.containsKey("tplType")) {
				o = mapData.get("tplType");
				psTzDcDyT.setTzAppTplLx(String.valueOf(o));
			}
			/* 模板报文 */
			psTzDcDyT.setTzApptplJsonStr(jacksonUtil.Map2json(mapData));
			System.out.println("==JsonStr==:"+jacksonUtil.Map2json(mapData));
			/* 卷头内容 */
			if (mapData.containsKey("header")) {
				o = mapData.get("header");
				psTzDcDyT.setTzDcJtnr(String.valueOf(o));
			}
			/* 卷尾内容 */
			if (mapData.containsKey("footer")) {
				o = mapData.get("footer");
				psTzDcDyT.setTzDcJwnr(String.valueOf(o));
			}
			/* 启用状态 */
			if (mapData.containsKey("state")) {
				o = mapData.get("state");
				psTzDcDyT.setTzEffexpZt(String.valueOf(o));
			}

			psTzDcDyT.setRowLastmantDttm(new java.util.Date());

			psTzDcDyT.setRowLastmantOprid(userID);

			int count = psTzDcDyTMapper.updateByPrimaryKeySelective(psTzDcDyT);
			if (count <= 0) {
				errMsg[0] = "1";
				errMsg[1] = "创建新的模板失败！";
				return strRet;
			}
			/*------- 模板基本信息更新 End -------*/

			/*------- 报名表模板信息项维护 Begin -------*/

			Map<String, Object> jsonArray = (Map<String, Object>) mapData.get("items");
			Map<String, Object> dataMap = null;
			String strItemId = null;
			if (jsonArray!=null&&jsonArray.size() > 0) {
				this.delTpl(strTid);
				for (Object itemMap : jsonArray.values()) {
					dataMap = (Map<String, Object>) itemMap;
					// 信息项编号
					if (dataMap.containsKey("itemId")) {
						strItemId = String.valueOf(dataMap.get("itemId"));
					}
					this.savePerXXX(strTid, dataMap);
				}

			}

			/*-- 报名表模板信息项维护 End --*/
		}

		String str_alter_message = "";

		// {
		// "code":"%BIND(:1)",
		// "msg":"%bind(:2)",
		// "insid":"%bind(:3)",
		// "pageCompleteState":[%bind(:4)],
		// "alterMsg":"%bind(:5)",
		// "appInsVersionId":"%bind(:6)"
		// }
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("code", successFlag);
		mapRet.put("msg", strMsg);
		mapRet.put("insid", "");
		mapRet.put("pageCompleteState", "");
		mapRet.put("alterMsg", str_alter_message);
		mapRet.put("appInsVersionId", "");
		return jacksonUtil.Map2json(mapRet);
		// Return GetHTMLText(HTML.TZ_RES_HTML, &successFlag,
		// TZ_ESCAPE_CHAR(&strMsg), "", "", &str_alter_message);

	}

	/*
	 * 保存信息项
	 */
	private void savePerXXX(String m_tplId, Map<String, Object> mapData) {
		System.out.println("==savePerXXX()方法执行==");
		System.out.println("==mapData:=="+new JacksonUtil().Map2json(mapData));
		PsTzDcXxxPzTWithBLOBs psTzDcXxxPzT = new PsTzDcXxxPzTWithBLOBs();
		/* 模板编号 */
		psTzDcXxxPzT.setTzAppTplId(m_tplId);

		Object o = null;
		/* 信息项编号 */
		if (mapData.containsKey("itemId")) {
			o = mapData.get("itemId");
			psTzDcXxxPzT.setTzXxxBh(String.valueOf(o));
		}

		/* 信息项实例ID */
		if (mapData.containsKey("instanceId")) {
			o = mapData.get("instanceId");
			psTzDcXxxPzT.setTzXxxSlid(String.valueOf(o));
		}

		/* 信息项名称 */
		if (mapData.containsKey("itemName") && mapData.get("itemName") != null) {
			o = mapData.get("itemName");
			psTzDcXxxPzT.setTzXxxMc(String.valueOf(o));
		}

		/* 信息项标题(富文本) */
		if (mapData.containsKey("title") && mapData.get("title") != null) {
			o = mapData.get("title");
			psTzDcXxxPzT.setTzTitle(String.valueOf(o));
		}

		/* 题号 */
		if (mapData.containsKey("qCode") && mapData.get("qCode") != null) {
			o = mapData.get("qCode");
			psTzDcXxxPzT.setTzXxxQid(String.valueOf(o));
		}

		/* 排序序号 */
		if (mapData.containsKey("orderby") && mapData.get("orderby") != null) {
			o = mapData.get("orderby");
			psTzDcXxxPzT.setTzOrder(new Integer(String.valueOf(o)));
		}

		/* 分页号 */
		if (mapData.containsKey("pageno") && mapData.get("pageno") != null) {
			o = mapData.get("pageno");
			psTzDcXxxPzT.setTzPageNo(new Integer(String.valueOf(o)));
		}

		/* 控件类名称 */
		if (mapData.containsKey("classname") && mapData.get("classname") != null) {
			o = mapData.get("classname");
			psTzDcXxxPzT.setTzComLmc(String.valueOf(o));
		}

		/* 空值提示信息 */
		if (mapData.containsKey("emptyText") && mapData.get("emptyText") != null) {
			o = mapData.get("emptyText");
			psTzDcXxxPzT.setTzXxxKztsxx(String.valueOf(o));
		}

		/* 焦点提示信息 */
		if (mapData.containsKey("onFoucsMessage") && mapData.get("onFoucsMessage") != null) {
			o = mapData.get("onFoucsMessage");
			psTzDcXxxPzT.setTzXxxJdtsxx(String.valueOf(o));
		}

		/* 默认值 */
		if (mapData.containsKey("defaultval") && mapData.get("defaultval") != null) {
			o = mapData.get("defaultval");
			psTzDcXxxPzT.setTzXxxMrz(String.valueOf(o));
		}

		/* 后缀 */
		if (mapData.containsKey("suffix") && mapData.get("suffix") != null) {
			o = mapData.get("suffix");
			psTzDcXxxPzT.setTzXxxHz(String.valueOf(o));
		}

		/* 后缀链接 */
		if (mapData.containsKey("suffixUrl") && mapData.get("suffixUrl") != null) {
			o = mapData.get("suffixUrl");
			psTzDcXxxPzT.setTzXxxHzlj(String.valueOf(o));
		}

		/* 存储类型 */
		if (mapData.containsKey("StorageType") && mapData.get("StorageType") != null) {
			o = mapData.get("StorageType");
			psTzDcXxxPzT.setTzXxxCclx(String.valueOf(o));
		}

		/* 文本框大小 */
		if (mapData.containsKey("boxSize") && mapData.get("boxSize") != null) {
			o = mapData.get("boxSize");
			psTzDcXxxPzT.setTzXxxWbkdx(String.valueOf(o));
		}

		/* 日期格式 */
		if (mapData.containsKey("dateformate") && mapData.get("dateformate") != null) {
			o = mapData.get("dateformate");
			psTzDcXxxPzT.setTzXxxRqgs(String.valueOf(o));
		}

		/* 年份最小值 */
		if (mapData.containsKey("minYear") && mapData.get("minYear") != null) {
			o = mapData.get("minYear");
			psTzDcXxxPzT.setTzXxxNfmin(String.valueOf(o));
		}

		/* 年份最大值 */
		if (mapData.containsKey("maxYear") && mapData.get("maxYear") != null) {
			o = mapData.get("maxYear");
			psTzDcXxxPzT.setTzXxxNfmax(String.valueOf(o));
		}

		/* 单选、多选排列方式 */
		if (mapData.containsKey("plfs") && mapData.get("plfs") != null) {
			o = mapData.get("plfs");
			psTzDcXxxPzT.setTzXxxPlfs(String.valueOf(o));
		}

		/* 多选最少选择个数 */
		if (mapData.containsKey("minSelect") && mapData.get("minSelect") != null) {
			o = mapData.get("minSelect");
			psTzDcXxxPzT.setTzXxxZsxzgs(new Short(String.valueOf(o)));
		}

		/* 多选最多选择个数 */
		if (mapData.containsKey("maxSelect") && mapData.get("maxSelect") != null) {
			o = mapData.get("maxSelect");
			psTzDcXxxPzT.setTzXxxZdxzgs(new Short(String.valueOf(o)));
		}

		/* 允许上传类型 */
		if (mapData.containsKey("yxsclx") && mapData.get("yxsclx") != null) {
			o = mapData.get("yxsclx");
			psTzDcXxxPzT.setTzXxxYxsclx(String.valueOf(o));
		}

		/* 允许上传大小 */
		if (mapData.containsKey("yxscdx") && mapData.get("yxscdx") != null) {
			o = mapData.get("yxscdx");
			psTzDcXxxPzT.setTzXxxYxscdx(new Short(String.valueOf(o)));
		}

		/* 允许多附件上传 */
		if (mapData.containsKey("allowMultiAtta") && mapData.get("allowMultiAtta") != null) {
			o = mapData.get("allowMultiAtta");
			psTzDcXxxPzT.setTzXxxMulti(String.valueOf(o));
		}

		/* 图片是否裁剪 */
		if (mapData.containsKey("tpsfcj") && mapData.get("tpsfcj") != null) {
			o = mapData.get("tpsfcj");
			psTzDcXxxPzT.setTzXxxSfcj(String.valueOf(o));
		}

		/* 图片裁剪类型 */
		if (mapData.containsKey("tpcjlx") && mapData.get("tpcjlx") != null) {
			o = mapData.get("tpcjlx");
			psTzDcXxxPzT.setTzXxxTpcjlx(String.valueOf(o));
		}

		/* 文字说明 */
		if (mapData.containsKey("wzsm") && mapData.get("wzsm") != null) {
			o = mapData.get("wzsm");
			psTzDcXxxPzT.setTzXxxWzsm(String.valueOf(o));
		}

		/* 是否必填 */
		if (mapData.containsKey("isRequire") && mapData.get("isRequire") != null) {
			o = mapData.get("isRequire");
			psTzDcXxxPzT.setTzXxxBtBz(String.valueOf(o));
		}

		/* 是否字数限制 */
		if (mapData.containsKey("isCheckStrLen") && mapData.get("isCheckStrLen") != null) {
			o = mapData.get("isCheckStrLen");
			psTzDcXxxPzT.setTzXxxCharBz(String.valueOf(o));
		}

		/* 最小长度 */
		if (mapData.containsKey("minLen") && mapData.get("minLen") != null) {
			o = mapData.get("minLen");
			psTzDcXxxPzT.setTzXxxMinlen(new Integer(String.valueOf(o)));
		}

		/* 最大长度 */
		if (mapData.containsKey("maxLen") && mapData.get("maxLen") != null) {
			o = mapData.get("maxLen");
			psTzDcXxxPzT.setTzXxxMaxlen(new Integer(String.valueOf(o)));
		}

		/* 是否限制数字范围 */
		if (mapData.containsKey("isNumSize") && mapData.get("isNumSize") != null) {
			o = mapData.get("isNumSize");
			psTzDcXxxPzT.setTzXxxNumBz(String.valueOf(o));
		}

		/* 最小值 */
		if (mapData.containsKey("min") && mapData.get("min") != null) {
			o = mapData.get("min");
			psTzDcXxxPzT.setTzXxxMin(new Long(String.valueOf(o)));
		}

		/* 最大值 */
		if (mapData.containsKey("max") && mapData.get("max") != null) {
			o = mapData.get("max");
			psTzDcXxxPzT.setTzXxxMax(new Long(String.valueOf(o)));
		}

		/* 小数位数 */
		if (mapData.containsKey("digits") && mapData.get("digits") != null) {
			o = mapData.get("digits");
			psTzDcXxxPzT.setTzXxxXsws(new Short(String.valueOf(o)));
		}

		/* 固定规则校验 */
		if (mapData.containsKey("preg") && mapData.get("preg") != null) {
			o = mapData.get("preg");
			psTzDcXxxPzT.setTzXxxGdgsjy(String.valueOf(o));
		}

		/* 是否多行容器 */
		if (mapData.containsKey("isDoubleLine") && mapData.get("isDoubleLine") != null) {
			o = mapData.get("isDoubleLine");
			psTzDcXxxPzT.setTzXxxDrqBz(String.valueOf(o));
		}

		/* 最小行记录数 */
		if (mapData.containsKey("minLines") && mapData.get("minLines") != null) {
			o = mapData.get("minLines");
			psTzDcXxxPzT.setTzXxxMinLine(new Short(String.valueOf(o)));
		}

		/* 最大行记录数 */
		if (mapData.containsKey("maxLines") && mapData.get("maxLines") != null) {
			o = mapData.get("maxLines");
			psTzDcXxxPzT.setTzXxxMaxLine(new Short(String.valueOf(o)));
		}

		/* 是否为单行组合 */
		if (mapData.containsKey("isSingleLine") && mapData.get("isSingleLine") != null) {
			o = mapData.get("isSingleLine");
			psTzDcXxxPzT.setTzXxxSrqBz(String.valueOf(o));
		}

		/* 外网是否可下载 */
		if (mapData.containsKey("isDownLoad") && mapData.get("isDownLoad") != null) {
			o = mapData.get("isDownLoad");
			psTzDcXxxPzT.setTzIsDownload(String.valueOf(o));
		}

		/* 是否显示在报名表审核 */
		if (mapData.containsKey("isShow") && mapData.get("isShow") != null) {
			o = mapData.get("isShow");
			psTzDcXxxPzT.setTzIsShow(String.valueOf(o));
		}

		/* 子问题横向排列 */
		if (mapData.containsKey("inth") && mapData.get("inth") != null) {
			o = mapData.get("inth");
			psTzDcXxxPzT.setTzSubQsInth(String.valueOf(o));
		}

		/* 每列/行只能选择一个 */
		if (mapData.containsKey("oneChoice") && mapData.get("oneChoice") != null) {
			o = mapData.get("oneChoice");
			psTzDcXxxPzT.setTzIsOnechoice(String.valueOf(o));
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		/*------- 是否Option Begin -------*/
		Map<String, Object> jsonArray = (Map<String, Object>) mapData.get("option");
		if (jsonArray != null && jsonArray.size() > 0) {
			for (Object optionMapValue : jsonArray.values()) {
				Map<String, Object> listData = (Map<String, Object>) optionMapValue;
				if ("T".equals(psTzDcXxxPzT.getTzXxxCclx())) {
					PsTzDcBgtKxzT recBGTKXZ = new PsTzDcBgtKxzT();
					/* 模板编号 */
					recBGTKXZ.setTzAppTplId(m_tplId);
					/* 信息项编号 */
					if (mapData.containsKey("itemId")) {
						recBGTKXZ.setTzXxxBh(String.valueOf(mapData.get("itemId")));
					}

					/* 可选值编码 */
					if (listData.containsKey("code") && listData.get("code") != null) {
						o = listData.get("code");
						recBGTKXZ.setTzOptCode(String.valueOf(o));
					}

					/* 排序序号 */
					if (listData.containsKey("orderby") && listData.get("orderby") != null) {
						o = listData.get("orderby");
						recBGTKXZ.setTzOrder(Integer.valueOf((String.valueOf(o))));
					}

					/* 可选值描述 */
					if (listData.containsKey("txt") && listData.get("txt") != null) {
						o = listData.get("txt");
						recBGTKXZ.setTzOptName(String.valueOf(o));
					}

					/* 权重 */
					if (listData.containsKey("weight") && listData.get("weight") != null) {
						o = listData.get("weight");
						recBGTKXZ.setTzWeight(new BigDecimal(String.valueOf(o)));
					}
					//psTzDcBgtKxzTMapper.updateByPrimaryKey(recBGTKXZ);
					psTzDcBgtKxzTMapper.insertSelective(recBGTKXZ);
				} else {
					PsTzDcXxxKxzT recKXZ = new PsTzDcXxxKxzT();

					/* 报名表模板编号 */
					recKXZ.setTzAppTplId(m_tplId);

					/* 信息项编号 */
					if (mapData.containsKey("itemId")) {
						recKXZ.setTzXxxBh(String.valueOf(mapData.get("itemId")));
					}

					/* 可选值编码 */
					if (listData.containsKey("code") && listData.get("code") != null) {
						o = listData.get("code");
						recKXZ.setTzXxxkxzMc(String.valueOf(o));
					}

					/* 排序序号 */
					if (listData.containsKey("orderby") && listData.get("orderby") != null) {
						o = listData.get("orderby");
						recKXZ.setTzOrder(new Integer(String.valueOf(o)));
					}

					/* 可选值描述 */
					if (listData.containsKey("txt") && listData.get("txt") != null) {
						o = listData.get("txt");
						recKXZ.setTzXxxkxzMs(String.valueOf(o));
					}

					/* 是否为默认 */
					if (listData.containsKey("defaultval") && listData.get("defaultval") != null) {
						o = listData.get("defaultval");
						recKXZ.setTzKxzMrzBz(String.valueOf(o));
					}

					/* 是否为其他 */
					if (listData.containsKey("other") && listData.get("other") != null) {
						o = listData.get("other");
						recKXZ.setTzKxzQtBz(String.valueOf(o));
					}

					/* 权重 */
					if (listData.containsKey("weight") && listData.get("weight") != null) {
						o = listData.get("weight");
						recKXZ.setTzXxxkxzQz(new BigDecimal(String.valueOf(o)));
					}

					psTzDcXxxKxzTMapper.insert(recKXZ);
				}
			}

		}

		/*------- 是否Option End -------*/

		/*------- 是否Events Begin -------*/
		jsonArray = (Map<String, Object>) mapData.get("rules");
		if (jsonArray != null && jsonArray.size() > 0) {
			for (Object ruleMap : jsonArray.values()) {
				Map<String, Object> listData = (Map<String, Object>) ruleMap;
				PsTzDcXxJygzT recJYGZ = new PsTzDcXxJygzT();
				/* 模板编号 */
				recJYGZ.setTzAppTplId(m_tplId);

				/* 信息项编号 */
				if (mapData.containsKey("itemId")) {
					recJYGZ.setTzXxxBh(String.valueOf(mapData.get("itemId")));
				}

				/* 校验规则ID */
				if (listData.containsKey("ruleId") && listData.get("ruleId") != null) {
					o = listData.get("ruleId");
					recJYGZ.setTzJygzId(String.valueOf(o));
				}

				/* 是否启用 */
				if (listData.containsKey("isEnable") && listData.get("isEnable") != null) {
					o = listData.get("isEnable");
					recJYGZ.setTzQyBz(String.valueOf(o));
				}

				/* 提示信息 */
				if (listData.containsKey("messages") && listData.get("messages") != null) {
					o = listData.get("messages");
					recJYGZ.setTzJygzTsxx(String.valueOf(o));
				}
				System.out.println("m_tplId:"+m_tplId+"==itemId:"+mapData.get("itemId")+"==ruleId:"+listData.get("ruleId"));
				//psTzDcXxJygzTMapper.updateByPrimaryKey(recJYGZ);
				psTzDcXxJygzTMapper.insert(recJYGZ);
			}
		}

		/*------- 是否Events End -------*/

		/*------- 是否表格题子问题 Begin -------*/
		jsonArray = (Map<String, Object>) mapData.get("child");
		if (jsonArray != null && jsonArray.size() > 0) {
			for (Object childMap : jsonArray.values()) {
				Map<String, Object> listData = (Map<String, Object>) childMap;
				PsTzDcBgtZwtT recBGTZWT = new PsTzDcBgtZwtT();
				/* 模板编号 */
				recBGTZWT.setTzAppTplId(m_tplId);

				/* 信息项编号 */
				if (mapData.containsKey("itemId")) {
					recBGTZWT.setTzXxxBh(String.valueOf(mapData.get("itemId")));
				}

				/* 问题编码 */
				if (listData.containsKey("sqCode") && listData.get("sqCode") != null) {
					o = listData.get("sqCode");
					recBGTZWT.setTzQuCode(String.valueOf(o));
				}

				/* 排序序号 */
				if (listData.containsKey("orderby") && listData.get("orderby") != null) {
					o = listData.get("orderby");
					recBGTZWT.setTzOrder(new Integer(String.valueOf(o)));
				}

				/* 子问题名称 */
				if (listData.containsKey("question") && listData.get("question") != null) {
					o = listData.get("question");
					recBGTZWT.setTzQuName(String.valueOf(o));
				}

				/* 简称 */
				if (listData.containsKey("shortDesc") && listData.get("shortDesc") != null) {
					o = listData.get("shortDesc");
					recBGTZWT.setTzShortName(String.valueOf(o));
				}

				/* 权重 */
				if (listData.containsKey("weight") && listData.get("weight") != null) {
					o = listData.get("weight");
					recBGTZWT.setTzWeight(new BigDecimal(String.valueOf(o)));
				}
				//psTzDcBgtZwtTMapper.updateByPrimaryKey(recBGTZWT);
				psTzDcBgtZwtTMapper.insert(recBGTZWT);
			}

		}

		/*------- 是否表格题子问题 End -------*/

		psTzDcXxxPzTMapper.insert(psTzDcXxxPzT);

	}

	@Transactional
	private boolean delTpl(String tplId) {
		try {
			if (!StringUtils.isBlank(tplId)) {
				/* 在线调查模板信息项配置表 */
				System.out.println("===delTpl()方法实执行==");
				Object[] args = new Object[] { tplId };
				String sql = "DELETE FROM PS_TZ_DC_XXXPZ_T WHERE TZ_APP_TPL_ID = ? ";
				jdbcTemplate.update(sql, args);

				/* 在线调查模板信息项可选值定义表 */
				sql = "DELETE FROM PS_TZ_DC_XXX_KXZ_T WHERE TZ_APP_TPL_ID = ? ";
				jdbcTemplate.update(sql, args);

				/* 在线调查模板表格选择题子问题定义表 */
				sql = "DELETE FROM PS_TZ_DC_BGT_ZWT_T WHERE TZ_APP_TPL_ID = ? ";
				jdbcTemplate.update(sql, args);

				/* 在线调查模板表格选择题可选定义表 */
				sql = "DELETE FROM PS_TZ_DC_BGT_KXZ_T WHERE TZ_APP_TPL_ID = ? ";
				jdbcTemplate.update(sql, args);

				/* 在线调查模板信息项校验规则表 */
				sql = "DELETE FROM PS_TZ_DC_XX_JYGZ_T WHERE TZ_APP_TPL_ID = ? ";
				jdbcTemplate.update(sql, args);

				/* 在线调查模板表格选择题可选定义表 */
				sql = "DELETE FROM PS_TZ_RQ_XXXPZ_T WHERE TZ_APP_TPL_ID = ? ";
				jdbcTemplate.update(sql, args);
				return true;
			} else {
				System.out.println("tplId is null!");
				return false;
			}
		} catch (Exception e) {
			System.out.println("delete error:");
			e.printStackTrace();
			return false;
		}
	}
}
