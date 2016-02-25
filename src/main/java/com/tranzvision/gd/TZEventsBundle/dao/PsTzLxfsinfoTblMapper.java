package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzLxfsinfoTbl;
import com.tranzvision.gd.TZEventsBundle.model.PsTzLxfsinfoTblKey;

public interface PsTzLxfsinfoTblMapper {
    int deleteByPrimaryKey(PsTzLxfsinfoTblKey key);

    int insert(PsTzLxfsinfoTbl record);

    int insertSelective(PsTzLxfsinfoTbl record);

    PsTzLxfsinfoTbl selectByPrimaryKey(PsTzLxfsinfoTblKey key);

    int updateByPrimaryKeySelective(PsTzLxfsinfoTbl record);

    int updateByPrimaryKey(PsTzLxfsinfoTbl record);
}