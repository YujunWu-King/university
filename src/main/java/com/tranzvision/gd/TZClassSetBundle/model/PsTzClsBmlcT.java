package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsBmlcT extends PsTzClsBmlcTKey {
    private Integer tzSortNum;

    private String tzAppproName;

    private String tzIsPublic;

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }

    public String getTzAppproName() {
        return tzAppproName;
    }

    public void setTzAppproName(String tzAppproName) {
        this.tzAppproName = tzAppproName == null ? null : tzAppproName.trim();
    }

    public String getTzIsPublic() {
        return tzIsPublic;
    }

    public void setTzIsPublic(String tzIsPublic) {
        this.tzIsPublic = tzIsPublic == null ? null : tzIsPublic.trim();
    }
}