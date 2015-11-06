package com.tranzvision.gd.TZOrganizationMgBundle.dao;

import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgLoginbjT;

public interface PsTzJgLoginbjTMapper {
    int deleteByPrimaryKey(String tzAttachsysfilena);

    int insert(PsTzJgLoginbjT record);

    int insertSelective(PsTzJgLoginbjT record);

    PsTzJgLoginbjT selectByPrimaryKey(String tzAttachsysfilena);

    int updateByPrimaryKeySelective(PsTzJgLoginbjT record);

    int updateByPrimaryKey(PsTzJgLoginbjT record);
}