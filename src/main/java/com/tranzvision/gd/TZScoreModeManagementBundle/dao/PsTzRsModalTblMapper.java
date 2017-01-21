package com.tranzvision.gd.TZScoreModeManagementBundle.dao;

import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTblKey;

public interface PsTzRsModalTblMapper {
    int deleteByPrimaryKey(PsTzRsModalTblKey key);

    int insert(PsTzRsModalTbl record);

    int insertSelective(PsTzRsModalTbl record);

    PsTzRsModalTbl selectByPrimaryKey(PsTzRsModalTblKey key);

    int updateByPrimaryKeySelective(PsTzRsModalTbl record);

    int updateByPrimaryKey(PsTzRsModalTbl record);
}