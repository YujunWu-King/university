/**
 * 
 */
package com.tranzvision.gd.util.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.springframework.stereotype.Service;

/**
 * 过滤非法字符工具类
 * 
 * @author SHIHUA
 * @since 2015-11-24
 */
@Service
public class TzFilterIllegalCharacter {

	private String filterRegExAll = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
	
	private String filterRegEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

	/**
	 * 过滤目录名称的非法字符
	 * 
	 * @param str
	 * @return
	 */
	public String filterDirectoryIllegalCharacter(String str) throws PatternSyntaxException {
		Pattern p = Pattern.compile(this.filterRegEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	/**
	 * 过滤所有的非法字符
	 * 
	 * @param str
	 * @return
	 */
	public String filterAllIllegalCharacter(String str) throws PatternSyntaxException {
		//先替换掉英文双引号
		str = str.replace("\"", "");
		//其他字符用正则替换
		Pattern p = Pattern.compile(this.filterRegExAll);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
