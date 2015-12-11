package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzOprPhotoT;

public interface PsTzOprPhotoTMapper {
    int deleteByPrimaryKey(String tzAttachsysfilena);

    int insert(PsTzOprPhotoT record);

    int insertSelective(PsTzOprPhotoT record);

    PsTzOprPhotoT selectByPrimaryKey(String tzAttachsysfilena);

    int updateByPrimaryKeySelective(PsTzOprPhotoT record);

    int updateByPrimaryKey(PsTzOprPhotoT record);
}