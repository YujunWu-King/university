package com.tranzvision.gd.TZRuleSetBundle.dao;

import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyT;

public interface PsTzJygzDyTMapper {
    int deleteByPrimaryKey(String tzJygzId);

    int insert(PsTzJygzDyT record);

    int insertSelective(PsTzJygzDyT record);

    PsTzJygzDyT selectByPrimaryKey(String tzJygzId);

    int updateByPrimaryKeySelective(PsTzJygzDyT record);

    int updateByPrimaryKey(PsTzJygzDyT record);
}