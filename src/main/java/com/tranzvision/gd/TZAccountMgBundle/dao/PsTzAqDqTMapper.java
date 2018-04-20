package com.tranzvision.gd.TZAccountMgBundle.dao;

import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqDqTKey;

public interface PsTzAqDqTMapper {
    int deleteByPrimaryKey(PsTzAqDqTKey key);

    int insert(PsTzAqDqTKey record);

    int insertSelective(PsTzAqDqTKey record);
}