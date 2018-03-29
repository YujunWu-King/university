package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMsjgT;

public interface PsTzMsjgTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzMsjgT record);

    int insertSelective(PsTzMsjgT record);

    PsTzMsjgT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzMsjgT record);

    int updateByPrimaryKey(PsTzMsjgT record);
}