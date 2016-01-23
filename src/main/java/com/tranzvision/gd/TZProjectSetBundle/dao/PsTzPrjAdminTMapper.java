package com.tranzvision.gd.TZProjectSetBundle.dao;

import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjAdminTKey;

public interface PsTzPrjAdminTMapper {
    int deleteByPrimaryKey(PsTzPrjAdminTKey key);

    int insert(PsTzPrjAdminTKey record);

    int insertSelective(PsTzPrjAdminTKey record);
}