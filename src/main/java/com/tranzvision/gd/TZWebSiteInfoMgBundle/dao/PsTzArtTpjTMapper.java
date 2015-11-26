package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTpjT;

public interface PsTzArtTpjTMapper {
    int deleteByPrimaryKey(String tzAttachsysfilena);

    int insert(PsTzArtTpjT record);

    int insertSelective(PsTzArtTpjT record);

    PsTzArtTpjT selectByPrimaryKey(String tzAttachsysfilena);

    int updateByPrimaryKeySelective(PsTzArtTpjT record);

    int updateByPrimaryKey(PsTzArtTpjT record);
}