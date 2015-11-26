package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTitimgT;

public interface PsTzArtTitimgTMapper {
    int deleteByPrimaryKey(String tzAttachsysfilena);

    int insert(PsTzArtTitimgT record);

    int insertSelective(PsTzArtTitimgT record);

    PsTzArtTitimgT selectByPrimaryKey(String tzAttachsysfilena);

    int updateByPrimaryKeySelective(PsTzArtTitimgT record);

    int updateByPrimaryKey(PsTzArtTitimgT record);
}