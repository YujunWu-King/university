package com.tranzvision.gd.TZResourceCollectionMgBundle.model;

public class PsTzPtZyxxTbl extends PsTzPtZyxxTblKey {
    private String tzResMc;

    private String tzResFileType;

    private String tzResFileName;

    private String tzResFilePath;

    public String getTzResMc() {
        return tzResMc;
    }

    public void setTzResMc(String tzResMc) {
        this.tzResMc = tzResMc == null ? null : tzResMc.trim();
    }

    public String getTzResFileType() {
        return tzResFileType;
    }

    public void setTzResFileType(String tzResFileType) {
        this.tzResFileType = tzResFileType == null ? null : tzResFileType.trim();
    }

    public String getTzResFileName() {
        return tzResFileName;
    }

    public void setTzResFileName(String tzResFileName) {
        this.tzResFileName = tzResFileName == null ? null : tzResFileName.trim();
    }

    public String getTzResFilePath() {
        return tzResFilePath;
    }

    public void setTzResFilePath(String tzResFilePath) {
        this.tzResFilePath = tzResFilePath == null ? null : tzResFilePath.trim();
    }
}