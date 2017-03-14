package com.tranzvision.gd.TZInterviewArrangementBundle.model;

public class PsTzMsArrDceAet {
    private String runId;

    private String tzRelUrl;

    private String tzJdUrl;

    private String tzExpParamsStr;

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId == null ? null : runId.trim();
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

    public String getTzExpParamsStr() {
        return tzExpParamsStr;
    }

    public void setTzExpParamsStr(String tzExpParamsStr) {
        this.tzExpParamsStr = tzExpParamsStr == null ? null : tzExpParamsStr.trim();
    }
}