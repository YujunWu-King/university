package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppEventsT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppEventsTKey;

public interface PsTzAppEventsTMapper {
    int deleteByPrimaryKey(PsTzAppEventsTKey key);

    int insert(PsTzAppEventsT record);

    int insertSelective(PsTzAppEventsT record);

    PsTzAppEventsT selectByPrimaryKey(PsTzAppEventsTKey key);

    int updateByPrimaryKeySelective(PsTzAppEventsT record);

    int updateByPrimaryKey(PsTzAppEventsT record);
}