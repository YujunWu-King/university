package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;

public interface PsTzKsTjxTblMapper {
    int deleteByPrimaryKey(String tzRefLetterId);

    int insert(PsTzKsTjxTbl record);

    int insertSelective(PsTzKsTjxTbl record);

    PsTzKsTjxTbl selectByPrimaryKey(String tzRefLetterId);

    int updateByPrimaryKeySelective(PsTzKsTjxTbl record);

    int updateByPrimaryKey(PsTzKsTjxTbl record);
}