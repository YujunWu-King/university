package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxJygzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxJygzTKey;

public interface PsTzDcXxJygzTMapper {
    int deleteByPrimaryKey(PsTzDcXxJygzTKey key);

    int insert(PsTzDcXxJygzT record);

    int insertSelective(PsTzDcXxJygzT record);

    PsTzDcXxJygzT selectByPrimaryKey(PsTzDcXxJygzTKey key);

    int updateByPrimaryKeySelective(PsTzDcXxJygzT record);

    int updateByPrimaryKey(PsTzDcXxJygzT record);
}