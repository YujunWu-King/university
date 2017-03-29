package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.TzImpYlqTbl;

public interface TzImpYlqTblMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(TzImpYlqTbl record);

    int insertSelective(TzImpYlqTbl record);

    TzImpYlqTbl selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(TzImpYlqTbl record);

    int updateByPrimaryKey(TzImpYlqTbl record);
}