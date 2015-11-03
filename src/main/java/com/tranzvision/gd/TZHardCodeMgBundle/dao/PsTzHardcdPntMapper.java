package com.tranzvision.gd.TZHardCodeMgBundle.dao;

import com.tranzvision.gd.TZHardCodeMgBundle.model.PsTzHardcdPnt;

public interface PsTzHardcdPntMapper {
    int deleteByPrimaryKey(String tzHardcodePnt);

    int insert(PsTzHardcdPnt record);

    int insertSelective(PsTzHardcdPnt record);

    PsTzHardcdPnt selectByPrimaryKey(String tzHardcodePnt);

    int updateByPrimaryKeySelective(PsTzHardcdPnt record);

    int updateByPrimaryKeyWithBLOBs(PsTzHardcdPnt record);

    int updateByPrimaryKey(PsTzHardcdPnt record);
}