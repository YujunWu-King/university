package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiColuT extends PsTzSiteiColuTKey {
    private String tzColuName;

    private String tzColuType;

    private String tzTempId;

    private String tzContType;

    private String tzContTemp;

    private String tzMenuTypeId;

    private String tzColuState;

    private String tzFColuId;

    private String tzColuPath;

    private String tzOutUrl;

    private String tzArtTypeId;

    private Integer tzColuLevel;

    private String tzColuAbout;

    public String getTzColuName() {
        return tzColuName;
    }

    public void setTzColuName(String tzColuName) {
        this.tzColuName = tzColuName == null ? null : tzColuName.trim();
    }

    public String getTzColuType() {
        return tzColuType;
    }

    public void setTzColuType(String tzColuType) {
        this.tzColuType = tzColuType == null ? null : tzColuType.trim();
    }

    public String getTzTempId() {
        return tzTempId;
    }

    public void setTzTempId(String tzTempId) {
        this.tzTempId = tzTempId == null ? null : tzTempId.trim();
    }

    public String getTzContType() {
        return tzContType;
    }

    public void setTzContType(String tzContType) {
        this.tzContType = tzContType == null ? null : tzContType.trim();
    }

    public String getTzContTemp() {
        return tzContTemp;
    }

    public void setTzContTemp(String tzContTemp) {
        this.tzContTemp = tzContTemp == null ? null : tzContTemp.trim();
    }

    public String getTzMenuTypeId() {
        return tzMenuTypeId;
    }

    public void setTzMenuTypeId(String tzMenuTypeId) {
        this.tzMenuTypeId = tzMenuTypeId == null ? null : tzMenuTypeId.trim();
    }

    public String getTzColuState() {
        return tzColuState;
    }

    public void setTzColuState(String tzColuState) {
        this.tzColuState = tzColuState == null ? null : tzColuState.trim();
    }

    public String getTzFColuId() {
        return tzFColuId;
    }

    public void setTzFColuId(String tzFColuId) {
        this.tzFColuId = tzFColuId == null ? null : tzFColuId.trim();
    }

    public String getTzColuPath() {
        return tzColuPath;
    }

    public void setTzColuPath(String tzColuPath) {
        this.tzColuPath = tzColuPath == null ? null : tzColuPath.trim();
    }

    public String getTzOutUrl() {
        return tzOutUrl;
    }

    public void setTzOutUrl(String tzOutUrl) {
        this.tzOutUrl = tzOutUrl == null ? null : tzOutUrl.trim();
    }

    public String getTzArtTypeId() {
        return tzArtTypeId;
    }

    public void setTzArtTypeId(String tzArtTypeId) {
        this.tzArtTypeId = tzArtTypeId == null ? null : tzArtTypeId.trim();
    }

    public Integer getTzColuLevel() {
        return tzColuLevel;
    }

    public void setTzColuLevel(Integer tzColuLevel) {
        this.tzColuLevel = tzColuLevel;
    }

    public String getTzColuAbout() {
        return tzColuAbout;
    }

    public void setTzColuAbout(String tzColuAbout) {
        this.tzColuAbout = tzColuAbout == null ? null : tzColuAbout.trim();
    }
}