package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxMsgT;

public interface PsTzZnxMsgTMapper {
    int deleteByPrimaryKey(String tzZnxMsgid);

    int insert(PsTzZnxMsgT record);

    int insertSelective(PsTzZnxMsgT record);

    PsTzZnxMsgT selectByPrimaryKey(String tzZnxMsgid);

    int updateByPrimaryKeySelective(PsTzZnxMsgT record);

    int updateByPrimaryKey(PsTzZnxMsgT record);
}