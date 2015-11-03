package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltprmFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltprmFldTKey;

public interface PsTzFltprmFldTMapper {
    int deleteByPrimaryKey(PsTzFltprmFldTKey key);

    int insert(PsTzFltprmFldT record);

    int insertSelective(PsTzFltprmFldT record);

    PsTzFltprmFldT selectByPrimaryKey(PsTzFltprmFldTKey key);

    int updateByPrimaryKeySelective(PsTzFltprmFldT record);

    int updateByPrimaryKey(PsTzFltprmFldT record);
}