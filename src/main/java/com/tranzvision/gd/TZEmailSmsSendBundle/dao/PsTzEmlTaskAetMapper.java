package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzEmlTaskAet;

public interface PsTzEmlTaskAetMapper {
    int deleteByPrimaryKey(String runId);

    int insert(PsTzEmlTaskAet record);

    int insertSelective(PsTzEmlTaskAet record);

    PsTzEmlTaskAet selectByPrimaryKey(String runId);

    int updateByPrimaryKeySelective(PsTzEmlTaskAet record);

    int updateByPrimaryKey(PsTzEmlTaskAet record);
}