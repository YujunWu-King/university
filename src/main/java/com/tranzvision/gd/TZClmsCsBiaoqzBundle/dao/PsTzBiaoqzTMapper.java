package com.tranzvision.gd.TZClmsCsBiaoqzBundle.dao;

import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzT;
import com.tranzvision.gd.TZClmsCsBiaoqzBundle.model.PsTzBiaoqzTKey;

public interface PsTzBiaoqzTMapper {
    int deleteByPrimaryKey(PsTzBiaoqzTKey key);

    int insert(PsTzBiaoqzT record);

    int insertSelective(PsTzBiaoqzT record);

    PsTzBiaoqzT selectByPrimaryKey(PsTzBiaoqzTKey key);

    int updateByPrimaryKeySelective(PsTzBiaoqzT record);

    int updateByPrimaryKey(PsTzBiaoqzT record);
}