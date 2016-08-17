package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzArtTypeT;

public interface PsTzArtTypeTMapper {
    int deleteByPrimaryKey(String tzArtTypeId);

    int insert(PsTzArtTypeT record);

    int insertSelective(PsTzArtTypeT record);

    PsTzArtTypeT selectByPrimaryKey(String tzArtTypeId);

    int updateByPrimaryKeySelective(PsTzArtTypeT record);

    int updateByPrimaryKey(PsTzArtTypeT record);
}