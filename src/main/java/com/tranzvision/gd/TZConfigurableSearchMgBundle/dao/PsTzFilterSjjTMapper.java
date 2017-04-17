package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterSjjT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterSjjTKey;

public interface PsTzFilterSjjTMapper {
    int deleteByPrimaryKey(PsTzFilterSjjTKey key);

    int insert(PsTzFilterSjjT record);

    int insertSelective(PsTzFilterSjjT record);

    PsTzFilterSjjT selectByPrimaryKey(PsTzFilterSjjTKey key);

    int updateByPrimaryKeySelective(PsTzFilterSjjT record);

    int updateByPrimaryKey(PsTzFilterSjjT record);
}