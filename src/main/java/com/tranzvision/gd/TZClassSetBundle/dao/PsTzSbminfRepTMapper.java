package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzSbminfRepT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzSbminfRepTKey;

public interface PsTzSbminfRepTMapper {
    int deleteByPrimaryKey(PsTzSbminfRepTKey key);

    int insert(PsTzSbminfRepT record);

    int insertSelective(PsTzSbminfRepT record);

    PsTzSbminfRepT selectByPrimaryKey(PsTzSbminfRepTKey key);

    int updateByPrimaryKeySelective(PsTzSbminfRepT record);

    int updateByPrimaryKey(PsTzSbminfRepT record);
}