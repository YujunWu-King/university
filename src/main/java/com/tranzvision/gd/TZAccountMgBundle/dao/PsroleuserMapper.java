package com.tranzvision.gd.TZAccountMgBundle.dao;

import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZAccountMgBundle.model.PsroleuserKey;

public interface PsroleuserMapper {
    int deleteByPrimaryKey(PsroleuserKey key);

    int insert(Psroleuser record);

    int insertSelective(Psroleuser record);

    Psroleuser selectByPrimaryKey(PsroleuserKey key);

    int updateByPrimaryKeySelective(Psroleuser record);

    int updateByPrimaryKey(Psroleuser record);
}