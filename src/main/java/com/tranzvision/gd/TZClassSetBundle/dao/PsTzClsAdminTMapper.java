package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsAdminTKey;

public interface PsTzClsAdminTMapper {
    int deleteByPrimaryKey(PsTzClsAdminTKey key);

    int insert(PsTzClsAdminTKey record);

    int insertSelective(PsTzClsAdminTKey record);
}