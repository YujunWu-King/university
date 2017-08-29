package com.tranzvision.gd.TZWeChatMsgBundle.dao;

import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgLogT;
import com.tranzvision.gd.TZWeChatMsgBundle.model.PsTzWxmsgLogTKey;

public interface PsTzWxmsgLogTMapper {
    int deleteByPrimaryKey(PsTzWxmsgLogTKey key);

    int insert(PsTzWxmsgLogT record);

    int insertSelective(PsTzWxmsgLogT record);

    PsTzWxmsgLogT selectByPrimaryKey(PsTzWxmsgLogTKey key);

    int updateByPrimaryKeySelective(PsTzWxmsgLogT record);

    int updateByPrimaryKey(PsTzWxmsgLogT record);
}