package com.tranzvision.gd.TZWebsiteApplicationBundle.model;

import java.util.Date;

public class PsTzFormAttT extends PsTzFormAttTKey {
    private String attachsysfilename;

    private String attachuserfile;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzAccessPath;

    private Integer filetype;

    public String getAttachsysfilename() {
        return attachsysfilename;
    }

    public void setAttachsysfilename(String attachsysfilename) {
        this.attachsysfilename = attachsysfilename == null ? null : attachsysfilename.trim();
    }

    public String getAttachuserfile() {
        return attachuserfile;
    }

    public void setAttachuserfile(String attachuserfile) {
        this.attachuserfile = attachuserfile == null ? null : attachuserfile.trim();
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

    public String getTzAccessPath() {
        return tzAccessPath;
    }

    public void setTzAccessPath(String tzAccessPath) {
        this.tzAccessPath = tzAccessPath == null ? null : tzAccessPath.trim();
    }

    public Integer getFiletype() {
        return filetype;
    }

    public void setFiletype(Integer filetype) {
        this.filetype = filetype;
    }
}