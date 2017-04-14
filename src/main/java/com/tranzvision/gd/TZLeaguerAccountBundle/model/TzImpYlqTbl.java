package com.tranzvision.gd.TZLeaguerAccountBundle.model;

public class TzImpYlqTbl {
    private Long tzAppInsId;

    private String tzScholarshipRst;

    private String tzTuitionReference;

    private String tzStuId;

    private String tzPyxyAccept;

    private String tzGzzmAccept;

    private String tzClassRst;

    private String tzEmail;

    private String tzInitialPswd;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzScholarshipRst() {
        return tzScholarshipRst;
    }

    public void setTzScholarshipRst(String tzScholarshipRst) {
        this.tzScholarshipRst = tzScholarshipRst == null ? null : tzScholarshipRst.trim();
    }

    public String getTzTuitionReference() {
        return tzTuitionReference;
    }

    public void setTzTuitionReference(String tzTuitionReference) {
        this.tzTuitionReference = tzTuitionReference == null ? null : tzTuitionReference.trim();
    }

    public String getTzStuId() {
        return tzStuId;
    }

    public void setTzStuId(String tzStuId) {
        this.tzStuId = tzStuId == null ? null : tzStuId.trim();
    }

    public String getTzPyxyAccept() {
        return tzPyxyAccept;
    }

    public void setTzPyxyAccept(String tzPyxyAccept) {
        this.tzPyxyAccept = tzPyxyAccept == null ? null : tzPyxyAccept.trim();
    }

    public String getTzGzzmAccept() {
        return tzGzzmAccept;
    }

    public void setTzGzzmAccept(String tzGzzmAccept) {
        this.tzGzzmAccept = tzGzzmAccept == null ? null : tzGzzmAccept.trim();
    }

    public String getTzClassRst() {
        return tzClassRst;
    }

    public void setTzClassRst(String tzClassRst) {
        this.tzClassRst = tzClassRst == null ? null : tzClassRst.trim();
    }

    public String getTzEmail() {
        return tzEmail;
    }

    public void setTzEmail(String tzEmail) {
        this.tzEmail = tzEmail == null ? null : tzEmail.trim();
    }

    public String getTzInitialPswd() {
        return tzInitialPswd;
    }

    public void setTzInitialPswd(String tzInitialPswd) {
        this.tzInitialPswd = tzInitialPswd == null ? null : tzInitialPswd.trim();
    }
}