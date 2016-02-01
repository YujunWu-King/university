package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCompTbl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCompTblKey;

public interface PsTzAppCompTblMapper {
    int deleteByPrimaryKey(PsTzAppCompTblKey key);

    int insert(PsTzAppCompTbl record);

    int insertSelective(PsTzAppCompTbl record);

    PsTzAppCompTbl selectByPrimaryKey(PsTzAppCompTblKey key);

    int updateByPrimaryKeySelective(PsTzAppCompTbl record);

    int updateByPrimaryKey(PsTzAppCompTbl record);
}