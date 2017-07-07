package com.tranzvision.gd.TZEventsSignInBundle.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


@Service("com.tranzvision.gd.TZEventsSignInBundle.service.impl.TzEventsBmrListServiceImpl")
public class TzEventsBmrListServiceImpl extends FrameworkImpl{

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;
	
	
	
	/**
	 * 活动报名人列表
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String bmrListHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String str_hd_id = "";//活动ID
			if(jacksonUtil.containsKey("tz_act_id")){
				str_hd_id = jacksonUtil.getString("tz_act_id");
			}else{
				str_hd_id = request.getParameter("tz_act_id");
			}
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			
			String str_hd_title = "";
			String str_ul_html = "";
			String str_bmr_li_html = "";
			String str_bmr_xq_url = "";
			
			if(!"".equals(str_hd_id) && str_hd_id != null){
				String sql = "SELECT TZ_NACT_NAME FROM PS_TZ_ART_HD_TBL WHERE TZ_ART_ID=?";
				str_hd_title = sqlQuery.queryForObject(sql, new Object[]{ str_hd_id }, "String");
				
				
				//构造确认签到url参数
				Map<String,String> comQdParamsMap = new HashMap<String,String>();
				comQdParamsMap.put("qdm", "");
				
				Map<String,Object> tzQdParamsMap = new HashMap<String,Object>();
				tzQdParamsMap.put("ComID", "TZ_HD_SIGN_COM");
				tzQdParamsMap.put("PageID", "TZ_HD_SIGNIN_STD");
				tzQdParamsMap.put("OperateType", "tzEventsSignInConfirm");
				tzQdParamsMap.put("comParams", comQdParamsMap);
				
				//活动未签到人员列表
				String wqdSql = tzGdObject.getSQLText("SQL.TZEventsSignInBundle.TzEventsUnSignInBmrListSql");
				List<Map<String,Object>> bmrList = sqlQuery.queryForList(wqdSql, new Object[]{ str_hd_id });
				for(Map<String,Object> bmrMap : bmrList){
				    String bmrId = bmrMap.get("TZ_HD_BMR_ID") == null ? "" : bmrMap.get("TZ_HD_BMR_ID").toString();
					String bmrName = bmrMap.get("TZ_CYR_NAME") == null ? "" : bmrMap.get("TZ_CYR_NAME").toString();
					String bmrMobile = bmrMap.get("TZ_ZY_SJ") == null ? "" : bmrMap.get("TZ_ZY_SJ").toString();
					String bmrEmail = bmrMap.get("TZ_ZY_EMAIL") == null ? "" : bmrMap.get("TZ_ZY_EMAIL").toString();
					String bmrQdm = bmrMap.get("TZ_HD_QDM") == null ? "" : bmrMap.get("TZ_HD_QDM").toString();
					String bmrOprid = bmrMap.get("OPRID") == null ? "" : bmrMap.get("OPRID").toString();
					
					// 头像地址;
					String str_img_path = "";
					String imageUrlSQL = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
					Map<String , Object> imgMap = sqlQuery.queryForMap(imageUrlSQL,new Object[]{bmrOprid});
					if(imgMap != null){
						String tzAttAUrl = imgMap.get("TZ_ATT_A_URL").toString();
						String sysImgName = imgMap.get("TZ_ATTACHSYSFILENA").toString();
						if(tzAttAUrl != null &&!"".equals(tzAttAUrl)
							&& sysImgName != null &&!"".equals(sysImgName)){
							if(tzAttAUrl.lastIndexOf("/") + 1 == tzAttAUrl.length()){
								str_img_path = tzAttAUrl + sysImgName;
							}else{
								str_img_path = tzAttAUrl + "/" + sysImgName;
							}
						}
					}
					if("".equals(str_img_path)){
						str_img_path = contextPath + "/statics/images/tranzvision/mrtx02.jpg";
					}else{
						str_img_path = contextPath + str_img_path;
					}
					
					//拨打电话签到按钮
					String call_qd_html = "";
					if(!"".equals(bmrMobile)){
						call_qd_html = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_WQDLI_CALL_PHO_HTML",contextPath,bmrMobile);
					}
					
					comQdParamsMap.replace("qdm", bmrQdm);
					tzQdParamsMap.replace("comParams", comQdParamsMap);
					//添加签到按钮
					String str_qd_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzQdParamsMap),"UTF-8");
					call_qd_html = call_qd_html 
							+ tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_WQDLI_QD_HTML",contextPath,str_qd_url);
					
					//报名人详情URL
					str_bmr_xq_url = ZSGL_URL + "?classid=bmrDetails&tz_act_id="+str_hd_id+"&tz_bmr_id="+bmrId;
					
					str_bmr_li_html = str_bmr_li_html 
							+ tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_BMR_LIST_HTML",
									bmrName,bmrEmail,bmrMobile,str_img_path,str_bmr_xq_url,call_qd_html);
				}
				
				str_ul_html = "<ul id='wqdList' class='liwithpic myWqdList'>" + str_bmr_li_html + "</ul>";
			}
			//活动管理URL
			String str_hd_url = ZSGL_URL + "?classid=eventsSignList";
			
			//构造url参数
			Map<String,String> comParamsMap = new HashMap<String,String>();
			comParamsMap.put("tz_act_id", str_hd_id);
			comParamsMap.put("listType", "WQD");

			Map<String,Object> tzParamsMap = new HashMap<String,Object>();
			tzParamsMap.put("ComID", "TZ_HD_SIGN_COM");
			tzParamsMap.put("PageID", "TZ_HD_BMR_LIST_STD");
			tzParamsMap.put("OperateType", "EJSON");
			tzParamsMap.put("comParams", comParamsMap);
			
			//未签到人员加载URL
			String str_wqd_load_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzParamsMap),"UTF-8");
			
			comParamsMap.replace("listType", "YQD");
			tzParamsMap.replace("comParams", comParamsMap);
			//未签到人员加载URL
			String str_yqd_load_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzParamsMap),"UTF-8");
			
			//ajax请求url
			Map<String,Object> ajaxParamsMap = new HashMap<String,Object>();
			ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
			ajaxParamsMap.put("PageID", "TZ_HD_SIGN_STD");
			ajaxParamsMap.put("OperateType", "EJSON");
			ajaxParamsMap.put("comParams", new HashMap<String,String>());
			String str_ajax_url  =  ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap),"UTF-8");
			
			bmrListHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_BMR_LISTS_HTML",
					contextPath,str_hd_url,str_hd_title,str_ul_html,str_wqd_load_url,str_yqd_load_url,str_ajax_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bmrListHtml;
	}
	
	
	
	/**
	 * 加载未签到列表或已签到列表
	 */
	@Override
	public String tzGetJsonData(String strParams) {
		String str_ul_html = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("bmrList", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String str_hd_id = jacksonUtil.getString("tz_act_id");
			//加载类型，WQD - 加载未签到列表，YQD - 加载已签到列表
			String listType = jacksonUtil.getString("listType");
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			
			String str_bmr_li_html = "";
			String str_bmr_xq_url = "";
			
			if(!"".equals(str_hd_id) && str_hd_id != null
					&& !"".equals(listType) && listType != null){
				
				//构造确认签到url参数
				Map<String,String> comQdParamsMap = new HashMap<String,String>();
				comQdParamsMap.put("qdm", "");
				
				Map<String,Object> tzQdParamsMap = new HashMap<String,Object>();
				tzQdParamsMap.put("ComID", "TZ_HD_SIGN_COM");
				tzQdParamsMap.put("PageID", "TZ_HD_SIGNIN_STD");
				tzQdParamsMap.put("OperateType", "tzEventsSignInConfirm");
				tzQdParamsMap.put("comParams", comQdParamsMap);
				
				//活动未签到人员列表
				String wqdSql = tzGdObject.getSQLText("SQL.TZEventsSignInBundle."
						+("WQD".equals(listType)? "TzEventsUnSignInBmrListSql" : "TzEventsSignInBmrListSql"));
				List<Map<String,Object>> bmrList = sqlQuery.queryForList(wqdSql, new Object[]{ str_hd_id });
				
				for(Map<String,Object> bmrMap : bmrList){
				    String bmrId = bmrMap.get("TZ_HD_BMR_ID") == null ? "" : bmrMap.get("TZ_HD_BMR_ID").toString();
					String bmrName = bmrMap.get("TZ_CYR_NAME") == null ? "" : bmrMap.get("TZ_CYR_NAME").toString();
					String bmrMobile = bmrMap.get("TZ_ZY_SJ") == null ? "" : bmrMap.get("TZ_ZY_SJ").toString();
					String bmrEmail = bmrMap.get("TZ_ZY_EMAIL") == null ? "" : bmrMap.get("TZ_ZY_EMAIL").toString();
					String bmrQdm = bmrMap.get("TZ_HD_QDM") == null ? "" : bmrMap.get("TZ_HD_QDM").toString();
					String bmrOprid = bmrMap.get("OPRID") == null ? "" : bmrMap.get("OPRID").toString();
					
					// 头像地址;
					String str_img_path = "";
					String imageUrlSQL = "SELECT B.TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A , PS_TZ_OPR_PHOTO_T B WHERE A.OPRID=? AND A.TZ_ATTACHSYSFILENA = B.TZ_ATTACHSYSFILENA";
					Map<String , Object> imgMap = sqlQuery.queryForMap(imageUrlSQL,new Object[]{bmrOprid});
					if(imgMap != null){
						String tzAttAUrl = imgMap.get("TZ_ATT_A_URL").toString();
						String sysImgName = imgMap.get("TZ_ATTACHSYSFILENA").toString();
						if(tzAttAUrl != null &&!"".equals(tzAttAUrl)
							&& sysImgName != null &&!"".equals(sysImgName)){
							if(tzAttAUrl.lastIndexOf("/") + 1 == tzAttAUrl.length()){
								str_img_path = tzAttAUrl + sysImgName;
							}else{
								str_img_path = tzAttAUrl + "/" + sysImgName;
							}
						}
					}
					
					if("".equals(str_img_path)){
						str_img_path = contextPath + "/statics/images/tranzvision/mrtx02.jpg";
					}else{
						str_img_path = contextPath + str_img_path;
					}
					
					//拨打电话签到按钮
					String call_qd_html = "";
					if("WQD".equals(listType)){
						if(!"".equals(bmrMobile)){
							call_qd_html = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_WQDLI_CALL_PHO_HTML",contextPath,bmrMobile);
						}
						
						comQdParamsMap.replace("qdm", bmrQdm);
						tzQdParamsMap.replace("comParams", comQdParamsMap);
						//添加签到按钮
						String str_qd_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzQdParamsMap),"UTF-8");
						call_qd_html = call_qd_html 
								+ tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_WQDLI_QD_HTML",contextPath,str_qd_url);
					}
					
					//报名人详情URL
					str_bmr_xq_url = ZSGL_URL + "?classid=bmrDetails&tz_act_id="+str_hd_id+"&tz_bmr_id="+bmrId;
					//报名人li
					str_bmr_li_html = str_bmr_li_html 
							+ tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_BMR_LIST_HTML",
									bmrName,bmrEmail,bmrMobile,str_img_path,str_bmr_xq_url,call_qd_html);
				}
				
				str_ul_html = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle."
						+("WQD".equals(listType)? "TZ_HDQD_BMR_WQD_HTML" : "TZ_HDQD_BMR_YQD_HTML"),str_bmr_li_html);
			}
			rtnMap.replace("bmrList", str_ul_html);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(rtnMap);
	}
	
	
	
}
