package com.tranzvision.gd.TZSchlrBundle.model;

public class PsTzSchlrRsltTbl extends PsTzSchlrRsltTblKey {
    private String tzIsApply;

    private String tzNote;

    public String getTzIsApply() {
        return tzIsApply;
    }

    public void setTzIsApply(String tzIsApply) {
        this.tzIsApply = tzIsApply == null ? null : tzIsApply.trim();
    }

    public String getTzNote() {
        return tzNote;
    }

    public void setTzNote(String tzNote) {
        this.tzNote = tzNote == null ? null : tzNote.trim();
    }
}