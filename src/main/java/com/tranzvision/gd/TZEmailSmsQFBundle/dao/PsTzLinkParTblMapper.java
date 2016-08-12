package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzLinkParTbl;

public interface PsTzLinkParTblMapper {
    int deleteByPrimaryKey(String tzLinkpraId);

    int insert(PsTzLinkParTbl record);

    int insertSelective(PsTzLinkParTbl record);

    PsTzLinkParTbl selectByPrimaryKey(String tzLinkpraId);

    int updateByPrimaryKeySelective(PsTzLinkParTbl record);

    int updateByPrimaryKey(PsTzLinkParTbl record);
}