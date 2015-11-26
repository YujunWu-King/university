package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPicT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPicTKey;

public interface PsTzArtPicTMapper {
    int deleteByPrimaryKey(PsTzArtPicTKey key);

    int insert(PsTzArtPicT record);

    int insertSelective(PsTzArtPicT record);

    PsTzArtPicT selectByPrimaryKey(PsTzArtPicTKey key);

    int updateByPrimaryKeySelective(PsTzArtPicT record);

    int updateByPrimaryKeyWithBLOBs(PsTzArtPicT record);

    int updateByPrimaryKey(PsTzArtPicT record);
}