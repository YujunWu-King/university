package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwkspcTmpT;

public interface PsTzPwkspcTmpTMapper {
    int deleteByPrimaryKey(Integer tzId);

    int insert(PsTzPwkspcTmpT record);

    int insertSelective(PsTzPwkspcTmpT record);

    PsTzPwkspcTmpT selectByPrimaryKey(Integer tzId);

    int updateByPrimaryKeySelective(PsTzPwkspcTmpT record);

    int updateByPrimaryKey(PsTzPwkspcTmpT record);
}