package com.tranzvision.gd.TZBatchProcessBundle.model;

import java.util.Date;

public class TzBatchProcess extends TzBatchProcessKey {
    private String tzJcMs;

    private String tzYxptLx;

    private String tzBeizhu;

    private String tzZczjId;

    private String tzJavaClass;

    private String tzTjr;

    private Date tzTjsj;

    private String tzZhxgr;

    private Date tzZhxgsj;

    private String tzIsDispatch;

    public String getTzJcMs() {
        return tzJcMs;
    }

    public void setTzJcMs(String tzJcMs) {
        this.tzJcMs = tzJcMs == null ? null : tzJcMs.trim();
    }

    public String getTzYxptLx() {
        return tzYxptLx;
    }

    public void setTzYxptLx(String tzYxptLx) {
        this.tzYxptLx = tzYxptLx == null ? null : tzYxptLx.trim();
    }

    public String getTzBeizhu() {
        return tzBeizhu;
    }

    public void setTzBeizhu(String tzBeizhu) {
        this.tzBeizhu = tzBeizhu == null ? null : tzBeizhu.trim();
    }

    public String getTzZczjId() {
        return tzZczjId;
    }

    public void setTzZczjId(String tzZczjId) {
        this.tzZczjId = tzZczjId == null ? null : tzZczjId.trim();
    }

    public String getTzJavaClass() {
        return tzJavaClass;
    }

    public void setTzJavaClass(String tzJavaClass) {
        this.tzJavaClass = tzJavaClass == null ? null : tzJavaClass.trim();
    }

    public String getTzTjr() {
        return tzTjr;
    }

    public void setTzTjr(String tzTjr) {
        this.tzTjr = tzTjr == null ? null : tzTjr.trim();
    }

    public Date getTzTjsj() {
        return tzTjsj;
    }

    public void setTzTjsj(Date tzTjsj) {
        this.tzTjsj = tzTjsj;
    }

    public String getTzZhxgr() {
        return tzZhxgr;
    }

    public void setTzZhxgr(String tzZhxgr) {
        this.tzZhxgr = tzZhxgr == null ? null : tzZhxgr.trim();
    }

    public Date getTzZhxgsj() {
        return tzZhxgsj;
    }

    public void setTzZhxgsj(Date tzZhxgsj) {
        this.tzZhxgsj = tzZhxgsj;
    }

    public String getTzIsDispatch() {
        return tzIsDispatch;
    }

    public void setTzIsDispatch(String tzIsDispatch) {
        this.tzIsDispatch = tzIsDispatch == null ? null : tzIsDispatch.trim();
    }
}