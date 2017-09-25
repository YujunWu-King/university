package com.tranzvision.gd.TZWeChatMaterialBundle.model;

import java.util.Date;

public class PsTzWxTwlTbl extends PsTzWxTwlTblKey {
    private String tzTwTitle;

    private String tzTwDescr;

    private String tzAuthor;

    private String tzHeadImage;

    private String tzShcpicFlg;

    private String tzArtUrl;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzContent;

    public String getTzTwTitle() {
        return tzTwTitle;
    }

    public void setTzTwTitle(String tzTwTitle) {
        this.tzTwTitle = tzTwTitle == null ? null : tzTwTitle.trim();
    }

    public String getTzTwDescr() {
        return tzTwDescr;
    }

    public void setTzTwDescr(String tzTwDescr) {
        this.tzTwDescr = tzTwDescr == null ? null : tzTwDescr.trim();
    }

    public String getTzAuthor() {
        return tzAuthor;
    }

    public void setTzAuthor(String tzAuthor) {
        this.tzAuthor = tzAuthor == null ? null : tzAuthor.trim();
    }

    public String getTzHeadImage() {
        return tzHeadImage;
    }

    public void setTzHeadImage(String tzHeadImage) {
        this.tzHeadImage = tzHeadImage == null ? null : tzHeadImage.trim();
    }

    public String getTzShcpicFlg() {
        return tzShcpicFlg;
    }

    public void setTzShcpicFlg(String tzShcpicFlg) {
        this.tzShcpicFlg = tzShcpicFlg == null ? null : tzShcpicFlg.trim();
    }

    public String getTzArtUrl() {
        return tzArtUrl;
    }

    public void setTzArtUrl(String tzArtUrl) {
        this.tzArtUrl = tzArtUrl == null ? null : tzArtUrl.trim();
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

    public String getTzContent() {
        return tzContent;
    }

    public void setTzContent(String tzContent) {
        this.tzContent = tzContent == null ? null : tzContent.trim();
    }
}