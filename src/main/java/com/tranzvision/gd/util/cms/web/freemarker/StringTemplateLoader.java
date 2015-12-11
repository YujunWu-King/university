package com.tranzvision.gd.util.cms.web.freemarker;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.lang3.StringUtils;

import freemarker.cache.TemplateLoader;


/**
 * 通过字符串加载模板
 * 
 * @author WRL
 *
 */
public class StringTemplateLoader implements TemplateLoader {

	private String template;
	
	public StringTemplateLoader(String template) {
		this.template = template;
		if(StringUtils.isBlank(template)){
			this.template = "";
		}
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
		((StringReader)templateSource).close();

	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return new StringReader(template);
	}

	@Override
	public long getLastModified(Object templateSource) {
		return 0;
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return (Reader)templateSource;
	}

}
