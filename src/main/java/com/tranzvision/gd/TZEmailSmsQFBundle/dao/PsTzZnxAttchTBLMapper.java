package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxAttchTBL;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxAttchTBLKey;

public interface PsTzZnxAttchTBLMapper {
    int deleteByPrimaryKey(PsTzZnxAttchTBLKey key);

    int insert(PsTzZnxAttchTBL record);

    int insertSelective(PsTzZnxAttchTBL record);

    PsTzZnxAttchTBL selectByPrimaryKey(PsTzZnxAttchTBLKey key);

    int updateByPrimaryKeySelective(PsTzZnxAttchTBL record);

    int updateByPrimaryKey(PsTzZnxAttchTBL record);
}