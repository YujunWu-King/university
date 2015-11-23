package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemImgT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemImgTKey;

public interface PsTzSitemImgTMapper {
    int deleteByPrimaryKey(PsTzSitemImgTKey key);

    int insert(PsTzSitemImgT record);

    int insertSelective(PsTzSitemImgT record);

    PsTzSitemImgT selectByPrimaryKey(PsTzSitemImgTKey key);

    int updateByPrimaryKeySelective(PsTzSitemImgT record);

    int updateByPrimaryKey(PsTzSitemImgT record);
}