package com.tranzvision.gd.TZWeChatMsgBundle.dao;

import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgUserT;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgUserTKey;

public interface PsTzWxmsgUserTMapper {
    int deleteByPrimaryKey(PsTzWxmsgUserTKey key);

    int insert(PsTzWxmsgUserT record);

    int insertSelective(PsTzWxmsgUserT record);

    PsTzWxmsgUserT selectByPrimaryKey(PsTzWxmsgUserTKey key);

    int updateByPrimaryKeySelective(PsTzWxmsgUserT record);

    int updateByPrimaryKey(PsTzWxmsgUserT record);
}