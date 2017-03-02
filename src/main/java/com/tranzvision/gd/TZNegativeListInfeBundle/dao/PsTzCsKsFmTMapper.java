package com.tranzvision.gd.TZNegativeListInfeBundle.dao;

import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmTKey;

public interface PsTzCsKsFmTMapper {
    int deleteByPrimaryKey(PsTzCsKsFmTKey key);

    int insert(PsTzCsKsFmTKey record);

    int insertSelective(PsTzCsKsFmTKey record);
}