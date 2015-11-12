package com.tranzvision.gd.TZTranslateMgBundle.dao;

import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxTbl;
import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxTblKey;

public interface PsTzPtZhzxxTblMapper {
    int deleteByPrimaryKey(PsTzPtZhzxxTblKey key);

    int insert(PsTzPtZhzxxTbl record);

    int insertSelective(PsTzPtZhzxxTbl record);

    PsTzPtZhzxxTbl selectByPrimaryKey(PsTzPtZhzxxTblKey key);

    int updateByPrimaryKeySelective(PsTzPtZhzxxTbl record);

    int updateByPrimaryKey(PsTzPtZhzxxTbl record);
}