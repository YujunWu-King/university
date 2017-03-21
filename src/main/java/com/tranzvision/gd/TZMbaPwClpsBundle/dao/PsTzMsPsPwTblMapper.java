package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsPwTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsPwTblKey;

public interface PsTzMsPsPwTblMapper {
    int deleteByPrimaryKey(PsTzMsPsPwTblKey key);

    int insert(PsTzMsPsPwTbl record);

    int insertSelective(PsTzMsPsPwTbl record);

    PsTzMsPsPwTbl selectByPrimaryKey(PsTzMsPsPwTblKey key);

    int updateByPrimaryKeySelective(PsTzMsPsPwTbl record);

    int updateByPrimaryKey(PsTzMsPsPwTbl record);
}