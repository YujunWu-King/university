package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjYbgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjYbgzTKey;

public interface PsTzDcWjYbgzTMapper {
    int deleteByPrimaryKey(PsTzDcWjYbgzTKey key);

    int insert(PsTzDcWjYbgzT record);

    int insertSelective(PsTzDcWjYbgzT record);

    PsTzDcWjYbgzT selectByPrimaryKey(PsTzDcWjYbgzTKey key);

    int updateByPrimaryKeySelective(PsTzDcWjYbgzT record);

    int updateByPrimaryKey(PsTzDcWjYbgzT record);
}