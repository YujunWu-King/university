package com.tranzvision.gd.TZInterviewArrangementBundle.model;

public class PsTzMsyyKsTblKey {
    private String tzClassId;

    private String tzBatchId;

    private String tzMsPlanSeq;

    private String oprid;

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

    public String getTzMsPlanSeq() {
        return tzMsPlanSeq;
    }

    public void setTzMsPlanSeq(String tzMsPlanSeq) {
        this.tzMsPlanSeq = tzMsPlanSeq == null ? null : tzMsPlanSeq.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }
}