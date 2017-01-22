package com.tranzvision.gd.TZScoolLiblistManage.dao;

import com.tranzvision.gd.TZScoolLiblistManage.model.PsTzPayLogT;

public interface PsTzPayLogTMapper {
    int deleteByPrimaryKey(String tzJgId);

    int insert(PsTzPayLogT record);

    int insertSelective(PsTzPayLogT record);

    PsTzPayLogT selectByPrimaryKey(String tzJgId);

    int updateByPrimaryKeySelective(PsTzPayLogT record);

    int updateByPrimaryKey(PsTzPayLogT record);
}