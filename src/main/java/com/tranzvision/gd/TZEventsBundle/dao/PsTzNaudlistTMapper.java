package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistTKey;

public interface PsTzNaudlistTMapper {
    int deleteByPrimaryKey(PsTzNaudlistTKey key);

    int insert(PsTzNaudlistT record);

    int insertSelective(PsTzNaudlistT record);

    PsTzNaudlistT selectByPrimaryKey(PsTzNaudlistTKey key);

    int updateByPrimaryKeySelective(PsTzNaudlistT record);

    int updateByPrimaryKey(PsTzNaudlistT record);
}