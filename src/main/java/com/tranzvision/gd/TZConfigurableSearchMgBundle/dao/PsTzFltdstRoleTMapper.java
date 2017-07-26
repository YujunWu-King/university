package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltdstRoleT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltdstRoleTKey;

public interface PsTzFltdstRoleTMapper {
    int deleteByPrimaryKey(PsTzFltdstRoleTKey key);

    int insert(PsTzFltdstRoleT record);

    int insertSelective(PsTzFltdstRoleT record);

    PsTzFltdstRoleT selectByPrimaryKey(PsTzFltdstRoleTKey key);

    int updateByPrimaryKeySelective(PsTzFltdstRoleT record);

    int updateByPrimaryKey(PsTzFltdstRoleT record);
}