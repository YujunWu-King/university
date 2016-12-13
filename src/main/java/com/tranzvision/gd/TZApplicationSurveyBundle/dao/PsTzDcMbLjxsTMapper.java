package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbLjxsTKey;

public interface PsTzDcMbLjxsTMapper {
    int deleteByPrimaryKey(PsTzDcMbLjxsTKey key);

    int insert(PsTzDcMbLjxsTKey record);

    int insertSelective(PsTzDcMbLjxsTKey record);
}