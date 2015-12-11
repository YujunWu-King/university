package com.tranzvision.gd.util.cms.action.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 文本字符串截断
 * 
 * @author WRL
 * 
 */
public class TextCutDirective implements TemplateDirectiveModel {

	public static final String PARAM_S = "s";
	public static final String PARAM_LEN = "len";
	public static final String PARAM_APPEND = "append";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVar2,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String s = DirectiveUtils.getString(PARAM_S, params);
		Integer len = DirectiveUtils.getInt(PARAM_LEN, params);
		String append = DirectiveUtils.getString(PARAM_APPEND, params);

		if (StringUtils.isNotBlank(s)) {
			Writer out = env.getOut();
			if (len != null) {
				out.append(DirectiveUtils.textCut(s, len, append));
			} else {
				out.append(s);
			}
		}
	}
}
