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

	private String filterRegEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

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

}
