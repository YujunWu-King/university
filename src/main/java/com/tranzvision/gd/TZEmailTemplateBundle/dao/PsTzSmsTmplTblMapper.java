package com.tranzvision.gd.TZEmailTemplateBundle.dao;

import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzSmsTmplTbl;
import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzSmsTmplTblKey;

public interface PsTzSmsTmplTblMapper {
    int deleteByPrimaryKey(PsTzSmsTmplTblKey key);

    int insert(PsTzSmsTmplTbl record);

    int insertSelective(PsTzSmsTmplTbl record);

    PsTzSmsTmplTbl selectByPrimaryKey(PsTzSmsTmplTblKey key);

    int updateByPrimaryKeySelective(PsTzSmsTmplTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzSmsTmplTbl record);

    int updateByPrimaryKey(PsTzSmsTmplTbl record);
}