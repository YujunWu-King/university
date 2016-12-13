package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjgzT;

public interface PsTzDcWjLjgzTMapper {
    int deleteByPrimaryKey(String tzDcLjtjId);

    int insert(PsTzDcWjLjgzT record);

    int insertSelective(PsTzDcWjLjgzT record);

    PsTzDcWjLjgzT selectByPrimaryKey(String tzDcLjtjId);

    int updateByPrimaryKeySelective(PsTzDcWjLjgzT record);

    int updateByPrimaryKey(PsTzDcWjLjgzT record);
}