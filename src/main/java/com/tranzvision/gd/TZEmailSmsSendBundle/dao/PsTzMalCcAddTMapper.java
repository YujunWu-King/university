package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzMalCcAddT;

public interface PsTzMalCcAddTMapper {
    int deleteByPrimaryKey(String tzEmlSmsTaskId);

    int insert(PsTzMalCcAddT record);

    int insertSelective(PsTzMalCcAddT record);

    PsTzMalCcAddT selectByPrimaryKey(String tzEmlSmsTaskId);

    int updateByPrimaryKeySelective(PsTzMalCcAddT record);

    int updateByPrimaryKeyWithBLOBs(PsTzMalCcAddT record);
}