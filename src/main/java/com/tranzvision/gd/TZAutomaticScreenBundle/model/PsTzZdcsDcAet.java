package com.tranzvision.gd.TZAutomaticScreenBundle.model;

public class PsTzZdcsDcAet {
    private String runCntlId;

    private String tzClassId;

    private String tzBatchId;

    private String tzRelUrl;

    private String tzJdUrl;

    private String tzParamsStr;

    public String getRunCntlId() {
        return runCntlId;
    }

    public void setRunCntlId(String runCntlId) {
        this.runCntlId = runCntlId == null ? null : runCntlId.trim();
    }

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzBatchId() {
        return tzBatchId;
    }

    public void setTzBatchId(String tzBatchId) {
        this.tzBatchId = tzBatchId == null ? null : tzBatchId.trim();
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