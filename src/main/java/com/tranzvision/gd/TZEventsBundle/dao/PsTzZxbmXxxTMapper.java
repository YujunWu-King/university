package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzZxbmXxxT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzZxbmXxxTKey;

public interface PsTzZxbmXxxTMapper {
    int deleteByPrimaryKey(PsTzZxbmXxxTKey key);

    int insert(PsTzZxbmXxxT record);

    int insertSelective(PsTzZxbmXxxT record);

    PsTzZxbmXxxT selectByPrimaryKey(PsTzZxbmXxxTKey key);

    int updateByPrimaryKeySelective(PsTzZxbmXxxT record);

    int updateByPrimaryKey(PsTzZxbmXxxT record);
}