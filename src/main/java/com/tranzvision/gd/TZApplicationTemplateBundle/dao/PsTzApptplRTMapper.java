package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplRTKey;

public interface PsTzApptplRTMapper {
    int deleteByPrimaryKey(PsTzApptplRTKey key);

    int insert(PsTzApptplRTKey record);

    int insertSelective(PsTzApptplRTKey record);
}