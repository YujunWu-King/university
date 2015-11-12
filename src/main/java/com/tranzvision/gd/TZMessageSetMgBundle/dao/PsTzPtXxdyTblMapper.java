package com.tranzvision.gd.TZMessageSetMgBundle.dao;

import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTbl;
import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxdyTblKey;

public interface PsTzPtXxdyTblMapper {
    int deleteByPrimaryKey(PsTzPtXxdyTblKey key);

    int insert(PsTzPtXxdyTbl record);

    int insertSelective(PsTzPtXxdyTbl record);

    PsTzPtXxdyTbl selectByPrimaryKey(PsTzPtXxdyTblKey key);

    int updateByPrimaryKeySelective(PsTzPtXxdyTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzPtXxdyTbl record);

    int updateByPrimaryKey(PsTzPtXxdyTbl record);
}