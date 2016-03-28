/**
 * 
 */
package com.tranzvision.gd.util.captcha;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.bingoohuang.patchca.color.ColorFactory;
import com.github.bingoohuang.patchca.color.SingleColorFactory;
import com.github.bingoohuang.patchca.filter.predefined.*;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.utils.encoder.EncoderHelper;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
import com.tranzvision.gd.util.session.TzSession;

/**
 * 生成验证码图片及session
 * 
 * @author SHIHUA
 * @since 2015-11-03
 */
public class Patchca {

	private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();

	private static RandomWordFactory wf = new RandomWordFactory();

	private static Random random = new Random();

	private TzSession tzSession = null;

	private GetCookieSessionProps getSessionProps;

	final static String tokenName = "captchaToken";

	/**
	 * 验证码最小长度
	 */
	private int minLength = 4;

	/**
	 * 验证码最大长度
	 */
	private int maxLength = 4;

	/**
	 * 构造函数
	 */
	public Patchca() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		getSessionProps = (GetCookieSessionProps) getSpringBeanUtil.getAutowiredSpringBean("GetCookieSessionProps");
	}

	/**
	 * 实例化tzSession对象
	 * 
	 * @param request
	 */
	private void insTzSession(HttpServletRequest request) {
		if (null == tzSession)
			tzSession = new TzSession(request);
	}

	/**
	 * 设置验证码的最小长度、最大长度
	 * 
	 * @param minLength
	 * @param maxLength
	 */
	public void setMinMaxLength(int minLength, int maxLength) {
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	/**
	 * 设置验证码图片为多色彩
	 */
	public void setColorful() {
		cs.setColorFactory(new ColorFactory() {
			@Override
			public Color getColor(int x) {
				int[] c = new int[3];
				int i = random.nextInt(c.length);
				for (int fi = 0; fi < c.length; fi++) {
					if (fi == i) {
						c[fi] = random.nextInt(71);
					} else {
						c[fi] = random.nextInt(256);
					}
				}
				return new Color(c[0], c[1], c[2]);
			}
		});

	}

	/**
	 * 设置验证码图片为单一颜色值
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setSingleColor(int r, int g, int b) {
		cs.setColorFactory(new SingleColorFactory(new Color(r, g, b)));
	}

	/**
	 * 设置验证码的随机文字内容
	 */
	public void setCharacters() {
		wf.setCharacters("23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ");
	}

	/**
	 * 随机生成一种验证码样式
	 * 
	 * @param request
	 * @param response
	 */
	public void genCaptcha(HttpServletRequest request, HttpServletResponse response) {
		int type = random.nextInt(5) + 1;
		this.genCaptcha(request, response, type);
	}

	/**
	 * 生成指定类型的验证码样式（type值 1-5）
	 * 
	 * @param request
	 * @param response
	 * @param type
	 * @return boolean
	 */
	public boolean genCaptcha(HttpServletRequest request, HttpServletResponse response, int type) {
		this.setCharacters();
		wf.setMinLength(minLength);
		wf.setMaxLength(maxLength);
		cs.setWordFactory(wf);

		switch (type) {
		case 1:
			cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
			break;
		case 2:
			cs.setFilterFactory(new MarbleRippleFilterFactory());
			break;
		case 3:
			cs.setFilterFactory(new DoubleRippleFilterFactory());
			break;
		case 4:
			cs.setFilterFactory(new WobbleRippleFilterFactory());
			break;
		case 5:
			cs.setFilterFactory(new DiffuseRippleFilterFactory());
			break;
		}

		try {
			this.insTzSession(request);
			this.removeToken(request);
			this.setResponseHeaders(response);
			String token = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
			tzSession.addSession(tokenName, token);
			// System.out.println("SessionID=" + tzSession.getSessionId() +
			// ",token=" + token);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 设置HttpHeader
	 * 
	 * @param response
	 */
	protected void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}

	/**
	 * 获取session中的验证码值
	 * 
	 * @param request
	 * @return String
	 */
	public String getToken(HttpServletRequest request) {
		this.insTzSession(request);
		Object tokenObject = tzSession.getSession(tokenName);
		if (tokenObject == null) {
			return null;
		}
		return tokenObject.toString();
	}

	/**
	 * 销毁Session中的token值
	 * 
	 * @param request
	 */
	public void removeToken(HttpServletRequest request) {
		this.insTzSession(request);
		tzSession.removeSession(tokenName);
	}

	/**
	 * 校验输入的验证码是否正确
	 * 
	 * @param request
	 * @param ckToken
	 * @return boolean
	 */
	public boolean verifyToken(HttpServletRequest request, String ckToken) {

		boolean debugging = getSessionProps.getDebug();
		if (debugging) {
			return true;
		}

		String token = this.getToken(request);
		if (token == null) {
			return false;
		}

		token = token.toUpperCase();
		if (token.equals(ckToken.trim().toUpperCase())) {
			// 销毁sessionToken
			// this.removeToken(request);
			return true;
		}
		return false;
	}

}
