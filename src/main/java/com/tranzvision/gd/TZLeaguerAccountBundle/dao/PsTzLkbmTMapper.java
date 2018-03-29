package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLkbmT;

public interface PsTzLkbmTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzLkbmT record);

    int insertSelective(PsTzLkbmT record);

    PsTzLkbmT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzLkbmT record);

    int updateByPrimaryKey(PsTzLkbmT record);
}