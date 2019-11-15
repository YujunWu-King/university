package com.tranzvision.gd.TZEventsMobileBundle.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzLxfsinfoTblMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 手机版活动、活动报名
 * @author zhanglang
 *
 */
@Controller
@RequestMapping(value = { "/mEvents" })
public class TzMEventsDetailsController {
	
	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	PsTzNaudlistTMapper psTzNaudlistTMapper;

	@Autowired
	PsTzLxfsinfoTblMapper psTzLxfsinfoTblMapper;
	
	
	/**
	 * 手机活动详情页，获取活动信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getInfo", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getEventInfoData(HttpServletRequest request, HttpServletResponse response){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("diaplayAppBar", "N");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date dateNow = new Date();
			//活动编号
			String actId = request.getParameter("actId");
			
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
			
			//活动听众判断
			boolean isInAud = false;
			String audSql = "select TZ_AUD_ID from PS_TZ_ART_AUDIENCE_T where TZ_ART_ID=? and exists(select 'X' from PS_TZ_ART_REC_TBL where TZ_ART_ID=PS_TZ_ART_AUDIENCE_T.TZ_ART_ID and TZ_PROJECT_LIMIT='B')";
			List<Map<String,Object>> audList = sqlQuery.queryForList(audSql, new Object[]{ actId });
			if(audList != null && audList.size() > 0){
				for(Map<String,Object> audMap: audList){
					String audId = audMap.get("TZ_AUD_ID") == null ? "" : audMap.get("TZ_AUD_ID").toString();
					String inAudSql = "select 'Y' from PS_TZ_AUD_LIST_T where TZ_AUD_ID=? and TZ_DXZT<>'N' and OPRID=? limit 1";
					String inAud = sqlQuery.queryForObject(inAudSql, new Object[]{ audId, oprid }, "String");
					if("Y".equals(inAud)){
						isInAud = true;
					}
				}
			}else{
				isInAud = true;
			}
			
			
			// 获取活动显示模式
			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventDisplayMode");
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { dateNow, dateNow, dateNow, dateNow, actId });
			
			// 是否有效记录
			String validTD = "";
			// 是否启用在线报名
			String strQy_zxbm = "";
			//活动是否未开始
			String actNoStart = "";
			if (mapData != null) {
				validTD = mapData.get("VALID_TD") == null ? "" : String.valueOf(mapData.get("VALID_TD"));
				strQy_zxbm = mapData.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapData.get("TZ_QY_ZXBM"));
				
				//活动是否未开始
				actNoStart = mapData.get("IS_NOT_START") == null ? "" : String.valueOf(mapData.get("IS_NOT_START"));
			}
			
			// 只有启用在线报名才显示在线报名条,如果设置听众当前人在听众内显示报名条
			if ("Y".equals(strQy_zxbm) && isInAud) {
				rtnMap.replace("diaplayAppBar","Y");
				
				sql = "select 'Y' REG_FLAG,TZ_HD_BMR_ID,TZ_NREG_STAT FROM PS_TZ_NAUDLIST_T where OPRID=? and TZ_ART_ID=? and TZ_NREG_STAT IN('1','4')";
				Map<String, Object> mapBM = sqlQuery.queryForMap(sql, new Object[] { oprid, actId });

				// 是否已注册报名标识
				String regFlag = "";
				// 报名人ID
				String strBmrId = "";
				//报名状态
				String applySta = "";
				if (mapBM != null) {
					regFlag = mapBM.get("REG_FLAG") == null ? "" : String.valueOf(mapBM.get("REG_FLAG"));
					strBmrId = mapBM.get("TZ_HD_BMR_ID") == null ? "" : String.valueOf(mapBM.get("TZ_HD_BMR_ID"));
					applySta = mapBM.get("TZ_NREG_STAT") == null ? "" : String.valueOf(mapBM.get("TZ_NREG_STAT"));
				}
				
				//已报名活动，活动尚未开始，可以撤销报名
				if("Y".equals(regFlag) && "Y".equals(actNoStart)){
					validTD = "Y";
				}
				
				
				//显示报名状态
				String statusText = "";
				switch(applySta){
				case "1":
					statusText = "已报名";
					break;
				case "4":
					//等候席位数
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetWaitingNumber");
					int waitNum = sqlQuery.queryForObject(sql, new Object[]{ actId, strBmrId }, "int");
					statusText = "等候席第"+ waitNum +"位";
					break;
				}
				
				if(oprid==null||"".equals(oprid)){
					regFlag = "";
				}
				
				rtnMap.put("statusText", statusText);

				rtnMap.put("regFlag", regFlag);
				rtnMap.put("bmrId", strBmrId);
				rtnMap.put("valid_dt", validTD);
				
				
				String strBaseUrl = request.getServletContext().getContextPath() + "/dispatcher?tzParams=";
				//在线报名URL
				String strAppFormUrl = request.getContextPath() + "/mEvents/apply/"+actId;
				
				// 构造链接参数
				Map<String, Object> mapComParams = new HashMap<String, Object>();
				Map<String, Object> mapParams = new HashMap<String, Object>();
				
				mapComParams.put("APPLYID", actId);
				mapComParams.put("BMRID", strBmrId);

				mapParams.put("ComID", "TZ_APPONL_COM");
				mapParams.put("PageID", "TZ_APPBAR_VIEW_STD");
				mapParams.put("OperateType", "EJSON");
				mapParams.put("comParams", mapComParams);

				String strUrlParams = jacksonUtil.Map2json(mapParams);
				//撤销报名URL
				String cancelApplyUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");
				
				rtnMap.put("appFormUrl", strAppFormUrl);
				rtnMap.put("cancelApplyUrl", cancelApplyUrl);
			}
			
			String actAddr = "";
			String startTime = "";
			String endTime = "";
			SimpleDateFormat simpleDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			sql = "select TZ_NACT_ADDR,concat(TZ_START_DT,' ',TZ_START_TM) as TZ_START_DT,concat(TZ_END_DT,' ',TZ_END_TM) as TZ_END_DT from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
			Map<String, Object> actMap = sqlQuery.queryForMap(sql, new Object[] { actId });
			if(actMap != null){
				actAddr = actMap.get("TZ_NACT_ADDR") == null ? "" : actMap.get("TZ_NACT_ADDR").toString();
				startTime = actMap.get("TZ_START_DT") == null ? "" : actMap.get("TZ_START_DT").toString();
				endTime = actMap.get("TZ_END_DT") == null ? "" : actMap.get("TZ_END_DT").toString();
				
				SimpleDateFormat simpleDttmFormat = new SimpleDateFormat("MM/dd HH:mm");
				if(!"".equals(startTime)){
					startTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(startTime)); 
				}
				if(!"".equals(endTime)){
					endTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(endTime)); 
				}
			}
			
			if("".equals(actAddr) && "".equals(startTime) && "".equals(endTime)){
				//如果不是活动，地点取内容表中的TZ_LONG1字段，时间就取TZ_DATE1
				sql = "select TZ_LONG1,date_format(TZ_DATE1,'%Y/%m/%d') as TZ_DATE1 from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
				actMap = sqlQuery.queryForMap(sql, new Object[] { actId });
				if(actMap != null){
					actAddr = actMap.get("TZ_LONG1") == null ? "" : actMap.get("TZ_LONG1").toString();
					startTime = actMap.get("TZ_DATE1") == null ? "" : actMap.get("TZ_DATE1").toString();
				}
			}
			
			if(!"".equals(endTime)){
				startTime = startTime+ " - " +endTime;
			}
			
			rtnMap.put("location", actAddr);
			rtnMap.put("dateTime", startTime);

		}catch(Exception e){
			e.printStackTrace();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 手机活动报名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "apply/{actId}", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String eventsAppForm(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "actId") String actId){
		String strRet = "";
		Map<String,Object> itemsMap = new HashMap<String,Object>(); 
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();

			//当前登录人oprid
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);

			String contextPath = request.getContextPath();
			// 统一URL;
			String ZSGL_URL = contextPath + "/dispatcher";

			// 姓名
			String sql = "select TZ_REALNAME from PS_TZ_REG_USER_T where OPRID=?";
			String name = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
			if(name == null){
				name = "";
			}
			// 邮箱
			String email = "";
			// 手机
			String mobile = "";
			
			/*手机邮箱信息从联系方式表中获取*/
			sql = "select TZ_ZY_EMAIL,TZ_ZY_SJ from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
			Map<String, Object> mapUserEmlPhone = sqlQuery.queryForMap(sql, new Object[] { oprid });
			if(mapUserEmlPhone != null){
				email = mapUserEmlPhone.get("TZ_ZY_EMAIL") == null ? "" : String.valueOf(mapUserEmlPhone.get("TZ_ZY_EMAIL"));
				mobile = mapUserEmlPhone.get("TZ_ZY_SJ") == null ? "" : String.valueOf(mapUserEmlPhone.get("TZ_ZY_SJ"));
			}

