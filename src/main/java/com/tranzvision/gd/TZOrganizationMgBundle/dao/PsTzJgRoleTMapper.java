package com.tranzvision.gd.TZOrganizationMgBundle.dao;

import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgRoleT;
import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgRoleTKey;

public interface PsTzJgRoleTMapper {
    int deleteByPrimaryKey(PsTzJgRoleTKey key);

    int insert(PsTzJgRoleT record);

    int insertSelective(PsTzJgRoleT record);

    PsTzJgRoleT selectByPrimaryKey(PsTzJgRoleTKey key);

    int updateByPrimaryKeySelective(PsTzJgRoleT record);

    int updateByPrimaryKey(PsTzJgRoleT record);
}