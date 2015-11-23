package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemDefnT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemDefnTWithBLOBs;

public interface PsTzSitemDefnTMapper {
    int deleteByPrimaryKey(String tzSitemId);

    int insert(PsTzSitemDefnTWithBLOBs record);

    int insertSelective(PsTzSitemDefnTWithBLOBs record);

    PsTzSitemDefnTWithBLOBs selectByPrimaryKey(String tzSitemId);

    int updateByPrimaryKeySelective(PsTzSitemDefnTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzSitemDefnTWithBLOBs record);

    int updateByPrimaryKey(PsTzSitemDefnT record);
}