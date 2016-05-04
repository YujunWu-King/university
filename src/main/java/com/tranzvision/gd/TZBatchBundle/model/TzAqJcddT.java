package com.tranzvision.gd.TZBatchBundle.model;

import java.util.Date;

public class TzAqJcddT extends TzAqJcddTKey {
    private String tzDdFlg;

    private String tzTjr;

    private Date tzTjsj;

    private String tzZhxgr;

    private Date tzZhxgsj;

    public String getTzDdFlg() {
        return tzDdFlg;
    }

    public void setTzDdFlg(String tzDdFlg) {
        this.tzDdFlg = tzDdFlg == null ? null : tzDdFlg.trim();
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
}