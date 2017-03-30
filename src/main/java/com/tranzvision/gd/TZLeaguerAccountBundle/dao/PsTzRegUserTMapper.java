package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;

public interface PsTzRegUserTMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PsTzRegUserT record);

    int insertSelective(PsTzRegUserT record);

    PsTzRegUserT selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PsTzRegUserT record);

    int updateByPrimaryKey(PsTzRegUserT record);
}