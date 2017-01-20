package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxRecT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxRecTKey;

public interface PsTzZnxRecTMapper {
    int deleteByPrimaryKey(PsTzZnxRecTKey key);

    int insert(PsTzZnxRecT record);

    int insertSelective(PsTzZnxRecT record);

    PsTzZnxRecT selectByPrimaryKey(PsTzZnxRecTKey key);

    int updateByPrimaryKeySelective(PsTzZnxRecT record);

    int updateByPrimaryKeyWithBLOBs(PsTzZnxRecT record);

    int updateByPrimaryKey(PsTzZnxRecT record);
}