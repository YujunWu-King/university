package com.tranzvision.gd.TZOrganizationMgBundle.dao;

import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgMgrTKey;

public interface PsTzJgMgrTMapper {
    int deleteByPrimaryKey(PsTzJgMgrTKey key);

    int insert(PsTzJgMgrTKey record);

    int insertSelective(PsTzJgMgrTKey record);
}