package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzDxyjqfTbl {
    private String runCntlId;

    private String tzMlsmQfpcId;

    private String tzEmlSmsTaskId;

    public String getRunCntlId() {
        return runCntlId;
    }

    public void setRunCntlId(String runCntlId) {
        this.runCntlId = runCntlId == null ? null : runCntlId.trim();
    }

    public String getTzMlsmQfpcId() {
        return tzMlsmQfpcId;
    }

    public void setTzMlsmQfpcId(String tzMlsmQfpcId) {
        this.tzMlsmQfpcId = tzMlsmQfpcId == null ? null : tzMlsmQfpcId.trim();
    }

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }
}