package com.tranzvision.gd.TZSitePageBundle.dao;

import com.tranzvision.gd.TZSitePageBundle.model.PsSiteBmClassSzT;

public interface PsSiteBmClassSzTMapper {
    int deleteByPrimaryKey(String tzSiteiId);

    int insert(PsSiteBmClassSzT record);

    int insertSelective(PsSiteBmClassSzT record);

    PsSiteBmClassSzT selectByPrimaryKey(String tzSiteiId);

    int updateByPrimaryKeySelective(PsSiteBmClassSzT record);

    int updateByPrimaryKey(PsSiteBmClassSzT record);
}