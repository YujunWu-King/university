package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

/**
 * @author caoy
 * @version 创建时间：2016年6月22日 下午3:37:51 类说明 PDF打印需要的一些参数的BEAN
 */
public class DataBean {
	private int rs;

	private String orgid;
	private String templateFileName;
	private String templateID;
	private String downloadFileName;
	private String filePath;
	
	private String recommendName;
	
	
	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private String pdfFieldsValues;
	private String rootpath;

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRootpath() {
		return rootpath;
	}

	public void setRootpath(String rootpath) {
		this.rootpath = rootpath;
	}

	public String getPdfFieldsValues() {
		return pdfFieldsValues;
	}

	public void setPdfFieldsValues(String pdfFieldsValues) {
		this.pdfFieldsValues = pdfFieldsValues;
	}

	private String msg;

	public int getRs() {
		return rs;
	}

	public void setRs(int rs) {
		this.rs = rs;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String getTemplateID() {
		return templateID;
	}

	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public String getMsg() {
		String msg = "";
		switch (rs) {
		case 0:
			msg = "OK";
			break;
		case -1:
			msg = "配置文件不存在";
			break;
		case -2:
			msg = "实例不存在";
			break;
		case -3:
			msg = "PDF模版文件不存在";
			break;
		case -4:
			msg = "模版不存在";
			break;
		case -5:
			msg = "PDF模版文件无可替换字段";
			break;
		case -6:
			msg = "生成文件失败";
			break;
		case -7:
			msg = "数据库操作异常";
			break;
		case -8:
			msg = "模版打印类型不是pdf模板打印";
			break;
		case -9:
			msg = "模版实例没有数据";
			break;
		case -10:
			msg = "报名人姓名不存在";
			break;
		case -11:
			msg = "报名的班级名称不存在";
			break;
		default:
			msg = "未知原因错误";
		}
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
