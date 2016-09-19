package com.tranzvision.gd.util.cms;

import static com.tranzvision.gd.util.cms.web.Constants.UTF8;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkertUtils {
	public static Template getStyleTpl(String tplSource, String tplName){
		try {
			Configuration cfg = new Configuration();
			cfg.setDefaultEncoding(UTF8);

			StringTemplateLoader strLoader = new StringTemplateLoader();
			strLoader.putTemplate(tplName, tplSource);

			cfg.setTemplateLoader(strLoader);
			cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
			return cfg.getTemplate(tplName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void processTemplate(String tplSource, String tplName,
			Map<String, Object> root, StringWriter out) {
				try {
					Configuration cfg = new Configuration();
					cfg.setDefaultEncoding(UTF8);

					StringTemplateLoader strLoader = new StringTemplateLoader();
					strLoader.putTemplate(tplName, tplSource);

					cfg.setTemplateLoader(strLoader);
					cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
					Template template = cfg.getTemplate(tplName);
					template.process(root, out);
					out.flush();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

	}
}