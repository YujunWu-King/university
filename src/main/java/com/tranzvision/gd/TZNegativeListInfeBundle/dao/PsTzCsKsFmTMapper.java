package com.tranzvision.gd.TZNegativeListInfeBundle.dao;

import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmT;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsFmTKey;

public interface PsTzCsKsFmTMapper {
    int deleteByPrimaryKey(PsTzCsKsFmTKey key);

    int insert(PsTzCsKsFmT record);

    int insertSelective(PsTzCsKsFmT record);

    PsTzCsKsFmT selectByPrimaryKey(PsTzCsKsFmTKey key);

    int updateByPrimaryKeySelective(PsTzCsKsFmT record);

    int updateByPrimaryKey(PsTzCsKsFmT record);
}