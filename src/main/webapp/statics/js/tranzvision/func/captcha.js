/**
 * 生成验证码的url
 * 
 * @author SHIHUA
 * @since 2015-11-04
 */
function GenCaptchaUrl() {

	var _captchaURL = TzUniversityContextPath + "/captcha";
	
	return _captchaURL + "?" + Math.random();

}