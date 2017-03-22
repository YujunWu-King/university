package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsksTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsksTblKey;

public interface PsTzMsPsksTblMapper {
    int deleteByPrimaryKey(PsTzMsPsksTblKey key);

    int insert(PsTzMsPsksTbl record);

    int insertSelective(PsTzMsPsksTbl record);

    PsTzMsPsksTbl selectByPrimaryKey(PsTzMsPsksTblKey key);

    int updateByPrimaryKeySelective(PsTzMsPsksTbl record);

    int updateByPrimaryKey(PsTzMsPsksTbl record);
}