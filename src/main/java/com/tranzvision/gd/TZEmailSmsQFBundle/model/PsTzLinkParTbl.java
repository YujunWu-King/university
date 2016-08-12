package com.tranzvision.gd.TZEmailSmsQFBundle.model;

import java.util.Date;

public class PsTzLinkParTbl {
    private String tzLinkpraId;

    private String tzLinkType;

    private String tzMlsmQfpcId;

    private String tzEmlSmsTaskId;

    private String tzRedtUrl;

    private Date tzAddDttm;

    public String getTzLinkpraId() {
        return tzLinkpraId;
    }

    public void setTzLinkpraId(String tzLinkpraId) {
        this.tzLinkpraId = tzLinkpraId == null ? null : tzLinkpraId.trim();
    }

    public String getTzLinkType() {
        return tzLinkType;
    }

    public void setTzLinkType(String tzLinkType) {
        this.tzLinkType = tzLinkType == null ? null : tzLinkType.trim();
    }

    public String getTzMlsmQfpcId() {
        return tzMlsmQfpcId;
    }

    public void setTzMlsmQfpcId(String tzMlsmQfpcId) {
        this.tzMlsmQfpcId = tzMlsmQfpcId == null ? null : tzMlsmQfpcId.trim();
    }

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzRedtUrl() {
        return tzRedtUrl;
    }

    public void setTzRedtUrl(String tzRedtUrl) {
        this.tzRedtUrl = tzRedtUrl == null ? null : tzRedtUrl.trim();
    }

    public Date getTzAddDttm() {
        return tzAddDttm;
    }

    public void setTzAddDttm(Date tzAddDttm) {
        this.tzAddDttm = tzAddDttm;
    }
}