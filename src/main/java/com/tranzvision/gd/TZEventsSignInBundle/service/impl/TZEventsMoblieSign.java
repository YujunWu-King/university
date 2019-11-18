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
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartJSSDKSign;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZEventsSignInBundle.service.impl.TZEventsMoblieSign")
public class TZEventsMoblieSign extends FrameworkImpl {

	@Autowired
	private TZGDObject tzGdObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery SqlQuery;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private TzWeChartJSSDKSign tzWeChartJSSDKSign;

	@Override
	public String tzGetHtmlContent(String strParams) {
		String qdmInputHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 当前登录机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String contextPath = request.getContextPath();
			String tz_act_id = request.getParameter("tz_act_id");
			String act_name = SqlQuery.queryForObject("select TZ_NACT_NAME FROM PS_TZ_ART_HD_TBL WHERE TZ_ART_ID=?",
					new Object[] { tz_act_id }, "String");
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";

			// 构造签到url参数
			Map<String, String> comQdParamsMap = new HashMap<String, String>();

			Map<String, Object> tzQdParamsMap = new HashMap<String, Object>();
			tzQdParamsMap.put("ComID", "TZ_HD_SIGN_COM");
			tzQdParamsMap.put("PageID", "TZ_HD_SIGNIN_STD");
			tzQdParamsMap.put("OperateType", "EJSON");
			tzQdParamsMap.put("comParams", comQdParamsMap);
			// 签到码签到URL
			String str_sign_url = ZSGL_URL + "?tzParams="
					+ URLEncoder.encode(jacksonUtil.Map2json(tzQdParamsMap), "UTF-8");
			// 返回主菜单
			// String str_main_url = ZSGL_URL + "?classid=eventsSign";
			String str_main_url = contextPath + "/signIn/" + orgId;

			// 签到人详情确认页URL
			String str_qdr_xq_url = ZSGL_URL + "?classid=moblieSign&OperateType=QR";

			// ajax请求url
			Map<String, Object> ajaxParamsMap = new HashMap<String, Object>();
			ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
			ajaxParamsMap.put("PageID", "TZ_HD_MOB_SIGN_STD");
			ajaxParamsMap.put("OperateType", "EJSON");
			ajaxParamsMap.put("comParams", new HashMap<String, String>());
			String str_ajax_url = ZSGL_URL + "?tzParams="
					+ URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap), "UTF-8");
			if ("".equals(act_name)) {
				act_name = "手机活动现场管理";
			}
			
			String nonceStr = "";
			String timestamp = "0";
			String signature = "";
			
			String basePath = request.getScheme() + "://"+request.getServerName() + "/dispatcher";
			String url;
			if("".equals(request.getQueryString()) || request.getQueryString() == null){
				//url = request.getRequestURL().toString();
				url = basePath;
			}else{
				//url = request.getRequestURL().toString()+"?"+request.getQueryString();
				url = basePath + "?" + request.getQueryString();
			}
			url = url.split("#")[0];
			
			String corpid = "";
			String corpsecret = "";
			try {
				corpid = getHardCodePoint.getHardCodePointVal("TZ_WX_CORPID");
				corpsecret = getHardCodePoint.getHardCodePointVal("TZ_WX_SECRET");
				System.out.println("corpid：==========="+corpid);
				System.out.println("corpsecret：==========="+corpsecret);

				Map<String,String> signMap = tzWeChartJSSDKSign.sign(corpid, corpsecret, "GZ", url);
				System.out.println("signMap:"+signMap);
				if(signMap != null){
					nonceStr = signMap.get("nonceStr");
					timestamp = signMap.get("timestamp");
					signature = signMap.get("signature");
					System.out.println("signature:======="+signature);
				}
				System.out.println("signature:end=======");
			} catch (NullPointerException nullE) {
				nullE.printStackTrace();
			}
			//签到人详情确认页URL
			String str_qdr_xq_url1  = ZSGL_URL + "?classid=signConfirm";
			
			//微信扫一扫签到url
			String str_wxsys_qd_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzQdParamsMap),"UTF-8");
			
			qdmInputHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_MOBLIE_SIGN", contextPath,
					str_sign_url, str_qdr_xq_url, str_main_url, str_ajax_url, act_name, tz_act_id,corpid,timestamp,nonceStr,signature,str_wxsys_qd_url,str_qdr_xq_url1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return qdmInputHtml;
	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		switch (oprType) {
		case "QM":
			jacksonUtil.json2Map(strParams);
			String moblieNu = jacksonUtil.getString("moblieNu");
			String artId = jacksonUtil.getString("artId");
			System.out.println("moblieNu:" + moblieNu);
			System.out.println("artId:" + artId);
			StringBuffer sb = new StringBuffer();

			// 指定的活动,必须审核过 的才可以签到
			sb.append("SELECT A.TZ_ZY_SJ,B.TZ_CYR_NAME,B.TZ_HD_BMR_ID,C.TZ_NACT_NAME,C.TZ_ART_ID ");
			sb.append("FROM  PS_TZ_LXFSINFO_TBL A,PS_TZ_NAUDLIST_T B,PS_TZ_ART_HD_TBL C ");
			sb.append("WHERE A.TZ_LXFS_LY=? ");
			sb.append("AND B.TZ_HD_BMR_ID=A.TZ_LYDX_ID ");
			sb.append("AND B.TZ_ART_ID=C.TZ_ART_ID ");
			sb.append("AND A.TZ_ZY_SJ LIKE ? AND B.TZ_ART_ID=? AND B.TZ_NREG_STAT=?");

			// String TZ_START_DT = DateUtil.ISOSECDateString(new Date());

			List<Map<String, Object>> listdata = SqlQuery.queryForList(sb.toString(),
					new Object[] { "HDBM", "%" + moblieNu, artId,"1" });
			returnMap.put("data", listdata);
			break;
		case "QD":
			jacksonUtil.json2Map(strParams);
			// 签到程序
			String TZ_ART_ID = jacksonUtil.getString("artId");
			String TZ_HD_BMR_ID = jacksonUtil.getString("bmrId");
			String sql = "update PS_TZ_NAUDLIST_T set TZ_QD_TIME=?,TZ_BMCY_ZT=? where TZ_ART_ID=? and TZ_HD_BMR_ID=?";
			int i = SqlQuery.update(sql, new Object[] { new Date(), "A", TZ_ART_ID, TZ_HD_BMR_ID });
			if (i > 0) {
				returnMap.put("result", "SUC");
			} else {
				returnMap.put("result", "FAL");
			}

		}
		return jacksonUtil.Map2json(returnMap);
	}
}
