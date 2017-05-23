package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcwjDcAet {
    private String runCntlId;

    private String tzDcWjId;

    private String tzRelUrl;

    private String tzJdUrl;

    public String getRunCntlId() {
        return runCntlId;
    }

    public void setRunCntlId(String runCntlId) {
        this.runCntlId = runCntlId == null ? null : runCntlId.trim();
    }

    public String getTzDcWjId() {
        return tzDcWjId;
    }

    public void setTzDcWjId(String tzDcWjId) {
        this.tzDcWjId = tzDcWjId == null ? null : tzDcWjId.trim();
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
}