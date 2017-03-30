package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppKsInExtTbl;

public interface PsTzAppKsInExtTblMapper {
    int deleteByPrimaryKey(String tzOprid);

    int insert(PsTzAppKsInExtTbl record);

    int insertSelective(PsTzAppKsInExtTbl record);

    PsTzAppKsInExtTbl selectByPrimaryKey(String tzOprid);

    int updateByPrimaryKeySelective(PsTzAppKsInExtTbl record);

    int updateByPrimaryKey(PsTzAppKsInExtTbl record);
}