package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;

public interface PsTzCsKsTblMapper {
    int deleteByPrimaryKey(PsTzCsKsTblKey key);

    int insert(PsTzCsKsTbl record);

    int insertSelective(PsTzCsKsTbl record);

    PsTzCsKsTbl selectByPrimaryKey(PsTzCsKsTblKey key);

    int updateByPrimaryKeySelective(PsTzCsKsTbl record);

    int updateByPrimaryKey(PsTzCsKsTbl record);
}