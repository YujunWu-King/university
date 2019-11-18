package com.tranzvision.gd.TZEventsSignInBundle.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 微信二维码或签到码签到
 * @author zhanglang
 *
 */
@Service("com.tranzvision.gd.TZEventsSignInBundle.service.impl.TzEventsQrcodeQdmSignServiceImpl")
public class TzEventsQrcodeQdmSignServiceImpl extends FrameworkImpl{

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PsTzNaudlistTMapper psTzNaudlistTMapper;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	
	/**
	 * 判断签到码能都签到
	 */
	@Override
	public String tzGetJsonData(String strParams) {
		String signJson = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		rtnMap.put("errMsg", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			String str_qdm = "";//签到码
			String qdType = ""; //签到类型， qdm-签到码签到，ewm-二维码签到
			if(jacksonUtil.containsKey("qdm")){
				str_qdm = jacksonUtil.getString("qdm");
			}else{
				str_qdm = request.getParameter("qdm");
			}
			
			if(jacksonUtil.containsKey("type")){
				qdType = jacksonUtil.getString("type");
			}else{
				qdType = request.getParameter("type");
			}
			
			String str_message = "";
			if(!"".equals(str_qdm) && str_qdm != null){
				String bmrSql = "SELECT TZ_ART_ID,TZ_HD_BMR_ID,TZ_NREG_STAT,TZ_BMCY_ZT FROM PS_TZ_NAUDLIST_T A,PS_TZ_LXFSINFO_TBL B WHERE TZ_HD_QDM=? AND  B.TZ_LXFS_LY='HDBM' AND A.TZ_HD_BMR_ID=B.TZ_LYDX_ID";
				Map<String,Object> bmrMap = sqlQuery.queryForMap(bmrSql, new Object[]{ str_qdm });

				String str_hd_id = ""; //活动ID
				String str_hd_bmrid = "";	//报名人ID

				String str_bm_state = "";	//报名状态
				String str_sign_status = "";	//签到状态
				if(bmrMap != null){
					str_hd_id = bmrMap.get("TZ_ART_ID") == null ? "" : bmrMap.get("TZ_ART_ID").toString();
					str_hd_bmrid = bmrMap.get("TZ_HD_BMR_ID") == null ? "" : bmrMap.get("TZ_HD_BMR_ID").toString();
					str_bm_state = bmrMap.get("TZ_NREG_STAT") == null ? "" : bmrMap.get("TZ_NREG_STAT").toString();
					str_sign_status = bmrMap.get("TZ_BMCY_ZT") == null ? "" : bmrMap.get("TZ_BMCY_ZT").toString();
					
					if("".equals(str_hd_id) || "".equals(str_hd_bmrid)){
						str_message = "没有该"+ ("qdm".equals(qdType)? "签到码" : "二维码") + "对应的报名信息！";
					}
					//活动是否发布
					if("".equals(str_message)){
						String hdSql = "SELECT 'Y' FROM PS_TZ_LM_NR_GL_T WHERE TZ_ART_ID =?  AND TZ_ART_PUB_STATE='Y' limit 1";
						//活动发布状态
						String pubStatus = sqlQuery.queryForObject(hdSql, new Object[]{ str_hd_id }, "String");
						if(!"Y".equals(pubStatus)){
							str_message = "该活动未发布，不能签到！";
						}
					}
					//未报名
					if("".equals(str_message) && !"1".equals(str_bm_state)){
						str_message = "未报名或已撤销报名，不能签到！";
					}
					//已签到
					if("".equals(str_message) && "A".equals(str_sign_status)){
						str_message = "该"+("qdm".equals(qdType)? "签到码" : "二维码")+"已签到!";
					}
				}else{
					str_message = "签到码"+str_qdm+"不存在，不能签到！";
				}
				
				if("".equals(str_message)){
					//可以签到
					rtnMap.replace("result", "success");
				}else{
					//签到失败
					rtnMap.replace("result", "failure");
					rtnMap.replace("errMsg", str_message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		signJson = jacksonUtil.Map2json(rtnMap);
		return signJson;
	}
	
	
	
	
	/**
	 * 签到人信息确认页面
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String bmrInfoHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			//当前登录机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			String str_qdm = "";//签到码
			String qdType = ""; //签到类型， qdm-签到码签到，ewm-二维码签到
			if(jacksonUtil.containsKey("qdm")){
				str_qdm = jacksonUtil.getString("qdm");
			}else{
				str_qdm = request.getParameter("qdm");
			}
			
			if(jacksonUtil.containsKey("type")){
				qdType = jacksonUtil.getString("type");
			}else{
				qdType = request.getParameter("type");
			}
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			
			if(!"".equals(str_qdm) && str_qdm != null){
				String bmrSql = "SELECT TZ_ART_ID,TZ_HD_BMR_ID,TZ_CYR_NAME,TZ_ZY_SJ,TZ_ZY_EMAIL FROM PS_TZ_NAUDLIST_T A,PS_TZ_LXFSINFO_TBL B WHERE TZ_HD_QDM=? AND  B.TZ_LXFS_LY='HDBM' AND A.TZ_HD_BMR_ID=B.TZ_LYDX_ID";
				Map<String,Object> bmrMap = sqlQuery.queryForMap(bmrSql, new Object[]{ str_qdm });

				String str_hd_id = ""; //活动ID
				String str_hd_bmrid = "";	//报名人ID
				String str_cyr_name = ""; 	//报名人姓名
				String str_cyr_mobile = "";	//报名人手机
				String str_cyr_email = "";	//报名人邮箱
				
				if(bmrMap != null){
					str_hd_id = bmrMap.get("TZ_ART_ID") == null ? "" : bmrMap.get("TZ_ART_ID").toString();
					str_hd_bmrid = bmrMap.get("TZ_HD_BMR_ID") == null ? "" : bmrMap.get("TZ_HD_BMR_ID").toString();
					str_cyr_name = bmrMap.get("TZ_CYR_NAME") == null ? "" : bmrMap.get("TZ_CYR_NAME").toString();
					str_cyr_mobile = bmrMap.get("TZ_ZY_SJ") == null ? "" : bmrMap.get("TZ_ZY_SJ").toString();
					str_cyr_email = bmrMap.get("TZ_ZY_EMAIL") == null ? "" : bmrMap.get("TZ_ZY_EMAIL").toString();
					
					String str_hd_title = "";
					String str_start_time = "";
					String str_end_time = "";
					String sql = "SELECT TZ_NACT_NAME,concat(date_format(TZ_START_DT,'%Y年%m月%d日'),' ',date_format(TZ_START_TM,'%H:%i')) TZ_START_TIME ,concat(date_format(TZ_END_DT,'%Y年%m月%d日'),' ',date_format(TZ_END_TM,'%H:%i')) TZ_END_TIME FROM PS_TZ_ART_HD_TBL WHERE TZ_ART_ID=?";
					Map<String,Object> hdInfoMap =  sqlQuery.queryForMap(sql, new Object[]{ str_hd_id });
					if(hdInfoMap != null){
						str_hd_title = hdInfoMap.get("TZ_NACT_NAME") == null ? "" : hdInfoMap.get("TZ_NACT_NAME").toString();
						str_start_time = hdInfoMap.get("TZ_START_TIME") == null ? "" : hdInfoMap.get("TZ_START_TIME").toString();
						str_end_time = hdInfoMap.get("TZ_END_TIME") == null ? "" : hdInfoMap.get("TZ_END_TIME").toString();
					}
					/*报名信息*/
					String itemsHtml = this.getApplyItemsHtml(str_hd_id, str_hd_bmrid);
					
					//构造确认签到url参数
					Map<String,String> comQdParamsMap = new HashMap<String,String>();
					comQdParamsMap.put("qdm", str_qdm);
					
					Map<String,Object> tzQdParamsMap = new HashMap<String,Object>();
					tzQdParamsMap.put("ComID", "TZ_HD_SIGN_COM");
					tzQdParamsMap.put("PageID", "TZ_HD_SIGNIN_STD");
					tzQdParamsMap.put("OperateType", "tzEventsSignInConfirm");
					tzQdParamsMap.put("comParams", comQdParamsMap);

					String str_confirm_qd_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzQdParamsMap),"UTF-8");
					
					String str_main_url = "";
					if("qdm".equals(qdType)){
						//返回签到码签到页面
						//str_main_url = "document.referrer";
						str_main_url = ZSGL_URL + "?classid=signCode";
					}else{
						//返回活动签到管理主页面
						//str_main_url = ZSGL_URL + "?classid=eventsSign";
						str_main_url = contextPath + "/signIn/"+orgId;
					}
					
					//ajax请求url
					Map<String,Object> ajaxParamsMap = new HashMap<String,Object>();
					ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
					ajaxParamsMap.put("PageID", "TZ_HD_SIGN_STD");
					ajaxParamsMap.put("OperateType", "EJSON");
					ajaxParamsMap.put("comParams", new HashMap<String,String>());
					String str_ajax_url  =  ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap),"UTF-8");
					
					bmrInfoHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_BMR_INFO_HTML",
							contextPath,str_hd_title,str_cyr_name,str_cyr_mobile,str_cyr_email,itemsHtml,str_confirm_qd_url,str_ajax_url,str_main_url,str_start_time,str_end_time);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bmrInfoHtml;
	}
	
	
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
			case "tzEventsSignInConfirm":
				//确认签到
				strRet = this.tzEventsSignInConfirm(strParams,errorMsg);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	
	
	/**
	 * 确认签到
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzEventsSignInConfirm(String strParams, String[] errorMsg){
		String strRtn = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		rtnMap.put("errMsg", "");
		
		String result = "";
		String errMsg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			
			String str_qdm = "";//签到码
			if(jacksonUtil.containsKey("qdm")){
				str_qdm = jacksonUtil.getString("qdm");
			}else{
				str_qdm = request.getParameter("qdm");
			}
			
			if(!"".equals(str_qdm) && str_qdm != null){
				String sql = "SELECT TZ_BMCY_ZT,TZ_ART_ID,TZ_HD_BMR_ID,TZ_NREG_STAT FROM PS_TZ_NAUDLIST_T WHERE TZ_HD_QDM=? limit 1";
				Map<String,Object> bmrMap = sqlQuery.queryForMap(sql, new Object[]{ str_qdm });
				if(bmrMap != null){
					String cy_Status = bmrMap.get("TZ_BMCY_ZT") == null ? "" :bmrMap.get("TZ_BMCY_ZT").toString();
					String str_hd_id = bmrMap.get("TZ_ART_ID") == null ? "" : bmrMap.get("TZ_ART_ID").toString();
					String str_bmr_id = bmrMap.get("TZ_HD_BMR_ID") == null ? "" : bmrMap.get("TZ_HD_BMR_ID").toString();
					String str_nreg_stat=bmrMap.get("TZ_NREG_STAT") == null ? "" : bmrMap.get("TZ_NREG_STAT").toString();
					if(!"1".equals(str_nreg_stat)){
						result = "false";
						errMsg = "签到失败,报名状态未审核！";
					}else{
						if("A".equals(cy_Status)){
							result = "false";
							errMsg = "已签到，请不要重复签到！";
						}else{
							PsTzNaudlistTKey psTzNaudlistTKey = new PsTzNaudlistTKey();
							psTzNaudlistTKey.setTzArtId(str_hd_id);
							psTzNaudlistTKey.setTzHdBmrId(str_bmr_id);
							PsTzNaudlistT psTzNaudlistT = psTzNaudlistTMapper.selectByPrimaryKey(psTzNaudlistTKey);
							if(psTzNaudlistT != null){
								psTzNaudlistT.setTzBmcyZt("A");
								psTzNaudlistT.setTzQdTime(new Date());
								int rtn = psTzNaudlistTMapper.updateByPrimaryKey(psTzNaudlistT);
								if(rtn != 0){
									result = "true";
									errMsg = "签到成功！";
								}else{
									result = "false";
									errMsg = "签到失败！";
								}
							}
						}
					}
				}else{
					result = "false";
					errMsg = "签到码无效！";
				}
			}else{
				result = "false";
				errMsg = "请提供签到码！";
			}
		}catch(Exception e){			
			e.printStackTrace();
			result = "false";
			errMsg = "系统错误，"+e.getMessage();
		}
		
		rtnMap.replace("result", result);
		rtnMap.replace("errMsg", errMsg);
		strRtn = jacksonUtil.Map2json(rtnMap);
		
		return strRtn;
	}
	
	
	
	
	/**
	 * 显示签到人报名信息
	 * @param str_hd_id
	 * @param bmr_id
	 * @return
	 */
	public String getApplyItemsHtml(String str_hd_id,String bmr_id){
		String itemsHtml = "";
		try{
			String sql = "SELECT TZ_ZXBM_XXX_ID,TZ_ZXBM_XXX_NAME,TZ_ZXBM_XXX_ZSXS FROM PS_TZ_ZXBM_XXX_T WHERE TZ_ART_ID=? AND TZ_ZXBM_XXX_ID not in('TZ_CYR_NAME','TZ_ZY_SJ','TZ_ZY_EMAIL') ORDER BY TZ_PX_XH";
			List<Map<String,Object>> itemsOList = sqlQuery.queryForList(sql, new Object[]{ str_hd_id });
			
			String itemID, itemName, itemType;
			for(Map<String,Object> itemMap : itemsOList){
				itemID = itemMap.get("TZ_ZXBM_XXX_ID").toString();
				itemName = itemMap.get("TZ_ZXBM_XXX_NAME").toString();
				itemType = itemMap.get("TZ_ZXBM_XXX_ZSXS").toString();
				
				String itemVal="";
				switch (itemID) {
				case "TZ_ZY_SJ":
					sql = "SELECT TZ_ZY_SJ FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='HDBM' AND TZ_LYDX_ID=?";
					itemVal = sqlQuery.queryForObject(sql, new Object[]{  bmr_id }, "String");
					break;
				case "TZ_ZY_EMAIL":
					sql = "SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='HDBM' AND TZ_LYDX_ID=?";
					itemVal = sqlQuery.queryForObject(sql, new Object[]{  bmr_id }, "String");
					break;
				case "TZ_ZY_COMPANY":
					sql = "SELECT TZ_ZY_COMPANY FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='HDBM' AND TZ_LYDX_ID=?";
					itemVal = sqlQuery.queryForObject(sql, new Object[]{  bmr_id }, "String");
					break;
				case "TZ_ZY_POSITION":
					sql = "SELECT TZ_ZY_POSITION FROM PS_TZ_LXFSINFO_TBL WHERE  TZ_LXFS_LY='HDBM' AND TZ_LYDX_ID=?";
					itemVal = sqlQuery.queryForObject(sql, new Object[]{  bmr_id }, "String");
					break;
				default:
					sql = "SELECT "+ itemID +" FROM PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_HD_BMR_ID=?";
					itemVal = sqlQuery.queryForObject(sql, new Object[]{ str_hd_id, bmr_id }, "String");
				}
				//下拉框
				if("2".equals(itemType)){
					sql = "SELECT TZ_XXX_TRANS_NAME FROM PS_TZ_XXX_TRANS_T WHERE TZ_ART_ID=? AND TZ_ZXBM_XXX_ID=? AND TZ_XXX_TRANS_ID=?";
					itemVal = sqlQuery.queryForObject(sql, new Object[]{ str_hd_id, itemID, itemVal }, "String");
				}
				
				if(!"".equals(itemVal) && itemVal != null){
					itemsHtml = itemsHtml + tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_BM_XXX_HTML",itemName,itemVal);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return itemsHtml;
	}
	
	
}
