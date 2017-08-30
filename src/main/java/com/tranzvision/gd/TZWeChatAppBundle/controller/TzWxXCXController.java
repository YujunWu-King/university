package com.tranzvision.gd.TZWeChatAppBundle.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZWeChatBundle.dao.PsTzWxGzhcsTMapper;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxGzhcsTKey;
import com.tranzvision.gd.TZWeChatAppBundle.service.impl.TzSignInfoImpl;
import com.tranzvision.gd.TZWeChatAppBundle.service.impl.TzWeChartSign;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Controller
@RequestMapping(value = { "/wxXCX" })
public class TzWxXCXController {
	@Autowired
	private SqlQuery jdbcTemplate;	
	
	@Autowired
	private TzWeChartSign tzWeChartJSSDKSign;
		
	private static String APP_ID="wx59bee93825611289";//小程序APPID
	
	private static String APP_SECRET="cd5e2ab8b9b06142a44070c238507544";//小程序密钥
	
	/**
	 * xcxLogin
	 * 小程序 code 获取 openid
	 * 
	 */
	 @RequestMapping(value = "xcxLogin",  produces = "text/html;charset=UTF-8")
	 @ResponseBody
     public String weixinRedirect(HttpServletRequest request, HttpServletResponse response) {
		String jsCode = request.getParameter("code");
		System.out.println("=====code====");
		System.out.println(jsCode);
		Map<String,Object> map = tzWeChartJSSDKSign.getWxXCXOpenId(APP_ID, APP_SECRET,jsCode);
		JacksonUtil jacksonUtil = new JacksonUtil();
		System.out.println("json======"+jacksonUtil.Map2json(map));
		return jacksonUtil.Map2json(map);
     }
	
		/**
		 * xcxLogin
		 * 小程序 code 获取 openid
		 * 
		 */
		 @RequestMapping(value = "isLogin",  produces = "text/html;charset=UTF-8")
		 @ResponseBody
	     public String isLogin(HttpServletRequest request, HttpServletResponse response) {
			String openid = request.getParameter("openid");
			System.out.println("=====openid====");
			System.out.println(openid);
			String sql = "SELECT NAME  FROM WECHAT_USER WHERE openid= ? ";
			String name =jdbcTemplate.queryForObject(sql, new Object[] {openid },"String");
			Map<String, Object> map = new HashMap<String, Object>();
            if(name==null){
            	map.put("isLogin", false);
            }else{
            	map.put("isLogin", true);
            	map.put("name", name);
            }
			JacksonUtil jacksonUtil = new JacksonUtil();
			System.out.println("json======"+jacksonUtil.Map2json(map));
			return jacksonUtil.Map2json(map);
	     }
		
	
	
	
	
	/**
	 * 绑定
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String openid = request.getParameter("openid");
		String name = request.getParameter("name");
		if(openid==null||"".equals(openid)||name==null||"".equals(name)||"undefined".equals(name)||"undefined".equals(openid)){
			jsonMap.put("success", false);
			jsonMap.put("msg", "数据不正确，请检查后重试");
		}else{
			String sql = "INSERT INTO WECHAT_USER(OPENID,NAME) VALUES(?,?) ";
			int i= jdbcTemplate.update(sql, new Object[] { openid, name});
			if(i==-1){//绑定失败
				jsonMap.put("success", false);
				jsonMap.put("msg", "微信已绑定，请刷新");
			}else{
				jsonMap.put("success", true);
			}
		}
		return jacksonUtil.Map2json(jsonMap);
	}
	
	/**
	 * 签到设备
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "signInfo", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String signInfo(HttpServletRequest request, HttpServletResponse response) {
		String major = request.getParameter("major");
		String minor =request.getParameter("minor");
		String uuid = request.getParameter("uuid");
		System.out.println("==========");
		System.out.println("uuid ="+uuid);
		System.out.println("major ="+major);
		System.out.println("minor ="+minor);
		JacksonUtil jacksonUtil = new JacksonUtil();
		String sql = "SELECT IBEACON_NAME  FROM IBEACON_INFO WHERE MAJOR= ? AND MINOR=? AND UUID=?";
		String  ibeaconName =jdbcTemplate.queryForObject(sql, new Object[] { major, minor, uuid },"String");
		System.out.println("===========");
		System.out.println("ibeaconName==" +ibeaconName);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(ibeaconName!=null){
			jsonMap.put("success", true);
			jsonMap.put("ibeaconName", ibeaconName);
		}else{
			jsonMap.put("success", false);
		}
		return jacksonUtil.Map2json(jsonMap);
	}
	
	/**
	 * 签到
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "signIn", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String signIn(HttpServletRequest request, HttpServletResponse response) {
		String openId = request.getParameter("openId");
		String ibeaconName = request.getParameter("ibeaconName");
		String name = request.getParameter("name");
		String accuracy = request.getParameter("accuracy");
		System.out.println("==========");
		System.out.println("openId ="+openId);
		System.out.println("ibeaconName ="+ibeaconName);
		System.out.println("name ="+name);
		System.out.println("accuracy ="+accuracy);
		JacksonUtil jacksonUtil = new JacksonUtil();
		String sql = "INSERT INTO SIGN_INFO (OPENID,NICK_NAME,IBEACON_NAME,SIGN_TIME,SIGN_ACCURACY) VALUES (?,?,?,?,?)";
		int i= jdbcTemplate.update(sql,  new Object[]{openId,name,ibeaconName,new Date(),accuracy});
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(i==-1){//签到失败
			jsonMap.put("success", false);
		}else{
			jsonMap.put("success", true);
		}
		return jacksonUtil.Map2json(jsonMap);
	}
	
	
	
	/**
	 * 签到记录展示
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "showInfo", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String showInfo(HttpServletRequest request, HttpServletResponse response) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		String openId = request.getParameter("openId");
		int page = Integer.parseInt(request.getParameter("pageindex"));
		int callcount = Integer.parseInt(request.getParameter("callbackcount"));
		int start = (page-1)*callcount;
		System.out.println("==========");
		System.out.println("openId ="+openId);
		System.out.println("page ="+page);
		System.out.println("callcount ="+callcount);
		System.out.println("start ="+start);
		String sql = "SELECT * FROM  SIGN_INFO WHERE OPENID=? ORDER BY SIGN_TIME DESC LIMIT ?,?";
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { openId,start,callcount });
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("ibeaconName", (String) list.get(i).get("IBEACON_NAME"));
				mapList.put("signTime", sdf.format((Date)list.get(i).get("SIGN_TIME")));
				listData.add(mapList);
			}
			mapRet.put("signInfoList", listData);
		}
		mapRet.put("success", true);
		return jacksonUtil.Map2json(mapRet);
	}
	/**
	 * 获取accessToken
	 */
	@RequestMapping(value = "getToken", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getToken(HttpServletRequest request, HttpServletResponse response) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();   
		String accessToken = tzWeChartJSSDKSign.getAccessToken(APP_ID, APP_SECRET,"GZ");
		if(!"".equals(accessToken)){
			mapRet.put("success", true);
		}else{
			mapRet.put("success", false);
		}
		mapRet.put("accessToken", accessToken);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		System.out.println("json======"+jacksonUtil.Map2json(mapRet));
		return jacksonUtil.Map2json(mapRet);
	}
}
