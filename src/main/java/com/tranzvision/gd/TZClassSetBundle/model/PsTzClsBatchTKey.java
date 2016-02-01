package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsBatchTKey {
    private String tzClassId;

    private String tzBatchId;

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
}