package com.tranzvision.gd.TZMenuMgBundle.dao;

import com.tranzvision.gd.TZMenuMgBundle.model.PsTzAqCdjdTbl;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTzAqCdjdTblKey;

public interface PsTzAqCdjdTblMapper {
    int deleteByPrimaryKey(PsTzAqCdjdTblKey key);

    int insert(PsTzAqCdjdTbl record);

    int insertSelective(PsTzAqCdjdTbl record);

    PsTzAqCdjdTbl selectByPrimaryKey(PsTzAqCdjdTblKey key);

    int updateByPrimaryKeySelective(PsTzAqCdjdTbl record);

    int updateByPrimaryKey(PsTzAqCdjdTbl record);
}