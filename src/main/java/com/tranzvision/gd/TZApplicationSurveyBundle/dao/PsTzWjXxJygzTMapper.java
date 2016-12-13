package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzWjXxJygzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzWjXxJygzTKey;

public interface PsTzWjXxJygzTMapper {
    int deleteByPrimaryKey(PsTzWjXxJygzTKey key);

    int insert(PsTzWjXxJygzT record);

    int insertSelective(PsTzWjXxJygzT record);

    PsTzWjXxJygzT selectByPrimaryKey(PsTzWjXxJygzTKey key);

    int updateByPrimaryKeySelective(PsTzWjXxJygzT record);

    int updateByPrimaryKey(PsTzWjXxJygzT record);
}