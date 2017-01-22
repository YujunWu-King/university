package com.tranzvision.gd.TZScoreModeManagementBundle.dao;

import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblWithBLOBs;

public interface PsTzModalDtTblMapper {
    int deleteByPrimaryKey(PsTzModalDtTblKey key);

    int insert(PsTzModalDtTblWithBLOBs record);

    int insertSelective(PsTzModalDtTblWithBLOBs record);

    PsTzModalDtTblWithBLOBs selectByPrimaryKey(PsTzModalDtTblKey key);

    int updateByPrimaryKeySelective(PsTzModalDtTblWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzModalDtTblWithBLOBs record);

    int updateByPrimaryKey(PsTzModalDtTbl record);
}