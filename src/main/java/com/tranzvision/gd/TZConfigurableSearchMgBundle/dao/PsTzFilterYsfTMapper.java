package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfTKey;

public interface PsTzFilterYsfTMapper {
    int deleteByPrimaryKey(PsTzFilterYsfTKey key);

    int insert(PsTzFilterYsfT record);

    int insertSelective(PsTzFilterYsfT record);

    PsTzFilterYsfT selectByPrimaryKey(PsTzFilterYsfTKey key);

    int updateByPrimaryKeySelective(PsTzFilterYsfT record);

    int updateByPrimaryKey(PsTzFilterYsfT record);
}