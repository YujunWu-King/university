package com.tranzvision.gd.TZSystemVariableMgBundle.dao;

import com.tranzvision.gd.TZSystemVariableMgBundle.model.PsTzSysvarT;

public interface PsTzSysvarTMapper {
    int deleteByPrimaryKey(String tzSysvarid);

    int insert(PsTzSysvarT record);

    int insertSelective(PsTzSysvarT record);

    PsTzSysvarT selectByPrimaryKey(String tzSysvarid);

    int updateByPrimaryKeySelective(PsTzSysvarT record);

    int updateByPrimaryKeyWithBLOBs(PsTzSysvarT record);

    int updateByPrimaryKey(PsTzSysvarT record);
}