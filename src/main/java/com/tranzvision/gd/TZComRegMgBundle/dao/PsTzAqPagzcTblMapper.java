package com.tranzvision.gd.TZComRegMgBundle.dao;

import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqPagzcTbl;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqPagzcTblKey;

public interface PsTzAqPagzcTblMapper {
    int deleteByPrimaryKey(PsTzAqPagzcTblKey key);

    int insert(PsTzAqPagzcTbl record);

    int insertSelective(PsTzAqPagzcTbl record);

    PsTzAqPagzcTbl selectByPrimaryKey(PsTzAqPagzcTblKey key);

    int updateByPrimaryKeySelective(PsTzAqPagzcTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzAqPagzcTbl record);

    int updateByPrimaryKey(PsTzAqPagzcTbl record);
}