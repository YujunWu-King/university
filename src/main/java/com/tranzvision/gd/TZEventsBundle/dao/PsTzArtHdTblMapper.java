package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzArtHdTbl;

public interface PsTzArtHdTblMapper {
    int deleteByPrimaryKey(String tzArtId);

    int insert(PsTzArtHdTbl record);

    int insertSelective(PsTzArtHdTbl record);

    PsTzArtHdTbl selectByPrimaryKey(String tzArtId);

    int updateByPrimaryKeySelective(PsTzArtHdTbl record);

    int updateByPrimaryKey(PsTzArtHdTbl record);
}