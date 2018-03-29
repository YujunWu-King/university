package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMszgT;

public interface PsTzMszgTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzMszgT record);

    int insertSelective(PsTzMszgT record);

    PsTzMszgT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzMszgT record);

    int updateByPrimaryKey(PsTzMszgT record);
}