package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormPhotoT;

public interface PsTzFormPhotoTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzFormPhotoT record);

    int insertSelective(PsTzFormPhotoT record);

    PsTzFormPhotoT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzFormPhotoT record);

    int updateByPrimaryKey(PsTzFormPhotoT record);
}