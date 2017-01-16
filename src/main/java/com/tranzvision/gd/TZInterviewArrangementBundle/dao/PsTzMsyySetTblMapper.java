package com.tranzvision.gd.TZInterviewArrangementBundle.dao;

import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyySetTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyySetTblKey;

public interface PsTzMsyySetTblMapper {
    int deleteByPrimaryKey(PsTzMsyySetTblKey key);

    int insert(PsTzMsyySetTbl record);

    int insertSelective(PsTzMsyySetTbl record);

    PsTzMsyySetTbl selectByPrimaryKey(PsTzMsyySetTblKey key);

    int updateByPrimaryKeySelective(PsTzMsyySetTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzMsyySetTbl record);

    int updateByPrimaryKey(PsTzMsyySetTbl record);
}