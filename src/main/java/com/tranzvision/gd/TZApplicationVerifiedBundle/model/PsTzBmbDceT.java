package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzBmbDceT {
    private String runCntlId;

    private String tzAppTplId;

    private String tzExportTmpId;

    private String tzExcelName;

    private String tzAudList;

    public String getRunCntlId() {
        return runCntlId;
    }

    public void setRunCntlId(String runCntlId) {
        this.runCntlId = runCntlId == null ? null : runCntlId.trim();
    }

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzExportTmpId() {
        return tzExportTmpId;
    }

    public void setTzExportTmpId(String tzExportTmpId) {
        this.tzExportTmpId = tzExportTmpId == null ? null : tzExportTmpId.trim();
    }

    public String getTzExcelName() {
        return tzExcelName;
    }

    public void setTzExcelName(String tzExcelName) {
        this.tzExcelName = tzExcelName == null ? null : tzExcelName.trim();
    }

    public String getTzAudList() {
        return tzAudList;
    }

    public void setTzAudList(String tzAudList) {
        this.tzAudList = tzAudList == null ? null : tzAudList.trim();
    }
}