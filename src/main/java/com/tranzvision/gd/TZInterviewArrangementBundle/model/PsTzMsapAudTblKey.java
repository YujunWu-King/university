package com.tranzvision.gd.TZInterviewArrangementBundle.model;

public class PsTzMsapAudTblKey {
    private String tzClassId;

    private String tzBatchId;

    private String tzAudId;

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

    public String getTzAudId() {
        return tzAudId;
    }

    public void setTzAudId(String tzAudId) {
        this.tzAudId = tzAudId == null ? null : tzAudId.trim();
    }
}