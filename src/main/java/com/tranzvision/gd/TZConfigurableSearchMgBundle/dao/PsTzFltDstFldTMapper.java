package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstFldTKey;

public interface PsTzFltDstFldTMapper {
    int deleteByPrimaryKey(PsTzFltDstFldTKey key);

    int insert(PsTzFltDstFldT record);

    int insertSelective(PsTzFltDstFldT record);

    PsTzFltDstFldT selectByPrimaryKey(PsTzFltDstFldTKey key);

    int updateByPrimaryKeySelective(PsTzFltDstFldT record);

    int updateByPrimaryKey(PsTzFltDstFldT record);
}