package com.tranzvision.gd.TZPaymentBundle.dao;

import com.tranzvision.gd.TZPaymentBundle.model.PsPrjPayT;

public interface PsPrjPayTMapper {
    int deleteByPrimaryKey(String tzPayPrjId);

    int insert(PsPrjPayT record);

    int insertSelective(PsPrjPayT record);

    PsPrjPayT selectByPrimaryKey(String tzPayPrjId);

    int updateByPrimaryKeySelective(PsPrjPayT record);

    int updateByPrimaryKey(PsPrjPayT record);
}