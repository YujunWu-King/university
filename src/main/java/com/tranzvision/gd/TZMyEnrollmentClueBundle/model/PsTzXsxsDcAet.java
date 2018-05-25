package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

public class PsTzXsxsDcAet {
    private String runCntlId;

    private String tzType;

    private String tzRelUrl;

    private String tzJdUrl;

    private String tzParamsStr;

    public String getRunCntlId() {
        return runCntlId;
    }

    public void setRunCntlId(String runCntlId) {
        this.runCntlId = runCntlId == null ? null : runCntlId.trim();
    }

    public String getTzType() {
        return tzType;
    }

    public void setTzType(String tzType) {
        this.tzType = tzType == null ? null : tzType.trim();
    }

    public String getTzRelUrl() {
        return tzRelUrl;
    }

    public void setTzRelUrl(String tzRelUrl) {
        this.tzRelUrl = tzRelUrl == null ? null : tzRelUrl.trim();
    }

    public String getTzJdUrl() {
        return tzJdUrl;
    }

    public void setTzJdUrl(String tzJdUrl) {
        this.tzJdUrl = tzJdUrl == null ? null : tzJdUrl.trim();
    }

    public String getTzParamsStr() {
        return tzParamsStr;
    }

    public void setTzParamsStr(String tzParamsStr) {
        this.tzParamsStr = tzParamsStr == null ? null : tzParamsStr.trim();
    }
}