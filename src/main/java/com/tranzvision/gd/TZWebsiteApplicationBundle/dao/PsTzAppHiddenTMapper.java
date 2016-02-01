package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppHiddenT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppHiddenTKey;

public interface PsTzAppHiddenTMapper {
    int deleteByPrimaryKey(PsTzAppHiddenTKey key);

    int insert(PsTzAppHiddenT record);

    int insertSelective(PsTzAppHiddenT record);

    PsTzAppHiddenT selectByPrimaryKey(PsTzAppHiddenTKey key);

    int updateByPrimaryKeySelective(PsTzAppHiddenT record);

    int updateByPrimaryKey(PsTzAppHiddenT record);
}