package com.tranzvision.gd.TZInterviewArrangementBundle.dao;

import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMspsKshTblKey;

public interface PsTzMspsKshTblMapper {
    int deleteByPrimaryKey(PsTzMspsKshTblKey key);

    int insert(PsTzMspsKshTbl record);

    int insertSelective(PsTzMspsKshTbl record);

    PsTzMspsKshTbl selectByPrimaryKey(PsTzMspsKshTblKey key);

    int updateByPrimaryKeySelective(PsTzMspsKshTbl record);

    int updateByPrimaryKey(PsTzMspsKshTbl record);
}