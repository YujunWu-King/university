package com.tranzvision.gd.TZUniPrintBundle.dao;

import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbYsT;
import com.tranzvision.gd.TZUniPrintBundle.model.PsTzDymbYsTKey;

public interface PsTzDymbYsTMapper {
    int deleteByPrimaryKey(PsTzDymbYsTKey key);

    int insert(PsTzDymbYsT record);

    int insertSelective(PsTzDymbYsT record);

    PsTzDymbYsT selectByPrimaryKey(PsTzDymbYsTKey key);

    int updateByPrimaryKeySelective(PsTzDymbYsT record);

    int updateByPrimaryKey(PsTzDymbYsT record);
}