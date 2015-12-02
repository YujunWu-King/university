package com.tranzvision.gd.TZTemplateBundle.model;

public class PsTzTmpRrkfTbl extends PsTzTmpRrkfTblKey {
    private String tzParaId;

    public String getTzParaId() {
        return tzParaId;
    }

    public void setTzParaId(String tzParaId) {
        this.tzParaId = tzParaId == null ? null : tzParaId.trim();
    }
}