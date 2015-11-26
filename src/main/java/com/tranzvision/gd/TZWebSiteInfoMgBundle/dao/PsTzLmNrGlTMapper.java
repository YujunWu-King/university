package com.tranzvision.gd.TZWebSiteInfoMgBundle.dao;

import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;

public interface PsTzLmNrGlTMapper {
    int deleteByPrimaryKey(PsTzLmNrGlTKey key);

    int insert(PsTzLmNrGlTWithBLOBs record);

    int insertSelective(PsTzLmNrGlTWithBLOBs record);

    PsTzLmNrGlTWithBLOBs selectByPrimaryKey(PsTzLmNrGlTKey key);

    int updateByPrimaryKeySelective(PsTzLmNrGlTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzLmNrGlTWithBLOBs record);

    int updateByPrimaryKey(PsTzLmNrGlT record);
}