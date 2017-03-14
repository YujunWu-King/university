package com.tranzvision.gd.TZWeChatBundle.dao;

import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxGzhcsT;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxGzhcsTKey;

public interface PsTzWxGzhcsTMapper {
    int deleteByPrimaryKey(PsTzWxGzhcsTKey key);

    int insert(PsTzWxGzhcsT record);

    int insertSelective(PsTzWxGzhcsT record);

    PsTzWxGzhcsT selectByPrimaryKey(PsTzWxGzhcsTKey key);

    int updateByPrimaryKeySelective(PsTzWxGzhcsT record);

    int updateByPrimaryKey(PsTzWxGzhcsT record);
}