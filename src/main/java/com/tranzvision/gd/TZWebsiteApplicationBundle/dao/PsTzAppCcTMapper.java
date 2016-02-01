package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcTKey;

public interface PsTzAppCcTMapper {
    int deleteByPrimaryKey(PsTzAppCcTKey key);

    int insert(PsTzAppCcT record);

    int insertSelective(PsTzAppCcT record);

    PsTzAppCcT selectByPrimaryKey(PsTzAppCcTKey key);

    int updateByPrimaryKeySelective(PsTzAppCcT record);

    int updateByPrimaryKeyWithBLOBs(PsTzAppCcT record);

    int updateByPrimaryKey(PsTzAppCcT record);
}