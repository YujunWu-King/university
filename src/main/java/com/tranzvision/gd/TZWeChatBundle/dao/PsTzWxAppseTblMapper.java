package com.tranzvision.gd.TZWeChatBundle.dao;

import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxAppseTbl;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxAppseTblKey;

public interface PsTzWxAppseTblMapper {
    int deleteByPrimaryKey(PsTzWxAppseTblKey key);

    int insert(PsTzWxAppseTbl record);

    int insertSelective(PsTzWxAppseTbl record);

    PsTzWxAppseTbl selectByPrimaryKey(PsTzWxAppseTblKey key);

    int updateByPrimaryKeySelective(PsTzWxAppseTbl record);

    int updateByPrimaryKey(PsTzWxAppseTbl record);
}