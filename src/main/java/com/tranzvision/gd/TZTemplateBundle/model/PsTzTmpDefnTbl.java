package com.tranzvision.gd.TZTemplateBundle.model;

import java.util.Date;

public class PsTzTmpDefnTbl extends PsTzTmpDefnTblKey {
    private String tzYmbName;

    private String tzUseFlag;

    private String tzDefaultOpen;

    private String tzExtendCTmpl;

    private String tzDtgxhhbFlg;

    private String tzYmbDesc;

    private String tzEmlservId;

    private String tzSmsServId;

    private String tzYmbCslbm;

    private String tzYmbNrgm;

    private String tzAppcls;

    private String tzDsrecName;

    private String tzDsrecAlias;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzYmbName() {
        return tzYmbName;
    }

    public void setTzYmbName(String tzYmbName) {
        this.tzYmbName = tzYmbName == null ? null : tzYmbName.trim();
    }

    public String getTzUseFlag() {
        return tzUseFlag;
    }

    public void setTzUseFlag(String tzUseFlag) {
        this.tzUseFlag = tzUseFlag == null ? null : tzUseFlag.trim();
    }

    public String getTzDefaultOpen() {
        return tzDefaultOpen;
    }

    public void setTzDefaultOpen(String tzDefaultOpen) {
        this.tzDefaultOpen = tzDefaultOpen == null ? null : tzDefaultOpen.trim();
    }

    public String getTzExtendCTmpl() {
        return tzExtendCTmpl;
    }

    public void setTzExtendCTmpl(String tzExtendCTmpl) {
        this.tzExtendCTmpl = tzExtendCTmpl == null ? null : tzExtendCTmpl.trim();
    }

    public String getTzDtgxhhbFlg() {
        return tzDtgxhhbFlg;
    }

    public void setTzDtgxhhbFlg(String tzDtgxhhbFlg) {
        this.tzDtgxhhbFlg = tzDtgxhhbFlg == null ? null : tzDtgxhhbFlg.trim();
    }

    public String getTzYmbDesc() {
        return tzYmbDesc;
    }

    public void setTzYmbDesc(String tzYmbDesc) {
        this.tzYmbDesc = tzYmbDesc == null ? null : tzYmbDesc.trim();
    }

    public String getTzEmlservId() {
        return tzEmlservId;
    }

    public void setTzEmlservId(String tzEmlservId) {
        this.tzEmlservId = tzEmlservId == null ? null : tzEmlservId.trim();
    }

    public String getTzSmsServId() {
        return tzSmsServId;
    }

    public void setTzSmsServId(String tzSmsServId) {
        this.tzSmsServId = tzSmsServId == null ? null : tzSmsServId.trim();
    }

    public String getTzYmbCslbm() {
        return tzYmbCslbm;
    }

    public void setTzYmbCslbm(String tzYmbCslbm) {
        this.tzYmbCslbm = tzYmbCslbm == null ? null : tzYmbCslbm.trim();
    }

    public String getTzYmbNrgm() {
        return tzYmbNrgm;
    }

    public void setTzYmbNrgm(String tzYmbNrgm) {
        this.tzYmbNrgm = tzYmbNrgm == null ? null : tzYmbNrgm.trim();
    }

    public String getTzAppcls() {
        return tzAppcls;
    }

    public void setTzAppcls(String tzAppcls) {
        this.tzAppcls = tzAppcls == null ? null : tzAppcls.trim();
    }

    public String getTzDsrecName() {
        return tzDsrecName;
    }

    public void setTzDsrecName(String tzDsrecName) {
        this.tzDsrecName = tzDsrecName == null ? null : tzDsrecName.trim();
    }

    public String getTzDsrecAlias() {
        return tzDsrecAlias;
    }

    public void setTzDsrecAlias(String tzDsrecAlias) {
        this.tzDsrecAlias = tzDsrecAlias == null ? null : tzDsrecAlias.trim();
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
}