package com.tranzvision.gd.TZWeChatUserBundle.dao;

import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxuserTagT;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxuserTagTKey;

public interface PsTzWxuserTagTMapper {
    int deleteByPrimaryKey(PsTzWxuserTagTKey key);

    int insert(PsTzWxuserTagT record);

    int insertSelective(PsTzWxuserTagT record);

    PsTzWxuserTagT selectByPrimaryKey(PsTzWxuserTagTKey key);

    int updateByPrimaryKeySelective(PsTzWxuserTagT record);

    int updateByPrimaryKey(PsTzWxuserTagT record);
}