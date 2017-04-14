package com.tranzvision.gd.TZEventsSignInBundle.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZEventsSignInBundle.service.impl.TzEventsBmrDetailsServiceImpl")
public class TzEventsBmrDetailsServiceImpl extends FrameworkImpl{

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzEventsQrcodeQdmSignServiceImpl tzEventsQrcodeQdmSign;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String bmrDetailsHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String str_hd_id = "";//活动ID
			String str_bmr_id = ""; //报名人ID
			if(jacksonUtil.containsKey("tz_act_id")){
				str_hd_id = jacksonUtil.getString("tz_act_id");
			}else{
				str_hd_id = request.getParameter("tz_act_id");
			}
			
			if(jacksonUtil.containsKey("tz_bmr_id")){
				str_bmr_id = jacksonUtil.getString("tz_bmr_id");
			}else{
				str_bmr_id = request.getParameter("tz_bmr_id");
			}
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			
			if(!"".equals(str_hd_id) && str_hd_id != null
					&& !"".equals(str_bmr_id) && str_bmr_id != null){
				
				String str_hd_title = "";
				String bmrName = "";
				String bmrMobile = "";
				String bmrEmail = "";
				String bmrOprid = "";
				String bmDatetime  = "";
				String str_img_path = "";
				
				String sql = "SELECT TZ_NACT_NAME FROM PS_TZ_ART_HD_TBL WHERE TZ_ART_ID=?";
				str_hd_title = sqlQuery.queryForObject(sql, new Object[]{ str_hd_id }, "String");
				
				sql = "SELECT TZ_CYR_NAME,TZ_ZY_SJ,TZ_ZY_EMAIL,OPRID,concat(date_format(TZ_REG_TIME,'%Y年%m月%d日'),' ',date_format(TZ_REG_TIME,'%H:%i:%s')) TZ_BM_TIME FROM PS_TZ_NAUDLIST_T A,PS_TZ_LXFSINFO_TBL B WHERE A.TZ_ART_ID=? AND TZ_HD_BMR_ID=? AND B.TZ_LXFS_LY='HDBM' AND A.TZ_HD_BMR_ID=B.TZ_LYDX_ID";
				Map<String,Object> bmrMap = sqlQuery.queryForMap(sql, new Object[]{ str_hd_id, str_bmr_id });
				if(bmrMap != null){
					bmrName = bmrMap.get("TZ_CYR_NAME") == null ? "" : bmrMap.get("TZ_CYR_NAME").toString();
					bmrMobile = bmrMap.get("TZ_ZY_SJ") == null ? "" : bmrMap.get("TZ_ZY_SJ").toString();
					bmrEmail = bmrMap.get("TZ_ZY_EMAIL") == null ? "" : bmrMap.get("TZ_ZY_EMAIL").toString();
					bmrOprid = bmrMap.get("OPRID") == null ? "" : bmrMap.get("OPRID").toString();
					bmDatetime = bmrMap.get("TZ_BM_TIME") == null ? "" : bmrMap.get("TZ_BM_TIME").toString();
					
					
					// 头像地址;
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
				}
				
				if("".equals(str_img_path)){
					str_img_path = contextPath + "/statics/images/tranzvision/mrtx02.jpg";
				}else{
					str_img_path = contextPath + str_img_path;
				}
				
				String other_items_html = tzEventsQrcodeQdmSign.getApplyItemsHtml(str_hd_id, str_bmr_id);
				//报名时间
				other_items_html = other_items_html + tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_BM_XXX_HTML","报名时间",bmDatetime);
				
				
				//ajax请求url
				Map<String,Object> ajaxParamsMap = new HashMap<String,Object>();
				ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
				ajaxParamsMap.put("PageID", "TZ_HD_SIGN_STD");
				ajaxParamsMap.put("OperateType", "EJSON");
				ajaxParamsMap.put("comParams", new HashMap<String,String>());
				String str_ajax_url  =  ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap),"UTF-8");
				
				bmrDetailsHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_BMR_DETAILS_HTML",
						contextPath,str_hd_title,str_img_path,bmrName,bmrEmail,bmrMobile,other_items_html,str_ajax_url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bmrDetailsHtml;
	}
}
