package com.tranzvision.gd.TZInterviewArrangementBundle.dao;

import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsapAudTblKey;

public interface PsTzMsapAudTblMapper {
    int deleteByPrimaryKey(PsTzMsapAudTblKey key);

    int insert(PsTzMsapAudTblKey record);

    int insertSelective(PsTzMsapAudTblKey record);
}