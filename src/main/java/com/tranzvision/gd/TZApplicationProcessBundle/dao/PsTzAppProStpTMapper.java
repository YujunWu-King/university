package com.tranzvision.gd.TZApplicationProcessBundle.dao;

import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProStpT;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProStpTKey;

public interface PsTzAppProStpTMapper {
    int deleteByPrimaryKey(PsTzAppProStpTKey key);

    int insert(PsTzAppProStpT record);

    int insertSelective(PsTzAppProStpT record);

    PsTzAppProStpT selectByPrimaryKey(PsTzAppProStpTKey key);

    int updateByPrimaryKeySelective(PsTzAppProStpT record);

    int updateByPrimaryKeyWithBLOBs(PsTzAppProStpT record);

    int updateByPrimaryKey(PsTzAppProStpT record);
}