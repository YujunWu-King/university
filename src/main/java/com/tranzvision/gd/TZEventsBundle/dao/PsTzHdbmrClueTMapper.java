package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzHdbmrClueTKey;

public interface PsTzHdbmrClueTMapper {
    int deleteByPrimaryKey(PsTzHdbmrClueTKey key);

    int insert(PsTzHdbmrClueTKey record);

    int insertSelective(PsTzHdbmrClueTKey record);
}