package com.tranzvision.gd.TZEmailTemplateBundle.dao;

import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzEmalTmpTbl;
import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzEmalTmpTblKey;

public interface PsTzEmalTmpTblMapper {
    int deleteByPrimaryKey(PsTzEmalTmpTblKey key);

    int insert(PsTzEmalTmpTbl record);

    int insertSelective(PsTzEmalTmpTbl record);

    PsTzEmalTmpTbl selectByPrimaryKey(PsTzEmalTmpTblKey key);

    int updateByPrimaryKeySelective(PsTzEmalTmpTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzEmalTmpTbl record);

    int updateByPrimaryKey(PsTzEmalTmpTbl record);
}