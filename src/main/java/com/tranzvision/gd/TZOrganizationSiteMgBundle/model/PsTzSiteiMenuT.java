package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

import java.util.Date;

public class PsTzSiteiMenuT extends PsTzSiteiMenuTKey {
    private String tzMenuName;

    private String tzMenuColumn;

    private String tzMenuState;

    private String tzMenuTypeId;

    private String tzMenuOpurlType;

    private String tzIsDel;

    private String tzIsEditor;

    private Integer tzMenuXh;

    private String tzTypeImg;

    private String tzNowImg;

    private Date tzAddedDttm;

    private String tzAddedOprid;

    private Date tzLastmantDttm;

    private String tzLastmantOprid;

    private String tzMenuUrl;

    public String getTzMenuName() {
        return tzMenuName;
    }

    public void setTzMenuName(String tzMenuName) {
        this.tzMenuName = tzMenuName == null ? null : tzMenuName.trim();
    }

    public String getTzMenuColumn() {
        return tzMenuColumn;
    }

    public void setTzMenuColumn(String tzMenuColumn) {
        this.tzMenuColumn = tzMenuColumn == null ? null : tzMenuColumn.trim();
    }

    public String getTzMenuState() {
        return tzMenuState;
    }

    public void setTzMenuState(String tzMenuState) {
        this.tzMenuState = tzMenuState == null ? null : tzMenuState.trim();
    }

    public String getTzMenuTypeId() {
        return tzMenuTypeId;
    }

    public void setTzMenuTypeId(String tzMenuTypeId) {
        this.tzMenuTypeId = tzMenuTypeId == null ? null : tzMenuTypeId.trim();
    }

    public String getTzMenuOpurlType() {
        return tzMenuOpurlType;
    }

    public void setTzMenuOpurlType(String tzMenuOpurlType) {
        this.tzMenuOpurlType = tzMenuOpurlType == null ? null : tzMenuOpurlType.trim();
    }

    public String getTzIsDel() {
        return tzIsDel;
    }

    public void setTzIsDel(String tzIsDel) {
        this.tzIsDel = tzIsDel == null ? null : tzIsDel.trim();
    }

    public String getTzIsEditor() {
        return tzIsEditor;
    }

    public void setTzIsEditor(String tzIsEditor) {
        this.tzIsEditor = tzIsEditor == null ? null : tzIsEditor.trim();
    }

    public Integer getTzMenuXh() {
        return tzMenuXh;
    }

    public void setTzMenuXh(Integer tzMenuXh) {
        this.tzMenuXh = tzMenuXh;
    }

    public String getTzTypeImg() {
        return tzTypeImg;
    }

    public void setTzTypeImg(String tzTypeImg) {
        this.tzTypeImg = tzTypeImg == null ? null : tzTypeImg.trim();
    }

    public String getTzNowImg() {
        return tzNowImg;
    }

    public void setTzNowImg(String tzNowImg) {
        this.tzNowImg = tzNowImg == null ? null : tzNowImg.trim();
    }

    public Date getTzAddedDttm() {
        return tzAddedDttm;
    }

    public void setTzAddedDttm(Date tzAddedDttm) {
        this.tzAddedDttm = tzAddedDttm;
    }

    public String getTzAddedOprid() {
        return tzAddedOprid;
    }

    public void setTzAddedOprid(String tzAddedOprid) {
        this.tzAddedOprid = tzAddedOprid == null ? null : tzAddedOprid.trim();
    }

    public Date getTzLastmantDttm() {
        return tzLastmantDttm;
    }

    public void setTzLastmantDttm(Date tzLastmantDttm) {
        this.tzLastmantDttm = tzLastmantDttm;
    }

    public String getTzLastmantOprid() {
        return tzLastmantOprid;
    }

    public void setTzLastmantOprid(String tzLastmantOprid) {
        this.tzLastmantOprid = tzLastmantOprid == null ? null : tzLastmantOprid.trim();
    }

    public String getTzMenuUrl() {
        return tzMenuUrl;
    }

    public void setTzMenuUrl(String tzMenuUrl) {
        this.tzMenuUrl = tzMenuUrl == null ? null : tzMenuUrl.trim();
    }
}