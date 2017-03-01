package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTblKey;

public interface PsTzClpsGzTblMapper {
    int deleteByPrimaryKey(PsTzClpsGzTblKey key);

    int insert(PsTzClpsGzTbl record);

    int insertSelective(PsTzClpsGzTbl record);

    PsTzClpsGzTbl selectByPrimaryKey(PsTzClpsGzTblKey key);

    int updateByPrimaryKeySelective(PsTzClpsGzTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzClpsGzTbl record);

    int updateByPrimaryKey(PsTzClpsGzTbl record);
}