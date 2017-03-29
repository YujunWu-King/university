package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzReUserTbl;

public interface PsTzReUserTblMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PsTzReUserTbl record);

    int insertSelective(PsTzReUserTbl record);

    PsTzReUserTbl selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PsTzReUserTbl record);

    int updateByPrimaryKey(PsTzReUserTbl record);
}