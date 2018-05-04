package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTblKey;

public interface PsTzMsPskshTblMapper {
    int deleteByPrimaryKey(PsTzMsPskshTblKey key);

    int insert(PsTzMsPskshTbl record);

    int insertSelective(PsTzMsPskshTbl record);

    PsTzMsPskshTbl selectByPrimaryKey(PsTzMsPskshTblKey key);

    int updateByPrimaryKeySelective(PsTzMsPskshTbl record);

    int updateByPrimaryKey(PsTzMsPskshTbl record);
}