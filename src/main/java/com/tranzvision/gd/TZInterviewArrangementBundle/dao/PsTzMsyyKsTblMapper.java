package com.tranzvision.gd.TZInterviewArrangementBundle.dao;

import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTblKey;

public interface PsTzMsyyKsTblMapper {
    int deleteByPrimaryKey(PsTzMsyyKsTblKey key);

    int insert(PsTzMsyyKsTbl record);

    int insertSelective(PsTzMsyyKsTbl record);

    PsTzMsyyKsTbl selectByPrimaryKey(PsTzMsyyKsTblKey key);

    int updateByPrimaryKeySelective(PsTzMsyyKsTbl record);

    int updateByPrimaryKey(PsTzMsyyKsTbl record);
}