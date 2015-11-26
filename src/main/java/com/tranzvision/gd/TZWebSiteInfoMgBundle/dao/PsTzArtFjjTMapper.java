package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtFjjT;

public interface PsTzArtFjjTMapper {
    int deleteByPrimaryKey(String tzAttachsysfilena);

    int insert(PsTzArtFjjT record);

    int insertSelective(PsTzArtFjjT record);

    PsTzArtFjjT selectByPrimaryKey(String tzAttachsysfilena);

    int updateByPrimaryKeySelective(PsTzArtFjjT record);

    int updateByPrimaryKey(PsTzArtFjjT record);
}