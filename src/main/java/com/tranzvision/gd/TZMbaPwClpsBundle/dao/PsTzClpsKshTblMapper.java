package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTblKey;

public interface PsTzClpsKshTblMapper {
    int deleteByPrimaryKey(PsTzClpsKshTblKey key);

    int insert(PsTzClpsKshTbl record);

    int insertSelective(PsTzClpsKshTbl record);

    PsTzClpsKshTbl selectByPrimaryKey(PsTzClpsKshTblKey key);

    int updateByPrimaryKeySelective(PsTzClpsKshTbl record);

    int updateByPrimaryKey(PsTzClpsKshTbl record);
}