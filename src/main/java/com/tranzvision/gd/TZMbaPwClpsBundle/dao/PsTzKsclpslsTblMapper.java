package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTblKey;

public interface PsTzKsclpslsTblMapper {
    int deleteByPrimaryKey(PsTzKsclpslsTblKey key);

    int insert(PsTzKsclpslsTbl record);

    int insertSelective(PsTzKsclpslsTbl record);

    PsTzKsclpslsTbl selectByPrimaryKey(PsTzKsclpslsTblKey key);

    int updateByPrimaryKeySelective(PsTzKsclpslsTbl record);

    int updateByPrimaryKey(PsTzKsclpslsTbl record);
}