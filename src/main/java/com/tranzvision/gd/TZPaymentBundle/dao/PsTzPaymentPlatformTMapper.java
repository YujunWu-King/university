package com.tranzvision.gd.TZPaymentBundle.dao;

import com.tranzvision.gd.TZPaymentBundle.model.PsTzPaymentPlatformT;

public interface PsTzPaymentPlatformTMapper {
    int deleteByPrimaryKey(String tzPlatformId);

    int insert(PsTzPaymentPlatformT record);

    int insertSelective(PsTzPaymentPlatformT record);

    PsTzPaymentPlatformT selectByPrimaryKey(String tzPlatformId);

    int updateByPrimaryKeySelective(PsTzPaymentPlatformT record);

    int updateByPrimaryKey(PsTzPaymentPlatformT record);
}