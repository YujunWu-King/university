package com.tranzvision.gd.TZApplicationStatsBundle.dao;

import com.tranzvision.gd.TZApplicationStatsBundle.model.PsTzStatsProjectT;
import com.tranzvision.gd.TZApplicationStatsBundle.model.PsTzStatsProjectTKey;

public interface PsTzStatsProjectTMapper {
    int deleteByPrimaryKey(PsTzStatsProjectTKey key);

    int insert(PsTzStatsProjectT record);

    int insertSelective(PsTzStatsProjectT record);

    PsTzStatsProjectT selectByPrimaryKey(PsTzStatsProjectTKey key);

    int updateByPrimaryKeySelective(PsTzStatsProjectT record);

    int updateByPrimaryKey(PsTzStatsProjectT record);
}