package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldTKey;

public interface PsTzFilterFldTMapper {
    int deleteByPrimaryKey(PsTzFilterFldTKey key);

    int insert(PsTzFilterFldT record);

    int insertSelective(PsTzFilterFldT record);

    PsTzFilterFldT selectByPrimaryKey(PsTzFilterFldTKey key);

    int updateByPrimaryKeySelective(PsTzFilterFldT record);

    int updateByPrimaryKey(PsTzFilterFldT record);
}