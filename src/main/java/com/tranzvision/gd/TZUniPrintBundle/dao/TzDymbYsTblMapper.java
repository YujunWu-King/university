package com.tranzvision.gd.TZUniPrintBundle.dao;

import com.tranzvision.gd.TZUniPrintBundle.model.TzDymbYsTbl;
import com.tranzvision.gd.TZUniPrintBundle.model.TzDymbYsTblKey;

public interface TzDymbYsTblMapper {
    int deleteByPrimaryKey(TzDymbYsTblKey key);

    int insert(TzDymbYsTbl record);

    int insertSelective(TzDymbYsTbl record);

    TzDymbYsTbl selectByPrimaryKey(TzDymbYsTblKey key);

    int updateByPrimaryKeySelective(TzDymbYsTbl record);

    int updateByPrimaryKey(TzDymbYsTbl record);
}