package com.tranzvision.gd.TZApplicationStatsBundle.dao;

import com.tranzvision.gd.TZApplicationStatsBundle.model.PsTzStuStatsT;

public interface PsTzStuStatsTMapper {
    int deleteByPrimaryKey(Long tzStatsId);

    int insert(PsTzStuStatsT record);

    int insertSelective(PsTzStuStatsT record);

    PsTzStuStatsT selectByPrimaryKey(Long tzStatsId);

    int updateByPrimaryKeySelective(PsTzStuStatsT record);

    int updateByPrimaryKey(PsTzStuStatsT record);
}