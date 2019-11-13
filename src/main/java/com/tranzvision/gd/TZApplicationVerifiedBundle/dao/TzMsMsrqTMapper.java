package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.TzMsMsrqT;

public interface TzMsMsrqTMapper {
    int deleteByPrimaryKey(String tzBmbId);

    int insert(TzMsMsrqT record);

    int insertSelective(TzMsMsrqT record);

    TzMsMsrqT selectByPrimaryKey(String tzBmbId);

    int updateByPrimaryKeySelective(TzMsMsrqT record);

    int updateByPrimaryKey(TzMsMsrqT record);
}