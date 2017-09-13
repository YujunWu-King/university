package com.tranzvision.gd.TZDistrubutionAppBunld.dao;

import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTranzAppTbl;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTranzAppTblKey;

public interface PsTzTranzAppTblMapper {
    int deleteByPrimaryKey(PsTzTranzAppTblKey key);

    int insert(PsTzTranzAppTbl record);

    int insertSelective(PsTzTranzAppTbl record);

    PsTzTranzAppTbl selectByPrimaryKey(PsTzTranzAppTblKey key);

    int updateByPrimaryKeySelective(PsTzTranzAppTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzTranzAppTbl record);

    int updateByPrimaryKey(PsTzTranzAppTbl record);
}