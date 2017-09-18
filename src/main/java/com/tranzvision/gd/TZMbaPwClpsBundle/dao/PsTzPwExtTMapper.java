package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwExtT;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwExtTKey;

public interface PsTzPwExtTMapper {
    int deleteByPrimaryKey(PsTzPwExtTKey key);

    int insert(PsTzPwExtT record);

    int insertSelective(PsTzPwExtT record);

    PsTzPwExtT selectByPrimaryKey(PsTzPwExtTKey key);

    int updateByPrimaryKeySelective(PsTzPwExtT record);

    int updateByPrimaryKey(PsTzPwExtT record);
}