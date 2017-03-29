package com.tranzvision.gd.TZApplicationExportTemplateBundle.model;

public class PsTzExpFrmFldT extends PsTzExpFrmFldTKey {
    private String tzDcFieldName;

    private String tzAppclsId;

    private Integer tzSortNum;

    private String tzDcFieldFgf;

    private Short tzDcColWidth;

    private String tzDcColFilter;

    public String getTzDcFieldName() {
        return tzDcFieldName;
    }

    public void setTzDcFieldName(String tzDcFieldName) {
        this.tzDcFieldName = tzDcFieldName == null ? null : tzDcFieldName.trim();
    }

    public String getTzAppclsId() {
        return tzAppclsId;
    }

    public void setTzAppclsId(String tzAppclsId) {
        this.tzAppclsId = tzAppclsId == null ? null : tzAppclsId.trim();
    }

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }

    public String getTzDcFieldFgf() {
        return tzDcFieldFgf;
    }

    public void setTzDcFieldFgf(String tzDcFieldFgf) {
        this.tzDcFieldFgf = tzDcFieldFgf == null ? null : tzDcFieldFgf.trim();
    }

    public Short getTzDcColWidth() {
        return tzDcColWidth;
    }

    public void setTzDcColWidth(Short tzDcColWidth) {
        this.tzDcColWidth = tzDcColWidth;
    }

    public String getTzDcColFilter() {
        return tzDcColFilter;
    }

    public void setTzDcColFilter(String tzDcColFilter) {
        this.tzDcColFilter = tzDcColFilter == null ? null : tzDcColFilter.trim();
    }
}