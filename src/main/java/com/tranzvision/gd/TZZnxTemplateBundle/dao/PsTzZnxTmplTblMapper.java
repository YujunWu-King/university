package com.tranzvision.gd.TZZnxTemplateBundle.dao;

import com.tranzvision.gd.TZZnxTemplateBundle.model.PsTzZnxTmplTbl;
import com.tranzvision.gd.TZZnxTemplateBundle.model.PsTzZnxTmplTblKey;

public interface PsTzZnxTmplTblMapper {
    int deleteByPrimaryKey(PsTzZnxTmplTblKey key);

    int insert(PsTzZnxTmplTbl record);

    int insertSelective(PsTzZnxTmplTbl record);

    PsTzZnxTmplTbl selectByPrimaryKey(PsTzZnxTmplTblKey key);

    int updateByPrimaryKeySelective(PsTzZnxTmplTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzZnxTmplTbl record);

    int updateByPrimaryKey(PsTzZnxTmplTbl record);
}