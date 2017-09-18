package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnTKey;

public interface PsTzFilterDfnTMapper {
    int deleteByPrimaryKey(PsTzFilterDfnTKey key);

    int insert(PsTzFilterDfnT record);

    int insertSelective(PsTzFilterDfnT record);

    PsTzFilterDfnT selectByPrimaryKey(PsTzFilterDfnTKey key);

    int updateByPrimaryKeySelective(PsTzFilterDfnT record);

    int updateByPrimaryKeyWithBLOBs(PsTzFilterDfnT record);

    int updateByPrimaryKey(PsTzFilterDfnT record);
}