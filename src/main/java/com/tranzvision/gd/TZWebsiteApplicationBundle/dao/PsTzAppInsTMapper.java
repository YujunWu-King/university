package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;

public interface PsTzAppInsTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzAppInsT record);

    int insertSelective(PsTzAppInsT record);

    PsTzAppInsT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzAppInsT record);

    int updateByPrimaryKeyWithBLOBs(PsTzAppInsT record);

    int updateByPrimaryKey(PsTzAppInsT record);
}