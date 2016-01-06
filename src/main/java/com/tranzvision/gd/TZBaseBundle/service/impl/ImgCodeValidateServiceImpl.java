package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.captcha.Patchca;

@Service("com.tranzvision.gd.TZBaseBundle.service.impl.ImgCodeValidateServiceImpl")
public class ImgCodeValidateServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		Map<String , Object> map = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String code = jacksonUtil.getString("yzm");
		// 校验验证码
		Patchca patchca = new Patchca();
		if (!patchca.verifyToken(request, code)) {
			map.put("success", "false");
		}else{
			map.put("success", "true");
		}
		return jacksonUtil.Map2json(map);
	}
}
