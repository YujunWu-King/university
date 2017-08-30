package com.tranzvision.gd.TZWeChatUserBundle.dao;

import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserTbl;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserTblKey;

public interface PsTzWxUserTblMapper {
    int deleteByPrimaryKey(PsTzWxUserTblKey key);

    int insert(PsTzWxUserTbl record);

    int insertSelective(PsTzWxUserTbl record);

    PsTzWxUserTbl selectByPrimaryKey(PsTzWxUserTblKey key);

    int updateByPrimaryKeySelective(PsTzWxUserTbl record);

    int updateByPrimaryKey(PsTzWxUserTbl record);
}