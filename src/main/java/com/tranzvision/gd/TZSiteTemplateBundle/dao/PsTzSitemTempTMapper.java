package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemTempT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemTempTKey;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemTempTWithBLOBs;

public interface PsTzSitemTempTMapper {
    int deleteByPrimaryKey(PsTzSitemTempTKey key);

    int insert(PsTzSitemTempTWithBLOBs record);

    int insertSelective(PsTzSitemTempTWithBLOBs record);

    PsTzSitemTempTWithBLOBs selectByPrimaryKey(PsTzSitemTempTKey key);

    int updateByPrimaryKeySelective(PsTzSitemTempTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzSitemTempTWithBLOBs record);

    int updateByPrimaryKey(PsTzSitemTempT record);
}