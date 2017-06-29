package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstConT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstConTKey;

public interface PsTzFltDstConTMapper {
    int deleteByPrimaryKey(PsTzFltDstConTKey key);

    int insert(PsTzFltDstConT record);

    int insertSelective(PsTzFltDstConT record);

    PsTzFltDstConT selectByPrimaryKey(PsTzFltDstConTKey key);

    int updateByPrimaryKeySelective(PsTzFltDstConT record);

    int updateByPrimaryKey(PsTzFltDstConT record);
}