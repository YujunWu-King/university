package com.tranzvision.gd.TZMessageSetMgBundle.dao;

import com.tranzvision.gd.TZMessageSetMgBundle.model.PsTzPtXxjhTbl;

public interface PsTzPtXxjhTblMapper {
    int deleteByPrimaryKey(String tzXxjhId);

    int insert(PsTzPtXxjhTbl record);

    int insertSelective(PsTzPtXxjhTbl record);

    PsTzPtXxjhTbl selectByPrimaryKey(String tzXxjhId);

    int updateByPrimaryKeySelective(PsTzPtXxjhTbl record);

    int updateByPrimaryKey(PsTzPtXxjhTbl record);
}