package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzRwzxshilTbl;

public interface PsTzRwzxshilTblMapper {
    int deleteByPrimaryKey(String tzZdBh);

    int insert(PsTzRwzxshilTbl record);

    int insertSelective(PsTzRwzxshilTbl record);

    PsTzRwzxshilTbl selectByPrimaryKey(String tzZdBh);

    int updateByPrimaryKeySelective(PsTzRwzxshilTbl record);

    int updateByPrimaryKey(PsTzRwzxshilTbl record);
}