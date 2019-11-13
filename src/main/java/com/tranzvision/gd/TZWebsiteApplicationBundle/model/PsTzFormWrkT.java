package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

import java.util.Date;

public class PsTzFormWrkT extends PsTzFormWrkTKey {
    private Long tzAppInsId;

    private String tzBatchId;

    private String tzFormSpSta;

    private String tzColorSortId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzRemarkShort;

    private String tzIsGuest;

    private String tzMemo;

    private String tzMemoSec;

    private String tzMshId;

    private String tzRefteaName;

    private String tzRefteaMobile;

    private String tzRecommendFlag;

    private String tzRemark;

    private String tzIsMszg;
    
    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzBatchId() {
        return tzBatchId;
    }

    public void setTzBatchId(String tzBatchId) {
        this.tzBatchId = tzBatchId == null ? null : tzBatchId.trim();
    }

    public String getTzFormSpSta() {
        return tzFormSpSta;
    }

    public void setTzFormSpSta(String tzFormSpSta) {
        this.tzFormSpSta = tzFormSpSta == null ? null : tzFormSpSta.trim();
    }

    public String getTzColorSortId() {
        return tzColorSortId;
    }

    public void setTzColorSortId(String tzColorSortId) {
        this.tzColorSortId = tzColorSortId == null ? null : tzColorSortId.trim();
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

    public String getTzRemarkShort() {
        return tzRemarkShort;
    }

    public void setTzRemarkShort(String tzRemarkShort) {
        this.tzRemarkShort = tzRemarkShort == null ? null : tzRemarkShort.trim();
    }

    public String getTzIsGuest() {
        return tzIsGuest;
    }

    public void setTzIsGuest(String tzIsGuest) {
        this.tzIsGuest = tzIsGuest == null ? null : tzIsGuest.trim();
    }

    public String getTzMemo() {
        return tzMemo;
    }

    public void setTzMemo(String tzMemo) {
        this.tzMemo = tzMemo == null ? null : tzMemo.trim();
    }

    public String getTzMemoSec() {
        return tzMemoSec;
    }

    public void setTzMemoSec(String tzMemoSec) {
        this.tzMemoSec = tzMemoSec == null ? null : tzMemoSec.trim();
    }

    public String getTzMshId() {
        return tzMshId;
    }

    public void setTzMshId(String tzMshId) {
        this.tzMshId = tzMshId == null ? null : tzMshId.trim();
    }

    public String getTzRefteaName() {
        return tzRefteaName;
    }

    public void setTzRefteaName(String tzRefteaName) {
        this.tzRefteaName = tzRefteaName == null ? null : tzRefteaName.trim();
    }

    public String getTzRefteaMobile() {
        return tzRefteaMobile;
    }

    public void setTzRefteaMobile(String tzRefteaMobile) {
        this.tzRefteaMobile = tzRefteaMobile == null ? null : tzRefteaMobile.trim();
    }

    public String getTzRecommendFlag() {
        return tzRecommendFlag;
    }

    public void setTzRecommendFlag(String tzRecommendFlag) {
        this.tzRecommendFlag = tzRecommendFlag == null ? null : tzRecommendFlag.trim();
    }

    public String getTzRemark() {
        return tzRemark;
    }

    public void setTzRemark(String tzRemark) {
        this.tzRemark = tzRemark == null ? null : tzRemark.trim();
    }

	public String getTzIsMszg() {
		return tzIsMszg;
	}

	public void setTzIsMszg(String tzIsMszg) {
		this.tzIsMszg = tzIsMszg == null?null:tzIsMszg.trim();
	}
}