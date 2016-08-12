package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjDsfsT;

public interface PsTzDxYjDsfsTMapper {
    int deleteByPrimaryKey(String tzMlsmQfpcId);

    int insert(PsTzDxYjDsfsT record);

    int insertSelective(PsTzDxYjDsfsT record);

    PsTzDxYjDsfsT selectByPrimaryKey(String tzMlsmQfpcId);

    int updateByPrimaryKeySelective(PsTzDxYjDsfsT record);

    int updateByPrimaryKey(PsTzDxYjDsfsT record);
}