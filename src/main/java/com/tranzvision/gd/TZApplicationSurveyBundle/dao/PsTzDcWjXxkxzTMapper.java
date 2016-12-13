package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjXxkxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjXxkxzTKey;

public interface PsTzDcWjXxkxzTMapper {
    int deleteByPrimaryKey(PsTzDcWjXxkxzTKey key);

    int insert(PsTzDcWjXxkxzT record);

    int insertSelective(PsTzDcWjXxkxzT record);

    PsTzDcWjXxkxzT selectByPrimaryKey(PsTzDcWjXxkxzTKey key);

    int updateByPrimaryKeySelective(PsTzDcWjXxkxzT record);

    int updateByPrimaryKey(PsTzDcWjXxkxzT record);
}