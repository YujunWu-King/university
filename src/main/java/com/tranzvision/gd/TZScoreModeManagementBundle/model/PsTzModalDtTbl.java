package com.tranzvision.gd.TZScoreModeManagementBundle.model;

import java.math.BigDecimal;
import java.util.Date;

public class PsTzModalDtTbl extends PsTzModalDtTblKey {
    private String descr;

    private String tzScoreItemType;

    private BigDecimal tzScoreHz;

    private BigDecimal tzScoreQz;

    private String tzMFbdzMxXxJx;

    private BigDecimal tzScoreLimited2;

    private String tzMFbdzMxSxJx;

    private BigDecimal tzScoreLimited;

    private Integer tzScorePyZslim0;

    private Integer tzScorePyZslim;

    private String tzScrToScore;

    private BigDecimal tzScrSqz;

    private String tzScoreCkzl;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr == null ? null : descr.trim();
    }

    public String getTzScoreItemType() {
        return tzScoreItemType;
    }

    public void setTzScoreItemType(String tzScoreItemType) {
        this.tzScoreItemType = tzScoreItemType == null ? null : tzScoreItemType.trim();
    }

    public BigDecimal getTzScoreHz() {
        return tzScoreHz;
    }

    public void setTzScoreHz(BigDecimal tzScoreHz) {
        this.tzScoreHz = tzScoreHz;
    }

    public BigDecimal getTzScoreQz() {
        return tzScoreQz;
    }

    public void setTzScoreQz(BigDecimal tzScoreQz) {
        this.tzScoreQz = tzScoreQz;
    }

    public String getTzMFbdzMxXxJx() {
        return tzMFbdzMxXxJx;
    }

    public void setTzMFbdzMxXxJx(String tzMFbdzMxXxJx) {
        this.tzMFbdzMxXxJx = tzMFbdzMxXxJx == null ? null : tzMFbdzMxXxJx.trim();
    }

    public BigDecimal getTzScoreLimited2() {
        return tzScoreLimited2;
    }

    public void setTzScoreLimited2(BigDecimal tzScoreLimited2) {
        this.tzScoreLimited2 = tzScoreLimited2;
    }

    public String getTzMFbdzMxSxJx() {
        return tzMFbdzMxSxJx;
    }

    public void setTzMFbdzMxSxJx(String tzMFbdzMxSxJx) {
        this.tzMFbdzMxSxJx = tzMFbdzMxSxJx == null ? null : tzMFbdzMxSxJx.trim();
    }

    public BigDecimal getTzScoreLimited() {
        return tzScoreLimited;
    }

    public void setTzScoreLimited(BigDecimal tzScoreLimited) {
        this.tzScoreLimited = tzScoreLimited;
    }

    public Integer getTzScorePyZslim0() {
        return tzScorePyZslim0;
    }

    public void setTzScorePyZslim0(Integer tzScorePyZslim0) {
        this.tzScorePyZslim0 = tzScorePyZslim0;
    }

    public Integer getTzScorePyZslim() {
        return tzScorePyZslim;
    }

    public void setTzScorePyZslim(Integer tzScorePyZslim) {
        this.tzScorePyZslim = tzScorePyZslim;
    }

    public String getTzScrToScore() {
        return tzScrToScore;
    }

    public void setTzScrToScore(String tzScrToScore) {
        this.tzScrToScore = tzScrToScore == null ? null : tzScrToScore.trim();
    }

    public BigDecimal getTzScrSqz() {
        return tzScrSqz;
    }

    public void setTzScrSqz(BigDecimal tzScrSqz) {
        this.tzScrSqz = tzScrSqz;
    }

    public String getTzScoreCkzl() {
        return tzScoreCkzl;
    }

    public void setTzScoreCkzl(String tzScoreCkzl) {
        this.tzScoreCkzl = tzScoreCkzl == null ? null : tzScoreCkzl.trim();
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