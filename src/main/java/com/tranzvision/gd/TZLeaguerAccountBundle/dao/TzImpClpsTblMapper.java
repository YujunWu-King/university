package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpClpsTbl;

public interface TzImpClpsTblMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(TzImpClpsTbl record);

    int insertSelective(TzImpClpsTbl record);

    TzImpClpsTbl selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(TzImpClpsTbl record);

    int updateByPrimaryKey(TzImpClpsTbl record);
}