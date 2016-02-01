package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZControlSetBundle.dao.PsTzComDyTMapper;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyT;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyEngMapper;
import com.tranzvision.gd.TZRuleSetBundle.dao.PsTzJygzDyTMapper;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEng;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEngKey;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service
public class TemplateEngine {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private PsTzJygzDyTMapper psTzJygzDyTMapper;

	@Autowired
	private PsTzJygzDyEngMapper psTzJygzDyEngMapper;

	@Autowired
	private PsTzApptplDyTMapper psTzApptplDyTMapper;

	@Autowired
	private PsTzComDyTMapper psTzComDyTMapper;

	@Autowired
	private TZGDObject tzGdObject;

	public String saveTpl(String tid, Map<String, Object> infoData) {
		String successFlag = "0";
		String strMsg = "";
		String diffMsg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (StringUtils.isNotBlank(tid) && infoData != null) {

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			Date dateNow = new Date();
			String mainTplId = "";
			try {
				/* -------模板基本信息更新------- Begin */

				PsTzApptplDyTWithBLOBs psTzApptplDyT = new PsTzApptplDyTWithBLOBs();
				psTzApptplDyT.setTzAppTplId(tid);

				// 模板名称
				if (infoData.containsKey("tplName")) {
					psTzApptplDyT.setTzAppTplMc((String) infoData.get("tplName"));
				}

				// 模板描述
				if (infoData.containsKey("tplDesc")) {
					psTzApptplDyT.setTzAppTplMs((String) infoData.get("tplDesc"));
				}

				// 模板用途（在线报名、在线调查）
				if (infoData.containsKey("tplUse")) {
					psTzApptplDyT.setTzAppTplYt((String) infoData.get("tplUse"));
				}

				// 模板类型（报名表、推荐信）
				if (infoData.containsKey("tplUseType")) {
					psTzApptplDyT.setTzUseType((String) infoData.get("tplUseType"));
				}

				// 标签位置
				if (infoData.containsKey("labelPostion")) {
					psTzApptplDyT.setTzAppLabelWz((String) infoData.get("labelPostion"));
				}

				// 提示信息方式
				if (infoData.containsKey("showType")) {
					psTzApptplDyT.setTzAppTsxxFs((String) infoData.get("showType"));
				}

				// 语言
				if (infoData.containsKey("lang")) {
					psTzApptplDyT.setTzAppTplLan((String) infoData.get("lang"));
				}

				// 文件名
				if (infoData.containsKey("filename")) {
					psTzApptplDyT.setTzAttachfileName((String) infoData.get("filename"));
				}

				// 系统文件名
				if (infoData.containsKey("sysFileName")) {
					psTzApptplDyT.setTzAttsysfilename((String) infoData.get("sysFileName"));
				}

				// 绝对路径
				if (infoData.containsKey("path")) {
					psTzApptplDyT.setTzAttPUrl((String) infoData.get("path"));
				}

				// 访问路径
				if (infoData.containsKey("accessPath")) {
					psTzApptplDyT.setTzAttAUrl((String) infoData.get("accessPath"));
				}

				// 提交跳转方式
				if (infoData.containsKey("targetType")) {
					psTzApptplDyT.setTzAppTzfs((String) infoData.get("targetType"));
				}

				// Redirect Url
				if (infoData.containsKey("redirectUrl")) {
					psTzApptplDyT.setTzAppTzurl((String) infoData.get("redirectUrl"));
				}

				// 报名表状态
				if (infoData.containsKey("state")) {
					psTzApptplDyT.setTzEffexpZt((String) infoData.get("state"));
				}

				// 报名表主模板ID
				if (infoData.containsKey("mainTemplate")) {
					mainTplId = (String) infoData.get("mainTemplate");
					psTzApptplDyT.setTzAppMTplId(mainTplId);
				}

				// 提交后是否发送邮件
				if (infoData.containsKey("isSendMail")) {
					psTzApptplDyT.setTzIssendmail((String) infoData.get("isSendMail"));
				}

				// 邮件模板
				if (infoData.containsKey("mailTemplate")) {
					psTzApptplDyT.setTzEmlModalId((String) infoData.get("mailTemplate"));
				}

				// 报名表报文
				psTzApptplDyT.setTzApptplJsonStr(jacksonUtil.Map2json(infoData));
				psTzApptplDyT.setRowLastmantDttm(dateNow);
				psTzApptplDyT.setRowLastmantOprid(oprid);

				psTzApptplDyTMapper.updateByPrimaryKeySelective(psTzApptplDyT);
				/*------- 模板基本信息更新 End -------*/

				/*------- 模板事件设置更新 Begin -------*/
				if (infoData.containsKey("events")) {

					Map<String, Object> eventsData = (Map<String, Object>) infoData.get("events");

				}
				// If &jsonObject.containsKey("events") Then
				//
				// Local JavaObject &eventsObj =
				// &jsonObject.getJSONObject("events");
				// Local boolean &booDelEvent = %This.delEvent(&tplId);
				// For &i = 0 To &eventsObj.names().size() - 1
				//
				//
				// Local string &eventkey = &eventsObj.names().getString(&i);
				//
				// Local JavaObject &event =
				// &eventsObj.getJSONObject(&eventkey);
				//
				// Local Record &TZ_APP_EVENTS_T =
				// CreateRecord(Record.TZ_APP_EVENTS_T);
				// /*模板编号*/
				// &TZ_APP_EVENTS_T.TZ_APP_TPL_ID.Value = &tplId;
				//
				// /*事件编号*/
				// &TZ_APP_EVENTS_T.TZ_EVENT_ID.Value = &eventkey;
				//
				// /*是否启用*/
				// If &event.containsKey("isEff") Then
				// &TZ_APP_EVENTS_T.TZ_QY_BZ.Value = &event.getString("isEff");
				// End-If;
				//
				// /*应用程序类路径*/
				// If &event.containsKey("classPath") Then
				// &TZ_APP_EVENTS_T.CMBC_APPCLS_PATH.Value =
				// &event.getString("classPath");
				// End-If;
				//
				// /*应用程序类名称*/
				// If &event.containsKey("className") Then
				// &TZ_APP_EVENTS_T.CMBC_APPCLS_NAME.Value =
				// &event.getString("className");
				// End-If;
				//
				// /*应用程序类方法*/
				// If &event.containsKey("classFun") Then
				// &TZ_APP_EVENTS_T.CMBC_APPCLS_METHOD.Value =
				// &event.getString("classFun");
				// End-If;
				//
				// /*事件类型*/
				// If &event.containsKey("eventType") Then
				// &TZ_APP_EVENTS_T.TZ_EVENT_TYPE.Value =
				// &event.getString("eventType");
				// End-If;
				// &TZ_APP_EVENTS_T.Insert();
				// End-For;
				// End-If;
				/*------- 模板事件设置更新 End -------*/

				/* 检查主副报名表的差异 */
				if (StringUtils.isNotBlank(mainTplId)) {
					diffMsg = "";
					List<?> resultlist = sqlQuery.queryForList(
							tzSQLObject.getSQLText("SQL.TZApplicationTemplateBundle.TZ_CHECK_ZF_TPL_SQL"),
							new Object[] { mainTplId, tid });
					for (Object obj : resultlist) {
						Map<String, Object> result = (Map<String, Object>) obj;

						String xxxBh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
						String xxxMc = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
						diffMsg = diffMsg + "信息项编号:" + xxxBh + ",信息项名称:" + xxxMc + "\n";
					}

				}
			} catch (Exception e) {
				successFlag = "1";
				strMsg = e.toString();
				e.printStackTrace();
			}
		}

		String alterMsg = "";

		if (StringUtils.isBlank(diffMsg)) {
			alterMsg = "当前报名表模版中的如下信息项在主模版中不存在或控件类名称不一致，请检查当前模版信息项配置。";
			alterMsg = alterMsg + "\n" + diffMsg;
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("code", successFlag);
		mapRet.put("msg", strMsg);
		mapRet.put("insid", "");
		mapRet.put("pageCompleteState", "");
		mapRet.put("alterMsg", alterMsg);
		mapRet.put("appInsVersionId", "");

		return jacksonUtil.Map2json(mapRet);
	}

	/* 获取模板设计页面HTML */
	public String init(String tplId, String insId) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		String sql = "SELECT TZ_APP_TPL_MC FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String tplName = sqlQuery.queryForObject(sql, new Object[] { tplId }, "String");
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

		String sqlLang = "SELECT TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String language = sqlQuery.queryForObject(sqlLang, new Object[] { tplId }, "String");

		String msgSet = gdObjectServiceImpl.getMessageSetByLanguageCd("TZGD_APPONLINE_MSGSET", language);
		jacksonUtil.json2Map(msgSet);
		if (jacksonUtil.containsKey(language)) {
			msgSet = jacksonUtil.getString(language);
		}

		String tplHtml = "";

		componentData = componentData.replace("\"", "\\\\\"");
		try {
			tplHtml = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TEMPLATE_HTML", true,
					request.getContextPath(), tplName, tplId, componentData, tzGeneralURL, msgSet);
		} catch (TzSystemException e) {
			e.printStackTrace();
			tplHtml = "";
		}

