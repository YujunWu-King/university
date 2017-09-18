package com.tranzvision.gd.TZWeChatUserBundle.dao;

import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserAet;

public interface PsTzWxUserAetMapper {
    int deleteByPrimaryKey(String runCntlId);

    int insert(PsTzWxUserAet record);

    int insertSelective(PsTzWxUserAet record);

    PsTzWxUserAet selectByPrimaryKey(String runCntlId);

    int updateByPrimaryKeySelective(PsTzWxUserAet record);

    int updateByPrimaryKey(PsTzWxUserAet record);
}