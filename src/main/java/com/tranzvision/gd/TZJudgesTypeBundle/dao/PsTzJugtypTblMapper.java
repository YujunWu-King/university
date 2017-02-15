package com.tranzvision.gd.TZJudgesTypeBundle.dao;

import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzJugtypTbl;

public interface PsTzJugtypTblMapper {
    int deleteByPrimaryKey(String tzJugtypId);

    int insert(PsTzJugtypTbl record);

    int insertSelective(PsTzJugtypTbl record);

    PsTzJugtypTbl selectByPrimaryKey(String tzJugtypId);

    int updateByPrimaryKeySelective(PsTzJugtypTbl record);

    int updateByPrimaryKey(PsTzJugtypTbl record);
}