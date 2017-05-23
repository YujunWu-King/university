package com.tranzvision.gd.TZWeChatBundle.dao;

import com.tranzvision.gd.TZWeChatBundle.model.PsTzOpenidTbl;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzOpenidTblKey;

public interface PsTzOpenidTblMapper {
    int deleteByPrimaryKey(PsTzOpenidTblKey key);

    int insert(PsTzOpenidTbl record);

    int insertSelective(PsTzOpenidTbl record);

    PsTzOpenidTbl selectByPrimaryKey(PsTzOpenidTblKey key);

    int updateByPrimaryKeySelective(PsTzOpenidTbl record);

    int updateByPrimaryKey(PsTzOpenidTbl record);
}