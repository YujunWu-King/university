package com.tranzvision.gd.TZApplicationSurveyBundle.model;

import java.util.Date;

public class PsTzDcWjattT extends PsTzDcWjattTKey {
    private String attachsysfilename;

    private String attachuserfile;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

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
}