			// 必填项的html标识
			String strBtHtml = "<span>*</span>";

			// 活动ID的隐藏域
			String str_items_html = "<input type=\"hidden\" id=\"TZ_APPLY_ID\" name=\"TZ_APPLY_ID\" value=\""
					+ actId + "\"/>";

			// 双语化
			sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { actId }, "String");

			sql = "select TZ_ZXBM_XXX_ID,TZ_ZXBM_XXX_NAME,TZ_ZXBM_XXX_BT,TZ_ZXBM_XXX_ZSXS from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=? order by TZ_PX_XH";
			List<Map<String, Object>> listItems = sqlQuery.queryForList(sql, new Object[] { actId });

			for (Map<String, Object> mapItem : listItems) {
				// 信息项编号
				String strItemId = mapItem.get("TZ_ZXBM_XXX_ID") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_ID"));
				// 信息项名称
				String strItemName = mapItem.get("TZ_ZXBM_XXX_NAME") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_NAME"));
				// 信息项是否必填
				String strBT = mapItem.get("TZ_ZXBM_XXX_BT") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_BT"));
				// 信息项显示模式
				String strType = mapItem.get("TZ_ZXBM_XXX_ZSXS") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_ZSXS"));

				// 判断站点语言
				if ("ENG".equals(tzSiteLang)) {
					sql = "select TZ_ZXBM_XXX_NAME from PS_TZ_ZXBM_XXX_E_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and LANGUAGE_CD='ENG'";
					strItemName = sqlQuery.queryForObject(sql, new Object[] { actId, strItemId }, "String");
				}

				String itemNameHtml = "";
				String required = "";
				if ("Y".equals(strBT)) {
					itemNameHtml = strItemName + strBtHtml;
					required = "required";
				}else{
					itemNameHtml = strItemName;
				}

				switch (strType) {
				case "1":
					if ("TZ_CYR_NAME".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, name,contextPath);
					} else if ("TZ_ZY_SJ".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, mobile,contextPath);
					} else if ("TZ_ZY_EMAIL".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, email,contextPath);
					} else {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, "",contextPath);
					}
					break;

				case "2":
					sql = "select TZ_XXX_TRANS_ID,TZ_XXX_TRANS_NAME from PS_TZ_XXX_TRANS_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? order by TZ_PX_XH";
					List<Map<String, Object>> listOpts = sqlQuery.queryForList(sql,
							new Object[] { actId, strItemId });

					String strOptHtml = "<option><option>";

					for (Map<String, Object> mapOpt : listOpts) {
						String strOptId = mapOpt.get("TZ_XXX_TRANS_ID") == null ? ""
								: String.valueOf(mapOpt.get("TZ_XXX_TRANS_ID"));
						String strOptVal = mapOpt.get("TZ_XXX_TRANS_NAME") == null ? ""
								: String.valueOf(mapOpt.get("TZ_XXX_TRANS_NAME"));

						// 下拉值双语化
						if ("ENG".equals(tzSiteLang)) {
							sql = "select TZ_OPT_VALUE from PS_TZ_XXX_TR_EN_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and TZ_XXX_TRANS_ID=? and LANGUAGE_CD='ENG'";
							strOptVal = sqlQuery.queryForObject(sql, new Object[] { actId, strItemId, strOptId },
									"String");
						}

						strOptHtml = strOptHtml + "<option value=\"" + strOptId + "\">" + strOptVal + "</option>";

					}

					str_items_html = str_items_html + tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_SELECT_HTML", itemNameHtml,strItemId, strOptHtml,required);
					break;
				}
				itemsMap.put(strItemId, strBT);
			}

			// 验证码
			str_items_html = str_items_html + tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_CODE_HTML");
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_AQ_YHXX_TBL WHERE OPRID=?",new Object[]{oprid},"String");
			
			if (JGID == null || JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
			System.out.println(this.getClass().getName()+":"+JGID);
			strRet = tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_FORM_HTML", contextPath,str_items_html, ZSGL_URL,jacksonUtil.Map2json(itemsMap),JGID);

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "系统错误：" + e.toString();
		}

		return strRet;
	}
}
