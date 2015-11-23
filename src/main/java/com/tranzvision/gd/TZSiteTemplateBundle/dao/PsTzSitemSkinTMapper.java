package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemSkinTKey;

public interface PsTzSitemSkinTMapper {
    int deleteByPrimaryKey(PsTzSitemSkinTKey key);

    int insert(PsTzSitemSkinT record);

    int insertSelective(PsTzSitemSkinT record);

    PsTzSitemSkinT selectByPrimaryKey(PsTzSitemSkinTKey key);

    int updateByPrimaryKeySelective(PsTzSitemSkinT record);

    int updateByPrimaryKeyWithBLOBs(PsTzSitemSkinT record);

    int updateByPrimaryKey(PsTzSitemSkinT record);
}