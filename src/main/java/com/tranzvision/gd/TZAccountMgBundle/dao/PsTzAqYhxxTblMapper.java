package com.tranzvision.gd.TZAccountMgBundle.dao;

import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;

public interface PsTzAqYhxxTblMapper {
    int deleteByPrimaryKey(PsTzAqYhxxTblKey key);

    int insert(PsTzAqYhxxTbl record);

    int insertSelective(PsTzAqYhxxTbl record);

    PsTzAqYhxxTbl selectByPrimaryKey(PsTzAqYhxxTblKey key);

    int updateByPrimaryKeySelective(PsTzAqYhxxTbl record);

    int updateByPrimaryKey(PsTzAqYhxxTbl record);
}