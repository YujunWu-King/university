package com.tranzvision.gd.TZClassDefnBundle.dao;

import com.tranzvision.gd.TZClassDefnBundle.model.PsTzAppclsTbl;

public interface PsTzAppclsTblMapper {
    int deleteByPrimaryKey(String tzAppclsId);

    int insert(PsTzAppclsTbl record);

    int insertSelective(PsTzAppclsTbl record);

    PsTzAppclsTbl selectByPrimaryKey(String tzAppclsId);

    int updateByPrimaryKeySelective(PsTzAppclsTbl record);

    int updateByPrimaryKey(PsTzAppclsTbl record);
}