package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzLxfInfoTbl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzLxfInfoTblKey;

public interface PsTzLxfInfoTblMapper {
    int deleteByPrimaryKey(PsTzLxfInfoTblKey key);

    int insert(PsTzLxfInfoTbl record);

    int insertSelective(PsTzLxfInfoTbl record);

    PsTzLxfInfoTbl selectByPrimaryKey(PsTzLxfInfoTblKey key);

    int updateByPrimaryKeySelective(PsTzLxfInfoTbl record);

    int updateByPrimaryKey(PsTzLxfInfoTbl record);
}