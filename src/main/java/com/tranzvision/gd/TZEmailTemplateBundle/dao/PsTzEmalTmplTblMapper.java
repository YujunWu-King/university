package com.tranzvision.gd.TZEmailTemplateBundle.dao;

import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzEmalTmplTbl;
import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzEmalTmplTblKey;

public interface PsTzEmalTmplTblMapper {
    int deleteByPrimaryKey(PsTzEmalTmplTblKey key);

    int insert(PsTzEmalTmplTbl record);

    int insertSelective(PsTzEmalTmplTbl record);

    PsTzEmalTmplTbl selectByPrimaryKey(PsTzEmalTmplTblKey key);

    int updateByPrimaryKeySelective(PsTzEmalTmplTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzEmalTmplTbl record);

    int updateByPrimaryKey(PsTzEmalTmplTbl record);
}