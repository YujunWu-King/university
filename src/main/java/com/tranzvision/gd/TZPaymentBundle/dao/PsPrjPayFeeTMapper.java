package com.tranzvision.gd.TZPaymentBundle.dao;

import com.tranzvision.gd.TZPaymentBundle.model.PsPrjPayFeeT;
import com.tranzvision.gd.TZPaymentBundle.model.PsPrjPayFeeTKey;

public interface PsPrjPayFeeTMapper {
    int deleteByPrimaryKey(PsPrjPayFeeTKey key);

    int insert(PsPrjPayFeeT record);

    int insertSelective(PsPrjPayFeeT record);

    PsPrjPayFeeT selectByPrimaryKey(PsPrjPayFeeTKey key);

    int updateByPrimaryKeySelective(PsPrjPayFeeT record);

    int updateByPrimaryKey(PsPrjPayFeeT record);
}