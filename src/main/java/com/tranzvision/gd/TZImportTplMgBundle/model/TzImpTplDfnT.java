package com.tranzvision.gd.TZImportTplMgBundle.model;

public class TzImpTplDfnT {
    private String tzTplId;

    private String tzTplName;

    private String tzTargetTbl;

    private String tzJavaClass;

    private String tzExcelTpl;

    private String tzEnableMapping;

    public String getTzTplId() {
        return tzTplId;
    }

    public void setTzTplId(String tzTplId) {
        this.tzTplId = tzTplId == null ? null : tzTplId.trim();
    }

    public String getTzTplName() {
        return tzTplName;
    }

    public void setTzTplName(String tzTplName) {
        this.tzTplName = tzTplName == null ? null : tzTplName.trim();
    }

    public String getTzTargetTbl() {
        return tzTargetTbl;
    }

    public void setTzTargetTbl(String tzTargetTbl) {
        this.tzTargetTbl = tzTargetTbl == null ? null : tzTargetTbl.trim();
    }

    public String getTzJavaClass() {
        return tzJavaClass;
    }

    public void setTzJavaClass(String tzJavaClass) {
        this.tzJavaClass = tzJavaClass == null ? null : tzJavaClass.trim();
    }

    public String getTzExcelTpl() {
        return tzExcelTpl;
    }

    public void setTzExcelTpl(String tzExcelTpl) {
        this.tzExcelTpl = tzExcelTpl == null ? null : tzExcelTpl.trim();
    }

    public String getTzEnableMapping() {
        return tzEnableMapping;
    }

    public void setTzEnableMapping(String tzEnableMapping) {
        this.tzEnableMapping = tzEnableMapping == null ? null : tzEnableMapping.trim();
    }
}