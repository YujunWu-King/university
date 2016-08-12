package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyTWithBLOBs;

public interface PsTzDxyjQfDyTMapper {
    int deleteByPrimaryKey(String tzMlsmQfpcId);

    int insert(PsTzDxyjQfDyTWithBLOBs record);

    int insertSelective(PsTzDxyjQfDyTWithBLOBs record);

    PsTzDxyjQfDyTWithBLOBs selectByPrimaryKey(String tzMlsmQfpcId);

    int updateByPrimaryKeySelective(PsTzDxyjQfDyTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzDxyjQfDyTWithBLOBs record);

    int updateByPrimaryKey(PsTzDxyjQfDyT record);
}