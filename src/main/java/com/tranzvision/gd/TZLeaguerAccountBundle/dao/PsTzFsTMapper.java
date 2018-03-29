package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzFsT;

public interface PsTzFsTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzFsT record);

    int insertSelective(PsTzFsT record);

    PsTzFsT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzFsT record);

    int updateByPrimaryKey(PsTzFsT record);
}