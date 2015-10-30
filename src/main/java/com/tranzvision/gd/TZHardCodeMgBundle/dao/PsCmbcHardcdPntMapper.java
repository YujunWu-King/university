package com.tranzvision.gd.TZHardCodeMgBundle.dao;

import com.tranzvision.gd.TZHardCodeMgBundle.model.PsCmbcHardcdPnt;

public interface PsCmbcHardcdPntMapper {
    int deleteByPrimaryKey(String cmbcHardcodePnt);

    int insert(PsCmbcHardcdPnt record);

    int insertSelective(PsCmbcHardcdPnt record);

    PsCmbcHardcdPnt selectByPrimaryKey(String cmbcHardcodePnt);

    int updateByPrimaryKeySelective(PsCmbcHardcdPnt record);

    int updateByPrimaryKeyWithBLOBs(PsCmbcHardcdPnt record);

    int updateByPrimaryKey(PsCmbcHardcdPnt record);
}