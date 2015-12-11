package com.tranzvision.gd.util.cms.web.freemarker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * Freemarker标签工具类
 * 
 * @author WRL
 * 
 */
public abstract class DirectiveUtils {
	
	/**
	 * 输出参数：列表数据
	 */
	public static final String OUT_LIST = "tag_list";
	
	/**
	 * 输出参数：图片列表数据
	 */
	public static final String OUT_IMGLIST = "tag_imglist";
	
	/**
	 * 输出参数：附件列表数据
	 */
	public static final String OUT_ATTLIST = "tag_attlist";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 输出参数：对象数据
	 */
	public static final String OUT_BEAN = "tag_bean";

	/**
	 * 输出参数：分页数据
	 */
	public static final String OUT_PAGINATION = "tag_pagination";
	/**
	 * 参数：是否调用模板。
	 */
	public static final String PARAM_TPL = "tpl";
	/**
	 * 参数：次级模板名称
	 */
	public static final String PARAM_TPL_SUB = "tplSub";

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}
	/**
	 * 将params的值复制到variable中
	 * 
	 * @param env
	 * @param params
	 * @return 原Variable中的值
	 * @throws TemplateException
	 */
	public static Map<String, TemplateModel> addParamsToVariable(
			Environment env, Map<String, TemplateModel> params)
			throws TemplateException {
		Map<String, TemplateModel> origMap = new HashMap<String, TemplateModel>();
		if (params.size() <= 0) {
			return origMap;
		}
		Set<Map.Entry<String, TemplateModel>> entrySet = params.entrySet();
		String key;
		TemplateModel value;
		for (Map.Entry<String, TemplateModel> entry : entrySet) {
			key = entry.getKey();
			value = env.getVariable(key);
			if (value != null) {
				origMap.put(key, value);
			}
			env.setVariable(key, entry.getValue());
		}
		return origMap;
	}

	/**
	 * 将variable中的params值移除
	 * 
	 * @param env
	 * @param params
	 * @param origMap
	 * @throws TemplateException
	 */
	public static void removeParamsFromVariable(Environment env,
			Map<String, TemplateModel> params,
			Map<String, TemplateModel> origMap) throws TemplateException {
		if (params.size() <= 0) {
			return;
		}
		for (String key : params.keySet()) {
			env.setVariable(key, origMap.get(key));
		}
	}


	public static String getString(String name,
			Map<String, TemplateModel> params) throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) model).getAsString();
		} else if ((model instanceof TemplateNumberModel)) {
			return ((TemplateNumberModel) model).getAsNumber().toString();
		} else {
			throw new MustStringException(name);
		}
	}

	public static Long getLong(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return null;
			}
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				throw new MustNumberException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().longValue();
		} else {
			throw new MustNumberException(name);
		}
	}

	public static Integer getInt(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			if (StringUtils.isBlank(s)) {
				return null;
			}
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				throw new MustNumberException(name);
			}
		} else if (model instanceof TemplateNumberModel) {
			return ((TemplateNumberModel) model).getAsNumber().intValue();
		} else {
			throw new MustNumberException(name);
		}
	}

	public static String[] getStringArray(String name,
			Map<String, TemplateModel> params) throws TemplateException {
		String str = DirectiveUtils.getString(name, params);
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] arr = StringUtils.split(str, ',');
		String[] ids = new String[arr.length];
		int i = 0;
		try {
			for (String s : arr) {
				ids[i++] = s;
			}
			return ids;
		} catch (NumberFormatException e) {
			throw new MustSplitNumberException(name, e);
		}
	}

	public static Boolean getBool(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateBooleanModel) {
			return ((TemplateBooleanModel) model).getAsBoolean();
		} else if (model instanceof TemplateNumberModel) {
			return !(((TemplateNumberModel) model).getAsNumber().intValue() == 0);
		} else if (model instanceof TemplateScalarModel) {
			String s = ((TemplateScalarModel) model).getAsString();
			// 空串应该返回null还是true呢？
			if (!StringUtils.isBlank(s)) {
				return !(s.equals("0") || s.equalsIgnoreCase("false") || s
						.equalsIgnoreCase("f"));
			} else {
				return null;
			}
		} else {
			throw new MustBooleanException(name);
		}
	}

	public static Date getDate(String name, Map<String, TemplateModel> params)
			throws TemplateException {
		TemplateModel model = params.get(name);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateDateModel) {
			return ((TemplateDateModel) model).getAsDate();
		} else if (model instanceof TemplateScalarModel) {
			DateTypeEditor editor = new DateTypeEditor();
			editor.setAsText(((TemplateScalarModel) model).getAsString());
			return (Date) editor.getValue();
		} else {
			throw new MustDateException(name);
		}
	}

	/**
	 * 模板调用类型
	 * 
	 * @author WRL
	 */
	public enum InvokeType {
		body, userDefined
	};

	/**
	 * 是否调用模板
	 * 
	 * 0：不调用，使用标签的body；1：调用用户预定义模板。默认：0。
	 * 
	 * @param params
	 * @return
	 * @throws TemplateException
	 */
	public static InvokeType getInvokeType(Map<String, TemplateModel> params)
			throws TemplateException {
		String tpl = getString(PARAM_TPL, params);
		if ("1".equals(tpl)) {
			return InvokeType.userDefined;
		} else {
			return InvokeType.body;
		}
	}

	public static String getStringIds(String name,
			Map<String, TemplateModel> params) throws TemplateException {
		String str = DirectiveUtils.getString(name, params);
		if (StringUtils.isBlank(str)) {
			return null;
		}

		return StringUtils.replace(str, ",", "','");
	}
}
