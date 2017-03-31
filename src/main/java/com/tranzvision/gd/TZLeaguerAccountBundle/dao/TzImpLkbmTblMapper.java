package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpLkbmTbl;

public interface TzImpLkbmTblMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(TzImpLkbmTbl record);

    int insertSelective(TzImpLkbmTbl record);

    TzImpLkbmTbl selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(TzImpLkbmTbl record);

    int updateByPrimaryKey(TzImpLkbmTbl record);
}