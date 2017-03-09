package com.tranzvision.gd.TZInterviewArrangementBundle.dao;

import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsArrDceAet;

public interface PsTzMsArrDceAetMapper {
    int deleteByPrimaryKey(String runId);

    int insert(PsTzMsArrDceAet record);

    int insertSelective(PsTzMsArrDceAet record);

    PsTzMsArrDceAet selectByPrimaryKey(String runId);

    int updateByPrimaryKeySelective(PsTzMsArrDceAet record);

    int updateByPrimaryKeyWithBLOBs(PsTzMsArrDceAet record);

    int updateByPrimaryKey(PsTzMsArrDceAet record);
}