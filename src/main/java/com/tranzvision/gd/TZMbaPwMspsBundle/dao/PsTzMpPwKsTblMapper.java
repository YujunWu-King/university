package com.tranzvision.gd.TZMbaPwMspsBundle.dao;

import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTblKey;

public interface PsTzMpPwKsTblMapper {
    int deleteByPrimaryKey(PsTzMpPwKsTblKey key);

    int insert(PsTzMpPwKsTbl record);

    int insertSelective(PsTzMpPwKsTbl record);

    PsTzMpPwKsTbl selectByPrimaryKey(PsTzMpPwKsTblKey key);

    int updateByPrimaryKeySelective(PsTzMpPwKsTbl record);

    int updateByPrimaryKey(PsTzMpPwKsTbl record);
}