package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTblKey;

public interface PsTzClpskspwTblMapper {
    int deleteByPrimaryKey(PsTzClpskspwTblKey key);

    int insert(PsTzClpskspwTbl record);

    int insertSelective(PsTzClpskspwTbl record);

    PsTzClpskspwTbl selectByPrimaryKey(PsTzClpskspwTblKey key);

    int updateByPrimaryKeySelective(PsTzClpskspwTbl record);

    int updateByPrimaryKey(PsTzClpskspwTbl record);
}