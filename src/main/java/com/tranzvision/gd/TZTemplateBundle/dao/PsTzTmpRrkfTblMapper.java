package com.tranzvision.gd.TZTemplateBundle.dao;

import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpRrkfTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpRrkfTblKey;

public interface PsTzTmpRrkfTblMapper {
    int deleteByPrimaryKey(PsTzTmpRrkfTblKey key);

    int insert(PsTzTmpRrkfTbl record);

    int insertSelective(PsTzTmpRrkfTbl record);

    PsTzTmpRrkfTbl selectByPrimaryKey(PsTzTmpRrkfTblKey key);

    int updateByPrimaryKeySelective(PsTzTmpRrkfTbl record);

    int updateByPrimaryKey(PsTzTmpRrkfTbl record);
}