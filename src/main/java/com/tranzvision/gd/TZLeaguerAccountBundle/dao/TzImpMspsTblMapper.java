package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpMspsTbl;

public interface TzImpMspsTblMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(TzImpMspsTbl record);

    int insertSelective(TzImpMspsTbl record);

    TzImpMspsTbl selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(TzImpMspsTbl record);

    int updateByPrimaryKey(TzImpMspsTbl record);
}