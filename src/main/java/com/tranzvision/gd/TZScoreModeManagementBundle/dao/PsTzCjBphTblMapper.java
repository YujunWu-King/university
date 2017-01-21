package com.tranzvision.gd.TZScoreModeManagementBundle.dao;

import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzCjBphTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzCjBphTblKey;

public interface PsTzCjBphTblMapper {
    int deleteByPrimaryKey(PsTzCjBphTblKey key);

    int insert(PsTzCjBphTbl record);

    int insertSelective(PsTzCjBphTbl record);

    PsTzCjBphTbl selectByPrimaryKey(PsTzCjBphTblKey key);

    int updateByPrimaryKeySelective(PsTzCjBphTbl record);

    int updateByPrimaryKey(PsTzCjBphTbl record);
}