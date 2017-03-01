package com.tranzvision.gd.TZMbaPwMspsBundle.dao;

import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMspskspwTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMspskspwTblKey;

public interface PsTzMspskspwTblMapper {
    int deleteByPrimaryKey(PsTzMspskspwTblKey key);

    int insert(PsTzMspskspwTbl record);

    int insertSelective(PsTzMspskspwTbl record);

    PsTzMspskspwTbl selectByPrimaryKey(PsTzMspskspwTblKey key);

    int updateByPrimaryKeySelective(PsTzMspskspwTbl record);

    int updateByPrimaryKey(PsTzMspskspwTbl record);
}