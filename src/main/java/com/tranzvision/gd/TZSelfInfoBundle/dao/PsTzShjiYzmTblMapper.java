package com.tranzvision.gd.TZSelfInfoBundle.dao;

import com.tranzvision.gd.TZSelfInfoBundle.model.PsTzShjiYzmTbl;
import com.tranzvision.gd.TZSelfInfoBundle.model.PsTzShjiYzmTblKey;

public interface PsTzShjiYzmTblMapper {
    int deleteByPrimaryKey(PsTzShjiYzmTblKey key);

    int insert(PsTzShjiYzmTbl record);

    int insertSelective(PsTzShjiYzmTbl record);

    PsTzShjiYzmTbl selectByPrimaryKey(PsTzShjiYzmTblKey key);

    int updateByPrimaryKeySelective(PsTzShjiYzmTbl record);

    int updateByPrimaryKey(PsTzShjiYzmTbl record);
}