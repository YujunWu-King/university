package com.tranzvision.gd.TZClassIndividualizationBundle.dao;

import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzCAttrOptT;
import com.tranzvision.gd.TZClassIndividualizationBundle.model.PsTzCAttrOptTKey;

public interface PsTzCAttrOptTMapper {
    int deleteByPrimaryKey(PsTzCAttrOptTKey key);

    int insert(PsTzCAttrOptT record);

    int insertSelective(PsTzCAttrOptT record);

    PsTzCAttrOptT selectByPrimaryKey(PsTzCAttrOptTKey key);

    int updateByPrimaryKeySelective(PsTzCAttrOptT record);

    int updateByPrimaryKey(PsTzCAttrOptT record);
}