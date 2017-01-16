package com.tranzvision.gd.TZInterviewArrangementBundle.dao;

import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMssjArrTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMssjArrTblKey;

public interface PsTzMssjArrTblMapper {
    int deleteByPrimaryKey(PsTzMssjArrTblKey key);

    int insert(PsTzMssjArrTbl record);

    int insertSelective(PsTzMssjArrTbl record);

    PsTzMssjArrTbl selectByPrimaryKey(PsTzMssjArrTblKey key);

    int updateByPrimaryKeySelective(PsTzMssjArrTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzMssjArrTbl record);

    int updateByPrimaryKey(PsTzMssjArrTbl record);
}