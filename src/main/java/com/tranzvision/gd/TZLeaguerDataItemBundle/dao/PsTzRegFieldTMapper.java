package com.tranzvision.gd.TZLeaguerDataItemBundle.dao;

import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzRegFieldT;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzRegFieldTKey;

public interface PsTzRegFieldTMapper {
    int deleteByPrimaryKey(PsTzRegFieldTKey key);

    int insert(PsTzRegFieldT record);

    int insertSelective(PsTzRegFieldT record);

    PsTzRegFieldT selectByPrimaryKey(PsTzRegFieldTKey key);

    int updateByPrimaryKeySelective(PsTzRegFieldT record);

    int updateByPrimaryKey(PsTzRegFieldT record);
}