package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsLabelTblKey;

public interface PsTzXsLabelTblMapper {
    int deleteByPrimaryKey(PsTzXsLabelTblKey key);

    int insert(PsTzXsLabelTblKey record);

    int insertSelective(PsTzXsLabelTblKey record);
}