package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjDcAet;

public interface PsTzDcwjDcAetMapper {
    int deleteByPrimaryKey(String runCntlId);

    int insert(PsTzDcwjDcAet record);

    int insertSelective(PsTzDcwjDcAet record);

    PsTzDcwjDcAet selectByPrimaryKey(String runCntlId);

    int updateByPrimaryKeySelective(PsTzDcwjDcAet record);

    int updateByPrimaryKey(PsTzDcwjDcAet record);
}