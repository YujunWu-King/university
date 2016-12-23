package com.tranzvision.gd.TZPaymentBundle.dao;

import com.tranzvision.gd.TZPaymentBundle.model.PsTzPaymentAccountT;

public interface PsTzPaymentAccountTMapper {
    int deleteByPrimaryKey(String tzAccountId);

    int insert(PsTzPaymentAccountT record);

    int insertSelective(PsTzPaymentAccountT record);

    PsTzPaymentAccountT selectByPrimaryKey(String tzAccountId);

    int updateByPrimaryKeySelective(PsTzPaymentAccountT record);

    int updateByPrimaryKey(PsTzPaymentAccountT record);
}