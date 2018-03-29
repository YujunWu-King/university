package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzQtT;

public interface PsTzQtTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzQtT record);

    int insertSelective(PsTzQtT record);

    PsTzQtT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzQtT record);

    int updateByPrimaryKey(PsTzQtT record);
}