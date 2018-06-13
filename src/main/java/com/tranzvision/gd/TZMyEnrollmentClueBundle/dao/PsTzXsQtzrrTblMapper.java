package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsQtzrrTblKey;

public interface PsTzXsQtzrrTblMapper {
    int deleteByPrimaryKey(PsTzXsQtzrrTblKey key);

    int insert(PsTzXsQtzrrTblKey record);

    int insertSelective(PsTzXsQtzrrTblKey record);
}