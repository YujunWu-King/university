package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzHdBmrdcAet;

public interface PsTzHdBmrdcAetMapper {
    int deleteByPrimaryKey(String runCntlId);

    int insert(PsTzHdBmrdcAet record);

    int insertSelective(PsTzHdBmrdcAet record);

    PsTzHdBmrdcAet selectByPrimaryKey(String runCntlId);

    int updateByPrimaryKeySelective(PsTzHdBmrdcAet record);

    int updateByPrimaryKeyWithBLOBs(PsTzHdBmrdcAet record);

    int updateByPrimaryKey(PsTzHdBmrdcAet record);
}