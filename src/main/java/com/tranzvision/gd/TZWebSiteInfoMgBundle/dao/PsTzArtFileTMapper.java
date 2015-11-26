package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtFileTKey;

public interface PsTzArtFileTMapper {
    int deleteByPrimaryKey(PsTzArtFileTKey key);

    int insert(PsTzArtFileTKey record);

    int insertSelective(PsTzArtFileTKey record);
}