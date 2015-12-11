package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjmbshliTbl;

public interface PsTzYjmbshliTblMapper {
    int deleteByPrimaryKey(String tzEmlSmsTaskId);

    int insert(PsTzYjmbshliTbl record);

    int insertSelective(PsTzYjmbshliTbl record);

    PsTzYjmbshliTbl selectByPrimaryKey(String tzEmlSmsTaskId);

    int updateByPrimaryKeySelective(PsTzYjmbshliTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzYjmbshliTbl record);

    int updateByPrimaryKey(PsTzYjmbshliTbl record);
}