package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzMalBcAddT;

public interface PsTzMalBcAddTMapper {
    int deleteByPrimaryKey(String tzEmlSmsTaskId);

    int insert(PsTzMalBcAddT record);

    int insertSelective(PsTzMalBcAddT record);

    PsTzMalBcAddT selectByPrimaryKey(String tzEmlSmsTaskId);

    int updateByPrimaryKeySelective(PsTzMalBcAddT record);

    int updateByPrimaryKeyWithBLOBs(PsTzMalBcAddT record);
}