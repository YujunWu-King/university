package com.tranzvision.gd.TZApplicationExportTemplateBundle.model;

public class PsTzFrmFldGlT extends PsTzFrmFldGlTKey {
    private String tzCodeTableId;

    private Integer tzSortNum;

    public String getTzCodeTableId() {
        return tzCodeTableId;
    }

    public void setTzCodeTableId(String tzCodeTableId) {
        this.tzCodeTableId = tzCodeTableId == null ? null : tzCodeTableId.trim();
    }

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }
}