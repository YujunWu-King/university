package com.tranzvision.gd.TZPaymentBundle.dao;

import com.tranzvision.gd.TZPaymentBundle.model.PsTzPaymentInfoT;

public interface PsTzPaymentInfoTMapper {
    int deleteByPrimaryKey(String tzPayId);

    int insert(PsTzPaymentInfoT record);

    int insertSelective(PsTzPaymentInfoT record);

    PsTzPaymentInfoT selectByPrimaryKey(String tzPayId);

    int updateByPrimaryKeySelective(PsTzPaymentInfoT record);

    int updateByPrimaryKey(PsTzPaymentInfoT record);
}