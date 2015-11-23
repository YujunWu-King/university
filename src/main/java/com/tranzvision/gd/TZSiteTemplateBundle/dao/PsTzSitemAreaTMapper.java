package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemAreaT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemAreaTKey;

public interface PsTzSitemAreaTMapper {
    int deleteByPrimaryKey(PsTzSitemAreaTKey key);

    int insert(PsTzSitemAreaT record);

    int insertSelective(PsTzSitemAreaT record);

    PsTzSitemAreaT selectByPrimaryKey(PsTzSitemAreaTKey key);

    int updateByPrimaryKeySelective(PsTzSitemAreaT record);

    int updateByPrimaryKeyWithBLOBs(PsTzSitemAreaT record);

    int updateByPrimaryKey(PsTzSitemAreaT record);
}