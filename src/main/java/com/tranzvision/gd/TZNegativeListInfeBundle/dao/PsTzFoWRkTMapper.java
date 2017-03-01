package com.tranzvision.gd.TZNegativeListInfeBundle.dao;

import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzFoWRkT;
import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsTzFoWRkTKey;

public interface PsTzFoWRkTMapper {
    int deleteByPrimaryKey(PsTzFoWRkTKey key);

    int insert(PsTzFoWRkT record);

    int insertSelective(PsTzFoWRkT record);

    PsTzFoWRkT selectByPrimaryKey(PsTzFoWRkTKey key);

    int updateByPrimaryKeySelective(PsTzFoWRkT record);

    int updateByPrimaryKeyWithBLOBs(PsTzFoWRkT record);

    int updateByPrimaryKey(PsTzFoWRkT record);
}