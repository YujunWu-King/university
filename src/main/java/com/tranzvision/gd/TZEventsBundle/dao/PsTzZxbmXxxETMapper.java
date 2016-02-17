package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzZxbmXxxET;
import com.tranzvision.gd.TZEventsBundle.model.PsTzZxbmXxxETKey;

public interface PsTzZxbmXxxETMapper {
    int deleteByPrimaryKey(PsTzZxbmXxxETKey key);

    int insert(PsTzZxbmXxxET record);

    int insertSelective(PsTzZxbmXxxET record);

    PsTzZxbmXxxET selectByPrimaryKey(PsTzZxbmXxxETKey key);

    int updateByPrimaryKeySelective(PsTzZxbmXxxET record);

    int updateByPrimaryKey(PsTzZxbmXxxET record);
}