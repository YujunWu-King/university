/**
 * 
 */
package com.tranzvision.gd.TZBaseBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranzvision.gd.util.captcha.Patchca;

/**
 * 验证码前端控制器
 * 
 * @author SHIHUA
 * @since 2015-11-03
 */
@Controller
@RequestMapping("/captcha")
public class TzCaptchaController {

	@RequestMapping(value = { "/", "" })
	public void genCaptchaPNG(HttpServletRequest request, HttpServletResponse response) {

		Patchca patcha = new Patchca();

		patcha.setColorful();
		patcha.genCaptcha(request, response);

	}


}
