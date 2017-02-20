package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzCpPwKsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzCpPwKsTblKey;

public interface PsTzCpPwKsTblMapper {
    int deleteByPrimaryKey(PsTzCpPwKsTblKey key);

    int insert(PsTzCpPwKsTbl record);

    int insertSelective(PsTzCpPwKsTbl record);

    PsTzCpPwKsTbl selectByPrimaryKey(PsTzCpPwKsTblKey key);

    int updateByPrimaryKeySelective(PsTzCpPwKsTbl record);

    int updateByPrimaryKey(PsTzCpPwKsTbl record);
}