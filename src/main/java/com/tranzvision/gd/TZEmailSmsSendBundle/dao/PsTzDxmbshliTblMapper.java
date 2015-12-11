package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxmbshliTbl;

public interface PsTzDxmbshliTblMapper {
    int deleteByPrimaryKey(String tzEmlSmsTaskId);

    int insert(PsTzDxmbshliTbl record);

    int insertSelective(PsTzDxmbshliTbl record);

    PsTzDxmbshliTbl selectByPrimaryKey(String tzEmlSmsTaskId);

    int updateByPrimaryKeySelective(PsTzDxmbshliTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzDxmbshliTbl record);
}