package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjxsTKey;

public interface PsTzDcWjLjxsTMapper {
    int deleteByPrimaryKey(PsTzDcWjLjxsTKey key);

    int insert(PsTzDcWjLjxsTKey record);

    int insertSelective(PsTzDcWjLjxsTKey record);
}