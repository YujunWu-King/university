package com.tranzvision.gd.TZAuthBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.collectsecurity.SecurityForCollectFee;

/**
 * 电话盒子相关接口
 * 
 * @author caoy
 *
 */
@Controller
@RequestMapping(value = { "/box" })
public class TzLoginForBoxController {

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	/**
	 * 登录接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "login", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> jsonMap = null;
		JacksonUtil jacksonUtil = new JacksonUtil();

		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String timestamp = request.getParameter("timestamp");
		String authenticator = request.getParameter("authenticator");
		// username + pwd + timestamp
		LinkedHashMap<String, String> tm = new LinkedHashMap<String, String>();
		tm.put("username", username);
		tm.put("pwd", pwd);
		tm.put("timestamp", timestamp);
		tm.put("authenticator", authenticator);

		// 校验传入参数
		jsonMap = this.checkParameter(tm);
		if (jsonMap != null) {
			return jacksonUtil.Map2json(jsonMap);
		}
		// 登录处理 稍后更新

		// 生产返回的
		String usertoken = "TEST";
		String oprid = "TEST";
		jsonMap = new HashMap<String, Object>();
		jsonMap.put("res_code", "0");
		jsonMap.put("res_message", "Success");
		jsonMap.put("usertoken", usertoken);
		jsonMap.put("oprid", oprid);

		return jacksonUtil.Map2json(jsonMap);
	}

	/**
	 * 传入参数的校验
	 * 
	 * @param tm
	 * @return
	 */
	private Map<String, Object> checkParameter(LinkedHashMap<String, String> tm) {
		Map<String, Object> jsonMap = null;
		String key = "";
		int len = tm.size();
		int i = 0;
		StringBuffer sb = new StringBuffer();
		String Authenticator = "";
		for (Map.Entry<String, String> entry : tm.entrySet()) {
			key = entry.getKey();
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			if (entry.getValue() == null || StringUtils.isBlank(entry.getValue().toString())) {
				jsonMap = new HashMap<String, Object>();
				jsonMap.put("res_code", "-0001");
				jsonMap.put("res_message", "传入参数" + key + "为空");
				return jsonMap;
			}
			// 是否超时
			if (key.equals("timestamp")) {
				try {
					long timep = Long.parseLong(entry.getValue().toString());
					long time = System.currentTimeMillis();
					if (time - timep > 2 * 60 * 1000) {
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("res_code", "-0001");
						jsonMap.put("res_message", "登录超时");
						return jsonMap;
					}
				} catch (Exception e) {
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("res_code", "-0001");
					jsonMap.put("res_message", "时间戳格式错误");
					return jsonMap;
				}
			}
			if (i < len - 1) {
				sb.append(entry.getValue().toString());
			}
			if (i == len - 1 && key.equals("authenticator")) {
				Authenticator = entry.getValue().toString();
			}
			i++;
		}

		// 3 校验 加密是否正确
		String securitykey = getSysHardCodeVal.getBoxKey();

		System.out.println("Authenticator:" + Authenticator);
		System.out.println("securitykey:" + securitykey);
		System.out.println("dev:" + sb.toString());
		int r = SecurityForCollectFee.checkAuthenticator(Authenticator, securitykey, sb.toString());
		if (r != 0) {
			jsonMap = new HashMap<String, Object>();
			jsonMap.put("res_code", "-0001");
			jsonMap.put("res_message", "解密失败");
			return jsonMap;
		}
		return jsonMap;
	}

	/**
	 * 弹屏请求接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bombScreen", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doBombScreen(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> jsonMap = null;
		JacksonUtil jacksonUtil = new JacksonUtil();

		String usertoken = request.getParameter("usertoken");
		String tel = request.getParameter("tel");
		String callid = request.getParameter("callid");
		String authenticator = request.getParameter("authenticator");
		String timestamp = request.getParameter("timestamp");

		// 校验传入参数
		// usertoken+tel+callid+timestamp
		LinkedHashMap<String, String> tm = new LinkedHashMap<String, String>();
		tm.put("usertoken", usertoken);
		tm.put("tel", tel);
		tm.put("callid", callid);
		tm.put("timestamp", timestamp);
		tm.put("authenticator", authenticator);

		// 校验传入参数
		jsonMap = this.checkParameter(tm);
		if (jsonMap != null) {
			return jacksonUtil.Map2json(jsonMap);

		}

		jsonMap = new HashMap<String, Object>();
		return "The Page OK";
	}

}
