package com.tranzvision.gd.TZLeaguerDataItemBundle.dao;

import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzTbl;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzTblKey;

public interface PsTzYhzcXxzTblMapper {
    int deleteByPrimaryKey(PsTzYhzcXxzTblKey key);

    int insert(PsTzYhzcXxzTbl record);

    int insertSelective(PsTzYhzcXxzTbl record);

    PsTzYhzcXxzTbl selectByPrimaryKey(PsTzYhzcXxzTblKey key);

    int updateByPrimaryKeySelective(PsTzYhzcXxzTbl record);

    int updateByPrimaryKey(PsTzYhzcXxzTbl record);
}