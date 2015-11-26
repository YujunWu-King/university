package com.baidu.ueditor.upload;

import com.baidu.ueditor.define.State;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;
	
	private String orgid;

	public Uploader(HttpServletRequest request, String orgid, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
		this.orgid = orgid;
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request, this.orgid, this.request.getParameter(filedName),
					this.conf);
		} else {
			state = BinaryUploader.save(this.request, this.orgid, this.conf);
		}

		return state;
	}
}
