package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcBgtKxzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcBgtZwtTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjXxkxzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjXxxPzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcXxJygzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcXxxKxzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcXxxPzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcwjBgkxzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcwjBgzwtTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzWjXxJygzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtZwtT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjXxkxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjXxxPzTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxJygzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxPzTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjBgkxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjBgzwtT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzWjXxJygzT;
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

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireEditorEngineImpl")

public class QuestionnaireEditorEngineImpl {

	@Autowired
	private PsTzDcWjXxxPzTMapper psTzDcWjXxxPzTMapper;
	
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private PsTzJygzDyEngMapper psTzJygzDyEngMapper;
	
	@Autowired
	private PsTzDcWjXxkxzTMapper psTzDcWjXxkxzTMapper;
	
	@Autowired
	private PsTzDcwjBgzwtTMapper psTzDcwjBgzwtTMapper;
	
	@Autowired
	private PsTzWjXxJygzTMapper psTzWjXxJygzTMapper;
	
	@Autowired
	private PsTzDcwjBgkxzTMapper psTzDcWjBgkxzTMapper;
	@Autowired
	private PsTzComDyTMapper psTzComDyTMapper;
	
	@Autowired
	private PsTzJygzDyTMapper psTzJygzDyTMapper;
	
	@Autowired
	private PsTzDcWjDyTMapper psTzDcWjDyTMapper;

	
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

	private String survyID;

