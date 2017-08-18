package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjAppclsT;

public interface PsTzDcWjAppclsTMapper {
    int deleteByPrimaryKey(Integer tzSeqnum);

    int insert(PsTzDcWjAppclsT record);

    int insertSelective(PsTzDcWjAppclsT record);

    PsTzDcWjAppclsT selectByPrimaryKey(Integer tzSeqnum);

    int updateByPrimaryKeySelective(PsTzDcWjAppclsT record);

    int updateByPrimaryKey(PsTzDcWjAppclsT record);
}