package com.tranzvision.gd.TZAccountMgBundle.dao;

import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;

public interface PsoprdefnMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(Psoprdefn record);

    int insertSelective(Psoprdefn record);

    Psoprdefn selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(Psoprdefn record);

    int updateByPrimaryKey(Psoprdefn record);
}