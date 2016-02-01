package com.tranzvision.gd.TZAdditionalFieldBundle.dao;

import com.tranzvision.gd.TZAdditionalFieldBundle.model.PsTzFAttrOptT;
import com.tranzvision.gd.TZAdditionalFieldBundle.model.PsTzFAttrOptTKey;

public interface PsTzFAttrOptTMapper {
    int deleteByPrimaryKey(PsTzFAttrOptTKey key);

    int insert(PsTzFAttrOptT record);

    int insertSelective(PsTzFAttrOptT record);

    PsTzFAttrOptT selectByPrimaryKey(PsTzFAttrOptTKey key);

    int updateByPrimaryKeySelective(PsTzFAttrOptT record);

    int updateByPrimaryKey(PsTzFAttrOptT record);
}