package com.tranzvision.gd.TZUniPrintBundle.dao;

import com.tranzvision.gd.TZUniPrintBundle.model.TzDymbTbl;
import com.tranzvision.gd.TZUniPrintBundle.model.TzDymbTblKey;

public interface TzDymbTblMapper {
    int deleteByPrimaryKey(TzDymbTblKey key);

    int insert(TzDymbTbl record);

    int insertSelective(TzDymbTbl record);

    TzDymbTbl selectByPrimaryKey(TzDymbTblKey key);

    int updateByPrimaryKeySelective(TzDymbTbl record);

    int updateByPrimaryKey(TzDymbTbl record);
}