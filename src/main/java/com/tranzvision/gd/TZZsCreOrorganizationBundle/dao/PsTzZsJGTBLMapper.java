package com.tranzvision.gd.TZZsCreOrorganizationBundle.dao;

import com.tranzvision.gd.TZZsCreOrorganizationBundle.model.PsTzZsJGTBL;
import com.tranzvision.gd.TZZsCreOrorganizationBundle.model.PsTzZsJGTBLKey;

public interface PsTzZsJGTBLMapper {
    int deleteByPrimaryKey(PsTzZsJGTBLKey key);

    int insert(PsTzZsJGTBL record);

    int insertSelective(PsTzZsJGTBL record);

    PsTzZsJGTBL selectByPrimaryKey(PsTzZsJGTBLKey key);

    int updateByPrimaryKeySelective(PsTzZsJGTBL record);

    int updateByPrimaryKey(PsTzZsJGTBL record);
}