package com.tranzvision.gd.TZDistrubutionAppBunld.dao;

import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTappUserTbl;
import com.tranzvision.gd.TZDistrubutionAppBunld.model.PsTzTappUserTblKey;

public interface PsTzTappUserTblMapper {
    int deleteByPrimaryKey(PsTzTappUserTblKey key);

    int insert(PsTzTappUserTbl record);

    int insertSelective(PsTzTappUserTbl record);

    PsTzTappUserTbl selectByPrimaryKey(PsTzTappUserTblKey key);

    int updateByPrimaryKeySelective(PsTzTappUserTbl record);

    int updateByPrimaryKey(PsTzTappUserTbl record);
}