		return tplHtml;
	}

	/*
	 * 获取当前模板对应的控件列表以及模板的基本信息以及信息项
	 */
	private String getComponentData(String tplId) {
		JacksonUtil jacksonUtil = new JacksonUtil();

		ArrayList<Map<String, Object>> comDfn = this.getComDfn(tplId);

		String sqlTplData = "SELECT TZ_APPTPL_JSON_STR FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String tplData = sqlQuery.queryForObject(sqlTplData, new Object[] { tplId }, "String");
		if (StringUtils.isBlank(tplData)) {
			tplData = "{}";
		}
		jacksonUtil.json2Map(tplData);
		Map<String, Object> comData = new HashMap<String, Object>();
		comData.put("componentDfn", comDfn);
		comData.put("componentInstance", jacksonUtil.getMap());

		return jacksonUtil.Map2json(comData);
	}

	/*
	 * 获取模板相关控件
	 */
	private ArrayList<Map<String, Object>> getComDfn(String tplId) {
		String contextUrl = request.getContextPath();

		String sqlLang = "SELECT TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String language = sqlQuery.queryForObject(sqlLang, new Object[] { tplId }, "String");

		String sql = "SELECT TZ_APP_TPL_YT FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
		String tplYt = sqlQuery.queryForObject(sql, new Object[] { tplId }, "String");

		String sqlYt = "SELECT * FROM PS_TZ_COM_YT_T WHERE TZ_COM_YT_ID = ? AND TZ_QY_BZ = 'Y' ORDER BY TZ_ORDER ASC";
		List<?> resultlist = sqlQuery.queryForList(sqlYt, new Object[] { tplYt });

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;
			String componentId = result.get("TZ_COM_ID") == null ? "" : String.valueOf(result.get("TZ_COM_ID"));
			PsTzComDyT psTzComDyT = psTzComDyTMapper.selectByPrimaryKey(componentId);

			String componentName = psTzComDyT.getTzComMc();
			String displayArea = result.get("TZ_COM_LX_ID") == null ? "" : String.valueOf(result.get("TZ_COM_LX_ID"));
			String jsfileUrl = contextUrl + psTzComDyT.getTzComJslj();
			String[] arrLj = jsfileUrl.split("/");
			String className = StringUtils.substringBeforeLast(arrLj[arrLj.length - 1], ".js");
			String iconPath = contextUrl + psTzComDyT.getTzComIconlj();

			Map<String, Object> mapOptJson = new HashMap<String, Object>();
			mapOptJson.put("componentId", componentId);
			mapOptJson.put("componentName", componentName);
			mapOptJson.put("displayArea", displayArea);
			mapOptJson.put("jsfileUrl", jsfileUrl);
			mapOptJson.put("className", className);
			mapOptJson.put("iconPath", iconPath);

			String sqlRules = "SELECT * FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? ORDER BY TZ_ORDER ASC";
			List<?> rulelist = sqlQuery.queryForList(sqlRules, new Object[] { "tplYt" });
			Map<String, Object> mapRuleRet = new HashMap<String, Object>();

			for (Object rule : rulelist) {
				Map<String, Object> rul = (Map<String, Object>) rule;
				String ruleId = result.get("TZ_JYGZ_ID") == null ? "" : String.valueOf(result.get("TZ_JYGZ_ID"));
				PsTzJygzDyT psTzJygzDyT = psTzJygzDyTMapper.selectByPrimaryKey(ruleId);

				String ruleName = psTzJygzDyT.getTzJygzMc();
				String className1 = psTzJygzDyT.getTzJygzJslmc();
				String messages = psTzJygzDyT.getTzJygzTsxx();
				if (!StringUtils.equals(language, "ZHS")) {
					PsTzJygzDyEngKey psTzJygzDyEngKey = new PsTzJygzDyEngKey();
					psTzJygzDyEngKey.setLanguageCd(language);
					psTzJygzDyEngKey.setTzJygzId(ruleId);
					PsTzJygzDyEng psTzJygzDyEng = psTzJygzDyEngMapper.selectByPrimaryKey(psTzJygzDyEngKey);
					messages = psTzJygzDyEng.getTzJygzTsxx();
				}
				String isEnable = result.get("TZ_QY_BZ") == null ? "" : String.valueOf(result.get("TZ_QY_BZ"));

				Map<String, Object> ruleJson = new HashMap<String, Object>();
				ruleJson.put("ruleId", ruleId);
				ruleJson.put("ruleName", ruleName);
				ruleJson.put("className", className1);
				ruleJson.put("isEnable", isEnable);
				ruleJson.put("messages", messages);
				mapRuleRet.put(ruleId, ruleJson);
			}
			mapOptJson.put("rules", mapRuleRet);
			listData.add(mapOptJson);
		}
		return listData;
	}
}
