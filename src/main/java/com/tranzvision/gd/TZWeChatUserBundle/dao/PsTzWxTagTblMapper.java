package com.tranzvision.gd.TZWeChatUserBundle.dao;

import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTbl;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxTagTblKey;

public interface PsTzWxTagTblMapper {
    int deleteByPrimaryKey(PsTzWxTagTblKey key);

    int insert(PsTzWxTagTbl record);

    int insertSelective(PsTzWxTagTbl record);

    PsTzWxTagTbl selectByPrimaryKey(PsTzWxTagTblKey key);

    int updateByPrimaryKeySelective(PsTzWxTagTbl record);

    int updateByPrimaryKey(PsTzWxTagTbl record);
}