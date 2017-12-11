package com.tranzvision.gd.TZUniPrintBundle.dao;

import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbT;
import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbTKey;

public interface PsTzDymbTMapper {
    int deleteByPrimaryKey(PsTzDymbTKey key);

    int insert(PsTzDymbT record);

    int insertSelective(PsTzDymbT record);

    PsTzDymbT selectByPrimaryKey(PsTzDymbTKey key);

    int updateByPrimaryKeySelective(PsTzDymbT record);

    int updateByPrimaryKey(PsTzDymbT record);
}