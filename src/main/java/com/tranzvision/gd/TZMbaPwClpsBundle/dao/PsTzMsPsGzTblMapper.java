package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsGzTblKey;

public interface PsTzMsPsGzTblMapper {
    int deleteByPrimaryKey(PsTzMsPsGzTblKey key);

    int insert(PsTzMsPsGzTbl record);

    int insertSelective(PsTzMsPsGzTbl record);

    PsTzMsPsGzTbl selectByPrimaryKey(PsTzMsPsGzTblKey key);

    int updateByPrimaryKeySelective(PsTzMsPsGzTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzMsPsGzTbl record);

    int updateByPrimaryKey(PsTzMsPsGzTbl record);
}