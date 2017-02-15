package com.tranzvision.gd.TZClmsCsBiaoqzBundle.dao;

import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzBqT;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzBqTKey;

public interface PsTzBiaoqzBqTMapper {
    int deleteByPrimaryKey(PsTzBiaoqzBqTKey key);

    int insert(PsTzBiaoqzBqT record);

    int insertSelective(PsTzBiaoqzBqT record);

    PsTzBiaoqzBqT selectByPrimaryKey(PsTzBiaoqzBqTKey key);

    int updateByPrimaryKeySelective(PsTzBiaoqzBqT record);

    int updateByPrimaryKey(PsTzBiaoqzBqT record);
}