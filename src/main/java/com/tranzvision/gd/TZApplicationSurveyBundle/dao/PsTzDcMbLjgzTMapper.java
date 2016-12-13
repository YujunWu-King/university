package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbLjgzT;

public interface PsTzDcMbLjgzTMapper {
    int deleteByPrimaryKey(String tzDcLjtjId);

    int insert(PsTzDcMbLjgzT record);

    int insertSelective(PsTzDcMbLjgzT record);

    PsTzDcMbLjgzT selectByPrimaryKey(String tzDcLjtjId);

    int updateByPrimaryKeySelective(PsTzDcMbLjgzT record);

    int updateByPrimaryKey(PsTzDcMbLjgzT record);
}