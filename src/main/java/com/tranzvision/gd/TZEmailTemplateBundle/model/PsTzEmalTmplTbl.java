package com.tranzvision.gd.TZEmailTemplateBundle.model;

import java.util.Date;

public class PsTzEmalTmplTbl extends PsTzEmalTmplTblKey {
    private String tzTmplName;

    private String tzUseFlag;

    private String tzYmbId;

    private String tzEmlservId;

    private String tzTmplDesc;

    private String tzMalSubjuect;

    private String tzDynamicFlag;

    private String tzWebmalFlag;

    private String tzEmlIfPrt;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzMalContent;

    public String getTzTmplName() {
        return tzTmplName;
    }

    public void setTzTmplName(String tzTmplName) {
        this.tzTmplName = tzTmplName == null ? null : tzTmplName.trim();
    }

    public String getTzUseFlag() {
        return tzUseFlag;
    }

    public void setTzUseFlag(String tzUseFlag) {
        this.tzUseFlag = tzUseFlag == null ? null : tzUseFlag.trim();
    }

    public String getTzYmbId() {
        return tzYmbId;
    }

    public void setTzYmbId(String tzYmbId) {
        this.tzYmbId = tzYmbId == null ? null : tzYmbId.trim();
    }

    public String getTzEmlservId() {
        return tzEmlservId;
    }

    public void setTzEmlservId(String tzEmlservId) {
        this.tzEmlservId = tzEmlservId == null ? null : tzEmlservId.trim();
    }

    public String getTzTmplDesc() {
        return tzTmplDesc;
    }

    public void setTzTmplDesc(String tzTmplDesc) {
        this.tzTmplDesc = tzTmplDesc == null ? null : tzTmplDesc.trim();
    }

    public String getTzMalSubjuect() {
        return tzMalSubjuect;
    }

    public void setTzMalSubjuect(String tzMalSubjuect) {
        this.tzMalSubjuect = tzMalSubjuect == null ? null : tzMalSubjuect.trim();
    }

    public String getTzDynamicFlag() {
        return tzDynamicFlag;
    }

    public void setTzDynamicFlag(String tzDynamicFlag) {
        this.tzDynamicFlag = tzDynamicFlag == null ? null : tzDynamicFlag.trim();
    }

    public String getTzWebmalFlag() {
        return tzWebmalFlag;
    }

    public void setTzWebmalFlag(String tzWebmalFlag) {
        this.tzWebmalFlag = tzWebmalFlag == null ? null : tzWebmalFlag.trim();
    }

    public String getTzEmlIfPrt() {
        return tzEmlIfPrt;
    }

    public void setTzEmlIfPrt(String tzEmlIfPrt) {
        this.tzEmlIfPrt = tzEmlIfPrt == null ? null : tzEmlIfPrt.trim();
    }

    public Date getRowAddedDttm() {
        return rowAddedDttm;
    }

    public void setRowAddedDttm(Date rowAddedDttm) {
        this.rowAddedDttm = rowAddedDttm;
    }

    public String getRowAddedOprid() {
        return rowAddedOprid;
    }

    public void setRowAddedOprid(String rowAddedOprid) {
        this.rowAddedOprid = rowAddedOprid == null ? null : rowAddedOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }

    public Date getSyncdttm() {
        return syncdttm;
    }

    public void setSyncdttm(Date syncdttm) {
        this.syncdttm = syncdttm;
    }

    public String getTzMalContent() {
        return tzMalContent;
    }

    public void setTzMalContent(String tzMalContent) {
        this.tzMalContent = tzMalContent == null ? null : tzMalContent.trim();
    }
}