package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMtypT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMtypTKey;

public interface PsTzSitemMtypTMapper {
    int deleteByPrimaryKey(PsTzSitemMtypTKey key);

    int insert(PsTzSitemMtypT record);

    int insertSelective(PsTzSitemMtypT record);

    PsTzSitemMtypT selectByPrimaryKey(PsTzSitemMtypTKey key);

    int updateByPrimaryKeySelective(PsTzSitemMtypT record);

    int updateByPrimaryKeyWithBLOBs(PsTzSitemMtypT record);

    int updateByPrimaryKey(PsTzSitemMtypT record);
}