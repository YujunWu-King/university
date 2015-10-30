package com.tranzvision.gd.TZPermissionDefnBundle.dao;

import com.tranzvision.gd.TZPermissionDefnBundle.model.PsTzAqComsqTbl;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsTzAqComsqTblKey;

public interface PsTzAqComsqTblMapper {
    int deleteByPrimaryKey(PsTzAqComsqTblKey key);

    int insert(PsTzAqComsqTbl record);

    int insertSelective(PsTzAqComsqTbl record);

    PsTzAqComsqTbl selectByPrimaryKey(PsTzAqComsqTblKey key);

    int updateByPrimaryKeySelective(PsTzAqComsqTbl record);

    int updateByPrimaryKey(PsTzAqComsqTbl record);
}