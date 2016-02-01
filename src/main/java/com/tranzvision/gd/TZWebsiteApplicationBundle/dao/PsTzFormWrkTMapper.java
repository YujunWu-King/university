package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkTKey;

public interface PsTzFormWrkTMapper {
    int deleteByPrimaryKey(PsTzFormWrkTKey key);

    int insert(PsTzFormWrkT record);

    int insertSelective(PsTzFormWrkT record);

    PsTzFormWrkT selectByPrimaryKey(PsTzFormWrkTKey key);

    int updateByPrimaryKeySelective(PsTzFormWrkT record);

    int updateByPrimaryKeyWithBLOBs(PsTzFormWrkT record);

    int updateByPrimaryKey(PsTzFormWrkT record);
}