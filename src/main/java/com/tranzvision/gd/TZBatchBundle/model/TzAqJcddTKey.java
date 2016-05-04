package com.tranzvision.gd.TZBatchBundle.model;

public class TzAqJcddTKey {
    private String classid;

    private String tzComId;

    private String tzJgId;

    private String tzJcMc;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid == null ? null : classid.trim();
    }

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzJcMc() {
        return tzJcMc;
    }

    public void setTzJcMc(String tzJcMc) {
        this.tzJcMc = tzJcMc == null ? null : tzJcMc.trim();
    }
}