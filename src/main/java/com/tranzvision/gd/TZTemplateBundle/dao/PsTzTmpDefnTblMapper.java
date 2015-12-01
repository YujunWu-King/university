package com.tranzvision.gd.TZTemplateBundle.dao;

import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTblKey;

public interface PsTzTmpDefnTblMapper {
    int deleteByPrimaryKey(PsTzTmpDefnTblKey key);

    int insert(PsTzTmpDefnTbl record);

    int insertSelective(PsTzTmpDefnTbl record);

    PsTzTmpDefnTbl selectByPrimaryKey(PsTzTmpDefnTblKey key);

    int updateByPrimaryKeySelective(PsTzTmpDefnTbl record);

    int updateByPrimaryKey(PsTzTmpDefnTbl record);
}