	// init()方法：
	String init(String surveyID) {
		this.survyID = surveyID;
		JacksonUtil jsonUtil = new JacksonUtil();
		/* 问卷标题,问卷语言 */
		String strTname, strLang;
		Map<String, Object> map = jdbcTemplate.queryForMap(
				"SELECT TZ_DC_WJBT,TZ_APP_TPL_LAN FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID =?",
				new Object[] { surveyID });
		strTname = map.get("TZ_DC_WJBT").toString();
		strLang = map.get("TZ_APP_TPL_LAN")==null?"ZHS":toString();

		// 1、读取控件注册信息，显示问卷编辑页面左侧工具条;
		String componentData = getComponentData();
		String contextUrl = request.getContextPath();
		String tzGeneralURL = contextUrl + "/dispatcher";

		if (StringUtils.isBlank(strLang)) {
			strLang = gdKjComService.getLoginLanguage(request, response);
		}
		String msgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response, "TZGD_SURVEY_MSGSET", strLang);
		jsonUtil.json2Map(msgSet);
		if (jsonUtil.containsKey(strLang)) {
			Map<String, Object> msgLang = jsonUtil.getMap(strLang);
			msgSet = jsonUtil.Map2json(msgLang);
		}
		String tplHtml = "";
		componentData = componentData.replace("\\", "\\\\");
		componentData = componentData.replace("$", "\\$");
		try {
			tplHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_TEMPLATE_HTML", strTname,
					survyID, componentData, tzGeneralURL, msgSet, "SRVY", request.getContextPath(), contextUrl);
		} catch (TzSystemException e) {
			e.printStackTrace();
			tplHtml = "";
		}
		return tplHtml;
	}
	// getComDfn()
	@SuppressWarnings("unchecked")
	ArrayList<Map<String,Object>> getComDfn(String survyID) {
		// 保存最终结果的List
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// 页面url
		String contextUrl = request.getContextPath();
		//用图表中查出所有的满足条件的SRVY数据
		List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
		recordList = jdbcTemplate.queryForList(
				"select * from PS_TZ_COM_YT_T where TZ_COM_YT_ID=? and  TZ_QY_BZ = 'Y' ORDER BY TZ_ORDER ASC",
				new Object[] { "SRVY" });

		PsTzComDyTWithBLOBs psTzComDyT = null;
		for (int i = 0; i < recordList.size(); i++) {
			// 将用途表的ID 给定义表，然后使用ID查询
			String componentId = recordList.get(i).get("TZ_COM_ID").toString();
			// psTzComDyT=jdbcTemplate.queryForList("select * from
			// PS_TZ_COM_DY_T where TZ_COM_ID=?",new Object[]{componentId});
			psTzComDyT = psTzComDyTMapper.selectByPrimaryKey(componentId);
			Map<String, Object> mapOptJson = new HashMap<String, Object>();
			if (psTzComDyT != null) {
				String componentName = psTzComDyT.getTzComMc();
				String displayArea = recordList.get(i).get("TZ_COM_LX_ID") == null ? ""
						: String.valueOf(recordList.get(i).get("TZ_COM_LX_ID"));
				String jsfileUrl = contextUrl + psTzComDyT.getTzComJslj();
				String arrLj[] = jsfileUrl.split("/");
				String className = StringUtils.substringBeforeLast(arrLj[arrLj.length - 1], ".js");
				String iconPath = contextUrl + psTzComDyT.getTzComIconlj();

				mapOptJson.put("componentId", componentId);
				mapOptJson.put("componentName", componentName);
				mapOptJson.put("displayArea", displayArea);
				mapOptJson.put("jsfileUrl", jsfileUrl);
				mapOptJson.put("className", className);
				mapOptJson.put("iconPath", iconPath);
				// -----------------------------------------------------
				// 读取语言
				String sqlLang = "SELECT TZ_APP_TPL_LAN FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ?";
				String strLang = jdbcTemplate.queryForObject(sqlLang, new Object[] { survyID }, "String");
				// REM 2、获取当前控件的校验规则;
				String sqlRules = "SELECT * FROM PS_TZ_COM_JYGZPZ_T WHERE TZ_COM_ID = ? ORDER BY TZ_ORDER ASC";
				List<?> rulelist = new ArrayList<Map<String, Object>>();
				rulelist = jdbcTemplate.queryForList(sqlRules, new Object[] { componentId });
				Map<String, Object> mapRuleRet = new HashMap<String, Object>();

				for (Object rule : rulelist) {
					Map<String, Object> rul = new HashMap<String,Object>();
					rul=(Map<String, Object>) rule;
					String ruleId = rul.get("TZ_JYGZ_ID") == null ? "" : String.valueOf(rul.get("TZ_JYGZ_ID"));
					PsTzJygzDyT psTzJygzDyT = psTzJygzDyTMapper.selectByPrimaryKey(ruleId);

					String ruleName = psTzJygzDyT.getTzJygzMc();
					String className1 = psTzJygzDyT.getTzJygzJslmc();
					String messages = psTzJygzDyT.getTzJygzTsxx();
					if (!StringUtils.equals(strLang, "ZHS")) {
						PsTzJygzDyEngKey psTzJygzDyEngKey = new PsTzJygzDyEngKey();
						psTzJygzDyEngKey.setLanguageCd(strLang);
						psTzJygzDyEngKey.setTzJygzId(ruleId);
						PsTzJygzDyEng psTzJygzDyEng = psTzJygzDyEngMapper.selectByPrimaryKey(psTzJygzDyEngKey);
						if(psTzJygzDyEng!=null)
							messages = psTzJygzDyEng.getTzJygzTsxx();
					}
					String isEnable = rul.get("TZ_QY_BZ") == null ? "" : String.valueOf(rul.get("TZ_QY_BZ"));

					Map<String, Object> ruleJson = new HashMap<String, Object>();
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
		return listData;
	}

	// getComponentData方法
	String getComponentData() {
		JacksonUtil jsonUtil = new JacksonUtil();
		ArrayList<Map<String,Object>> strComDfn = getComDfn(this.survyID);
		String strSurveyData = jdbcTemplate.queryForObject(
				"SELECT TZ_APPTPL_JSON_STR FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID =?", new Object[] { this.survyID },"String");
		if (StringUtils.isBlank(strSurveyData))
			strSurveyData = "{}";
		jsonUtil.json2Map(strSurveyData);
		System.out.println("----me----strSurveyData:"+strSurveyData);
		Map<String, Object> comData = new HashMap<String, Object>();
		comData.put("componentDfn", strComDfn);
		comData.put("componentInstance", jsonUtil.getMap());

		return jsonUtil.Map2json(comData);
	}

	// 保存(对于新建的调查问卷---要重新保存模板的情况) 2对于复制情况要保存
	@SuppressWarnings("unchecked")
	public String saveSurvy(String survyID, Map<String, Object> mapData, String userID, String[] errMsg) {
		System.out.println("======saveSurvy执行=======");
		System.out.println("========strTid:"+survyID);
		System.out.println("=======mapData+"+new JacksonUtil().Map2json(mapData));
		
		//1.将所有的信息存到TZ_DC_WJ_DY_T的TZ_APPTPL_JOSN_STR中去
		PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs=new PsTzDcWjDyTWithBLOBs();
		psTzDcWjDyTWithBLOBs.setTzDcWjId(survyID);
		psTzDcWjDyTWithBLOBs.setTzApptplJsonStr(new JacksonUtil().Map2json(mapData));
		
		if(psTzDcWjDyTMapper.selectByPrimaryKey(survyID)!=null)
			psTzDcWjDyTMapper.updateByPrimaryKeySelective(psTzDcWjDyTWithBLOBs);
		else
			psTzDcWjDyTMapper.insertSelective(psTzDcWjDyTWithBLOBs);
		//2.更新TZ_DC_WJ_DY_T中的属性，查到mapData中的items然后逐一存储信息
		if(!StringUtils.isBlank(survyID) && mapData != null)
		{
			if(mapData.containsKey("items"))
			{
				//将items中的属性取出//(匿名属性值)转成List取出
				Map<String,Object>items=(Map<String, Object> )mapData.get("items");
				System.out.println("====survyID=====survyID:"+survyID);
				this.survyID=survyID;
				delSurvy(survyID);
				for(Object obj:items.values())
				{
					Map<String,Object>item=(Map<String,Object>)obj;
					//savePerXXX(survyID,item);
					savePerItem(item);
				}
			}
			
		}
		
		//-----------------------------
		
		// {
				// "code":"%BIND(:1)",
				// "msg":"%bind(:2)",
				// "insid":"%bind(:3)",
				// "pageCompleteState":[%bind(:4)],
				// "alterMsg":"%bind(:5)",
				// "appInsVersionId":"%bind(:6)"
				// }
		JacksonUtil jacksonUtil=new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("code", "0");
		mapRet.put("msg", "");
		mapRet.put("insid", "");
		mapRet.put("pageCompleteState", "");
		mapRet.put("alterMsg", "");
		mapRet.put("appInsVersionId", "");
		return jacksonUtil.Map2json(mapRet);
	}
		
	@SuppressWarnings("unchecked")
	private void savePerItem(Map<String, Object> mapData){
		System.out.println("====savePerItem====urvyID:"+this.survyID);
		//数据库表：(1)TZ_DCWJ_XXXPZ_T (2)TZ_DCWJ_BGKXZ_T TZ_DCWJ_XXKXZ_T对应option (3)TZ_WJ_XX_JYGZ_T对应rules (4)TZ_DCWJ_BGZWT_T对应表格子问题child
		PsTzDcWjXxxPzTWithBLOBs psTzDcWjXxxPzTWithBLOBs=new PsTzDcWjXxxPzTWithBLOBs();
		//问卷ID:
		psTzDcWjXxxPzTWithBLOBs.setTzDcWjId(this.survyID);
		//信息项编号：
		if (mapData.containsKey("itemId")) 
			psTzDcWjXxxPzTWithBLOBs.setTzXxxBh(mapData.get("itemId").toString());
		//信息项实例ID:
		if(mapData.containsKey("instanceId"))
			psTzDcWjXxxPzTWithBLOBs.setTzXxxSlid(mapData.get("instanceId").toString());
		//信息项名称
		if(mapData.containsKey("itemName")&&mapData.get("itemName")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMc(mapData.get("itemName").toString());
		//信息项标题(富文本)
		if(mapData.containsKey("title")&&mapData.get("title")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzTitle(mapData.get("title").toString());
		//题号 数据库字段不明确
		if(mapData.containsKey("qCode")&&mapData.get("qCode")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxQid(mapData.get("qCode").toString());
		//是否计算分值
		if(mapData.containsKey("isAvg")&&mapData.get("isAvg")!=null){
			psTzDcWjXxxPzTWithBLOBs.setTzIsAvg(mapData.get("isAvg").toString());
		}
		//排序序号
		if(mapData.containsKey("orderby")&&mapData.get("orderby")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzOrder(Integer.valueOf(mapData.get("orderby").toString()));
		//分页号
		if(mapData.containsKey("pageno")&&mapData.get("pageno")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzPageNo(Integer.valueOf(mapData.get("pageno").toString()));
		//控件类名称classname
		if(mapData.containsKey("classname")&&mapData.get("classname")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzComLmc(mapData.get("classname").toString());
		/*空值提示信息*/
		if(mapData.containsKey("emptyText")&&mapData.get("emptyText")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxKztsxx(mapData.get("emptyText").toString());
		/*焦点提示信息*/
		if(mapData.containsKey("onFoucsMessage")&&mapData.get("onFoucsMessage")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxJdtsxx(mapData.get("onFoucsMessage").toString());
		/*默认值*/
		if(mapData.containsKey("defaultval")&&mapData.get("defaultval")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMrz(mapData.get("defaultval").toString());
		/*后缀*/
		if(mapData.containsKey("suffix")&&mapData.get("suffix")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxHz(mapData.get("suffix").toString());
		/*后缀链接*/
		if(mapData.containsKey("suffixUrl")&&mapData.get("suffixUrl")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxHzlj(mapData.get("suffixUrl").toString());
		/*存储类型*/
		if(mapData.containsKey("StorageType")&&mapData.get("StorageType")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxCclx(mapData.get("StorageType").toString());
		/*文本框大小*/
		if(mapData.containsKey("boxSize")&&mapData.get("boxSize")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxWbkdx(mapData.get("boxSize").toString());
		 /*日期格式*/
		if(mapData.containsKey("dateformate")&&mapData.get("dateformate")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxRqgs(mapData.get("dateformate").toString());
		 /*年份最小值*/
		if(mapData.containsKey("minYear")&&mapData.get("minYear")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxNfmin(mapData.get("minYear").toString());
		 /*年份最大值*/
		if(mapData.containsKey("maxYear")&&mapData.get("maxYear")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxNfmax(mapData.get("maxYear").toString());
	
		/*单选、多选排列方式*/
		if(mapData.containsKey("plfs")&&mapData.get("plfs")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxPlfs(mapData.get("plfs").toString());
		/*多选最少选择个数*/
		if(mapData.containsKey("minSelect")&&mapData.get("minSelect")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxZsxzgs(Short.valueOf(mapData.get("minSelect").toString()));
		/*多选最多选择个数*/
		if(mapData.containsKey("maxSelect")&&mapData.get("maxSelect")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxZdxzgs(Short.valueOf(mapData.get("maxSelect").toString()));
		/*允许上传类型*/
		if(mapData.containsKey("yxsclx")&&mapData.get("yxsclx")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxYxsclx(mapData.get("yxsclx").toString());
		 /*允许上传大小*/
		if(mapData.containsKey("yxscdx")&&mapData.get("yxscdx")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxYxscdx(Short.valueOf(mapData.get("yxscdx").toString()));
		/*允许多附件上传  字段不明确或者未创建
		//if(mapData.containsKey("allowMultiAtta"))
			
		/*图片是否裁剪*/
		if(mapData.containsKey("tpsfcj")&&mapData.get("tpsfcj")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxSfcj(mapData.get("tpsfcj").toString());
		/*图片裁剪类型*/
		if(mapData.containsKey("tpcjlx")&&mapData.get("tpcjlx")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxTpcjlx(mapData.get("tpcjlx").toString());
		/*文字说明*/
		if(mapData.containsKey("wzsm")&&mapData.get("wzsm")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxWzsm(mapData.get("wzsm").toString());
		/*是否必填*/
		if(mapData.containsKey("isRequire")&&mapData.get("isRequire")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxBtBz(mapData.get("isRequire").toString());
		/*是否字数限制*/
		if(mapData.containsKey("isCheckStrLen")&&mapData.get("isCheckStrLen")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxCharBz(mapData.get("isCheckStrLen").toString());
		/*最小长度*/
		if(mapData.containsKey("minLen")&&mapData.get("minLen")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMinlen(Integer.valueOf(mapData.get("minLen").toString()));
		 /*最大长度*/
		if(mapData.containsKey("maxLen")&&mapData.get("maxLen")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMaxlen(Integer.valueOf(mapData.get("maxLen").toString()));
		/*是否限制数字范围*/
		if(mapData.containsKey("isNumSize")&&mapData.get("isNumSize")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxNumBz(mapData.get("isNumSize").toString());
		 /*最小值*/
		if(mapData.containsKey("min")&&mapData.get("min")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMin(Long.valueOf(mapData.get("min").toString()));
		/*最大值*/
		if(mapData.containsKey("max")&&mapData.get("max")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMax(Long.valueOf(mapData.get("max").toString()));
		/*小数位数*/
		if(mapData.containsKey("digits")&&mapData.get("digits")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxXsws(Short.valueOf(mapData.get("digits").toString()));
		 /*固定规则校验*/
		if(mapData.containsKey("preg")&&mapData.get("preg")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxGdgsjy(mapData.get("preg").toString());
		/*是否多行容器*/
		if(mapData.containsKey("isDoubleLine")&&mapData.get("isDoubleLine")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxDrqBz(mapData.get("isDoubleLine").toString());
		/*最小行记录数*/
		if(mapData.containsKey("minLines")&&mapData.get("minLines")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMinLine(Short.valueOf(mapData.get("minLines").toString()));
		/*最大行记录数*/
		if(mapData.containsKey("maxLines")&&mapData.get("maxLines")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxMaxLine(Short.valueOf(mapData.get("maxLines").toString()));
		/*是否为单行组合*/
		if(mapData.containsKey("isSingleLine")&&mapData.get("isSingleLine")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzXxxSrqBz(mapData.get("isSingleLine").toString());
		/*外网是否可下载*/
		if(mapData.containsKey("isDownLoad")&&mapData.get("isDownLoad")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzIsDownload(mapData.get("isDownLoad").toString());
		/*是否显示在报名表审核*/
		if(mapData.containsKey("isShow")&&mapData.get("isShow")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzIsShow(mapData.get("isShow").toString());
		 /*子问题横向排列*/
		if(mapData.containsKey("inth")&&mapData.get("inth")!=null)
			psTzDcWjXxxPzTWithBLOBs.setTzSubQsInth(mapData.get("inth").toString());
		/*每列/行只能选择一个 数据表中该字段缺失*/
		//if(mapData.containsKey("oneChoice"))
		
		 /*------- 是否Option Begin -------*/
		if(mapData.containsKey("option")){
			Map<String,Object>optionMaps=(Map<String, Object>) mapData.get("option");
			if(optionMaps!=null&&optionMaps.size()>0)
			{
				for(Object optionMapValue:optionMaps.values()){
				Map<String,Object>optionMap=(Map<String, Object>) optionMapValue;	
				//存储类型为T ：模板
				if("T".equals(psTzDcWjXxxPzTWithBLOBs.getTzXxxCclx()))
				{
					PsTzDcwjBgkxzT psTzDcwjBgkxzT=new PsTzDcwjBgkxzT();
					 /*模板编号*/
					psTzDcwjBgkxzT.setTzDcWjId(this.survyID);
					/*信息项编号*/
					if(mapData.containsKey("itemId"))
						psTzDcwjBgkxzT.setTzXxxBh(mapData.get("itemId").toString());
		            /*可选值编码*/
					if(optionMap.containsKey("code")&&optionMap.get("code")!=null)
						psTzDcwjBgkxzT.setTzOptCode(optionMap.get("code").toString());
					/*排序序号*/
					if(optionMap.containsKey("orderby")&&optionMap.get("orderby")!=null)
						psTzDcwjBgkxzT.setTzOrder(Integer.valueOf(optionMap.get("orderby").toString()));
					 /*可选值描述*/
					if(optionMap.containsKey("txt")&&optionMap.get("txt")!=null)
						psTzDcwjBgkxzT.setTzOptName(optionMap.get("txt").toString());
		            /*权重* */
					if(optionMap.containsKey("weight")&&optionMap.get("weight")!=null)
						psTzDcwjBgkxzT.setTzWeight(optionMap.get("weight").toString());
					//疑问：直接执行insert是否不妥？ 存在对应主键是否进行update?
					psTzDcWjBgkxzTMapper.insert(psTzDcwjBgkxzT);
				}
				else{
					PsTzDcWjXxkxzT psTzDcWjXxkxzT=new PsTzDcWjXxkxzT();
					//问卷ID
					psTzDcWjXxkxzT.setTzDcWjId(this.survyID);
					/*信息项编号*/
					if(mapData.containsKey("itemId"))
						psTzDcWjXxkxzT.setTzXxxBh(mapData.get("itemId").toString());
					  /*可选值编码*/
					if(optionMap.containsKey("code")&&optionMap.get("code")!=null)
						psTzDcWjXxkxzT.setTzXxxkxzMc(optionMap.get("code").toString());
		            /*排序序号*/
					if(optionMap.containsKey("orderby")&&optionMap.get("orderby")!=null)
						psTzDcWjXxkxzT.setTzOrder(Integer.valueOf(optionMap.get("orderby").toString()));
		            /*可选值描述*/
					if(optionMap.containsKey("txt")&&optionMap.get("txt")!=null)
						psTzDcWjXxkxzT.setTzXxxkxzMs(optionMap.get("txt").toString());
					if(optionMap.containsKey("defaultval")&&optionMap.get("defaultval")!=null)
						psTzDcWjXxkxzT.setTzKxzMrzBz(optionMap.get("defaultval").toString());
		            /*是否为其他*/
					if(optionMap.containsKey("other")&&optionMap.get("other")!=null)
						psTzDcWjXxkxzT.setTzKxzQtBz(optionMap.get("other").toString());
		            /*权重*/
					if(optionMap.containsKey("weight")&&optionMap.get("weight")!=null)
						psTzDcWjXxkxzT.setTzXxxkxzQz(new BigDecimal(optionMap.get("weight").toString()));
					//直接执行插入操作是否不妥？
					psTzDcWjXxkxzTMapper.insert(psTzDcWjXxkxzT);
				}
			  }
			}
		}
		
			/*------- 是否Rules Begin -------*/
		   if(mapData.containsKey("rules")){
			Map<String,Object>ruleMaps=(Map<String, Object>) mapData.get("rules");
			  if(ruleMaps!=null&&ruleMaps.size()>0){
				  //PS中对应的TZ_WJ_XX_JYGZ_T表缺失
				 //rules对应的值取出作为Map 
				  for(Object ruleMapValue:ruleMaps.values()){
						Map<String,Object>ruleMap=(Map<String, Object>) ruleMapValue;
					PsTzWjXxJygzT psTzWjXxJygzT=new PsTzWjXxJygzT();
					/*问卷编号*/
					psTzWjXxJygzT.setTzDcWjId(this.survyID);
					/*信息项编号*/
					if(mapData.containsKey("itemId"))
						psTzWjXxJygzT.setTzXxxBh(mapData.get("itemId").toString());
					/*校验规则ID*/
					if(ruleMap.containsKey("ruleId"))
						psTzWjXxJygzT.setTzJygzId(ruleMap.get("ruleId").toString());
					 /*是否启用*/
					if(ruleMap.containsKey("isEnable")&&ruleMap.get("isEnable")!=null)
						psTzWjXxJygzT.setTzQyBz(ruleMap.get("isEnable").toString());
					/*提示信息*/
					if(ruleMap.containsKey("messages")&&ruleMap.get("messages")!=null)
						psTzWjXxJygzT.setTzJygzTsxx(ruleMap.get("messages").toString());
					//插入数据
					psTzWjXxJygzTMapper.insert(psTzWjXxJygzT);
			  } 
		   }
		   }
		   /*------- 是否Rules End -------*/
		   
		   /*------- 是否表格题子问题 Begin -------*/
		   if(mapData.containsKey("child")){
			   Map<String,Object>chidrenMap=(Map<String, Object>) mapData.get("child");
			   if(chidrenMap!=null&&chidrenMap.size()>0)
			   {
				   for(Object childrenMapValue:chidrenMap.values()){
				   Map<String,Object>childMap=(Map<String, Object>) childrenMapValue;
				   PsTzDcwjBgzwtT psTzDcwjBgzwtT=new PsTzDcwjBgzwtT();
				   /*问卷编号*/
				   psTzDcwjBgzwtT.setTzDcWjId(this.survyID);
				   /*信息项编号*/
				   if(mapData.containsKey("itemId"))
					   psTzDcwjBgzwtT.setTzXxxBh(mapData.get("itemId").toString());
				   /*问题编码 数据库字段缺失*/
				   if(childMap.containsKey("sqCode")&&childMap.get("sqCode")!=null)
					   psTzDcwjBgzwtT.setTzQuCode(childMap.get("sqCode").toString());
				   if(childMap.containsKey("orderby")&&childMap.get("orderby")!=null)
						psTzDcwjBgzwtT.setTzOrder(Integer.valueOf(childMap.get("orderby").toString()));
				   /*子问题名称 */
				   if(childMap.containsKey("question")&&childMap.get("question")!=null)
					   psTzDcwjBgzwtT.setTzQuName(childMap.get("question").toString());
				   //缺失-？是否数据库 表移植名称错误？
				   if(childMap.containsKey("shortDesc")&&childMap.get("shortDesc")!=null)
					   psTzDcwjBgzwtT.setTzShortName(childMap.get("shortDesc").toString());
					//System.out.println("请核对PS_TZ_DCWJ_BGZWT_T表和PS中的TZ_DCWJ_BGZWT_T字段是否移植正确");   
					System.out.println("====chidrenMap===="+new JacksonUtil().Map2json(chidrenMap));
					psTzDcwjBgzwtTMapper.insert(psTzDcwjBgzwtT);
					
				   }
			   }
		   }
		   psTzDcWjXxxPzTMapper.insertSelective(psTzDcWjXxxPzTWithBLOBs);
	}		
	
	private boolean delSurvy(String survyID) {
		try {
			if (!StringUtils.isBlank(survyID)) {
				 /*在线调查问卷信息项配置表*/
				Object[] args = new Object[] { survyID };
				String sql = "DELETE FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID = ? ";
				jdbcTemplate.update(sql, args);

				 /*在线调查问卷信息项可选值定义表*/
				sql = "DELETE FROM PS_TZ_DCWJ_XXKXZ_T WHERE TZ_DC_WJ_ID = ? ";
				jdbcTemplate.update(sql, args);

				 /*在线调查问卷表格选择题可选项定义表*/
				sql = "DELETE FROM PS_TZ_DCWJ_BGKXZ_T WHERE TZ_DC_WJ_ID = ? ";
				jdbcTemplate.update(sql, args);

				 /*在线调查问卷表格选择题子问题定义表*/
				sql = "DELETE FROM PS_TZ_DCWJ_BGZWT_T WHERE TZ_DC_WJ_ID = ? ";
				jdbcTemplate.update(sql, args);

				 /*在线调查问卷信息项校验规则表*/
				sql = "DELETE FROM PS_TZ_WJ_XX_JYGZ_T WHERE TZ_DC_WJ_ID = ? ";
				jdbcTemplate.update(sql, args);

				return true;
			} else {
				return false;
			}
		} catch (Exception E) {
			return false;
		}
	}
}
