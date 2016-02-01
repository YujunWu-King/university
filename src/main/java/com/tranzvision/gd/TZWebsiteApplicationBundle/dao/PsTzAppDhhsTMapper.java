package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhhsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhhsTKey;

public interface PsTzAppDhhsTMapper {
    int deleteByPrimaryKey(PsTzAppDhhsTKey key);

    int insert(PsTzAppDhhsT record);

    int insertSelective(PsTzAppDhhsT record);

    PsTzAppDhhsT selectByPrimaryKey(PsTzAppDhhsTKey key);

    int updateByPrimaryKeySelective(PsTzAppDhhsT record);

    int updateByPrimaryKey(PsTzAppDhhsT record);
}