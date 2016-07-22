package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPrjTKey;

public interface PsTzArtPrjTMapper {
    int deleteByPrimaryKey(PsTzArtPrjTKey key);

    int insert(PsTzArtPrjTKey record);

    int insertSelective(PsTzArtPrjTKey record);
}