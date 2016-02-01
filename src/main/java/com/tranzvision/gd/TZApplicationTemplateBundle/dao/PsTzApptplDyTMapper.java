package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;

public interface PsTzApptplDyTMapper {
    int deleteByPrimaryKey(String tzAppTplId);

    int insert(PsTzApptplDyTWithBLOBs record);

    int insertSelective(PsTzApptplDyTWithBLOBs record);

    PsTzApptplDyTWithBLOBs selectByPrimaryKey(String tzAppTplId);

    int updateByPrimaryKeySelective(PsTzApptplDyTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzApptplDyTWithBLOBs record);

    int updateByPrimaryKey(PsTzApptplDyT record);
}