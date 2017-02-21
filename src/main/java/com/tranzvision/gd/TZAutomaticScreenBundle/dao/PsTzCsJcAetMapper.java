package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcAet;

public interface PsTzCsJcAetMapper {
    int deleteByPrimaryKey(String runId);

    int insert(PsTzCsJcAet record);

    int insertSelective(PsTzCsJcAet record);

    PsTzCsJcAet selectByPrimaryKey(String runId);

    int updateByPrimaryKeySelective(PsTzCsJcAet record);

    int updateByPrimaryKey(PsTzCsJcAet record);
}