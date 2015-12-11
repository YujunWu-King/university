package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTblKey;

public interface PsTzLxfsInfoTblMapper {
    int deleteByPrimaryKey(PsTzLxfsInfoTblKey key);

    int insert(PsTzLxfsInfoTbl record);

    int insertSelective(PsTzLxfsInfoTbl record);

    PsTzLxfsInfoTbl selectByPrimaryKey(PsTzLxfsInfoTblKey key);

    int updateByPrimaryKeySelective(PsTzLxfsInfoTbl record);

    int updateByPrimaryKey(PsTzLxfsInfoTbl record);
}