package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjQfSjrT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxYjQfSjrTKey;

public interface PsTzDxYjQfSjrTMapper {
    int deleteByPrimaryKey(PsTzDxYjQfSjrTKey key);

    int insert(PsTzDxYjQfSjrT record);

    int insertSelective(PsTzDxYjQfSjrT record);

    PsTzDxYjQfSjrT selectByPrimaryKey(PsTzDxYjQfSjrTKey key);

    int updateByPrimaryKeySelective(PsTzDxYjQfSjrT record);

    int updateByPrimaryKey(PsTzDxYjQfSjrT record);
}