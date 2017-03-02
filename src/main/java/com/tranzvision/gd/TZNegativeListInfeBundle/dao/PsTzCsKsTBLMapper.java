package com.tranzvision.gd.TZNegativeListInfeBundle.dao;

import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsTBL;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzCsKsTBLKey;

public interface PsTzCsKsTBLMapper {
    int deleteByPrimaryKey(PsTzCsKsTBLKey key);

    int insert(PsTzCsKsTBL record);

    int insertSelective(PsTzCsKsTBL record);

    PsTzCsKsTBL selectByPrimaryKey(PsTzCsKsTBLKey key);

    int updateByPrimaryKeySelective(PsTzCsKsTBL record);

    int updateByPrimaryKey(PsTzCsKsTBL record);
}