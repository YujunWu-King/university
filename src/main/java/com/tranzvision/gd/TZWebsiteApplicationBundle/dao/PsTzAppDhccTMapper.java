package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccTKey;

public interface PsTzAppDhccTMapper {
    int deleteByPrimaryKey(PsTzAppDhccTKey key);

    int insert(PsTzAppDhccT record);

    int insertSelective(PsTzAppDhccT record);

    PsTzAppDhccT selectByPrimaryKey(PsTzAppDhccTKey key);

    int updateByPrimaryKeySelective(PsTzAppDhccT record);

    int updateByPrimaryKey(PsTzAppDhccT record);
}