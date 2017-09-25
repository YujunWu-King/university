package com.tranzvision.gd.TZWeChatAppletBugMBundle.dao;

import com.tranzvision.gd.TZWeChatAppletBugMBundle.model.PsTzBugmgDfnT;
import com.tranzvision.gd.TZWeChatAppletBugMBundle.model.PsTzBugmgDfnTKey;
import com.tranzvision.gd.TZWeChatAppletBugMBundle.model.PsTzBugmgDfnTWithBLOBs;

public interface PsTzBugmgDfnTMapper {
    int deleteByPrimaryKey(PsTzBugmgDfnTKey key);

    int insert(PsTzBugmgDfnTWithBLOBs record);

    int insertSelective(PsTzBugmgDfnTWithBLOBs record);

    PsTzBugmgDfnTWithBLOBs selectByPrimaryKey(PsTzBugmgDfnTKey key);

    int updateByPrimaryKeySelective(PsTzBugmgDfnTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzBugmgDfnTWithBLOBs record);

    int updateByPrimaryKey(PsTzBugmgDfnT record);
}